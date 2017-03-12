package android.hardware.camera2.legacy;

import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.ICameraDeviceCallbacks;
import android.hardware.camera2.ICameraDeviceUser;
import android.hardware.camera2.impl.CameraMetadataNative;
import android.hardware.camera2.impl.CaptureResultExtras;
import android.hardware.camera2.params.OutputConfiguration;
import android.hardware.camera2.utils.CameraBinderDecorator;
import android.hardware.camera2.utils.CameraRuntimeException;
import android.hardware.camera2.utils.LongParcelable;
import android.os.ConditionVariable;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.util.SparseArray;
import android.view.Surface;
import java.util.ArrayList;
import java.util.List;

public class CameraDeviceUserShim implements ICameraDeviceUser {
    private static final boolean DEBUG = false;
    private static final int OPEN_CAMERA_TIMEOUT_MS = 5000;
    private static final String TAG = "CameraDeviceUserShim";
    private final CameraCallbackThread mCameraCallbacks;
    private final CameraCharacteristics mCameraCharacteristics;
    private final CameraLooper mCameraInit;
    private final Object mConfigureLock = new Object();
    private boolean mConfiguring;
    private final LegacyCameraDevice mLegacyDevice;
    private int mSurfaceIdCounter;
    private final SparseArray<Surface> mSurfaces;

    private static class CameraCallbackThread implements ICameraDeviceCallbacks {
        private static final int CAMERA_ERROR = 0;
        private static final int CAMERA_IDLE = 1;
        private static final int CAPTURE_STARTED = 2;
        private static final int PREPARED = 4;
        private static final int RESULT_RECEIVED = 3;
        private final ICameraDeviceCallbacks mCallbacks;
        private Handler mHandler;
        private final HandlerThread mHandlerThread = new HandlerThread("LegacyCameraCallback");

        private class CallbackHandler extends Handler {
            public CallbackHandler(Looper l) {
                super(l);
            }

            public void handleMessage(Message msg) {
                try {
                    switch (msg.what) {
                        case 0:
                            CameraCallbackThread.this.mCallbacks.onDeviceError(msg.arg1, msg.obj);
                            return;
                        case 1:
                            CameraCallbackThread.this.mCallbacks.onDeviceIdle();
                            return;
                        case 2:
                            CaptureResultExtras resultExtras = (CaptureResultExtras) msg.obj;
                            CameraCallbackThread.this.mCallbacks.onCaptureStarted(resultExtras, ((((long) msg.arg2) & 4294967295L) << 32) | (((long) msg.arg1) & 4294967295L));
                            return;
                        case 3:
                            Object[] resultArray = (Object[]) msg.obj;
                            CameraCallbackThread.this.mCallbacks.onResultReceived(resultArray[0], (CaptureResultExtras) resultArray[1]);
                            return;
                        case 4:
                            CameraCallbackThread.this.mCallbacks.onPrepared(msg.arg1);
                            return;
                        default:
                            throw new IllegalArgumentException("Unknown callback message " + msg.what);
                    }
                } catch (RemoteException e) {
                    throw new IllegalStateException("Received remote exception during camera callback " + msg.what, e);
                }
                throw new IllegalStateException("Received remote exception during camera callback " + msg.what, e);
            }
        }

        public CameraCallbackThread(ICameraDeviceCallbacks callbacks) {
            this.mCallbacks = callbacks;
            this.mHandlerThread.start();
        }

        public void close() {
            this.mHandlerThread.quitSafely();
        }

        public void onDeviceError(int errorCode, CaptureResultExtras resultExtras) {
            getHandler().sendMessage(getHandler().obtainMessage(0, errorCode, 0, resultExtras));
        }

        public void onDeviceIdle() {
            getHandler().sendMessage(getHandler().obtainMessage(1));
        }

        public void onCaptureStarted(CaptureResultExtras resultExtras, long timestamp) {
            getHandler().sendMessage(getHandler().obtainMessage(2, (int) (timestamp & 4294967295L), (int) ((timestamp >> 32) & 4294967295L), resultExtras));
        }

