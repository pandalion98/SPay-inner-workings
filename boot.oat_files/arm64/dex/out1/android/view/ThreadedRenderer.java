package android.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Binder;
import android.os.Build;
import android.os.Debug;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.Trace;
import android.util.Log;
import android.view.IGraphicsStats.Stub;
import android.view.Surface.OutOfResourcesException;
import com.android.internal.R;
import java.io.FileDescriptor;
import java.io.PrintWriter;

public class ThreadedRenderer extends HardwareRenderer {
    private static final boolean DEBUG_DCS = "eng".equals(Build.TYPE);
    private static final boolean DYNAMIC_COLOR_SCALING_IS_ENABLED = true;
    private static final int FLAG_DUMP_FRAMESTATS = 1;
    private static final int FLAG_DUMP_RESET = 2;
    private static final String LOGTAG = "ThreadedRenderer";
    private static final boolean SAFE_DEBUG = (Debug.isProductShip() != 1);
    private static final int SYNC_INVALIDATE_REQUIRED = 1;
    private static final int SYNC_LOST_SURFACE_REWARD_IF_FOUND = 2;
    private static final int SYNC_OK = 0;
    private static final String[] VISUALIZERS = new String[]{HardwareRenderer.PROFILE_PROPERTY_VISUALIZE_BARS};
    private final int mAmbientShadowAlpha;
    private Choreographer mChoreographer;
    private boolean mDemoted;
    private boolean mHasInsets;
    private int mHeight;
    private boolean mInitialized;
    private int mInsetLeft;
    private int mInsetTop;
    private final float mLightRadius;
    private final float mLightY;
    private final float mLightZ;
    private long mNativeProxy;
    private RenderNode mRootNode;
    private boolean mRootNodeNeedsUpdate;
    private final int mSpotShadowAlpha;
    private int mSurfaceHeight;
    private int mSurfaceWidth;
    private int mWidth;

    private static class ProcessInitializer {
        static ProcessInitializer sInstance = new ProcessInitializer();
        private static IBinder sProcToken;
        private boolean mInitialized = false;

        private ProcessInitializer() {
        }

        synchronized void init(Context context, long renderProxy) {
            if (!this.mInitialized) {
                this.mInitialized = true;
                initGraphicsStats(context, renderProxy);
                initAssetAtlas(context, renderProxy);
            }
        }

        private static void initGraphicsStats(Context context, long renderProxy) {
            try {
                IBinder binder = ServiceManager.getService("graphicsstats");
                if (binder != null) {
                    IGraphicsStats graphicsStatsService = Stub.asInterface(binder);
                    sProcToken = new Binder();
                    ParcelFileDescriptor pfd = graphicsStatsService.requestBufferForProcess(context.getApplicationInfo().packageName, sProcToken);
                    ThreadedRenderer.nSetProcessStatsBuffer(renderProxy, pfd.getFd());
                    pfd.close();
                }
            } catch (Throwable t) {
                Log.w("HardwareRenderer", "Could not acquire gfx stats buffer", t);
            }
        }

        private static void initAssetAtlas(Context context, long renderProxy) {
            IBinder binder = ServiceManager.getService("assetatlas");
            if (binder != null) {
                IAssetAtlas atlas = IAssetAtlas.Stub.asInterface(binder);
                try {
                    if (atlas.isCompatible(Process.myPpid())) {
                        GraphicBuffer buffer = atlas.getBuffer();
                        if (buffer != null) {
                            long[] map = atlas.getMap();
                            if (map != null) {
                                ThreadedRenderer.nSetAtlas(renderProxy, buffer, map);
                            }
                            if (atlas.getClass() != binder.getClass()) {
                                buffer.destroy();
                            }
                        }
                    }
                } catch (RemoteException e) {
                    Log.w("HardwareRenderer", "Could not acquire atlas", e);
                }
            }
        }
    }

    private static native void nBuildLayer(long j, long j2);

    private static native void nCancelLayerUpdate(long j, long j2);

    private static native boolean nCopyLayerInto(long j, long j2, Bitmap bitmap);

    private static native long nCreateProxy(boolean z, long j, boolean z2);

    private static native long nCreateRootRenderNode();

    private static native long nCreateTextureLayer(long j);

    private static native boolean nDcsInitialize(long j, Surface surface, int i, double d);

    private static native void nDeleteProxy(long j);

