package android.graphics;

import android.app.ActivityThread;
import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager.AssetInputStream;
import android.content.res.Resources;
import android.drm.DrmInfo;
import android.drm.DrmInfoRequest;
import android.drm.DrmManagerClient;
import android.graphics.Bitmap.Config;
import android.net.ProxyInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Trace;
import android.util.Log;
import android.util.TypedValue;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

public class BitmapFactory {
    private static final int DECODE_BUFFER_SIZE = 16384;
    private static Class<?> SprClass = null;
    private static Method SprCreateFromStream = null;
    private static Method SprGetBitmap = null;
    private static final String TAG = "BitmapFactory";
    static boolean checkedPreferIPT = false;

    public static class Options {
        public Bitmap inBitmap;
        public int inDensity;
        public boolean inDither = false;
        @Deprecated
        public boolean inInputShareable;
        public boolean inJustDecodeBounds;
        public boolean inMutable;
        public boolean inPreferQualityOverSpeed;
        public Config inPreferredConfig = Config.ARGB_8888;
        public boolean inPremultiplied = true;
        @Deprecated
        public boolean inPurgeable;
        public int inSampleSize;
        public boolean inScaled = true;
        public int inScreenDensity;
        public int inTargetDensity;
        public byte[] inTempStorage;
        public boolean isPreview = false;
        public boolean mCancel;
        public int outHeight;
        public String outMimeType;
        public int outWidth;

        private native void requestCancel();

        public void requestCancelDecode() {
            this.mCancel = true;
            requestCancel();
        }
    }

    private static native Bitmap nativeDecodeAsset(long j, Rect rect, Options options);

    private static native Bitmap nativeDecodeByteArray(byte[] bArr, int i, int i2, Options options);

    private static native Bitmap nativeDecodeFileDescriptor(FileDescriptor fileDescriptor, Rect rect, Options options);

    private static native Bitmap nativeDecodeStream(InputStream inputStream, byte[] bArr, Rect rect, Options options);

    private static native boolean nativeIsSeekable(FileDescriptor fileDescriptor);

    private static native void nativeSetPreferIPTToRGBA();