        public void onResultReceived(CameraMetadataNative result, CaptureResultExtras resultExtras) {
            getHandler().sendMessage(getHandler().obtainMessage(3, new Object[]{result, resultExtras}));
        }

        public void onPrepared(int streamId) {
            getHandler().sendMessage(getHandler().obtainMessage(4, streamId, 0));
        }

        public IBinder asBinder() {
            return null;
        }

        private Handler getHandler() {
            if (this.mHandler == null) {
                this.mHandler = new CallbackHandler(this.mHandlerThread.getLooper());
            }
            return this.mHandler;
        }
    }

    private static class CameraLooper implements Runnable, AutoCloseable {
        private final Camera mCamera = Camera.openUninitialized();
        private final int mCameraId;
        private volatile int mInitErrors;
        private Looper mLooper;
        private final ConditionVariable mStartDone = new ConditionVariable();
        private final Thread mThread;

        public CameraLooper(int cameraId) {
            this.mCameraId = cameraId;
            this.mThread = new Thread(this);
            this.mThread.start();
        }

        public Camera getCamera() {
            return this.mCamera;
        }

        public void run() {
            Looper.prepare();
            this.mLooper = Looper.myLooper();
            this.mInitErrors = this.mCamera.cameraInitUnspecified(this.mCameraId);
            this.mStartDone.open();
            Looper.loop();
        }

        public void close() {
            if (this.mLooper != null) {
                this.mLooper.quitSafely();
                try {
                    this.mThread.join();
                    this.mLooper = null;
                } catch (InterruptedException e) {
                    throw new AssertionError(e);
                }
            }
        }

        public int waitForOpen(int timeoutMs) {
            if (this.mStartDone.block((long) timeoutMs)) {
                return this.mInitErrors;
            }
            Log.e(CameraDeviceUserShim.TAG, "waitForOpen - Camera failed to open after timeout of 5000 ms");
            try {
                this.mCamera.release();
            } catch (RuntimeException e) {
                Log.e(CameraDeviceUserShim.TAG, "connectBinderShim - Failed to release camera after timeout ", e);
            }
            throw new CameraRuntimeException(3);
        }
    }

    protected CameraDeviceUserShim(int cameraId, LegacyCameraDevice legacyCamera, CameraCharacteristics characteristics, CameraLooper cameraInit, CameraCallbackThread cameraCallbacks) {
        this.mLegacyDevice = legacyCamera;
        this.mConfiguring = false;
        this.mSurfaces = new SparseArray();
        this.mCameraCharacteristics = characteristics;
        this.mCameraInit = cameraInit;
        this.mCameraCallbacks = cameraCallbacks;
        this.mSurfaceIdCounter = 0;
    }

    public static CameraDeviceUserShim connectBinderShim(ICameraDeviceCallbacks callbacks, int cameraId) {
        CameraLooper init = new CameraLooper(cameraId);
        CameraCallbackThread threadCallbacks = new CameraCallbackThread(callbacks);
        int initErrors = init.waitForOpen(5000);
        Camera legacyCamera = init.getCamera();
        CameraBinderDecorator.throwOnError(initErrors);
        legacyCamera.disableShutterSound();
        CameraInfo info = new CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        try {
            CameraCharacteristics characteristics = LegacyMetadataMapper.createCharacteristics(legacyCamera.getParameters(), info);
            return new CameraDeviceUserShim(cameraId, new LegacyCameraDevice(cameraId, legacyCamera, characteristics, threadCallbacks), characteristics, init, threadCallbacks);
        } catch (RuntimeException e) {
            throw new CameraRuntimeException(3, "Unable to get initial parameters", e);
        }
    }

    public void disconnect() {
        if (this.mLegacyDevice.isClosed()) {
            Log.w(TAG, "Cannot disconnect, device has already been closed.");
        }
        try {
            this.mLegacyDevice.close();
        } finally {
            this.mCameraInit.close();
            this.mCameraCallbacks.close();
        }
    }

