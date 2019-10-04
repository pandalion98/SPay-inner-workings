/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.math.BigInteger
 */
package org.bouncycastle.pqc.math.linearalgebra;

import java.math.BigInteger;
import org.bouncycastle.pqc.math.linearalgebra.BigEndianConversions;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

public final class IntUtils {
    private IntUtils() {
    }

    public static int[] clone(int[] arrn) {
        int[] arrn2 = new int[arrn.length];
        System.arraycopy((Object)arrn, (int)0, (Object)arrn2, (int)0, (int)arrn.length);
        return arrn2;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static boolean equals(int[] arrn, int[] arrn2) {
        if (arrn.length != arrn2.length) {
            return false;
        }
        int n = -1 + arrn.length;
        boolean bl = true;
        while (n >= 0) {
            boolean bl2 = arrn[n] == arrn2[n];
            bl &= bl2;
            --n;
        }
        return bl;
    }

    public static void fill(int[] arrn, int n) {
        for (int i = -1 + arrn.length; i >= 0; --i) {
            arrn[i] = n;
        }
    }

    private static int partition(int[] arrn, int n, int n2, int n3) {
        int n4 = arrn[n3];
        arrn[n3] = arrn[n2];
        arrn[n2] = n4;
        int n5 = n;
        while (n < n2) {
            if (arrn[n] <= n4) {
                int n6 = arrn[n5];
                arrn[n5] = arrn[n];
                arrn[n] = n6;
                ++n5;
            }
            ++n;
        }
        int n7 = arrn[n5];
        arrn[n5] = arrn[n2];
        arrn[n2] = n7;
        return n5;
    }

    public static void quicksort(int[] arrn) {
        IntUtils.quicksort(arrn, 0, -1 + arrn.length);
    }

    public static void quicksort(int[] arrn, int n, int n2) {
        if (n2 > n) {
            int n3 = IntUtils.partition(arrn, n, n2, n2);
            IntUtils.quicksort(arrn, n, n3 - 1);
            IntUtils.quicksort(arrn, n3 + 1, n2);
        }
    }

    public static int[] subArray(int[] arrn, int n, int n2) {
        int[] arrn2 = new int[n2 - n];
        System.arraycopy((Object)arrn, (int)n, (Object)arrn2, (int)0, (int)(n2 - n));
        return arrn2;
    }

    public static BigInteger[] toFlexiBigIntArray(int[] arrn) {
        BigInteger[] arrbigInteger = new BigInteger[arrn.length];
        for (int i = 0; i < arrn.length; ++i) {
            arrbigInteger[i] = BigInteger.valueOf((long)arrn[i]);
        }
        return arrbigInteger;
    }

    public static String toHexString(int[] arrn) {
        return ByteUtils.toHexString(BigEndianConversions.toByteArray(arrn));
    }

    public static String toString(int[] arrn) {
        String string = "";
        for (int i = 0; i < arrn.length; ++i) {
            string = string + arrn[i] + " ";
        }
        return string;
    }
}

