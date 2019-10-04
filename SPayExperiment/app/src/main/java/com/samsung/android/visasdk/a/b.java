/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Character
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.lang.System
 *  java.util.Arrays
 */
package com.samsung.android.visasdk.a;

import com.samsung.android.visasdk.a.c;
import com.samsung.android.visasdk.c.a;
import java.util.Arrays;

public class b {
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    public static byte a(byte by, int n2, int n3) {
        if (n2 < 1 || n2 > 8 || n3 < 0 || n3 > 1) {
            a.e("Utils", "parameter 'bitPos' must be between 1 and 8. value must be 0 or 1");
        }
        if (n3 == 1) {
            return (byte)(by | 1 << n2 - 1);
        }
        return (byte)(by & (-1 ^ 1 << n2 - 1));
    }

    /*
     * Enabled aggressive block sorting
     */
    public static void a(byte[] arrby, int n2, byte by, int n3) {
        if (arrby != null && n2 < arrby.length) {
            int n4 = arrby.length < n2 + n3 ? arrby.length : n2 + n3;
            while (n2 < n4) {
                arrby[n2] = by;
                ++n2;
            }
        }
    }

    public static void a(byte[] arrby, int n2, byte[] arrby2, int n3, int n4) {
        if (arrby == null || arrby2 == null) {
            return;
        }
        System.arraycopy((Object)arrby, (int)n2, (Object)arrby2, (int)n3, (int)n4);
    }

    public static String bF(String string) {
        if (string == null) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder("");
        int n2 = 0;
        int n3;
        while (n2 < string.length() && (n3 = Integer.parseInt((String)string.substring(n2, n2 + 2), (int)16)) != 0) {
            stringBuilder.append((char)n3);
            n2 += 2;
        }
        return stringBuilder.toString();
    }

    public static byte[] d(byte[] arrby, int n2, int n3) {
        byte[] arrby2 = new byte[n3];
        b.a(arrby, n2, arrby2, 0, n3);
        return arrby2;
    }

    public static void e(String string, byte[] arrby) {
        if (c.LOG_DEBUG) {
            a.d("Utils", string + b.o(arrby));
        }
    }

    public static long fU() {
        return System.currentTimeMillis() / 1000L;
    }

    public static boolean g(byte[] arrby, byte[] arrby2) {
        return Arrays.equals((byte[])arrby, (byte[])arrby2);
    }

    public static final short getShort(byte[] arrby, int n2) {
        return (short)(((short)arrby[n2] << 8) + (255 & (short)arrby[(short)(n2 + 1)]));
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static byte[] hexStringToBytes(String string) {
        if (string == null) {
            return null;
        }
        int n2 = string.length();
        byte[] arrby = new byte[n2 / 2];
        int n3 = 0;
        while (n3 < n2) {
            arrby[n3 / 2] = (byte)(Character.digit((char)string.charAt(n3), (int)16) << 4 | Character.digit((char)string.charAt(n3 + 1), (int)16));
            n3 += 2;
        }
        return arrby;
    }

    public static boolean isBitSet(byte by, int n2) {
        if (n2 < 1 || n2 > 8) {
            a.e("Utils", "parameter 'bitPos' must be between 1 and 8");
        }
        return (by & 1 << n2 - 1) != 0;
    }

    public static String o(byte[] arrby) {
        if (arrby == null) {
            return null;
        }
        char[] arrc = new char[2 * arrby.length];
        for (int i2 = 0; i2 < arrby.length; ++i2) {
            int n2 = 255 & arrby[i2];
            arrc[i2 * 2] = HEX_ARRAY[n2 >>> 4];
            arrc[1 + i2 * 2] = HEX_ARRAY[n2 & 15];
        }
        return new String(arrc);
    }

    /*
     * Enabled aggressive block sorting
     */
    public static void p(byte[] arrby) {
        if (arrby != null) {
            for (int i2 = 0; i2 < arrby.length; ++i2) {
                arrby[i2] = 0;
            }
        }
    }
}

