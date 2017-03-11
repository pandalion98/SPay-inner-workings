package org.bouncycastle.pqc.math.linearalgebra;

public final class PolynomialRingGF2 {
    private PolynomialRingGF2() {
    }

    public static int add(int i, int i2) {
        return i ^ i2;
    }

    public static int degree(int i) {
        int i2 = -1;
        while (i != 0) {
            i2++;
            i >>>= 1;
        }
        return i2;
    }

    public static int degree(long j) {
        int i = 0;
        while (j != 0) {
            i++;
            j >>>= 1;
        }
        return i - 1;
    }

    public static int gcd(int i, int i2) {
        while (i2 != 0) {
            int remainder = remainder(i, i2);
            i = i2;
            i2 = remainder;
        }
        return i;
    }

    public static int getIrreduciblePolynomial(int i) {
        if (i < 0) {
            System.err.println("The Degree is negative");
            return 0;
        } else if (i > 31) {
            System.err.println("The Degree is more then 31");
            return 0;
        } else if (i == 0) {
            return 1;
        } else {
            int i2 = 1 << (i + 1);
            for (int i3 = (1 << i) + 1; i3 < i2; i3 += 2) {
                if (isIrreducible(i3)) {
                    return i3;
                }
            }
            return 0;
        }
    }

    public static boolean isIrreducible(int i) {
        if (i == 0) {
            return false;
        }
        int degree = degree(i) >>> 1;
        int i2 = 2;
        for (int i3 = 0; i3 < degree; i3++) {
            i2 = modMultiply(i2, i2, i);
            if (gcd(i2 ^ 2, i) != 1) {
                return false;
            }
        }
        return true;
    }

    public static int modMultiply(int i, int i2, int i3) {
        int i4 = 0;
        int remainder = remainder(i, i3);
        int remainder2 = remainder(i2, i3);
        if (remainder2 != 0) {
            int degree = 1 << degree(i3);
            int i5 = remainder;
            remainder = remainder2;
            while (i5 != 0) {
                remainder2 = ((byte) (i5 & 1)) == (byte) 1 ? i4 ^ remainder : i4;
                i5 >>>= 1;
                i4 = remainder << 1;
                if (i4 >= degree) {
                    i4 ^= i3;
                }
                remainder = i4;
                i4 = remainder2;
            }
        }
        return i4;
    }

    public static long multiply(int i, int i2) {
        long j = 0;
        if (i2 != 0) {
            long j2 = ((long) i2) & 4294967295L;
            while (i != 0) {
                if (((byte) (i & 1)) == (byte) 1) {
                    j ^= j2;
                }
                i >>>= 1;
                j2 <<= 1;
            }
        }
        return j;
    }

    public static int remainder(int i, int i2) {
        if (i2 == 0) {
            System.err.println("Error: to be divided by 0");
            return 0;
        }
        while (degree(i) >= degree(i2)) {
            i ^= i2 << (degree(i) - degree(i2));
        }
        return i;
    }

    public static int rest(long j, int i) {
        if (i == 0) {
            System.err.println("Error: to be divided by 0");
            return 0;
        }
        long j2 = ((long) i) & 4294967295L;
        while ((j >>> 32) != 0) {
            j ^= j2 << (degree(j) - degree(j2));
        }
        int i2 = (int) (-1 & j);
        while (degree(i2) >= degree(i)) {
            i2 ^= i << (degree(i2) - degree(i));
        }
        return i2;
    }
}
