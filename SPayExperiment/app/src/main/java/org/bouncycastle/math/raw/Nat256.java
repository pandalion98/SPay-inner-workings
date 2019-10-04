/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.math.BigInteger
 */
package org.bouncycastle.math.raw;

import java.math.BigInteger;
import org.bouncycastle.math.raw.Nat;
import org.bouncycastle.util.Pack;

public abstract class Nat256 {
    private static final long M = 0xFFFFFFFFL;

    public static int add(int[] arrn, int n, int[] arrn2, int n2, int[] arrn3, int n3) {
        long l = 0L + ((0xFFFFFFFFL & (long)arrn[n + 0]) + (0xFFFFFFFFL & (long)arrn2[n2 + 0]));
        arrn3[n3 + 0] = (int)l;
        long l2 = (l >>> 32) + ((0xFFFFFFFFL & (long)arrn[n + 1]) + (0xFFFFFFFFL & (long)arrn2[n2 + 1]));
        arrn3[n3 + 1] = (int)l2;
        long l3 = (l2 >>> 32) + ((0xFFFFFFFFL & (long)arrn[n + 2]) + (0xFFFFFFFFL & (long)arrn2[n2 + 2]));
        arrn3[n3 + 2] = (int)l3;
        long l4 = (l3 >>> 32) + ((0xFFFFFFFFL & (long)arrn[n + 3]) + (0xFFFFFFFFL & (long)arrn2[n2 + 3]));
        arrn3[n3 + 3] = (int)l4;
        long l5 = (l4 >>> 32) + ((0xFFFFFFFFL & (long)arrn[n + 4]) + (0xFFFFFFFFL & (long)arrn2[n2 + 4]));
        arrn3[n3 + 4] = (int)l5;
        long l6 = (l5 >>> 32) + ((0xFFFFFFFFL & (long)arrn[n + 5]) + (0xFFFFFFFFL & (long)arrn2[n2 + 5]));
        arrn3[n3 + 5] = (int)l6;
        long l7 = (l6 >>> 32) + ((0xFFFFFFFFL & (long)arrn[n + 6]) + (0xFFFFFFFFL & (long)arrn2[n2 + 6]));
        arrn3[n3 + 6] = (int)l7;
        long l8 = (l7 >>> 32) + ((0xFFFFFFFFL & (long)arrn[n + 7]) + (0xFFFFFFFFL & (long)arrn2[n2 + 7]));
        arrn3[n3 + 7] = (int)l8;
        return (int)(l8 >>> 32);
    }

    public static int add(int[] arrn, int[] arrn2, int[] arrn3) {
        long l = 0L + ((0xFFFFFFFFL & (long)arrn[0]) + (0xFFFFFFFFL & (long)arrn2[0]));
        arrn3[0] = (int)l;
        long l2 = (l >>> 32) + ((0xFFFFFFFFL & (long)arrn[1]) + (0xFFFFFFFFL & (long)arrn2[1]));
        arrn3[1] = (int)l2;
        long l3 = (l2 >>> 32) + ((0xFFFFFFFFL & (long)arrn[2]) + (0xFFFFFFFFL & (long)arrn2[2]));
        arrn3[2] = (int)l3;
        long l4 = (l3 >>> 32) + ((0xFFFFFFFFL & (long)arrn[3]) + (0xFFFFFFFFL & (long)arrn2[3]));
        arrn3[3] = (int)l4;
        long l5 = (l4 >>> 32) + ((0xFFFFFFFFL & (long)arrn[4]) + (0xFFFFFFFFL & (long)arrn2[4]));
        arrn3[4] = (int)l5;
        long l6 = (l5 >>> 32) + ((0xFFFFFFFFL & (long)arrn[5]) + (0xFFFFFFFFL & (long)arrn2[5]));
        arrn3[5] = (int)l6;
        long l7 = (l6 >>> 32) + ((0xFFFFFFFFL & (long)arrn[6]) + (0xFFFFFFFFL & (long)arrn2[6]));
        arrn3[6] = (int)l7;
        long l8 = (l7 >>> 32) + ((0xFFFFFFFFL & (long)arrn[7]) + (0xFFFFFFFFL & (long)arrn2[7]));
        arrn3[7] = (int)l8;
        return (int)(l8 >>> 32);
    }

    public static int addBothTo(int[] arrn, int n, int[] arrn2, int n2, int[] arrn3, int n3) {
        long l = 0L + ((0xFFFFFFFFL & (long)arrn[n + 0]) + (0xFFFFFFFFL & (long)arrn2[n2 + 0]) + (0xFFFFFFFFL & (long)arrn3[n3 + 0]));
        arrn3[n3 + 0] = (int)l;
        long l2 = (l >>> 32) + ((0xFFFFFFFFL & (long)arrn[n + 1]) + (0xFFFFFFFFL & (long)arrn2[n2 + 1]) + (0xFFFFFFFFL & (long)arrn3[n3 + 1]));
        arrn3[n3 + 1] = (int)l2;
        long l3 = (l2 >>> 32) + ((0xFFFFFFFFL & (long)arrn[n + 2]) + (0xFFFFFFFFL & (long)arrn2[n2 + 2]) + (0xFFFFFFFFL & (long)arrn3[n3 + 2]));
        arrn3[n3 + 2] = (int)l3;
        long l4 = (l3 >>> 32) + ((0xFFFFFFFFL & (long)arrn[n + 3]) + (0xFFFFFFFFL & (long)arrn2[n2 + 3]) + (0xFFFFFFFFL & (long)arrn3[n3 + 3]));
        arrn3[n3 + 3] = (int)l4;
        long l5 = (l4 >>> 32) + ((0xFFFFFFFFL & (long)arrn[n + 4]) + (0xFFFFFFFFL & (long)arrn2[n2 + 4]) + (0xFFFFFFFFL & (long)arrn3[n3 + 4]));
        arrn3[n3 + 4] = (int)l5;
        long l6 = (l5 >>> 32) + ((0xFFFFFFFFL & (long)arrn[n + 5]) + (0xFFFFFFFFL & (long)arrn2[n2 + 5]) + (0xFFFFFFFFL & (long)arrn3[n3 + 5]));
        arrn3[n3 + 5] = (int)l6;
        long l7 = (l6 >>> 32) + ((0xFFFFFFFFL & (long)arrn[n + 6]) + (0xFFFFFFFFL & (long)arrn2[n2 + 6]) + (0xFFFFFFFFL & (long)arrn3[n3 + 6]));
        arrn3[n3 + 6] = (int)l7;
        long l8 = (l7 >>> 32) + ((0xFFFFFFFFL & (long)arrn[n + 7]) + (0xFFFFFFFFL & (long)arrn2[n2 + 7]) + (0xFFFFFFFFL & (long)arrn3[n3 + 7]));
        arrn3[n3 + 7] = (int)l8;
        return (int)(l8 >>> 32);
    }

    public static int addBothTo(int[] arrn, int[] arrn2, int[] arrn3) {
        long l = 0L + ((0xFFFFFFFFL & (long)arrn[0]) + (0xFFFFFFFFL & (long)arrn2[0]) + (0xFFFFFFFFL & (long)arrn3[0]));
        arrn3[0] = (int)l;
        long l2 = (l >>> 32) + ((0xFFFFFFFFL & (long)arrn[1]) + (0xFFFFFFFFL & (long)arrn2[1]) + (0xFFFFFFFFL & (long)arrn3[1]));
        arrn3[1] = (int)l2;
        long l3 = (l2 >>> 32) + ((0xFFFFFFFFL & (long)arrn[2]) + (0xFFFFFFFFL & (long)arrn2[2]) + (0xFFFFFFFFL & (long)arrn3[2]));
        arrn3[2] = (int)l3;
        long l4 = (l3 >>> 32) + ((0xFFFFFFFFL & (long)arrn[3]) + (0xFFFFFFFFL & (long)arrn2[3]) + (0xFFFFFFFFL & (long)arrn3[3]));
        arrn3[3] = (int)l4;
        long l5 = (l4 >>> 32) + ((0xFFFFFFFFL & (long)arrn[4]) + (0xFFFFFFFFL & (long)arrn2[4]) + (0xFFFFFFFFL & (long)arrn3[4]));
        arrn3[4] = (int)l5;
        long l6 = (l5 >>> 32) + ((0xFFFFFFFFL & (long)arrn[5]) + (0xFFFFFFFFL & (long)arrn2[5]) + (0xFFFFFFFFL & (long)arrn3[5]));
        arrn3[5] = (int)l6;
        long l7 = (l6 >>> 32) + ((0xFFFFFFFFL & (long)arrn[6]) + (0xFFFFFFFFL & (long)arrn2[6]) + (0xFFFFFFFFL & (long)arrn3[6]));
        arrn3[6] = (int)l7;
        long l8 = (l7 >>> 32) + ((0xFFFFFFFFL & (long)arrn[7]) + (0xFFFFFFFFL & (long)arrn2[7]) + (0xFFFFFFFFL & (long)arrn3[7]));
        arrn3[7] = (int)l8;
        return (int)(l8 >>> 32);
    }

