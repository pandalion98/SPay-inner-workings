/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.PrintStream
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 */
package org.bouncycastle.pqc.math.linearalgebra;

import java.io.PrintStream;

public final class PolynomialRingGF2 {
    private PolynomialRingGF2() {
    }

    public static int add(int n, int n2) {
        return n ^ n2;
    }

    public static int degree(int n) {
        int n2 = -1;
        while (n != 0) {
            ++n2;
            n >>>= 1;
        }
        return n2;
    }

    public static int degree(long l) {
        int n = 0;
        while (l != 0L) {
            ++n;
            l >>>= 1;
        }
        return n - 1;
    }

    public static int gcd(int n, int n2) {
        while (n2 != 0) {
            int n3 = PolynomialRingGF2.remainder(n, n2);
            n = n2;
            n2 = n3;
        }
        return n;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static int getIrreduciblePolynomial(int n) {
        if (n < 0) {
            System.err.println("The Degree is negative");
            return 0;
        }
        if (n > 31) {
            System.err.println("The Degree is more then 31");
            return 0;
        }
        if (n == 0) {
            return 1;
        }
        int n2 = 1 + (1 << n);
        int n3 = 1 << n + 1;
        int n4 = n2;
        while (n4 < n3) {
            if (PolynomialRingGF2.isIrreducible(n4)) {
                return n4;
            }
            n4 += 2;
        }
        return 0;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static boolean isIrreducible(int n) {
        if (n != 0) {
            int n2 = PolynomialRingGF2.degree(n) >>> 1;
            int n3 = 2;
            int n4 = 0;
            do {
                if (n4 >= n2) {
                    return true;
                }
                if (PolynomialRingGF2.gcd((n3 = PolynomialRingGF2.modMultiply(n3, n3, n)) ^ 2, n) != 1) break;
                ++n4;
            } while (true);
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static int modMultiply(int n, int n2, int n3) {
        int n4 = PolynomialRingGF2.remainder(n, n3);
        int n5 = PolynomialRingGF2.remainder(n2, n3);
        int n6 = 0;
        if (n5 != 0) {
            int n7 = 1 << PolynomialRingGF2.degree(n3);
            int n8 = n4;
            int n9 = n5;
            while (n8 != 0) {
                int n10 = (byte)(n8 & 1) == 1 ? n6 ^ n9 : n6;
                n8 >>>= 1;
                int n11 = n9 << 1;
                if (n11 >= n7) {
                    n11 ^= n3;
                }
                n9 = n11;
                n6 = n10;
            }
        }
        return n6;
    }

    public static long multiply(int n, int n2) {
        long l = 0L;
        if (n2 != 0) {
            long l2 = 0xFFFFFFFFL & (long)n2;
            while (n != 0) {
                if ((byte)(n & 1) == 1) {
                    l ^= l2;
                }
                n >>>= 1;
                l2 <<= 1;
            }
        }
        return l;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static int remainder(int n, int n2) {
        if (n2 == 0) {
            System.err.println("Error: to be divided by 0");
            return 0;
        }
        while (PolynomialRingGF2.degree(n) >= PolynomialRingGF2.degree(n2)) {
            n ^= n2 << PolynomialRingGF2.degree(n) - PolynomialRingGF2.degree(n2);
        }
        return n;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static int rest(long l, int n) {
        if (n == 0) {
            System.err.println("Error: to be divided by 0");
            return 0;
        }
        long l2 = 0xFFFFFFFFL & (long)n;
        while (l >>> 32 != 0L) {
            l ^= l2 << PolynomialRingGF2.degree(l) - PolynomialRingGF2.degree(l2);
        }
        int n2 = (int)(-1L & l);
        while (PolynomialRingGF2.degree(n2) >= PolynomialRingGF2.degree(n)) {
            n2 ^= n << PolynomialRingGF2.degree(n2) - PolynomialRingGF2.degree(n);
        }
        return n2;
    }
}

