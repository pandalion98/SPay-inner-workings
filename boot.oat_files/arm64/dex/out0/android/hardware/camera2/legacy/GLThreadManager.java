package android.hardware.camera2.legacy;

import android.graphics.SurfaceTexture;
import android.hardware.camera2.legacy.RequestThreadManager.FpsCounter;
import android.os.ConditionVariable;
import android.os.Handler;
import android.os.Handler.Callback;
import android.util.Log;
import android.util.Pair;
import android.util.Size;
import android.view.Surface;
import com.android.internal.util.Preconditions;
import java.util.Collection;

public class GLThreadManager {
    private static final boolean DEBUG = false;
    private static final int MSG_ALLOW_FRAMES = 5;
    private static final int MSG_CLEANUP = 3;
    private static final int MSG_DROP_FRAMES = 4;
    private static final int MSG_NEW_CONFIGURATION = 1;
    private static final int MSG_NEW_FRAME = 2;
    private final String TAG;
    private CaptureCollector mCaptureCollector;
    private final CameraDeviceState mDeviceState;
    private final Callback mGLHandlerCb = new Callback() {
        private boolean mCleanup = false;
        private boolean mConfigured = false;
        private boolean mDroppingFrames = false;

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean handleMessage(android.os.Message r7) {
            /*
            r6 = this;
            r5 = 1;
            r2 = r6.mCleanup;
            if (r2 == 0) goto L_0x0006;
        L_0x0005:
            return r5;
        L_0x0006:
            r2 = r7.what;	 Catch:{ Exception -> 0x0030 }
            switch(r2) {
                case -1: goto L_0x0005;
                case 0: goto L_0x000b;
                case 1: goto L_0x0046;
                case 2: goto L_0x0074;
                case 3: goto L_0x00a4;
                case 4: goto L_0x00b5;
                case 5: goto L_0x00ba;
                default: goto L_0x000b;
            };	 Catch:{ Exception -> 0x0030 }
        L_0x000b:
            r2 = android.hardware.camera2.legacy.GLThreadManager.this;	 Catch:{ Exception -> 0x0030 }
            r2 = r2.TAG;	 Catch:{ Exception -> 0x0030 }
            r3 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0030 }
            r3.<init>();	 Catch:{ Exception -> 0x0030 }
            r4 = "Unhandled message ";
            r3 = r3.append(r4);	 Catch:{ Exception -> 0x0030 }
            r4 = r7.what;	 Catch:{ Exception -> 0x0030 }
            r3 = r3.append(r4);	 Catch:{ Exception -> 0x0030 }
            r4 = " on GLThread.";
            r3 = r3.append(r4);	 Catch:{ Exception -> 0x0030 }
            r3 = r3.toString();	 Catch:{ Exception -> 0x0030 }
            android.util.Log.e(r2, r3);	 Catch:{ Exception -> 0x0030 }
            goto L_0x0005;
        L_0x0030:
            r1 = move-exception;
            r2 = android.hardware.camera2.legacy.GLThreadManager.this;
            r2 = r2.TAG;
            r3 = "Received exception on GL render thread: ";
            android.util.Log.e(r2, r3, r1);
            r2 = android.hardware.camera2.legacy.GLThreadManager.this;
            r2 = r2.mDeviceState;
            r2.setError(r5);
            goto L_0x0005;
        L_0x0046:
            r0 = r7.obj;	 Catch:{ Exception -> 0x0030 }
            r0 = (android.hardware.camera2.legacy.GLThreadManager.ConfigureHolder) r0;	 Catch:{ Exception -> 0x0030 }
            r2 = android.hardware.camera2.legacy.GLThreadManager.this;	 Catch:{ Exception -> 0x0030 }
            r2 = r2.mTextureRenderer;	 Catch:{ Exception -> 0x0030 }
            r2.cleanupEGLContext();	 Catch:{ Exception -> 0x0030 }
            r2 = android.hardware.camera2.legacy.GLThreadManager.this;	 Catch:{ Exception -> 0x0030 }
            r2 = r2.mTextureRenderer;	 Catch:{ Exception -> 0x0030 }
            r3 = r0.surfaces;	 Catch:{ Exception -> 0x0030 }
            r2.configureSurfaces(r3);	 Catch:{ Exception -> 0x0030 }
            r3 = android.hardware.camera2.legacy.GLThreadManager.this;	 Catch:{ Exception -> 0x0030 }
            r2 = r0.collector;	 Catch:{ Exception -> 0x0030 }
            r2 = com.android.internal.util.Preconditions.checkNotNull(r2);	 Catch:{ Exception -> 0x0030 }
            r2 = (android.hardware.camera2.legacy.CaptureCollector) r2;	 Catch:{ Exception -> 0x0030 }
            r3.mCaptureCollector = r2;	 Catch:{ Exception -> 0x0030 }
            r2 = r0.condition;	 Catch:{ Exception -> 0x0030 }
            r2.open();	 Catch:{ Exception -> 0x0030 }
            r2 = 1;
            r6.mConfigured = r2;	 Catch:{ Exception -> 0x0030 }
            goto L_0x0005;
        L_0x0074:
            r2 = r6.mDroppingFrames;	 Catch:{ Exception -> 0x0030 }
            if (r2 == 0) goto L_0x0084;
        L_0x0078:
            r2 = android.hardware.camera2.legacy.GLThreadManager.this;	 Catch:{ Exception -> 0x0030 }
            r2 = r2.TAG;	 Catch:{ Exception -> 0x0030 }
            r3 = "Ignoring frame.";
            android.util.Log.w(r2, r3);	 Catch:{ Exception -> 0x0030 }
            goto L_0x0005;
        L_0x0084:
            r2 = r6.mConfigured;	 Catch:{ Exception -> 0x0030 }
            if (r2 != 0) goto L_0x0093;
        L_0x0088:
            r2 = android.hardware.camera2.legacy.GLThreadManager.this;	 Catch:{ Exception -> 0x0030 }
            r2 = r2.TAG;	 Catch:{ Exception -> 0x0030 }
            r3 = "Dropping frame, EGL context not configured!";
            android.util.Log.e(r2, r3);	 Catch:{ Exception -> 0x0030 }
        L_0x0093:
            r2 = android.hardware.camera2.legacy.GLThreadManager.this;	 Catch:{ Exception -> 0x0030 }
            r2 = r2.mTextureRenderer;	 Catch:{ Exception -> 0x0030 }
            r3 = android.hardware.camera2.legacy.GLThreadManager.this;	 Catch:{ Exception -> 0x0030 }
            r3 = r3.mCaptureCollector;	 Catch:{ Exception -> 0x0030 }
            r2.drawIntoSurfaces(r3);	 Catch:{ Exception -> 0x0030 }
            goto L_0x0005;
        L_0x00a4:
            r2 = android.hardware.camera2.legacy.GLThreadManager.this;	 Catch:{ Exception -> 0x0030 }
            r2 = r2.mTextureRenderer;	 Catch:{ Exception -> 0x0030 }
            r2.cleanupEGLContext();	 Catch:{ Exception -> 0x0030 }
            r2 = 1;
            r6.mCleanup = r2;	 Catch:{ Exception -> 0x0030 }
            r2 = 0;
            r6.mConfigured = r2;	 Catch:{ Exception -> 0x0030 }
            goto L_0x0005;
        L_0x00b5:
            r2 = 1;
            r6.mDroppingFrames = r2;	 Catch:{ Exception -> 0x0030 }
            goto L_0x0005;
        L_0x00ba:
            r2 = 0;
            r6.mDroppingFrames = r2;	 Catch:{ Exception -> 0x0030 }
            goto L_0x0005;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.hardware.camera2.legacy.GLThreadManager.1.handleMessage(android.os.Message):boolean");
        }
    };
    private final RequestHandlerThread mGLHandlerThread;
    private final FpsCounter mPrevCounter = new FpsCounter("GL Preview Producer");
    private final SurfaceTextureRenderer mTextureRenderer;

    private static class ConfigureHolder {
        public final CaptureCollector collector;
        public final ConditionVariable condition;
        public final Collection<Pair<Surface, Size>> surfaces;

        public ConfigureHolder(ConditionVariable condition, Collection<Pair<Surface, Size>> surfaces, CaptureCollector collector) {
            this.condition = condition;
            this.surfaces = surfaces;
            this.collector = collector;
        }
    }

    public GLThreadManager(int cameraId, int facing, CameraDeviceState state) {
        this.mTextureRenderer = new SurfaceTextureRenderer(facing);
        this.TAG = String.format("CameraDeviceGLThread-%d", new Object[]{Integer.valueOf(cameraId)});
        this.mGLHandlerThread = new RequestHandlerThread(this.TAG, this.mGLHandlerCb);
        this.mDeviceState = state;
    }

    public void start() {
        this.mGLHandlerThread.start();
    }

    public void waitUntilStarted() {
        this.mGLHandlerThread.waitUntilStarted();
    }

    public void quit() {
        Handler handler = this.mGLHandlerThread.getHandler();
        handler.sendMessageAtFrontOfQueue(handler.obtainMessage(3));
        this.mGLHandlerThread.quitSafely();
        try {
            this.mGLHandlerThread.join();
        } catch (InterruptedException e) {
            Log.e(this.TAG, String.format("Thread %s (%d) interrupted while quitting.", new Object[]{this.mGLHandlerThread.getName(), Long.valueOf(this.mGLHandlerThread.getId())}));
        }
    }

    public void queueNewFrame() {
        Handler handler = this.mGLHandlerThread.getHandler();
        if (handler.hasMessages(2)) {
            Log.e(this.TAG, "GLThread dropping frame.  Not consuming frames quickly enough!");
        } else {
            handler.sendMessage(handler.obtainMessage(2));
        }
    }

    public void setConfigurationAndWait(Collection<Pair<Surface, Size>> surfaces, CaptureCollector collector) {
        Preconditions.checkNotNull(collector, "collector must not be null");
        Handler handler = this.mGLHandlerThread.getHandler();
        ConditionVariable condition = new ConditionVariable(false);
        handler.sendMessage(handler.obtainMessage(1, 0, 0, new ConfigureHolder(condition, surfaces, collector)));
        condition.block();
    }

    public SurfaceTexture getCurrentSurfaceTexture() {
        return this.mTextureRenderer.getSurfaceTexture();
    }

    public void ignoreNewFrames() {
        this.mGLHandlerThread.getHandler().sendEmptyMessage(4);
    }

    public void waitUntilIdle() {
        this.mGLHandlerThread.waitUntilIdle();
    }

    public void allowNewFrames() {
        this.mGLHandlerThread.getHandler().sendEmptyMessage(5);
    }
}
