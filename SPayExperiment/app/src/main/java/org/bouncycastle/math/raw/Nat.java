/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.System
 *  java.math.BigInteger
 */
package org.bouncycastle.math.raw;

import java.math.BigInteger;
import org.bouncycastle.util.Pack;

public abstract class Nat {
    private static final long M = 0xFFFFFFFFL;

    public static int add(int n, int[] arrn, int[] arrn2, int[] arrn3) {
        long l = 0L;
        for (int i = 0; i < n; ++i) {
            long l2 = l + ((0xFFFFFFFFL & (long)arrn[i]) + (0xFFFFFFFFL & (long)arrn2[i]));
            arrn3[i] = (int)l2;
            l = l2 >>> 32;
        }
        return (int)l;
    }

    public static int add33At(int n, int n2, int[] arrn, int n3) {
        long l = (0xFFFFFFFFL & (long)arrn[n3 + 0]) + (0xFFFFFFFFL & (long)n2);
        arrn[n3 + 0] = (int)l;
        long l2 = (l >>> 32) + (1L + (0xFFFFFFFFL & (long)arrn[n3 + 1]));
        arrn[n3 + 1] = (int)l2;
        if (l2 >>> 32 == 0L) {
            return 0;
        }
        return Nat.incAt(n, arrn, n3 + 2);
    }

    public static int add33At(int n, int n2, int[] arrn, int n3, int n4) {
        long l = (0xFFFFFFFFL & (long)arrn[n3 + n4]) + (0xFFFFFFFFL & (long)n2);
        arrn[n3 + n4] = (int)l;
        long l2 = (l >>> 32) + (1L + (0xFFFFFFFFL & (long)arrn[1 + (n3 + n4)]));
        arrn[1 + (n3 + n4)] = (int)l2;
        if (l2 >>> 32 == 0L) {
            return 0;
        }
        return Nat.incAt(n, arrn, n3, n4 + 2);
    }

    public static int add33To(int n, int n2, int[] arrn) {
        long l = (0xFFFFFFFFL & (long)arrn[0]) + (0xFFFFFFFFL & (long)n2);
        arrn[0] = (int)l;
        long l2 = (l >>> 32) + (1L + (0xFFFFFFFFL & (long)arrn[1]));
        arrn[1] = (int)l2;
        if (l2 >>> 32 == 0L) {
            return 0;
        }
        return Nat.incAt(n, arrn, 2);
    }

    public static int add33To(int n, int n2, int[] arrn, int n3) {
        long l = (0xFFFFFFFFL & (long)arrn[n3 + 0]) + (0xFFFFFFFFL & (long)n2);
        arrn[n3 + 0] = (int)l;
        long l2 = (l >>> 32) + (1L + (0xFFFFFFFFL & (long)arrn[n3 + 1]));
        arrn[n3 + 1] = (int)l2;
        if (l2 >>> 32 == 0L) {
            return 0;
        }
        return Nat.incAt(n, arrn, n3, 2);
    }

    public static int addBothTo(int n, int[] arrn, int n2, int[] arrn2, int n3, int[] arrn3, int n4) {
        long l = 0L;
        for (int i = 0; i < n; ++i) {
            long l2 = l + ((0xFFFFFFFFL & (long)arrn[n2 + i]) + (0xFFFFFFFFL & (long)arrn2[n3 + i]) + (0xFFFFFFFFL & (long)arrn3[n4 + i]));
            arrn3[n4 + i] = (int)l2;
            l = l2 >>> 32;
        }
        return (int)l;
    }

    public static int addBothTo(int n, int[] arrn, int[] arrn2, int[] arrn3) {
        long l = 0L;
        for (int i = 0; i < n; ++i) {
            long l2 = l + ((0xFFFFFFFFL & (long)arrn[i]) + (0xFFFFFFFFL & (long)arrn2[i]) + (0xFFFFFFFFL & (long)arrn3[i]));
            arrn3[i] = (int)l2;
            l = l2 >>> 32;
        }
        return (int)l;
    }

    public static int addDWordAt(int n, long l, int[] arrn, int n2) {
        long l2 = (0xFFFFFFFFL & (long)arrn[n2 + 0]) + (l & 0xFFFFFFFFL);
        arrn[n2 + 0] = (int)l2;
        long l3 = (l2 >>> 32) + ((0xFFFFFFFFL & (long)arrn[n2 + 1]) + (l >>> 32));
        arrn[n2 + 1] = (int)l3;
        if (l3 >>> 32 == 0L) {
            return 0;
        }
        return Nat.incAt(n, arrn, n2 + 2);
    }