    public static Bitmap decodeFile(String pathName, Options opts) {
        Bitmap bm = null;
        InputStream stream = null;
        if (pathName != null) {
            try {
                if (pathName.endsWith(".dcf")) {
                    stream = null;
                    DrmManagerClient drmClient = new DrmManagerClient(null);
                    String drmMimetype = drmClient.getOriginalMimeType(pathName);
                    Log.i(TAG, "decodeFile drmMimetype = " + drmMimetype);
                    if (drmMimetype != null && drmMimetype.startsWith("image/")) {
                        int rightStatus = drmClient.checkRightsStatus(pathName, 7);
                        if (rightStatus == 0) {
                            long fileLength = new File(pathName).length();
                            DrmInfoRequest drmInfoRequest = new DrmInfoRequest(10, "application/vnd.oma.drm.content");
                            drmInfoRequest.put(DrmInfoRequest.DRM_PATH, pathName);
                            drmInfoRequest.put("LENGTH", Long.valueOf(fileLength).toString());
                            DrmInfo resultInfo = drmClient.acquireDrmInfo(drmInfoRequest);
                            String status = resultInfo.get("status").toString();
                            Log.i(TAG, "decodeFile acquireDrmInfo status is " + status);
                            if (!status.equals(DrmInfoRequest.SUCCESS)) {
                                Log.e(TAG, "decodeFile FAIL status = " + resultInfo.get("INFO"));
                            } else if (resultInfo.getData() != null) {
                                stream = new ByteArrayInputStream(resultInfo.getData());
                            } else {
                                Log.e(TAG, "decodeFile acquireDrmInfo resultInfo is null");
                            }
                        } else {
                            Log.i(TAG, "decodeFile Rights not present. rightStatus = " + rightStatus);
                        }
                    }
                    if (drmClient != null) {
                        drmClient.release();
                    }
                } else {
                    stream = new FileInputStream(pathName);
                }
            } catch (Exception e) {
                Log.e(TAG, "Unable to decode stream: " + e);
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException e2) {
                    }
                }
            } catch (Throwable th) {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException e3) {
                    }
                }
            }
        }
        if (stream != null) {
            bm = decodeStream(stream, null, opts);
            stream.close();
        }
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e4) {
            }
        }
        if (bm != null) {
            bm.setImagePath(pathName);
        }
        return bm;
    }

    public static Bitmap decodeFile(String pathName) {
        return decodeFile(pathName, null);
    }

    public static Bitmap decodeResourceStream(Resources res, TypedValue value, InputStream is, Rect pad, Options opts) {
        Exception e;
        String srcName;
        byte[] b;
        Object spr;
        String text = ProxyInfo.LOCAL_EXCL_LIST;
        if (value != null) {
            try {
                text = res.getString(value.resourceId);
                String str = ".9.";
            } catch (Exception e2) {
            }
        } else {
            Log.i(TAG, "DecodeImagePath(decodeResourceStream3) : value is null. res : " + res);
        }
        String mctRootDirPath = "/data/mct/external_res";
        if (!(!Build.TYPE.equals("eng") || res == null || value == null || value.resourceId == -1 || !new File("/data/mct/external_res").exists())) {
            String pkgName = res.getResourcePackageName(value.resourceId);
            Log.d(TAG, "[MCT] ResSwitch - OriginalResInfo(1)_PackageName : " + pkgName);
            String valueStr = res.getString(value.resourceId);
            Log.d(TAG, "[MCT] ResSwitch - OriginalResInfo(2)_ValueString : " + valueStr);
            String valueEntryName = res.getResourceEntryName(value.resourceId);
            Log.d(TAG, "[MCT] ResSwitch - OriginalResInfo(3)_EntryName : " + valueEntryName);
            File nullFlagFile = new File("/data/mct/external_res/" + pkgName + "/all_images_null");
            if (nullFlagFile == null || !nullFlagFile.exists()) {
                final String str2;
                String[] externalFilename;
                String subDirPath = ProxyInfo.LOCAL_EXCL_LIST;
                if (valueStr.contains("/")) {
                    subDirPath = valueStr.substring(0, valueStr.lastIndexOf(47));
                }
                if (subDirPath.contains("-v")) {
                    subDirPath = subDirPath.substring(0, subDirPath.lastIndexOf("-v"));
                }
                Log.d(TAG, "[MCT] ResSwitch - OriginalResInfo(4)_subDir : " + subDirPath);
                boolean found = false;
                String externalImagePath = ProxyInfo.LOCAL_EXCL_LIST;
                File file = new File("/data/mct/external_res/" + pkgName + "/" + subDirPath);
                if (file != null && file.isDirectory()) {
                    Log.d(TAG, "[MCT] ResSwitch - External subDir exists : /data/mct/external_res/" + pkgName + "/" + subDirPath);
                    str2 = valueEntryName;
                    externalFilename = file.list(new FilenameFilter() {
                        public boolean accept(File dir, String filename) {
                            return filename.startsWith(str2 + ".");
                        }
                    });
                    if (!(externalFilename == null || externalFilename.length <= 0 || externalFilename[0] == null)) {
                        found = true;
                        externalImagePath = "/data/mct/external_res/" + pkgName + "/" + subDirPath + "/" + externalFilename[0];
                    }
                }
                if (!found) {
                    File pkgDir = new File("/data/mct/external_res/" + pkgName);
                    if (pkgDir != null && pkgDir.isDirectory()) {
                        Log.d(TAG, "[MCT] ResSwitch - External pkgDir exists : /data/mct/external_res/" + pkgName);
                        str2 = valueEntryName;
                        externalFilename = pkgDir.list(new FilenameFilter() {
                            public boolean accept(File dir, String filename) {
                                return filename.startsWith(str2 + ".");
                            }
                        });
                        if (!(externalFilename == null || externalFilename.length <= 0 || externalFilename[0] == null)) {
                            found = true;
                            externalImagePath = "/data/mct/external_res/" + pkgName + "/" + externalFilename[0];
                        }
                    }
                }
                if (found) {
                    try {
                        is.close();
                        InputStream is2 = new FileInputStream(externalImagePath);
                        try {
                            Log.d(TAG, "[MCT] ResSwitch - Succeeded in finding to external resource : " + externalImagePath);
                            is = is2;
                        } catch (Exception e3) {
                            e = e3;
                            is = is2;
                            Log.d(TAG, "[MCT] ResSwitch - FAIL : " + externalImagePath + " - " + e.getMessage());
                            e.printStackTrace();
                            if (value != null) {
                                try {
                                    srcName = value.string.toString();
                                    b = new byte[3];
                                    is.mark(3);
                                    is.read(b, 0, 3);
                                    is.reset();
                                    if (SprClass == null) {
                                        SprClass = Class.forName("com.samsung.android.sdk.spr.drawable.SprDrawable");
                                    }
                                    if (SprCreateFromStream == null) {
                                        SprCreateFromStream = SprClass.getMethod("createFromStream", new Class[]{String.class, InputStream.class});
                                    }
                                    spr = SprCreateFromStream.invoke(SprClass, new Object[]{srcName, is});
                                    if (SprGetBitmap == null) {
                                        SprGetBitmap = SprClass.getMethod("getBitmap", new Class[0]);
                                    }
                                    return (Bitmap) SprGetBitmap.invoke(spr, new Object[0]);
                                } catch (Exception e4) {
                                    e4.printStackTrace();
                                }
                            }
                            srcName = null;
                            b = new byte[3];
                            is.mark(3);
                            is.read(b, 0, 3);
                            is.reset();
                            if (SprClass == null) {
                                SprClass = Class.forName("com.samsung.android.sdk.spr.drawable.SprDrawable");
                            }
                            if (SprCreateFromStream == null) {
                                SprCreateFromStream = SprClass.getMethod("createFromStream", new Class[]{String.class, InputStream.class});
                            }
                            spr = SprCreateFromStream.invoke(SprClass, new Object[]{srcName, is});
                            if (SprGetBitmap == null) {
                                SprGetBitmap = SprClass.getMethod("getBitmap", new Class[0]);
                            }
                            return (Bitmap) SprGetBitmap.invoke(spr, new Object[0]);
                        }
                    } catch (Exception e5) {
                        e4 = e5;
                        Log.d(TAG, "[MCT] ResSwitch - FAIL : " + externalImagePath + " - " + e4.getMessage());
                        e4.printStackTrace();
                        if (value != null) {
                            srcName = value.string.toString();
                            b = new byte[3];
                            is.mark(3);
                            is.read(b, 0, 3);
                            is.reset();
                            if (SprClass == null) {
                                SprClass = Class.forName("com.samsung.android.sdk.spr.drawable.SprDrawable");
                            }
                            if (SprCreateFromStream == null) {
                                SprCreateFromStream = SprClass.getMethod("createFromStream", new Class[]{String.class, InputStream.class});
                            }
                            spr = SprCreateFromStream.invoke(SprClass, new Object[]{srcName, is});
                            if (SprGetBitmap == null) {
                                SprGetBitmap = SprClass.getMethod("getBitmap", new Class[0]);
                            }
                            return (Bitmap) SprGetBitmap.invoke(spr, new Object[0]);
                        }
                        srcName = null;
                        b = new byte[3];
                        is.mark(3);
                        is.read(b, 0, 3);
                        is.reset();
                        if (SprClass == null) {
                            SprClass = Class.forName("com.samsung.android.sdk.spr.drawable.SprDrawable");
                        }
                        if (SprCreateFromStream == null) {
                            SprCreateFromStream = SprClass.getMethod("createFromStream", new Class[]{String.class, InputStream.class});
                        }
                        spr = SprCreateFromStream.invoke(SprClass, new Object[]{srcName, is});
                        if (SprGetBitmap == null) {
                            SprGetBitmap = SprClass.getMethod("getBitmap", new Class[0]);
                        }
                        return (Bitmap) SprGetBitmap.invoke(spr, new Object[0]);
                    }
                }
            }
            Log.d(TAG, "[MCT] ResSwitch - Using emptyBmp mode");
            return Bitmap.createBitmap(1, 1, Config.RGB_565);
        }
        if (value != null) {
            if (value.string != null && value.type == 3) {
                srcName = value.string.toString();
                if ((srcName == null || srcName.isEmpty() || srcName.endsWith(".bmp") || srcName.endsWith(".spr")) && is.markSupported()) {
                    b = new byte[3];
                    is.mark(3);
                    is.read(b, 0, 3);
                    is.reset();
                    if (b[0] == (byte) 83 && b[1] == (byte) 80 && b[2] == (byte) 82) {
                        if (SprClass == null) {
                            SprClass = Class.forName("com.samsung.android.sdk.spr.drawable.SprDrawable");
                        }
                        if (SprCreateFromStream == null) {
                            SprCreateFromStream = SprClass.getMethod("createFromStream", new Class[]{String.class, InputStream.class});
                        }
                        spr = SprCreateFromStream.invoke(SprClass, new Object[]{srcName, is});
                        if (SprGetBitmap == null) {
                            SprGetBitmap = SprClass.getMethod("getBitmap", new Class[0]);
                        }
                        return (Bitmap) SprGetBitmap.invoke(spr, new Object[0]);
                    }
                }
                if (opts == null) {
                    opts = new Options();
                }
                if (opts.inDensity == 0 && value != null) {
                    int density = value.density;
                    if (density == 0) {
                        opts.inDensity = 160;
                    } else if (density != 65535) {
                        opts.inDensity = density;
                    }
                }
                if (opts.inTargetDensity == 0 && res != null) {
                    opts.inTargetDensity = res.getDisplayMetrics().densityDpi;
                }
                Bitmap tmpBitmap = decodeStream(is, pad, opts);
                if (tmpBitmap != null) {
                    tmpBitmap.setImagePath(value);
                }
                return tmpBitmap;
            }
        }
        srcName = null;
        b = new byte[3];
        is.mark(3);
        is.read(b, 0, 3);
        is.reset();
        if (SprClass == null) {
            SprClass = Class.forName("com.samsung.android.sdk.spr.drawable.SprDrawable");
        }
        if (SprCreateFromStream == null) {
            SprCreateFromStream = SprClass.getMethod("createFromStream", new Class[]{String.class, InputStream.class});
        }
        spr = SprCreateFromStream.invoke(SprClass, new Object[]{srcName, is});
        if (SprGetBitmap == null) {
            SprGetBitmap = SprClass.getMethod("getBitmap", new Class[0]);
        }
        return (Bitmap) SprGetBitmap.invoke(spr, new Object[0]);
    }

    public static Bitmap decodeResource(Resources res, int id, Options opts) {
        Bitmap bm = null;
        InputStream is = null;
        try {
            TypedValue value = new TypedValue();
            is = res.openRawResource(id, value);
            bm = decodeResourceStream(res, value, is, null, opts);
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        } catch (Exception e2) {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e3) {
                }
            }
        } catch (Throwable th) {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e4) {
                }
            }
        }
        if (bm != null || opts == null || opts.inBitmap == null) {
            return bm;
        }
        throw new IllegalArgumentException("Problem decoding into existing bitmap");
    }

    public static Bitmap decodeResource(Resources res, int id) {
        return decodeResource(res, id, null);
    }

    public static Bitmap decodeByteArray(byte[] data, int offset, int length, Options opts) {
        if ((offset | length) < 0 || data.length < offset + length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        checkPreferIPTToRGBA();
        Trace.traceBegin(2, "decodeBitmap");
        try {
            Bitmap bm = nativeDecodeByteArray(data, offset, length, opts);
            if (bm != null || opts == null || opts.inBitmap == null) {
                setDensityFromOptions(bm, opts);
                return bm;
            }
            throw new IllegalArgumentException("Problem decoding into existing bitmap");
        } finally {
            Trace.traceEnd(2);
        }
    }

    public static Bitmap decodeByteArray(byte[] data, int offset, int length) {
        return decodeByteArray(data, offset, length, null);
    }

    private static void setDensityFromOptions(Bitmap outputBitmap, Options opts) {
        if (outputBitmap != null && opts != null) {
            int density = opts.inDensity;
            if (density != 0) {
                outputBitmap.setDensity(density);
                int targetDensity = opts.inTargetDensity;
                if (targetDensity != 0 && density != targetDensity && density != opts.inScreenDensity) {
                    byte[] np = outputBitmap.getNinePatchChunk();
                    boolean isNinePatch = np != null && NinePatch.isNinePatchChunk(np);
                    if (opts.inScaled || isNinePatch) {
                        outputBitmap.setDensity(targetDensity);
                    }
                }
            } else if (opts.inBitmap != null) {
                outputBitmap.setDensity(Bitmap.getDefaultDensity());
            }
        }
    }

    public static Bitmap decodeStream(InputStream is, Rect outPadding, Options opts) {
        checkPreferIPTToRGBA();
        if (is == null) {
            return null;
        }
        Bitmap bm = null;
        Trace.traceBegin(2, "decodeBitmap");
        try {
            if (is instanceof AssetInputStream) {
                bm = nativeDecodeAsset(((AssetInputStream) is).getNativeAsset(), outPadding, opts);
            } else {
                bm = decodeStreamInternal(is, outPadding, opts);
            }
            if (bm != null || opts == null || opts.inBitmap == null) {
                setDensityFromOptions(bm, opts);
                return bm;
            }
            throw new IllegalArgumentException("Problem decoding into existing bitmap");
        } finally {
            Trace.traceEnd(2);
        }
    }

    private static Bitmap decodeStreamInternal(InputStream is, Rect outPadding, Options opts) {
        byte[] tempStorage = null;
        if (opts != null) {
            tempStorage = opts.inTempStorage;
        }
        if (tempStorage == null) {
            tempStorage = new byte[16384];
        }
        return nativeDecodeStream(is, tempStorage, outPadding, opts);
    }

    public static Bitmap decodeStream(InputStream is) {
        return decodeStream(is, null, null);
    }

    public static Bitmap decodeFileDescriptor(FileDescriptor fd, Rect outPadding, Options opts) {
        checkPreferIPTToRGBA();
        Trace.traceBegin(2, "decodeFileDescriptor");
        FileInputStream fis;
        try {
            Bitmap bm;
            if (nativeIsSeekable(fd)) {
                bm = nativeDecodeFileDescriptor(fd, outPadding, opts);
            } else {
                fis = new FileInputStream(fd);
                bm = decodeStreamInternal(fis, outPadding, opts);
                try {
                    fis.close();
                } catch (Throwable th) {
                }
            }
            if (bm != null || opts == null || opts.inBitmap == null) {
                setDensityFromOptions(bm, opts);
                Trace.traceEnd(2);
                return bm;
            }
            throw new IllegalArgumentException("Problem decoding into existing bitmap");
        } catch (Throwable th2) {
            Trace.traceEnd(2);
        }
    }

    public static Bitmap decodeFileDescriptor(FileDescriptor fd) {
        return decodeFileDescriptor(fd, null, null);
    }

    private static void checkPreferIPTToRGBA() {
        if (!checkedPreferIPT && ActivityThread.currentActivityThread() != null) {
            Application app = ActivityThread.currentApplication();
            if (app != null) {
                String packageName = app.getPackageName();
                if (packageName != null) {
                    checkedPreferIPT = true;
                    try {
                        ApplicationInfo appInfo = app.getPackageManager().getApplicationInfo(packageName, 128);
                        if (appInfo != null) {
                            Bundle aBundle = appInfo.metaData;
                            if (aBundle != null && aBundle.getBoolean("index-palette-bitmaps-supported", false)) {
                                nativeSetPreferIPTToRGBA();
                            }
                        }
                    } catch (NameNotFoundException ex) {
                        Log.i(TAG, "GFX Error checking checkPreferIPTToRGBA" + packageName);
                        ex.printStackTrace();
                    }
                }
            }
        }
    }
}