    public int submitRequest(CaptureRequest request, boolean streaming, LongParcelable lastFrameNumber) {
        if (this.mLegacyDevice.isClosed()) {
            Log.e(TAG, "Cannot submit request, device has been closed.");
            return -19;
        }
        synchronized (this.mConfigureLock) {
            if (this.mConfiguring) {
                Log.e(TAG, "Cannot submit request, configuration change in progress.");
                return -38;
            }
            return this.mLegacyDevice.submitRequest(request, streaming, lastFrameNumber);
        }
    }

    public int submitRequestList(List<CaptureRequest> request, boolean streaming, LongParcelable lastFrameNumber) {
        if (this.mLegacyDevice.isClosed()) {
            Log.e(TAG, "Cannot submit request list, device has been closed.");
            return -19;
        }
        synchronized (this.mConfigureLock) {
            if (this.mConfiguring) {
                Log.e(TAG, "Cannot submit request, configuration change in progress.");
                return -38;
            }
            return this.mLegacyDevice.submitRequestList(request, streaming, lastFrameNumber);
        }
    }

    public int cancelRequest(int requestId, LongParcelable lastFrameNumber) {
        if (this.mLegacyDevice.isClosed()) {
            Log.e(TAG, "Cannot cancel request, device has been closed.");
            return -19;
        }
        synchronized (this.mConfigureLock) {
            if (this.mConfiguring) {
                Log.e(TAG, "Cannot cancel request, configuration change in progress.");
                return -38;
            }
            lastFrameNumber.setNumber(this.mLegacyDevice.cancelRequest(requestId));
            return 0;
        }
    }

    public int beginConfigure() {
        if (this.mLegacyDevice.isClosed()) {
            Log.e(TAG, "Cannot begin configure, device has been closed.");
            return -19;
        }
        synchronized (this.mConfigureLock) {
            if (this.mConfiguring) {
                Log.e(TAG, "Cannot begin configure, configuration change already in progress.");
                return -38;
            }
            this.mConfiguring = true;
            return 0;
        }
    }

    public int endConfigure(boolean isConstrainedHighSpeed) {
        Throwable th;
        if (this.mLegacyDevice.isClosed()) {
            Log.e(TAG, "Cannot end configure, device has been closed.");
            return -19;
        }
        ArrayList<Surface> surfaces = null;
        synchronized (this.mConfigureLock) {
            try {
                if (this.mConfiguring) {
                    int numSurfaces = this.mSurfaces.size();
                    if (numSurfaces > 0) {
                        ArrayList<Surface> surfaces2 = new ArrayList();
                        int i = 0;
                        while (i < numSurfaces) {
                            try {
                                surfaces2.add(this.mSurfaces.valueAt(i));
                                i++;
                            } catch (Throwable th2) {
                                th = th2;
                                surfaces = surfaces2;
                            }
                        }
                        surfaces = surfaces2;
                    }
                    this.mConfiguring = false;
                    return this.mLegacyDevice.configureOutputs(surfaces);
                }
                Log.e(TAG, "Cannot end configure, no configuration change in progress.");
                return -38;
            } catch (Throwable th3) {
                th = th3;
                throw th;
            }
        }
    }

    public int deleteStream(int streamId) {
        if (this.mLegacyDevice.isClosed()) {
            Log.e(TAG, "Cannot delete stream, device has been closed.");
            return -19;
        }
        synchronized (this.mConfigureLock) {
            if (this.mConfiguring) {
                int index = this.mSurfaces.indexOfKey(streamId);
                if (index < 0) {
                    Log.e(TAG, "Cannot delete stream, stream id " + streamId + " doesn't exist.");
                    return -22;
                }
                this.mSurfaces.removeAt(index);
                return 0;
            }
            Log.e(TAG, "Cannot delete stream, beginConfigure hasn't been called yet.");
            return -38;
        }
    }

