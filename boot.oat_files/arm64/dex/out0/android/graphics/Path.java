package android.graphics;

public class Path {
    static final FillType[] sFillTypeArray = new FillType[]{FillType.WINDING, FillType.EVEN_ODD, FillType.INVERSE_WINDING, FillType.INVERSE_EVEN_ODD};
    public boolean isSimplePath;
    private Direction mLastDirection;
    public final long mNativePath;
    public Region rects;

    public enum Direction {
        CW(1),
        CCW(2);
        
        final int nativeInt;

        private Direction(int ni) {
            this.nativeInt = ni;
        }
    }

    public enum FillType {
        WINDING(0),
        EVEN_ODD(1),
        INVERSE_WINDING(2),
        INVERSE_EVEN_ODD(3);
        
        final int nativeInt;

        private FillType(int ni) {
            this.nativeInt = ni;
        }
    }

    public enum Op {
        DIFFERENCE,
        INTERSECT,
        UNION,
        XOR,
        REVERSE_DIFFERENCE
    }

    private static native void finalizer(long j);

    private static native long init1();

    private static native long init2(long j);

    private static native void native_addArc(long j, float f, float f2, float f3, float f4, float f5, float f6);

    private static native void native_addCircle(long j, float f, float f2, float f3, int i);

    private static native void native_addOval(long j, float f, float f2, float f3, float f4, int i);

    private static native void native_addPath(long j, long j2);

    private static native void native_addPath(long j, long j2, float f, float f2);

    private static native void native_addPath(long j, long j2, long j3);

    private static native void native_addRect(long j, float f, float f2, float f3, float f4, int i);

    private static native void native_addRoundRect(long j, float f, float f2, float f3, float f4, float f5, float f6, int i);

    private static native void native_addRoundRect(long j, float f, float f2, float f3, float f4, float[] fArr, int i);

    private static native float[] native_approximate(long j, float f);

    private static native void native_arcTo(long j, float f, float f2, float f3, float f4, float f5, float f6, boolean z);

    private static native void native_close(long j);

    private static native void native_computeBounds(long j, RectF rectF);

    private static native void native_cubicTo(long j, float f, float f2, float f3, float f4, float f5, float f6);

    private static native int native_getFillType(long j);

    private static native void native_incReserve(long j, int i);

    private static native boolean native_isConvex(long j);

    private static native boolean native_isEmpty(long j);

    private static native boolean native_isRect(long j, RectF rectF);

    private static native void native_lineTo(long j, float f, float f2);

    private static native void native_moveTo(long j, float f, float f2);

    private static native void native_offset(long j, float f, float f2);

    private static native void native_offset(long j, float f, float f2, long j2);

    private static native boolean native_op(long j, long j2, int i, long j3);

    private static native void native_quadTo(long j, float f, float f2, float f3, float f4);

    private static native void native_rCubicTo(long j, float f, float f2, float f3, float f4, float f5, float f6);

    private static native void native_rLineTo(long j, float f, float f2);

    private static native void native_rMoveTo(long j, float f, float f2);

    private static native void native_rQuadTo(long j, float f, float f2, float f3, float f4);

    private static native void native_reset(long j);

    private static native void native_rewind(long j);

    private static native void native_set(long j, long j2);

    private static native void native_setFillType(long j, int i);

    private static native void native_setLastPoint(long j, float f, float f2);

    private static native void native_transform(long j, long j2);

    private static native void native_transform(long j, long j2, long j3);

    public Path() {
        this.isSimplePath = true;
        this.mLastDirection = null;
        this.mNativePath = init1();
    }

    public Path(Path src) {
        this.isSimplePath = true;
        this.mLastDirection = null;
        long valNative = 0;
        if (src != null) {
            valNative = src.mNativePath;
            this.isSimplePath = src.isSimplePath;
            if (src.rects != null) {
                this.rects = new Region(src.rects);
            }
        }
        this.mNativePath = init2(valNative);
    }

    public void reset() {
        this.isSimplePath = true;
        this.mLastDirection = null;
        if (this.rects != null) {
            this.rects.setEmpty();
        }
        FillType fillType = getFillType();
        native_reset(this.mNativePath);
        setFillType(fillType);
    }

