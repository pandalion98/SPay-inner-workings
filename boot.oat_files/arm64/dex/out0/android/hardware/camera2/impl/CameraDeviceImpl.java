package android.hardware.camera2.impl;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraDevice.StateCallback;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureRequest.Builder;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.ICameraDeviceCallbacks.Stub;
import android.hardware.camera2.ICameraDeviceUser;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.InputConfiguration;
import android.hardware.camera2.params.OutputConfiguration;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.hardware.camera2.utils.CameraBinderDecorator;
import android.hardware.camera2.utils.CameraRuntimeException;
import android.hardware.camera2.utils.LongParcelable;
import android.hardware.camera2.utils.SurfaceUtils;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import android.util.Size;
import android.util.SparseArray;
import android.view.Surface;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class CameraDeviceImpl extends CameraDevice {
    private static final int REQUEST_ID_NONE = -1;
    private final boolean DEBUG = false;
    private final String TAG;
    private final Runnable mCallOnActive = new Runnable() {
        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            /*
            r3 = this;
            r0 = 0;
            r1 = android.hardware.camera2.impl.CameraDeviceImpl.this;
            r2 = r1.mInterfaceLock;
            monitor-enter(r2);
            r1 = android.hardware.camera2.impl.CameraDeviceImpl.this;	 Catch:{ all -> 0x001f }
            r1 = r1.mRemoteDevice;	 Catch:{ all -> 0x001f }
            if (r1 != 0) goto L_0x0010;
        L_0x000e:
            monitor-exit(r2);	 Catch:{ all -> 0x001f }
        L_0x000f:
            return;
        L_0x0010:
            r1 = android.hardware.camera2.impl.CameraDeviceImpl.this;	 Catch:{ all -> 0x001f }
            r0 = r1.mSessionStateCallback;	 Catch:{ all -> 0x001f }
            monitor-exit(r2);	 Catch:{ all -> 0x001f }
            if (r0 == 0) goto L_0x000f;
        L_0x0019:
            r1 = android.hardware.camera2.impl.CameraDeviceImpl.this;
            r0.onActive(r1);
            goto L_0x000f;
        L_0x001f:
            r1 = move-exception;
            monitor-exit(r2);	 Catch:{ all -> 0x001f }
            throw r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.hardware.camera2.impl.CameraDeviceImpl.3.run():void");
        }
    };
    private final Runnable mCallOnBusy = new Runnable() {
        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            /*
            r3 = this;
            r0 = 0;
            r1 = android.hardware.camera2.impl.CameraDeviceImpl.this;
            r2 = r1.mInterfaceLock;
            monitor-enter(r2);
            r1 = android.hardware.camera2.impl.CameraDeviceImpl.this;	 Catch:{ all -> 0x001f }
            r1 = r1.mRemoteDevice;	 Catch:{ all -> 0x001f }
            if (r1 != 0) goto L_0x0010;
        L_0x000e:
            monitor-exit(r2);	 Catch:{ all -> 0x001f }
        L_0x000f:
            return;
        L_0x0010:
            r1 = android.hardware.camera2.impl.CameraDeviceImpl.this;	 Catch:{ all -> 0x001f }
            r0 = r1.mSessionStateCallback;	 Catch:{ all -> 0x001f }
            monitor-exit(r2);	 Catch:{ all -> 0x001f }
            if (r0 == 0) goto L_0x000f;
        L_0x0019:
            r1 = android.hardware.camera2.impl.CameraDeviceImpl.this;
            r0.onBusy(r1);
            goto L_0x000f;
        L_0x001f:
            r1 = move-exception;
            monitor-exit(r2);	 Catch:{ all -> 0x001f }
            throw r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.hardware.camera2.impl.CameraDeviceImpl.4.run():void");
        }
    };
    private final Runnable mCallOnClosed = new Runnable() {
        private boolean mClosedOnce = false;

        public void run() {
            if (this.mClosedOnce) {
                throw new AssertionError("Don't post #onClosed more than once");
            }
            synchronized (CameraDeviceImpl.this.mInterfaceLock) {
                StateCallbackKK sessionCallback = CameraDeviceImpl.this.mSessionStateCallback;
            }
            if (sessionCallback != null) {
                sessionCallback.onClosed(CameraDeviceImpl.this);
            }
            CameraDeviceImpl.this.mDeviceCallback.onClosed(CameraDeviceImpl.this);
            this.mClosedOnce = true;
        }
    };
    private final Runnable mCallOnDisconnected = new Runnable() {
        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            /*
            r3 = this;
            r0 = 0;
            r1 = android.hardware.camera2.impl.CameraDeviceImpl.this;
            r2 = r1.mInterfaceLock;
            monitor-enter(r2);
            r1 = android.hardware.camera2.impl.CameraDeviceImpl.this;	 Catch:{ all -> 0x002a }
            r1 = r1.mRemoteDevice;	 Catch:{ all -> 0x002a }
            if (r1 != 0) goto L_0x0010;
        L_0x000e:
            monitor-exit(r2);	 Catch:{ all -> 0x002a }
        L_0x000f:
            return;
        L_0x0010:
            r1 = android.hardware.camera2.impl.CameraDeviceImpl.this;	 Catch:{ all -> 0x002a }
            r0 = r1.mSessionStateCallback;	 Catch:{ all -> 0x002a }
            monitor-exit(r2);	 Catch:{ all -> 0x002a }
            if (r0 == 0) goto L_0x001e;
        L_0x0019:
            r1 = android.hardware.camera2.impl.CameraDeviceImpl.this;
            r0.onDisconnected(r1);
        L_0x001e:
            r1 = android.hardware.camera2.impl.CameraDeviceImpl.this;
            r1 = r1.mDeviceCallback;
            r2 = android.hardware.camera2.impl.CameraDeviceImpl.this;
            r1.onDisconnected(r2);
            goto L_0x000f;
        L_0x002a:
            r1 = move-exception;
            monitor-exit(r2);	 Catch:{ all -> 0x002a }
            throw r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.hardware.camera2.impl.CameraDeviceImpl.7.run():void");
        }
    };
    private final Runnable mCallOnIdle = new Runnable() {
        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            /*
            r3 = this;
            r0 = 0;
            r1 = android.hardware.camera2.impl.CameraDeviceImpl.this;
            r2 = r1.mInterfaceLock;
            monitor-enter(r2);
            r1 = android.hardware.camera2.impl.CameraDeviceImpl.this;	 Catch:{ all -> 0x001f }
            r1 = r1.mRemoteDevice;	 Catch:{ all -> 0x001f }
            if (r1 != 0) goto L_0x0010;
        L_0x000e:
            monitor-exit(r2);	 Catch:{ all -> 0x001f }
        L_0x000f:
            return;
        L_0x0010:
            r1 = android.hardware.camera2.impl.CameraDeviceImpl.this;	 Catch:{ all -> 0x001f }
            r0 = r1.mSessionStateCallback;	 Catch:{ all -> 0x001f }
            monitor-exit(r2);	 Catch:{ all -> 0x001f }
            if (r0 == 0) goto L_0x000f;
        L_0x0019:
            r1 = android.hardware.camera2.impl.CameraDeviceImpl.this;
            r0.onIdle(r1);
            goto L_0x000f;
        L_0x001f:
            r1 = move-exception;
            monitor-exit(r2);	 Catch:{ all -> 0x001f }
            throw r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.hardware.camera2.impl.CameraDeviceImpl.6.run():void");
        }
    };
    private final Runnable mCallOnOpened = new Runnable() {
        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            /*
            r3 = this;
            r0 = 0;
            r1 = android.hardware.camera2.impl.CameraDeviceImpl.this;
            r2 = r1.mInterfaceLock;
            monitor-enter(r2);
            r1 = android.hardware.camera2.impl.CameraDeviceImpl.this;	 Catch:{ all -> 0x002a }
            r1 = r1.mRemoteDevice;	 Catch:{ all -> 0x002a }
            if (r1 != 0) goto L_0x0010;
        L_0x000e:
            monitor-exit(r2);	 Catch:{ all -> 0x002a }
        L_0x000f:
            return;
        L_0x0010:
            r1 = android.hardware.camera2.impl.CameraDeviceImpl.this;	 Catch:{ all -> 0x002a }
            r0 = r1.mSessionStateCallback;	 Catch:{ all -> 0x002a }
            monitor-exit(r2);	 Catch:{ all -> 0x002a }
            if (r0 == 0) goto L_0x001e;
        L_0x0019:
            r1 = android.hardware.camera2.impl.CameraDeviceImpl.this;
            r0.onOpened(r1);
        L_0x001e:
            r1 = android.hardware.camera2.impl.CameraDeviceImpl.this;
            r1 = r1.mDeviceCallback;
            r2 = android.hardware.camera2.impl.CameraDeviceImpl.this;
            r1.onOpened(r2);
            goto L_0x000f;
        L_0x002a:
            r1 = move-exception;
            monitor-exit(r2);	 Catch:{ all -> 0x002a }
            throw r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.hardware.camera2.impl.CameraDeviceImpl.1.run():void");
        }
    };
    private final Runnable mCallOnUnconfigured = new Runnable() {
        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            /*
            r3 = this;
            r0 = 0;
            r1 = android.hardware.camera2.impl.CameraDeviceImpl.this;
            r2 = r1.mInterfaceLock;
            monitor-enter(r2);
            r1 = android.hardware.camera2.impl.CameraDeviceImpl.this;	 Catch:{ all -> 0x001f }
            r1 = r1.mRemoteDevice;	 Catch:{ all -> 0x001f }
            if (r1 != 0) goto L_0x0010;
        L_0x000e:
            monitor-exit(r2);	 Catch:{ all -> 0x001f }
        L_0x000f:
            return;
        L_0x0010:
            r1 = android.hardware.camera2.impl.CameraDeviceImpl.this;	 Catch:{ all -> 0x001f }
            r0 = r1.mSessionStateCallback;	 Catch:{ all -> 0x001f }
            monitor-exit(r2);	 Catch:{ all -> 0x001f }
            if (r0 == 0) goto L_0x000f;
        L_0x0019:
            r1 = android.hardware.camera2.impl.CameraDeviceImpl.this;
            r0.onUnconfigured(r1);
            goto L_0x000f;
        L_0x001f:
            r1 = move-exception;
            monitor-exit(r2);	 Catch:{ all -> 0x001f }
            throw r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.hardware.camera2.impl.CameraDeviceImpl.2.run():void");
        }
    };
    private final CameraDeviceCallbacks mCallbacks = new CameraDeviceCallbacks();
    private final String mCameraId;
    private final SparseArray<CaptureCallbackHolder> mCaptureCallbackMap = new SparseArray();
    private final CameraCharacteristics mCharacteristics;
    private final AtomicBoolean mClosing = new AtomicBoolean();
    private SimpleEntry<Integer, InputConfiguration> mConfiguredInput = new SimpleEntry(Integer.valueOf(-1), null);
    private final SparseArray<OutputConfiguration> mConfiguredOutputs = new SparseArray();
    private CameraCaptureSessionCore mCurrentSession;
    private final StateCallback mDeviceCallback;
    private final Handler mDeviceHandler;
    private final FrameNumberTracker mFrameNumberTracker = new FrameNumberTracker();
    private boolean mIdle = true;
    private boolean mInError = false;
    final Object mInterfaceLock = new Object();
    private int mNextSessionId = 0;
    private ICameraDeviceUser mRemoteDevice;
    private int mRepeatingRequestId = -1;
    private final ArrayList<Integer> mRepeatingRequestIdDeletedList = new ArrayList();
    private final List<RequestLastFrameNumbersHolder> mRequestLastFrameNumbersList = new ArrayList();
    private volatile StateCallbackKK mSessionStateCallback;
    private final int mTotalPartialCount;

    public static abstract class CaptureCallback {
        public static final int NO_FRAMES_CAPTURED = -1;

        public void onCaptureStarted(CameraDevice camera, CaptureRequest request, long timestamp, long frameNumber) {
        }

        public void onCapturePartial(CameraDevice camera, CaptureRequest request, CaptureResult result) {
        }

        public void onCaptureProgressed(CameraDevice camera, CaptureRequest request, CaptureResult partialResult) {
        }

        public void onCaptureCompleted(CameraDevice camera, CaptureRequest request, TotalCaptureResult result) {
        }

        public void onCaptureFailed(CameraDevice camera, CaptureRequest request, CaptureFailure failure) {
        }

        public void onCaptureSequenceCompleted(CameraDevice camera, int sequenceId, long frameNumber) {
        }

        public void onCaptureSequenceAborted(CameraDevice camera, int sequenceId) {
        }
    }

    public static abstract class StateCallbackKK extends StateCallback {
        public void onUnconfigured(CameraDevice camera) {
        }

        public void onActive(CameraDevice camera) {
        }

        public void onBusy(CameraDevice camera) {
        }

        public void onIdle(CameraDevice camera) {
        }

        public void onSurfacePrepared(Surface surface) {
        }
    }

    public class CameraDeviceCallbacks extends Stub {
        public static final int ERROR_CAMERA_BUFFER = 5;
        public static final int ERROR_CAMERA_DEVICE = 1;
        public static final int ERROR_CAMERA_DISCONNECTED = 0;
        public static final int ERROR_CAMERA_REQUEST = 3;
        public static final int ERROR_CAMERA_RESULT = 4;
        public static final int ERROR_CAMERA_SERVICE = 2;

        public IBinder asBinder() {
            return this;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onDeviceError(final int r6, android.hardware.camera2.impl.CaptureResultExtras r7) {
            /*
            r5 = this;
            r1 = android.hardware.camera2.impl.CameraDeviceImpl.this;
            r2 = r1.mInterfaceLock;
            monitor-enter(r2);
            r1 = android.hardware.camera2.impl.CameraDeviceImpl.this;	 Catch:{ all -> 0x0044 }
            r1 = r1.mRemoteDevice;	 Catch:{ all -> 0x0044 }
            if (r1 != 0) goto L_0x000f;
        L_0x000d:
            monitor-exit(r2);	 Catch:{ all -> 0x0044 }
        L_0x000e:
            return;
        L_0x000f:
            switch(r6) {
                case 0: goto L_0x0047;
                case 1: goto L_0x002e;
                case 2: goto L_0x002e;
                case 3: goto L_0x0057;
                case 4: goto L_0x0057;
                case 5: goto L_0x0057;
                default: goto L_0x0012;
            };	 Catch:{ all -> 0x0044 }
        L_0x0012:
            r1 = android.hardware.camera2.impl.CameraDeviceImpl.this;	 Catch:{ all -> 0x0044 }
            r1 = r1.TAG;	 Catch:{ all -> 0x0044 }
            r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0044 }
            r3.<init>();	 Catch:{ all -> 0x0044 }
            r4 = "Unknown error from camera device: ";
            r3 = r3.append(r4);	 Catch:{ all -> 0x0044 }
            r3 = r3.append(r6);	 Catch:{ all -> 0x0044 }
            r3 = r3.toString();	 Catch:{ all -> 0x0044 }
            android.util.Log.e(r1, r3);	 Catch:{ all -> 0x0044 }
        L_0x002e:
            r1 = android.hardware.camera2.impl.CameraDeviceImpl.this;	 Catch:{ all -> 0x0044 }
            r3 = 1;
            r1.mInError = r3;	 Catch:{ all -> 0x0044 }
            r0 = new android.hardware.camera2.impl.CameraDeviceImpl$CameraDeviceCallbacks$1;	 Catch:{ all -> 0x0044 }
            r0.<init>(r6);	 Catch:{ all -> 0x0044 }
            r1 = android.hardware.camera2.impl.CameraDeviceImpl.this;	 Catch:{ all -> 0x0044 }
            r1 = r1.mDeviceHandler;	 Catch:{ all -> 0x0044 }
            r1.post(r0);	 Catch:{ all -> 0x0044 }
        L_0x0042:
            monitor-exit(r2);	 Catch:{ all -> 0x0044 }
            goto L_0x000e;
        L_0x0044:
            r1 = move-exception;
            monitor-exit(r2);	 Catch:{ all -> 0x0044 }
            throw r1;
        L_0x0047:
            r1 = android.hardware.camera2.impl.CameraDeviceImpl.this;	 Catch:{ all -> 0x0044 }
            r1 = r1.mDeviceHandler;	 Catch:{ all -> 0x0044 }
            r3 = android.hardware.camera2.impl.CameraDeviceImpl.this;	 Catch:{ all -> 0x0044 }
            r3 = r3.mCallOnDisconnected;	 Catch:{ all -> 0x0044 }
            r1.post(r3);	 Catch:{ all -> 0x0044 }
            goto L_0x0042;
        L_0x0057:
            r5.onCaptureErrorLocked(r6, r7);	 Catch:{ all -> 0x0044 }
            goto L_0x0042;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.hardware.camera2.impl.CameraDeviceImpl.CameraDeviceCallbacks.onDeviceError(int, android.hardware.camera2.impl.CaptureResultExtras):void");
        }

        public void onDeviceIdle() {
            synchronized (CameraDeviceImpl.this.mInterfaceLock) {
                if (CameraDeviceImpl.this.mRemoteDevice == null) {
                    return;
                }
                if (!CameraDeviceImpl.this.mIdle) {
                    CameraDeviceImpl.this.mDeviceHandler.post(CameraDeviceImpl.this.mCallOnIdle);
                }
                CameraDeviceImpl.this.mIdle = true;
            }
        }

        public void onCaptureStarted(CaptureResultExtras resultExtras, long timestamp) {
            int requestId = resultExtras.getRequestId();
            final long frameNumber = resultExtras.getFrameNumber();
            synchronized (CameraDeviceImpl.this.mInterfaceLock) {
                if (CameraDeviceImpl.this.mRemoteDevice == null) {
                    return;
                }
                final CaptureCallbackHolder holder = (CaptureCallbackHolder) CameraDeviceImpl.this.mCaptureCallbackMap.get(requestId);
                if (holder == null) {
                } else if (CameraDeviceImpl.this.isClosed()) {
                } else {
                    final CaptureResultExtras captureResultExtras = resultExtras;
                    final long j = timestamp;
                    holder.getHandler().post(new Runnable() {
                        public void run() {
                            if (!CameraDeviceImpl.this.isClosed()) {
                                holder.getCallback().onCaptureStarted(CameraDeviceImpl.this, holder.getRequest(captureResultExtras.getSubsequenceId()), j, frameNumber);
                            }
                        }
                    });
                }
            }
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onResultReceived(android.hardware.camera2.impl.CameraMetadataNative r19, android.hardware.camera2.impl.CaptureResultExtras r20) throws android.os.RemoteException {
            /*
            r18 = this;
            r15 = r20.getRequestId();
            r4 = r20.getFrameNumber();
            r0 = r18;
            r3 = android.hardware.camera2.impl.CameraDeviceImpl.this;
            r0 = r3.mInterfaceLock;
            r17 = r0;
            monitor-enter(r17);
            r0 = r18;
            r3 = android.hardware.camera2.impl.CameraDeviceImpl.this;	 Catch:{ all -> 0x006b }
            r3 = r3.mRemoteDevice;	 Catch:{ all -> 0x006b }
            if (r3 != 0) goto L_0x001d;
        L_0x001b:
            monitor-exit(r17);	 Catch:{ all -> 0x006b }
        L_0x001c:
            return;
        L_0x001d:
            r3 = android.hardware.camera2.CameraCharacteristics.LENS_INFO_SHADING_MAP_SIZE;	 Catch:{ all -> 0x006b }
            r0 = r18;
            r10 = android.hardware.camera2.impl.CameraDeviceImpl.this;	 Catch:{ all -> 0x006b }
            r10 = r10.getCharacteristics();	 Catch:{ all -> 0x006b }
            r12 = android.hardware.camera2.CameraCharacteristics.LENS_INFO_SHADING_MAP_SIZE;	 Catch:{ all -> 0x006b }
            r10 = r10.get(r12);	 Catch:{ all -> 0x006b }
            r0 = r19;
            r0.set(r3, r10);	 Catch:{ all -> 0x006b }
            r0 = r18;
            r3 = android.hardware.camera2.impl.CameraDeviceImpl.this;	 Catch:{ all -> 0x006b }
            r3 = r3.mCaptureCallbackMap;	 Catch:{ all -> 0x006b }
            r2 = r3.get(r15);	 Catch:{ all -> 0x006b }
            r2 = (android.hardware.camera2.impl.CameraDeviceImpl.CaptureCallbackHolder) r2;	 Catch:{ all -> 0x006b }
            r3 = r20.getSubsequenceId();	 Catch:{ all -> 0x006b }
            r11 = r2.getRequest(r3);	 Catch:{ all -> 0x006b }
            r3 = r20.getPartialResultCount();	 Catch:{ all -> 0x006b }
            r0 = r18;
            r10 = android.hardware.camera2.impl.CameraDeviceImpl.this;	 Catch:{ all -> 0x006b }
            r10 = r10.mTotalPartialCount;	 Catch:{ all -> 0x006b }
            if (r3 >= r10) goto L_0x006e;
        L_0x0056:
            r7 = 1;
        L_0x0057:
            r8 = r11.isReprocess();	 Catch:{ all -> 0x006b }
            if (r2 != 0) goto L_0x0070;
        L_0x005d:
            r0 = r18;
            r3 = android.hardware.camera2.impl.CameraDeviceImpl.this;	 Catch:{ all -> 0x006b }
            r3 = r3.mFrameNumberTracker;	 Catch:{ all -> 0x006b }
            r6 = 0;
            r3.updateTracker(r4, r6, r7, r8);	 Catch:{ all -> 0x006b }
            monitor-exit(r17);	 Catch:{ all -> 0x006b }
            goto L_0x001c;
        L_0x006b:
            r3 = move-exception;
            monitor-exit(r17);	 Catch:{ all -> 0x006b }
            throw r3;
        L_0x006e:
            r7 = 0;
            goto L_0x0057;
        L_0x0070:
            r0 = r18;
            r3 = android.hardware.camera2.impl.CameraDeviceImpl.this;	 Catch:{ all -> 0x006b }
            r3 = r3.isClosed();	 Catch:{ all -> 0x006b }
            if (r3 == 0) goto L_0x0088;
        L_0x007a:
            r0 = r18;
            r3 = android.hardware.camera2.impl.CameraDeviceImpl.this;	 Catch:{ all -> 0x006b }
            r3 = r3.mFrameNumberTracker;	 Catch:{ all -> 0x006b }
            r6 = 0;
            r3.updateTracker(r4, r6, r7, r8);	 Catch:{ all -> 0x006b }
            monitor-exit(r17);	 Catch:{ all -> 0x006b }
            goto L_0x001c;
        L_0x0088:
            r16 = 0;
            if (r7 == 0) goto L_0x00bf;
        L_0x008c:
            r9 = new android.hardware.camera2.CaptureResult;	 Catch:{ all -> 0x006b }
            r0 = r19;
            r1 = r20;
            r9.<init>(r0, r11, r1);	 Catch:{ all -> 0x006b }
            r16 = new android.hardware.camera2.impl.CameraDeviceImpl$CameraDeviceCallbacks$3;	 Catch:{ all -> 0x006b }
            r0 = r16;
            r1 = r18;
            r0.<init>(r2, r11, r9);	 Catch:{ all -> 0x006b }
            r6 = r9;
        L_0x009f:
            r3 = r2.getHandler();	 Catch:{ all -> 0x006b }
            r0 = r16;
            r3.post(r0);	 Catch:{ all -> 0x006b }
            r0 = r18;
            r3 = android.hardware.camera2.impl.CameraDeviceImpl.this;	 Catch:{ all -> 0x006b }
            r3 = r3.mFrameNumberTracker;	 Catch:{ all -> 0x006b }
            r3.updateTracker(r4, r6, r7, r8);	 Catch:{ all -> 0x006b }
            if (r7 != 0) goto L_0x00bc;
        L_0x00b5:
            r0 = r18;
            r3 = android.hardware.camera2.impl.CameraDeviceImpl.this;	 Catch:{ all -> 0x006b }
            r3.checkAndFireSequenceComplete();	 Catch:{ all -> 0x006b }
        L_0x00bc:
            monitor-exit(r17);	 Catch:{ all -> 0x006b }
            goto L_0x001c;
        L_0x00bf:
            r0 = r18;
            r3 = android.hardware.camera2.impl.CameraDeviceImpl.this;	 Catch:{ all -> 0x006b }
            r3 = r3.mFrameNumberTracker;	 Catch:{ all -> 0x006b }
            r13 = r3.popPartialResults(r4);	 Catch:{ all -> 0x006b }
            r9 = new android.hardware.camera2.TotalCaptureResult;	 Catch:{ all -> 0x006b }
            r14 = r2.getSessionId();	 Catch:{ all -> 0x006b }
            r10 = r19;
            r12 = r20;
            r9.<init>(r10, r11, r12, r13, r14);	 Catch:{ all -> 0x006b }
            r16 = new android.hardware.camera2.impl.CameraDeviceImpl$CameraDeviceCallbacks$4;	 Catch:{ all -> 0x006b }
            r0 = r16;
            r1 = r18;
            r0.<init>(r2, r11, r9);	 Catch:{ all -> 0x006b }
            r6 = r9;
            goto L_0x009f;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.hardware.camera2.impl.CameraDeviceImpl.CameraDeviceCallbacks.onResultReceived(android.hardware.camera2.impl.CameraMetadataNative, android.hardware.camera2.impl.CaptureResultExtras):void");
        }

        public void onPrepared(int streamId) {
            synchronized (CameraDeviceImpl.this.mInterfaceLock) {
                OutputConfiguration output = (OutputConfiguration) CameraDeviceImpl.this.mConfiguredOutputs.get(streamId);
                StateCallbackKK sessionCallback = CameraDeviceImpl.this.mSessionStateCallback;
            }
            if (sessionCallback != null) {
                if (output == null) {
                    Log.w(CameraDeviceImpl.this.TAG, "onPrepared invoked for unknown output Surface");
                } else {
                    sessionCallback.onSurfacePrepared(output.getSurface());
                }
            }
        }

        private void onCaptureErrorLocked(int errorCode, CaptureResultExtras resultExtras) {
            int requestId = resultExtras.getRequestId();
            int subsequenceId = resultExtras.getSubsequenceId();
            long frameNumber = resultExtras.getFrameNumber();
            final CaptureCallbackHolder holder = (CaptureCallbackHolder) CameraDeviceImpl.this.mCaptureCallbackMap.get(requestId);
            final CaptureRequest request = holder.getRequest(subsequenceId);
            if (errorCode == 5) {
                Log.e(CameraDeviceImpl.this.TAG, String.format("Lost output buffer reported for frame %d", new Object[]{Long.valueOf(frameNumber)}));
                return;
            }
            boolean mayHaveBuffers = errorCode == 4;
            int reason = (CameraDeviceImpl.this.mCurrentSession == null || !CameraDeviceImpl.this.mCurrentSession.isAborting()) ? 0 : 1;
            final CaptureFailure failure = new CaptureFailure(request, reason, mayHaveBuffers, requestId, frameNumber);
            holder.getHandler().post(new Runnable() {
                public void run() {
                    if (!CameraDeviceImpl.this.isClosed()) {
                        holder.getCallback().onCaptureFailed(CameraDeviceImpl.this, request, failure);
                    }
                }
            });
            CameraDeviceImpl.this.mFrameNumberTracker.updateTracker(frameNumber, true, request.isReprocess());
            CameraDeviceImpl.this.checkAndFireSequenceComplete();
        }
    }

    static class CaptureCallbackHolder {
        private final CaptureCallback mCallback;
        private final Handler mHandler;
        private final boolean mRepeating;
        private final List<CaptureRequest> mRequestList;
        private final int mSessionId;

        CaptureCallbackHolder(CaptureCallback callback, List<CaptureRequest> requestList, Handler handler, boolean repeating, int sessionId) {
            if (callback == null || handler == null) {
                throw new UnsupportedOperationException("Must have a valid handler and a valid callback");
            }
            this.mRepeating = repeating;
            this.mHandler = handler;
            this.mRequestList = new ArrayList(requestList);
            this.mCallback = callback;
            this.mSessionId = sessionId;
        }

        public boolean isRepeating() {
            return this.mRepeating;
        }

        public CaptureCallback getCallback() {
            return this.mCallback;
        }

        public CaptureRequest getRequest(int subsequenceId) {
            if (subsequenceId >= this.mRequestList.size()) {
                throw new IllegalArgumentException(String.format("Requested subsequenceId %d is larger than request list size %d.", new Object[]{Integer.valueOf(subsequenceId), Integer.valueOf(this.mRequestList.size())}));
            } else if (subsequenceId >= 0) {
                return (CaptureRequest) this.mRequestList.get(subsequenceId);
            } else {
                throw new IllegalArgumentException(String.format("Requested subsequenceId %d is negative", new Object[]{Integer.valueOf(subsequenceId)}));
            }
        }

        public CaptureRequest getRequest() {
            return getRequest(0);
        }

        public Handler getHandler() {
            return this.mHandler;
        }

        public int getSessionId() {
            return this.mSessionId;
        }
    }

    public class FrameNumberTracker {
        private long mCompletedFrameNumber = -1;
        private long mCompletedReprocessFrameNumber = -1;
        private final TreeMap<Long, Boolean> mFutureErrorMap = new TreeMap();
        private final HashMap<Long, List<CaptureResult>> mPartialResults = new HashMap();
        private final LinkedList<Long> mSkippedRegularFrameNumbers = new LinkedList();
        private final LinkedList<Long> mSkippedReprocessFrameNumbers = new LinkedList();

        private void update() {
            Iterator iter = this.mFutureErrorMap.entrySet().iterator();
            while (iter.hasNext()) {
                Entry pair = (Entry) iter.next();
                Long errorFrameNumber = (Long) pair.getKey();
                Boolean reprocess = (Boolean) pair.getValue();
                Boolean removeError = Boolean.valueOf(true);
                if (reprocess.booleanValue()) {
                    if (errorFrameNumber.longValue() == this.mCompletedReprocessFrameNumber + 1) {
                        this.mCompletedReprocessFrameNumber = errorFrameNumber.longValue();
                    } else if (this.mSkippedReprocessFrameNumbers.isEmpty() || errorFrameNumber != this.mSkippedReprocessFrameNumbers.element()) {
                        removeError = Boolean.valueOf(false);
                    } else {
                        this.mCompletedReprocessFrameNumber = errorFrameNumber.longValue();
                        this.mSkippedReprocessFrameNumbers.remove();
                    }
                } else if (errorFrameNumber.longValue() == this.mCompletedFrameNumber + 1) {
                    this.mCompletedFrameNumber = errorFrameNumber.longValue();
                } else if (this.mSkippedRegularFrameNumbers.isEmpty() || errorFrameNumber != this.mSkippedRegularFrameNumbers.element()) {
                    removeError = Boolean.valueOf(false);
                } else {
                    this.mCompletedFrameNumber = errorFrameNumber.longValue();
                    this.mSkippedRegularFrameNumbers.remove();
                }
                if (removeError.booleanValue()) {
                    iter.remove();
                }
            }
        }

        public void updateTracker(long frameNumber, boolean isError, boolean isReprocess) {
            if (isError) {
                this.mFutureErrorMap.put(Long.valueOf(frameNumber), Boolean.valueOf(isReprocess));
            } else if (isReprocess) {
                try {
                    updateCompletedReprocessFrameNumber(frameNumber);
                } catch (IllegalArgumentException e) {
                    Log.e(CameraDeviceImpl.this.TAG, e.getMessage());
                }
            } else {
                updateCompletedFrameNumber(frameNumber);
            }
            update();
        }

        public void updateTracker(long frameNumber, CaptureResult result, boolean partial, boolean isReprocess) {
            if (!partial) {
                updateTracker(frameNumber, false, isReprocess);
            } else if (result != null) {
                List<CaptureResult> partials = (List) this.mPartialResults.get(Long.valueOf(frameNumber));
                if (partials == null) {
                    partials = new ArrayList();
                    this.mPartialResults.put(Long.valueOf(frameNumber), partials);
                }
                partials.add(result);
            }
        }

        public List<CaptureResult> popPartialResults(long frameNumber) {
            return (List) this.mPartialResults.remove(Long.valueOf(frameNumber));
        }

        public long getCompletedFrameNumber() {
            return this.mCompletedFrameNumber;
        }

        public long getCompletedReprocessFrameNumber() {
            return this.mCompletedReprocessFrameNumber;
        }

        private void updateCompletedFrameNumber(long frameNumber) throws IllegalArgumentException {
            if (frameNumber <= this.mCompletedFrameNumber) {
                throw new IllegalArgumentException("frame number " + frameNumber + " is a repeat");
            }
            if (frameNumber > this.mCompletedReprocessFrameNumber) {
                for (long i = Math.max(this.mCompletedFrameNumber, this.mCompletedReprocessFrameNumber) + 1; i < frameNumber; i++) {
                    this.mSkippedReprocessFrameNumbers.add(Long.valueOf(i));
                }
            } else if (this.mSkippedRegularFrameNumbers.isEmpty() || frameNumber < ((Long) this.mSkippedRegularFrameNumbers.element()).longValue()) {
                throw new IllegalArgumentException("frame number " + frameNumber + " is a repeat");
            } else if (frameNumber > ((Long) this.mSkippedRegularFrameNumbers.element()).longValue()) {
                throw new IllegalArgumentException("frame number " + frameNumber + " comes out of order. Expecting " + this.mSkippedRegularFrameNumbers.element());
            } else {
                this.mSkippedRegularFrameNumbers.remove();
            }
            this.mCompletedFrameNumber = frameNumber;
        }

        private void updateCompletedReprocessFrameNumber(long frameNumber) throws IllegalArgumentException {
            if (frameNumber < this.mCompletedReprocessFrameNumber) {
                throw new IllegalArgumentException("frame number " + frameNumber + " is a repeat");
            }
            if (frameNumber >= this.mCompletedFrameNumber) {
                for (long i = Math.max(this.mCompletedFrameNumber, this.mCompletedReprocessFrameNumber) + 1; i < frameNumber; i++) {
                    this.mSkippedRegularFrameNumbers.add(Long.valueOf(i));
                }
            } else if (this.mSkippedReprocessFrameNumbers.isEmpty() || frameNumber < ((Long) this.mSkippedReprocessFrameNumbers.element()).longValue()) {
                throw new IllegalArgumentException("frame number " + frameNumber + " is a repeat");
            } else if (frameNumber > ((Long) this.mSkippedReprocessFrameNumbers.element()).longValue()) {
                throw new IllegalArgumentException("frame number " + frameNumber + " comes out of order. Expecting " + this.mSkippedReprocessFrameNumbers.element());
            } else {
                this.mSkippedReprocessFrameNumbers.remove();
            }
            this.mCompletedReprocessFrameNumber = frameNumber;
        }
    }

    static class RequestLastFrameNumbersHolder {
        private final long mLastRegularFrameNumber;
        private final long mLastReprocessFrameNumber;
        private final int mRequestId;

        public RequestLastFrameNumbersHolder(List<CaptureRequest> requestList, int requestId, long lastFrameNumber) {
            long lastRegularFrameNumber = -1;
            long lastReprocessFrameNumber = -1;
            long frameNumber = lastFrameNumber;
            if (lastFrameNumber < ((long) (requestList.size() - 1))) {
                throw new IllegalArgumentException("lastFrameNumber: " + lastFrameNumber + " should be at least " + (requestList.size() - 1) + " for the number of " + " requests in the list: " + requestList.size());
            }
            for (int i = requestList.size() - 1; i >= 0; i--) {
                CaptureRequest request = (CaptureRequest) requestList.get(i);
                if (request.isReprocess() && lastReprocessFrameNumber == -1) {
                    lastReprocessFrameNumber = frameNumber;
                } else if (!request.isReprocess() && lastRegularFrameNumber == -1) {
                    lastRegularFrameNumber = frameNumber;
                }
                if (lastReprocessFrameNumber != -1 && lastRegularFrameNumber != -1) {
                    break;
                }
                frameNumber--;
            }
            this.mLastRegularFrameNumber = lastRegularFrameNumber;
            this.mLastReprocessFrameNumber = lastReprocessFrameNumber;
            this.mRequestId = requestId;
        }

        public RequestLastFrameNumbersHolder(int requestId, long lastRegularFrameNumber) {
            this.mLastRegularFrameNumber = lastRegularFrameNumber;
            this.mLastReprocessFrameNumber = -1;
            this.mRequestId = requestId;
        }

        public long getLastRegularFrameNumber() {
            return this.mLastRegularFrameNumber;
        }

        public long getLastReprocessFrameNumber() {
            return this.mLastReprocessFrameNumber;
        }

        public long getLastFrameNumber() {
            return Math.max(this.mLastRegularFrameNumber, this.mLastReprocessFrameNumber);
        }

        public int getRequestId() {
            return this.mRequestId;
        }
    }

    public CameraDeviceImpl(String cameraId, StateCallback callback, Handler handler, CameraCharacteristics characteristics) {
        if (cameraId == null || callback == null || handler == null || characteristics == null) {
            throw new IllegalArgumentException("Null argument given");
        }
        this.mCameraId = cameraId;
        this.mDeviceCallback = callback;
        this.mDeviceHandler = handler;
        this.mCharacteristics = characteristics;
        String tag = String.format("CameraDevice-JV-%s", new Object[]{this.mCameraId});
        if (tag.length() > 23) {
            tag = tag.substring(0, 23);
        }
        this.TAG = tag;
        Integer partialCount = (Integer) this.mCharacteristics.get(CameraCharacteristics.REQUEST_PARTIAL_RESULT_COUNT);
        if (partialCount == null) {
            this.mTotalPartialCount = 1;
        } else {
            this.mTotalPartialCount = partialCount.intValue();
        }
    }

    public CameraDeviceCallbacks getCallbacks() {
        return this.mCallbacks;
    }

    public void setRemoteDevice(ICameraDeviceUser remoteDevice) {
        synchronized (this.mInterfaceLock) {
            if (this.mInError) {
                return;
            }
            this.mRemoteDevice = (ICameraDeviceUser) CameraBinderDecorator.newInstance(remoteDevice);
            this.mDeviceHandler.post(this.mCallOnOpened);
            this.mDeviceHandler.post(this.mCallOnUnconfigured);
        }
    }

    public void setRemoteFailure(CameraRuntimeException failure) {
        int failureCode = 4;
        boolean failureIsError = true;
        switch (failure.getReason()) {
            case 1:
                failureCode = 3;
                break;
            case 2:
                failureIsError = false;
                break;
            case 3:
                failureCode = 4;
                break;
            case 4:
                failureCode = 1;
                break;
            case 5:
                failureCode = 2;
                break;
            default:
                Log.wtf(this.TAG, "Unknown failure in opening camera device: " + failure.getReason());
                break;
        }
        final int code = failureCode;
        final boolean isError = failureIsError;
        synchronized (this.mInterfaceLock) {
            this.mInError = true;
            this.mDeviceHandler.post(new Runnable() {
                public void run() {
                    if (isError) {
                        CameraDeviceImpl.this.mDeviceCallback.onError(CameraDeviceImpl.this, code);
                    } else {
                        CameraDeviceImpl.this.mDeviceCallback.onDisconnected(CameraDeviceImpl.this);
                    }
                }
            });
        }
    }

    public String getId() {
        return this.mCameraId;
    }

    public void configureOutputs(List<Surface> outputs) throws CameraAccessException {
        ArrayList<OutputConfiguration> outputConfigs = new ArrayList(outputs.size());
        for (Surface s : outputs) {
            outputConfigs.add(new OutputConfiguration(s));
        }
        configureStreamsChecked(null, outputConfigs, false);
    }

    public boolean configureStreamsChecked(InputConfiguration inputConfig, List<OutputConfiguration> outputs, boolean isConstrainedHighSpeed) throws CameraAccessException {
        if (outputs == null) {
            outputs = new ArrayList();
        }
        if (outputs.size() != 0 || inputConfig == null) {
            checkInputConfiguration(inputConfig);
            synchronized (this.mInterfaceLock) {
                OutputConfiguration outConfig;
                checkIfCameraClosedOrInError();
                HashSet<OutputConfiguration> addSet = new HashSet(outputs);
                List<Integer> deleteList = new ArrayList();
                for (int i = 0; i < this.mConfiguredOutputs.size(); i++) {
                    int streamId = this.mConfiguredOutputs.keyAt(i);
                    outConfig = (OutputConfiguration) this.mConfiguredOutputs.valueAt(i);
                    if (outputs.contains(outConfig)) {
                        addSet.remove(outConfig);
                    } else {
                        deleteList.add(Integer.valueOf(streamId));
                    }
                }
                this.mDeviceHandler.post(this.mCallOnBusy);
                stopRepeating();
                try {
                    waitUntilIdle();
                    this.mRemoteDevice.beginConfigure();
                    InputConfiguration currentInputConfig = (InputConfiguration) this.mConfiguredInput.getValue();
                    if (inputConfig != currentInputConfig && (inputConfig == null || !inputConfig.equals(currentInputConfig))) {
                        if (currentInputConfig != null) {
                            this.mRemoteDevice.deleteStream(((Integer) this.mConfiguredInput.getKey()).intValue());
                            this.mConfiguredInput = new SimpleEntry(Integer.valueOf(-1), null);
                        }
                        if (inputConfig != null) {
                            this.mConfiguredInput = new SimpleEntry(Integer.valueOf(this.mRemoteDevice.createInputStream(inputConfig.getWidth(), inputConfig.getHeight(), inputConfig.getFormat())), inputConfig);
                        }
                    }
                    for (Integer streamId2 : deleteList) {
                        this.mRemoteDevice.deleteStream(streamId2.intValue());
                        this.mConfiguredOutputs.delete(streamId2.intValue());
                    }
                    for (OutputConfiguration outConfig2 : outputs) {
                        if (addSet.contains(outConfig2)) {
                            this.mConfiguredOutputs.put(this.mRemoteDevice.createStream(outConfig2), outConfig2);
                        }
                    }
                    try {
                        this.mRemoteDevice.endConfigure(isConstrainedHighSpeed);
                        if (1 != null) {
                            if (outputs.size() > 0) {
                                this.mDeviceHandler.post(this.mCallOnIdle);
                            }
                        }
                        this.mDeviceHandler.post(this.mCallOnUnconfigured);
                    } catch (IllegalArgumentException e) {
                        Log.w(this.TAG, "Stream configuration failed");
                        if (null != null) {
                            if (outputs.size() > 0) {
                                this.mDeviceHandler.post(this.mCallOnIdle);
                                return false;
                            }
                        }
                        this.mDeviceHandler.post(this.mCallOnUnconfigured);
                        return false;
                    }
                } catch (RemoteException e2) {
                    if (e2.getReason() == 4) {
                        throw new IllegalStateException("The camera is currently busy. You must wait until the previous operation completes.");
                    }
                    throw e2.asChecked();
                } catch (RemoteException e3) {
                    if (null != null) {
                        if (outputs.size() > 0) {
                            this.mDeviceHandler.post(this.mCallOnIdle);
                            return false;
                        }
                    }
                    this.mDeviceHandler.post(this.mCallOnUnconfigured);
                    return false;
                } catch (Throwable th) {
                    if (null != null) {
                        if (outputs.size() > 0) {
                            this.mDeviceHandler.post(this.mCallOnIdle);
                        }
                    }
                    this.mDeviceHandler.post(this.mCallOnUnconfigured);
                }
            }
            return true;
        }
        throw new IllegalArgumentException("cannot configure an input stream without any output streams");
    }

    public void createCaptureSession(List<Surface> outputs, CameraCaptureSession.StateCallback callback, Handler handler) throws CameraAccessException {
        List<OutputConfiguration> outConfigurations = new ArrayList(outputs.size());
        for (Surface surface : outputs) {
            outConfigurations.add(new OutputConfiguration(surface));
        }
        createCaptureSessionInternal(null, outConfigurations, callback, handler, false);
    }

    public void createCaptureSessionByOutputConfiguration(List<OutputConfiguration> outputConfigurations, CameraCaptureSession.StateCallback callback, Handler handler) throws CameraAccessException {
        createCaptureSessionInternal(null, outputConfigurations, callback, handler, false);
    }

    public void createReprocessableCaptureSession(InputConfiguration inputConfig, List<Surface> outputs, CameraCaptureSession.StateCallback callback, Handler handler) throws CameraAccessException {
        if (inputConfig == null) {
            throw new IllegalArgumentException("inputConfig cannot be null when creating a reprocessable capture session");
        }
        List<OutputConfiguration> outConfigurations = new ArrayList(outputs.size());
        for (Surface surface : outputs) {
            outConfigurations.add(new OutputConfiguration(surface));
        }
        createCaptureSessionInternal(inputConfig, outConfigurations, callback, handler, false);
    }

    public void createConstrainedHighSpeedCaptureSession(List<Surface> outputs, CameraCaptureSession.StateCallback callback, Handler handler) throws CameraAccessException {
        if (outputs == null || outputs.size() == 0 || outputs.size() > 2) {
            throw new IllegalArgumentException("Output surface list must not be null and the size must be no more than 2");
        }
        SurfaceUtils.checkConstrainedHighSpeedSurfaces(outputs, null, (StreamConfigurationMap) getCharacteristics().get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP));
        List<OutputConfiguration> outConfigurations = new ArrayList(outputs.size());
        for (Surface surface : outputs) {
            outConfigurations.add(new OutputConfiguration(surface));
        }
        createCaptureSessionInternal(null, outConfigurations, callback, handler, true);
    }

    private void createCaptureSessionInternal(InputConfiguration inputConfig, List<OutputConfiguration> outputConfigurations, CameraCaptureSession.StateCallback callback, Handler handler, boolean isConstrainedHighSpeed) throws CameraAccessException {
        CameraAccessException e;
        int i;
        CameraCaptureSessionCore newSession;
        int i2;
        CameraCaptureSessionCore cameraCaptureSessionImpl;
        synchronized (this.mInterfaceLock) {
            checkIfCameraClosedOrInError();
            if (!isConstrainedHighSpeed || inputConfig == null) {
                boolean configureSuccess;
                List<Surface> outSurfaces;
                if (this.mCurrentSession != null) {
                    this.mCurrentSession.replaceSessionClose();
                }
                CameraAccessException pendingException = null;
                Surface input = null;
                try {
                    configureSuccess = configureStreamsChecked(inputConfig, outputConfigurations, isConstrainedHighSpeed);
                    if (configureSuccess && inputConfig != null) {
                        Surface input2 = new Surface();
                        try {
                            this.mRemoteDevice.getInputSurface(input2);
                            input = input2;
                        } catch (CameraRuntimeException e2) {
                            try {
                                e2.asChecked();
                                input = input2;
                            } catch (CameraAccessException e3) {
                                e = e3;
                                input = input2;
                                configureSuccess = false;
                                pendingException = e;
                                input = null;
                                outSurfaces = new ArrayList(outputConfigurations.size());
                                for (OutputConfiguration config : outputConfigurations) {
                                    outSurfaces.add(config.getSurface());
                                }
                                if (isConstrainedHighSpeed) {
                                    i = this.mNextSessionId;
                                    this.mNextSessionId = i + 1;
                                    newSession = new CameraConstrainedHighSpeedCaptureSessionImpl(i, outSurfaces, callback, handler, this, this.mDeviceHandler, configureSuccess, this.mCharacteristics);
                                } else {
                                    i2 = this.mNextSessionId;
                                    this.mNextSessionId = i2 + 1;
                                    cameraCaptureSessionImpl = new CameraCaptureSessionImpl(i2, input, outSurfaces, callback, handler, this, this.mDeviceHandler, configureSuccess);
                                }
                                this.mCurrentSession = newSession;
                                if (pendingException != null) {
                                    throw pendingException;
                                }
                                this.mSessionStateCallback = this.mCurrentSession.getDeviceStateCallback();
                                return;
                            } catch (RemoteException e4) {
                                input = input2;
                                return;
                            }
                        }
                    }
                } catch (CameraAccessException e5) {
                    e = e5;
                    configureSuccess = false;
                    pendingException = e;
                    input = null;
                    outSurfaces = new ArrayList(outputConfigurations.size());
                    while (i$.hasNext()) {
                        outSurfaces.add(config.getSurface());
                    }
                    if (isConstrainedHighSpeed) {
                        i2 = this.mNextSessionId;
                        this.mNextSessionId = i2 + 1;
                        cameraCaptureSessionImpl = new CameraCaptureSessionImpl(i2, input, outSurfaces, callback, handler, this, this.mDeviceHandler, configureSuccess);
                    } else {
                        i = this.mNextSessionId;
                        this.mNextSessionId = i + 1;
                        newSession = new CameraConstrainedHighSpeedCaptureSessionImpl(i, outSurfaces, callback, handler, this, this.mDeviceHandler, configureSuccess, this.mCharacteristics);
                    }
                    this.mCurrentSession = newSession;
                    if (pendingException != null) {
                        this.mSessionStateCallback = this.mCurrentSession.getDeviceStateCallback();
                        return;
                    }
                    throw pendingException;
                } catch (RemoteException e6) {
                    return;
                }
                outSurfaces = new ArrayList(outputConfigurations.size());
                while (i$.hasNext()) {
                    outSurfaces.add(config.getSurface());
                }
                if (isConstrainedHighSpeed) {
                    i = this.mNextSessionId;
                    this.mNextSessionId = i + 1;
                    newSession = new CameraConstrainedHighSpeedCaptureSessionImpl(i, outSurfaces, callback, handler, this, this.mDeviceHandler, configureSuccess, this.mCharacteristics);
                } else {
                    i2 = this.mNextSessionId;
                    this.mNextSessionId = i2 + 1;
                    cameraCaptureSessionImpl = new CameraCaptureSessionImpl(i2, input, outSurfaces, callback, handler, this, this.mDeviceHandler, configureSuccess);
                }
                this.mCurrentSession = newSession;
                if (pendingException != null) {
                    throw pendingException;
                }
                this.mSessionStateCallback = this.mCurrentSession.getDeviceStateCallback();
                return;
            }
            throw new IllegalArgumentException("Constrained high speed session doesn't support input configuration yet.");
        }
    }

    public void setSessionListener(StateCallbackKK sessionCallback) {
        synchronized (this.mInterfaceLock) {
            this.mSessionStateCallback = sessionCallback;
        }
    }

    public Builder createCaptureRequest(int templateType) throws CameraAccessException {
        Builder builder;
        synchronized (this.mInterfaceLock) {
            checkIfCameraClosedOrInError();
            CameraMetadataNative templatedRequest = new CameraMetadataNative();
            try {
                this.mRemoteDevice.createDefaultRequest(templateType, templatedRequest);
                builder = new Builder(templatedRequest, false, -1);
            } catch (CameraRuntimeException e) {
                throw e.asChecked();
            } catch (RemoteException e2) {
                builder = null;
            }
        }
        return builder;
    }

    public Builder createReprocessCaptureRequest(TotalCaptureResult inputResult) throws CameraAccessException {
        Builder builder;
        synchronized (this.mInterfaceLock) {
            checkIfCameraClosedOrInError();
            builder = new Builder(new CameraMetadataNative(inputResult.getNativeCopy()), true, inputResult.getSessionId());
        }
        return builder;
    }

    public void prepare(Surface surface) throws CameraAccessException {
        if (surface == null) {
            throw new IllegalArgumentException("Surface is null");
        }
        synchronized (this.mInterfaceLock) {
            int streamId = -1;
            for (int i = 0; i < this.mConfiguredOutputs.size(); i++) {
                if (surface == ((OutputConfiguration) this.mConfiguredOutputs.valueAt(i)).getSurface()) {
                    streamId = this.mConfiguredOutputs.keyAt(i);
                    break;
                }
            }
            if (streamId == -1) {
                throw new IllegalArgumentException("Surface is not part of this session");
            }
            try {
                this.mRemoteDevice.prepare(streamId);
            } catch (CameraRuntimeException e) {
                throw e.asChecked();
            } catch (RemoteException e2) {
            }
        }
    }

    public void prepare(int maxCount, Surface surface) throws CameraAccessException {
        if (surface == null) {
            throw new IllegalArgumentException("Surface is null");
        } else if (maxCount <= 0) {
            throw new IllegalArgumentException("Invalid maxCount given: " + maxCount);
        } else {
            synchronized (this.mInterfaceLock) {
                int streamId = -1;
                for (int i = 0; i < this.mConfiguredOutputs.size(); i++) {
                    if (surface == ((OutputConfiguration) this.mConfiguredOutputs.valueAt(i)).getSurface()) {
                        streamId = this.mConfiguredOutputs.keyAt(i);
                        break;
                    }
                }
                if (streamId == -1) {
                    throw new IllegalArgumentException("Surface is not part of this session");
                }
                try {
                    this.mRemoteDevice.prepare2(maxCount, streamId);
                } catch (CameraRuntimeException e) {
                    throw e.asChecked();
                } catch (RemoteException e2) {
                }
            }
        }
    }

    public void tearDown(Surface surface) throws CameraAccessException {
        if (surface == null) {
            throw new IllegalArgumentException("Surface is null");
        }
        synchronized (this.mInterfaceLock) {
            int streamId = -1;
            for (int i = 0; i < this.mConfiguredOutputs.size(); i++) {
                if (surface == ((OutputConfiguration) this.mConfiguredOutputs.valueAt(i)).getSurface()) {
                    streamId = this.mConfiguredOutputs.keyAt(i);
                    break;
                }
            }
            if (streamId == -1) {
                throw new IllegalArgumentException("Surface is not part of this session");
            }
            try {
                this.mRemoteDevice.tearDown(streamId);
            } catch (CameraRuntimeException e) {
                throw e.asChecked();
            } catch (RemoteException e2) {
            }
        }
    }

    public int capture(CaptureRequest request, CaptureCallback callback, Handler handler) throws CameraAccessException {
        List<CaptureRequest> requestList = new ArrayList();
        requestList.add(request);
        return submitCaptureRequest(requestList, callback, handler, false);
    }

    public int captureBurst(List<CaptureRequest> requests, CaptureCallback callback, Handler handler) throws CameraAccessException {
        if (requests != null && !requests.isEmpty()) {
            return submitCaptureRequest(requests, callback, handler, false);
        }
        throw new IllegalArgumentException("At least one request must be given");
    }

    private void checkEarlyTriggerSequenceComplete(final int requestId, long lastFrameNumber) {
        if (lastFrameNumber == -1) {
            int index = this.mCaptureCallbackMap.indexOfKey(requestId);
            final CaptureCallbackHolder holder = index >= 0 ? (CaptureCallbackHolder) this.mCaptureCallbackMap.valueAt(index) : null;
            if (holder != null) {
                this.mCaptureCallbackMap.removeAt(index);
            }
            if (holder != null) {
                holder.getHandler().post(new Runnable() {
                    public void run() {
                        if (!CameraDeviceImpl.this.isClosed()) {
                            holder.getCallback().onCaptureSequenceAborted(CameraDeviceImpl.this, requestId);
                        }
                    }
                });
                return;
            } else {
                Log.w(this.TAG, String.format("did not register callback to request %d", new Object[]{Integer.valueOf(requestId)}));
                return;
            }
        }
        this.mRequestLastFrameNumbersList.add(new RequestLastFrameNumbersHolder(requestId, lastFrameNumber));
        checkAndFireSequenceComplete();
    }

    private int submitCaptureRequest(List<CaptureRequest> requestList, CaptureCallback callback, Handler handler, boolean repeating) throws CameraAccessException {
        int requestId;
        handler = checkHandler(handler, callback);
        for (CaptureRequest request : requestList) {
            if (request.getTargets().isEmpty()) {
                throw new IllegalArgumentException("Each request must have at least one Surface target");
            }
            for (Surface surface : request.getTargets()) {
                if (surface == null) {
                    throw new IllegalArgumentException("Null Surface targets are not allowed");
                }
            }
        }
        synchronized (this.mInterfaceLock) {
            checkIfCameraClosedOrInError();
            if (repeating) {
                stopRepeating();
            }
            LongParcelable lastFrameNumberRef = new LongParcelable();
            try {
                requestId = this.mRemoteDevice.submitRequestList(requestList, repeating, lastFrameNumberRef);
                if (callback != null) {
                    SparseArray sparseArray = this.mCaptureCallbackMap;
                    sparseArray.put(requestId, new CaptureCallbackHolder(callback, requestList, handler, repeating, this.mNextSessionId - 1));
                }
                long lastFrameNumber = lastFrameNumberRef.getNumber();
                if (repeating) {
                    if (this.mRepeatingRequestId != -1) {
                        checkEarlyTriggerSequenceComplete(this.mRepeatingRequestId, lastFrameNumber);
                    }
                    this.mRepeatingRequestId = requestId;
                } else {
                    this.mRequestLastFrameNumbersList.add(new RequestLastFrameNumbersHolder(requestList, requestId, lastFrameNumber));
                }
                if (this.mIdle) {
                    this.mDeviceHandler.post(this.mCallOnActive);
                }
                this.mIdle = false;
            } catch (CameraRuntimeException e) {
                throw e.asChecked();
            } catch (RemoteException e2) {
                requestId = -1;
            }
        }
        return requestId;
    }

    public int setRepeatingRequest(CaptureRequest request, CaptureCallback callback, Handler handler) throws CameraAccessException {
        List<CaptureRequest> requestList = new ArrayList();
        requestList.add(request);
        return submitCaptureRequest(requestList, callback, handler, true);
    }

    public int setRepeatingBurst(List<CaptureRequest> requests, CaptureCallback callback, Handler handler) throws CameraAccessException {
        if (requests != null && !requests.isEmpty()) {
            return submitCaptureRequest(requests, callback, handler, true);
        }
        throw new IllegalArgumentException("At least one request must be given");
    }

    public void stopRepeating() throws CameraAccessException {
        synchronized (this.mInterfaceLock) {
            checkIfCameraClosedOrInError();
            if (this.mRepeatingRequestId != -1) {
                int requestId = this.mRepeatingRequestId;
                this.mRepeatingRequestId = -1;
                if (this.mCaptureCallbackMap.get(requestId) != null) {
                    this.mRepeatingRequestIdDeletedList.add(Integer.valueOf(requestId));
                }
                try {
                    LongParcelable lastFrameNumberRef = new LongParcelable();
                    this.mRemoteDevice.cancelRequest(requestId, lastFrameNumberRef);
                    checkEarlyTriggerSequenceComplete(requestId, lastFrameNumberRef.getNumber());
                } catch (CameraRuntimeException e) {
                    throw e.asChecked();
                } catch (RemoteException e2) {
                    return;
                }
            }
        }
    }

    private void waitUntilIdle() throws CameraAccessException {
        synchronized (this.mInterfaceLock) {
            checkIfCameraClosedOrInError();
            if (this.mRepeatingRequestId != -1) {
                throw new IllegalStateException("Active repeating request ongoing");
            }
            try {
                this.mRemoteDevice.waitUntilIdle();
            } catch (CameraRuntimeException e) {
                throw e.asChecked();
            } catch (RemoteException e2) {
            }
        }
    }

    public void flush() throws CameraAccessException {
        synchronized (this.mInterfaceLock) {
            checkIfCameraClosedOrInError();
            this.mDeviceHandler.post(this.mCallOnBusy);
            if (this.mIdle) {
                this.mDeviceHandler.post(this.mCallOnIdle);
                return;
            }
            try {
                LongParcelable lastFrameNumberRef = new LongParcelable();
                this.mRemoteDevice.flush(lastFrameNumberRef);
                if (this.mRepeatingRequestId != -1) {
                    checkEarlyTriggerSequenceComplete(this.mRepeatingRequestId, lastFrameNumberRef.getNumber());
                    this.mRepeatingRequestId = -1;
                }
            } catch (CameraRuntimeException e) {
                throw e.asChecked();
            } catch (RemoteException e2) {
            }
        }
    }

    public void close() {
        synchronized (this.mInterfaceLock) {
            if (this.mClosing.getAndSet(true)) {
                return;
            }
            try {
                if (this.mRemoteDevice != null) {
                    this.mRemoteDevice.disconnect();
                }
            } catch (CameraRuntimeException e) {
                Log.e(this.TAG, "Exception while closing: ", e.asChecked());
            } catch (RemoteException e2) {
            }
            if (this.mRemoteDevice != null || this.mInError) {
                this.mDeviceHandler.post(this.mCallOnClosed);
            }
            this.mRemoteDevice = null;
        }
    }

    protected void finalize() throws Throwable {
        try {
            close();
        } finally {
            super.finalize();
        }
    }

    private void checkInputConfiguration(InputConfiguration inputConfig) {
        if (inputConfig != null) {
            StreamConfigurationMap configMap = (StreamConfigurationMap) this.mCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            boolean validFormat = false;
            for (int format : configMap.getInputFormats()) {
                if (format == inputConfig.getFormat()) {
                    validFormat = true;
                }
            }
            if (validFormat) {
                boolean validSize = false;
                for (Size s : configMap.getInputSizes(inputConfig.getFormat())) {
                    if (inputConfig.getWidth() == s.getWidth() && inputConfig.getHeight() == s.getHeight()) {
                        validSize = true;
                    }
                }
                if (!validSize) {
                    throw new IllegalArgumentException("input size " + inputConfig.getWidth() + "x" + inputConfig.getHeight() + " is not valid");
                }
                return;
            }
            throw new IllegalArgumentException("input format " + inputConfig.getFormat() + " is not valid");
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void checkAndFireSequenceComplete() {
        /*
        r21 = this;
        r0 = r21;
        r0 = r0.mFrameNumberTracker;
        r18 = r0;
        r2 = r18.getCompletedFrameNumber();
        r0 = r21;
        r0 = r0.mFrameNumberTracker;
        r18 = r0;
        r4 = r18.getCompletedReprocessFrameNumber();
        r8 = 0;
        r0 = r21;
        r0 = r0.mRequestLastFrameNumbersList;
        r18 = r0;
        r9 = r18.iterator();
    L_0x001f:
        r18 = r9.hasNext();
        if (r18 == 0) goto L_0x0050;
    L_0x0025:
        r15 = r9.next();
        r15 = (android.hardware.camera2.impl.CameraDeviceImpl.RequestLastFrameNumbersHolder) r15;
        r17 = 0;
        r14 = r15.getRequestId();
        r0 = r21;
        r0 = r0.mInterfaceLock;
        r19 = r0;
        monitor-enter(r19);
        r0 = r21;
        r0 = r0.mRemoteDevice;	 Catch:{ all -> 0x00b0 }
        r18 = r0;
        if (r18 != 0) goto L_0x0051;
    L_0x0040:
        r0 = r21;
        r0 = r0.TAG;	 Catch:{ all -> 0x00b0 }
        r18 = r0;
        r20 = "Camera closed while checking sequences";
        r0 = r18;
        r1 = r20;
        android.util.Log.w(r0, r1);	 Catch:{ all -> 0x00b0 }
        monitor-exit(r19);	 Catch:{ all -> 0x00b0 }
    L_0x0050:
        return;
    L_0x0051:
        r0 = r21;
        r0 = r0.mCaptureCallbackMap;	 Catch:{ all -> 0x00b0 }
        r18 = r0;
        r0 = r18;
        r7 = r0.indexOfKey(r14);	 Catch:{ all -> 0x00b0 }
        if (r7 < 0) goto L_0x00ae;
    L_0x005f:
        r0 = r21;
        r0 = r0.mCaptureCallbackMap;	 Catch:{ all -> 0x00b0 }
        r18 = r0;
        r0 = r18;
        r18 = r0.valueAt(r7);	 Catch:{ all -> 0x00b0 }
        r18 = (android.hardware.camera2.impl.CameraDeviceImpl.CaptureCallbackHolder) r18;	 Catch:{ all -> 0x00b0 }
        r6 = r18;
    L_0x006f:
        if (r6 == 0) goto L_0x008e;
    L_0x0071:
        r10 = r15.getLastRegularFrameNumber();	 Catch:{ all -> 0x00b0 }
        r12 = r15.getLastReprocessFrameNumber();	 Catch:{ all -> 0x00b0 }
        r18 = (r10 > r2 ? 1 : (r10 == r2 ? 0 : -1));
        if (r18 > 0) goto L_0x008e;
    L_0x007d:
        r18 = (r12 > r4 ? 1 : (r12 == r4 ? 0 : -1));
        if (r18 > 0) goto L_0x008e;
    L_0x0081:
        r17 = 1;
        r0 = r21;
        r0 = r0.mCaptureCallbackMap;	 Catch:{ all -> 0x00b0 }
        r18 = r0;
        r0 = r18;
        r0.removeAt(r7);	 Catch:{ all -> 0x00b0 }
    L_0x008e:
        monitor-exit(r19);	 Catch:{ all -> 0x00b0 }
        if (r6 == 0) goto L_0x0093;
    L_0x0091:
        if (r17 == 0) goto L_0x0096;
    L_0x0093:
        r9.remove();
    L_0x0096:
        if (r17 == 0) goto L_0x001f;
    L_0x0098:
        r16 = new android.hardware.camera2.impl.CameraDeviceImpl$10;
        r0 = r16;
        r1 = r21;
        r0.<init>(r14, r6, r15);
        r18 = r6.getHandler();
        r0 = r18;
        r1 = r16;
        r0.post(r1);
        goto L_0x001f;
    L_0x00ae:
        r6 = 0;
        goto L_0x006f;
    L_0x00b0:
        r18 = move-exception;
        monitor-exit(r19);	 Catch:{ all -> 0x00b0 }
        throw r18;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.hardware.camera2.impl.CameraDeviceImpl.checkAndFireSequenceComplete():void");
    }

    static Handler checkHandler(Handler handler) {
        if (handler != null) {
            return handler;
        }
        Looper looper = Looper.myLooper();
        if (looper != null) {
            return new Handler(looper);
        }
        throw new IllegalArgumentException("No handler given, and current thread has no looper!");
    }

    static <T> Handler checkHandler(Handler handler, T callback) {
        if (callback != null) {
            return checkHandler(handler);
        }
        return handler;
    }

    private void checkIfCameraClosedOrInError() throws CameraAccessException {
        if (this.mRemoteDevice == null) {
            throw new IllegalStateException("CameraDevice was already closed");
        } else if (this.mInError) {
            throw new CameraAccessException(3, "The camera device has encountered a serious error");
        }
    }

    private boolean isClosed() {
        return this.mClosing.get();
    }

    private CameraCharacteristics getCharacteristics() {
        return this.mCharacteristics;
    }
}