    public int createStream(OutputConfiguration outputConfiguration) {
        if (this.mLegacyDevice.isClosed()) {
            Log.e(TAG, "Cannot create stream, device has been closed.");
            return -19;
        }
        synchronized (this.mConfigureLock) {
            if (!this.mConfiguring) {
                Log.e(TAG, "Cannot create stream, beginConfigure hasn't been called yet.");
                return -38;
            } else if (outputConfiguration.getRotation() != 0) {
                Log.e(TAG, "Cannot create stream, stream rotation is not supported.");
                return -38;
            } else {
                int id = this.mSurfaceIdCounter + 1;
                this.mSurfaceIdCounter = id;
                this.mSurfaces.put(id, outputConfiguration.getSurface());
                return id;
            }
        }
    }

    public int createInputStream(int width, int height, int format) {
        Log.e(TAG, "creating input stream is not supported on legacy devices");
        return -38;
    }

    public int getInputSurface(Surface surface) {
        Log.e(TAG, "getting input surface is not supported on legacy devices");
        return -38;
    }

    public int createDefaultRequest(int templateId, CameraMetadataNative request) {
        if (this.mLegacyDevice.isClosed()) {
            Log.e(TAG, "Cannot create default request, device has been closed.");
            return -19;
        }
        try {
            request.swap(LegacyMetadataMapper.createRequestTemplate(this.mCameraCharacteristics, templateId));
            return 0;
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "createDefaultRequest - invalid templateId specified");
            return -22;
        }
    }

    public int getCameraInfo(CameraMetadataNative info) {
        Log.e(TAG, "getCameraInfo unimplemented.");
        return 0;
    }

    public int waitUntilIdle() throws RemoteException {
        if (this.mLegacyDevice.isClosed()) {
            Log.e(TAG, "Cannot wait until idle, device has been closed.");
            return -19;
        }
        synchronized (this.mConfigureLock) {
            if (this.mConfiguring) {
                Log.e(TAG, "Cannot wait until idle, configuration change in progress.");
                return -38;
            }
            this.mLegacyDevice.waitUntilIdle();
            return 0;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int flush(android.hardware.camera2.utils.LongParcelable r6) {
        /*
        r5 = this;
        r2 = r5.mLegacyDevice;
        r2 = r2.isClosed();
        if (r2 == 0) goto L_0x0012;
    L_0x0008:
        r2 = "CameraDeviceUserShim";
        r3 = "Cannot flush, device has been closed.";
        android.util.Log.e(r2, r3);
        r2 = -19;
    L_0x0011:
        return r2;
    L_0x0012:
        r3 = r5.mConfigureLock;
        monitor-enter(r3);
        r2 = r5.mConfiguring;	 Catch:{ all -> 0x0024 }
        if (r2 == 0) goto L_0x0027;
    L_0x0019:
        r2 = "CameraDeviceUserShim";
        r4 = "Cannot flush, configuration change in progress.";
        android.util.Log.e(r2, r4);	 Catch:{ all -> 0x0024 }
        r2 = -38;
        monitor-exit(r3);	 Catch:{ all -> 0x0024 }
        goto L_0x0011;
    L_0x0024:
        r2 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x0024 }
        throw r2;
    L_0x0027:
        monitor-exit(r3);	 Catch:{ all -> 0x0024 }
        r2 = r5.mLegacyDevice;
        r0 = r2.flush();
        if (r6 == 0) goto L_0x0033;
    L_0x0030:
        r6.setNumber(r0);
    L_0x0033:
        r2 = 0;
        goto L_0x0011;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.hardware.camera2.legacy.CameraDeviceUserShim.flush(android.hardware.camera2.utils.LongParcelable):int");
    }

    public int prepare(int streamId) {
        if (this.mLegacyDevice.isClosed()) {
            Log.e(TAG, "Cannot prepare stream, device has been closed.");
            return -19;
        }
        this.mCameraCallbacks.onPrepared(streamId);
        return 0;
    }

    public int prepare2(int maxCount, int streamId) {
        return prepare(streamId);
    }

    public int tearDown(int streamId) {
        if (!this.mLegacyDevice.isClosed()) {
            return 0;
        }
        Log.e(TAG, "Cannot tear down stream, device has been closed.");
        return -19;
    }

    public IBinder asBinder() {
        return null;
    }
}
