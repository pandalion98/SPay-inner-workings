/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.util.Base64
 *  java.lang.CharSequence
 *  java.lang.Character
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.List
 */
package com.samsung.android.spayfw.cncc;

import android.util.Base64;
import com.samsung.android.spayfw.b.c;
import java.util.ArrayList;
import java.util.List;
import javolution.io.Struct;

public class Utils {
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    private static final String TAG = "Utils";

    public static final byte[] convertToDer(String string) {
        return Base64.decode((String)string.replace((CharSequence)"-----BEGIN CERTIFICATE-----", (CharSequence)"").replace((CharSequence)"-----END CERTIFICATE-----", (CharSequence)"").replace((CharSequence)"\n", (CharSequence)""), (int)0);
    }

    public static final String convertToPem(byte[] arrby) {
        return Utils.convertToPem(arrby, false);
    }

    public static final String convertToPem(byte[] arrby, boolean bl) {
        String string = Base64.encodeToString((byte[])arrby, (int)2).replaceAll("(.{64})", "$1");
        String string2 = "-----BEGIN CERTIFICATE-----" + string + "-----END CERTIFICATE-----";
        if (bl) {
            string2 = Base64.encodeToString((byte[])string2.getBytes(), (int)2);
        }
        return string2;
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

    public static final byte[] fromBase64(String string) {
        if (string == null || string.length() == 0) {
            return null;
        }
        return Base64.decode((String)string, (int)0);
    }

    public static final byte[][] fromBase64(String[] arrstring) {
        if (arrstring == null || arrstring.length == 0) {
            return null;
        }
        byte[][] arrarrby = new byte[arrstring.length][];
        for (int i2 = 0; i2 < arrstring.length; ++i2) {
            arrarrby[i2] = Base64.decode((String)arrstring[i2], (int)0);
        }
        return arrarrby;
    }

    public static byte[] getByteArray(Struct.Unsigned8[] arrunsigned8) {
        if (arrunsigned8 == null || arrunsigned8.length == 0) {
            return null;
        }
        byte[] arrby = new byte[arrunsigned8.length];
        for (int i2 = 0; i2 < arrby.length; ++i2) {
            arrby[i2] = (byte)arrunsigned8[i2].get();
        }
        return arrby;
    }

    public static final List<byte[]> getDerChain(String string) {
        ArrayList arrayList = new ArrayList();
        String[] arrstring = Utils.splitPem(string);
        for (int i2 = 0; i2 < arrstring.length; ++i2) {
            arrayList.add((Object)Base64.decode((String)Utils.stripPem(arrstring[i2]), (int)0));
        }
        return arrayList;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static void setUnsigned8ArrayData(Struct.Unsigned8[] arrunsigned8, byte[] arrby) {
        if (arrby == null || arrby.length <= 0) return;
        {
            if (arrby.length > arrunsigned8.length) {
                c.e(TAG, "Error: Can't set a buffer more than the size of the destination Byte Array");
                return;
            } else {
                for (int i2 = 0; i2 < arrby.length; ++i2) {
                    arrunsigned8[i2].set(arrby[i2]);
                }
            }
        }
    }

    static String[] splitPem(String string) {
        String[] arrstring = string.split("-----BEGIN CERTIFICATE-----");
        String[] arrstring2 = new String[-1 + arrstring.length];
        for (int i2 = 1; i2 < arrstring.length; ++i2) {
            arrstring2[i2 - 1] = Utils.stripPem("-----BEGIN CERTIFICATE-----" + arrstring[i2]);
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

    public static final String[] toBase64(byte[][] arrby) {
        if (arrby == null || arrby.length == 0) {
            return null;
        }
        String[] arrstring = new String[arrby.length];
        for (int i2 = 0; i2 < arrby.length; ++i2) {
            arrstring[i2] = Base64.encodeToString((byte[])arrby[i2], (int)2);
        }
        return arrstring;
    }
}

