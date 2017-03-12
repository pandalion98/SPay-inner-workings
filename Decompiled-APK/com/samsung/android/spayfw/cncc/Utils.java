package com.samsung.android.spayfw.cncc;

import android.util.Base64;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.util.ArrayList;
import java.util.List;
import javolution.io.Struct.Unsigned8;

public class Utils {
    private static final char[] HEX_ARRAY;
    private static final String TAG = "Utils";

    static {
        HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    }

    public static final String toBase64(byte[] bArr) {
        return (bArr == null || bArr.length == 0) ? null : Base64.encodeToString(bArr, 2);
    }

    public static final byte[] fromBase64(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        return Base64.decode(str, 0);
    }

    public static final String[] toBase64(byte[][] bArr) {
        if (bArr == null || bArr.length == 0) {
            return null;
        }
        String[] strArr = new String[bArr.length];
        for (int i = 0; i < bArr.length; i++) {
            strArr[i] = Base64.encodeToString(bArr[i], 2);
        }
        return strArr;
    }

    public static final byte[][] fromBase64(String[] strArr) {
        if (strArr == null || strArr.length == 0) {
            return (byte[][]) null;
        }
        byte[][] bArr = new byte[strArr.length][];
        for (int i = 0; i < strArr.length; i++) {
            bArr[i] = Base64.decode(strArr[i], 0);
        }
        return bArr;
    }

    public static final String convertToPem(byte[] bArr) {
        return convertToPem(bArr, false);
    }

    public static final String convertToPem(byte[] bArr, boolean z) {
        String str = "-----BEGIN CERTIFICATE-----" + Base64.encodeToString(bArr, 2).replaceAll("(.{64})", "$1") + "-----END CERTIFICATE-----";
        if (z) {
            return Base64.encodeToString(str.getBytes(), 2);
        }
        return str;
    }

    public static final byte[] convertToDer(String str) {
        return Base64.decode(str.replace("-----BEGIN CERTIFICATE-----", BuildConfig.FLAVOR).replace("-----END CERTIFICATE-----", BuildConfig.FLAVOR).replace("\n", BuildConfig.FLAVOR), 0);
    }

    static String[] splitPem(String str) {
        String str2 = "-----BEGIN CERTIFICATE-----";
        String[] split = str.split(str2);
        String[] strArr = new String[(split.length - 1)];
        for (int i = 1; i < split.length; i++) {
            strArr[i - 1] = stripPem(str2 + split[i]);
        }
        return strArr;
    }

    public static final String stripPem(String str) {
        return str.replace("-----BEGIN CERTIFICATE-----", BuildConfig.FLAVOR).replace("-----END CERTIFICATE-----", BuildConfig.FLAVOR).replace("\n", BuildConfig.FLAVOR);
    }

    public static final List<byte[]> getDerChain(String str) {
        List<byte[]> arrayList = new ArrayList();
        String[] splitPem = splitPem(str);
        for (String stripPem : splitPem) {
            arrayList.add(Base64.decode(stripPem(stripPem), 0));
        }
        return arrayList;
    }

    public static final String encodeHex(byte[] bArr) {
        int i = 0;
        int length = bArr.length;
        char[] cArr = new char[(length << 1)];
        for (int i2 = 0; i2 < length; i2++) {
            int i3 = i + 1;
            cArr[i] = HEX_ARRAY[(bArr[i2] & 240) >>> 4];
            i = i3 + 1;
            cArr[i3] = HEX_ARRAY[bArr[i2] & 15];
        }
        return new String(cArr);
    }

    public static final byte[] decodeHex(String str) {
        int length = str.length();
        byte[] bArr = new byte[(length / 2)];
        for (int i = 0; i < length; i += 2) {
            bArr[i / 2] = (byte) ((Character.digit(str.charAt(i), 16) << 4) + Character.digit(str.charAt(i + 1), 16));
        }
        return bArr;
    }

    public static void setUnsigned8ArrayData(Unsigned8[] unsigned8Arr, byte[] bArr) {
        if (bArr != null && bArr.length > 0) {
            if (bArr.length > unsigned8Arr.length) {
                Log.m286e(TAG, "Error: Can't set a buffer more than the size of the destination Byte Array");
                return;
            }
            for (int i = 0; i < bArr.length; i++) {
                unsigned8Arr[i].set((short) bArr[i]);
            }
        }
    }

    public static byte[] getByteArray(Unsigned8[] unsigned8Arr) {
        if (unsigned8Arr == null || unsigned8Arr.length == 0) {
            return null;
        }
        byte[] bArr = new byte[unsigned8Arr.length];
        for (int i = 0; i < bArr.length; i++) {
            bArr[i] = (byte) unsigned8Arr[i].get();
        }
        return bArr;
    }
}
