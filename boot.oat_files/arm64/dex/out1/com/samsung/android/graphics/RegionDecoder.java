package com.samsung.android.graphics;

import android.graphics.Bitmap;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.util.Log;
import com.samsung.android.graphics.Decoder.Options;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class RegionDecoder {
    private static final String TAG = "RegionDecoder";
    private static final boolean USE_MULTICORE = true;
    private BitmapRegionDecoder mBitmapRegionDecoder;
    private final Object mNativeLock;
    private final Object mNativeLock_decode;
    private RegionDecoder mNativeSisoRegionDecoder;
    private boolean mRecycled;
    private long secmmrd;

    private static native int RequestCancelDecode(long j);

    private static native int configLTN(long j);

    private static native int configMultiCore(long j);

    private static native Bitmap nativeDecodeRegion(long j, int i, int i2, int i3, int i4, int i5, Bitmap bitmap, int i6, int i7);

    private static native ByteBuffer nativeDecodeRegionBB(long j, int i, int i2, int i3, int i4, int i5, ByteBuffer byteBuffer, int i6, int i7);

    private static native int nativeGetHeight(long j);

    private static native int nativeGetWidth(long j);

    private static native long nativerdinstance(String str, byte[] bArr, int i, int i2, boolean z, boolean z2);

    private static native int nativerecycle(long j);

    static {
        try {
            System.loadLibrary("MMCodec");
        } catch (Exception e) {
            Log.e(TAG, "Load library fail : " + e.toString());
        }
    }

    private RegionDecoder(String pathName, byte[] data, int off, int len, boolean isShareabl, boolean isPreview) {
        this.secmmrd = 0;
        this.mRecycled = true;
        this.mNativeLock = new Object();
        this.mNativeLock_decode = new Object();
        this.secmmrd = 0;
        this.secmmrd = nativerdinstance(pathName, data, off, len, isShareabl, isPreview);
        configMultiCore(this.secmmrd);
        this.mRecycled = false;
    }

    public Bitmap decodeRegion(Rect rect, Options options) {
        try {
            if (this.mNativeSisoRegionDecoder == null && this.mBitmapRegionDecoder != null) {
                return this.mBitmapRegionDecoder.decodeRegion(rect, Decoder.getBitmapFactoryOptions(options));
            }
            int tile_width = rect.width();
            int tile_height = rect.height();
            int samplesize = options.inSampleSize;
            if (samplesize == 0) {
                samplesize = 1;
            }
            tile_width = ((tile_width + samplesize) - 1) / samplesize;
            tile_height = ((tile_height + samplesize) - 1) / samplesize;
            if (options.inBitmap == null || (options.inBitmap.getWidth() == tile_width && options.inBitmap.getHeight() == tile_height)) {
                Bitmap nativeDecodeRegion;
                synchronized (this.mNativeLock_decode) {
                    nativeDecodeRegion = nativeDecodeRegion(this.secmmrd, rect.left, rect.right, rect.top, rect.bottom, samplesize, options.inBitmap, tile_width, tile_height);
                }
                return nativeDecodeRegion;
            }
            Log.v(TAG, "inBitmap Erraneous\n");
            return options.inBitmap;
        } catch (Exception e) {
            Log.w(TAG, e.toString());
            return options.inBitmap;
        }
    }

    public int getWidth() {
        int nativeGetWidth;
        synchronized (this.mNativeLock) {
            checkRecycled("getWidth called on recycled region decoder");
            if (this.mNativeSisoRegionDecoder != null || this.mBitmapRegionDecoder == null) {
                nativeGetWidth = nativeGetWidth(this.secmmrd);
            } else {
                nativeGetWidth = this.mBitmapRegionDecoder.getWidth();
            }
        }
        return nativeGetWidth;
    }

    public int getHeight() {
        int nativeGetHeight;
        synchronized (this.mNativeLock) {
            checkRecycled("getHeight called on recycled region decoder");
            if (this.mNativeSisoRegionDecoder != null || this.mBitmapRegionDecoder == null) {
                nativeGetHeight = nativeGetHeight(this.secmmrd);
            } else {
                nativeGetHeight = this.mBitmapRegionDecoder.getHeight();
            }
        }
        return nativeGetHeight;
    }

    public static RegionDecoder newInstance(String pathName, boolean isShareable) {
        try {
            RegionDecoder decoder = new RegionDecoder(pathName, null, 0, 0, isShareable, false);
            if (decoder.secmmrd != 0) {
                return decoder;
            }
            decoder.mNativeSisoRegionDecoder = null;
            try {
                decoder.mBitmapRegionDecoder = BitmapRegionDecoder.newInstance(pathName, isShareable);
            } catch (IOException e) {
                Log.w(TAG, e.toString());
            }
            return decoder.mBitmapRegionDecoder != null ? decoder : null;
        } catch (Exception e2) {
            Log.w(TAG, e2.toString());
            return null;
        }
    }

    public static RegionDecoder newInstance(InputStream is, boolean isShareable) {
        if (is == null) {
            return null;
        }
        try {
            RegionDecoder decoder = new RegionDecoder(null, convertInputStreamToByteArray(is), 0, 0, isShareable, false);
            if (decoder.secmmrd != 0) {
                return decoder;
            }
            decoder.mNativeSisoRegionDecoder = null;
            try {
                decoder.mBitmapRegionDecoder = BitmapRegionDecoder.newInstance(is, isShareable);
            } catch (IOException e) {
                Log.w(TAG, e.toString());
            }
            if (decoder.mBitmapRegionDecoder == null) {
                return null;
            }
            return decoder;
        } catch (Exception e2) {
            Log.w(TAG, e2.toString());
            return null;
        }
    }

    public static RegionDecoder newInstance(byte[] data, int offset, int length, boolean isShareable) {
        if ((offset | length) < 0 || data.length < offset + length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        try {
            RegionDecoder decoder = new RegionDecoder(null, data, offset, length, isShareable, false);
            if (decoder.secmmrd != 0) {
                return decoder;
            }
            decoder.mNativeSisoRegionDecoder = null;
            try {
                decoder.mBitmapRegionDecoder = BitmapRegionDecoder.newInstance(data, offset, length, isShareable);
            } catch (IOException e) {
                Log.w(TAG, e.toString());
            }
            return decoder.mBitmapRegionDecoder != null ? decoder : null;
        } catch (Exception e2) {
            Log.w(TAG, e2.toString());
            return null;
        }
    }

    public final boolean isRecycled() {
        return this.mRecycled;
    }

    private void checkRecycled(String errorMessage) {
        if (this.mRecycled) {
            throw new IllegalStateException(errorMessage);
        }
    }

    public void recycle() {
        synchronized (this.mNativeLock) {
            synchronized (this.mNativeLock_decode) {
                if (!this.mRecycled) {
                    nativerecycle(this.secmmrd);
                    this.secmmrd = 0;
                    this.mRecycled = true;
                }
            }
        }
    }

    public void requestCancelDecode() {
        synchronized (this.mNativeLock) {
            if (!this.mRecycled) {
                RequestCancelDecode(this.secmmrd);
            }
            recycle();
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int useRowDecode() {
        /*
        r6 = this;
        r1 = r6.mNativeLock;
        monitor-enter(r1);
        r0 = r6.mRecycled;	 Catch:{ all -> 0x001a }
        if (r0 != 0) goto L_0x0017;
    L_0x0007:
        r2 = r6.secmmrd;	 Catch:{ all -> 0x001a }
        r4 = 0;
        r0 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
        if (r0 == 0) goto L_0x0017;
    L_0x000f:
        r2 = r6.secmmrd;	 Catch:{ all -> 0x001a }
        r0 = configLTN(r2);	 Catch:{ all -> 0x001a }
        monitor-exit(r1);	 Catch:{ all -> 0x001a }
    L_0x0016:
        return r0;
    L_0x0017:
        monitor-exit(r1);	 Catch:{ all -> 0x001a }
        r0 = 0;
        goto L_0x0016;
    L_0x001a:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x001a }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.graphics.RegionDecoder.useRowDecode():int");
    }

    protected void finalize() throws Throwable {
        try {
            recycle();
        } finally {
            super.finalize();
        }
    }

    private static byte[] convertInputStreamToByteArray(InputStream is) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(is);
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        for (int result = bis.read(); result != -1; result = bis.read()) {
            buf.write((byte) result);
        }
        return buf.toByteArray();
    }
}
