package android.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.CanvasProperty;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Pools.SynchronizedPool;

public class DisplayListCanvas extends Canvas {
    private static final int POOL_LIMIT = 25;
    private static boolean sIsAvailable = nIsAvailable();
    private static final SynchronizedPool<DisplayListCanvas> sPool = new SynchronizedPool(25);
    private int mHeight;
    RenderNode mNode;
    private int mWidth;

    private static native void nCallDrawGLFunction(long j, long j2);

    private static native long nCreateDisplayListCanvas();

    private static native void nDrawCircle(long j, long j2, long j3, long j4, long j5);

    private static native void nDrawLayer(long j, long j2, float f, float f2);

    private static native void nDrawPatch(long j, Bitmap bitmap, long j2, float f, float f2, float f3, float f4, long j3);

    private static native void nDrawRects(long j, long j2, long j3);

    private static native void nDrawRenderNode(long j, long j2);

    private static native void nDrawRoundRect(long j, long j2, long j3, long j4, long j5, long j6, long j7, long j8);

    private static native void nFinish(long j);

    protected static native long nFinishRecording(long j);

    private static native boolean nGetHighContrastText(long j);

    private static native int nGetMaximumTextureHeight();

    private static native int nGetMaximumTextureWidth();

    private static native void nInsertReorderBarrier(long j, boolean z);

    private static native boolean nIsAvailable();

    private static native void nPrepare(long j);

    private static native void nPrepareDirty(long j, int i, int i2, int i3, int i4);

    private static native void nSetHighContrastText(long j, boolean z);

    private static native void nSetViewport(long j, int i, int i2);

    static DisplayListCanvas obtain(RenderNode node) {
        if (node == null) {
            throw new IllegalArgumentException("node cannot be null");
        }
        DisplayListCanvas canvas = (DisplayListCanvas) sPool.acquire();
        if (canvas == null) {
            canvas = new DisplayListCanvas();
        }
        canvas.mNode = node;
        return canvas;
    }

    void recycle() {
        this.mNode = null;
        sPool.release(this);
    }

    long finishRecording() {
        return nFinishRecording(this.mNativeCanvasWrapper);
    }

    public boolean isRecordingFor(Object o) {
        return o == this.mNode;
    }

    static boolean isAvailable() {
        return sIsAvailable;
    }

    private DisplayListCanvas() {
        super(nCreateDisplayListCanvas());
        this.mDensity = 0;
    }

    public void setDensity(int density) {
    }

    public boolean isHardwareAccelerated() {
        return true;
    }

    public void setBitmap(Bitmap bitmap) {
        throw new UnsupportedOperationException();
    }

    public boolean isOpaque() {
        return false;
    }

    public int getWidth() {
        return this.mWidth;
    }

    public int getHeight() {
        return this.mHeight;
    }

    public int getMaximumBitmapWidth() {
        return nGetMaximumTextureWidth();
    }

    public int getMaximumBitmapHeight() {
        return nGetMaximumTextureHeight();
    }

    long getRenderer() {
        return this.mNativeCanvasWrapper;
    }

    public void setViewport(int width, int height) {
        this.mWidth = width;
        this.mHeight = height;
        nSetViewport(this.mNativeCanvasWrapper, width, height);
    }

    public void setHighContrastText(boolean highContrastText) {
        nSetHighContrastText(this.mNativeCanvasWrapper, highContrastText);
    }

    public boolean getHighContrastText() {
        return nGetHighContrastText(this.mNativeCanvasWrapper);
    }

    public void insertReorderBarrier() {
        nInsertReorderBarrier(this.mNativeCanvasWrapper, true);
    }

    public void insertInorderBarrier() {
        nInsertReorderBarrier(this.mNativeCanvasWrapper, false);
    }

    public void onPreDraw(Rect dirty) {
        if (dirty != null) {
            nPrepareDirty(this.mNativeCanvasWrapper, dirty.left, dirty.top, dirty.right, dirty.bottom);
        } else {
            nPrepare(this.mNativeCanvasWrapper);
        }
    }

    public void onPostDraw() {
        nFinish(this.mNativeCanvasWrapper);
    }

    public void callDrawGLFunction2(long drawGLFunction) {
        nCallDrawGLFunction(this.mNativeCanvasWrapper, drawGLFunction);
    }

    public void drawRenderNode(RenderNode renderNode) {
        nDrawRenderNode(this.mNativeCanvasWrapper, renderNode.getNativeDisplayList());
    }

    void drawHardwareLayer(HardwareLayer layer, float x, float y, Paint paint) {
        layer.setLayerPaint(paint);
        nDrawLayer(this.mNativeCanvasWrapper, layer.getLayerHandle(), x, y);
    }

    public void drawPatch(NinePatch patch, Rect dst, Paint paint) {
        Bitmap bitmap = patch.getBitmap();
        throwIfCannotDraw(bitmap);
        nDrawPatch(this.mNativeCanvasWrapper, bitmap, patch.mNativeChunk, (float) dst.left, (float) dst.top, (float) dst.right, (float) dst.bottom, paint == null ? 0 : paint.getNativeInstance());
    }

    public void drawPatch(NinePatch patch, RectF dst, Paint paint) {
        Bitmap bitmap = patch.getBitmap();
        throwIfCannotDraw(bitmap);
        nDrawPatch(this.mNativeCanvasWrapper, bitmap, patch.mNativeChunk, dst.left, dst.top, dst.right, dst.bottom, paint == null ? 0 : paint.getNativeInstance());
    }

    public void drawCircle(CanvasProperty<Float> cx, CanvasProperty<Float> cy, CanvasProperty<Float> radius, CanvasProperty<Paint> paint) {
        nDrawCircle(this.mNativeCanvasWrapper, cx.getNativeContainer(), cy.getNativeContainer(), radius.getNativeContainer(), paint.getNativeContainer());
    }

    public void drawRoundRect(CanvasProperty<Float> left, CanvasProperty<Float> top, CanvasProperty<Float> right, CanvasProperty<Float> bottom, CanvasProperty<Float> rx, CanvasProperty<Float> ry, CanvasProperty<Paint> paint) {
        nDrawRoundRect(this.mNativeCanvasWrapper, left.getNativeContainer(), top.getNativeContainer(), right.getNativeContainer(), bottom.getNativeContainer(), rx.getNativeContainer(), ry.getNativeContainer(), paint.getNativeContainer());
    }

    public void drawPath(Path path, Paint paint) {
        if (!path.isSimplePath) {
            super.drawPath(path, paint);
        } else if (path.rects != null) {
            nDrawRects(this.mNativeCanvasWrapper, path.rects.mNativeRegion, paint.getNativeInstance());
        }
    }
}
