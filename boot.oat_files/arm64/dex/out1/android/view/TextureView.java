package android.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.graphics.SurfaceTexture.OnFrameAvailableListener;
import android.util.AttributeSet;
import android.util.Log;

public class TextureView extends View {
    private static final String LOG_TAG = "TextureView";
    private Canvas mCanvas;
    private boolean mHadSurface;
    private HardwareLayer mLayer;
    private SurfaceTextureListener mListener;
    private final Object[] mLock;
    private final Matrix mMatrix;
    private boolean mMatrixChanged;
    private long mNativeWindow;
    private final Object[] mNativeWindowLock;
    private boolean mOpaque;
    private int mSaveCount;
    private SurfaceTexture mSurface;
    private boolean mUpdateLayer;
    private final OnFrameAvailableListener mUpdateListener;
    private boolean mUpdateSurface;

    public interface SurfaceTextureListener {
        void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i2);

        boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture);

        void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2);

        void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture);
    }

    private native void nCreateNativeWindow(SurfaceTexture surfaceTexture);

    private native void nDestroyNativeWindow();

    private static native boolean nLockCanvas(long j, Canvas canvas, Rect rect);

    private static native void nUnlockCanvasAndPost(long j, Canvas canvas);

    public TextureView(Context context) {
        super(context);
        this.mOpaque = true;
        this.mMatrix = new Matrix();
        this.mLock = new Object[0];
        this.mNativeWindowLock = new Object[0];
        this.mUpdateListener = new OnFrameAvailableListener() {
            public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                TextureView.this.updateLayer();
                TextureView.this.invalidate();
            }
        };
        init();
    }

    public TextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mOpaque = true;
        this.mMatrix = new Matrix();
        this.mLock = new Object[0];
        this.mNativeWindowLock = new Object[0];
        this.mUpdateListener = /* anonymous class already generated */;
        init();
    }

    public TextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mOpaque = true;
        this.mMatrix = new Matrix();
        this.mLock = new Object[0];
        this.mNativeWindowLock = new Object[0];
        this.mUpdateListener = /* anonymous class already generated */;
        init();
    }

    public TextureView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mOpaque = true;
        this.mMatrix = new Matrix();
        this.mLock = new Object[0];
        this.mNativeWindowLock = new Object[0];
        this.mUpdateListener = /* anonymous class already generated */;
        init();
    }

    private void init() {
        this.mLayerPaint = new Paint();
    }

    public boolean isOpaque() {
        return this.mOpaque;
    }

    public void setOpaque(boolean opaque) {
        if (opaque != this.mOpaque) {
            this.mOpaque = opaque;
            if (this.mLayer != null) {
                updateLayerAndInvalidate();
            }
        }
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isHardwareAccelerated()) {
            Log.w(LOG_TAG, "A TextureView or a subclass can only be used with hardware acceleration enabled.");
        }
        if (this.mHadSurface) {
            invalidate(true);
            this.mHadSurface = false;
        }
    }

    protected void onDetachedFromWindowInternal() {
        destroySurface();
        super.onDetachedFromWindowInternal();
    }

    private void destroySurface() {
        if (this.mLayer != null) {
            this.mLayer.detachSurfaceTexture();
            boolean shouldRelease = true;
            if (this.mListener != null) {
                shouldRelease = this.mListener.onSurfaceTextureDestroyed(this.mSurface);
            }
            synchronized (this.mNativeWindowLock) {
                nDestroyNativeWindow();
            }
            this.mLayer.destroy();
            if (shouldRelease) {
                this.mSurface.release();
            }
            this.mSurface = null;
            this.mLayer = null;
            this.mMatrixChanged = true;
            this.mHadSurface = true;
        }
    }

    public void setLayerType(int layerType, Paint paint) {
        if (paint != this.mLayerPaint) {
            if (paint == null) {
                paint = new Paint();
            }
            this.mLayerPaint = paint;
            invalidate();
        }
    }

    public void setLayerPaint(Paint paint) {
        setLayerType(0, paint);
    }

    public int getLayerType() {
        return 2;
    }

    public void buildLayer() {
    }

    public final void draw(Canvas canvas) {
        this.mPrivateFlags = (this.mPrivateFlags & -6291457) | 32;
        applyUpdate();
        applyTransformMatrix();
    }

    protected final void onDraw(Canvas canvas) {
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (this.mSurface != null) {
            this.mSurface.setDefaultBufferSize(getWidth(), getHeight());
            updateLayer();
            if (this.mListener != null) {
                this.mListener.onSurfaceTextureSizeChanged(this.mSurface, getWidth(), getHeight());
            }
        }
    }

    protected void destroyHardwareResources() {
        super.destroyHardwareResources();
        destroySurface();
        invalidateParentCaches();
        invalidate(true);
    }

    HardwareLayer getHardwareLayer() {
        this.mPrivateFlags |= 32800;
        this.mPrivateFlags &= -6291457;
        if (this.mLayer == null) {
            if (this.mAttachInfo == null || this.mAttachInfo.mHardwareRenderer == null) {
                return null;
            }
            this.mLayer = this.mAttachInfo.mHardwareRenderer.createTextureLayer();
            if (!this.mUpdateSurface) {
                this.mSurface = new SurfaceTexture(false);
                this.mLayer.setSurfaceTexture(this.mSurface);
            }
            this.mSurface.setDefaultBufferSize(getWidth(), getHeight());
            nCreateNativeWindow(this.mSurface);
            this.mSurface.setOnFrameAvailableListener(this.mUpdateListener, this.mAttachInfo.mHandler);
            if (!(this.mListener == null || this.mUpdateSurface)) {
                this.mListener.onSurfaceTextureAvailable(this.mSurface, getWidth(), getHeight());
            }
            this.mLayer.setLayerPaint(this.mLayerPaint);
        }
        if (this.mUpdateSurface) {
            this.mUpdateSurface = false;
            updateLayer();
            this.mMatrixChanged = true;
            this.mLayer.setSurfaceTexture(this.mSurface);
            this.mSurface.setDefaultBufferSize(getWidth(), getHeight());
        }
        applyUpdate();
        applyTransformMatrix();
        return this.mLayer;
    }

    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (this.mSurface == null) {
            return;
        }
        if (visibility == 0) {
            if (this.mLayer != null) {
                this.mSurface.setOnFrameAvailableListener(this.mUpdateListener, this.mAttachInfo.mHandler);
            }
            updateLayerAndInvalidate();
            return;
        }
        this.mSurface.setOnFrameAvailableListener(null);
    }

    private void updateLayer() {
        synchronized (this.mLock) {
            this.mUpdateLayer = true;
        }
    }

    private void updateLayerAndInvalidate() {
        synchronized (this.mLock) {
            this.mUpdateLayer = true;
        }
        invalidate();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void applyUpdate() {
        /*
        r4 = this;
        r0 = r4.mLayer;
        if (r0 != 0) goto L_0x0005;
    L_0x0004:
        return;
    L_0x0005:
        r1 = r4.mLock;
        monitor-enter(r1);
        r0 = r4.mUpdateLayer;	 Catch:{ all -> 0x0032 }
        if (r0 == 0) goto L_0x0030;
    L_0x000c:
        r0 = 0;
        r4.mUpdateLayer = r0;	 Catch:{ all -> 0x0032 }
        monitor-exit(r1);	 Catch:{ all -> 0x0032 }
        r0 = r4.mLayer;
        r1 = r4.getWidth();
        r2 = r4.getHeight();
        r3 = r4.mOpaque;
        r0.prepare(r1, r2, r3);
        r0 = r4.mLayer;
        r0.updateSurfaceTexture();
        r0 = r4.mListener;
        if (r0 == 0) goto L_0x0004;
    L_0x0028:
        r0 = r4.mListener;
        r1 = r4.mSurface;
        r0.onSurfaceTextureUpdated(r1);
        goto L_0x0004;
    L_0x0030:
        monitor-exit(r1);	 Catch:{ all -> 0x0032 }
        goto L_0x0004;
    L_0x0032:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0032 }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.TextureView.applyUpdate():void");
    }

    public void setTransform(Matrix transform) {
        this.mMatrix.set(transform);
        this.mMatrixChanged = true;
        invalidateParentIfNeeded();
    }

    public Matrix getTransform(Matrix transform) {
        if (transform == null) {
            transform = new Matrix();
        }
        transform.set(this.mMatrix);
        return transform;
    }

    private void applyTransformMatrix() {
        if (this.mMatrixChanged && this.mLayer != null) {
            this.mLayer.setTransform(this.mMatrix);
            this.mMatrixChanged = false;
        }
    }

    public Bitmap getBitmap() {
        return getBitmap(getWidth(), getHeight());
    }

    public Bitmap getBitmap(int width, int height) {
        if (!isAvailable() || width <= 0 || height <= 0) {
            return null;
        }
        return getBitmap(Bitmap.createBitmap(getResources().getDisplayMetrics(), width, height, Config.ARGB_8888));
    }

    public Bitmap getBitmap(Bitmap bitmap) {
        if (bitmap != null && isAvailable()) {
            applyUpdate();
            applyTransformMatrix();
            if (this.mLayer == null && this.mUpdateSurface) {
                getHardwareLayer();
            }
            if (this.mLayer != null) {
                this.mLayer.copyInto(bitmap);
            }
        }
        return bitmap;
    }

    public boolean isAvailable() {
        return this.mSurface != null;
    }

    public Canvas lockCanvas() {
        return lockCanvas(null);
    }

    public Canvas lockCanvas(Rect dirty) {
        if (!isAvailable()) {
            return null;
        }
        if (this.mCanvas == null) {
            this.mCanvas = new Canvas();
        }
        synchronized (this.mNativeWindowLock) {
            if (nLockCanvas(this.mNativeWindow, this.mCanvas, dirty)) {
                this.mSaveCount = this.mCanvas.save();
                return this.mCanvas;
            }
            return null;
        }
    }

    public void unlockCanvasAndPost(Canvas canvas) {
        if (this.mCanvas != null && canvas == this.mCanvas) {
            canvas.restoreToCount(this.mSaveCount);
            this.mSaveCount = 0;
            synchronized (this.mNativeWindowLock) {
                nUnlockCanvasAndPost(this.mNativeWindow, this.mCanvas);
            }
        }
    }

    public SurfaceTexture getSurfaceTexture() {
        return this.mSurface;
    }

    public void setSurfaceTexture(SurfaceTexture surfaceTexture) {
        if (surfaceTexture == null) {
            throw new NullPointerException("surfaceTexture must not be null");
        } else if (surfaceTexture == this.mSurface) {
            throw new IllegalArgumentException("Trying to setSurfaceTexture to the same SurfaceTexture that's already set.");
        } else if (surfaceTexture.isReleased()) {
            throw new IllegalArgumentException("Cannot setSurfaceTexture to a released SurfaceTexture");
        } else {
            if (this.mSurface != null) {
                this.mSurface.release();
            }
            this.mSurface = surfaceTexture;
            if ((this.mViewFlags & 12) == 0 && this.mLayer != null) {
                this.mSurface.setOnFrameAvailableListener(this.mUpdateListener, this.mAttachInfo.mHandler);
            }
            this.mUpdateSurface = true;
            invalidateParentIfNeeded();
        }
    }

    public SurfaceTextureListener getSurfaceTextureListener() {
        return this.mListener;
    }

    public void setSurfaceTextureListener(SurfaceTextureListener listener) {
        this.mListener = listener;
    }
}
