/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.util.Base64
 *  java.lang.Object
 *  java.lang.String
 *  java.util.UUID
 */
package com.samsung.android.spayfw.payprovider.plcc.util;

import android.util.Base64;
import java.util.UUID;

public final class Util {
    protected static final char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] arrby) {
        char[] arrc = new char[2 * arrby.length];
        for (int i2 = 0; i2 < arrby.length; ++i2) {
            int n2 = 255 & arrby[i2];
            arrc[i2 * 2] = hexArray[n2 >>> 4];
            arrc[1 + i2 * 2] = hexArray[n2 & 15];
        }
        return new String(arrc).toLowerCase();
    }

    private static byte charToByte(char c2) {
        return (byte)"0123456789ABCDEF".indexOf((int)c2);
    }

    public static final String convertToPem(byte[] arrby) {
        String string = Base64.encodeToString((byte[])arrby, (int)0);
        return "-----BEGIN CERTIFICATE-----\n" + string + "-----END CERTIFICATE-----";
    }

    public static long createProviderKey(String string) {
        return UUID.fromString((String)string).getMostSignificantBits();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static byte[] hexStringToBytes(String string) {
        if (string == null) return null;
        if (string.equals((Object)"")) {
            return null;
        }
        String string2 = string.toUpperCase();
        int n2 = string2.length() / 2;
        char[] arrc = string2.toCharArray();
        byte[] arrby = new byte[n2];
        int n3 = 0;
        while (n3 < n2) {
            int n4 = n3 * 2;
            arrby[n3] = (byte)(Util.charToByte(arrc[n4]) << 4 | Util.charToByte(arrc[n4 + 1]));
            ++n3;
        }
        return arrby;
    }
}

