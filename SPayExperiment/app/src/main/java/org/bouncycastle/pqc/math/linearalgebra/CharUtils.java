/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.System
 */
package org.bouncycastle.pqc.math.linearalgebra;

public final class CharUtils {
    private CharUtils() {
    }

    public static char[] clone(char[] arrc) {
        char[] arrc2 = new char[arrc.length];
        System.arraycopy((Object)arrc, (int)0, (Object)arrc2, (int)0, (int)arrc.length);
        return arrc2;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static boolean equals(char[] arrc, char[] arrc2) {
        if (arrc.length != arrc2.length) {
            return false;
        }
        int n = -1 + arrc.length;
        boolean bl = true;
        while (n >= 0) {
            boolean bl2 = arrc[n] == arrc2[n];
            bl &= bl2;
            --n;
        }
        return bl;
    }

    public static byte[] toByteArray(char[] arrc) {
        byte[] arrby = new byte[arrc.length];
        for (int i = -1 + arrc.length; i >= 0; --i) {
            arrby[i] = (byte)arrc[i];
        }
        return arrby;
    }

    public static byte[] toByteArrayForPBE(char[] arrc) {
        byte[] arrby = new byte[arrc.length];
        for (int i = 0; i < arrc.length; ++i) {
            arrby[i] = (byte)arrc[i];
        }
        int n = 2 * arrby.length;
        byte[] arrby2 = new byte[n + 2];
        for (int i = 0; i < arrby.length; ++i) {
            int n2 = i * 2;
            arrby2[n2] = 0;
            arrby2[n2 + 1] = arrby[i];
        }
        arrby2[n] = 0;
        arrby2[n + 1] = 0;
        return arrby2;
    }
}

