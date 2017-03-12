package android.graphics;

import android.graphics.NinePatch.InsetStruct;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.Trace;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import dalvik.system.VMRuntime;
import java.io.OutputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public final class Bitmap implements Parcelable {
    public static final Creator<Bitmap> CREATOR = new Creator<Bitmap>() {
        public Bitmap createFromParcel(Parcel p) {
            Bitmap bm = Bitmap.nativeCreateFromParcel(p);
            if (bm != null) {
                return bm;
            }
            throw new RuntimeException("Failed to unparcel Bitmap");
        }

        public Bitmap[] newArray(int size) {
            return new Bitmap[size];
        }
    };
    public static final int DENSITY_NONE = 0;
    private static final String TAG = "Bitmap";
    private static final int WORKING_COMPRESS_STORAGE = 4096;
    private static volatile int sDefaultDensity = -1;
    private static volatile Matrix sScaleMatrix;
    private byte[] mBuffer;
    int mDensity = getDefaultDensity();
    private final BitmapFinalizer mFinalizer;
    private int mHeight;
    private String mImagePath;
    private final boolean mIsMutable;
    private final long mNativePtr;
    private byte[] mNinePatchChunk;
    private InsetStruct mNinePatchInsets;
    private boolean mRecycled;
    private boolean mRequestPremultiplied;
    private int mWidth;

    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$android$graphics$Bitmap$Config = new int[Config.values().length];

        static {
            try {
                $SwitchMap$android$graphics$Bitmap$Config[Config.RGB_565.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$android$graphics$Bitmap$Config[Config.ALPHA_8.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$android$graphics$Bitmap$Config[Config.ARGB_4444.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$android$graphics$Bitmap$Config[Config.ARGB_8888.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    public enum AstcOptions {
        veryfast4x4lowPriority(0),
        veryfast4x4mediumPriority(1),
        veryfast4x4highPriority(2),
        veryfast6x6lowPriority(3),
        veryfast6x6mediumPriority(4),
        veryfast6x6highPriority(5),
        veryfast8x8lowPriority(6),
        veryfast8x8mediumPriority(7),
        veryfast8x8highPriority(8),
        veryfast12x12lowPriority(9),
        veryfast12x12mediumPriority(10),
        veryfast12x12highPriority(11),
        fast4x4lowPriority(12),
        fast4x4mediumPriority(13),
        fast4x4highPriority(14),
        fast6x6lowPriority(15),
        fast6x6mediumPriority(16),
        fast6x6highPriority(17),
        fast8x8lowPriority(18),
        fast8x8mediumPriority(19),
        fast8x8highPriority(20),
        fast12x12lowPriority(21),
        fast12x12mediumPriority(22),
        fast12x12highPriority(23),
        medium4x4lowPriority(24),
        medium4x4mediumPriority(25),
        medium4x4highPriority(26),
        medium6x6lowPriority(27),
        medium6x6mediumPriority(28),
        medium6x6highPriority(29),
        medium8x8lowPriority(30),
        medium8x8mediumPriority(31),
        medium8x8highPriority(32),
        medium12x12lowPriority(33),
        medium12x12mediumPriority(34),
        medium12x12highPriority(35),
        thorough4x4lowPriority(36),
        thorough4x4mediumPriority(37),
        thorough4x4highPriority(38),
        thorough6x6lowPriority(39),
        thorough6x6mediumPriority(40),
        thorough6x6highPriority(41),
        thorough8x8lowPriority(42),
        thorough8x8mediumPriority(43),
        thorough8x8highPriority(44),
        thorough12x12lowPriority(45),
        thorough12x12mediumPriority(46),
        thorough12x12highPriority(47);
        
        final int nativeValue;

        private AstcOptions(int value) {
            this.nativeValue = value;
        }

        public int getValue() {
            return this.nativeValue;
        }
    }

    private static class BitmapFinalizer {
        private int mNativeAllocationByteCount;
        private long mNativeBitmap;

        BitmapFinalizer(long nativeBitmap) {
            this.mNativeBitmap = nativeBitmap;
        }

        public void setNativeAllocationByteCount(int nativeByteCount) {
            if (this.mNativeAllocationByteCount != 0) {
                VMRuntime.getRuntime().registerNativeFree(this.mNativeAllocationByteCount);
            }
            this.mNativeAllocationByteCount = nativeByteCount;
            if (this.mNativeAllocationByteCount != 0) {
                VMRuntime.getRuntime().registerNativeAllocation(this.mNativeAllocationByteCount);
            }
        }

        public void finalize() {
            try {
                super.finalize();
            } catch (Throwable th) {
            } finally {
                setNativeAllocationByteCount(0);
                Bitmap.nativeDestructor(this.mNativeBitmap);
                this.mNativeBitmap = 0;
            }
        }
    }

    public enum CompressFormat {
        JPEG(0),
        PNG(1),
        WEBP(2);
        
        final int nativeInt;

        private CompressFormat(int nativeInt) {
            this.nativeInt = nativeInt;
        }
    }

    public enum Config {
        ALPHA_8(1),
        RGB_565(3),
        ARGB_4444(4),
        ARGB_8888(5);
        
        private static Config[] sConfigs;
        final int nativeInt;

        static {
            sConfigs = new Config[]{null, ALPHA_8, null, RGB_565, ARGB_4444, ARGB_8888};
        }

        private Config(int ni) {
            this.nativeInt = ni;
        }

        static Config nativeToConfig(int ni) {
            if (ni > ARGB_8888.nativeInt) {
                return null;
            }
            return sConfigs[ni];
        }
    }

    public enum ExtendedCompressFormat {
        ASTC(99),
        SPI(100);
        
        final int nativeInt;

        private ExtendedCompressFormat(int nativeInt) {
            this.nativeInt = nativeInt;
        }
    }

    public enum GLCompressionConfig {
        PVRTC2_2(8),
        ETC1_ALPHA(9),
        ETC1(10),
        ETC2_ALPHA(11),
        ETC2(12),
        ASTC(13);
        
        private static GLCompressionConfig[] compressedConfigLookup;
        final int nativeInt;

        static {
            compressedConfigLookup = new GLCompressionConfig[]{null, null, null, null, null, null, null, null, PVRTC2_2, ETC1_ALPHA, ETC1, ETC2_ALPHA, ETC2, ASTC};
        }

        private GLCompressionConfig(int ni) {
            this.nativeInt = ni;
        }

        static GLCompressionConfig nativeToConfig(int nativeConfig) {
            return compressedConfigLookup[nativeConfig];
        }
    }

    private static native boolean nativeCompress(long j, int i, int i2, OutputStream outputStream, byte[] bArr);

    private static native int nativeConfig(long j);

    private static native Bitmap nativeCopy(long j, int i, boolean z);

    private static native Bitmap nativeCopyAshmem(long j);

    private static native void nativeCopyPixelsFromBuffer(long j, Buffer buffer);

    private static native void nativeCopyPixelsToBuffer(long j, Buffer buffer);

    private static native Bitmap nativeCreate(int[] iArr, int i, int i2, int i3, int i4, int i5, boolean z);

    private static native Bitmap nativeCreateFromParcel(Parcel parcel);

    private static native void nativeDestructor(long j);

    private static native void nativeErase(long j, int i);

    private static native Bitmap nativeExtractAlpha(long j, long j2, int[] iArr);

    private static native int nativeGenerationId(long j);

    private static native int nativeGetPixel(long j, int i, int i2);

    private static native void nativeGetPixels(long j, int[] iArr, int i, int i2, int i3, int i4, int i5, int i6);

    private static native boolean nativeHasAlpha(long j);

    private static native boolean nativeHasMipMap(long j);

    private static native boolean nativeIsPremultiplied(long j);

    private static native void nativeReconfigure(long j, int i, int i2, int i3, int i4, boolean z);

    private static native boolean nativeRecycle(long j);

    private static native long nativeRefPixelRef(long j);

    private static native int nativeRowBytes(long j);

    private static native boolean nativeSameAs(long j, long j2);

    private static native void nativeSetHasAlpha(long j, boolean z, boolean z2);

    private static native void nativeSetHasMipMap(long j, boolean z);

    private static native void nativeSetPixel(long j, int i, int i2, int i3);

    private static native void nativeSetPixels(long j, int[] iArr, int i, int i2, int i3, int i4, int i5, int i6);

    private static native void nativeSetPremultiplied(long j, boolean z);

    private static native boolean nativeWriteToParcel(long j, boolean z, int i, Parcel parcel);

    public long getNativePtr() {
        return this.mNativePtr;
    }

    public String getImagePath() {
        return this.mImagePath;
    }

    public void setImagePath(String imagePath) {
        if (Build.IS_SYSTEM_SECURE) {
            this.mImagePath = imagePath;
        }
    }

    public void setImagePath(TypedValue value) {
        if (Build.IS_SYSTEM_SECURE && value != null) {
            CharSequence path = value.coerceToString();
            if (path != null) {
                this.mImagePath = path.toString();
            }
        }
    }

    public static void setDefaultDensity(int density) {
        sDefaultDensity = density;
    }

    static int getDefaultDensity() {
        if (sDefaultDensity >= 0) {
            return sDefaultDensity;
        }
        sDefaultDensity = DisplayMetrics.DENSITY_DEVICE;
        return sDefaultDensity;
    }

    Bitmap(long nativeBitmap, byte[] buffer, int width, int height, int density, boolean isMutable, boolean requestPremultiplied, byte[] ninePatchChunk, InsetStruct ninePatchInsets) {
        if (nativeBitmap == 0) {
            throw new RuntimeException("internal error: native bitmap is 0");
        }
        this.mWidth = width;
        this.mHeight = height;
        this.mIsMutable = isMutable;
        this.mRequestPremultiplied = requestPremultiplied;
        this.mBuffer = buffer;
        this.mNinePatchChunk = ninePatchChunk;
        this.mNinePatchInsets = ninePatchInsets;
        if (density >= 0) {
            this.mDensity = density;
        }
        this.mNativePtr = nativeBitmap;
        this.mFinalizer = new BitmapFinalizer(nativeBitmap);
        this.mFinalizer.setNativeAllocationByteCount(buffer == null ? getByteCount() : 0);
    }

    void reinit(int width, int height, boolean requestPremultiplied) {
        this.mWidth = width;
        this.mHeight = height;
        this.mRequestPremultiplied = requestPremultiplied;
    }

    public int getDensity() {
        if (this.mRecycled) {
            Log.w(TAG, "Called getDensity() on a recycle()'d bitmap! This is undefined behavior!");
        }
        return this.mDensity;
    }

    public void setDensity(int density) {
        this.mDensity = density;
    }

    public void reconfigure(int width, int height, Config config) {
        checkRecycled("Can't call reconfigure() on a recycled bitmap");
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("width and height must be > 0");
        } else if (!isMutable()) {
            throw new IllegalStateException("only mutable bitmaps may be reconfigured");
        } else if (this.mBuffer == null) {
            throw new IllegalStateException("native-backed bitmaps may not be reconfigured");
        } else {
            nativeReconfigure(this.mFinalizer.mNativeBitmap, width, height, config.nativeInt, this.mBuffer.length, this.mRequestPremultiplied);
            this.mWidth = width;
            this.mHeight = height;
        }
    }

    public void setWidth(int width) {
        reconfigure(width, getHeight(), getConfig());
    }

    public void setHeight(int height) {
        reconfigure(getWidth(), height, getConfig());
    }

    public void setConfig(Config config) {
        reconfigure(getWidth(), getHeight(), config);
    }

    public void setNinePatchChunk(byte[] chunk) {
        this.mNinePatchChunk = chunk;
    }

    public void recycle() {
        if (!this.mRecycled && this.mFinalizer.mNativeBitmap != 0) {
            if (nativeRecycle(this.mFinalizer.mNativeBitmap)) {
                this.mBuffer = null;
                this.mNinePatchChunk = null;
            }
            this.mRecycled = true;
        }
    }

    public final boolean isRecycled() {
        return this.mRecycled;
    }

    public int getGenerationId() {
        if (this.mRecycled) {
            Log.w(TAG, "Called getGenerationId() on a recycle()'d bitmap! This is undefined behavior!");
        }
        return nativeGenerationId(this.mFinalizer.mNativeBitmap);
    }

    private void checkRecycled(String errorMessage) {
        if (this.mRecycled) {
            throw new IllegalStateException(errorMessage);
        }
    }

    private void checkGPUCompression(String errorMessage) {
        if (isGLCompressed()) {
            Log.e("Bitmap GFX_COMPR", Log.getStackTraceString(new Throwable()));
        }
        if (getConfig() == null) {
            Log.e("Bitmap GFX_PIO", Log.getStackTraceString(new Throwable()));
        }
    }

    private static void checkXYSign(int x, int y) {
        if (x < 0) {
            throw new IllegalArgumentException("x must be >= 0");
        } else if (y < 0) {
            throw new IllegalArgumentException("y must be >= 0");
        }
    }

    private static void checkWidthHeight(int width, int height) {
        if (width <= 0) {
            throw new IllegalArgumentException("width must be > 0");
        } else if (height <= 0) {
            throw new IllegalArgumentException("height must be > 0");
        }
    }

    public boolean isGLCompressed() {
        return getGLCompressionConfig() != null;
    }

    public GLCompressionConfig getGLCompressionConfig() {
        return GLCompressionConfig.nativeToConfig(nativeConfig(this.mFinalizer.mNativeBitmap));
    }

    public void copyPixelsToBuffer(Buffer dst) {
        int shift;
        checkGPUCompression("Critical: Access to pixel information is not available for compressed textures and Indexed textures");
        int elements = dst.remaining();
        if (dst instanceof ByteBuffer) {
            shift = 0;
        } else if (dst instanceof ShortBuffer) {
            shift = 1;
        } else if (dst instanceof IntBuffer) {
            shift = 2;
        } else {
            throw new RuntimeException("unsupported Buffer subclass");
        }
        long pixelSize = (long) getByteCount();
        if ((((long) elements) << shift) < pixelSize) {
            throw new RuntimeException("Buffer not large enough for pixels");
        }
        nativeCopyPixelsToBuffer(this.mFinalizer.mNativeBitmap, dst);
        dst.position((int) (((long) dst.position()) + (pixelSize >> shift)));
    }

    public void copyPixelsFromBuffer(Buffer src) {
        int shift;
        checkRecycled("copyPixelsFromBuffer called on recycled bitmap");
        checkGPUCompression("Critical: Access to pixel information is not available for GPU compressed textures and Indexed textures");
        int elements = src.remaining();
        if (src instanceof ByteBuffer) {
            shift = 0;
        } else if (src instanceof ShortBuffer) {
            shift = 1;
        } else if (src instanceof IntBuffer) {
            shift = 2;
        } else {
            throw new RuntimeException("unsupported Buffer subclass");
        }
        long bitmapBytes = (long) getByteCount();
        if ((((long) elements) << shift) < bitmapBytes) {
            throw new RuntimeException("Buffer not large enough for pixels");
        }
        nativeCopyPixelsFromBuffer(this.mFinalizer.mNativeBitmap, src);
        src.position((int) (((long) src.position()) + (bitmapBytes >> shift)));
    }

    public Bitmap copy(Config config, boolean isMutable) {
        checkRecycled("Can't copy a recycled bitmap");
        if (config == null) {
            config = Config.ARGB_8888;
        }
        Bitmap b = nativeCopy(this.mFinalizer.mNativeBitmap, config.nativeInt, isMutable);
        if (b != null) {
            b.setPremultiplied(this.mRequestPremultiplied);
            b.mDensity = this.mDensity;
        }
        return b;
    }

    public Bitmap createAshmemBitmap() {
        checkRecycled("Can't copy a recycled bitmap");
        Bitmap b = nativeCopyAshmem(this.mFinalizer.mNativeBitmap);
        if (b != null) {
            b.setPremultiplied(this.mRequestPremultiplied);
            b.mDensity = this.mDensity;
        }
        return b;
    }

    public static Bitmap createScaledBitmap(Bitmap src, int dstWidth, int dstHeight, boolean filter) {
        Matrix m;
        synchronized (Bitmap.class) {
            m = sScaleMatrix;
            sScaleMatrix = null;
        }
        if (m == null) {
            m = new Matrix();
        }
        int width = src.getWidth();
        int height = src.getHeight();
        m.setScale(((float) dstWidth) / ((float) width), ((float) dstHeight) / ((float) height));
        Bitmap b = createBitmap(src, 0, 0, width, height, m, filter);
        synchronized (Bitmap.class) {
            if (sScaleMatrix == null) {
                sScaleMatrix = m;
            }
        }
        return b;
    }

    public static Bitmap createBitmap(Bitmap src) {
        return createBitmap(src, 0, 0, src.getWidth(), src.getHeight());
    }

    public static Bitmap createBitmap(Bitmap source, int x, int y, int width, int height) {
        return createBitmap(source, x, y, width, height, null, false);
    }

    public static Bitmap createBitmap(Bitmap source, int x, int y, int width, int height, Matrix m, boolean filter) {
        checkXYSign(x, y);
        checkWidthHeight(width, height);
        if (x + width > source.getWidth()) {
            throw new IllegalArgumentException("x + width must be <= bitmap.width()");
        } else if (y + height > source.getHeight()) {
            throw new IllegalArgumentException("y + height must be <= bitmap.height()");
        } else if (!source.isMutable() && x == 0 && y == 0 && width == source.getWidth() && height == source.getHeight() && (m == null || m.isIdentity())) {
            return source;
        } else {
            Bitmap bitmap;
            Paint paint;
            int neww = width;
            int newh = height;
            Canvas canvas = new Canvas();
            Rect srcR = new Rect(x, y, x + width, y + height);
            RectF dstR = new RectF(0.0f, 0.0f, (float) width, (float) height);
            Config newConfig = Config.ARGB_8888;
            Config config = source.getConfig();
            if (config != null) {
                switch (AnonymousClass2.$SwitchMap$android$graphics$Bitmap$Config[config.ordinal()]) {
                    case 1:
                        newConfig = Config.RGB_565;
                        break;
                    case 2:
                        newConfig = Config.ALPHA_8;
                        break;
                    default:
                        newConfig = Config.ARGB_8888;
                        break;
                }
            }
            if (m == null || m.isIdentity()) {
                bitmap = createBitmap(neww, newh, newConfig, source.hasAlpha());
                paint = null;
            } else {
                boolean transformed = !m.rectStaysRect();
                RectF deviceR = new RectF();
                m.mapRect(deviceR, dstR);
                neww = Math.round(deviceR.width());
                newh = Math.round(deviceR.height());
                if (transformed) {
                    newConfig = Config.ARGB_8888;
                }
                boolean z = transformed || source.hasAlpha();
                bitmap = createBitmap(neww, newh, newConfig, z);
                canvas.translate(-deviceR.left, -deviceR.top);
                canvas.concat(m);
                paint = new Paint();
                paint.setFilterBitmap(filter);
                if (transformed) {
                    paint.setAntiAlias(true);
                }
            }
            bitmap.mDensity = source.mDensity;
            bitmap.setHasAlpha(source.hasAlpha());
            bitmap.setPremultiplied(source.mRequestPremultiplied);
            canvas.setBitmap(bitmap);
            canvas.drawBitmap(source, srcR, dstR, paint);
            canvas.setBitmap(null);
            return bitmap;
        }
    }

    public static Bitmap createBitmap(int width, int height, Config config) {
        return createBitmap(width, height, config, true);
    }

    public static Bitmap createBitmap(DisplayMetrics display, int width, int height, Config config) {
        return createBitmap(display, width, height, config, true);
    }

    private static Bitmap createBitmap(int width, int height, Config config, boolean hasAlpha) {
        return createBitmap(null, width, height, config, hasAlpha);
    }

    private static Bitmap createBitmap(DisplayMetrics display, int width, int height, Config config, boolean hasAlpha) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("width and height must be > 0");
        }
        if (config == null) {
            config = Config.ARGB_8888;
        }
        Bitmap bm = nativeCreate(null, 0, width, width, height, config.nativeInt, true);
        if (display != null) {
            bm.mDensity = display.densityDpi;
        }
        bm.setHasAlpha(hasAlpha);
        if (config == Config.ARGB_8888 && !hasAlpha) {
            nativeErase(bm.mFinalizer.mNativeBitmap, -16777216);
        }
        return bm;
    }

    public static Bitmap createBitmap(int[] colors, int offset, int stride, int width, int height, Config config) {
        return createBitmap(null, colors, offset, stride, width, height, config);
    }

    public static Bitmap createBitmap(DisplayMetrics display, int[] colors, int offset, int stride, int width, int height, Config config) {
        checkWidthHeight(width, height);
        if (Math.abs(stride) < width) {
            throw new IllegalArgumentException("abs(stride) must be >= width");
        }
        int lastScanline = offset + ((height - 1) * stride);
        int length = colors.length;
        if (offset < 0 || offset + width > length || lastScanline < 0 || lastScanline + width > length) {
            throw new ArrayIndexOutOfBoundsException();
        } else if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("width and height must be > 0");
        } else {
            Bitmap bm = nativeCreate(colors, offset, stride, width, height, config.nativeInt, false);
            if (display != null) {
                bm.mDensity = display.densityDpi;
            }
            return bm;
        }
    }

    public static Bitmap createBitmap(int[] colors, int width, int height, Config config) {
        return createBitmap(null, colors, 0, width, width, height, config);
    }

    public static Bitmap createBitmap(DisplayMetrics display, int[] colors, int width, int height, Config config) {
        return createBitmap(display, colors, 0, width, width, height, config);
    }

    public byte[] getNinePatchChunk() {
        return this.mNinePatchChunk;
    }

    public void getOpticalInsets(Rect outInsets) {
        if (this.mNinePatchInsets == null) {
            outInsets.setEmpty();
        } else {
            outInsets.set(this.mNinePatchInsets.opticalRect);
        }
    }

    public InsetStruct getNinePatchInsets() {
        return this.mNinePatchInsets;
    }

    public boolean compress(CompressFormat format, int quality, OutputStream stream) {
        checkRecycled("Can't compress a recycled bitmap");
        if (stream == null) {
            throw new NullPointerException();
        } else if (quality < 0 || quality > 100) {
            throw new IllegalArgumentException("quality must be 0..100");
        } else {
            Trace.traceBegin(8192, "Bitmap.compress");
            boolean result = nativeCompress(this.mFinalizer.mNativeBitmap, format.nativeInt, quality, stream, new byte[4096]);
            Trace.traceEnd(8192);
            return result;
        }
    }

    public boolean compress(ExtendedCompressFormat format, int quality, OutputStream stream) {
        checkRecycled("Can't compress a recycled bitmap");
        if (stream == null) {
            throw new NullPointerException();
        } else if (quality < 0 || quality > 100) {
            throw new IllegalArgumentException("quality must be 0..100");
        } else {
            Trace.traceBegin(8192, "Bitmap.ExtendedCompress");
            boolean result = false;
            if (format == ExtendedCompressFormat.ASTC && getNinePatchChunk() == null) {
                result = nativeCompress(this.mFinalizer.mNativeBitmap, format.nativeInt, quality, stream, new byte[4096]);
            }
            Trace.traceEnd(8192);
            return result;
        }
    }

    public final boolean isMutable() {
        return this.mIsMutable;
    }

    public final boolean isPremultiplied() {
        if (this.mRecycled) {
            Log.w(TAG, "Called isPremultiplied() on a recycle()'d bitmap! This is undefined behavior!");
        }
        return nativeIsPremultiplied(this.mFinalizer.mNativeBitmap);
    }

    public final void setPremultiplied(boolean premultiplied) {
        checkRecycled("setPremultiplied called on a recycled bitmap");
        this.mRequestPremultiplied = premultiplied;
        nativeSetPremultiplied(this.mFinalizer.mNativeBitmap, premultiplied);
    }

    public final int getWidth() {
        if (this.mRecycled) {
            Log.w(TAG, "Called getWidth() on a recycle()'d bitmap! This is undefined behavior!");
        }
        return this.mWidth;
    }

    public final int getHeight() {
        if (this.mRecycled) {
            Log.w(TAG, "Called getHeight() on a recycle()'d bitmap! This is undefined behavior!");
        }
        return this.mHeight;
    }

    public int getScaledWidth(Canvas canvas) {
        return scaleFromDensity(getWidth(), this.mDensity, canvas.mDensity);
    }

    public int getScaledHeight(Canvas canvas) {
        return scaleFromDensity(getHeight(), this.mDensity, canvas.mDensity);
    }

    public int getScaledWidth(DisplayMetrics metrics) {
        return scaleFromDensity(getWidth(), this.mDensity, metrics.densityDpi);
    }

    public int getScaledHeight(DisplayMetrics metrics) {
        return scaleFromDensity(getHeight(), this.mDensity, metrics.densityDpi);
    }

    public int getScaledWidth(int targetDensity) {
        return scaleFromDensity(getWidth(), this.mDensity, targetDensity);
    }

    public int getScaledHeight(int targetDensity) {
        return scaleFromDensity(getHeight(), this.mDensity, targetDensity);
    }

    public static int scaleFromDensity(int size, int sdensity, int tdensity) {
        return (sdensity == 0 || tdensity == 0 || sdensity == tdensity) ? size : ((size * tdensity) + (sdensity >> 1)) / sdensity;
    }

    public final int getRowBytes() {
        if (this.mRecycled) {
            Log.w(TAG, "Called getRowBytes() on a recycle()'d bitmap! This is undefined behavior!");
        }
        return nativeRowBytes(this.mFinalizer.mNativeBitmap);
    }

    public final int getByteCount() {
        return getRowBytes() * getHeight();
    }

    public final int getAllocationByteCount() {
        if (this.mBuffer == null) {
            return getByteCount();
        }
        return this.mBuffer.length;
    }

    public final Config getConfig() {
        if (this.mRecycled) {
            Log.w(TAG, "Called getConfig() on a recycle()'d bitmap! This is undefined behavior!");
        }
        return Config.nativeToConfig(nativeConfig(this.mFinalizer.mNativeBitmap));
    }

    public final boolean hasAlpha() {
        if (this.mRecycled) {
            Log.w(TAG, "Called hasAlpha() on a recycle()'d bitmap! This is undefined behavior!");
        }
        return nativeHasAlpha(this.mFinalizer.mNativeBitmap);
    }

    public void setHasAlpha(boolean hasAlpha) {
        checkRecycled("setHasAlpha called on a recycled bitmap");
        nativeSetHasAlpha(this.mFinalizer.mNativeBitmap, hasAlpha, this.mRequestPremultiplied);
    }

    public final boolean hasMipMap() {
        if (this.mRecycled) {
            Log.w(TAG, "Called hasMipMap() on a recycle()'d bitmap! This is undefined behavior!");
        }
        return nativeHasMipMap(this.mFinalizer.mNativeBitmap);
    }

    public final void setHasMipMap(boolean hasMipMap) {
        checkRecycled("setHasMipMap called on a recycled bitmap");
        nativeSetHasMipMap(this.mFinalizer.mNativeBitmap, hasMipMap);
    }

    public void eraseColor(int c) {
        checkRecycled("Can't erase a recycled bitmap");
        if (isMutable()) {
            nativeErase(this.mFinalizer.mNativeBitmap, c);
            return;
        }
        throw new IllegalStateException("cannot erase immutable bitmaps");
    }

    public int getPixel(int x, int y) {
        checkRecycled("Can't call getPixel() on a recycled bitmap");
        checkPixelAccess(x, y);
        return nativeGetPixel(this.mFinalizer.mNativeBitmap, x, y);
    }

    public void getPixels(int[] pixels, int offset, int stride, int x, int y, int width, int height) {
        checkRecycled("Can't call getPixels() on a recycled bitmap");
        if (width != 0 && height != 0) {
            checkPixelsAccess(x, y, width, height, offset, stride, pixels);
            nativeGetPixels(this.mFinalizer.mNativeBitmap, pixels, offset, stride, x, y, width, height);
        }
    }

    private void checkPixelAccess(int x, int y) {
        checkXYSign(x, y);
        if (x >= getWidth()) {
            throw new IllegalArgumentException("x must be < bitmap.width()");
        } else if (y >= getHeight()) {
            throw new IllegalArgumentException("y must be < bitmap.height()");
        }
    }

    private void checkPixelsAccess(int x, int y, int width, int height, int offset, int stride, int[] pixels) {
        checkXYSign(x, y);
        if (width < 0) {
            throw new IllegalArgumentException("width must be >= 0");
        } else if (height < 0) {
            throw new IllegalArgumentException("height must be >= 0");
        } else if (x + width > getWidth()) {
            throw new IllegalArgumentException("x + width must be <= bitmap.width()");
        } else if (y + height > getHeight()) {
            throw new IllegalArgumentException("y + height must be <= bitmap.height()");
        } else if (Math.abs(stride) < width) {
            throw new IllegalArgumentException("abs(stride) must be >= width");
        } else {
            int lastScanline = offset + ((height - 1) * stride);
            int length = pixels.length;
            if (offset < 0 || offset + width > length || lastScanline < 0 || lastScanline + width > length) {
                throw new ArrayIndexOutOfBoundsException();
            }
        }
    }

    public void setPixel(int x, int y, int color) {
        checkRecycled("Can't call setPixel() on a recycled bitmap");
        if (isMutable()) {
            checkPixelAccess(x, y);
            nativeSetPixel(this.mFinalizer.mNativeBitmap, x, y, color);
            return;
        }
        throw new IllegalStateException();
    }

    public void setPixels(int[] pixels, int offset, int stride, int x, int y, int width, int height) {
        checkRecycled("Can't call setPixels() on a recycled bitmap");
        if (!isMutable()) {
            throw new IllegalStateException();
        } else if (width != 0 && height != 0) {
            checkPixelsAccess(x, y, width, height, offset, stride, pixels);
            nativeSetPixels(this.mFinalizer.mNativeBitmap, pixels, offset, stride, x, y, width, height);
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel p, int flags) {
        checkRecycled("Can't parcel a recycled bitmap");
        if (!nativeWriteToParcel(this.mFinalizer.mNativeBitmap, this.mIsMutable, this.mDensity, p)) {
            throw new RuntimeException("native writeToParcel failed");
        }
    }

    public Bitmap extractAlpha() {
        return extractAlpha(null, null);
    }

    public Bitmap extractAlpha(Paint paint, int[] offsetXY) {
        checkRecycled("Can't extractAlpha on a recycled bitmap");
        Bitmap bm = nativeExtractAlpha(this.mFinalizer.mNativeBitmap, paint != null ? paint.getNativeInstance() : 0, offsetXY);
        if (bm == null) {
            throw new RuntimeException("Failed to extractAlpha on Bitmap");
        }
        bm.mDensity = this.mDensity;
        return bm;
    }

    public boolean sameAs(Bitmap other) {
        checkRecycled("Can't call sameAs on a recycled bitmap!");
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (!other.isRecycled()) {
            return nativeSameAs(this.mFinalizer.mNativeBitmap, other.mFinalizer.mNativeBitmap);
        }
        throw new IllegalArgumentException("Can't compare to a recycled bitmap!");
    }

    public void prepareToDraw() {
        checkRecycled("Can't prepareToDraw on a recycled bitmap!");
    }

    public final long refSkPixelRef() {
        checkRecycled("Can't refSkPixelRef on a recycled bitmap!");
        return nativeRefPixelRef(this.mNativePtr);
    }
}