    public static int addDWordAt(int n, long l, int[] arrn, int n2, int n3) {
        long l2 = (0xFFFFFFFFL & (long)arrn[n2 + n3]) + (l & 0xFFFFFFFFL);
        arrn[n2 + n3] = (int)l2;
        long l3 = (l2 >>> 32) + ((0xFFFFFFFFL & (long)arrn[1 + (n2 + n3)]) + (l >>> 32));
        arrn[1 + (n2 + n3)] = (int)l3;
        if (l3 >>> 32 == 0L) {
            return 0;
        }
        return Nat.incAt(n, arrn, n2, n3 + 2);
    }

    public static int addDWordTo(int n, long l, int[] arrn) {
        long l2 = (0xFFFFFFFFL & (long)arrn[0]) + (l & 0xFFFFFFFFL);
        arrn[0] = (int)l2;
        long l3 = (l2 >>> 32) + ((0xFFFFFFFFL & (long)arrn[1]) + (l >>> 32));
        arrn[1] = (int)l3;
        if (l3 >>> 32 == 0L) {
            return 0;
        }
        return Nat.incAt(n, arrn, 2);
    }

    public static int addDWordTo(int n, long l, int[] arrn, int n2) {
        long l2 = (0xFFFFFFFFL & (long)arrn[n2 + 0]) + (l & 0xFFFFFFFFL);
        arrn[n2 + 0] = (int)l2;
        long l3 = (l2 >>> 32) + ((0xFFFFFFFFL & (long)arrn[n2 + 1]) + (l >>> 32));
        arrn[n2 + 1] = (int)l3;
        if (l3 >>> 32 == 0L) {
            return 0;
        }
        return Nat.incAt(n, arrn, n2, 2);
    }

    public static int addTo(int n, int[] arrn, int n2, int[] arrn2, int n3) {
        long l = 0L;
        for (int i = 0; i < n; ++i) {
            long l2 = l + ((0xFFFFFFFFL & (long)arrn[n2 + i]) + (0xFFFFFFFFL & (long)arrn2[n3 + i]));
            arrn2[n3 + i] = (int)l2;
            l = l2 >>> 32;
        }
        return (int)l;
    }

    public static int addTo(int n, int[] arrn, int[] arrn2) {
        long l = 0L;
        for (int i = 0; i < n; ++i) {
            long l2 = l + ((0xFFFFFFFFL & (long)arrn[i]) + (0xFFFFFFFFL & (long)arrn2[i]));
            arrn2[i] = (int)l2;
            l = l2 >>> 32;
        }
        return (int)l;
    }

    public static int addWordAt(int n, int n2, int[] arrn, int n3) {
        long l = (0xFFFFFFFFL & (long)n2) + (0xFFFFFFFFL & (long)arrn[n3]);
        arrn[n3] = (int)l;
        if (l >>> 32 == 0L) {
            return 0;
        }
        return Nat.incAt(n, arrn, n3 + 1);
    }

    public static int addWordAt(int n, int n2, int[] arrn, int n3, int n4) {
        long l = (0xFFFFFFFFL & (long)n2) + (0xFFFFFFFFL & (long)arrn[n3 + n4]);
        arrn[n3 + n4] = (int)l;
        if (l >>> 32 == 0L) {
            return 0;
        }
        return Nat.incAt(n, arrn, n3, n4 + 1);
    }

    public static int addWordTo(int n, int n2, int[] arrn) {
        long l = (0xFFFFFFFFL & (long)n2) + (0xFFFFFFFFL & (long)arrn[0]);
        arrn[0] = (int)l;
        if (l >>> 32 == 0L) {
            return 0;
        }
        return Nat.incAt(n, arrn, 1);
    }

    public static int addWordTo(int n, int n2, int[] arrn, int n3) {
        long l = (0xFFFFFFFFL & (long)n2) + (0xFFFFFFFFL & (long)arrn[n3]);
        arrn[n3] = (int)l;
        if (l >>> 32 == 0L) {
            return 0;
        }
        return Nat.incAt(n, arrn, n3, 1);
    }

    public static void copy(int n, int[] arrn, int[] arrn2) {
        System.arraycopy((Object)arrn, (int)0, (Object)arrn2, (int)0, (int)n);
    }

    public static int[] copy(int n, int[] arrn) {
        int[] arrn2 = new int[n];
        System.arraycopy((Object)arrn, (int)0, (Object)arrn2, (int)0, (int)n);
        return arrn2;
    }

