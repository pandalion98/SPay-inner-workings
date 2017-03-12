package android.media;

import android.app.backup.FullBackup;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.MediaFile.MediaFileType;
import android.net.ProxyInfo;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;

public class ThumbnailUtils {
    private static final int MAX_NUM_PIXELS_MICRO_THUMBNAIL = 19200;
    private static final int MAX_NUM_PIXELS_THUMBNAIL = 196608;
    private static final int OPTIONS_NONE = 0;
    public static final int OPTIONS_RECYCLE_INPUT = 2;
    private static final int OPTIONS_SCALE_UP = 1;
    private static final String TAG = "ThumbnailUtils";
    public static final int TARGET_SIZE_MICRO_THUMBNAIL = 96;
    public static final int TARGET_SIZE_MINI_THUMBNAIL = 320;
    private static final int UNCONSTRAINED = -1;

    private static class SizedThumbnailBitmap {
        public Bitmap mBitmap;
        public byte[] mThumbnailData;
        public int mThumbnailHeight;
        public int mThumbnailWidth;

        private SizedThumbnailBitmap() {
        }
    }

    public static Bitmap createImageThumbnail(String filePath, int kind) {
        IOException ex;
        Throwable th;
        OutOfMemoryError oom;
        boolean wantMini = kind == 1;
        int targetSize = wantMini ? 320 : 96;
        int maxPixels = wantMini ? 196608 : MAX_NUM_PIXELS_MICRO_THUMBNAIL;
        SizedThumbnailBitmap sizedThumbnailBitmap = new SizedThumbnailBitmap();
        Bitmap bitmap = null;
        MediaFileType fileType = MediaFile.getFileType(filePath);
        if (fileType != null && fileType.fileType == 31) {
            createThumbnailFromEXIF(filePath, targetSize, maxPixels, sizedThumbnailBitmap);
            bitmap = sizedThumbnailBitmap.mBitmap;
        }
        if (bitmap == null) {
            FileInputStream stream = null;
            try {
                FileInputStream stream2 = new FileInputStream(filePath);
                try {
                    FileDescriptor fd = stream2.getFD();
                    Options options = new Options();
                    options.inSampleSize = 1;
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFileDescriptor(fd, null, options);
                    if (!options.mCancel && options.outWidth != -1 && options.outHeight != -1) {
                        options.inSampleSize = computeSampleSize(options, targetSize, maxPixels);
                        options.inJustDecodeBounds = false;
                        options.inDither = false;
                        options.inPreferredConfig = Config.ARGB_8888;
                        bitmap = BitmapFactory.decodeFileDescriptor(fd, null, options);
                        if (stream2 != null) {
                            try {
                                stream2.close();
                            } catch (IOException ex2) {
                                Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, ex2);
                            }
                        }
                    } else if (stream2 == null) {
                        return null;
                    } else {
                        try {
                            stream2.close();
                            return null;
                        } catch (IOException ex22) {
                            Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, ex22);
                            return null;
                        }
                    }
                } catch (IOException e) {
                    ex22 = e;
                    stream = stream2;
                    try {
                        Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, ex22);
                        if (stream != null) {
                            try {
                                stream.close();
                            } catch (IOException ex222) {
                                Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, ex222);
                            }
                        }
                        if (kind == 3) {
                            bitmap = extractThumbnail(bitmap, 96, 96, 2);
                        }
                        return bitmap;
                    } catch (Throwable th2) {
                        th = th2;
                        if (stream != null) {
                            try {
                                stream.close();
                            } catch (IOException ex2222) {
                                Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, ex2222);
                            }
                        }
                        throw th;
                    }
                } catch (OutOfMemoryError e2) {
                    oom = e2;
                    stream = stream2;
                    Log.e(TAG, "Unable to decode file " + filePath + ". OutOfMemoryError.", oom);
                    if (stream != null) {
                        try {
                            stream.close();
                        } catch (IOException ex22222) {
                            Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, ex22222);
                        }
                    }
                    if (kind == 3) {
                        bitmap = extractThumbnail(bitmap, 96, 96, 2);
                    }
                    return bitmap;
                } catch (Throwable th3) {
                    th = th3;
                    stream = stream2;
                    if (stream != null) {
                        stream.close();
                    }
                    throw th;
                }
            } catch (IOException e3) {
                ex22222 = e3;
                Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, ex22222);
                if (stream != null) {
                    stream.close();
                }
                if (kind == 3) {
                    bitmap = extractThumbnail(bitmap, 96, 96, 2);
                }
                return bitmap;
            } catch (OutOfMemoryError e4) {
                oom = e4;
                Log.e(TAG, "Unable to decode file " + filePath + ". OutOfMemoryError.", oom);
                if (stream != null) {
                    stream.close();
                }
                if (kind == 3) {
                    bitmap = extractThumbnail(bitmap, 96, 96, 2);
                }
                return bitmap;
            }
        }
        if (kind == 3) {
            bitmap = extractThumbnail(bitmap, 96, 96, 2);
        }
        return bitmap;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static android.graphics.Bitmap createVideoThumbnail(java.lang.String r14, int r15) {
        /*
        r13 = 96;
        r12 = 1;
        r0 = 0;
        r5 = new android.media.MediaMetadataRetriever;
        r5.<init>();
        r5.setDataSource(r14);	 Catch:{ OutOfMemoryError -> 0x001a, IllegalArgumentException -> 0x0028, RuntimeException -> 0x002f }
        r10 = 15000000; // 0xe4e1c0 float:2.1019477E-38 double:7.4109847E-317;
        r0 = r5.getFrameAtTime(r10);	 Catch:{ OutOfMemoryError -> 0x001a, IllegalArgumentException -> 0x0028, RuntimeException -> 0x002f }
        r5.release();	 Catch:{ RuntimeException -> 0x006d }
    L_0x0016:
        if (r0 != 0) goto L_0x003b;
    L_0x0018:
        r9 = 0;
    L_0x0019:
        return r9;
    L_0x001a:
        r1 = move-exception;
        r9 = "ThumbnailUtils";
        r10 = "Got OOM error";
        android.util.Log.e(r9, r10, r1);	 Catch:{ all -> 0x0036 }
        r5.release();	 Catch:{ RuntimeException -> 0x0026 }
        goto L_0x0016;
    L_0x0026:
        r9 = move-exception;
        goto L_0x0016;
    L_0x0028:
        r9 = move-exception;
        r5.release();	 Catch:{ RuntimeException -> 0x002d }
        goto L_0x0016;
    L_0x002d:
        r9 = move-exception;
        goto L_0x0016;
    L_0x002f:
        r9 = move-exception;
        r5.release();	 Catch:{ RuntimeException -> 0x0034 }
        goto L_0x0016;
    L_0x0034:
        r9 = move-exception;
        goto L_0x0016;
    L_0x0036:
        r9 = move-exception;
        r5.release();	 Catch:{ RuntimeException -> 0x006f }
    L_0x003a:
        throw r9;
    L_0x003b:
        if (r15 != r12) goto L_0x0064;
    L_0x003d:
        r8 = r0.getWidth();
        r3 = r0.getHeight();
        r4 = java.lang.Math.max(r8, r3);
        r9 = 512; // 0x200 float:7.175E-43 double:2.53E-321;
        if (r4 <= r9) goto L_0x0062;
    L_0x004d:
        r9 = 1140850688; // 0x44000000 float:512.0 double:5.63655132E-315;
        r10 = (float) r4;
        r6 = r9 / r10;
        r9 = (float) r8;
        r9 = r9 * r6;
        r7 = java.lang.Math.round(r9);
        r9 = (float) r3;
        r9 = r9 * r6;
        r2 = java.lang.Math.round(r9);
        r0 = android.graphics.Bitmap.createScaledBitmap(r0, r7, r2, r12);
    L_0x0062:
        r9 = r0;
        goto L_0x0019;
    L_0x0064:
        r9 = 3;
        if (r15 != r9) goto L_0x0062;
    L_0x0067:
        r9 = 2;
        r0 = extractThumbnail(r0, r13, r13, r9);
        goto L_0x0062;
    L_0x006d:
        r9 = move-exception;
        goto L_0x0016;
    L_0x006f:
        r10 = move-exception;
        goto L_0x003a;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.ThumbnailUtils.createVideoThumbnail(java.lang.String, int):android.graphics.Bitmap");
    }

    public static Bitmap extractThumbnail(Bitmap source, int width, int height) {
        return extractThumbnail(source, width, height, 0);
    }

    public static Bitmap extractThumbnail(Bitmap source, int width, int height, int options) {
        if (source == null) {
            return null;
        }
        float scale;
        if (source.getWidth() < source.getHeight()) {
            scale = ((float) width) / ((float) source.getWidth());
        } else {
            scale = ((float) height) / ((float) source.getHeight());
        }
        Matrix matrix = new Matrix();
        matrix.setScale(scale, scale);
        return transform(matrix, source, width, height, options | 1);
    }

    private static int computeSampleSize(Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
        if (initialSize > 8) {
            return ((initialSize + 7) / 8) * 8;
        }
        int roundedSize = 1;
        while (roundedSize < initialSize) {
            roundedSize <<= 1;
        }
        return roundedSize;
    }

    private static int computeInitialSampleSize(Options options, int minSideLength, int maxNumOfPixels) {
        double w = (double) options.outWidth;
        double h = (double) options.outHeight;
        int lowerBound = maxNumOfPixels == -1 ? 1 : (int) Math.ceil(Math.sqrt((w * h) / ((double) maxNumOfPixels)));
        int upperBound = minSideLength == -1 ? 128 : (int) Math.min(Math.floor(w / ((double) minSideLength)), Math.floor(h / ((double) minSideLength)));
        if (upperBound < lowerBound) {
            return lowerBound;
        }
        if (maxNumOfPixels == -1 && minSideLength == -1) {
            return 1;
        }
        if (minSideLength != -1) {
            return upperBound;
        }
        return lowerBound;
    }

    private static Bitmap makeBitmap(int minSideLength, int maxNumOfPixels, Uri uri, ContentResolver cr, ParcelFileDescriptor pfd, Options options) {
        if (pfd == null) {
            try {
                pfd = makeInputStream(uri, cr);
            } catch (OutOfMemoryError ex) {
                Log.e(TAG, "Got oom exception ", ex);
                return null;
            } finally {
                closeSilently(pfd);
            }
        }
        if (pfd == null) {
            closeSilently(pfd);
            return null;
        }
        if (options == null) {
            options = new Options();
        }
        FileDescriptor fd = pfd.getFileDescriptor();
        options.inSampleSize = 1;
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fd, null, options);
        if (options.mCancel || options.outWidth == -1 || options.outHeight == -1) {
            closeSilently(pfd);
            return null;
        }
        options.inSampleSize = computeSampleSize(options, minSideLength, maxNumOfPixels);
        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inPreferredConfig = Config.ARGB_8888;
        Bitmap b = BitmapFactory.decodeFileDescriptor(fd, null, options);
        closeSilently(pfd);
        return b;
    }

    private static void closeSilently(ParcelFileDescriptor c) {
        if (c != null) {
            try {
                c.close();
            } catch (Throwable th) {
            }
        }
    }

    private static ParcelFileDescriptor makeInputStream(Uri uri, ContentResolver cr) {
        try {
            return cr.openFileDescriptor(uri, FullBackup.ROOT_TREE_TOKEN);
        } catch (IOException e) {
            return null;
        }
    }

    private static Bitmap transform(Matrix scaler, Bitmap source, int targetWidth, int targetHeight, int options) {
        Bitmap b2;
        boolean scaleUp = (options & 1) != 0;
        boolean recycle = (options & 2) != 0;
        int deltaX = source.getWidth() - targetWidth;
        int deltaY = source.getHeight() - targetHeight;
        if (scaleUp || (deltaX >= 0 && deltaY >= 0)) {
            Bitmap b1;
            float bitmapWidthF = (float) source.getWidth();
            float bitmapHeightF = (float) source.getHeight();
            float scale;
            if (bitmapWidthF / bitmapHeightF > ((float) targetWidth) / ((float) targetHeight)) {
                scale = ((float) targetHeight) / bitmapHeightF;
                if (scale < 0.9f || scale > 1.0f) {
                    scaler.setScale(scale, scale);
                } else {
                    scaler = null;
                }
            } else {
                scale = ((float) targetWidth) / bitmapWidthF;
                if (scale < 0.9f || scale > 1.0f) {
                    scaler.setScale(scale, scale);
                } else {
                    scaler = null;
                }
            }
            if (scaler != null) {
                b1 = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), scaler, true);
            } else {
                b1 = source;
            }
            if (recycle && b1 != source) {
                source.recycle();
            }
            b2 = Bitmap.createBitmap(b1, Math.max(0, b1.getWidth() - targetWidth) / 2, Math.max(0, b1.getHeight() - targetHeight) / 2, targetWidth, targetHeight);
            if (b2 != b1 && (recycle || b1 != source)) {
                b1.recycle();
            }
        } else {
            b2 = Bitmap.createBitmap(targetWidth, targetHeight, Config.ARGB_8888);
            Canvas c = new Canvas(b2);
            int deltaXHalf = Math.max(0, deltaX / 2);
            int deltaYHalf = Math.max(0, deltaY / 2);
            Rect rect = new Rect(deltaXHalf, deltaYHalf, Math.min(targetWidth, source.getWidth()) + deltaXHalf, Math.min(targetHeight, source.getHeight()) + deltaYHalf);
            int dstX = (targetWidth - rect.width()) / 2;
            int dstY = (targetHeight - rect.height()) / 2;
            c.drawBitmap(source, rect, new Rect(dstX, dstY, targetWidth - dstX, targetHeight - dstY), null);
            if (recycle) {
                source.recycle();
            }
            c.setBitmap(null);
        }
        return b2;
    }

    private static void createThumbnailFromEXIF(String filePath, int targetSize, int maxPixels, SizedThumbnailBitmap sizedThumbBitmap) {
        IOException ex;
        ExifInterface exifInterface;
        Options fullOptions;
        Options exifOptions;
        int exifThumbWidth;
        int fullThumbWidth;
        if (filePath != null) {
            byte[] thumbData = null;
            try {
                ExifInterface exif = new ExifInterface(filePath);
                if (exif != null) {
                    try {
                        thumbData = exif.getThumbnail();
                    } catch (IOException e) {
                        ex = e;
                        exifInterface = exif;
                        Log.w(TAG, ex);
                        fullOptions = new Options();
                        exifOptions = new Options();
                        exifThumbWidth = 0;
                        if (thumbData != null) {
                            exifOptions.inJustDecodeBounds = true;
                            BitmapFactory.decodeByteArray(thumbData, 0, thumbData.length, exifOptions);
                            exifOptions.inSampleSize = computeSampleSize(exifOptions, targetSize, maxPixels);
                            exifThumbWidth = exifOptions.outWidth / exifOptions.inSampleSize;
                        }
                        fullOptions.inJustDecodeBounds = true;
                        BitmapFactory.decodeFile(filePath, fullOptions);
                        fullOptions.inSampleSize = computeSampleSize(fullOptions, targetSize, maxPixels);
                        fullThumbWidth = fullOptions.outWidth / fullOptions.inSampleSize;
                        if (thumbData != null) {
                        }
                        fullOptions.inJustDecodeBounds = false;
                        sizedThumbBitmap.mBitmap = BitmapFactory.decodeFile(filePath, fullOptions);
                        return;
                    }
                }
                exifInterface = exif;
            } catch (IOException e2) {
                ex = e2;
                Log.w(TAG, ex);
                fullOptions = new Options();
                exifOptions = new Options();
                exifThumbWidth = 0;
                if (thumbData != null) {
                    exifOptions.inJustDecodeBounds = true;
                    BitmapFactory.decodeByteArray(thumbData, 0, thumbData.length, exifOptions);
                    exifOptions.inSampleSize = computeSampleSize(exifOptions, targetSize, maxPixels);
                    exifThumbWidth = exifOptions.outWidth / exifOptions.inSampleSize;
                }
                fullOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(filePath, fullOptions);
                fullOptions.inSampleSize = computeSampleSize(fullOptions, targetSize, maxPixels);
                fullThumbWidth = fullOptions.outWidth / fullOptions.inSampleSize;
                if (thumbData != null) {
                }
                fullOptions.inJustDecodeBounds = false;
                sizedThumbBitmap.mBitmap = BitmapFactory.decodeFile(filePath, fullOptions);
                return;
            }
            fullOptions = new Options();
            exifOptions = new Options();
            exifThumbWidth = 0;
            if (thumbData != null) {
                exifOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeByteArray(thumbData, 0, thumbData.length, exifOptions);
                exifOptions.inSampleSize = computeSampleSize(exifOptions, targetSize, maxPixels);
                exifThumbWidth = exifOptions.outWidth / exifOptions.inSampleSize;
            }
            fullOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filePath, fullOptions);
            fullOptions.inSampleSize = computeSampleSize(fullOptions, targetSize, maxPixels);
            fullThumbWidth = fullOptions.outWidth / fullOptions.inSampleSize;
            if (thumbData != null || exifThumbWidth < fullThumbWidth) {
                fullOptions.inJustDecodeBounds = false;
                sizedThumbBitmap.mBitmap = BitmapFactory.decodeFile(filePath, fullOptions);
                return;
            }
            int width = exifOptions.outWidth;
            int height = exifOptions.outHeight;
            exifOptions.inJustDecodeBounds = false;
            sizedThumbBitmap.mBitmap = BitmapFactory.decodeByteArray(thumbData, 0, thumbData.length, exifOptions);
            if (sizedThumbBitmap.mBitmap != null) {
                sizedThumbBitmap.mThumbnailData = thumbData;
                sizedThumbBitmap.mThumbnailWidth = width;
                sizedThumbBitmap.mThumbnailHeight = height;
            }
        }
    }
}