    public void rewind() {
        this.isSimplePath = true;
        this.mLastDirection = null;
        if (this.rects != null) {
            this.rects.setEmpty();
        }
        native_rewind(this.mNativePath);
    }

    public void set(Path src) {
        if (this != src) {
            this.isSimplePath = src.isSimplePath;
            native_set(this.mNativePath, src.mNativePath);
        }
    }

    public boolean op(Path path, Op op) {
        return op(this, path, op);
    }

    public boolean op(Path path1, Path path2, Op op) {
        if (!native_op(path1.mNativePath, path2.mNativePath, op.ordinal(), this.mNativePath)) {
            return false;
        }
        this.isSimplePath = false;
        this.rects = null;
        return true;
    }

    public boolean isConvex() {
        return native_isConvex(this.mNativePath);
    }

    public FillType getFillType() {
        return sFillTypeArray[native_getFillType(this.mNativePath)];
    }

    public void setFillType(FillType ft) {
        native_setFillType(this.mNativePath, ft.nativeInt);
    }

    public boolean isInverseFillType() {
        return (FillType.INVERSE_WINDING.nativeInt & native_getFillType(this.mNativePath)) != 0;
    }

    public void toggleInverseFillType() {
        native_setFillType(this.mNativePath, native_getFillType(this.mNativePath) ^ FillType.INVERSE_WINDING.nativeInt);
    }

    public boolean isEmpty() {
        return native_isEmpty(this.mNativePath);
    }

    public boolean isRect(RectF rect) {
        return native_isRect(this.mNativePath, rect);
    }

    public void computeBounds(RectF bounds, boolean exact) {
        native_computeBounds(this.mNativePath, bounds);
    }

    public void incReserve(int extraPtCount) {
        native_incReserve(this.mNativePath, extraPtCount);
    }

    public void moveTo(float x, float y) {
        native_moveTo(this.mNativePath, x, y);
    }

    public void rMoveTo(float dx, float dy) {
        native_rMoveTo(this.mNativePath, dx, dy);
    }

    public void lineTo(float x, float y) {
        this.isSimplePath = false;
        native_lineTo(this.mNativePath, x, y);
    }

    public void rLineTo(float dx, float dy) {
        this.isSimplePath = false;
        native_rLineTo(this.mNativePath, dx, dy);
    }

    public void quadTo(float x1, float y1, float x2, float y2) {
        this.isSimplePath = false;
        native_quadTo(this.mNativePath, x1, y1, x2, y2);
    }

    public void rQuadTo(float dx1, float dy1, float dx2, float dy2) {
        this.isSimplePath = false;
        native_rQuadTo(this.mNativePath, dx1, dy1, dx2, dy2);
    }

    public void cubicTo(float x1, float y1, float x2, float y2, float x3, float y3) {
        this.isSimplePath = false;
        native_cubicTo(this.mNativePath, x1, y1, x2, y2, x3, y3);
    }

    public void rCubicTo(float x1, float y1, float x2, float y2, float x3, float y3) {
        this.isSimplePath = false;
        native_rCubicTo(this.mNativePath, x1, y1, x2, y2, x3, y3);
    }

    public void arcTo(RectF oval, float startAngle, float sweepAngle, boolean forceMoveTo) {
        arcTo(oval.left, oval.top, oval.right, oval.bottom, startAngle, sweepAngle, forceMoveTo);
    }

    public void arcTo(RectF oval, float startAngle, float sweepAngle) {
        arcTo(oval.left, oval.top, oval.right, oval.bottom, startAngle, sweepAngle, false);
    }

    public void arcTo(float left, float top, float right, float bottom, float startAngle, float sweepAngle, boolean forceMoveTo) {
        this.isSimplePath = false;
        native_arcTo(this.mNativePath, left, top, right, bottom, startAngle, sweepAngle, forceMoveTo);
    }

    public void close() {
        this.isSimplePath = false;
        native_close(this.mNativePath);
    }

    private void detectSimplePath(float left, float top, float right, float bottom, Direction dir) {
        if (this.mLastDirection == null) {
            this.mLastDirection = dir;
        }
        if (this.mLastDirection != dir) {
            this.isSimplePath = false;
            return;
        }
        if (this.rects == null) {
            this.rects = new Region();
        }
        this.rects.op((int) left, (int) top, (int) right, (int) bottom, android.graphics.Region.Op.UNION);
    }

