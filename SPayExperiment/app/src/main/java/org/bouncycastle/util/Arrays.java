/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuffer
 *  java.lang.System
 *  java.lang.UnsupportedOperationException
 *  java.math.BigInteger
 *  java.util.Iterator
 */
package org.bouncycastle.util;

import java.math.BigInteger;

public final class Arrays {
    private Arrays() {
    }

    public static byte[] append(byte[] arrby, byte by) {
        if (arrby == null) {
            return new byte[]{by};
        }
        int n = arrby.length;
        byte[] arrby2 = new byte[n + 1];
        System.arraycopy((Object)arrby, (int)0, (Object)arrby2, (int)0, (int)n);
        arrby2[n] = by;
        return arrby2;
    }

    public static int[] append(int[] arrn, int n) {
        if (arrn == null) {
            return new int[]{n};
        }
        int n2 = arrn.length;
        int[] arrn2 = new int[n2 + 1];
        System.arraycopy((Object)arrn, (int)0, (Object)arrn2, (int)0, (int)n2);
        arrn2[n2] = n;
        return arrn2;
    }

    public static short[] append(short[] arrs, short s) {
        if (arrs == null) {
            return new short[]{s};
        }
        int n = arrs.length;
        short[] arrs2 = new short[n + 1];
        System.arraycopy((Object)arrs, (int)0, (Object)arrs2, (int)0, (int)n);
        arrs2[n] = s;
        return arrs2;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static boolean areEqual(byte[] arrby, byte[] arrby2) {
        if (arrby == arrby2) {
            return true;
        }
        boolean bl = false;
        if (arrby == null) return bl;
        bl = false;
        if (arrby2 == null) return bl;
        int n = arrby.length;
        int n2 = arrby2.length;
        bl = false;
        if (n != n2) return bl;
        int n3 = 0;
        while (n3 != arrby.length) {
            byte by = arrby[n3];
            byte by2 = arrby2[n3];
            bl = false;
            if (by != by2) return bl;
            ++n3;
        }
        return true;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static boolean areEqual(char[] arrc, char[] arrc2) {
        if (arrc == arrc2) {
            return true;
        }
        boolean bl = false;
        if (arrc == null) return bl;
        bl = false;
        if (arrc2 == null) return bl;
        int n = arrc.length;
        int n2 = arrc2.length;
        bl = false;
        if (n != n2) return bl;
        int n3 = 0;
        while (n3 != arrc.length) {
            char c = arrc[n3];
            char c2 = arrc2[n3];
            bl = false;
            if (c != c2) return bl;
            ++n3;
        }
        return true;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static boolean areEqual(int[] arrn, int[] arrn2) {
        if (arrn == arrn2) {
            return true;
        }
        boolean bl = false;
        if (arrn == null) return bl;
        bl = false;
        if (arrn2 == null) return bl;
        int n = arrn.length;
        int n2 = arrn2.length;
        bl = false;
        if (n != n2) return bl;
        int n3 = 0;
        while (n3 != arrn.length) {
            int n4 = arrn[n3];
            int n5 = arrn2[n3];
            bl = false;
            if (n4 != n5) return bl;
            ++n3;
        }
        return true;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static boolean areEqual(long[] arrl, long[] arrl2) {
        if (arrl == arrl2) {
            return true;
        }
        boolean bl = false;
        if (arrl == null) return bl;
        bl = false;
        if (arrl2 == null) return bl;
        int n = arrl.length;
        int n2 = arrl2.length;
        bl = false;
        if (n != n2) return bl;
        int n3 = 0;
        while (n3 != arrl.length) {
            long l = arrl[n3] LCMP arrl2[n3];
            bl = false;
            if (l != false) return bl;
            ++n3;
        }
        return true;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static boolean areEqual(Object[] arrobject, Object[] arrobject2) {
        if (arrobject == arrobject2) {
            return true;
        }
        boolean bl = false;
        if (arrobject == null) return bl;
        bl = false;
        if (arrobject2 == null) return bl;
        int n = arrobject.length;
        int n2 = arrobject2.length;
        bl = false;
        if (n != n2) return bl;
        int n3 = 0;
        while (n3 != arrobject.length) {
            Object object = arrobject[n3];
            Object object2 = arrobject2[n3];
            if (object == null) {
                bl = false;
                if (object2 != null) return bl;
            } else if (!object.equals(object2)) {
                return false;
            }
            ++n3;
        }
        return true;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static boolean areEqual(boolean[] arrbl, boolean[] arrbl2) {
        if (arrbl == arrbl2) {
            return true;
        }
        boolean bl = false;
        if (arrbl == null) return bl;
        bl = false;
        if (arrbl2 == null) return bl;
        int n = arrbl.length;
        int n2 = arrbl2.length;
        bl = false;
        if (n != n2) return bl;
        int n3 = 0;
        while (n3 != arrbl.length) {
            boolean bl2 = arrbl[n3];
            boolean bl3 = arrbl2[n3];
            bl = false;
            if (bl2 != bl3) return bl;
            ++n3;
        }
        return true;
    }

    public static byte[] clone(byte[] arrby) {
        if (arrby == null) {
            return null;
        }
        byte[] arrby2 = new byte[arrby.length];
        System.arraycopy((Object)arrby, (int)0, (Object)arrby2, (int)0, (int)arrby.length);
        return arrby2;
    }

    public static byte[] clone(byte[] arrby, byte[] arrby2) {
        if (arrby == null) {
            return null;
        }
        if (arrby2 == null || arrby2.length != arrby.length) {
            return Arrays.clone(arrby);
        }
        System.arraycopy((Object)arrby, (int)0, (Object)arrby2, (int)0, (int)arrby2.length);
        return arrby2;
    }

    public static int[] clone(int[] arrn) {
        if (arrn == null) {
            return null;
        }
        int[] arrn2 = new int[arrn.length];
        System.arraycopy((Object)arrn, (int)0, (Object)arrn2, (int)0, (int)arrn.length);
        return arrn2;
    }

    public static long[] clone(long[] arrl) {
        if (arrl == null) {
            return null;
        }
        long[] arrl2 = new long[arrl.length];
        System.arraycopy((Object)arrl, (int)0, (Object)arrl2, (int)0, (int)arrl.length);
        return arrl2;
    }

    public static long[] clone(long[] arrl, long[] arrl2) {
        if (arrl == null) {
            return null;
        }
        if (arrl2 == null || arrl2.length != arrl.length) {
            return Arrays.clone(arrl);
        }
        System.arraycopy((Object)arrl, (int)0, (Object)arrl2, (int)0, (int)arrl2.length);
        return arrl2;
    }

    public static BigInteger[] clone(BigInteger[] arrbigInteger) {
        if (arrbigInteger == null) {
            return null;
        }
        BigInteger[] arrbigInteger2 = new BigInteger[arrbigInteger.length];
        System.arraycopy((Object)arrbigInteger, (int)0, (Object)arrbigInteger2, (int)0, (int)arrbigInteger.length);
        return arrbigInteger2;
    }

    public static short[] clone(short[] arrs) {
        if (arrs == null) {
            return null;
        }
        short[] arrs2 = new short[arrs.length];
        System.arraycopy((Object)arrs, (int)0, (Object)arrs2, (int)0, (int)arrs.length);
        return arrs2;
    }

    public static byte[][] clone(byte[][] arrby) {
        if (arrby == null) {
            return null;
        }
        byte[][] arrarrby = new byte[arrby.length][];
        for (int i = 0; i != arrarrby.length; ++i) {
            arrarrby[i] = Arrays.clone(arrby[i]);
        }
        return arrarrby;
    }

    public static byte[][][] clone(byte[][][] arrby) {
        if (arrby == null) {
            return null;
        }
        byte[][][] arrarrby = new byte[arrby.length][][];
        for (int i = 0; i != arrarrby.length; ++i) {
            arrarrby[i] = Arrays.clone(arrby[i]);
        }
        return arrarrby;
    }

    public static byte[] concatenate(byte[] arrby, byte[] arrby2) {
        if (arrby != null && arrby2 != null) {
            byte[] arrby3 = new byte[arrby.length + arrby2.length];
            System.arraycopy((Object)arrby, (int)0, (Object)arrby3, (int)0, (int)arrby.length);
            System.arraycopy((Object)arrby2, (int)0, (Object)arrby3, (int)arrby.length, (int)arrby2.length);
            return arrby3;
        }
        if (arrby2 != null) {
            return Arrays.clone(arrby2);
        }
        return Arrays.clone(arrby);
    }

    public static byte[] concatenate(byte[] arrby, byte[] arrby2, byte[] arrby3) {
        if (arrby != null && arrby2 != null && arrby3 != null) {
            byte[] arrby4 = new byte[arrby.length + arrby2.length + arrby3.length];
            System.arraycopy((Object)arrby, (int)0, (Object)arrby4, (int)0, (int)arrby.length);
            System.arraycopy((Object)arrby2, (int)0, (Object)arrby4, (int)arrby.length, (int)arrby2.length);
            System.arraycopy((Object)arrby3, (int)0, (Object)arrby4, (int)(arrby.length + arrby2.length), (int)arrby3.length);
            return arrby4;
        }
        if (arrby2 == null) {
            return Arrays.concatenate(arrby, arrby3);
        }
        return Arrays.concatenate(arrby, arrby2);
    }

    public static byte[] concatenate(byte[] arrby, byte[] arrby2, byte[] arrby3, byte[] arrby4) {
        if (arrby != null && arrby2 != null && arrby3 != null && arrby4 != null) {
            byte[] arrby5 = new byte[arrby.length + arrby2.length + arrby3.length + arrby4.length];
            System.arraycopy((Object)arrby, (int)0, (Object)arrby5, (int)0, (int)arrby.length);
            System.arraycopy((Object)arrby2, (int)0, (Object)arrby5, (int)arrby.length, (int)arrby2.length);
            System.arraycopy((Object)arrby3, (int)0, (Object)arrby5, (int)(arrby.length + arrby2.length), (int)arrby3.length);
            System.arraycopy((Object)arrby4, (int)0, (Object)arrby5, (int)(arrby.length + arrby2.length + arrby3.length), (int)arrby4.length);
            return arrby5;
        }
        if (arrby4 == null) {
            return Arrays.concatenate(arrby, arrby2, arrby3);
        }
        if (arrby3 == null) {
            return Arrays.concatenate(arrby, arrby2, arrby4);
        }
        if (arrby2 == null) {
            return Arrays.concatenate(arrby, arrby3, arrby4);
        }
        return Arrays.concatenate(arrby2, arrby3, arrby4);
    }

    public static int[] concatenate(int[] arrn, int[] arrn2) {
        if (arrn == null) {
            return Arrays.clone(arrn2);
        }
        if (arrn2 == null) {
            return Arrays.clone(arrn);
        }
        int[] arrn3 = new int[arrn.length + arrn2.length];
        System.arraycopy((Object)arrn, (int)0, (Object)arrn3, (int)0, (int)arrn.length);
        System.arraycopy((Object)arrn2, (int)0, (Object)arrn3, (int)arrn.length, (int)arrn2.length);
        return arrn3;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static boolean constantTimeAreEqual(byte[] arrby, byte[] arrby2) {
        if (arrby == arrby2) {
            return true;
        }
        boolean bl = false;
        if (arrby == null) return bl;
        bl = false;
        if (arrby2 == null) return bl;
        int n = arrby.length;
        int n2 = arrby2.length;
        bl = false;
        if (n != n2) return bl;
        int n3 = 0;
        for (int i = 0; i != arrby.length; ++i) {
            n3 |= arrby[i] ^ arrby2[i];
        }
        bl = false;
        if (n3 != 0) return bl;
        return true;
    }

    public static boolean contains(int[] arrn, int n) {
        int n2 = 0;
        do {
            block4 : {
                boolean bl;
                block3 : {
                    int n3 = arrn.length;
                    bl = false;
                    if (n2 >= n3) break block3;
                    if (arrn[n2] != n) break block4;
                    bl = true;
                }
                return bl;
            }
            ++n2;
        } while (true);
    }

    public static boolean contains(short[] arrs, short s) {
        int n = 0;
        do {
            block4 : {
                boolean bl;
                block3 : {
                    int n2 = arrs.length;
                    bl = false;
                    if (n >= n2) break block3;
                    if (arrs[n] != s) break block4;
                    bl = true;
                }
                return bl;
            }
            ++n;
        } while (true);
    }

    public static byte[] copyOf(byte[] arrby, int n) {
        byte[] arrby2 = new byte[n];
        if (n < arrby.length) {
            System.arraycopy((Object)arrby, (int)0, (Object)arrby2, (int)0, (int)n);
            return arrby2;
        }
        System.arraycopy((Object)arrby, (int)0, (Object)arrby2, (int)0, (int)arrby.length);
        return arrby2;
    }

    public static char[] copyOf(char[] arrc, int n) {
        char[] arrc2 = new char[n];
        if (n < arrc.length) {
            System.arraycopy((Object)arrc, (int)0, (Object)arrc2, (int)0, (int)n);
            return arrc2;
        }
        System.arraycopy((Object)arrc, (int)0, (Object)arrc2, (int)0, (int)arrc.length);
        return arrc2;
    }

    public static int[] copyOf(int[] arrn, int n) {
        int[] arrn2 = new int[n];
        if (n < arrn.length) {
            System.arraycopy((Object)arrn, (int)0, (Object)arrn2, (int)0, (int)n);
            return arrn2;
        }
        System.arraycopy((Object)arrn, (int)0, (Object)arrn2, (int)0, (int)arrn.length);
        return arrn2;
    }

    public static long[] copyOf(long[] arrl, int n) {
        long[] arrl2 = new long[n];
        if (n < arrl.length) {
            System.arraycopy((Object)arrl, (int)0, (Object)arrl2, (int)0, (int)n);
            return arrl2;
        }
        System.arraycopy((Object)arrl, (int)0, (Object)arrl2, (int)0, (int)arrl.length);
        return arrl2;
    }

    public static BigInteger[] copyOf(BigInteger[] arrbigInteger, int n) {
        BigInteger[] arrbigInteger2 = new BigInteger[n];
        if (n < arrbigInteger.length) {
            System.arraycopy((Object)arrbigInteger, (int)0, (Object)arrbigInteger2, (int)0, (int)n);
            return arrbigInteger2;
        }
        System.arraycopy((Object)arrbigInteger, (int)0, (Object)arrbigInteger2, (int)0, (int)arrbigInteger.length);
        return arrbigInteger2;
    }

    public static byte[] copyOfRange(byte[] arrby, int n, int n2) {
        int n3 = Arrays.getLength(n, n2);
        byte[] arrby2 = new byte[n3];
        if (arrby.length - n < n3) {
            System.arraycopy((Object)arrby, (int)n, (Object)arrby2, (int)0, (int)(arrby.length - n));
            return arrby2;
        }
        System.arraycopy((Object)arrby, (int)n, (Object)arrby2, (int)0, (int)n3);
        return arrby2;
    }

    public static int[] copyOfRange(int[] arrn, int n, int n2) {
        int n3 = Arrays.getLength(n, n2);
        int[] arrn2 = new int[n3];
        if (arrn.length - n < n3) {
            System.arraycopy((Object)arrn, (int)n, (Object)arrn2, (int)0, (int)(arrn.length - n));
            return arrn2;
        }
        System.arraycopy((Object)arrn, (int)n, (Object)arrn2, (int)0, (int)n3);
        return arrn2;
    }

    public static long[] copyOfRange(long[] arrl, int n, int n2) {
        int n3 = Arrays.getLength(n, n2);
        long[] arrl2 = new long[n3];
        if (arrl.length - n < n3) {
            System.arraycopy((Object)arrl, (int)n, (Object)arrl2, (int)0, (int)(arrl.length - n));
            return arrl2;
        }
        System.arraycopy((Object)arrl, (int)n, (Object)arrl2, (int)0, (int)n3);
        return arrl2;
    }

    public static BigInteger[] copyOfRange(BigInteger[] arrbigInteger, int n, int n2) {
        int n3 = Arrays.getLength(n, n2);
        BigInteger[] arrbigInteger2 = new BigInteger[n3];
        if (arrbigInteger.length - n < n3) {
            System.arraycopy((Object)arrbigInteger, (int)n, (Object)arrbigInteger2, (int)0, (int)(arrbigInteger.length - n));
            return arrbigInteger2;
        }
        System.arraycopy((Object)arrbigInteger, (int)n, (Object)arrbigInteger2, (int)0, (int)n3);
        return arrbigInteger2;
    }

    public static void fill(byte[] arrby, byte by) {
        for (int i = 0; i < arrby.length; ++i) {
            arrby[i] = by;
        }
    }

    public static void fill(char[] arrc, char c) {
        for (int i = 0; i < arrc.length; ++i) {
            arrc[i] = c;
        }
    }

    public static void fill(int[] arrn, int n) {
        for (int i = 0; i < arrn.length; ++i) {
            arrn[i] = n;
        }
    }

    public static void fill(long[] arrl, long l) {
        for (int i = 0; i < arrl.length; ++i) {
            arrl[i] = l;
        }
    }

    public static void fill(short[] arrs, short s) {
        for (int i = 0; i < arrs.length; ++i) {
            arrs[i] = s;
        }
    }

    private static int getLength(int n, int n2) {
        int n3 = n2 - n;
        if (n3 < 0) {
            StringBuffer stringBuffer = new StringBuffer(n);
            stringBuffer.append(" > ").append(n2);
            throw new IllegalArgumentException(stringBuffer.toString());
        }
        return n3;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static int hashCode(byte[] arrby) {
        if (arrby == null) {
            return 0;
        }
        int n = arrby.length;
        int n2 = n + 1;
        while (--n >= 0) {
            n2 = n2 * 257 ^ arrby[n];
        }
        return n2;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static int hashCode(byte[] arrby, int n, int n2) {
        if (arrby == null) {
            return 0;
        }
        int n3 = n2 + 1;
        while (--n2 >= 0) {
            n3 = n3 * 257 ^ arrby[n + n2];
        }
        return n3;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static int hashCode(char[] arrc) {
        if (arrc == null) {
            return 0;
        }
        int n = arrc.length;
        int n2 = n + 1;
        while (--n >= 0) {
            n2 = n2 * 257 ^ arrc[n];
        }
        return n2;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static int hashCode(int[] arrn) {
        if (arrn == null) {
            return 0;
        }
        int n = arrn.length;
        int n2 = n + 1;
        while (--n >= 0) {
            n2 = n2 * 257 ^ arrn[n];
        }
        return n2;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static int hashCode(int[] arrn, int n, int n2) {
        if (arrn == null) {
            return 0;
        }
        int n3 = n2 + 1;
        while (--n2 >= 0) {
            n3 = n3 * 257 ^ arrn[n + n2];
        }
        return n3;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static int hashCode(Object[] arrobject) {
        if (arrobject == null) {
            return 0;
        }
        int n = arrobject.length;
        int n2 = n + 1;
        while (--n >= 0) {
            n2 = n2 * 257 ^ arrobject[n].hashCode();
        }
        return n2;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static int hashCode(short[] arrs) {
        if (arrs == null) {
            return 0;
        }
        int n = arrs.length;
        int n2 = n + 1;
        while (--n >= 0) {
            n2 = n2 * 257 ^ 255 & arrs[n];
        }
        return n2;
    }

    public static int hashCode(int[][] arrn) {
        int n = 0;
        for (int i = 0; i != arrn.length; ++i) {
            n = n * 257 + Arrays.hashCode(arrn[i]);
        }
        return n;
    }

    public static int hashCode(short[][] arrs) {
        int n = 0;
        for (int i = 0; i != arrs.length; ++i) {
            n = n * 257 + Arrays.hashCode(arrs[i]);
        }
        return n;
    }

    public static int hashCode(short[][][] arrs) {
        int n = 0;
        for (int i = 0; i != arrs.length; ++i) {
            n = n * 257 + Arrays.hashCode(arrs[i]);
        }
        return n;
    }

    public static byte[] prepend(byte[] arrby, byte by) {
        if (arrby == null) {
            return new byte[]{by};
        }
        int n = arrby.length;
        byte[] arrby2 = new byte[n + 1];
        System.arraycopy((Object)arrby, (int)0, (Object)arrby2, (int)1, (int)n);
        arrby2[0] = by;
        return arrby2;
    }

    public static int[] prepend(int[] arrn, int n) {
        if (arrn == null) {
            return new int[]{n};
        }
        int n2 = arrn.length;
        int[] arrn2 = new int[n2 + 1];
        System.arraycopy((Object)arrn, (int)0, (Object)arrn2, (int)1, (int)n2);
        arrn2[0] = n;
        return arrn2;
    }

    public static short[] prepend(short[] arrs, short s) {
        if (arrs == null) {
            return new short[]{s};
        }
        int n = arrs.length;
        short[] arrs2 = new short[n + 1];
        System.arraycopy((Object)arrs, (int)0, (Object)arrs2, (int)1, (int)n);
        arrs2[0] = s;
        return arrs2;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static byte[] reverse(byte[] arrby) {
        if (arrby == null) {
            return null;
        }
        int n = 0;
        int n2 = arrby.length;
        byte[] arrby2 = new byte[n2];
        while (--n2 >= 0) {
            int n3 = n + 1;
            arrby2[n2] = arrby[n];
            n = n3;
        }
        return arrby2;
    }

    public static class Iterator<T>
    implements java.util.Iterator<T> {
        private final T[] dataArray;
        private int position = 0;

        public Iterator(T[] arrT) {
            this.dataArray = arrT;
        }

        public boolean hasNext() {
            return this.position < this.dataArray.length;
        }

        public T next() {
            T[] arrT = this.dataArray;
            int n = this.position;
            this.position = n + 1;
            return arrT[n];
        }

        public void remove() {
            throw new UnsupportedOperationException("Cannot remove element from an Array.");
        }
    }

}

