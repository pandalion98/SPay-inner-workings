package android.view;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.SurfaceTexture;
import com.android.internal.util.VirtualRefBasePtr;

final class HardwareLayer {
    private VirtualRefBasePtr mFinalizer;
    private HardwareRenderer mRenderer;

    private static native int nGetTexName(long j);

    private static native boolean nPrepare(long j, int i, int i2, boolean z);

    private static native void nSetLayerPaint(long j, long j2);

    private static native void nSetSurfaceTexture(long j, SurfaceTexture surfaceTexture, boolean z);

    private static native void nSetTransform(long j, long j2);

    private static native void nUpdateRenderLayer(long j, long j2, int i, int i2, int i3, int i4);

    private static native void nUpdateSurfaceTexture(long j);

    private HardwareLayer(HardwareRenderer renderer, long deferredUpdater) {
        if (renderer == null || deferredUpdater == 0) {
            throw new IllegalArgumentException("Either hardware renderer: " + renderer + " or deferredUpdater: " + deferredUpdater + " is invalid");
        }
        this.mRenderer = renderer;
        this.mFinalizer = new VirtualRefBasePtr(deferredUpdater);
    }

    public void setLayerPaint(Paint paint) {
        nSetLayerPaint(this.mFinalizer.get(), paint.getNativeInstance());
        this.mRenderer.pushLayerUpdate(this);
    }

    public boolean isValid() {
        return (this.mFinalizer == null || this.mFinalizer.get() == 0) ? false : true;
    }

    public void destroy() {
        if (isValid()) {
            this.mRenderer.onLayerDestroyed(this);
            this.mRenderer = null;
            this.mFinalizer.release();
            this.mFinalizer = null;
        }
    }

    public long getDeferredLayerUpdater() {
        return this.mFinalizer.get();
    }

    public boolean copyInto(Bitmap bitmap) {
        return this.mRenderer.copyLayerInto(this, bitmap);
    }

    public boolean prepare(int width, int height, boolean isOpaque) {
        return nPrepare(this.mFinalizer.get(), width, height, isOpaque);
    }

    public void setTransform(Matrix matrix) {
        nSetTransform(this.mFinalizer.get(), matrix.native_instance);
        this.mRenderer.pushLayerUpdate(this);
    }

    public void detachSurfaceTexture() {
        this.mRenderer.detachSurfaceTexture(this.mFinalizer.get());
    }

    public long getLayerHandle() {
        return this.mFinalizer.get();
    }

    public void setSurfaceTexture(SurfaceTexture surface) {
        nSetSurfaceTexture(this.mFinalizer.get(), surface, false);
        this.mRenderer.pushLayerUpdate(this);
    }

    public void updateSurfaceTexture() {
        nUpdateSurfaceTexture(this.mFinalizer.get());
        this.mRenderer.pushLayerUpdate(this);
    }

    static HardwareLayer adoptTextureLayer(HardwareRenderer renderer, long layer) {
        return new HardwareLayer(renderer, layer);
    }
}
