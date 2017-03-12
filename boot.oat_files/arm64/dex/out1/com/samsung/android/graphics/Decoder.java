package com.samsung.android.graphics;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.util.Log;

public class Decoder {
    private static final String TAG = "Decoder";

    public static class Options {
        public Bitmap inBitmap;
        public boolean inJustDecodeBounds;
        public boolean inMutable;
        public int inPageNum;
        public boolean inPreferQualityOverSpeed;
        public Config inPreferredConfig = Config.ARGB_8888;
        public int inSampleSize;
        public boolean isPreview = false;
        public int outHeight;
        public int outPageNum;
        public int outWidth;
    }

    private static native Bitmap nativeCreateBitmap(int i, int i2);

    private static native long nativeCreateFds();

    private static native int nativeDecode(long j, int i);

    private static native Bitmap nativeDecodeByteArray(byte[] bArr, int i, int i2, long j, int i3, Bitmap bitmap);

    private static native Bitmap nativeDecodeFile(String str, long j, int i, Bitmap bitmap);

    private static native int nativecopybytebuffer(long j, byte[] bArr, int i, int i2);

    private static native int nativecopyfilename(long j, String str);

    private static native long nativedecinstance(String str, byte[] bArr, int i, int i2, int i3, int[] iArr);

    private static native int nativefreeFds(long j);

    private static native int nativegetImageinfo(long j, int[] iArr);

    private static native int nativelockBitmap(long j, Bitmap bitmap);

    private static native int nativeunlockBitmap(Bitmap bitmap);

    static {
        try {
            System.loadLibrary("MMCodec");
        } catch (Exception e) {
            Log.e(TAG, "Load library fail : " + e.toString());
        }
    }

    public static Bitmap decodeByteArray(byte[] data, int offset, int length, Options opts) {
        if (data == null || length <= 0 || offset >= length) {
            return null;
        }
        long secmmfd = nativeCreateFds();
        int[] tmp = new int[2];
        nativecopybytebuffer(secmmfd, data, offset, data.length);
        nativegetImageinfo(secmmfd, tmp);
        Bitmap mBm = doDecode(secmmfd, tmp, opts);
        nativefreeFds(secmmfd);
        if (mBm != null) {
            return mBm;
        }
        android.graphics.BitmapFactory.Options bfOptions = getBitmapFactoryOptions(opts);
        mBm = BitmapFactory.decodeByteArray(data, offset, length, bfOptions);
        opts.outWidth = bfOptions.outWidth;
        opts.outHeight = bfOptions.outHeight;
        return mBm;
    }

    public static Bitmap decodeByteArray(byte[] data, int offset, int length) {
        if (data == null || length <= 0 || offset >= length) {
            return null;
        }
        byte[] bArr = data;
        int i = offset;
        long secmmfd = nativedecinstance("nofile", bArr, i, data.length, 0, new int[2]);
        if (secmmfd == 0) {
            return BitmapFactory.decodeByteArray(data, offset, length);
        }
        return nativeDecodeByteArray(data, offset, data.length, secmmfd, 1, null);
    }

    public static Bitmap decodeFile(String pathName) {
        if (pathName == null) {
            return null;
        }
        long secmmfd = nativedecinstance(pathName, null, 0, 0, 1, new int[2]);
        if (secmmfd != 0) {
            return nativeDecodeFile(pathName, secmmfd, 1, null);
        }
        return BitmapFactory.decodeFile(pathName);
    }

    public static Bitmap decodeFile(String pathName, Options opts) {
        if (pathName == null) {
            return null;
        }
        long secmmfd = nativeCreateFds();
        int[] tmp = new int[2];
        nativecopyfilename(secmmfd, pathName);
        nativegetImageinfo(secmmfd, tmp);
        Bitmap mBm = doDecode(secmmfd, tmp, opts);
        nativefreeFds(secmmfd);
        if (mBm != null) {
            return mBm;
        }
        android.graphics.BitmapFactory.Options bfOptions = getBitmapFactoryOptions(opts);
        mBm = BitmapFactory.decodeFile(pathName, bfOptions);
        opts.outWidth = bfOptions.outWidth;
        opts.outHeight = bfOptions.outHeight;
        return mBm;
    }

    private static Bitmap doDecode(long secmmfd, int[] tmp, Options opts) {
        if (secmmfd == 0) {
            return null;
        }
        int img_wid = tmp[0];
        int img_hei = tmp[1];
        if (img_wid == -1 || img_hei == -1) {
            return null;
        }
        if (opts.inJustDecodeBounds) {
            opts.outWidth = img_wid;
            opts.outHeight = img_hei;
            return opts.inBitmap;
        }
        Bitmap mBm;
        int samplesize = opts.inSampleSize;
        if (samplesize == 0) {
            samplesize = 1;
        }
        img_wid = ((img_wid + samplesize) - 1) / samplesize;
        img_hei = ((img_hei + samplesize) - 1) / samplesize;
        if (opts.inBitmap == null) {
            mBm = nativeCreateBitmap(img_wid, img_hei);
        } else if (opts.inBitmap.getWidth() == img_wid && opts.inBitmap.getHeight() == img_hei) {
            mBm = opts.inBitmap;
        } else {
            Log.v(TAG, "inBitmap Erraneous\n");
            mBm = null;
        }
        if (mBm == null) {
            return mBm;
        }
        nativelockBitmap(secmmfd, mBm);
        nativeDecode(secmmfd, samplesize);
        nativeunlockBitmap(mBm);
        return mBm;
    }

    protected static android.graphics.BitmapFactory.Options getBitmapFactoryOptions(Options options) {
        android.graphics.BitmapFactory.Options bfOptions = new android.graphics.BitmapFactory.Options();
        if (options != null) {
            bfOptions.inBitmap = options.inBitmap;
            bfOptions.inMutable = options.inMutable;
            bfOptions.inJustDecodeBounds = options.inJustDecodeBounds;
            bfOptions.inSampleSize = options.inSampleSize;
            bfOptions.inPreferredConfig = options.inPreferredConfig;
            bfOptions.inPreferQualityOverSpeed = options.inPreferQualityOverSpeed;
            bfOptions.isPreview = options.isPreview;
        }
        return bfOptions;
    }
}
