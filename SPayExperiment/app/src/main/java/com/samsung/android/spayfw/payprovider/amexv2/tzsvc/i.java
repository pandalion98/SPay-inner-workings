/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.util.Base64
 *  java.lang.CharSequence
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.List
 */
package com.samsung.android.spayfw.payprovider.amexv2.tzsvc;

import android.util.Base64;
import java.util.ArrayList;
import java.util.List;

public class i {
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    public static final String convertToPem(byte[] arrby, boolean bl) {
        String string = Base64.encodeToString((byte[])arrby, (int)2).replaceAll("(.{64})", "$1");
        String string2 = "-----BEGIN CERTIFICATE-----" + string + "-----END CERTIFICATE-----";
        if (bl) {
            string2 = Base64.encodeToString((byte[])string2.getBytes(), (int)2);
        }
        return string2;
    }

    public static final String encodeHex(byte[] arrby) {
        int n2 = 0;
        int n3 = arrby.length;
        char[] arrc = new char[n3 << 1];
        for (int i2 = 0; i2 < n3; ++i2) {
            int n4 = n2 + 1;
            arrc[n2] = HEX_ARRAY[(240 & arrby[i2]) >>> 4];
            n2 = n4 + 1;
            arrc[n4] = HEX_ARRAY[15 & arrby[i2]];
        }
        return new String(arrc);
    }

    public static final byte[] fromBase64(String string) {
        if (string == null || string.length() == 0) {
            return null;
        }
        return Base64.decode((String)string, (int)0);
    }

    public static final List<byte[]> getDerChain(String string) {
        ArrayList arrayList = new ArrayList();
        String[] arrstring = i.splitPem(string);
        for (int i2 = 0; i2 < arrstring.length; ++i2) {
            arrayList.add((Object)Base64.decode((String)i.stripPem(arrstring[i2]), (int)0));
        }
        return arrayList;
    }

    static String[] splitPem(String string) {
        String[] arrstring = string.split("-----BEGIN CERTIFICATE-----");
        String[] arrstring2 = new String[-1 + arrstring.length];
        for (int i2 = 1; i2 < arrstring.length; ++i2) {
            arrstring2[i2 - 1] = i.stripPem("-----BEGIN CERTIFICATE-----" + arrstring[i2]);
        }
        return arrstring2;
    }

    public static final String stripPem(String string) {
        return string.replace((CharSequence)"-----BEGIN CERTIFICATE-----", (CharSequence)"").replace((CharSequence)"-----END CERTIFICATE-----", (CharSequence)"").replace((CharSequence)"\n", (CharSequence)"");
    }

    public static final String toBase64(byte[] arrby) {
        if (arrby == null || arrby.length == 0) {
            return null;
        }
        return Base64.encodeToString((byte[])arrby, (int)2);
    }
}