    private static native void nDestroy(long j);

    private static native void nDestroyHardwareResources(long j);

    private static native void nDetachSurfaceTexture(long j, long j2);

    private static native void nDumpProfileData(byte[] bArr, FileDescriptor fileDescriptor);

    private static native void nDumpProfileInfo(long j, FileDescriptor fileDescriptor, int i);

    private static native void nFence(long j);

    private static native boolean nInitialize(long j, Surface surface);

    private static native void nInvokeFunctor(long j, boolean z);

    private static native boolean nLoadSystemProperties(long j);

    private static native void nNotifyFramePending(long j);

    private static native void nOverrideProperty(String str, String str2);

    private static native boolean nPauseSurface(long j, Surface surface);

    private static native void nPushLayerUpdate(long j, long j2);

    private static native void nRegisterAnimatingRenderNode(long j, long j2);

    private static native void nSetAtlas(long j, GraphicBuffer graphicBuffer, long[] jArr);

    private static native void nSetLightCenter(long j, float f, float f2, float f3);

    private static native void nSetName(long j, String str);

    private static native void nSetOpaque(long j, boolean z);

    private static native void nSetProcessStatsBuffer(long j, int i);

    private static native void nSetup(long j, int i, int i2, float f, int i3, int i4);

    private static native void nStopDrawing(long j);

    private static native int nSyncAndDrawFrame(long j, long[] jArr, int i);

    private static native void nTrimMemory(int i);

    private static native void nUpdateSurface(long j, Surface surface);

    static native void setupShadersDiskCache(String str);

    static native void setupVulkanLayerPath(String str);

    ThreadedRenderer(Context context, boolean translucent) {
        this(context, translucent, false);
    }

    ThreadedRenderer(Context context, boolean translucent, boolean demoted) {
        this.mInitialized = false;
        this.mDemoted = false;
        TypedArray a = context.obtainStyledAttributes(null, R.styleable.Lighting, 0, 0);
        this.mLightY = a.getDimension(2, 0.0f);
        this.mLightZ = a.getDimension(3, 0.0f);
        this.mLightRadius = a.getDimension(4, 0.0f);
        this.mAmbientShadowAlpha = (int) ((a.getFloat(0, 0.0f) * 255.0f) + 0.5f);
        this.mSpotShadowAlpha = (int) ((a.getFloat(1, 0.0f) * 255.0f) + 0.5f);
        a.recycle();
        long rootNodePtr = nCreateRootRenderNode();
        this.mRootNode = RenderNode.adopt(rootNodePtr);
        this.mRootNode.setClipToBounds(false);
        this.mNativeProxy = nCreateProxy(translucent, rootNodePtr, demoted);
        this.mDemoted = demoted;
        if (this.mDemoted) {
            Log.i(LOGTAG, "ThreadedRenderer created in DEMOTED mode");
        }
        ProcessInitializer.sInstance.init(context, this.mNativeProxy);
        loadSystemProperties();
    }

    void destroy() {
        this.mInitialized = false;
        updateEnabledState(null);
        nDestroy(this.mNativeProxy);
    }

    private void updateEnabledState(Surface surface) {
        if (surface == null || !surface.isValid()) {
            setEnabled(false);
        } else {
            setEnabled(this.mInitialized);
        }
    }

    boolean initialize(Surface surface) throws OutOfResourcesException {
        this.mInitialized = true;
        updateEnabledState(surface);
        return nInitialize(this.mNativeProxy, surface);
    }

    boolean initialize(Surface surface, boolean mIsDcsEnabledApp, int mDcsFormat, double mDssFactor) throws OutOfResourcesException {
        this.mInitialized = true;
        updateEnabledState(surface);
        if (!mIsDcsEnabledApp) {
            return nInitialize(this.mNativeProxy, surface);
        }
        Log.e("SRIB_DSS_ThreadedRenderer", "initialize, mIsDcsEnabledApp=" + mIsDcsEnabledApp + " , with format= " + mDcsFormat + " scaleFactor=" + mDssFactor);
        return nDcsInitialize(this.mNativeProxy, surface, mDcsFormat, mDssFactor);
    }

    void updateSurface(Surface surface) throws OutOfResourcesException {
        updateEnabledState(surface);
        nUpdateSurface(this.mNativeProxy, surface);
    }

