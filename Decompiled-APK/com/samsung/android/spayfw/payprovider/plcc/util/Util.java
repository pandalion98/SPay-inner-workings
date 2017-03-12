package com.samsung.android.spayfw.payprovider.plcc.util;

import android.util.Base64;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.util.UUID;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

public final class Util {
    protected static final char[] hexArray;

    static {
        hexArray = "0123456789ABCDEF".toCharArray();
    }

    public static final String convertToPem(byte[] bArr) {
        return "-----BEGIN CERTIFICATE-----\n" + Base64.encodeToString(bArr, 0) + "-----END CERTIFICATE-----";
    }

    public static long createProviderKey(String str) {
        return UUID.fromString(str).getMostSignificantBits();
    }

    public static String bytesToHex(byte[] bArr) {
        char[] cArr = new char[(bArr.length * 2)];
        for (int i = 0; i < bArr.length; i++) {
            int i2 = bArr[i] & GF2Field.MASK;
            cArr[i * 2] = hexArray[i2 >>> 4];
            cArr[(i * 2) + 1] = hexArray[i2 & 15];
        }
        return new String(cArr).toLowerCase();
    }

    public static byte[] hexStringToBytes(String str) {
        if (str == null || str.equals(BuildConfig.FLAVOR)) {
            return null;
        }
        String toUpperCase = str.toUpperCase();
        int length = toUpperCase.length() / 2;
        char[] toCharArray = toUpperCase.toCharArray();
        byte[] bArr = new byte[length];
        for (int i = 0; i < length; i++) {
            int i2 = i * 2;
            bArr[i] = (byte) (charToByte(toCharArray[i2 + 1]) | (charToByte(toCharArray[i2]) << 4));
        }
        return bArr;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
}
