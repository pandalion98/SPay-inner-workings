package android.graphics;

public class NinePatch {
    private final Bitmap mBitmap;
    public final long mNativeChunk;
    private Paint mPaint;
    private String mSrcName;

    public static class InsetStruct {
        public final Rect opticalRect;
        public final float outlineAlpha;
        public final float outlineRadius;
        public final Rect outlineRect;

        InsetStruct(int opticalLeft, int opticalTop, int opticalRight, int opticalBottom, int outlineLeft, int outlineTop, int outlineRight, int outlineBottom, float outlineRadius, int outlineAlpha, float decodeScale) {
            this.opticalRect = new Rect(opticalLeft, opticalTop, opticalRight, opticalBottom);
            this.outlineRect = new Rect(outlineLeft, outlineTop, outlineRight, outlineBottom);
            if (decodeScale != 1.0f) {
                this.opticalRect.scale(decodeScale);
                this.outlineRect.scaleRoundIn(decodeScale);
            }
            this.outlineRadius = outlineRadius * decodeScale;
            this.outlineAlpha = ((float) outlineAlpha) / 255.0f;
        }
    }

    public static native boolean isNinePatchChunk(byte[] bArr);

    private static native void nativeDraw(long j, Rect rect, Bitmap bitmap, long j2, long j3, int i, int i2);

    private static native void nativeDraw(long j, RectF rectF, Bitmap bitmap, long j2, long j3, int i, int i2);

    private static native void nativeFinalize(long j);

    private static native long nativeGetTransparentRegion(Bitmap bitmap, long j, Rect rect);

    private static native long validateNinePatchChunk(byte[] bArr);

    public NinePatch(Bitmap bitmap, byte[] chunk) {
        this(bitmap, chunk, null);
    }

    public NinePatch(Bitmap bitmap, byte[] chunk, String srcName) {
        this.mBitmap = bitmap;
        this.mSrcName = srcName;
        this.mNativeChunk = validateNinePatchChunk(chunk);
    }

    public NinePatch(NinePatch patch) {
        this.mBitmap = patch.mBitmap;
        this.mSrcName = patch.mSrcName;
        if (patch.mPaint != null) {
            this.mPaint = new Paint(patch.mPaint);
        }
        this.mNativeChunk = patch.mNativeChunk;
    }

    protected void finalize() throws Throwable {
        try {
            if (this.mNativeChunk != 0) {
                nativeFinalize(this.mNativeChunk);
            }
            super.finalize();
        } catch (Throwable th) {
            super.finalize();
        }
    }

    public String getName() {
        return this.mSrcName;
    }

    public Paint getPaint() {
        return this.mPaint;
    }

    public void setPaint(Paint p) {
        this.mPaint = p;
    }

    public Bitmap getBitmap() {
        return this.mBitmap;
    }

    public void draw(Canvas canvas, RectF location) {
        canvas.drawPatch(this, location, this.mPaint);
    }

    public void draw(Canvas canvas, Rect location) {
        canvas.drawPatch(this, location, this.mPaint);
    }

    public void draw(Canvas canvas, Rect location, Paint paint) {
        canvas.drawPatch(this, location, paint);
    }

    void drawSoftware(Canvas canvas, RectF location, Paint paint) {
        nativeDraw(canvas.getNativeCanvasWrapper(), location, this.mBitmap, this.mNativeChunk, paint != null ? paint.getNativeInstance() : 0, canvas.mDensity, this.mBitmap.mDensity);
    }

    void drawSoftware(Canvas canvas, Rect location, Paint paint) {
        nativeDraw(canvas.getNativeCanvasWrapper(), location, this.mBitmap, this.mNativeChunk, paint != null ? paint.getNativeInstance() : 0, canvas.mDensity, this.mBitmap.mDensity);
    }

    public int getDensity() {
        return this.mBitmap.mDensity;
    }

    public int getWidth() {
        return this.mBitmap.getWidth();
    }

    public int getHeight() {
        return this.mBitmap.getHeight();
    }

    public final boolean hasAlpha() {
        return this.mBitmap.hasAlpha();
    }

    public final Region getTransparentRegion(Rect bounds) {
        long r = nativeGetTransparentRegion(this.mBitmap, this.mNativeChunk, bounds);
        return r != 0 ? new Region(r) : null;
    }
}
