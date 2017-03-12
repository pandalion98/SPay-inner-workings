package android.graphics;

import android.content.res.AssetManager;

public class FontFamily {
    public long mNativePtr;

    private static native boolean nAddFont(long j, String str);

    private static native boolean nAddFontFromAsset(long j, AssetManager assetManager, String str);

    private static native boolean nAddFontWeightStyle(long j, String str, int i, boolean z);

    private static native long nCreateFamily(String str, int i);

    private static native void nUnrefFamily(long j);

    public FontFamily() {
        this.mNativePtr = nCreateFamily(null, 0);
        if (this.mNativePtr == 0) {
            throw new IllegalStateException("error creating native FontFamily");
        }
    }

    public FontFamily(String lang, String variant) {
        int varEnum = 0;
        if ("compact".equals(variant)) {
            varEnum = 1;
        } else if ("elegant".equals(variant)) {
            varEnum = 2;
        }
        this.mNativePtr = nCreateFamily(lang, varEnum);
        if (this.mNativePtr == 0) {
            throw new IllegalStateException("error creating native FontFamily");
        }
    }

    protected void finalize() throws Throwable {
        try {
            nUnrefFamily(this.mNativePtr);
        } finally {
            super.finalize();
        }
    }

    public boolean addFont(String path) {
        return nAddFont(this.mNativePtr, path);
    }

    public boolean addFontWeightStyle(String path, int weight, boolean style) {
        return nAddFontWeightStyle(this.mNativePtr, path, weight, style);
    }

    public boolean addFontFromAsset(AssetManager mgr, String path) {
        return nAddFontFromAsset(this.mNativePtr, mgr, path);
    }
}
