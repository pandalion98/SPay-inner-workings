/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 */
package com.samsung.android.spayfw.payprovider.discover.util;

public class b {
    public static String byteArrayToHex(byte[] arrby) {
        StringBuilder stringBuilder = new StringBuilder(2 * arrby.length);
        for (byte by : arrby) {
            Object[] arrobject = new Object[]{by & 255};
            stringBuilder.append(String.format((String)"%02x", (Object[])arrobject));
        }
        return stringBuilder.toString();
    }
}

