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

public abstract class Nat192 {
    private static final long M = 0xFFFFFFFFL;

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
        return (int)(l6 >>> 32);
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
        return (int)(l6 >>> 32);
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
        return (int)(l6 >>> 32);
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
        return (int)(l6 >>> 32);
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
        return (int)(l6 >>> 32);
    }

    public static void copy(int[] arrn, int[] arrn2) {
        arrn2[0] = arrn[0];
        arrn2[1] = arrn[1];
        arrn2[2] = arrn[2];
        arrn2[3] = arrn[3];
        arrn2[4] = arrn[4];
        arrn2[5] = arrn[5];
    }

    public static int[] create() {
        return new int[6];
    }

    public static int[] createExt() {
        return new int[12];
    }

    public static boolean diff(int[] arrn, int n, int[] arrn2, int n2, int[] arrn3, int n3) {
        boolean bl = Nat192.gte(arrn, n, arrn2, n2);
        if (bl) {
            Nat192.sub(arrn, n, arrn2, n2, arrn3, n3);
            return bl;
        }
        Nat192.sub(arrn2, n2, arrn, n, arrn3, n3);
        return bl;
    }

    public static boolean eq(int[] arrn, int[] arrn2) {
        for (int i = 5; i >= 0; --i) {
            if (arrn[i] == arrn2[i]) continue;
            return false;
        }
        return true;
    }

    public static int[] fromBigInteger(BigInteger bigInteger) {
        if (bigInteger.signum() < 0 || bigInteger.bitLength() > 192) {
            throw new IllegalArgumentException();
        }
        int[] arrn = Nat192.create();
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
        int n2 = n >> 5;
        int n3 = 0;
        if (n2 < 0) return n3;
        n3 = 0;
        if (n2 >= 6) return n3;
        int n4 = n & 31;
        return 1 & arrn[n2] >>> n4;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static boolean gte(int[] arrn, int n, int[] arrn2, int n2) {
        boolean bl = true;
        int n3 = 5;
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
        int n = 5;
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
                if (n >= 6) {
                    return true;
                }
                if (arrn[n] != 0) break;
                ++n;
            } while (true);
        }
        return false;
    }

    public static boolean isZero(int[] arrn) {
        for (int i = 0; i < 6; ++i) {
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
        long l7 = 0xFFFFFFFFL & (long)arrn[n + 0];
        long l8 = 0L + l7 * l;
        arrn3[n3 + 0] = (int)l8;
        long l9 = (l8 >>> 32) + l7 * l2;
        arrn3[n3 + 1] = (int)l9;
        long l10 = (l9 >>> 32) + l7 * l3;
        arrn3[n3 + 2] = (int)l10;
        long l11 = (l10 >>> 32) + l7 * l4;
        arrn3[n3 + 3] = (int)l11;
        long l12 = (l11 >>> 32) + l7 * l5;
        arrn3[n3 + 4] = (int)l12;
        long l13 = (l12 >>> 32) + l7 * l6;
        arrn3[n3 + 5] = (int)l13;
        long l14 = l13 >>> 32;
        arrn3[n3 + 6] = (int)l14;
        for (int i = 1; i < 6; ++i) {
            long l15 = 0xFFFFFFFFL & (long)arrn[n + i];
            long l16 = 0L + (l15 * l + (0xFFFFFFFFL & (long)arrn3[++n3 + 0]));
            arrn3[n3 + 0] = (int)l16;
            long l17 = (l16 >>> 32) + (l15 * l2 + (0xFFFFFFFFL & (long)arrn3[n3 + 1]));
            arrn3[n3 + 1] = (int)l17;
            long l18 = (l17 >>> 32) + (l15 * l3 + (0xFFFFFFFFL & (long)arrn3[n3 + 2]));
            arrn3[n3 + 2] = (int)l18;
            long l19 = (l18 >>> 32) + (l15 * l4 + (0xFFFFFFFFL & (long)arrn3[n3 + 3]));
            arrn3[n3 + 3] = (int)l19;
            long l20 = (l19 >>> 32) + (l15 * l5 + (0xFFFFFFFFL & (long)arrn3[n3 + 4]));
            arrn3[n3 + 4] = (int)l20;
            long l21 = (l20 >>> 32) + (l15 * l6 + (0xFFFFFFFFL & (long)arrn3[n3 + 5]));
            arrn3[n3 + 5] = (int)l21;
            long l22 = l21 >>> 32;
            arrn3[n3 + 6] = (int)l22;
        }
    }

    public static void mul(int[] arrn, int[] arrn2, int[] arrn3) {
        long l = 0xFFFFFFFFL & (long)arrn2[0];
        long l2 = 0xFFFFFFFFL & (long)arrn2[1];
        long l3 = 0xFFFFFFFFL & (long)arrn2[2];
        long l4 = 0xFFFFFFFFL & (long)arrn2[3];
        long l5 = 0xFFFFFFFFL & (long)arrn2[4];
        long l6 = 0xFFFFFFFFL & (long)arrn2[5];
        long l7 = 0xFFFFFFFFL & (long)arrn[0];
        long l8 = 0L + l7 * l;
        arrn3[0] = (int)l8;
        long l9 = (l8 >>> 32) + l7 * l2;
        arrn3[1] = (int)l9;
        long l10 = (l9 >>> 32) + l7 * l3;
        arrn3[2] = (int)l10;
        long l11 = (l10 >>> 32) + l7 * l4;
        arrn3[3] = (int)l11;
        long l12 = (l11 >>> 32) + l7 * l5;
        arrn3[4] = (int)l12;
        long l13 = (l12 >>> 32) + l7 * l6;
        arrn3[5] = (int)l13;
        arrn3[6] = (int)(l13 >>> 32);
        for (int i = 1; i < 6; ++i) {
            long l14 = 0xFFFFFFFFL & (long)arrn[i];
            long l15 = 0L + (l14 * l + (0xFFFFFFFFL & (long)arrn3[i + 0]));
            arrn3[i + 0] = (int)l15;
            long l16 = (l15 >>> 32) + (l14 * l2 + (0xFFFFFFFFL & (long)arrn3[i + 1]));
            arrn3[i + 1] = (int)l16;
            long l17 = (l16 >>> 32) + (l14 * l3 + (0xFFFFFFFFL & (long)arrn3[i + 2]));
            arrn3[i + 2] = (int)l17;
            long l18 = (l17 >>> 32) + (l14 * l4 + (0xFFFFFFFFL & (long)arrn3[i + 3]));
            arrn3[i + 3] = (int)l18;
            long l19 = (l18 >>> 32) + (l14 * l5 + (0xFFFFFFFFL & (long)arrn3[i + 4]));
            arrn3[i + 4] = (int)l19;
            long l20 = (l19 >>> 32) + (l14 * l6 + (0xFFFFFFFFL & (long)arrn3[i + 5]));
            arrn3[i + 5] = (int)l20;
            long l21 = l20 >>> 32;
            arrn3[i + 6] = (int)l21;
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
        return l17 + (l18 >>> 32);
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
        return Nat.incAt(6, arrn, n2, 4);
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
        return Nat.incAt(6, arrn, n3, 3);
    }

    public static int mulAddTo(int[] arrn, int n, int[] arrn2, int n2, int[] arrn3, int n3) {
        long l = 0xFFFFFFFFL & (long)arrn2[n2 + 0];
        long l2 = 0xFFFFFFFFL & (long)arrn2[n2 + 1];
        long l3 = 0xFFFFFFFFL & (long)arrn2[n2 + 2];
        long l4 = 0xFFFFFFFFL & (long)arrn2[n2 + 3];
        long l5 = 0xFFFFFFFFL & (long)arrn2[n2 + 4];
        long l6 = 0xFFFFFFFFL & (long)arrn2[n2 + 5];
        long l7 = 0L;
        for (int i = 0; i < 6; ++i) {
            long l8 = 0xFFFFFFFFL & (long)arrn[n + i];
            long l9 = 0L + (l8 * l + (0xFFFFFFFFL & (long)arrn3[n3 + 0]));
            arrn3[n3 + 0] = (int)l9;
            long l10 = (l9 >>> 32) + (l8 * l2 + (0xFFFFFFFFL & (long)arrn3[n3 + 1]));
            arrn3[n3 + 1] = (int)l10;
            long l11 = (l10 >>> 32) + (l8 * l3 + (0xFFFFFFFFL & (long)arrn3[n3 + 2]));
            arrn3[n3 + 2] = (int)l11;
            long l12 = (l11 >>> 32) + (l8 * l4 + (0xFFFFFFFFL & (long)arrn3[n3 + 3]));
            arrn3[n3 + 3] = (int)l12;
            long l13 = (l12 >>> 32) + (l8 * l5 + (0xFFFFFFFFL & (long)arrn3[n3 + 4]));
            arrn3[n3 + 4] = (int)l13;
            long l14 = (l13 >>> 32) + (l8 * l6 + (0xFFFFFFFFL & (long)arrn3[n3 + 5]));
            arrn3[n3 + 5] = (int)l14;
            long l15 = (l14 >>> 32) + (l7 + (0xFFFFFFFFL & (long)arrn3[n3 + 6]));
            arrn3[n3 + 6] = (int)l15;
            l7 = l15 >>> 32;
            ++n3;
        }
        return (int)l7;
    }

    public static int mulAddTo(int[] arrn, int[] arrn2, int[] arrn3) {
        long l = 0xFFFFFFFFL & (long)arrn2[0];
        long l2 = 0xFFFFFFFFL & (long)arrn2[1];
        long l3 = 0xFFFFFFFFL & (long)arrn2[2];
        long l4 = 0xFFFFFFFFL & (long)arrn2[3];
        long l5 = 0xFFFFFFFFL & (long)arrn2[4];
        long l6 = 0xFFFFFFFFL & (long)arrn2[5];
        long l7 = 0L;
        for (int i = 0; i < 6; ++i) {
            long l8 = 0xFFFFFFFFL & (long)arrn[i];
            long l9 = 0L + (l8 * l + (0xFFFFFFFFL & (long)arrn3[i + 0]));
            arrn3[i + 0] = (int)l9;
            long l10 = (l9 >>> 32) + (l8 * l2 + (0xFFFFFFFFL & (long)arrn3[i + 1]));
            arrn3[i + 1] = (int)l10;
            long l11 = (l10 >>> 32) + (l8 * l3 + (0xFFFFFFFFL & (long)arrn3[i + 2]));
            arrn3[i + 2] = (int)l11;
            long l12 = (l11 >>> 32) + (l8 * l4 + (0xFFFFFFFFL & (long)arrn3[i + 3]));
            arrn3[i + 3] = (int)l12;
            long l13 = (l12 >>> 32) + (l8 * l5 + (0xFFFFFFFFL & (long)arrn3[i + 4]));
            arrn3[i + 4] = (int)l13;
            long l14 = (l13 >>> 32) + (l8 * l6 + (0xFFFFFFFFL & (long)arrn3[i + 5]));
            arrn3[i + 5] = (int)l14;
            long l15 = (l14 >>> 32) + (l7 + (0xFFFFFFFFL & (long)arrn3[i + 6]));
            arrn3[i + 6] = (int)l15;
            l7 = l15 >>> 32;
        }
        return (int)l7;
    }

    public static int mulWord(int n, int[] arrn, int[] arrn2, int n2) {
        long l = 0L;
        long l2 = 0xFFFFFFFFL & (long)n;
        int n3 = 0;
        do {
            long l3 = l + l2 * (0xFFFFFFFFL & (long)arrn[n3]);
            arrn2[n2 + n3] = (int)l3;
            l = l3 >>> 32;
        } while (++n3 < 6);
        return (int)l;
    }

    public static int mulWordAddExt(int n, int[] arrn, int n2, int[] arrn2, int n3) {
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
        return (int)(l7 >>> 32);
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
        return Nat.incAt(6, arrn, n2, 3);
    }

    public static void square(int[] arrn, int n, int[] arrn2, int n2) {
        long l = 0xFFFFFFFFL & (long)arrn[n + 0];
        int n3 = 5;
        int n4 = 12;
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
                long l40 = l32 + l * l37;
                int n18 = (int)l40;
                arrn2[n2 + 5] = n17 | n18 << 1;
                int n19 = n18 >>> 31;
                long l41 = l34 + ((l40 >>> 32) + l37 * l6);
                long l42 = l36 + ((l41 >>> 32) + l37 * l10);
                long l43 = l35 + ((l42 >>> 32) + l37 * l17);
                long l44 = l38 + ((l43 >>> 32) + l37 * l26);
                long l45 = l39 + (l44 >>> 32);
                int n20 = (int)l41;
                arrn2[n2 + 6] = n19 | n20 << 1;
                int n21 = n20 >>> 31;
                int n22 = (int)l42;
                arrn2[n2 + 7] = n21 | n22 << 1;
                int n23 = n22 >>> 31;
                int n24 = (int)l43;
                arrn2[n2 + 8] = n23 | n24 << 1;
                int n25 = n24 >>> 31;
                int n26 = (int)l44;
                arrn2[n2 + 9] = n25 | n26 << 1;
                int n27 = n26 >>> 31;
                int n28 = (int)l45;
                arrn2[n2 + 10] = n27 | n28 << 1;
                int n29 = n28 >>> 31;
                int n30 = arrn2[n2 + 11] + (int)(l45 >> 32);
                arrn2[n2 + 11] = n29 | n30 << 1;
                return;
            }
            n5 = n8;
            n3 = n6;
        } while (true);
    }

    public static void square(int[] arrn, int[] arrn2) {
        long l = 0xFFFFFFFFL & (long)arrn[0];
        int n = 5;
        int n2 = 12;
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
                long l40 = l32 + l * l37;
                int n16 = (int)l40;
                arrn2[5] = n15 | n16 << 1;
                int n17 = n16 >>> 31;
                long l41 = l34 + ((l40 >>> 32) + l37 * l6);
                long l42 = l36 + ((l41 >>> 32) + l37 * l10);
                long l43 = l35 + ((l42 >>> 32) + l37 * l17);
                long l44 = l38 + ((l43 >>> 32) + l37 * l26);
                long l45 = l39 + (l44 >>> 32);
                int n18 = (int)l41;
                arrn2[6] = n17 | n18 << 1;
                int n19 = n18 >>> 31;
                int n20 = (int)l42;
                arrn2[7] = n19 | n20 << 1;
                int n21 = n20 >>> 31;
                int n22 = (int)l43;
                arrn2[8] = n21 | n22 << 1;
                int n23 = n22 >>> 31;
                int n24 = (int)l44;
                arrn2[9] = n23 | n24 << 1;
                int n25 = n24 >>> 31;
                int n26 = (int)l45;
                arrn2[10] = n25 | n26 << 1;
                arrn2[11] = n26 >>> 31 | arrn2[11] + (int)(l45 >> 32) << 1;
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
        return (int)(l6 >> 32);
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
        return (int)(l6 >> 32);
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
        return (int)(l6 >> 32);
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
        return (int)(l6 >> 32);
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
        return (int)(l6 >> 32);
    }

    public static BigInteger toBigInteger(int[] arrn) {
        byte[] arrby = new byte[24];
        for (int i = 0; i < 6; ++i) {
            int n = arrn[i];
            if (n == 0) continue;
            Pack.intToBigEndian(n, arrby, 5 - i << 2);
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
    }
}

