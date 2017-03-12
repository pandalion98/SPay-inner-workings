package android.graphics;

import android.content.res.AssetManager.AssetInputStream;
import android.drm.DrmInfo;
import android.drm.DrmInfoRequest;
import android.drm.DrmManagerClient;
import android.graphics.BitmapFactory.Options;
import android.util.Log;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class BitmapRegionDecoder {
    private long mNativeBitmapRegionDecoder;
    private final Object mNativeLock = new Object();
    private boolean mRecycled;

    private static native void nativeClean(long j);

    private static native Bitmap nativeDecodeRegion(long j, int i, int i2, int i3, int i4, Options options);

    private static native int nativeGetHeight(long j);

    private static native int nativeGetWidth(long j);

    private static native BitmapRegionDecoder nativeNewInstance(long j, boolean z);

    private static native BitmapRegionDecoder nativeNewInstance(FileDescriptor fileDescriptor, boolean z);

    private static native BitmapRegionDecoder nativeNewInstance(InputStream inputStream, byte[] bArr, boolean z);

    private static native BitmapRegionDecoder nativeNewInstance(byte[] bArr, int i, int i2, boolean z);

    public static BitmapRegionDecoder newInstance(byte[] data, int offset, int length, boolean isShareable) throws IOException {
        if ((offset | length) >= 0 && data.length >= offset + length) {
            return nativeNewInstance(data, offset, length, isShareable);
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    public static BitmapRegionDecoder newInstance(FileDescriptor fd, boolean isShareable) throws IOException {
        return nativeNewInstance(fd, isShareable);
    }

    public static BitmapRegionDecoder newInstance(InputStream is, boolean isShareable) throws IOException {
        if (is instanceof AssetInputStream) {
            return nativeNewInstance(((AssetInputStream) is).getNativeAsset(), isShareable);
        }
        return nativeNewInstance(is, new byte[16384], isShareable);
    }

    public static BitmapRegionDecoder newInstance(String pathName, boolean isShareable) throws IOException {
        Throwable th;
        BitmapRegionDecoder decoder = null;
        InputStream stream = null;
        if (pathName.endsWith(".dcf")) {
            DrmManagerClient drmClient = new DrmManagerClient(null);
            int rightStatus = drmClient.checkRightsStatus(pathName, 7);
            if (rightStatus == 0) {
                long fileLength = new File(pathName).length();
                DrmInfoRequest drmInfoRequest = new DrmInfoRequest(10, "application/vnd.oma.drm.content");
                drmInfoRequest.put(DrmInfoRequest.DRM_PATH, pathName);
                drmInfoRequest.put("LENGTH", Long.valueOf(fileLength).toString());
                DrmInfo resultInfo = drmClient.acquireDrmInfo(drmInfoRequest);
                String status = resultInfo.get("status").toString();
                Log.i("BitmapRegionDecoder", "newInstance acquireDrmInfo status is " + status);
                if (!status.equals(DrmInfoRequest.SUCCESS)) {
                    Log.e("BitmapRegionDecoder", "newInstance FAIL status = " + resultInfo.get("INFO"));
                } else if (resultInfo.getData() != null) {
                    decoder = newInstance(new ByteArrayInputStream(resultInfo.getData()), isShareable);
                } else {
                    Log.e("BitmapRegionDecoder", "newInstance acquireDrmInfo resultInfo is null");
                }
            } else {
                Log.i("BitmapRegionDecoder", "newInstance Rights not present. rightStatus = " + rightStatus);
            }
            if (drmClient != null) {
                drmClient.release();
            }
        } else {
            try {
                InputStream stream2 = new FileInputStream(pathName);
                try {
                    decoder = newInstance(stream2, isShareable);
                    if (stream2 != null) {
                        try {
                            stream2.close();
                            stream = stream2;
                        } catch (IOException e) {
                            stream = stream2;
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                    stream = stream2;
                    if (stream != null) {
                        try {
                            stream.close();
                        } catch (IOException e2) {
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                if (stream != null) {
                    stream.close();
                }
                throw th;
            }
        }
        return decoder;
    }

    public static BitmapRegionDecoder newInstance(String pathName, boolean isShareable, boolean isPreview) throws IOException {
        return newInstance(pathName, isShareable);
    }

    private BitmapRegionDecoder(long decoder) {
        this.mNativeBitmapRegionDecoder = decoder;
        this.mRecycled = false;
    }

    public Bitmap decodeRegion(Rect rect, Options options) {
        Bitmap nativeDecodeRegion;
        synchronized (this.mNativeLock) {
            checkRecycled("decodeRegion called on recycled region decoder");
            if (rect.right <= 0 || rect.bottom <= 0 || rect.left >= getWidth() || rect.top >= getHeight()) {
                throw new IllegalArgumentException("rectangle is outside the image");
            }
            nativeDecodeRegion = nativeDecodeRegion(this.mNativeBitmapRegionDecoder, rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top, options);
        }
        return nativeDecodeRegion;
    }

    public int getWidth() {
        int nativeGetWidth;
        synchronized (this.mNativeLock) {
            checkRecycled("getWidth called on recycled region decoder");
            nativeGetWidth = nativeGetWidth(this.mNativeBitmapRegionDecoder);
        }
        return nativeGetWidth;
    }

    public int getHeight() {
        int nativeGetHeight;
        synchronized (this.mNativeLock) {
            checkRecycled("getHeight called on recycled region decoder");
            nativeGetHeight = nativeGetHeight(this.mNativeBitmapRegionDecoder);
        }
        return nativeGetHeight;
    }

    public void recycle() {
        synchronized (this.mNativeLock) {
            if (!this.mRecycled) {
                nativeClean(this.mNativeBitmapRegionDecoder);
                this.mRecycled = true;
            }
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

    protected void finalize() throws Throwable {
        try {
            recycle();
        } finally {
            super.finalize();
        }
    }
}
