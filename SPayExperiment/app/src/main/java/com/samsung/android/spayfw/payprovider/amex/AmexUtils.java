/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.text.TextUtils
 *  java.lang.CharSequence
 *  java.lang.Character
 *  java.lang.Exception
 *  java.lang.Integer
 *  java.lang.Long
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.security.MessageDigest
 *  org.bouncycastle.util.encoders.Base64
 */
package com.samsung.android.spayfw.payprovider.amex;

import android.text.TextUtils;
import com.samsung.android.spayfw.b.c;
import java.security.MessageDigest;
import org.bouncycastle.util.encoders.Base64;

public class AmexUtils {
    private static final char[] HEX_ARRAY;
    protected static final char[] hexArray;
    private static int pB;

    static {
        pB = 10;
        HEX_ARRAY = "0123456789ABCDEF".toCharArray();
        hexArray = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    }

    public static LupcMetaData aA(String string) {
        int n2;
        if (TextUtils.isEmpty((CharSequence)string)) {
            c.e("AmexUtils", "metaDataBlob is empty!");
            return null;
        }
        if (!string.startsWith("E6")) {
            c.e("AmexUtils", "LUPC MetaData Tag not found");
            return null;
        }
        int n3 = (-4 + string.length()) / 2;
        if (n3 != (n2 = Integer.parseInt((String)string.substring(2, 4), (int)16))) {
            c.e("AmexUtils", "Length of data " + n3 + " does not match extracted length " + n2);
            return null;
        }
        LupcMetaData lupcMetaData = new LupcMetaData();
        if (!string.substring(4, 6).equals((Object)"16")) {
            c.e("AmexUtils", "NFC LUPC Count Tag not found");
            return null;
        }
        int n4 = 2 * Integer.parseInt((String)string.substring(6, 8), (int)16);
        lupcMetaData.nfcLupcCount = Integer.parseInt((String)string.substring(8, n4 + 8));
        int n5 = n4 + 8;
        if (!string.substring(n5, n5 + 2).equals((Object)"17")) {
            c.e("AmexUtils", "NFC LUPC Expiry Tag not found");
            return null;
        }
        int n6 = n5 + 2;
        int n7 = 2 * Integer.parseInt((String)string.substring(n6, n6 + 2), (int)16);
        int n8 = n6 + 2;
        lupcMetaData.nfcLupcExpiry = Long.parseLong((String)string.substring(n8, n8 + n7));
        int n9 = n8 + n7;
        if (n9 == 4 + n2 * 2) {
            return lupcMetaData;
        }
        if (!string.substring(n9, n9 + 2).equals((Object)"53")) {
            c.e("AmexUtils", "Other LUPC Count Tag not found");
            return null;
        }
        int n10 = n9 + 2;
        int n11 = 2 * Integer.parseInt((String)string.substring(n10, n10 + 2), (int)16);
        int n12 = n10 + 2;
        lupcMetaData.otherLupcCount = Integer.parseInt((String)string.substring(n12, n12 + n11));
        int n13 = n12 + n11;
        if (!string.substring(n13, n13 + 2).equals((Object)"54")) {
            c.e("AmexUtils", "Other LUPC Expiry Tag not found");
            return null;
        }
        int n14 = n13 + 2;
        int n15 = 2 * Integer.parseInt((String)string.substring(n14, n14 + 2), (int)16);
        int n16 = n14 + 2;
        lupcMetaData.otherLupcExpiry = Long.parseLong((String)string.substring(n16, n16 + n15));
        n16 + n15;
        return lupcMetaData;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static String az(String string) {
        String string2;
        String string3 = null;
        try {
            String string4;
            MessageDigest messageDigest = MessageDigest.getInstance((String)"SHA-256");
            byte[] arrby = string.getBytes("UTF-8");
            messageDigest.update(arrby);
            byte[] arrby2 = messageDigest.digest();
            String string5 = AmexUtils.encodeHex(arrby);
            String string6 = AmexUtils.encodeHex(arrby2) + "00000001";
            byte[] arrby3 = new byte[32];
            for (int i2 = AmexUtils.pB; i2 > 0; --i2) {
                string6 = AmexUtils.n(string5, string6);
                AmexUtils.xor(arrby3, AmexUtils.decodeHex(string6));
                String string7 = AmexUtils.encodeHex(arrby3);
                string3 = string7;
            }
            if (string3 == null) return string3;
            string2 = string3.length() > 64 ? (string4 = string3.substring(0, 64)) : string3;
        }
        catch (Exception exception) {
            return string3;
        }
        try {
            return new String(Base64.encode((byte[])AmexUtils.decodeHex(string2)));
        }
        catch (Exception exception) {
            return string2;
        }
    }

    public static String byteArrayToHex(byte[] arrby) {
        char[] arrc = new char[2 * arrby.length];
        for (int i2 = 0; i2 < arrby.length; ++i2) {
            int n2 = 255 & arrby[i2];
            arrc[i2 * 2] = hexArray[n2 >>> 4];
            arrc[1 + i2 * 2] = hexArray[n2 & 15];
        }
        return new String(arrc);
    }

    public static final byte[] decodeHex(String string) {
        int n2 = string.length();
        byte[] arrby = new byte[n2 / 2];
        for (int i2 = 0; i2 < n2; i2 += 2) {
            arrby[i2 / 2] = (byte)((Character.digit((char)string.charAt(i2), (int)16) << 4) + Character.digit((char)string.charAt(i2 + 1), (int)16));
        }
        return arrby;
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

    private static String n(String string, String string2) {
        int n2 = 0;
        byte[] arrby = new byte[65];
        byte[] arrby2 = new byte[65];
        new byte[16];
        MessageDigest messageDigest = MessageDigest.getInstance((String)"SHA-256");
        byte[] arrby3 = AmexUtils.decodeHex(string);
        byte[] arrby4 = AmexUtils.decodeHex(string2);
        if (arrby3.length > 64) {
            messageDigest.update(arrby3);
            arrby3 = messageDigest.digest();
        }
        System.arraycopy((Object)arrby3, (int)0, (Object)arrby2, (int)0, (int)arrby3.length);
        System.arraycopy((Object)arrby3, (int)0, (Object)arrby, (int)0, (int)arrby3.length);
        while (n2 < 64) {
            arrby2[n2] = (byte)(54 ^ arrby2[n2]);
            arrby[n2] = (byte)(92 ^ arrby[n2]);
            ++n2;
        }
        try {
            messageDigest.update(arrby2, 0, 64);
            messageDigest.update(arrby4, 0, arrby4.length);
            byte[] arrby5 = messageDigest.digest();
            messageDigest.update(arrby, 0, 64);
            messageDigest.update(arrby5, 0, messageDigest.getDigestLength());
            String string3 = AmexUtils.encodeHex(messageDigest.digest());
            return string3;
        }
        catch (Exception exception) {
            c.c("AmexUtils", exception.getMessage(), exception);
            return null;
        }
    }

    private static void xor(byte[] arrby, byte[] arrby2) {
        for (int i2 = 0; i2 < arrby.length; ++i2) {
            arrby[i2] = (byte)(arrby[i2] ^ arrby2[i2]);
        }
    }

    public static final class LupcMetaData {
        public int nfcLupcCount;
        public long nfcLupcExpiry;
        public int otherLupcCount;
        public long otherLupcExpiry;
    }

}