    boolean pauseSurface(Surface surface) {
        return nPauseSurface(this.mNativeProxy, surface);
    }

    void destroyHardwareResources(View view) {
        destroyResources(view);
        nDestroyHardwareResources(this.mNativeProxy);
    }

    private static void destroyResources(View view) {
        view.destroyHardwareResources();
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            int count = group.getChildCount();
            for (int i = 0; i < count; i++) {
                destroyResources(group.getChildAt(i));
            }
        }
    }

    void invalidate(Surface surface) {
        updateSurface(surface);
    }

    void detachSurfaceTexture(long hardwareLayer) {
        nDetachSurfaceTexture(this.mNativeProxy, hardwareLayer);
    }

    void setup(int width, int height, AttachInfo attachInfo, Rect surfaceInsets) {
        this.mWidth = width;
        this.mHeight = height;
        if (surfaceInsets == null || (surfaceInsets.left == 0 && surfaceInsets.right == 0 && surfaceInsets.top == 0 && surfaceInsets.bottom == 0)) {
            this.mHasInsets = false;
            this.mInsetLeft = 0;
            this.mInsetTop = 0;
            this.mSurfaceWidth = width;
            this.mSurfaceHeight = height;
        } else {
            this.mHasInsets = true;
            this.mInsetLeft = surfaceInsets.left;
            this.mInsetTop = surfaceInsets.top;
            this.mSurfaceWidth = (this.mInsetLeft + width) + surfaceInsets.right;
            this.mSurfaceHeight = (this.mInsetTop + height) + surfaceInsets.bottom;
            setOpaque(false);
        }
        this.mRootNode.setLeftTopRightBottom(-this.mInsetLeft, -this.mInsetTop, this.mSurfaceWidth, this.mSurfaceHeight);
        nSetup(this.mNativeProxy, this.mSurfaceWidth, this.mSurfaceHeight, this.mLightRadius, this.mAmbientShadowAlpha, this.mSpotShadowAlpha);
        setLightCenter(attachInfo);
    }

    void setLightCenter(AttachInfo attachInfo) {
        Point displaySize = attachInfo.mPoint;
        attachInfo.mDisplay.getRealSize(displaySize);
        nSetLightCenter(this.mNativeProxy, (((float) displaySize.x) / 2.0f) - ((float) attachInfo.mWindowLeft), this.mLightY - ((float) attachInfo.mWindowTop), this.mLightZ);
    }

    void setOpaque(boolean opaque) {
        long j = this.mNativeProxy;
        boolean z = opaque && !this.mHasInsets;
        nSetOpaque(j, z);
    }

    int getWidth() {
        return this.mWidth;
    }

    int getHeight() {
        return this.mHeight;
    }

    void dumpGfxInfo(PrintWriter pw, FileDescriptor fd, String[] args) {
        pw.flush();
        int flags = 0;
        for (String str : args) {
            Object obj = -1;
            switch (str.hashCode()) {
                case -252053678:
                    if (str.equals("framestats")) {
                        obj = null;
                        break;
                    }
                    break;
                case 108404047:
                    if (str.equals("reset")) {
                        obj = 1;
                        break;
                    }
                    break;
            }
            switch (obj) {
                case null:
                    flags |= 1;
                    break;
                case 1:
                    flags |= 2;
                    break;
                default:
                    break;
            }
        }
        nDumpProfileInfo(this.mNativeProxy, fd, flags);
    }

    boolean loadSystemProperties() {
        boolean changed = nLoadSystemProperties(this.mNativeProxy);
        if (changed) {
            invalidateRoot();
        }
        return changed;
    }

    private void updateViewTreeDisplayList(View view) {
        view.mPrivateFlags |= 32;
        view.mRecreateDisplayList = (view.mPrivateFlags & Integer.MIN_VALUE) == Integer.MIN_VALUE;
        view.mPrivateFlags &= Integer.MAX_VALUE;
        view.updateDisplayListIfDirty();
        view.mRecreateDisplayList = false;
    }

    private void updateRootDisplayList(View view, HardwareDrawCallbacks callbacks) {
        Trace.traceBegin(8, "Record View#draw()");
        updateViewTreeDisplayList(view);
        if (this.mRootNodeNeedsUpdate || !this.mRootNode.isValid()) {
            DisplayListCanvas canvas = this.mRootNode.start(this.mSurfaceWidth, this.mSurfaceHeight);
            try {
                int saveCount = canvas.save();
                canvas.translate((float) this.mInsetLeft, (float) this.mInsetTop);
                callbacks.onHardwarePreDraw(canvas);
                canvas.insertReorderBarrier();
                canvas.drawRenderNode(view.updateDisplayListIfDirty());
                canvas.insertInorderBarrier();
                callbacks.onHardwarePostDraw(canvas);
                canvas.restoreToCount(saveCount);
                this.mRootNodeNeedsUpdate = false;
            } finally {
                this.mRootNode.end(canvas);
            }
        }
        Trace.traceEnd(8);
    }

    void invalidateRoot() {
        this.mRootNodeNeedsUpdate = true;
    }

    void draw(View view, AttachInfo attachInfo, HardwareDrawCallbacks callbacks) {
        attachInfo.mIgnoreDirtyState = true;
        Choreographer choreographer = attachInfo.mViewRootImpl.mChoreographer;
        choreographer.mFrameInfo.markDrawStart();
        updateRootDisplayList(view, callbacks);
        attachInfo.mIgnoreDirtyState = false;
        if (attachInfo.mPendingAnimatingRenderNodes != null) {
            int count = attachInfo.mPendingAnimatingRenderNodes.size();
            for (int i = 0; i < count; i++) {
                registerAnimatingRenderNode((RenderNode) attachInfo.mPendingAnimatingRenderNodes.get(i));
            }
            attachInfo.mPendingAnimatingRenderNodes.clear();
            attachInfo.mPendingAnimatingRenderNodes = null;
        }
        long[] frameInfo = choreographer.mFrameInfo.mFrameInfo;
        if (SAFE_DEBUG) {
            Trace.traceBegin(8, "nSyncAndDrawFrame");
        }
        int syncResult = nSyncAndDrawFrame(this.mNativeProxy, frameInfo, frameInfo.length);
        if (SAFE_DEBUG) {
            Trace.traceEnd(8);
        }
        if ((syncResult & 2) != 0) {
            setEnabled(false);
            attachInfo.mViewRootImpl.mSurface.release();
            attachInfo.mViewRootImpl.invalidate();
        }
        if ((syncResult & 1) != 0) {
            attachInfo.mViewRootImpl.invalidate();
        }
    }

    static void invokeFunctor(long functor, boolean waitForCompletion) {
        nInvokeFunctor(functor, waitForCompletion);
    }

    HardwareLayer createTextureLayer() {
        return HardwareLayer.adoptTextureLayer(this, nCreateTextureLayer(this.mNativeProxy));
    }

    void buildLayer(RenderNode node) {
        nBuildLayer(this.mNativeProxy, node.getNativeDisplayList());
    }

    boolean copyLayerInto(HardwareLayer layer, Bitmap bitmap) {
        return nCopyLayerInto(this.mNativeProxy, layer.getDeferredLayerUpdater(), bitmap);
    }

    void pushLayerUpdate(HardwareLayer layer) {
        nPushLayerUpdate(this.mNativeProxy, layer.getDeferredLayerUpdater());
    }

    void onLayerDestroyed(HardwareLayer layer) {
        nCancelLayerUpdate(this.mNativeProxy, layer.getDeferredLayerUpdater());
    }

    void setName(String name) {
        nSetName(this.mNativeProxy, name);
    }

    void fence() {
        nFence(this.mNativeProxy);
    }

    void stopDrawing() {
        nStopDrawing(this.mNativeProxy);
    }

    public void notifyFramePending() {
        nNotifyFramePending(this.mNativeProxy);
    }

    void registerAnimatingRenderNode(RenderNode animator) {
        nRegisterAnimatingRenderNode(this.mRootNode.mNativeRenderNode, animator.mNativeRenderNode);
    }

    protected void finalize() throws Throwable {
        try {
            nDeleteProxy(this.mNativeProxy);
            this.mNativeProxy = 0;
        } finally {
            super.finalize();
        }
    }

    static void trimMemory(int level) {
        nTrimMemory(level);
    }

    public static void overrideProperty(String name, String value) {
        if (name == null || value == null) {
            throw new IllegalArgumentException("name and value must be non-null");
        }
        nOverrideProperty(name, value);
    }

    public static void dumpProfileData(byte[] data, FileDescriptor fd) {
        nDumpProfileData(data, fd);
    }
}
