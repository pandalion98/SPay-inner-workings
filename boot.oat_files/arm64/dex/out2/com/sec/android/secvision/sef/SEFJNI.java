package com.sec.android.secvision.sef;

class SEFJNI {
    static native int addFastSEFData(String str, String str2, int i, byte[] bArr, int i2, byte[] bArr2, int i3, int i4, int i5);

    static native int addFastSEFDataFile(String str, String str2, int i, byte[] bArr, int i2, String str3, int i3, int i4);

    static native int addSEFData(String str, String str2, int i, byte[] bArr, int i2, byte[] bArr2, int i3, int i4, int i5);

    static native int addSEFDataFile(String str, String str2, int i, byte[] bArr, int i2, String str3, int i3, int i4);

    static native int addSEFDataFileToMP4(String str, String str2, int i, byte[] bArr, int i2, String str3, int i3, int i4);

    static native int addSEFDataFiles(String str, String[] strArr, int[] iArr, String[] strArr2, int[] iArr2, int i, int i2);

    static native int addSEFDataToMP4(String str, String str2, int i, byte[] bArr, int i2, byte[] bArr2, int i3, int i4, int i5);

    static native int clearQdioData(String str);

    static native int clearSEFData(String str);

    static native int copyAllSEFData(String str, String str2);

    static native int deleteQdioData(String str, String str2, int i);

    static native int deleteSEFData(String str, String str2, int i);

    static native int fastClearSEFData(String str);

    static native int fastDeleteSEFData(String str, String str2, int i);

    static native int getNativeVersion();

    static native int getSEFDataCount(String str);

    static native int[] getSEFDataPosition(String str, String str2);

    static native long[] getSEFDataPosition64(String str, String str2);

    static native int getSEFDataType(String str, String str2);

    static native int[] getSEFSubDataPosition(String str, String str2);

    static native long[] getSEFSubDataPosition64(String str, String str2);

    static native int isSEFFile(String str);

    static native String[] listKeyNames(String str);

    static native String[] listKeyNamesByDataType(String str, int i);

    static native int[] listSEFDataTypes(String str);

    static native int saveAudioJPEG(String str, byte[] bArr, int i, String str2, int i2);

    SEFJNI() {
    }

    static {
        System.loadLibrary("SEF");
    }
}