    public static int addTo(int[] arrn, int n, int[] arrn2, int n2, int n3) {
        long l = (0xFFFFFFFFL & (long)n3) + ((0xFFFFFFFFL & (long)arrn[n + 0]) + (0xFFFFFFFFL & (long)arrn2[n2 + 0]));
        arrn2[n2 + 0] = (int)l;
        long l2 = (l >>> 32) + ((0xFFFFFFFFL & (long)arrn[n + 1]) + (0xFFFFFFFFL & (long)arrn2[n2 + 1]));
        arrn2[n2 + 1] = (int)l2;
        long l3 = (l2 >>> 32) + ((0xFFFFFFFFL & (long)arrn[n + 2]) + (0xFFFFFFFFL & (long)arrn2[n2 + 2]));
        arrn2[n2 + 2] = (int)l3;
        long l4 = (l3 >>> 32) + ((0xFFFFFFFFL & (long)arrn[n + 3]) + (0xFFFFFFFFL & (long)arrn2[n2 + 3]));
        arrn2[n2 + 3] = (int)l4;
        long l5 = (l4 >>> 32) + ((0xFFFFFFFFL & (long)arrn[n + 4]) + (0xFFFFFFFFL & (long)arrn2[n2 + 4]));
        arrn2[n2 + 4] = (int)l5;
        long l6 = (l5 >>> 32) + ((0xFFFFFFFFL & (long)arrn[n + 5]) + (0xFFFFFFFFL & (long)arrn2[n2 + 5]));
        arrn2[n2 + 5] = (int)l6;
        long l7 = (l6 >>> 32) + ((0xFFFFFFFFL & (long)arrn[n + 6]) + (0xFFFFFFFFL & (long)arrn2[n2 + 6]));
        arrn2[n2 + 6] = (int)l7;
        long l8 = (l7 >>> 32) + ((0xFFFFFFFFL & (long)arrn[n + 7]) + (0xFFFFFFFFL & (long)arrn2[n2 + 7]));
        arrn2[n2 + 7] = (int)l8;
        return (int)(l8 >>> 32);
    }

    public static int addTo(int[] arrn, int[] arrn2) {
        long l = 0L + ((0xFFFFFFFFL & (long)arrn[0]) + (0xFFFFFFFFL & (long)arrn2[0]));
        arrn2[0] = (int)l;
        long l2 = (l >>> 32) + ((0xFFFFFFFFL & (long)arrn[1]) + (0xFFFFFFFFL & (long)arrn2[1]));
        arrn2[1] = (int)l2;
        long l3 = (l2 >>> 32) + ((0xFFFFFFFFL & (long)arrn[2]) + (0xFFFFFFFFL & (long)arrn2[2]));
        arrn2[2] = (int)l3;
        long l4 = (l3 >>> 32) + ((0xFFFFFFFFL & (long)arrn[3]) + (0xFFFFFFFFL & (long)arrn2[3]));
        arrn2[3] = (int)l4;
        long l5 = (l4 >>> 32) + ((0xFFFFFFFFL & (long)arrn[4]) + (0xFFFFFFFFL & (long)arrn2[4]));
        arrn2[4] = (int)l5;
        long l6 = (l5 >>> 32) + ((0xFFFFFFFFL & (long)arrn[5]) + (0xFFFFFFFFL & (long)arrn2[5]));
        arrn2[5] = (int)l6;
        long l7 = (l6 >>> 32) + ((0xFFFFFFFFL & (long)arrn[6]) + (0xFFFFFFFFL & (long)arrn2[6]));
        arrn2[6] = (int)l7;
        long l8 = (l7 >>> 32) + ((0xFFFFFFFFL & (long)arrn[7]) + (0xFFFFFFFFL & (long)arrn2[7]));
        arrn2[7] = (int)l8;
        return (int)(l8 >>> 32);
    }

    public static int addToEachOther(int[] arrn, int n, int[] arrn2, int n2) {
        long l = 0L + ((0xFFFFFFFFL & (long)arrn[n + 0]) + (0xFFFFFFFFL & (long)arrn2[n2 + 0]));
        arrn[n + 0] = (int)l;
        arrn2[n2 + 0] = (int)l;
        long l2 = (l >>> 32) + ((0xFFFFFFFFL & (long)arrn[n + 1]) + (0xFFFFFFFFL & (long)arrn2[n2 + 1]));
        arrn[n + 1] = (int)l2;
        arrn2[n2 + 1] = (int)l2;
        long l3 = (l2 >>> 32) + ((0xFFFFFFFFL & (long)arrn[n + 2]) + (0xFFFFFFFFL & (long)arrn2[n2 + 2]));
        arrn[n + 2] = (int)l3;
        arrn2[n2 + 2] = (int)l3;
        long l4 = (l3 >>> 32) + ((0xFFFFFFFFL & (long)arrn[n + 3]) + (0xFFFFFFFFL & (long)arrn2[n2 + 3]));
        arrn[n + 3] = (int)l4;
        arrn2[n2 + 3] = (int)l4;
        long l5 = (l4 >>> 32) + ((0xFFFFFFFFL & (long)arrn[n + 4]) + (0xFFFFFFFFL & (long)arrn2[n2 + 4]));
        arrn[n + 4] = (int)l5;
        arrn2[n2 + 4] = (int)l5;
        long l6 = (l5 >>> 32) + ((0xFFFFFFFFL & (long)arrn[n + 5]) + (0xFFFFFFFFL & (long)arrn2[n2 + 5]));
        arrn[n + 5] = (int)l6;
        arrn2[n2 + 5] = (int)l6;
        long l7 = (l6 >>> 32) + ((0xFFFFFFFFL & (long)arrn[n + 6]) + (0xFFFFFFFFL & (long)arrn2[n2 + 6]));
        arrn[n + 6] = (int)l7;
        arrn2[n2 + 6] = (int)l7;
        long l8 = (l7 >>> 32) + ((0xFFFFFFFFL & (long)arrn[n + 7]) + (0xFFFFFFFFL & (long)arrn2[n2 + 7]));
        arrn[n + 7] = (int)l8;
        arrn2[n2 + 7] = (int)l8;
        return (int)(l8 >>> 32);
    }

    public static void copy(int[] arrn, int[] arrn2) {
        arrn2[0] = arrn[0];
        arrn2[1] = arrn[1];
        arrn2[2] = arrn[2];
        arrn2[3] = arrn[3];
        arrn2[4] = arrn[4];
        arrn2[5] = arrn[5];
        arrn2[6] = arrn[6];
        arrn2[7] = arrn[7];
    }

    public static int[] create() {
        return new int[8];
    }

    public static int[] createExt() {
        return new int[16];
    }

    public static boolean diff(int[] arrn, int n, int[] arrn2, int n2, int[] arrn3, int n3) {
        boolean bl = Nat256.gte(arrn, n, arrn2, n2);
        if (bl) {
            Nat256.sub(arrn, n, arrn2, n2, arrn3, n3);
            return bl;
        }
        Nat256.sub(arrn2, n2, arrn, n, arrn3, n3);
        return bl;
    }

    public static boolean eq(int[] arrn, int[] arrn2) {
        for (int i = 7; i >= 0; --i) {
            if (arrn[i] == arrn2[i]) continue;
            return false;
        }
        return true;
    }

