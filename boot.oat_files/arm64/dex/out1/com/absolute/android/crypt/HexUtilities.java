package com.absolute.android.crypt;

import android.text.format.DateFormat;

public class HexUtilities {
    private static final char[] a = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', DateFormat.CAPITAL_AM_PM, 'B', 'C', 'D', DateFormat.DAY, 'F'};

    private HexUtilities() {
    }

    public static byte[] GetBytesFromHexString(String str) {
        if (str == null || str.length() % 2 != 0) {
            throw new IllegalArgumentException("GetBytesFromHexString");
        }
        byte[] bArr = new byte[(str.length() / 2)];
        for (int i = 0; i < bArr.length; i++) {
            bArr[i] = (byte) Short.parseShort(str.substring(i * 2, (i * 2) + 2), 16);
        }
        return bArr;
    }

    public static String EncodeBytesAsHexString(byte[] bArr) {
        StringBuffer stringBuffer = new StringBuffer();
        if (bArr != null) {
            for (int i = 0; i < bArr.length; i++) {
                stringBuffer.append(a[(bArr[i] & 240) >> 4]);
                stringBuffer.append(a[bArr[i] & 15]);
            }
        }
        return stringBuffer.toString();
    }
}
