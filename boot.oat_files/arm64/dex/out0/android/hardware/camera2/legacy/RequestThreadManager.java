package android.hardware.camera2.legacy;

import android.graphics.SurfaceTexture;
import android.graphics.SurfaceTexture.OnFrameAvailableListener;
import android.hardware.Camera;
import android.hardware.Camera.ErrorCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.impl.CameraMetadataNative;
import android.hardware.camera2.legacy.LegacyExceptionUtils.BufferQueueAbandonedException;
import android.hardware.camera2.utils.LongParcelable;
import android.hardware.camera2.utils.SizeAreaComparator;
import android.os.ConditionVariable;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.util.MutableLong;
import android.util.Pair;
import android.util.Size;
import android.view.Surface;
import com.android.internal.util.Preconditions;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class RequestThreadManager {
    private static final float ASPECT_RATIO_TOLERANCE = 0.01f;
    private static final boolean DEBUG = false;
    private static final int JPEG_FRAME_TIMEOUT = 4000;
    private static final int MAX_IN_FLIGHT_REQUESTS = 2;
    private static final int MSG_CLEANUP = 3;
    private static final int MSG_CONFIGURE_OUTPUTS = 1;
    private static final int MSG_SUBMIT_CAPTURE_REQUEST = 2;
    private static final int PREVIEW_FRAME_TIMEOUT = 1000;
    private static final int REQUEST_COMPLETE_TIMEOUT = 4000;
    private static final boolean USE_BLOB_FORMAT_OVERRIDE = true;
    private static final boolean VERBOSE = false;
    private final String TAG;
    private final List<Surface> mCallbackOutputs = new ArrayList();
    private Camera mCamera;
    private final int mCameraId;
    private final CaptureCollector mCaptureCollector;
    private final CameraCharacteristics mCharacteristics;
    private final CameraDeviceState mDeviceState;
    private Surface mDummySurface;
    private SurfaceTexture mDummyTexture;
    private final ErrorCallback mErrorCallback = new ErrorCallback() {
        public void onError(int i, Camera camera) {
            switch (i) {
                case 2:
                    RequestThreadManager.this.flush();
                    RequestThreadManager.this.mDeviceState.setError(0);
                    return;
                default:
                    Log.e(RequestThreadManager.this.TAG, "Received error " + i + " from the Camera1 ErrorCallback");
                    RequestThreadManager.this.mDeviceState.setError(1);
                    return;
            }
        }
    };
    private final LegacyFaceDetectMapper mFaceDetectMapper;
    private final LegacyFocusStateMapper mFocusStateMapper;
    private GLThreadManager mGLThreadManager;
    private final Object mIdleLock = new Object();
    private Size mIntermediateBufferSize;
    private final PictureCallback mJpegCallback = new PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            Log.i(RequestThreadManager.this.TAG, "Received jpeg.");
            Pair<RequestHolder, Long> captureInfo = RequestThreadManager.this.mCaptureCollector.jpegProduced();
            if (captureInfo == null || captureInfo.first == null) {
                Log.e(RequestThreadManager.this.TAG, "Dropping jpeg frame.");
                return;
            }
            RequestHolder holder = captureInfo.first;
            long timestamp = ((Long) captureInfo.second).longValue();
            for (Surface s : holder.getHolderTargets()) {
                try {
                    if (LegacyCameraDevice.containsSurfaceId(s, RequestThreadManager.this.mJpegSurfaceIds)) {
                        Log.i(RequestThreadManager.this.TAG, "Producing jpeg buffer...");
                        int totalSize = ((data.length + LegacyCameraDevice.nativeGetJpegFooterSize()) + 3) & -4;
                        LegacyCameraDevice.setNextTimestamp(s, timestamp);
                        LegacyCameraDevice.setSurfaceFormat(s, 1);
                        int dimen = (((int) Math.ceil(Math.sqrt((double) totalSize))) + 15) & -16;
                        LegacyCameraDevice.setSurfaceDimens(s, dimen, dimen);
                        LegacyCameraDevice.produceFrame(s, data, dimen, dimen, 33);
                    }
                } catch (BufferQueueAbandonedException e) {
                    Log.w(RequestThreadManager.this.TAG, "Surface abandoned, dropping frame. ", e);
                }
            }
            RequestThreadManager.this.mReceivedJpeg.open();
        }
    };
    private final ShutterCallback mJpegShutterCallback = new ShutterCallback() {
        public void onShutter() {
            RequestThreadManager.this.mCaptureCollector.jpegCaptured(SystemClock.elapsedRealtimeNanos());
        }
    };
    private final List<Long> mJpegSurfaceIds = new ArrayList();
    private LegacyRequest mLastRequest = null;
    private Parameters mParams;
    private final FpsCounter mPrevCounter = new FpsCounter("Incoming Preview");
    private final OnFrameAvailableListener mPreviewCallback = new OnFrameAvailableListener() {
        public void onFrameAvailable(SurfaceTexture surfaceTexture) {
            RequestThreadManager.this.mGLThreadManager.queueNewFrame();
        }
    };
    private final List<Surface> mPreviewOutputs = new ArrayList();
    private boolean mPreviewRunning = false;
    private SurfaceTexture mPreviewTexture;
    private final AtomicBoolean mQuit = new AtomicBoolean(false);
    private final ConditionVariable mReceivedJpeg = new ConditionVariable(false);
    private final FpsCounter mRequestCounter = new FpsCounter("Incoming Requests");
    private final Callback mRequestHandlerCb = new Callback() {
        private boolean mCleanup = false;
        private final LegacyResultMapper mMapper = new LegacyResultMapper();

        public boolean handleMessage(Message msg) {
            if (this.mCleanup) {
                return true;
            }
            switch (msg.what) {
                case -1:
                    break;
                case 1:
                    ConfigureHolder config = msg.obj;
                    Log.i(RequestThreadManager.this.TAG, "Configure outputs: " + (config.surfaces != null ? config.surfaces.size() : 0) + " surfaces configured.");
                    try {
                        if (!RequestThreadManager.this.mCaptureCollector.waitForEmpty(4000, TimeUnit.MILLISECONDS)) {
                            Log.e(RequestThreadManager.this.TAG, "Timed out while queueing configure request.");
                            RequestThreadManager.this.mCaptureCollector.failAll();
                        }
                        RequestThreadManager.this.configureOutputs(config.surfaces);
                        config.condition.open();
                        break;
                    } catch (InterruptedException e) {
                        Log.e(RequestThreadManager.this.TAG, "Interrupted while waiting for requests to complete.");
                        RequestThreadManager.this.mDeviceState.setError(1);
                        break;
                    }
                case 2:
                    Handler handler = RequestThreadManager.this.mRequestThread.getHandler();
                    Pair<BurstHolder, Long> nextBurst = RequestThreadManager.this.mRequestQueue.getNext();
                    if (nextBurst == null) {
                        try {
                            if (!RequestThreadManager.this.mCaptureCollector.waitForEmpty(4000, TimeUnit.MILLISECONDS)) {
                                Log.e(RequestThreadManager.this.TAG, "Timed out while waiting for prior requests to complete.");
                                RequestThreadManager.this.mCaptureCollector.failAll();
                            }
                            synchronized (RequestThreadManager.this.mIdleLock) {
                                nextBurst = RequestThreadManager.this.mRequestQueue.getNext();
                                if (nextBurst == null) {
                                    RequestThreadManager.this.mDeviceState.setIdle();
                                    break;
                                }
                            }
                        } catch (InterruptedException e2) {
                            Log.e(RequestThreadManager.this.TAG, "Interrupted while waiting for requests to complete: ", e2);
                            RequestThreadManager.this.mDeviceState.setError(1);
                            break;
                        }
                    }
                    if (nextBurst != null) {
                        handler.sendEmptyMessage(2);
                    }
                    for (RequestHolder holder : ((BurstHolder) nextBurst.first).produceRequestHolders(((Long) nextBurst.second).longValue())) {
                        CaptureRequest request = holder.getRequest();
                        boolean paramsChanged = false;
                        if (RequestThreadManager.this.mLastRequest == null || RequestThreadManager.this.mLastRequest.captureRequest != request) {
                            LegacyRequest legacyRequest = new LegacyRequest(RequestThreadManager.this.mCharacteristics, request, ParameterUtils.convertSize(RequestThreadManager.this.mParams.getPreviewSize()), RequestThreadManager.this.mParams);
                            LegacyMetadataMapper.convertRequestMetadata(legacyRequest);
                            if (!RequestThreadManager.this.mParams.same(legacyRequest.parameters)) {
                                try {
                                    RequestThreadManager.this.mCamera.setParameters(legacyRequest.parameters);
                                    paramsChanged = true;
                                    RequestThreadManager.this.mParams = legacyRequest.parameters;
                                } catch (RuntimeException e3) {
                                    Log.e(RequestThreadManager.this.TAG, "Exception while setting camera parameters: ", e3);
                                    holder.failRequest();
                                    RequestThreadManager.this.mDeviceState.setCaptureStart(holder, 0, 3);
                                }
                            }
                            RequestThreadManager.this.mLastRequest = legacyRequest;
                        }
                        try {
                            if (RequestThreadManager.this.mCaptureCollector.queueRequest(holder, RequestThreadManager.this.mLastRequest, 4000, TimeUnit.MILLISECONDS)) {
                                if (holder.hasPreviewTargets()) {
                                    RequestThreadManager.this.doPreviewCapture(holder);
                                }
                                if (holder.hasJpegTargets()) {
                                    while (!RequestThreadManager.this.mCaptureCollector.waitForPreviewsEmpty(1000, TimeUnit.MILLISECONDS)) {
                                        Log.e(RequestThreadManager.this.TAG, "Timed out while waiting for preview requests to complete.");
                                        RequestThreadManager.this.mCaptureCollector.failNextPreview();
                                    }
                                    RequestThreadManager.this.mReceivedJpeg.close();
                                    RequestThreadManager.this.doJpegCapturePrepare(holder);
                                }
                                RequestThreadManager.this.mFaceDetectMapper.processFaceDetectMode(request, RequestThreadManager.this.mParams);
                                RequestThreadManager.this.mFocusStateMapper.processRequestTriggers(request, RequestThreadManager.this.mParams);
                                if (holder.hasJpegTargets()) {
                                    RequestThreadManager.this.doJpegCapture(holder);
                                    if (!RequestThreadManager.this.mReceivedJpeg.block(4000)) {
                                        Log.e(RequestThreadManager.this.TAG, "Hit timeout for jpeg callback!");
                                        RequestThreadManager.this.mCaptureCollector.failNextJpeg();
                                    }
                                }
                                if (paramsChanged) {
                                    try {
                                        RequestThreadManager.this.mParams = RequestThreadManager.this.mCamera.getParameters();
                                        RequestThreadManager.this.mLastRequest.setParameters(RequestThreadManager.this.mParams);
                                    } catch (RuntimeException e32) {
                                        Log.e(RequestThreadManager.this.TAG, "Received device exception: ", e32);
                                        RequestThreadManager.this.mDeviceState.setError(1);
                                        break;
                                    }
                                }
                                MutableLong timestampMutable = new MutableLong(0);
                                try {
                                    if (!RequestThreadManager.this.mCaptureCollector.waitForRequestCompleted(holder, 4000, TimeUnit.MILLISECONDS, timestampMutable)) {
                                        Log.e(RequestThreadManager.this.TAG, "Timed out while waiting for request to complete.");
                                        RequestThreadManager.this.mCaptureCollector.failAll();
                                    }
                                    CameraMetadataNative result = this.mMapper.cachedConvertResultMetadata(RequestThreadManager.this.mLastRequest, timestampMutable.value);
                                    RequestThreadManager.this.mFocusStateMapper.mapResultTriggers(result);
                                    RequestThreadManager.this.mFaceDetectMapper.mapResultFaces(result, RequestThreadManager.this.mLastRequest);
                                    if (!holder.requestFailed()) {
                                        RequestThreadManager.this.mDeviceState.setCaptureResult(holder, result, -1);
                                    }
                                } catch (InterruptedException e22) {
                                    Log.e(RequestThreadManager.this.TAG, "Interrupted waiting for request completion: ", e22);
                                    RequestThreadManager.this.mDeviceState.setError(1);
                                    break;
                                }
                            }
                            Log.e(RequestThreadManager.this.TAG, "Timed out while queueing capture request.");
                            holder.failRequest();
                            RequestThreadManager.this.mDeviceState.setCaptureStart(holder, 0, 3);
                        } catch (IOException e4) {
                            Log.e(RequestThreadManager.this.TAG, "Received device exception during capture call: ", e4);
                            RequestThreadManager.this.mDeviceState.setError(1);
                            break;
                        } catch (InterruptedException e222) {
                            Log.e(RequestThreadManager.this.TAG, "Interrupted during capture: ", e222);
                            RequestThreadManager.this.mDeviceState.setError(1);
                            break;
                        } catch (RuntimeException e322) {
                            Log.e(RequestThreadManager.this.TAG, "Received device exception during capture call: ", e322);
                            RequestThreadManager.this.mDeviceState.setError(1);
                            break;
                        }
                    }
                    break;
                case 3:
                    this.mCleanup = true;
                    try {
                        if (!RequestThreadManager.this.mCaptureCollector.waitForEmpty(4000, TimeUnit.MILLISECONDS)) {
                            Log.e(RequestThreadManager.this.TAG, "Timed out while queueing cleanup request.");
                            RequestThreadManager.this.mCaptureCollector.failAll();
                        }
                    } catch (InterruptedException e2222) {
                        Log.e(RequestThreadManager.this.TAG, "Interrupted while waiting for requests to complete: ", e2222);
                        RequestThreadManager.this.mDeviceState.setError(1);
                    }
                    if (RequestThreadManager.this.mGLThreadManager != null) {
                        RequestThreadManager.this.mGLThreadManager.quit();
                        RequestThreadManager.this.mGLThreadManager = null;
                    }
                    if (RequestThreadManager.this.mCamera != null) {
                        RequestThreadManager.this.mCamera.release();
                        RequestThreadManager.this.mCamera = null;
                    }
                    RequestThreadManager.this.resetJpegSurfaceFormats(RequestThreadManager.this.mCallbackOutputs);
                    break;
                default:
                    throw new AssertionError("Unhandled message " + msg.what + " on RequestThread.");
            }
            return true;
        }
    };
    private final RequestQueue mRequestQueue = new RequestQueue(this.mJpegSurfaceIds);
    private final RequestHandlerThread mRequestThread;

    private static class ConfigureHolder {
        public final ConditionVariable condition;
        public final Collection<Pair<Surface, Size>> surfaces;

        public ConfigureHolder(ConditionVariable condition, Collection<Pair<Surface, Size>> surfaces) {
            this.condition = condition;
            this.surfaces = surfaces;
        }
    }

    public static class FpsCounter {
        private static final long NANO_PER_SECOND = 1000000000;
        private static final String TAG = "FpsCounter";
        private int mFrameCount = 0;
        private double mLastFps = 0.0d;
        private long mLastPrintTime = 0;
        private long mLastTime = 0;
        private final String mStreamType;

        public FpsCounter(String streamType) {
            this.mStreamType = streamType;
        }

        public synchronized void countFrame() {
            this.mFrameCount++;
            long nextTime = SystemClock.elapsedRealtimeNanos();
            if (this.mLastTime == 0) {
                this.mLastTime = nextTime;
            }
            if (nextTime > this.mLastTime + NANO_PER_SECOND) {
                this.mLastFps = ((double) this.mFrameCount) * (1.0E9d / ((double) (nextTime - this.mLastTime)));
                this.mFrameCount = 0;
                this.mLastTime = nextTime;
            }
        }

        public synchronized double checkFps() {
            return this.mLastFps;
        }

        public synchronized void staggeredLog() {
            if (this.mLastTime > this.mLastPrintTime + 5000000000L) {
                this.mLastPrintTime = this.mLastTime;
                Log.d(TAG, "FPS for " + this.mStreamType + " stream: " + this.mLastFps);
            }
        }

        public synchronized void countAndLog() {
            countFrame();
            staggeredLog();
        }
    }

    private void createDummySurface() {
        if (this.mDummyTexture == null || this.mDummySurface == null) {
            this.mDummyTexture = new SurfaceTexture(0);
            this.mDummyTexture.setDefaultBufferSize(640, 480);
            this.mDummySurface = new Surface(this.mDummyTexture);
        }
    }

    private void stopPreview() {
        this.mCamera.stopPreview();
        this.mPreviewRunning = false;
    }

    private void startPreview() {
        if (!this.mPreviewRunning) {
            this.mCamera.startPreview();
            this.mPreviewRunning = true;
        }
    }

    private void doJpegCapturePrepare(RequestHolder request) throws IOException {
        if (!this.mPreviewRunning) {
            createDummySurface();
            this.mCamera.setPreviewTexture(this.mDummyTexture);
            startPreview();
        }
    }

    private void doJpegCapture(RequestHolder request) {
        this.mCamera.takePicture(this.mJpegShutterCallback, null, this.mJpegCallback);
        this.mPreviewRunning = false;
    }

    private void doPreviewCapture(RequestHolder request) throws IOException {
        if (!this.mPreviewRunning) {
            if (this.mPreviewTexture == null) {
                throw new IllegalStateException("Preview capture called with no preview surfaces configured.");
            }
            this.mPreviewTexture.setDefaultBufferSize(this.mIntermediateBufferSize.getWidth(), this.mIntermediateBufferSize.getHeight());
            this.mCamera.setPreviewTexture(this.mPreviewTexture);
            startPreview();
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void configureOutputs(java.util.Collection<android.util.Pair<android.view.Surface, android.util.Size>> r37) {
        /*
        r36 = this;
        r36.stopPreview();	 Catch:{ RuntimeException -> 0x00ee }
        r0 = r36;
        r0 = r0.mCamera;	 Catch:{ IOException -> 0x010a, RuntimeException -> 0x011c }
        r31 = r0;
        r32 = 0;
        r31.setPreviewTexture(r32);	 Catch:{ IOException -> 0x010a, RuntimeException -> 0x011c }
    L_0x000e:
        r0 = r36;
        r0 = r0.mGLThreadManager;
        r31 = r0;
        if (r31 == 0) goto L_0x0031;
    L_0x0016:
        r0 = r36;
        r0 = r0.mGLThreadManager;
        r31 = r0;
        r31.waitUntilStarted();
        r0 = r36;
        r0 = r0.mGLThreadManager;
        r31 = r0;
        r31.ignoreNewFrames();
        r0 = r36;
        r0 = r0.mGLThreadManager;
        r31 = r0;
        r31.waitUntilIdle();
    L_0x0031:
        r0 = r36;
        r0 = r0.mCallbackOutputs;
        r31 = r0;
        r0 = r36;
        r1 = r31;
        r0.resetJpegSurfaceFormats(r1);
        r0 = r36;
        r0 = r0.mPreviewOutputs;
        r31 = r0;
        r31.clear();
        r0 = r36;
        r0 = r0.mCallbackOutputs;
        r31 = r0;
        r31.clear();
        r0 = r36;
        r0 = r0.mJpegSurfaceIds;
        r31 = r0;
        r31.clear();
        r31 = 0;
        r0 = r31;
        r1 = r36;
        r1.mPreviewTexture = r0;
        r24 = new java.util.ArrayList;
        r24.<init>();
        r8 = new java.util.ArrayList;
        r8.<init>();
        r0 = r36;
        r0 = r0.mCharacteristics;
        r31 = r0;
        r32 = android.hardware.camera2.CameraCharacteristics.LENS_FACING;
        r31 = r31.get(r32);
        r31 = (java.lang.Integer) r31;
        r13 = r31.intValue();
        r0 = r36;
        r0 = r0.mCharacteristics;
        r31 = r0;
        r32 = android.hardware.camera2.CameraCharacteristics.SENSOR_ORIENTATION;
        r31 = r31.get(r32);
        r31 = (java.lang.Integer) r31;
        r20 = r31.intValue();
        if (r37 == 0) goto L_0x0166;
    L_0x0091:
        r15 = r37.iterator();
    L_0x0095:
        r31 = r15.hasNext();
        if (r31 == 0) goto L_0x0166;
    L_0x009b:
        r21 = r15.next();
        r21 = (android.util.Pair) r21;
        r0 = r21;
        r0 = r0.first;
        r27 = r0;
        r27 = (android.view.Surface) r27;
        r0 = r21;
        r0 = r0.second;
        r22 = r0;
        r22 = (android.util.Size) r22;
        r14 = android.hardware.camera2.legacy.LegacyCameraDevice.detectSurfaceType(r27);	 Catch:{ BufferQueueAbandonedException -> 0x00dd }
        r0 = r27;
        r1 = r20;
        android.hardware.camera2.legacy.LegacyCameraDevice.setSurfaceOrientation(r0, r13, r1);	 Catch:{ BufferQueueAbandonedException -> 0x00dd }
        switch(r14) {
            case 33: goto L_0x0138;
            default: goto L_0x00bf;
        };	 Catch:{ BufferQueueAbandonedException -> 0x00dd }
    L_0x00bf:
        r31 = 1;
        r0 = r27;
        r1 = r31;
        android.hardware.camera2.legacy.LegacyCameraDevice.setScalingMode(r0, r1);	 Catch:{ BufferQueueAbandonedException -> 0x00dd }
        r0 = r36;
        r0 = r0.mPreviewOutputs;	 Catch:{ BufferQueueAbandonedException -> 0x00dd }
        r31 = r0;
        r0 = r31;
        r1 = r27;
        r0.add(r1);	 Catch:{ BufferQueueAbandonedException -> 0x00dd }
        r0 = r24;
        r1 = r22;
        r0.add(r1);	 Catch:{ BufferQueueAbandonedException -> 0x00dd }
        goto L_0x0095;
    L_0x00dd:
        r12 = move-exception;
        r0 = r36;
        r0 = r0.TAG;
        r31 = r0;
        r32 = "Surface abandoned, skipping...";
        r0 = r31;
        r1 = r32;
        android.util.Log.w(r0, r1, r12);
        goto L_0x0095;
    L_0x00ee:
        r12 = move-exception;
        r0 = r36;
        r0 = r0.TAG;
        r31 = r0;
        r32 = "Received device exception in configure call: ";
        r0 = r31;
        r1 = r32;
        android.util.Log.e(r0, r1, r12);
        r0 = r36;
        r0 = r0.mDeviceState;
        r31 = r0;
        r32 = 1;
        r31.setError(r32);
    L_0x0109:
        return;
    L_0x010a:
        r12 = move-exception;
        r0 = r36;
        r0 = r0.TAG;
        r31 = r0;
        r32 = "Failed to clear prior SurfaceTexture, may cause GL deadlock: ";
        r0 = r31;
        r1 = r32;
        android.util.Log.w(r0, r1, r12);
        goto L_0x000e;
    L_0x011c:
        r12 = move-exception;
        r0 = r36;
        r0 = r0.TAG;
        r31 = r0;
        r32 = "Received device exception in configure call: ";
        r0 = r31;
        r1 = r32;
        android.util.Log.e(r0, r1, r12);
        r0 = r36;
        r0 = r0.mDeviceState;
        r31 = r0;
        r32 = 1;
        r31.setError(r32);
        goto L_0x0109;
    L_0x0138:
        r31 = 1;
        r0 = r27;
        r1 = r31;
        android.hardware.camera2.legacy.LegacyCameraDevice.setSurfaceFormat(r0, r1);	 Catch:{ BufferQueueAbandonedException -> 0x00dd }
        r0 = r36;
        r0 = r0.mJpegSurfaceIds;	 Catch:{ BufferQueueAbandonedException -> 0x00dd }
        r31 = r0;
        r32 = android.hardware.camera2.legacy.LegacyCameraDevice.getSurfaceId(r27);	 Catch:{ BufferQueueAbandonedException -> 0x00dd }
        r32 = java.lang.Long.valueOf(r32);	 Catch:{ BufferQueueAbandonedException -> 0x00dd }
        r31.add(r32);	 Catch:{ BufferQueueAbandonedException -> 0x00dd }
        r0 = r36;
        r0 = r0.mCallbackOutputs;	 Catch:{ BufferQueueAbandonedException -> 0x00dd }
        r31 = r0;
        r0 = r31;
        r1 = r27;
        r0.add(r1);	 Catch:{ BufferQueueAbandonedException -> 0x00dd }
        r0 = r22;
        r8.add(r0);	 Catch:{ BufferQueueAbandonedException -> 0x00dd }
        goto L_0x0095;
    L_0x0166:
        r0 = r36;
        r0 = r0.mCamera;	 Catch:{ RuntimeException -> 0x022a }
        r31 = r0;
        r31 = r31.getParameters();	 Catch:{ RuntimeException -> 0x022a }
        r0 = r31;
        r1 = r36;
        r1.mParams = r0;	 Catch:{ RuntimeException -> 0x022a }
        r0 = r36;
        r0 = r0.mParams;
        r31 = r0;
        r29 = r31.getSupportedPreviewFpsRange();
        r0 = r36;
        r1 = r29;
        r7 = r0.getPhotoPreviewFpsRange(r1);
        r0 = r36;
        r0 = r0.mParams;
        r31 = r0;
        r32 = 0;
        r32 = r7[r32];
        r33 = 1;
        r33 = r7[r33];
        r31.setPreviewFpsRange(r32, r33);
        r0 = r36;
        r0 = r0.mCallbackOutputs;
        r31 = r0;
        r0 = r36;
        r0 = r0.mParams;
        r32 = r0;
        r0 = r36;
        r1 = r31;
        r2 = r32;
        r28 = r0.calculatePictureSize(r1, r8, r2);
        r31 = r24.size();
        if (r31 <= 0) goto L_0x030c;
    L_0x01b5:
        r17 = android.hardware.camera2.utils.SizeAreaComparator.findLargestByArea(r24);
        r0 = r36;
        r0 = r0.mParams;
        r31 = r0;
        r16 = android.hardware.camera2.legacy.ParameterUtils.getLargestSupportedJpegSizeByArea(r31);
        if (r28 == 0) goto L_0x0247;
    L_0x01c5:
        r9 = r28;
    L_0x01c7:
        r0 = r36;
        r0 = r0.mParams;
        r31 = r0;
        r31 = r31.getSupportedPreviewSizes();
        r30 = android.hardware.camera2.legacy.ParameterUtils.convertSizeList(r31);
        r31 = r17.getHeight();
        r0 = r31;
        r0 = (long) r0;
        r32 = r0;
        r31 = r17.getWidth();
        r0 = r31;
        r0 = (long) r0;
        r34 = r0;
        r18 = r32 * r34;
        r6 = android.hardware.camera2.utils.SizeAreaComparator.findLargestByArea(r30);
        r15 = r30.iterator();
    L_0x01f1:
        r31 = r15.hasNext();
        if (r31 == 0) goto L_0x024b;
    L_0x01f7:
        r27 = r15.next();
        r27 = (android.util.Size) r27;
        r31 = r27.getWidth();
        r32 = r27.getHeight();
        r31 = r31 * r32;
        r0 = r31;
        r10 = (long) r0;
        r31 = r6.getWidth();
        r32 = r6.getHeight();
        r31 = r31 * r32;
        r0 = r31;
        r4 = (long) r0;
        r0 = r27;
        r31 = checkAspectRatiosMatch(r9, r0);
        if (r31 == 0) goto L_0x01f1;
    L_0x021f:
        r31 = (r10 > r4 ? 1 : (r10 == r4 ? 0 : -1));
        if (r31 >= 0) goto L_0x01f1;
    L_0x0223:
        r31 = (r10 > r18 ? 1 : (r10 == r18 ? 0 : -1));
        if (r31 < 0) goto L_0x01f1;
    L_0x0227:
        r6 = r27;
        goto L_0x01f1;
    L_0x022a:
        r12 = move-exception;
        r0 = r36;
        r0 = r0.TAG;
        r31 = r0;
        r32 = "Received device exception: ";
        r0 = r31;
        r1 = r32;
        android.util.Log.e(r0, r1, r12);
        r0 = r36;
        r0 = r0.mDeviceState;
        r31 = r0;
        r32 = 1;
        r31.setError(r32);
        goto L_0x0109;
    L_0x0247:
        r9 = r16;
        goto L_0x01c7;
    L_0x024b:
        r0 = r36;
        r0.mIntermediateBufferSize = r6;
        r0 = r36;
        r0 = r0.mParams;
        r31 = r0;
        r0 = r36;
        r0 = r0.mIntermediateBufferSize;
        r32 = r0;
        r32 = r32.getWidth();
        r0 = r36;
        r0 = r0.mIntermediateBufferSize;
        r33 = r0;
        r33 = r33.getHeight();
        r31.setPreviewSize(r32, r33);
    L_0x026c:
        if (r28 == 0) goto L_0x029f;
    L_0x026e:
        r0 = r36;
        r0 = r0.TAG;
        r31 = r0;
        r32 = new java.lang.StringBuilder;
        r32.<init>();
        r33 = "configureOutputs - set take picture size to ";
        r32 = r32.append(r33);
        r0 = r32;
        r1 = r28;
        r32 = r0.append(r1);
        r32 = r32.toString();
        android.util.Log.i(r31, r32);
        r0 = r36;
        r0 = r0.mParams;
        r31 = r0;
        r32 = r28.getWidth();
        r33 = r28.getHeight();
        r31.setPictureSize(r32, r33);
    L_0x029f:
        r0 = r36;
        r0 = r0.mGLThreadManager;
        r31 = r0;
        if (r31 != 0) goto L_0x02cd;
    L_0x02a7:
        r31 = new android.hardware.camera2.legacy.GLThreadManager;
        r0 = r36;
        r0 = r0.mCameraId;
        r32 = r0;
        r0 = r36;
        r0 = r0.mDeviceState;
        r33 = r0;
        r0 = r31;
        r1 = r32;
        r2 = r33;
        r0.<init>(r1, r13, r2);
        r0 = r31;
        r1 = r36;
        r1.mGLThreadManager = r0;
        r0 = r36;
        r0 = r0.mGLThreadManager;
        r31 = r0;
        r31.start();
    L_0x02cd:
        r0 = r36;
        r0 = r0.mGLThreadManager;
        r31 = r0;
        r31.waitUntilStarted();
        r26 = new java.util.ArrayList;
        r26.<init>();
        r25 = r24.iterator();
        r0 = r36;
        r0 = r0.mPreviewOutputs;
        r31 = r0;
        r15 = r31.iterator();
    L_0x02e9:
        r31 = r15.hasNext();
        if (r31 == 0) goto L_0x0316;
    L_0x02ef:
        r23 = r15.next();
        r23 = (android.view.Surface) r23;
        r31 = new android.util.Pair;
        r32 = r25.next();
        r0 = r31;
        r1 = r23;
        r2 = r32;
        r0.<init>(r1, r2);
        r0 = r26;
        r1 = r31;
        r0.add(r1);
        goto L_0x02e9;
    L_0x030c:
        r31 = 0;
        r0 = r31;
        r1 = r36;
        r1.mIntermediateBufferSize = r0;
        goto L_0x026c;
    L_0x0316:
        r0 = r36;
        r0 = r0.mGLThreadManager;
        r31 = r0;
        r0 = r36;
        r0 = r0.mCaptureCollector;
        r32 = r0;
        r0 = r31;
        r1 = r26;
        r2 = r32;
        r0.setConfigurationAndWait(r1, r2);
        r0 = r36;
        r0 = r0.mGLThreadManager;
        r31 = r0;
        r31.allowNewFrames();
        r0 = r36;
        r0 = r0.mGLThreadManager;
        r31 = r0;
        r31 = r31.getCurrentSurfaceTexture();
        r0 = r31;
        r1 = r36;
        r1.mPreviewTexture = r0;
        r0 = r36;
        r0 = r0.mPreviewTexture;
        r31 = r0;
        if (r31 == 0) goto L_0x035b;
    L_0x034c:
        r0 = r36;
        r0 = r0.mPreviewTexture;
        r31 = r0;
        r0 = r36;
        r0 = r0.mPreviewCallback;
        r32 = r0;
        r31.setOnFrameAvailableListener(r32);
    L_0x035b:
        r0 = r36;
        r0 = r0.mCamera;	 Catch:{ RuntimeException -> 0x036c }
        r31 = r0;
        r0 = r36;
        r0 = r0.mParams;	 Catch:{ RuntimeException -> 0x036c }
        r32 = r0;
        r31.setParameters(r32);	 Catch:{ RuntimeException -> 0x036c }
        goto L_0x0109;
    L_0x036c:
        r12 = move-exception;
        r0 = r36;
        r0 = r0.TAG;
        r31 = r0;
        r32 = "Received device exception while configuring: ";
        r0 = r31;
        r1 = r32;
        android.util.Log.e(r0, r1, r12);
        r0 = r36;
        r0 = r0.mDeviceState;
        r31 = r0;
        r32 = 1;
        r31.setError(r32);
        goto L_0x0109;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.hardware.camera2.legacy.RequestThreadManager.configureOutputs(java.util.Collection):void");
    }

    private void resetJpegSurfaceFormats(Collection<Surface> surfaces) {
        if (surfaces != null) {
            for (Surface s : surfaces) {
                if (s == null || !s.isValid()) {
                    Log.w(this.TAG, "Jpeg surface is invalid, skipping...");
                } else {
                    try {
                        LegacyCameraDevice.setSurfaceFormat(s, 33);
                    } catch (BufferQueueAbandonedException e) {
                        Log.w(this.TAG, "Surface abandoned, skipping...", e);
                    }
                }
            }
        }
    }

    private Size calculatePictureSize(List<Surface> callbackOutputs, List<Size> callbackSizes, Parameters params) {
        if (callbackOutputs.size() != callbackSizes.size()) {
            throw new IllegalStateException("Input collections must be same length");
        }
        List<Size> configuredJpegSizes = new ArrayList();
        Iterator<Size> sizeIterator = callbackSizes.iterator();
        for (Surface callbackSurface : callbackOutputs) {
            Size jpegSize = (Size) sizeIterator.next();
            if (LegacyCameraDevice.containsSurfaceId(callbackSurface, this.mJpegSurfaceIds)) {
                configuredJpegSizes.add(jpegSize);
            }
        }
        if (configuredJpegSizes.isEmpty()) {
            return null;
        }
        int maxConfiguredJpegWidth = -1;
        int maxConfiguredJpegHeight = -1;
        for (Size jpegSize2 : configuredJpegSizes) {
            if (jpegSize2.getWidth() > maxConfiguredJpegWidth) {
                maxConfiguredJpegWidth = jpegSize2.getWidth();
            }
            if (jpegSize2.getHeight() > maxConfiguredJpegHeight) {
                maxConfiguredJpegHeight = jpegSize2.getHeight();
            }
        }
        Size smallestBoundJpegSize = new Size(maxConfiguredJpegWidth, maxConfiguredJpegHeight);
        List<Size> supportedJpegSizes = ParameterUtils.convertSizeList(params.getSupportedPictureSizes());
        List<Size> candidateSupportedJpegSizes = new ArrayList();
        for (Size supportedJpegSize : supportedJpegSizes) {
            if (supportedJpegSize.getWidth() >= maxConfiguredJpegWidth && supportedJpegSize.getHeight() >= maxConfiguredJpegHeight) {
                candidateSupportedJpegSizes.add(supportedJpegSize);
            }
        }
        if (candidateSupportedJpegSizes.isEmpty()) {
            throw new AssertionError("Could not find any supported JPEG sizes large enough to fit " + smallestBoundJpegSize);
        }
        Size smallestSupportedJpegSize = (Size) Collections.min(candidateSupportedJpegSizes, new SizeAreaComparator());
        if (smallestSupportedJpegSize.equals(smallestBoundJpegSize)) {
            return smallestSupportedJpegSize;
        }
        Log.w(this.TAG, String.format("configureOutputs - Will need to crop picture %s into smallest bound size %s", new Object[]{smallestSupportedJpegSize, smallestBoundJpegSize}));
        return smallestSupportedJpegSize;
    }

    private static boolean checkAspectRatiosMatch(Size a, Size b) {
        return Math.abs((((float) a.getWidth()) / ((float) a.getHeight())) - (((float) b.getWidth()) / ((float) b.getHeight()))) < ASPECT_RATIO_TOLERANCE;
    }

    private int[] getPhotoPreviewFpsRange(List<int[]> frameRates) {
        if (frameRates.size() == 0) {
            Log.e(this.TAG, "No supported frame rates returned!");
            return null;
        }
        int bestMin = 0;
        int bestMax = 0;
        int bestIndex = 0;
        int index = 0;
        for (int[] rate : frameRates) {
            int minFps = rate[0];
            int maxFps = rate[1];
            if (maxFps > bestMax || (maxFps == bestMax && minFps > bestMin)) {
                bestMin = minFps;
                bestMax = maxFps;
                bestIndex = index;
            }
            index++;
        }
        return (int[]) frameRates.get(bestIndex);
    }

    public RequestThreadManager(int cameraId, Camera camera, CameraCharacteristics characteristics, CameraDeviceState deviceState) {
        this.mCamera = (Camera) Preconditions.checkNotNull(camera, "camera must not be null");
        this.mCameraId = cameraId;
        this.mCharacteristics = (CameraCharacteristics) Preconditions.checkNotNull(characteristics, "characteristics must not be null");
        String name = String.format("RequestThread-%d", new Object[]{Integer.valueOf(cameraId)});
        this.TAG = name;
        this.mDeviceState = (CameraDeviceState) Preconditions.checkNotNull(deviceState, "deviceState must not be null");
        this.mFocusStateMapper = new LegacyFocusStateMapper(this.mCamera);
        this.mFaceDetectMapper = new LegacyFaceDetectMapper(this.mCamera, this.mCharacteristics);
        this.mCaptureCollector = new CaptureCollector(2, this.mDeviceState);
        this.mRequestThread = new RequestHandlerThread(name, this.mRequestHandlerCb);
        this.mCamera.setErrorCallback(this.mErrorCallback);
    }

    public void start() {
        this.mRequestThread.start();
    }

    public long flush() {
        Log.i(this.TAG, "Flushing all pending requests.");
        long lastFrame = this.mRequestQueue.stopRepeating();
        this.mCaptureCollector.failAll();
        return lastFrame;
    }

    public void quit() {
        if (!this.mQuit.getAndSet(true)) {
            Handler handler = this.mRequestThread.waitAndGetHandler();
            handler.sendMessageAtFrontOfQueue(handler.obtainMessage(3));
            this.mRequestThread.quitSafely();
            try {
                this.mRequestThread.join();
            } catch (InterruptedException e) {
                Log.e(this.TAG, String.format("Thread %s (%d) interrupted while quitting.", new Object[]{this.mRequestThread.getName(), Long.valueOf(this.mRequestThread.getId())}));
            }
        }
    }

    public int submitCaptureRequests(List<CaptureRequest> requests, boolean repeating, LongParcelable frameNumber) {
        int ret;
        Handler handler = this.mRequestThread.waitAndGetHandler();
        synchronized (this.mIdleLock) {
            ret = this.mRequestQueue.submit(requests, repeating, frameNumber);
            handler.sendEmptyMessage(2);
        }
        return ret;
    }

    public long cancelRepeating(int requestId) {
        return this.mRequestQueue.stopRepeating(requestId);
    }

    public void configure(Collection<Pair<Surface, Size>> outputs) {
        Handler handler = this.mRequestThread.waitAndGetHandler();
        ConditionVariable condition = new ConditionVariable(false);
        handler.sendMessage(handler.obtainMessage(1, 0, 0, new ConfigureHolder(condition, outputs)));
        condition.block();
    }
}