    public void addRect(RectF rect, Direction dir) {
        addRect(rect.left, rect.top, rect.right, rect.bottom, dir);
    }

    public void addRect(float left, float top, float right, float bottom, Direction dir) {
        detectSimplePath(left, top, right, bottom, dir);
        native_addRect(this.mNativePath, left, top, right, bottom, dir.nativeInt);
    }

    public void addOval(RectF oval, Direction dir) {
        addOval(oval.left, oval.top, oval.right, oval.bottom, dir);
    }

    public void addOval(float left, float top, float right, float bottom, Direction dir) {
        this.isSimplePath = false;
        native_addOval(this.mNativePath, left, top, right, bottom, dir.nativeInt);
    }

    public void addCircle(float x, float y, float radius, Direction dir) {
        this.isSimplePath = false;
        native_addCircle(this.mNativePath, x, y, radius, dir.nativeInt);
    }

    public void addArc(RectF oval, float startAngle, float sweepAngle) {
        addArc(oval.left, oval.top, oval.right, oval.bottom, startAngle, sweepAngle);
    }

    public void addArc(float left, float top, float right, float bottom, float startAngle, float sweepAngle) {
        this.isSimplePath = false;
        native_addArc(this.mNativePath, left, top, right, bottom, startAngle, sweepAngle);
    }

    public void addRoundRect(RectF rect, float rx, float ry, Direction dir) {
        addRoundRect(rect.left, rect.top, rect.right, rect.bottom, rx, ry, dir);
    }

    public void addRoundRect(float left, float top, float right, float bottom, float rx, float ry, Direction dir) {
        this.isSimplePath = false;
        native_addRoundRect(this.mNativePath, left, top, right, bottom, rx, ry, dir.nativeInt);
    }

    public void addRoundRect(RectF rect, float[] radii, Direction dir) {
        if (rect == null) {
            throw new NullPointerException("need rect parameter");
        }
        addRoundRect(rect.left, rect.top, rect.right, rect.bottom, radii, dir);
    }

    public void addRoundRect(float left, float top, float right, float bottom, float[] radii, Direction dir) {
        if (radii.length < 8) {
            throw new ArrayIndexOutOfBoundsException("radii[] needs 8 values");
        }
        this.isSimplePath = false;
        native_addRoundRect(this.mNativePath, left, top, right, bottom, radii, dir.nativeInt);
    }

    public void addPath(Path src, float dx, float dy) {
        this.isSimplePath = false;
        native_addPath(this.mNativePath, src.mNativePath, dx, dy);
    }

    public void addPath(Path src) {
        this.isSimplePath = false;
        native_addPath(this.mNativePath, src.mNativePath);
    }

    public void addPath(Path src, Matrix matrix) {
        if (!src.isSimplePath) {
            this.isSimplePath = false;
        }
        native_addPath(this.mNativePath, src.mNativePath, matrix.native_instance);
    }

    public void offset(float dx, float dy, Path dst) {
        long dstNative = 0;
        if (dst != null) {
            dstNative = dst.mNativePath;
            dst.isSimplePath = false;
        }
        native_offset(this.mNativePath, dx, dy, dstNative);
    }

    public void offset(float dx, float dy) {
        this.isSimplePath = false;
        native_offset(this.mNativePath, dx, dy);
    }

    public void setLastPoint(float dx, float dy) {
        this.isSimplePath = false;
        native_setLastPoint(this.mNativePath, dx, dy);
    }

    public void transform(Matrix matrix, Path dst) {
        long dstNative = 0;
        if (dst != null) {
            dst.isSimplePath = false;
            dstNative = dst.mNativePath;
        }
        native_transform(this.mNativePath, matrix.native_instance, dstNative);
    }

    public void transform(Matrix matrix) {
        this.isSimplePath = false;
        native_transform(this.mNativePath, matrix.native_instance);
    }

    protected void finalize() throws Throwable {
        try {
            finalizer(this.mNativePath);
        } finally {
            super.finalize();
        }
    }

    final long ni() {
        return this.mNativePath;
    }

    public float[] approximate(float acceptableError) {
        return native_approximate(this.mNativePath, acceptableError);
    }
}