    public static int[] create(int n) {
        return new int[n];
    }

    public static int dec(int n, int[] arrn) {
        for (int i = 0; i < n; ++i) {
            int n2;
            arrn[i] = n2 = -1 + arrn[i];
            if (n2 == -1) continue;
            return 0;
        }
        return -1;
    }

    public static int dec(int n, int[] arrn, int[] arrn2) {
        int n2;
        block3 : {
            for (int i = 0; i < n; ++i) {
                int n3;
                arrn2[i] = n3 = -1 + arrn[i];
                if (n3 == -1) continue;
                int n4 = i;
                do {
                    n2 = 0;
                    if (n4 < n) {
                        arrn2[n4] = arrn[n4];
                        ++n4;
                        continue;
                    }
                    break block3;
                    break;
                } while (true);
            }
            n2 = -1;
        }
        return n2;
    }

    public static int decAt(int n, int[] arrn, int n2) {
        int n3 = -1;
        do {
            block4 : {
                block3 : {
                    int n4;
                    if (n2 >= n) break block3;
                    arrn[n2] = n4 = -1 + arrn[n2];
                    if (n4 == n3) break block4;
                    n3 = 0;
                }
                return n3;
            }
            ++n2;
        } while (true);
    }

    public static int decAt(int n, int[] arrn, int n2, int n3) {
        int n4 = -1;
        do {
            block4 : {
                block3 : {
                    int n5;
                    if (n3 >= n) break block3;
                    int n6 = n2 + n3;
                    arrn[n6] = n5 = -1 + arrn[n6];
                    if (n5 == n4) break block4;
                    n4 = 0;
                }
                return n4;
            }
            ++n3;
        } while (true);
    }

    public static boolean eq(int n, int[] arrn, int[] arrn2) {
        for (int i = n - 1; i >= 0; --i) {
            if (arrn[i] == arrn2[i]) continue;
            return false;
        }
        return true;
    }