    public static int[] fromBigInteger(BigInteger bigInteger) {
        if (bigInteger.signum() < 0 || bigInteger.bitLength() > 256) {
            throw new IllegalArgumentException();
        }
        int[] arrn = Nat256.create();
        int n = 0;
        while (bigInteger.signum() != 0) {
            int n2 = n + 1;
            arrn[n] = bigInteger.intValue();
            bigInteger = bigInteger.shiftRight(32);
            n = n2;
        }
        return arrn;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static int getBit(int[] arrn, int n) {
        if (n == 0) {
            return 1 & arrn[0];
        }
        int n2 = n & 255;
        int n3 = 0;
        if (n2 != n) return n3;
        int n4 = n >>> 5;
        int n5 = n & 31;
        return 1 & arrn[n4] >>> n5;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static boolean gte(int[] arrn, int n, int[] arrn2, int n2) {
        boolean bl = true;
        int n3 = 7;
        while (n3 >= 0) {
            int n4 = Integer.MIN_VALUE ^ arrn[n + n3];
            int n5 = Integer.MIN_VALUE ^ arrn2[n2 + n3];
            if (n4 < n5) {
                return false;
            }
            if (n4 > n5) return bl;
            --n3;
        }
        return bl;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static boolean gte(int[] arrn, int[] arrn2) {
        boolean bl = true;
        int n = 7;
        while (n >= 0) {
            int n2 = Integer.MIN_VALUE ^ arrn[n];
            int n3 = Integer.MIN_VALUE ^ arrn2[n];
            if (n2 < n3) {
                return false;
            }
            if (n2 > n3) return bl;
            --n;
        }
        return bl;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static boolean isOne(int[] arrn) {
        if (arrn[0] == 1) {
            int n = 1;
            do {
                if (n >= 8) {
                    return true;
                }
                if (arrn[n] != 0) break;
                ++n;
            } while (true);
        }
        return false;
    }

    public static boolean isZero(int[] arrn) {
        for (int i = 0; i < 8; ++i) {
            if (arrn[i] == 0) continue;
            return false;
        }
        return true;
    }

    public static void mul(int[] arrn, int n, int[] arrn2, int n2, int[] arrn3, int n3) {
        long l = 0xFFFFFFFFL & (long)arrn2[n2 + 0];
        long l2 = 0xFFFFFFFFL & (long)arrn2[n2 + 1];
        long l3 = 0xFFFFFFFFL & (long)arrn2[n2 + 2];
        long l4 = 0xFFFFFFFFL & (long)arrn2[n2 + 3];
        long l5 = 0xFFFFFFFFL & (long)arrn2[n2 + 4];
        long l6 = 0xFFFFFFFFL & (long)arrn2[n2 + 5];
        long l7 = 0xFFFFFFFFL & (long)arrn2[n2 + 6];
        long l8 = 0xFFFFFFFFL & (long)arrn2[n2 + 7];
        long l9 = 0xFFFFFFFFL & (long)arrn[n + 0];
        long l10 = 0L + l9 * l;
        arrn3[n3 + 0] = (int)l10;
        long l11 = (l10 >>> 32) + l9 * l2;
        arrn3[n3 + 1] = (int)l11;
        long l12 = (l11 >>> 32) + l9 * l3;
        arrn3[n3 + 2] = (int)l12;
        long l13 = (l12 >>> 32) + l9 * l4;
        arrn3[n3 + 3] = (int)l13;
        long l14 = (l13 >>> 32) + l9 * l5;
        arrn3[n3 + 4] = (int)l14;
        long l15 = (l14 >>> 32) + l9 * l6;
        arrn3[n3 + 5] = (int)l15;
        long l16 = (l15 >>> 32) + l9 * l7;
        arrn3[n3 + 6] = (int)l16;
        long l17 = (l16 >>> 32) + l9 * l8;
        arrn3[n3 + 7] = (int)l17;
        long l18 = l17 >>> 32;
        arrn3[n3 + 8] = (int)l18;
        for (int i = 1; i < 8; ++i) {
            long l19 = 0xFFFFFFFFL & (long)arrn[n + i];
            long l20 = 0L + (l19 * l + (0xFFFFFFFFL & (long)arrn3[++n3 + 0]));
            arrn3[n3 + 0] = (int)l20;
            long l21 = (l20 >>> 32) + (l19 * l2 + (0xFFFFFFFFL & (long)arrn3[n3 + 1]));
            arrn3[n3 + 1] = (int)l21;
            long l22 = (l21 >>> 32) + (l19 * l3 + (0xFFFFFFFFL & (long)arrn3[n3 + 2]));
            arrn3[n3 + 2] = (int)l22;
            long l23 = (l22 >>> 32) + (l19 * l4 + (0xFFFFFFFFL & (long)arrn3[n3 + 3]));
            arrn3[n3 + 3] = (int)l23;
            long l24 = (l23 >>> 32) + (l19 * l5 + (0xFFFFFFFFL & (long)arrn3[n3 + 4]));
            arrn3[n3 + 4] = (int)l24;
            long l25 = (l24 >>> 32) + (l19 * l6 + (0xFFFFFFFFL & (long)arrn3[n3 + 5]));
            arrn3[n3 + 5] = (int)l25;
            long l26 = (l25 >>> 32) + (l19 * l7 + (0xFFFFFFFFL & (long)arrn3[n3 + 6]));
            arrn3[n3 + 6] = (int)l26;
            long l27 = (l26 >>> 32) + (l19 * l8 + (0xFFFFFFFFL & (long)arrn3[n3 + 7]));
            arrn3[n3 + 7] = (int)l27;
            long l28 = l27 >>> 32;
            arrn3[n3 + 8] = (int)l28;
        }
    }

    public static void mul(int[] arrn, int[] arrn2, int[] arrn3) {
        long l = 0xFFFFFFFFL & (long)arrn2[0];
        long l2 = 0xFFFFFFFFL & (long)arrn2[1];
        long l3 = 0xFFFFFFFFL & (long)arrn2[2];
        long l4 = 0xFFFFFFFFL & (long)arrn2[3];
        long l5 = 0xFFFFFFFFL & (long)arrn2[4];
        long l6 = 0xFFFFFFFFL & (long)arrn2[5];
        long l7 = 0xFFFFFFFFL & (long)arrn2[6];
        long l8 = 0xFFFFFFFFL & (long)arrn2[7];
        long l9 = 0xFFFFFFFFL & (long)arrn[0];
        long l10 = 0L + l9 * l;
        arrn3[0] = (int)l10;
        long l11 = (l10 >>> 32) + l9 * l2;
        arrn3[1] = (int)l11;
        long l12 = (l11 >>> 32) + l9 * l3;
        arrn3[2] = (int)l12;
        long l13 = (l12 >>> 32) + l9 * l4;
        arrn3[3] = (int)l13;
        long l14 = (l13 >>> 32) + l9 * l5;
        arrn3[4] = (int)l14;
        long l15 = (l14 >>> 32) + l9 * l6;
        arrn3[5] = (int)l15;
        long l16 = (l15 >>> 32) + l9 * l7;
        arrn3[6] = (int)l16;
        long l17 = (l16 >>> 32) + l9 * l8;
        arrn3[7] = (int)l17;
        arrn3[8] = (int)(l17 >>> 32);
        for (int i = 1; i < 8; ++i) {
            long l18 = 0xFFFFFFFFL & (long)arrn[i];
            long l19 = 0L + (l18 * l + (0xFFFFFFFFL & (long)arrn3[i + 0]));
            arrn3[i + 0] = (int)l19;
            long l20 = (l19 >>> 32) + (l18 * l2 + (0xFFFFFFFFL & (long)arrn3[i + 1]));
            arrn3[i + 1] = (int)l20;
            long l21 = (l20 >>> 32) + (l18 * l3 + (0xFFFFFFFFL & (long)arrn3[i + 2]));
            arrn3[i + 2] = (int)l21;
            long l22 = (l21 >>> 32) + (l18 * l4 + (0xFFFFFFFFL & (long)arrn3[i + 3]));
            arrn3[i + 3] = (int)l22;
            long l23 = (l22 >>> 32) + (l18 * l5 + (0xFFFFFFFFL & (long)arrn3[i + 4]));
            arrn3[i + 4] = (int)l23;
            long l24 = (l23 >>> 32) + (l18 * l6 + (0xFFFFFFFFL & (long)arrn3[i + 5]));
            arrn3[i + 5] = (int)l24;
            long l25 = (l24 >>> 32) + (l18 * l7 + (0xFFFFFFFFL & (long)arrn3[i + 6]));
            arrn3[i + 6] = (int)l25;
            long l26 = (l25 >>> 32) + (l18 * l8 + (0xFFFFFFFFL & (long)arrn3[i + 7]));
            arrn3[i + 7] = (int)l26;
            long l27 = l26 >>> 32;
            arrn3[i + 8] = (int)l27;
        }
    }

    public static long mul33Add(int n, int[] arrn, int n2, int[] arrn2, int n3, int[] arrn3, int n4) {
        long l = 0xFFFFFFFFL & (long)n;
        long l2 = 0xFFFFFFFFL & (long)arrn[n2 + 0];
        long l3 = 0L + (l * l2 + (0xFFFFFFFFL & (long)arrn2[n3 + 0]));
        arrn3[n4 + 0] = (int)l3;
        long l4 = l3 >>> 32;
        long l5 = 0xFFFFFFFFL & (long)arrn[n2 + 1];
        long l6 = l4 + (l2 + l * l5 + (0xFFFFFFFFL & (long)arrn2[n3 + 1]));
        arrn3[n4 + 1] = (int)l6;
        long l7 = l6 >>> 32;
        long l8 = 0xFFFFFFFFL & (long)arrn[n2 + 2];
        long l9 = l7 + (l5 + l * l8 + (0xFFFFFFFFL & (long)arrn2[n3 + 2]));
        arrn3[n4 + 2] = (int)l9;
        long l10 = l9 >>> 32;
        long l11 = 0xFFFFFFFFL & (long)arrn[n2 + 3];
        long l12 = l10 + (l8 + l * l11 + (0xFFFFFFFFL & (long)arrn2[n3 + 3]));
        arrn3[n4 + 3] = (int)l12;
        long l13 = l12 >>> 32;
        long l14 = 0xFFFFFFFFL & (long)arrn[n2 + 4];
        long l15 = l13 + (l11 + l * l14 + (0xFFFFFFFFL & (long)arrn2[n3 + 4]));
        arrn3[n4 + 4] = (int)l15;
        long l16 = l15 >>> 32;
        long l17 = 0xFFFFFFFFL & (long)arrn[n2 + 5];
        long l18 = l16 + (l14 + l * l17 + (0xFFFFFFFFL & (long)arrn2[n3 + 5]));
        arrn3[n4 + 5] = (int)l18;
        long l19 = l18 >>> 32;
        long l20 = 0xFFFFFFFFL & (long)arrn[n2 + 6];
        long l21 = l19 + (l17 + l * l20 + (0xFFFFFFFFL & (long)arrn2[n3 + 6]));
        arrn3[n4 + 6] = (int)l21;
        long l22 = l21 >>> 32;
        long l23 = 0xFFFFFFFFL & (long)arrn[n2 + 7];
        long l24 = l22 + (l20 + l * l23 + (0xFFFFFFFFL & (long)arrn2[n3 + 7]));
        arrn3[n4 + 7] = (int)l24;
        return l23 + (l24 >>> 32);
    }

    public static int mul33DWordAdd(int n, long l, int[] arrn, int n2) {
        long l2 = 0xFFFFFFFFL & (long)n;
        long l3 = 0xFFFFFFFFL & l;
        long l4 = 0L + (l2 * l3 + (0xFFFFFFFFL & (long)arrn[n2 + 0]));
        arrn[n2 + 0] = (int)l4;
        long l5 = l4 >>> 32;
        long l6 = l >>> 32;
        long l7 = l5 + (l3 + l2 * l6 + (0xFFFFFFFFL & (long)arrn[n2 + 1]));
        arrn[n2 + 1] = (int)l7;
        long l8 = (l7 >>> 32) + (l6 + (0xFFFFFFFFL & (long)arrn[n2 + 2]));
        arrn[n2 + 2] = (int)l8;
        long l9 = (l8 >>> 32) + (0xFFFFFFFFL & (long)arrn[n2 + 3]);
        arrn[n2 + 3] = (int)l9;
        if (l9 >>> 32 == 0L) {
            return 0;
        }
        return Nat.incAt(8, arrn, n2, 4);
    }

    public static int mul33WordAdd(int n, int n2, int[] arrn, int n3) {
        long l = 0xFFFFFFFFL & (long)n;
        long l2 = 0xFFFFFFFFL & (long)n2;
        long l3 = 0L + (l * l2 + (0xFFFFFFFFL & (long)arrn[n3 + 0]));
        arrn[n3 + 0] = (int)l3;
        long l4 = (l3 >>> 32) + (l2 + (0xFFFFFFFFL & (long)arrn[n3 + 1]));
        arrn[n3 + 1] = (int)l4;
        long l5 = (l4 >>> 32) + (0xFFFFFFFFL & (long)arrn[n3 + 2]);
        arrn[n3 + 2] = (int)l5;
        if (l5 >>> 32 == 0L) {
            return 0;
        }
        return Nat.incAt(8, arrn, n3, 3);
    }

    public static int mulAddTo(int[] arrn, int n, int[] arrn2, int n2, int[] arrn3, int n3) {
        long l = 0xFFFFFFFFL & (long)arrn2[n2 + 0];
        long l2 = 0xFFFFFFFFL & (long)arrn2[n2 + 1];
        long l3 = 0xFFFFFFFFL & (long)arrn2[n2 + 2];
        long l4 = 0xFFFFFFFFL & (long)arrn2[n2 + 3];
        long l5 = 0xFFFFFFFFL & (long)arrn2[n2 + 4];
        long l6 = 0xFFFFFFFFL & (long)arrn2[n2 + 5];
        long l7 = 0xFFFFFFFFL & (long)arrn2[n2 + 6];
        long l8 = 0xFFFFFFFFL & (long)arrn2[n2 + 7];
        long l9 = 0L;
        for (int i = 0; i < 8; ++i) {
            long l10 = 0xFFFFFFFFL & (long)arrn[n + i];
            long l11 = 0L + (l10 * l + (0xFFFFFFFFL & (long)arrn3[n3 + 0]));
            arrn3[n3 + 0] = (int)l11;
            long l12 = (l11 >>> 32) + (l10 * l2 + (0xFFFFFFFFL & (long)arrn3[n3 + 1]));
            arrn3[n3 + 1] = (int)l12;
            long l13 = (l12 >>> 32) + (l10 * l3 + (0xFFFFFFFFL & (long)arrn3[n3 + 2]));
            arrn3[n3 + 2] = (int)l13;
            long l14 = (l13 >>> 32) + (l10 * l4 + (0xFFFFFFFFL & (long)arrn3[n3 + 3]));
            arrn3[n3 + 3] = (int)l14;
            long l15 = (l14 >>> 32) + (l10 * l5 + (0xFFFFFFFFL & (long)arrn3[n3 + 4]));
            arrn3[n3 + 4] = (int)l15;
            long l16 = (l15 >>> 32) + (l10 * l6 + (0xFFFFFFFFL & (long)arrn3[n3 + 5]));
            arrn3[n3 + 5] = (int)l16;
            long l17 = (l16 >>> 32) + (l10 * l7 + (0xFFFFFFFFL & (long)arrn3[n3 + 6]));
            arrn3[n3 + 6] = (int)l17;
            long l18 = (l17 >>> 32) + (l10 * l8 + (0xFFFFFFFFL & (long)arrn3[n3 + 7]));
            arrn3[n3 + 7] = (int)l18;
            long l19 = (l18 >>> 32) + (l9 + (0xFFFFFFFFL & (long)arrn3[n3 + 8]));
            arrn3[n3 + 8] = (int)l19;
            l9 = l19 >>> 32;
            ++n3;
        }
        return (int)l9;
    }

    public static int mulAddTo(int[] arrn, int[] arrn2, int[] arrn3) {
        long l = 0xFFFFFFFFL & (long)arrn2[0];
        long l2 = 0xFFFFFFFFL & (long)arrn2[1];
        long l3 = 0xFFFFFFFFL & (long)arrn2[2];
        long l4 = 0xFFFFFFFFL & (long)arrn2[3];
        long l5 = 0xFFFFFFFFL & (long)arrn2[4];
        long l6 = 0xFFFFFFFFL & (long)arrn2[5];
        long l7 = 0xFFFFFFFFL & (long)arrn2[6];
        long l8 = 0xFFFFFFFFL & (long)arrn2[7];
        long l9 = 0L;
        for (int i = 0; i < 8; ++i) {
            long l10 = 0xFFFFFFFFL & (long)arrn[i];
            long l11 = 0L + (l10 * l + (0xFFFFFFFFL & (long)arrn3[i + 0]));
            arrn3[i + 0] = (int)l11;
            long l12 = (l11 >>> 32) + (l10 * l2 + (0xFFFFFFFFL & (long)arrn3[i + 1]));
            arrn3[i + 1] = (int)l12;
            long l13 = (l12 >>> 32) + (l10 * l3 + (0xFFFFFFFFL & (long)arrn3[i + 2]));
            arrn3[i + 2] = (int)l13;
            long l14 = (l13 >>> 32) + (l10 * l4 + (0xFFFFFFFFL & (long)arrn3[i + 3]));
            arrn3[i + 3] = (int)l14;
            long l15 = (l14 >>> 32) + (l10 * l5 + (0xFFFFFFFFL & (long)arrn3[i + 4]));
            arrn3[i + 4] = (int)l15;
            long l16 = (l15 >>> 32) + (l10 * l6 + (0xFFFFFFFFL & (long)arrn3[i + 5]));
            arrn3[i + 5] = (int)l16;
            long l17 = (l16 >>> 32) + (l10 * l7 + (0xFFFFFFFFL & (long)arrn3[i + 6]));
            arrn3[i + 6] = (int)l17;
            long l18 = (l17 >>> 32) + (l10 * l8 + (0xFFFFFFFFL & (long)arrn3[i + 7]));
            arrn3[i + 7] = (int)l18;
            long l19 = (l18 >>> 32) + (l9 + (0xFFFFFFFFL & (long)arrn3[i + 8]));
            arrn3[i + 8] = (int)l19;
            l9 = l19 >>> 32;
        }
        return (int)l9;
    }

    public static int mulByWord(int n, int[] arrn) {
        long l = 0xFFFFFFFFL & (long)n;
        long l2 = 0L + l * (0xFFFFFFFFL & (long)arrn[0]);
        arrn[0] = (int)l2;
        long l3 = (l2 >>> 32) + l * (0xFFFFFFFFL & (long)arrn[1]);
        arrn[1] = (int)l3;
        long l4 = (l3 >>> 32) + l * (0xFFFFFFFFL & (long)arrn[2]);
        arrn[2] = (int)l4;
        long l5 = (l4 >>> 32) + l * (0xFFFFFFFFL & (long)arrn[3]);
        arrn[3] = (int)l5;
        long l6 = (l5 >>> 32) + l * (0xFFFFFFFFL & (long)arrn[4]);
        arrn[4] = (int)l6;
        long l7 = (l6 >>> 32) + l * (0xFFFFFFFFL & (long)arrn[5]);
        arrn[5] = (int)l7;
        long l8 = (l7 >>> 32) + l * (0xFFFFFFFFL & (long)arrn[6]);
        arrn[6] = (int)l8;
        long l9 = (l8 >>> 32) + l * (0xFFFFFFFFL & (long)arrn[7]);
        arrn[7] = (int)l9;
        return (int)(l9 >>> 32);
    }

    public static int mulByWordAddTo(int n, int[] arrn, int[] arrn2) {
        long l = 0xFFFFFFFFL & (long)n;
        long l2 = 0L + (l * (0xFFFFFFFFL & (long)arrn2[0]) + (0xFFFFFFFFL & (long)arrn[0]));
        arrn2[0] = (int)l2;
        long l3 = (l2 >>> 32) + (l * (0xFFFFFFFFL & (long)arrn2[1]) + (0xFFFFFFFFL & (long)arrn[1]));
        arrn2[1] = (int)l3;
        long l4 = (l3 >>> 32) + (l * (0xFFFFFFFFL & (long)arrn2[2]) + (0xFFFFFFFFL & (long)arrn[2]));
        arrn2[2] = (int)l4;
        long l5 = (l4 >>> 32) + (l * (0xFFFFFFFFL & (long)arrn2[3]) + (0xFFFFFFFFL & (long)arrn[3]));
        arrn2[3] = (int)l5;
        long l6 = (l5 >>> 32) + (l * (0xFFFFFFFFL & (long)arrn2[4]) + (0xFFFFFFFFL & (long)arrn[4]));
        arrn2[4] = (int)l6;
        long l7 = (l6 >>> 32) + (l * (0xFFFFFFFFL & (long)arrn2[5]) + (0xFFFFFFFFL & (long)arrn[5]));
        arrn2[5] = (int)l7;
        long l8 = (l7 >>> 32) + (l * (0xFFFFFFFFL & (long)arrn2[6]) + (0xFFFFFFFFL & (long)arrn[6]));
        arrn2[6] = (int)l8;
        long l9 = (l8 >>> 32) + (l * (0xFFFFFFFFL & (long)arrn2[7]) + (0xFFFFFFFFL & (long)arrn[7]));
        arrn2[7] = (int)l9;
        return (int)(l9 >>> 32);
    }

    public static int mulWord(int n, int[] arrn, int[] arrn2, int n2) {
        long l = 0L;
        long l2 = 0xFFFFFFFFL & (long)n;
        int n3 = 0;
        do {
            long l3 = l + l2 * (0xFFFFFFFFL & (long)arrn[n3]);
            arrn2[n2 + n3] = (int)l3;
            l = l3 >>> 32;
        } while (++n3 < 8);
        return (int)l;
    }

    public static int mulWordAddTo(int n, int[] arrn, int n2, int[] arrn2, int n3) {
        long l = 0xFFFFFFFFL & (long)n;
        long l2 = 0L + (l * (0xFFFFFFFFL & (long)arrn[n2 + 0]) + (0xFFFFFFFFL & (long)arrn2[n3 + 0]));
        arrn2[n3 + 0] = (int)l2;
        long l3 = (l2 >>> 32) + (l * (0xFFFFFFFFL & (long)arrn[n2 + 1]) + (0xFFFFFFFFL & (long)arrn2[n3 + 1]));
        arrn2[n3 + 1] = (int)l3;
        long l4 = (l3 >>> 32) + (l * (0xFFFFFFFFL & (long)arrn[n2 + 2]) + (0xFFFFFFFFL & (long)arrn2[n3 + 2]));
        arrn2[n3 + 2] = (int)l4;
        long l5 = (l4 >>> 32) + (l * (0xFFFFFFFFL & (long)arrn[n2 + 3]) + (0xFFFFFFFFL & (long)arrn2[n3 + 3]));
        arrn2[n3 + 3] = (int)l5;
        long l6 = (l5 >>> 32) + (l * (0xFFFFFFFFL & (long)arrn[n2 + 4]) + (0xFFFFFFFFL & (long)arrn2[n3 + 4]));
        arrn2[n3 + 4] = (int)l6;
        long l7 = (l6 >>> 32) + (l * (0xFFFFFFFFL & (long)arrn[n2 + 5]) + (0xFFFFFFFFL & (long)arrn2[n3 + 5]));
        arrn2[n3 + 5] = (int)l7;
        long l8 = (l7 >>> 32) + (l * (0xFFFFFFFFL & (long)arrn[n2 + 6]) + (0xFFFFFFFFL & (long)arrn2[n3 + 6]));
        arrn2[n3 + 6] = (int)l8;
        long l9 = (l8 >>> 32) + (l * (0xFFFFFFFFL & (long)arrn[n2 + 7]) + (0xFFFFFFFFL & (long)arrn2[n3 + 7]));
        arrn2[n3 + 7] = (int)l9;
        return (int)(l9 >>> 32);
    }

    public static int mulWordDwordAdd(int n, long l, int[] arrn, int n2) {
        long l2 = 0xFFFFFFFFL & (long)n;
        long l3 = 0L + (l2 * (0xFFFFFFFFL & l) + (0xFFFFFFFFL & (long)arrn[n2 + 0]));
        arrn[n2 + 0] = (int)l3;
        long l4 = (l3 >>> 32) + (l2 * (l >>> 32) + (0xFFFFFFFFL & (long)arrn[n2 + 1]));
        arrn[n2 + 1] = (int)l4;
        long l5 = (l4 >>> 32) + (0xFFFFFFFFL & (long)arrn[n2 + 2]);
        arrn[n2 + 2] = (int)l5;
        if (l5 >>> 32 == 0L) {
            return 0;
        }
        return Nat.incAt(8, arrn, n2, 3);
    }

    public static void square(int[] arrn, int n, int[] arrn2, int n2) {
        long l = 0xFFFFFFFFL & (long)arrn[n + 0];
        int n3 = 7;
        int n4 = 16;
        int n5 = 0;
        do {
            int n6 = n3 - 1;
            long l2 = 0xFFFFFFFFL & (long)arrn[n3 + n];
            long l3 = l2 * l2;
            int n7 = n4 - 1;
            arrn2[n2 + n7] = n5 << 31 | (int)(l3 >>> 33);
            n4 = n7 - 1;
            arrn2[n2 + n4] = (int)(l3 >>> 1);
            int n8 = (int)l3;
            if (n6 <= 0) {
                long l4 = l * l;
                long l5 = 0xFFFFFFFFL & (long)(n8 << 31) | l4 >>> 33;
                arrn2[n2 + 0] = (int)l4;
                int n9 = 1 & (int)(l4 >>> 32);
                long l6 = 0xFFFFFFFFL & (long)arrn[n + 1];
                long l7 = 0xFFFFFFFFL & (long)arrn2[n2 + 2];
                long l8 = l5 + l6 * l;
                int n10 = (int)l8;
                arrn2[n2 + 1] = n9 | n10 << 1;
                int n11 = n10 >>> 31;
                long l9 = l7 + (l8 >>> 32);
                long l10 = 0xFFFFFFFFL & (long)arrn[n + 2];
                long l11 = 0xFFFFFFFFL & (long)arrn2[n2 + 3];
                long l12 = 0xFFFFFFFFL & (long)arrn2[n2 + 4];
                long l13 = l9 + l10 * l;
                int n12 = (int)l13;
                arrn2[n2 + 2] = n11 | n12 << 1;
                int n13 = n12 >>> 31;
                long l14 = l11 + ((l13 >>> 32) + l10 * l6);
                long l15 = l12 + (l14 >>> 32);
                long l16 = l14 & 0xFFFFFFFFL;
                long l17 = 0xFFFFFFFFL & (long)arrn[n + 3];
                long l18 = 0xFFFFFFFFL & (long)arrn2[n2 + 5];
                long l19 = 0xFFFFFFFFL & (long)arrn2[n2 + 6];
                long l20 = l16 + l17 * l;
                int n14 = (int)l20;
                arrn2[n2 + 3] = n13 | n14 << 1;
                int n15 = n14 >>> 31;
                long l21 = l15 + ((l20 >>> 32) + l17 * l6);
                long l22 = l18 + ((l21 >>> 32) + l17 * l10);
                long l23 = l21 & 0xFFFFFFFFL;
                long l24 = l19 + (l22 >>> 32);
                long l25 = l22 & 0xFFFFFFFFL;
                long l26 = 0xFFFFFFFFL & (long)arrn[n + 4];
                long l27 = 0xFFFFFFFFL & (long)arrn2[n2 + 7];
                long l28 = 0xFFFFFFFFL & (long)arrn2[n2 + 8];
                long l29 = l23 + l26 * l;
                int n16 = (int)l29;
                arrn2[n2 + 4] = n15 | n16 << 1;
                int n17 = n16 >>> 31;
                long l30 = l25 + ((l29 >>> 32) + l26 * l6);
                long l31 = l24 + ((l30 >>> 32) + l26 * l10);
                long l32 = l30 & 0xFFFFFFFFL;
                long l33 = l27 + ((l31 >>> 32) + l26 * l17);
                long l34 = l31 & 0xFFFFFFFFL;
                long l35 = l28 + (l33 >>> 32);
                long l36 = l33 & 0xFFFFFFFFL;
                long l37 = 0xFFFFFFFFL & (long)arrn[n + 5];
                long l38 = 0xFFFFFFFFL & (long)arrn2[n2 + 9];
                long l39 = 0xFFFFFFFFL & (long)arrn2[n2 + 10];
                long l40 = l32 + l37 * l;
                int n18 = (int)l40;
                arrn2[n2 + 5] = n17 | n18 << 1;
                int n19 = n18 >>> 31;
                long l41 = l34 + ((l40 >>> 32) + l37 * l6);
                long l42 = l36 + ((l41 >>> 32) + l37 * l10);
                long l43 = l41 & 0xFFFFFFFFL;
                long l44 = l35 + ((l42 >>> 32) + l37 * l17);
                long l45 = l42 & 0xFFFFFFFFL;
                long l46 = l38 + ((l44 >>> 32) + l37 * l26);
                long l47 = l44 & 0xFFFFFFFFL;
                long l48 = l39 + (l46 >>> 32);
                long l49 = l46 & 0xFFFFFFFFL;
                long l50 = 0xFFFFFFFFL & (long)arrn[n + 6];
                long l51 = 0xFFFFFFFFL & (long)arrn2[n2 + 11];
                long l52 = 0xFFFFFFFFL & (long)arrn2[n2 + 12];
                long l53 = l43 + l50 * l;
                int n20 = (int)l53;
                arrn2[n2 + 6] = n19 | n20 << 1;
                int n21 = n20 >>> 31;
                long l54 = l45 + ((l53 >>> 32) + l50 * l6);
                long l55 = l47 + ((l54 >>> 32) + l50 * l10);
                long l56 = l54 & 0xFFFFFFFFL;
                long l57 = l49 + ((l55 >>> 32) + l50 * l17);
                long l58 = l55 & 0xFFFFFFFFL;
                long l59 = l48 + ((l57 >>> 32) + l50 * l26);
                long l60 = l57 & 0xFFFFFFFFL;
                long l61 = l51 + ((l59 >>> 32) + l50 * l37);
                long l62 = l59 & 0xFFFFFFFFL;
                long l63 = l52 + (l61 >>> 32);
                long l64 = l61 & 0xFFFFFFFFL;
                long l65 = 0xFFFFFFFFL & (long)arrn[n + 7];
                long l66 = 0xFFFFFFFFL & (long)arrn2[n2 + 13];
                long l67 = 0xFFFFFFFFL & (long)arrn2[n2 + 14];
                long l68 = l56 + l * l65;
                int n22 = (int)l68;
                arrn2[n2 + 7] = n21 | n22 << 1;
                int n23 = n22 >>> 31;
                long l69 = l58 + ((l68 >>> 32) + l65 * l6);
                long l70 = l60 + ((l69 >>> 32) + l65 * l10);
                long l71 = l62 + ((l70 >>> 32) + l65 * l17);
                long l72 = l64 + ((l71 >>> 32) + l65 * l26);
                long l73 = l63 + ((l72 >>> 32) + l65 * l37);
                long l74 = l66 + ((l73 >>> 32) + l65 * l50);
                long l75 = l67 + (l74 >>> 32);
                int n24 = (int)l69;
                arrn2[n2 + 8] = n23 | n24 << 1;
                int n25 = n24 >>> 31;
                int n26 = (int)l70;
                arrn2[n2 + 9] = n25 | n26 << 1;
                int n27 = n26 >>> 31;
                int n28 = (int)l71;
                arrn2[n2 + 10] = n27 | n28 << 1;
                int n29 = n28 >>> 31;
                int n30 = (int)l72;
                arrn2[n2 + 11] = n29 | n30 << 1;
                int n31 = n30 >>> 31;
                int n32 = (int)l73;
                arrn2[n2 + 12] = n31 | n32 << 1;
                int n33 = n32 >>> 31;
                int n34 = (int)l74;
                arrn2[n2 + 13] = n33 | n34 << 1;
                int n35 = n34 >>> 31;
                int n36 = (int)l75;
                arrn2[n2 + 14] = n35 | n36 << 1;
                int n37 = n36 >>> 31;
                int n38 = arrn2[n2 + 15] + (int)(l75 >> 32);
                arrn2[n2 + 15] = n37 | n38 << 1;
                return;
            }
            n5 = n8;
            n3 = n6;
        } while (true);
    }

    public static void square(int[] arrn, int[] arrn2) {
        long l = 0xFFFFFFFFL & (long)arrn[0];
        int n = 7;
        int n2 = 16;
        int n3 = 0;
        do {
            int n4 = n - 1;
            long l2 = 0xFFFFFFFFL & (long)arrn[n];
            long l3 = l2 * l2;
            int n5 = n2 - 1;
            arrn2[n5] = n3 << 31 | (int)(l3 >>> 33);
            n2 = n5 - 1;
            arrn2[n2] = (int)(l3 >>> 1);
            int n6 = (int)l3;
            if (n4 <= 0) {
                long l4 = l * l;
                long l5 = 0xFFFFFFFFL & (long)(n6 << 31) | l4 >>> 33;
                arrn2[0] = (int)l4;
                int n7 = 1 & (int)(l4 >>> 32);
                long l6 = 0xFFFFFFFFL & (long)arrn[1];
                long l7 = 0xFFFFFFFFL & (long)arrn2[2];
                long l8 = l5 + l6 * l;
                int n8 = (int)l8;
                arrn2[1] = n7 | n8 << 1;
                int n9 = n8 >>> 31;
                long l9 = l7 + (l8 >>> 32);
                long l10 = 0xFFFFFFFFL & (long)arrn[2];
                long l11 = 0xFFFFFFFFL & (long)arrn2[3];
                long l12 = 0xFFFFFFFFL & (long)arrn2[4];
                long l13 = l9 + l10 * l;
                int n10 = (int)l13;
                arrn2[2] = n9 | n10 << 1;
                int n11 = n10 >>> 31;
                long l14 = l11 + ((l13 >>> 32) + l10 * l6);
                long l15 = l12 + (l14 >>> 32);
                long l16 = l14 & 0xFFFFFFFFL;
                long l17 = 0xFFFFFFFFL & (long)arrn[3];
                long l18 = 0xFFFFFFFFL & (long)arrn2[5];
                long l19 = 0xFFFFFFFFL & (long)arrn2[6];
                long l20 = l16 + l17 * l;
                int n12 = (int)l20;
                arrn2[3] = n11 | n12 << 1;
                int n13 = n12 >>> 31;
                long l21 = l15 + ((l20 >>> 32) + l17 * l6);
                long l22 = l18 + ((l21 >>> 32) + l17 * l10);
                long l23 = l21 & 0xFFFFFFFFL;
                long l24 = l19 + (l22 >>> 32);
                long l25 = l22 & 0xFFFFFFFFL;
                long l26 = 0xFFFFFFFFL & (long)arrn[4];
                long l27 = 0xFFFFFFFFL & (long)arrn2[7];
                long l28 = 0xFFFFFFFFL & (long)arrn2[8];
                long l29 = l23 + l26 * l;
                int n14 = (int)l29;
                arrn2[4] = n13 | n14 << 1;
                int n15 = n14 >>> 31;
                long l30 = l25 + ((l29 >>> 32) + l26 * l6);
                long l31 = l24 + ((l30 >>> 32) + l26 * l10);
                long l32 = l30 & 0xFFFFFFFFL;
                long l33 = l27 + ((l31 >>> 32) + l26 * l17);
                long l34 = l31 & 0xFFFFFFFFL;
                long l35 = l28 + (l33 >>> 32);
                long l36 = l33 & 0xFFFFFFFFL;
                long l37 = 0xFFFFFFFFL & (long)arrn[5];
                long l38 = 0xFFFFFFFFL & (long)arrn2[9];
                long l39 = 0xFFFFFFFFL & (long)arrn2[10];
                long l40 = l32 + l37 * l;
                int n16 = (int)l40;
                arrn2[5] = n15 | n16 << 1;
                int n17 = n16 >>> 31;
                long l41 = l34 + ((l40 >>> 32) + l37 * l6);
                long l42 = l36 + ((l41 >>> 32) + l37 * l10);
                long l43 = l41 & 0xFFFFFFFFL;
                long l44 = l35 + ((l42 >>> 32) + l37 * l17);
                long l45 = l42 & 0xFFFFFFFFL;
                long l46 = l38 + ((l44 >>> 32) + l37 * l26);
                long l47 = l44 & 0xFFFFFFFFL;
                long l48 = l39 + (l46 >>> 32);
                long l49 = l46 & 0xFFFFFFFFL;
                long l50 = 0xFFFFFFFFL & (long)arrn[6];
                long l51 = 0xFFFFFFFFL & (long)arrn2[11];
                long l52 = 0xFFFFFFFFL & (long)arrn2[12];
                long l53 = l43 + l50 * l;
                int n18 = (int)l53;
                arrn2[6] = n17 | n18 << 1;
                int n19 = n18 >>> 31;
                long l54 = l45 + ((l53 >>> 32) + l50 * l6);
                long l55 = l47 + ((l54 >>> 32) + l50 * l10);
                long l56 = l54 & 0xFFFFFFFFL;
                long l57 = l49 + ((l55 >>> 32) + l50 * l17);
                long l58 = l55 & 0xFFFFFFFFL;
                long l59 = l48 + ((l57 >>> 32) + l50 * l26);
                long l60 = l57 & 0xFFFFFFFFL;
                long l61 = l51 + ((l59 >>> 32) + l50 * l37);
                long l62 = l59 & 0xFFFFFFFFL;
                long l63 = l52 + (l61 >>> 32);
                long l64 = l61 & 0xFFFFFFFFL;
                long l65 = 0xFFFFFFFFL & (long)arrn[7];
                long l66 = 0xFFFFFFFFL & (long)arrn2[13];
                long l67 = 0xFFFFFFFFL & (long)arrn2[14];
                long l68 = l56 + l * l65;
                int n20 = (int)l68;
                arrn2[7] = n19 | n20 << 1;
                int n21 = n20 >>> 31;
                long l69 = l58 + ((l68 >>> 32) + l65 * l6);
                long l70 = l60 + ((l69 >>> 32) + l65 * l10);
                long l71 = l62 + ((l70 >>> 32) + l65 * l17);
                long l72 = l64 + ((l71 >>> 32) + l65 * l26);
                long l73 = l63 + ((l72 >>> 32) + l65 * l37);
                long l74 = l66 + ((l73 >>> 32) + l65 * l50);
                long l75 = l67 + (l74 >>> 32);
                int n22 = (int)l69;
                arrn2[8] = n21 | n22 << 1;
                int n23 = n22 >>> 31;
                int n24 = (int)l70;
                arrn2[9] = n23 | n24 << 1;
                int n25 = n24 >>> 31;
                int n26 = (int)l71;
                arrn2[10] = n25 | n26 << 1;
                int n27 = n26 >>> 31;
                int n28 = (int)l72;
                arrn2[11] = n27 | n28 << 1;
                int n29 = n28 >>> 31;
                int n30 = (int)l73;
                arrn2[12] = n29 | n30 << 1;
                int n31 = n30 >>> 31;
                int n32 = (int)l74;
                arrn2[13] = n31 | n32 << 1;
                int n33 = n32 >>> 31;
                int n34 = (int)l75;
                arrn2[14] = n33 | n34 << 1;
                arrn2[15] = n34 >>> 31 | arrn2[15] + (int)(l75 >> 32) << 1;
                return;
            }
            n3 = n6;
            n = n4;
        } while (true);
    }

    public static int sub(int[] arrn, int n, int[] arrn2, int n2, int[] arrn3, int n3) {
        long l = 0L + ((0xFFFFFFFFL & (long)arrn[n + 0]) - (0xFFFFFFFFL & (long)arrn2[n2 + 0]));
        arrn3[n3 + 0] = (int)l;
        long l2 = (l >> 32) + ((0xFFFFFFFFL & (long)arrn[n + 1]) - (0xFFFFFFFFL & (long)arrn2[n2 + 1]));
        arrn3[n3 + 1] = (int)l2;
        long l3 = (l2 >> 32) + ((0xFFFFFFFFL & (long)arrn[n + 2]) - (0xFFFFFFFFL & (long)arrn2[n2 + 2]));
        arrn3[n3 + 2] = (int)l3;
        long l4 = (l3 >> 32) + ((0xFFFFFFFFL & (long)arrn[n + 3]) - (0xFFFFFFFFL & (long)arrn2[n2 + 3]));
        arrn3[n3 + 3] = (int)l4;
        long l5 = (l4 >> 32) + ((0xFFFFFFFFL & (long)arrn[n + 4]) - (0xFFFFFFFFL & (long)arrn2[n2 + 4]));
        arrn3[n3 + 4] = (int)l5;
        long l6 = (l5 >> 32) + ((0xFFFFFFFFL & (long)arrn[n + 5]) - (0xFFFFFFFFL & (long)arrn2[n2 + 5]));
        arrn3[n3 + 5] = (int)l6;
        long l7 = (l6 >> 32) + ((0xFFFFFFFFL & (long)arrn[n + 6]) - (0xFFFFFFFFL & (long)arrn2[n2 + 6]));
        arrn3[n3 + 6] = (int)l7;
        long l8 = (l7 >> 32) + ((0xFFFFFFFFL & (long)arrn[n + 7]) - (0xFFFFFFFFL & (long)arrn2[n2 + 7]));
        arrn3[n3 + 7] = (int)l8;
        return (int)(l8 >> 32);
    }

    public static int sub(int[] arrn, int[] arrn2, int[] arrn3) {
        long l = 0L + ((0xFFFFFFFFL & (long)arrn[0]) - (0xFFFFFFFFL & (long)arrn2[0]));
        arrn3[0] = (int)l;
        long l2 = (l >> 32) + ((0xFFFFFFFFL & (long)arrn[1]) - (0xFFFFFFFFL & (long)arrn2[1]));
        arrn3[1] = (int)l2;
        long l3 = (l2 >> 32) + ((0xFFFFFFFFL & (long)arrn[2]) - (0xFFFFFFFFL & (long)arrn2[2]));
        arrn3[2] = (int)l3;
        long l4 = (l3 >> 32) + ((0xFFFFFFFFL & (long)arrn[3]) - (0xFFFFFFFFL & (long)arrn2[3]));
        arrn3[3] = (int)l4;
        long l5 = (l4 >> 32) + ((0xFFFFFFFFL & (long)arrn[4]) - (0xFFFFFFFFL & (long)arrn2[4]));
        arrn3[4] = (int)l5;
        long l6 = (l5 >> 32) + ((0xFFFFFFFFL & (long)arrn[5]) - (0xFFFFFFFFL & (long)arrn2[5]));
        arrn3[5] = (int)l6;
        long l7 = (l6 >> 32) + ((0xFFFFFFFFL & (long)arrn[6]) - (0xFFFFFFFFL & (long)arrn2[6]));
        arrn3[6] = (int)l7;
        long l8 = (l7 >> 32) + ((0xFFFFFFFFL & (long)arrn[7]) - (0xFFFFFFFFL & (long)arrn2[7]));
        arrn3[7] = (int)l8;
        return (int)(l8 >> 32);
    }

    public static int subBothFrom(int[] arrn, int[] arrn2, int[] arrn3) {
        long l = 0L + ((0xFFFFFFFFL & (long)arrn3[0]) - (0xFFFFFFFFL & (long)arrn[0]) - (0xFFFFFFFFL & (long)arrn2[0]));
        arrn3[0] = (int)l;
        long l2 = (l >> 32) + ((0xFFFFFFFFL & (long)arrn3[1]) - (0xFFFFFFFFL & (long)arrn[1]) - (0xFFFFFFFFL & (long)arrn2[1]));
        arrn3[1] = (int)l2;
        long l3 = (l2 >> 32) + ((0xFFFFFFFFL & (long)arrn3[2]) - (0xFFFFFFFFL & (long)arrn[2]) - (0xFFFFFFFFL & (long)arrn2[2]));
        arrn3[2] = (int)l3;
        long l4 = (l3 >> 32) + ((0xFFFFFFFFL & (long)arrn3[3]) - (0xFFFFFFFFL & (long)arrn[3]) - (0xFFFFFFFFL & (long)arrn2[3]));
        arrn3[3] = (int)l4;
        long l5 = (l4 >> 32) + ((0xFFFFFFFFL & (long)arrn3[4]) - (0xFFFFFFFFL & (long)arrn[4]) - (0xFFFFFFFFL & (long)arrn2[4]));
        arrn3[4] = (int)l5;
        long l6 = (l5 >> 32) + ((0xFFFFFFFFL & (long)arrn3[5]) - (0xFFFFFFFFL & (long)arrn[5]) - (0xFFFFFFFFL & (long)arrn2[5]));
        arrn3[5] = (int)l6;
        long l7 = (l6 >> 32) + ((0xFFFFFFFFL & (long)arrn3[6]) - (0xFFFFFFFFL & (long)arrn[6]) - (0xFFFFFFFFL & (long)arrn2[6]));
        arrn3[6] = (int)l7;
        long l8 = (l7 >> 32) + ((0xFFFFFFFFL & (long)arrn3[7]) - (0xFFFFFFFFL & (long)arrn[7]) - (0xFFFFFFFFL & (long)arrn2[7]));
        arrn3[7] = (int)l8;
        return (int)(l8 >> 32);
    }

    public static int subFrom(int[] arrn, int n, int[] arrn2, int n2) {
        long l = 0L + ((0xFFFFFFFFL & (long)arrn2[n2 + 0]) - (0xFFFFFFFFL & (long)arrn[n + 0]));
        arrn2[n2 + 0] = (int)l;
        long l2 = (l >> 32) + ((0xFFFFFFFFL & (long)arrn2[n2 + 1]) - (0xFFFFFFFFL & (long)arrn[n + 1]));
        arrn2[n2 + 1] = (int)l2;
        long l3 = (l2 >> 32) + ((0xFFFFFFFFL & (long)arrn2[n2 + 2]) - (0xFFFFFFFFL & (long)arrn[n + 2]));
        arrn2[n2 + 2] = (int)l3;
        long l4 = (l3 >> 32) + ((0xFFFFFFFFL & (long)arrn2[n2 + 3]) - (0xFFFFFFFFL & (long)arrn[n + 3]));
        arrn2[n2 + 3] = (int)l4;
        long l5 = (l4 >> 32) + ((0xFFFFFFFFL & (long)arrn2[n2 + 4]) - (0xFFFFFFFFL & (long)arrn[n + 4]));
        arrn2[n2 + 4] = (int)l5;
        long l6 = (l5 >> 32) + ((0xFFFFFFFFL & (long)arrn2[n2 + 5]) - (0xFFFFFFFFL & (long)arrn[n + 5]));
        arrn2[n2 + 5] = (int)l6;
        long l7 = (l6 >> 32) + ((0xFFFFFFFFL & (long)arrn2[n2 + 6]) - (0xFFFFFFFFL & (long)arrn[n + 6]));
        arrn2[n2 + 6] = (int)l7;
        long l8 = (l7 >> 32) + ((0xFFFFFFFFL & (long)arrn2[n2 + 7]) - (0xFFFFFFFFL & (long)arrn[n + 7]));
        arrn2[n2 + 7] = (int)l8;
        return (int)(l8 >> 32);
    }

    public static int subFrom(int[] arrn, int[] arrn2) {
        long l = 0L + ((0xFFFFFFFFL & (long)arrn2[0]) - (0xFFFFFFFFL & (long)arrn[0]));
        arrn2[0] = (int)l;
        long l2 = (l >> 32) + ((0xFFFFFFFFL & (long)arrn2[1]) - (0xFFFFFFFFL & (long)arrn[1]));
        arrn2[1] = (int)l2;
        long l3 = (l2 >> 32) + ((0xFFFFFFFFL & (long)arrn2[2]) - (0xFFFFFFFFL & (long)arrn[2]));
        arrn2[2] = (int)l3;
        long l4 = (l3 >> 32) + ((0xFFFFFFFFL & (long)arrn2[3]) - (0xFFFFFFFFL & (long)arrn[3]));
        arrn2[3] = (int)l4;
        long l5 = (l4 >> 32) + ((0xFFFFFFFFL & (long)arrn2[4]) - (0xFFFFFFFFL & (long)arrn[4]));
        arrn2[4] = (int)l5;
        long l6 = (l5 >> 32) + ((0xFFFFFFFFL & (long)arrn2[5]) - (0xFFFFFFFFL & (long)arrn[5]));
        arrn2[5] = (int)l6;
        long l7 = (l6 >> 32) + ((0xFFFFFFFFL & (long)arrn2[6]) - (0xFFFFFFFFL & (long)arrn[6]));
        arrn2[6] = (int)l7;
        long l8 = (l7 >> 32) + ((0xFFFFFFFFL & (long)arrn2[7]) - (0xFFFFFFFFL & (long)arrn[7]));
        arrn2[7] = (int)l8;
        return (int)(l8 >> 32);
    }

    public static BigInteger toBigInteger(int[] arrn) {
        byte[] arrby = new byte[32];
        for (int i = 0; i < 8; ++i) {
            int n = arrn[i];
            if (n == 0) continue;
            Pack.intToBigEndian(n, arrby, 7 - i << 2);
        }
        return new BigInteger(1, arrby);
    }

    public static void zero(int[] arrn) {
        arrn[0] = 0;
        arrn[1] = 0;
        arrn[2] = 0;
        arrn[3] = 0;
        arrn[4] = 0;
        arrn[5] = 0;
        arrn[6] = 0;
        arrn[7] = 0;
    }
}