    public static int[] fromBigInteger(int n, BigInteger bigInteger) {
        if (bigInteger.signum() < 0 || bigInteger.bitLength() > n) {
            throw new IllegalArgumentException();
        }
        int[] arrn = Nat.create(n + 31 >> 5);
        int n2 = 0;
        while (bigInteger.signum() != 0) {
            int n3 = n2 + 1;
            arrn[n2] = bigInteger.intValue();
            bigInteger = bigInteger.shiftRight(32);
            n2 = n3;
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
        int n4 = arrn.length;
        n3 = 0;
        if (n2 >= n4) return n3;
        int n5 = n & 31;
        return 1 & arrn[n2] >>> n5;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static boolean gte(int n, int[] arrn, int[] arrn2) {
        boolean bl = true;
        int n2 = n - 1;
        while (n2 >= 0) {
            int n3 = Integer.MIN_VALUE ^ arrn[n2];
            int n4 = Integer.MIN_VALUE ^ arrn2[n2];
            if (n3 < n4) {
                return false;
            }
            if (n3 > n4) return bl;
            --n2;
        }
        return bl;
    }

    public static int inc(int n, int[] arrn) {
        for (int i = 0; i < n; ++i) {
            int n2;
            arrn[i] = n2 = 1 + arrn[i];
            if (n2 == 0) continue;
            return 0;
        }
        return 1;
    }

    public static int inc(int n, int[] arrn, int[] arrn2) {
        int n2;
        block3 : {
            for (int i = 0; i < n; ++i) {
                int n3;
                arrn2[i] = n3 = 1 + arrn[i];
                if (n3 == 0) continue;
                do {
                    n2 = 0;
                    if (i < n) {
                        arrn2[i] = arrn[i];
                        ++i;
                        continue;
                    }
                    break block3;
                    break;
                } while (true);
            }
            n2 = 1;
        }
        return n2;
    }

    public static int incAt(int n, int[] arrn, int n2) {
        while (n2 < n) {
            int n3;
            arrn[n2] = n3 = 1 + arrn[n2];
            if (n3 != 0) {
                return 0;
            }
            ++n2;
        }
        return 1;
    }

    public static int incAt(int n, int[] arrn, int n2, int n3) {
        while (n3 < n) {
            int n4;
            int n5 = n2 + n3;
            arrn[n5] = n4 = 1 + arrn[n5];
            if (n4 != 0) {
                return 0;
            }
            ++n3;
        }
        return 1;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static boolean isOne(int n, int[] arrn) {
        if (arrn[0] == 1) {
            int n2 = 1;
            do {
                if (n2 >= n) {
                    return true;
                }
                if (arrn[n2] != 0) break;
                ++n2;
            } while (true);
        }
        return false;
    }

    public static boolean isZero(int n, int[] arrn) {
        for (int i = 0; i < n; ++i) {
            if (arrn[i] == 0) continue;
            return false;
        }
        return true;
    }

    public static void mul(int n, int[] arrn, int n2, int[] arrn2, int n3, int[] arrn3, int n4) {
        arrn3[n4 + n] = Nat.mulWord(n, arrn[n2], arrn2, n3, arrn3, n4);
        for (int i = 1; i < n; ++i) {
            arrn3[n + (n4 + i)] = Nat.mulWordAddTo(n, arrn[n2 + i], arrn2, n3, arrn3, n4 + i);
        }
    }

    public static void mul(int n, int[] arrn, int[] arrn2, int[] arrn3) {
        arrn3[n] = Nat.mulWord(n, arrn[0], arrn2, arrn3);
        for (int i = 1; i < n; ++i) {
            arrn3[i + n] = Nat.mulWordAddTo(n, arrn[i], arrn2, 0, arrn3, i);
        }
    }

    public static int mul31BothAdd(int n, int n2, int[] arrn, int n3, int[] arrn2, int[] arrn3, int n4) {
        long l = 0L;
        long l2 = 0xFFFFFFFFL & (long)n2;
        long l3 = 0xFFFFFFFFL & (long)n3;
        int n5 = 0;
        do {
            long l4 = l + (l2 * (0xFFFFFFFFL & (long)arrn[n5]) + l3 * (0xFFFFFFFFL & (long)arrn2[n5]) + (0xFFFFFFFFL & (long)arrn3[n4 + n5]));
            arrn3[n4 + n5] = (int)l4;
            l = l4 >>> 32;
        } while (++n5 < n);
        return (int)l;
    }

    public static int mulAddTo(int n, int[] arrn, int n2, int[] arrn2, int n3, int[] arrn3, int n4) {
        long l = 0L;
        int n5 = n4;
        for (int i = 0; i < n; ++i) {
            long l2 = (0xFFFFFFFFL & (long)Nat.mulWordAddTo(n, arrn[n2 + i], arrn2, n3, arrn3, n5)) + (l + (0xFFFFFFFFL & (long)arrn3[n5 + n]));
            arrn3[n5 + n] = (int)l2;
            long l3 = l2 >>> 32;
            ++n5;
            l = l3;
        }
        return (int)l;
    }

    public static int mulAddTo(int n, int[] arrn, int[] arrn2, int[] arrn3) {
        long l = 0L;
        for (int i = 0; i < n; ++i) {
            long l2 = (0xFFFFFFFFL & (long)Nat.mulWordAddTo(n, arrn[i], arrn2, 0, arrn3, i)) + (l + (0xFFFFFFFFL & (long)arrn3[i + n]));
            arrn3[i + n] = (int)l2;
            long l3 = l2 >>> 32;
            l = l3;
        }
        return (int)l;
    }

    public static int mulWord(int n, int n2, int[] arrn, int n3, int[] arrn2, int n4) {
        long l = 0L;
        long l2 = 0xFFFFFFFFL & (long)n2;
        int n5 = 0;
        do {
            long l3 = l + l2 * (0xFFFFFFFFL & (long)arrn[n3 + n5]);
            arrn2[n4 + n5] = (int)l3;
            l = l3 >>> 32;
        } while (++n5 < n);
        return (int)l;
    }

    public static int mulWord(int n, int n2, int[] arrn, int[] arrn2) {
        long l = 0L;
        long l2 = 0xFFFFFFFFL & (long)n2;
        int n3 = 0;
        do {
            long l3 = l + l2 * (0xFFFFFFFFL & (long)arrn[n3]);
            arrn2[n3] = (int)l3;
            l = l3 >>> 32;
        } while (++n3 < n);
        return (int)l;
    }

    public static int mulWordAddTo(int n, int n2, int[] arrn, int n3, int[] arrn2, int n4) {
        long l = 0L;
        long l2 = 0xFFFFFFFFL & (long)n2;
        int n5 = 0;
        do {
            long l3 = l + (l2 * (0xFFFFFFFFL & (long)arrn[n3 + n5]) + (0xFFFFFFFFL & (long)arrn2[n4 + n5]));
            arrn2[n4 + n5] = (int)l3;
            l = l3 >>> 32;
        } while (++n5 < n);
        return (int)l;
    }

    public static int mulWordDwordAddAt(int n, int n2, long l, int[] arrn, int n3) {
        long l2 = 0xFFFFFFFFL & (long)n2;
        long l3 = 0L + (l2 * (0xFFFFFFFFL & l) + (0xFFFFFFFFL & (long)arrn[n3 + 0]));
        arrn[n3 + 0] = (int)l3;
        long l4 = (l3 >>> 32) + (l2 * (l >>> 32) + (0xFFFFFFFFL & (long)arrn[n3 + 1]));
        arrn[n3 + 1] = (int)l4;
        long l5 = (l4 >>> 32) + (0xFFFFFFFFL & (long)arrn[n3 + 2]);
        arrn[n3 + 2] = (int)l5;
        if (l5 >>> 32 == 0L) {
            return 0;
        }
        return Nat.incAt(n, arrn, n3 + 3);
    }

    public static int shiftDownBit(int n, int[] arrn, int n2) {
        while (--n >= 0) {
            int n3 = arrn[n];
            arrn[n] = n3 >>> 1 | n2 << 31;
            n2 = n3;
        }
        return n2 << 31;
    }

    public static int shiftDownBit(int n, int[] arrn, int n2, int n3) {
        while (--n >= 0) {
            int n4 = arrn[n2 + n];
            arrn[n2 + n] = n4 >>> 1 | n3 << 31;
            n3 = n4;
        }
        return n3 << 31;
    }

    public static int shiftDownBit(int n, int[] arrn, int n2, int n3, int[] arrn2, int n4) {
        while (--n >= 0) {
            int n5 = arrn[n2 + n];
            arrn2[n4 + n] = n5 >>> 1 | n3 << 31;
            n3 = n5;
        }
        return n3 << 31;
    }

    public static int shiftDownBit(int n, int[] arrn, int n2, int[] arrn2) {
        while (--n >= 0) {
            int n3 = arrn[n];
            arrn2[n] = n3 >>> 1 | n2 << 31;
            n2 = n3;
        }
        return n2 << 31;
    }

    public static int shiftDownBits(int n, int[] arrn, int n2, int n3) {
        while (--n >= 0) {
            int n4 = arrn[n];
            arrn[n] = n4 >>> n2 | n3 << -n2;
            n3 = n4;
        }
        return n3 << -n2;
    }

    public static int shiftDownBits(int n, int[] arrn, int n2, int n3, int n4) {
        while (--n >= 0) {
            int n5 = arrn[n2 + n];
            arrn[n2 + n] = n5 >>> n3 | n4 << -n3;
            n4 = n5;
        }
        return n4 << -n3;
    }

    public static int shiftDownBits(int n, int[] arrn, int n2, int n3, int n4, int[] arrn2, int n5) {
        while (--n >= 0) {
            int n6 = arrn[n2 + n];
            arrn2[n5 + n] = n6 >>> n3 | n4 << -n3;
            n4 = n6;
        }
        return n4 << -n3;
    }

    public static int shiftDownBits(int n, int[] arrn, int n2, int n3, int[] arrn2) {
        while (--n >= 0) {
            int n4 = arrn[n];
            arrn2[n] = n4 >>> n2 | n3 << -n2;
            n3 = n4;
        }
        return n3 << -n2;
    }

    public static int shiftDownWord(int n, int[] arrn, int n2) {
        while (--n >= 0) {
            int n3 = arrn[n];
            arrn[n] = n2;
            n2 = n3;
        }
        return n2;
    }

    public static int shiftUpBit(int n, int[] arrn, int n2) {
        for (int i = 0; i < n; ++i) {
            int n3 = arrn[i];
            arrn[i] = n3 << 1 | n2 >>> 31;
            n2 = n3;
        }
        return n2 >>> 31;
    }

    public static int shiftUpBit(int n, int[] arrn, int n2, int n3) {
        for (int i = 0; i < n; ++i) {
            int n4 = arrn[n2 + i];
            arrn[n2 + i] = n4 << 1 | n3 >>> 31;
            n3 = n4;
        }
        return n3 >>> 31;
    }

    public static int shiftUpBit(int n, int[] arrn, int n2, int n3, int[] arrn2, int n4) {
        for (int i = 0; i < n; ++i) {
            int n5 = arrn[n2 + i];
            arrn2[n4 + i] = n5 << 1 | n3 >>> 31;
            n3 = n5;
        }
        return n3 >>> 31;
    }

    public static int shiftUpBit(int n, int[] arrn, int n2, int[] arrn2) {
        for (int i = 0; i < n; ++i) {
            int n3 = arrn[i];
            arrn2[i] = n3 << 1 | n2 >>> 31;
            n2 = n3;
        }
        return n2 >>> 31;
    }

    public static int shiftUpBits(int n, int[] arrn, int n2, int n3) {
        for (int i = 0; i < n; ++i) {
            int n4 = arrn[i];
            arrn[i] = n4 << n2 | n3 >>> -n2;
            n3 = n4;
        }
        return n3 >>> -n2;
    }

    public static int shiftUpBits(int n, int[] arrn, int n2, int n3, int n4) {
        for (int i = 0; i < n; ++i) {
            int n5 = arrn[n2 + i];
            arrn[n2 + i] = n5 << n3 | n4 >>> -n3;
            n4 = n5;
        }
        return n4 >>> -n3;
    }

    public static int shiftUpBits(int n, int[] arrn, int n2, int n3, int n4, int[] arrn2, int n5) {
        for (int i = 0; i < n; ++i) {
            int n6 = arrn[n2 + i];
            arrn2[n5 + i] = n6 << n3 | n4 >>> -n3;
            n4 = n6;
        }
        return n4 >>> -n3;
    }

    public static int shiftUpBits(int n, int[] arrn, int n2, int n3, int[] arrn2) {
        for (int i = 0; i < n; ++i) {
            int n4 = arrn[i];
            arrn2[i] = n4 << n2 | n3 >>> -n2;
            n3 = n4;
        }
        return n3 >>> -n2;
    }

    public static void square(int n, int[] arrn, int n2, int[] arrn2, int n3) {
        int n4 = n << 1;
        int n5 = n;
        int n6 = 0;
        int n7 = n4;
        do {
            long l = 0xFFFFFFFFL & (long)arrn[n2 + --n5];
            long l2 = l * l;
            int n8 = n7 - 1;
            arrn2[n3 + n8] = n6 << 31 | (int)(l2 >>> 33);
            n7 = n8 - 1;
            arrn2[n3 + n7] = (int)(l2 >>> 1);
            n6 = (int)l2;
        } while (n5 > 0);
        for (int i = 1; i < n; ++i) {
            Nat.addWordAt(n4, Nat.squareWordAdd(arrn, n2, i, arrn2, n3), arrn2, n3, i << 1);
        }
        Nat.shiftUpBit(n4, arrn2, n3, arrn[n2] << 31);
    }

    public static void square(int n, int[] arrn, int[] arrn2) {
        int n2;
        int n3 = n2 = n << 1;
        int n4 = n;
        int n5 = 0;
        do {
            long l = 0xFFFFFFFFL & (long)arrn[--n4];
            long l2 = l * l;
            int n6 = n3 - 1;
            arrn2[n6] = n5 << 31 | (int)(l2 >>> 33);
            n3 = n6 - 1;
            arrn2[n3] = (int)(l2 >>> 1);
            n5 = (int)l2;
        } while (n4 > 0);
        for (int i = 1; i < n; ++i) {
            Nat.addWordAt(n2, Nat.squareWordAdd(arrn, i, arrn2), arrn2, i << 1);
        }
        Nat.shiftUpBit(n2, arrn2, arrn[0] << 31);
    }

    public static int squareWordAdd(int[] arrn, int n, int n2, int[] arrn2, int n3) {
        long l = 0L;
        long l2 = 0xFFFFFFFFL & (long)arrn[n + n2];
        int n4 = 0;
        do {
            long l3 = l + (l2 * (0xFFFFFFFFL & (long)arrn[n + n4]) + (0xFFFFFFFFL & (long)arrn2[n2 + n3]));
            arrn2[n2 + n3] = (int)l3;
            l = l3 >>> 32;
            ++n3;
        } while (++n4 < n2);
        return (int)l;
    }

    public static int squareWordAdd(int[] arrn, int n, int[] arrn2) {
        long l = 0L;
        long l2 = 0xFFFFFFFFL & (long)arrn[n];
        int n2 = 0;
        do {
            long l3 = l + (l2 * (0xFFFFFFFFL & (long)arrn[n2]) + (0xFFFFFFFFL & (long)arrn2[n + n2]));
            arrn2[n + n2] = (int)l3;
            l = l3 >>> 32;
        } while (++n2 < n);
        return (int)l;
    }

    public static int sub(int n, int[] arrn, int n2, int[] arrn2, int n3, int[] arrn3, int n4) {
        long l = 0L;
        for (int i = 0; i < n; ++i) {
            long l2 = l + ((0xFFFFFFFFL & (long)arrn[n2 + i]) - (0xFFFFFFFFL & (long)arrn2[n3 + i]));
            arrn3[n4 + i] = (int)l2;
            l = l2 >> 32;
        }
        return (int)l;
    }

    public static int sub(int n, int[] arrn, int[] arrn2, int[] arrn3) {
        long l = 0L;
        for (int i = 0; i < n; ++i) {
            long l2 = l + ((0xFFFFFFFFL & (long)arrn[i]) - (0xFFFFFFFFL & (long)arrn2[i]));
            arrn3[i] = (int)l2;
            l = l2 >> 32;
        }
        return (int)l;
    }

    public static int sub33At(int n, int n2, int[] arrn, int n3) {
        long l = (0xFFFFFFFFL & (long)arrn[n3 + 0]) - (0xFFFFFFFFL & (long)n2);
        arrn[n3 + 0] = (int)l;
        long l2 = (l >> 32) + ((0xFFFFFFFFL & (long)arrn[n3 + 1]) - 1L);
        arrn[n3 + 1] = (int)l2;
        if (l2 >> 32 == 0L) {
            return 0;
        }
        return Nat.decAt(n, arrn, n3 + 2);
    }

    public static int sub33At(int n, int n2, int[] arrn, int n3, int n4) {
        long l = (0xFFFFFFFFL & (long)arrn[n3 + n4]) - (0xFFFFFFFFL & (long)n2);
        arrn[n3 + n4] = (int)l;
        long l2 = (l >> 32) + ((0xFFFFFFFFL & (long)arrn[1 + (n3 + n4)]) - 1L);
        arrn[1 + (n3 + n4)] = (int)l2;
        if (l2 >> 32 == 0L) {
            return 0;
        }
        return Nat.decAt(n, arrn, n3, n4 + 2);
    }

    public static int sub33From(int n, int n2, int[] arrn) {
        long l = (0xFFFFFFFFL & (long)arrn[0]) - (0xFFFFFFFFL & (long)n2);
        arrn[0] = (int)l;
        long l2 = (l >> 32) + ((0xFFFFFFFFL & (long)arrn[1]) - 1L);
        arrn[1] = (int)l2;
        if (l2 >> 32 == 0L) {
            return 0;
        }
        return Nat.decAt(n, arrn, 2);
    }

    public static int sub33From(int n, int n2, int[] arrn, int n3) {
        long l = (0xFFFFFFFFL & (long)arrn[n3 + 0]) - (0xFFFFFFFFL & (long)n2);
        arrn[n3 + 0] = (int)l;
        long l2 = (l >> 32) + ((0xFFFFFFFFL & (long)arrn[n3 + 1]) - 1L);
        arrn[n3 + 1] = (int)l2;
        if (l2 >> 32 == 0L) {
            return 0;
        }
        return Nat.decAt(n, arrn, n3, 2);
    }

    public static int subBothFrom(int n, int[] arrn, int n2, int[] arrn2, int n3, int[] arrn3, int n4) {
        long l = 0L;
        for (int i = 0; i < n; ++i) {
            long l2 = l + ((0xFFFFFFFFL & (long)arrn3[n4 + i]) - (0xFFFFFFFFL & (long)arrn[n2 + i]) - (0xFFFFFFFFL & (long)arrn2[n3 + i]));
            arrn3[n4 + i] = (int)l2;
            l = l2 >> 32;
        }
        return (int)l;
    }

    public static int subBothFrom(int n, int[] arrn, int[] arrn2, int[] arrn3) {
        long l = 0L;
        for (int i = 0; i < n; ++i) {
            long l2 = l + ((0xFFFFFFFFL & (long)arrn3[i]) - (0xFFFFFFFFL & (long)arrn[i]) - (0xFFFFFFFFL & (long)arrn2[i]));
            arrn3[i] = (int)l2;
            l = l2 >> 32;
        }
        return (int)l;
    }

    public static int subDWordAt(int n, long l, int[] arrn, int n2) {
        long l2 = (0xFFFFFFFFL & (long)arrn[n2 + 0]) - (l & 0xFFFFFFFFL);
        arrn[n2 + 0] = (int)l2;
        long l3 = (l2 >> 32) + ((0xFFFFFFFFL & (long)arrn[n2 + 1]) - (l >>> 32));
        arrn[n2 + 1] = (int)l3;
        if (l3 >> 32 == 0L) {
            return 0;
        }
        return Nat.decAt(n, arrn, n2 + 2);
    }

    public static int subDWordAt(int n, long l, int[] arrn, int n2, int n3) {
        long l2 = (0xFFFFFFFFL & (long)arrn[n2 + n3]) - (l & 0xFFFFFFFFL);
        arrn[n2 + n3] = (int)l2;
        long l3 = (l2 >> 32) + ((0xFFFFFFFFL & (long)arrn[1 + (n2 + n3)]) - (l >>> 32));
        arrn[1 + (n2 + n3)] = (int)l3;
        if (l3 >> 32 == 0L) {
            return 0;
        }
        return Nat.decAt(n, arrn, n2, n3 + 2);
    }

    public static int subDWordFrom(int n, long l, int[] arrn) {
        long l2 = (0xFFFFFFFFL & (long)arrn[0]) - (l & 0xFFFFFFFFL);
        arrn[0] = (int)l2;
        long l3 = (l2 >> 32) + ((0xFFFFFFFFL & (long)arrn[1]) - (l >>> 32));
        arrn[1] = (int)l3;
        if (l3 >> 32 == 0L) {
            return 0;
        }
        return Nat.decAt(n, arrn, 2);
    }

    public static int subDWordFrom(int n, long l, int[] arrn, int n2) {
        long l2 = (0xFFFFFFFFL & (long)arrn[n2 + 0]) - (l & 0xFFFFFFFFL);
        arrn[n2 + 0] = (int)l2;
        long l3 = (l2 >> 32) + ((0xFFFFFFFFL & (long)arrn[n2 + 1]) - (l >>> 32));
        arrn[n2 + 1] = (int)l3;
        if (l3 >> 32 == 0L) {
            return 0;
        }
        return Nat.decAt(n, arrn, n2, 2);
    }

    public static int subFrom(int n, int[] arrn, int n2, int[] arrn2, int n3) {
        long l = 0L;
        for (int i = 0; i < n; ++i) {
            long l2 = l + ((0xFFFFFFFFL & (long)arrn2[n3 + i]) - (0xFFFFFFFFL & (long)arrn[n2 + i]));
            arrn2[n3 + i] = (int)l2;
            l = l2 >> 32;
        }
        return (int)l;
    }

    public static int subFrom(int n, int[] arrn, int[] arrn2) {
        long l = 0L;
        for (int i = 0; i < n; ++i) {
            long l2 = l + ((0xFFFFFFFFL & (long)arrn2[i]) - (0xFFFFFFFFL & (long)arrn[i]));
            arrn2[i] = (int)l2;
            l = l2 >> 32;
        }
        return (int)l;
    }

    public static int subWordAt(int n, int n2, int[] arrn, int n3) {
        long l = (0xFFFFFFFFL & (long)arrn[n3]) - (0xFFFFFFFFL & (long)n2);
        arrn[n3] = (int)l;
        if (l >> 32 == 0L) {
            return 0;
        }
        return Nat.decAt(n, arrn, n3 + 1);
    }

    public static int subWordAt(int n, int n2, int[] arrn, int n3, int n4) {
        long l = (0xFFFFFFFFL & (long)arrn[n3 + n4]) - (0xFFFFFFFFL & (long)n2);
        arrn[n3 + n4] = (int)l;
        if (l >> 32 == 0L) {
            return 0;
        }
        return Nat.decAt(n, arrn, n3, n4 + 1);
    }

    public static int subWordFrom(int n, int n2, int[] arrn) {
        long l = (0xFFFFFFFFL & (long)arrn[0]) - (0xFFFFFFFFL & (long)n2);
        arrn[0] = (int)l;
        if (l >> 32 == 0L) {
            return 0;
        }
        return Nat.decAt(n, arrn, 1);
    }

    public static int subWordFrom(int n, int n2, int[] arrn, int n3) {
        long l = (0xFFFFFFFFL & (long)arrn[n3 + 0]) - (0xFFFFFFFFL & (long)n2);
        arrn[n3 + 0] = (int)l;
        if (l >> 32 == 0L) {
            return 0;
        }
        return Nat.decAt(n, arrn, n3, 1);
    }

    public static BigInteger toBigInteger(int n, int[] arrn) {
        byte[] arrby = new byte[n << 2];
        for (int i = 0; i < n; ++i) {
            int n2 = arrn[i];
            if (n2 == 0) continue;
            Pack.intToBigEndian(n2, arrby, n - 1 - i << 2);
        }
        return new BigInteger(1, arrby);
    }

    public static void zero(int n, int[] arrn) {
        for (int i = 0; i < n; ++i) {
            arrn[i] = 0;
        }
    }
}

