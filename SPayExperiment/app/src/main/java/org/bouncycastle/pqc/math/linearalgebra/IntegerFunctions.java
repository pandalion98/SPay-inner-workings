/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.PrintStream
 *  java.lang.ArithmeticException
 *  java.lang.Float
 *  java.lang.IllegalArgumentException
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.math.BigInteger
 *  java.security.SecureRandom
 *  java.util.Random
 */
package org.bouncycastle.pqc.math.linearalgebra;

import java.io.PrintStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

public final class IntegerFunctions {
    private static final BigInteger FOUR;
    private static final BigInteger ONE;
    private static final int[] SMALL_PRIMES;
    private static final long SMALL_PRIME_PRODUCT = 152125131763605L;
    private static final BigInteger TWO;
    private static final BigInteger ZERO;
    private static final int[] jacobiTable;
    private static SecureRandom sr;

    static {
        ZERO = BigInteger.valueOf((long)0L);
        ONE = BigInteger.valueOf((long)1L);
        TWO = BigInteger.valueOf((long)2L);
        FOUR = BigInteger.valueOf((long)4L);
        SMALL_PRIMES = new int[]{3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41};
        sr = null;
        jacobiTable = new int[]{0, 1, 0, -1, 0, -1, 0, 1};
    }

    private IntegerFunctions() {
    }

    /*
     * Enabled aggressive block sorting
     */
    public static BigInteger binomial(int n, int n2) {
        BigInteger bigInteger = ONE;
        if (n == 0) {
            if (n2 == 0) return bigInteger;
            return ZERO;
        } else {
            if (n2 > n >>> 1) {
                n2 = n - n2;
            }
            for (int i = 1; i <= n2; ++i) {
                BigInteger bigInteger2 = bigInteger.multiply(BigInteger.valueOf((long)(n - (i - 1)))).divide(BigInteger.valueOf((long)i));
                bigInteger = bigInteger2;
            }
        }
        return bigInteger;
    }

    public static int bitCount(int n) {
        int n2 = 0;
        while (n != 0) {
            n2 += n & 1;
            n >>>= 1;
        }
        return n2;
    }

    public static int ceilLog(int n) {
        int n2 = 0;
        int n3 = 1;
        while (n3 < n) {
            n3 <<= 1;
            ++n2;
        }
        return n2;
    }

    public static int ceilLog(BigInteger bigInteger) {
        int n = 0;
        BigInteger bigInteger2 = ONE;
        while (bigInteger2.compareTo(bigInteger) < 0) {
            ++n;
            bigInteger2 = bigInteger2.shiftLeft(1);
        }
        return n;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static int ceilLog256(int n) {
        if (n == 0) {
            return 1;
        }
        if (n < 0) {
            n = -n;
        }
        int n2 = 0;
        while (n > 0) {
            ++n2;
            n >>>= 8;
        }
        return n2;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static int ceilLog256(long l) {
        if (l == 0L) {
            return 1;
        }
        if (l < 0L) {
            l = -l;
        }
        int n = 0;
        while (l > 0L) {
            ++n;
            l >>>= 8;
        }
        return n;
    }

    public static BigInteger divideAndRound(BigInteger bigInteger, BigInteger bigInteger2) {
        if (bigInteger.signum() < 0) {
            return IntegerFunctions.divideAndRound(bigInteger.negate(), bigInteger2).negate();
        }
        if (bigInteger2.signum() < 0) {
            return IntegerFunctions.divideAndRound(bigInteger, bigInteger2.negate()).negate();
        }
        return bigInteger.shiftLeft(1).add(bigInteger2).divide(bigInteger2.shiftLeft(1));
    }

    public static BigInteger[] divideAndRound(BigInteger[] arrbigInteger, BigInteger bigInteger) {
        BigInteger[] arrbigInteger2 = new BigInteger[arrbigInteger.length];
        for (int i = 0; i < arrbigInteger.length; ++i) {
            arrbigInteger2[i] = IntegerFunctions.divideAndRound(arrbigInteger[i], bigInteger);
        }
        return arrbigInteger2;
    }

    public static int[] extGCD(int n, int n2) {
        BigInteger[] arrbigInteger = IntegerFunctions.extgcd(BigInteger.valueOf((long)n), BigInteger.valueOf((long)n2));
        int[] arrn = new int[]{arrbigInteger[0].intValue(), arrbigInteger[1].intValue(), arrbigInteger[2].intValue()};
        return arrn;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static BigInteger[] extgcd(BigInteger bigInteger, BigInteger bigInteger2) {
        BigInteger bigInteger3;
        BigInteger bigInteger4;
        BigInteger bigInteger5 = ONE;
        BigInteger bigInteger6 = ZERO;
        if (bigInteger2.signum() != 0) {
            BigInteger bigInteger7 = ZERO;
            bigInteger4 = bigInteger;
            bigInteger3 = bigInteger5;
            BigInteger bigInteger8 = bigInteger7;
            BigInteger bigInteger9 = bigInteger2;
            while (bigInteger9.signum() != 0) {
                BigInteger[] arrbigInteger = bigInteger4.divideAndRemainder(bigInteger9);
                BigInteger bigInteger10 = arrbigInteger[0];
                BigInteger bigInteger11 = arrbigInteger[1];
                BigInteger bigInteger12 = bigInteger3.subtract(bigInteger10.multiply(bigInteger8));
                bigInteger4 = bigInteger9;
                bigInteger9 = bigInteger11;
                bigInteger3 = bigInteger8;
                bigInteger8 = bigInteger12;
            }
            bigInteger6 = bigInteger4.subtract(bigInteger.multiply(bigInteger3)).divide(bigInteger2);
            do {
                return new BigInteger[]{bigInteger4, bigInteger3, bigInteger6};
                break;
            } while (true);
        }
        bigInteger4 = bigInteger;
        bigInteger3 = bigInteger5;
        return new BigInteger[]{bigInteger4, bigInteger3, bigInteger6};
    }

    public static float floatLog(float f) {
        double d = (f - 1.0f) / (1.0f + f);
        int n = 1;
        float f2 = (float)d;
        double d2 = d;
        while (d2 > 0.001) {
            f2 = (float)((double)f2 + (d2 *= d * d) * (1.0 / (double)(n += 2)));
        }
        return f2 * 2.0f;
    }

    public static float floatPow(float f, int n) {
        float f2 = 1.0f;
        while (n > 0) {
            f2 *= f;
            --n;
        }
        return f2;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static int floorLog(int n) {
        if (n <= 0) {
            return -1;
        }
        int n2 = n >>> 1;
        int n3 = 0;
        int n4 = n2;
        while (n4 > 0) {
            int n5 = n3 + 1;
            n4 >>>= 1;
            n3 = n5;
        }
        return n3;
    }

    public static int floorLog(BigInteger bigInteger) {
        int n = -1;
        BigInteger bigInteger2 = ONE;
        while (bigInteger2.compareTo(bigInteger) <= 0) {
            ++n;
            bigInteger2 = bigInteger2.shiftLeft(1);
        }
        return n;
    }

    public static int gcd(int n, int n2) {
        return BigInteger.valueOf((long)n).gcd(BigInteger.valueOf((long)n2)).intValue();
    }

    public static float intRoot(int n, int n2) {
        float f = n / n2;
        float f2 = 0.0f;
        int n3 = 0;
        while ((double)Math.abs((float)(f2 - f)) > 1.0E-4) {
            float f3 = IntegerFunctions.floatPow(f, n2);
            while (Float.isInfinite((float)f3)) {
                float f4 = (f + f2) / 2.0f;
                float f5 = IntegerFunctions.floatPow(f4, n2);
                f = f4;
                f3 = f5;
            }
            ++n3;
            float f6 = f - (f3 - (float)n) / ((float)n2 * IntegerFunctions.floatPow(f, n2 - 1));
            float f7 = f;
            f = f6;
            f2 = f7;
        }
        return f;
    }

    public static byte[] integerToOctets(BigInteger bigInteger) {
        byte[] arrby = bigInteger.abs().toByteArray();
        if ((7 & bigInteger.bitLength()) != 0) {
            return arrby;
        }
        byte[] arrby2 = new byte[bigInteger.bitLength() >> 3];
        System.arraycopy((Object)arrby, (int)1, (Object)arrby2, (int)0, (int)arrby2.length);
        return arrby2;
    }

    public static boolean isIncreasing(int[] arrn) {
        int n;
        int n2 = n = 1;
        do {
            block4 : {
                block3 : {
                    if (n2 >= arrn.length) break block3;
                    if (arrn[n2 - 1] < arrn[n2]) break block4;
                    System.out.println("a[" + (n2 - 1) + "] = " + arrn[n2 - 1] + " >= " + arrn[n2] + " = a[" + n2 + "]");
                    n = 0;
                }
                return (boolean)n;
            }
            ++n2;
        } while (true);
    }

    /*
     * Enabled aggressive block sorting
     */
    public static int isPower(int n, int n2) {
        if (n > 0) {
            int n3 = 0;
            do {
                if (n <= 1) {
                    return n3;
                }
                if (n % n2 != 0) break;
                n /= n2;
                ++n3;
            } while (true);
        }
        return -1;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static boolean isPrime(int n) {
        block7 : {
            block6 : {
                if (n < 2) break block6;
                if (n == 2) {
                    return true;
                }
                if ((n & 1) == 0) break block6;
                if (n < 42) {
                    for (int i = 0; i < SMALL_PRIMES.length; ++i) {
                        if (n != SMALL_PRIMES[i]) continue;
                        return true;
                    }
                }
                if (n % 3 != 0 && n % 5 != 0 && n % 7 != 0 && n % 11 != 0 && n % 13 != 0 && n % 17 != 0 && n % 19 != 0 && n % 23 != 0 && n % 29 != 0 && n % 31 != 0 && n % 37 != 0 && n % 41 != 0) break block7;
            }
            return false;
        }
        return BigInteger.valueOf((long)n).isProbablePrime(20);
    }

    /*
     * Enabled aggressive block sorting
     */
    public static int jacobi(BigInteger bigInteger, BigInteger bigInteger2) {
        int n;
        int n2 = 1;
        long l = 1L;
        if (bigInteger2.equals((Object)ZERO)) {
            if (bigInteger.abs().equals((Object)ONE)) return n2;
            return 0;
        }
        if (!bigInteger.testBit(0)) {
            boolean bl = bigInteger2.testBit(0);
            n = 0;
            if (!bl) return n;
        }
        if (bigInteger2.signum() == -1) {
            bigInteger2 = bigInteger2.negate();
            if (bigInteger.signum() == -1) {
                l = -1L;
            }
        }
        BigInteger bigInteger3 = ZERO;
        while (!bigInteger2.testBit(0)) {
            bigInteger3 = bigInteger3.add(ONE);
            bigInteger2 = bigInteger2.divide(TWO);
        }
        if (bigInteger3.testBit(0)) {
            l *= (long)jacobiTable[7 & bigInteger.intValue()];
        }
        if (bigInteger.signum() < 0) {
            if (bigInteger2.testBit(n2)) {
                l = -l;
            }
            bigInteger = bigInteger.negate();
        }
        while (bigInteger.signum() != 0) {
            BigInteger bigInteger4 = ZERO;
            while (!bigInteger.testBit(0)) {
                bigInteger4 = bigInteger4.add(ONE);
                bigInteger = bigInteger.divide(TWO);
            }
            if (bigInteger4.testBit(0)) {
                l *= (long)jacobiTable[7 & bigInteger2.intValue()];
            }
            if (bigInteger.compareTo(bigInteger2) < 0) {
                if (bigInteger2.testBit(n2) && bigInteger.testBit(n2)) {
                    l = -l;
                    BigInteger bigInteger5 = bigInteger;
                    bigInteger = bigInteger2;
                    bigInteger2 = bigInteger5;
                } else {
                    BigInteger bigInteger6 = bigInteger;
                    bigInteger = bigInteger2;
                    bigInteger2 = bigInteger6;
                }
            }
            bigInteger = bigInteger.subtract(bigInteger2);
        }
        boolean bl = bigInteger2.equals((Object)ONE);
        n = 0;
        if (!bl) return n;
        return (int)l;
    }

    public static BigInteger leastCommonMultiple(BigInteger[] arrbigInteger) {
        int n = arrbigInteger.length;
        BigInteger bigInteger = arrbigInteger[0];
        for (int i = 1; i < n; ++i) {
            BigInteger bigInteger2 = bigInteger.gcd(arrbigInteger[i]);
            bigInteger = bigInteger.multiply(arrbigInteger[i]).divide(bigInteger2);
        }
        return bigInteger;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static int leastDiv(int n) {
        int n2 = n < 0 ? -n : n;
        if (n2 == 0) {
            return 1;
        }
        if ((n2 & 1) == 0) {
            return 2;
        }
        int n3 = 3;
        while (n3 <= n2 / n3) {
            if (n2 % n3 == 0) return n3;
            n3 += 2;
        }
        return n2;
    }

    public static double log(double d) {
        if (d > 0.0 && d < 1.0) {
            return -IntegerFunctions.log(1.0 / d);
        }
        int n = 0;
        double d2 = 1.0;
        double d3 = d;
        while (d3 > 2.0) {
            d3 /= 2.0;
            ++n;
            d2 *= 2.0;
        }
        return IntegerFunctions.logBKM(d / d2) + (double)n;
    }

    public static double log(long l) {
        int n = IntegerFunctions.floorLog(BigInteger.valueOf((long)l));
        long l2 = 1 << n;
        return IntegerFunctions.logBKM((double)l / (double)l2) + (double)n;
    }

    /*
     * Enabled aggressive block sorting
     */
    private static double logBKM(double d) {
        double[] arrd = new double[]{1.0, 0.5849625007211562, 0.32192809488736235, 0.16992500144231237, 0.0874628412503394, 0.044394119358453436, 0.02236781302845451, 0.01122725542325412, 0.005624549193878107, 0.0028150156070540383, 0.0014081943928083889, 7.042690112466433E-4, 3.5217748030102726E-4, 1.7609948644250602E-4, 8.80524301221769E-5, 4.4026886827316716E-5, 2.2013611360340496E-5, 1.1006847667481442E-5, 5.503434330648604E-6, 2.751719789561283E-6, 1.375860550841138E-6, 6.879304394358497E-7, 3.4396526072176454E-7, 1.7198264061184464E-7, 8.599132286866321E-8, 4.299566207501687E-8, 2.1497831197679756E-8, 1.0748915638882709E-8, 5.374457829452062E-9, 2.687228917228708E-9, 1.3436144592400231E-9, 6.718072297764289E-10, 3.3590361492731876E-10, 1.6795180747343547E-10, 8.397590373916176E-11, 4.1987951870191886E-11, 2.0993975935248694E-11, 1.0496987967662534E-11, 5.2484939838408146E-12, 2.624246991922794E-12, 1.3121234959619935E-12, 6.56061747981146E-13, 3.2803087399061026E-13, 1.6401543699531447E-13, 8.200771849765956E-14, 4.1003859248830365E-14, 2.0501929624415328E-14, 1.02509648122077E-14, 5.1254824061038595E-15, 2.5627412030519317E-15, 1.2813706015259665E-15, 6.406853007629834E-16, 3.203426503814917E-16, 1.6017132519074588E-16, 8.008566259537294E-17, 4.004283129768647E-17, 2.0021415648843235E-17, 1.0010707824421618E-17, 5.005353912210809E-18, 2.5026769561054044E-18, 1.2513384780527022E-18, 6.256692390263511E-19, 3.1283461951317555E-19, 1.5641730975658778E-19, 7.820865487829389E-20, 3.9104327439146944E-20, 1.9552163719573472E-20, 9.776081859786736E-21, 4.888040929893368E-21, 2.444020464946684E-21, 1.222010232473342E-21, 6.11005116236671E-22, 3.055025581183355E-22, 1.5275127905916775E-22, 7.637563952958387E-23, 3.818781976479194E-23, 1.909390988239597E-23, 9.546954941197984E-24, 4.773477470598992E-24, 2.386738735299496E-24, 1.193369367649748E-24, 5.96684683824874E-25, 2.98342341912437E-25, 1.491711709562185E-25, 7.458558547810925E-26, 3.7292792739054626E-26, 1.8646396369527313E-26, 9.323198184763657E-27, 4.661599092381828E-27, 2.330799546190914E-27, 1.165399773095457E-27, 5.826998865477285E-28, 2.9134994327386427E-28, 1.4567497163693213E-28, 7.283748581846607E-29, 3.6418742909233034E-29, 1.8209371454616517E-29, 9.104685727308258E-30, 4.552342863654129E-30, 2.2761714318270646E-30};
        int n = 0;
        double d2 = 1.0;
        double d3 = 0.0;
        double d4 = 1.0;
        while (n < 53) {
            double d5 = d4 + d4 * d2;
            if (d5 <= d) {
                d3 += arrd[n];
            } else {
                d5 = d4;
            }
            d2 *= 0.5;
            ++n;
            d4 = d5;
        }
        return d3;
    }

    public static void main(String[] arrstring) {
        System.out.println("test");
        System.out.println(IntegerFunctions.floatLog(10.0f));
        System.out.println("test2");
    }

    public static int maxPower(int n) {
        int n2 = 0;
        if (n != 0) {
            int n3 = 1;
            while ((n & n3) == 0) {
                int n4 = n2 + 1;
                n3 <<= 1;
                n2 = n4;
            }
        }
        return n2;
    }

    public static long mod(long l, long l2) {
        long l3 = l % l2;
        if (l3 < 0L) {
            l3 += l2;
        }
        return l3;
    }

    public static int modInverse(int n, int n2) {
        return BigInteger.valueOf((long)n).modInverse(BigInteger.valueOf((long)n2)).intValue();
    }

    public static long modInverse(long l, long l2) {
        return BigInteger.valueOf((long)l).modInverse(BigInteger.valueOf((long)l2)).longValue();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static int modPow(int n, int n2, int n3) {
        if (n3 <= 0) return 0;
        if (n3 * n3 > Integer.MAX_VALUE) return 0;
        if (n2 < 0) {
            return 0;
        }
        int n4 = (n3 + n % n3) % n3;
        int n5 = 1;
        while (n2 > 0) {
            if ((n2 & 1) == 1) {
                n5 = n5 * n4 % n3;
            }
            n4 = n4 * n4 % n3;
            n2 >>>= 1;
        }
        return n5;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static BigInteger nextPrime(long l) {
        boolean bl = false;
        long l2 = 0L;
        if (l <= 1L) {
            return BigInteger.valueOf((long)2L);
        }
        if (l == 2L) {
            return BigInteger.valueOf((long)3L);
        }
        for (long i = 1L + l + (1L & l); i <= l << 1 && !bl; i += 2L) {
            long l3;
            boolean bl2;
            boolean bl3 = bl;
            for (long j = 3L; j <= i >> 1 && !bl3; j += 2L) {
                if (i % j != 0L) continue;
                bl3 = true;
            }
            if (bl3) {
                long l4 = l2;
                bl2 = false;
                l3 = l4;
            } else {
                bl2 = true;
                l3 = i;
            }
            bl = bl2;
            l2 = l3;
        }
        return BigInteger.valueOf((long)l2);
    }

    public static BigInteger nextProbablePrime(BigInteger bigInteger) {
        return IntegerFunctions.nextProbablePrime(bigInteger, 20);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static BigInteger nextProbablePrime(BigInteger bigInteger, int n) {
        if (bigInteger.signum() < 0) return TWO;
        if (bigInteger.signum() == 0) return TWO;
        if (bigInteger.equals((Object)ONE)) {
            return TWO;
        }
        BigInteger bigInteger2 = bigInteger.add(ONE);
        if (!bigInteger2.testBit(0)) {
            bigInteger2 = bigInteger2.add(ONE);
        }
        do {
            long l;
            if (bigInteger2.bitLength() > 6 && ((l = bigInteger2.remainder(BigInteger.valueOf((long)152125131763605L)).longValue()) % 3L == 0L || l % 5L == 0L || l % 7L == 0L || l % 11L == 0L || l % 13L == 0L || l % 17L == 0L || l % 19L == 0L || l % 23L == 0L || l % 29L == 0L || l % 31L == 0L || l % 37L == 0L || l % 41L == 0L)) {
                bigInteger2 = bigInteger2.add(TWO);
                continue;
            }
            if (bigInteger2.bitLength() < 4) return bigInteger2;
            if (bigInteger2.isProbablePrime(n)) return bigInteger2;
            bigInteger2 = bigInteger2.add(TWO);
        } while (true);
    }

    /*
     * Enabled aggressive block sorting
     */
    public static int nextSmallerPrime(int n) {
        if (n <= 2) {
            return 1;
        }
        if (n == 3) {
            return 2;
        }
        int n2 = (n & 1) == 0 ? n - 1 : n - 2;
        boolean bl;
        boolean bl2;
        while ((bl = n2 > 3) & (bl2 = !IntegerFunctions.isPrime(n2))) {
            n2 -= 2;
        }
        return n2;
    }

    public static BigInteger octetsToInteger(byte[] arrby) {
        return IntegerFunctions.octetsToInteger(arrby, 0, arrby.length);
    }

    public static BigInteger octetsToInteger(byte[] arrby, int n, int n2) {
        byte[] arrby2 = new byte[n2 + 1];
        arrby2[0] = 0;
        System.arraycopy((Object)arrby, (int)n, (Object)arrby2, (int)1, (int)n2);
        return new BigInteger(arrby2);
    }

    /*
     * Enabled aggressive block sorting
     */
    public static int order(int n, int n2) {
        int n3 = n % n2;
        if (n3 == 0) {
            throw new IllegalArgumentException(n + " is not an element of Z/(" + n2 + "Z)^*; it is not meaningful to compute its order.");
        }
        int n4 = 1;
        while (n3 != 1) {
            if ((n3 = n3 * n % n2) < 0) {
                n3 += n2;
            }
            ++n4;
        }
        return n4;
    }

    public static boolean passesSmallPrimeTest(BigInteger bigInteger) {
        int[] arrn = new int[]{2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199, 211, 223, 227, 229, 233, 239, 241, 251, 257, 263, 269, 271, 277, 281, 283, 293, 307, 311, 313, 317, 331, 337, 347, 349, 353, 359, 367, 373, 379, 383, 389, 397, 401, 409, 419, 421, 431, 433, 439, 443, 449, 457, 461, 463, 467, 479, 487, 491, 499, 503, 509, 521, 523, 541, 547, 557, 563, 569, 571, 577, 587, 593, 599, 601, 607, 613, 617, 619, 631, 641, 643, 647, 653, 659, 661, 673, 677, 683, 691, 701, 709, 719, 727, 733, 739, 743, 751, 757, 761, 769, 773, 787, 797, 809, 811, 821, 823, 827, 829, 839, 853, 857, 859, 863, 877, 881, 883, 887, 907, 911, 919, 929, 937, 941, 947, 953, 967, 971, 977, 983, 991, 997, 1009, 1013, 1019, 1021, 1031, 1033, 1039, 1049, 1051, 1061, 1063, 1069, 1087, 1091, 1093, 1097, 1103, 1109, 1117, 1123, 1129, 1151, 1153, 1163, 1171, 1181, 1187, 1193, 1201, 1213, 1217, 1223, 1229, 1231, 1237, 1249, 1259, 1277, 1279, 1283, 1289, 1291, 1297, 1301, 1303, 1307, 1319, 1321, 1327, 1361, 1367, 1373, 1381, 1399, 1409, 1423, 1427, 1429, 1433, 1439, 1447, 1451, 1453, 1459, 1471, 1481, 1483, 1487, 1489, 1493, 1499};
        for (int i = 0; i < arrn.length; ++i) {
            if (!bigInteger.mod(BigInteger.valueOf((long)arrn[i])).equals((Object)ZERO)) continue;
            return false;
        }
        return true;
    }

    public static int pow(int n, int n2) {
        int n3 = 1;
        while (n2 > 0) {
            if ((n2 & 1) == 1) {
                n3 *= n;
            }
            n *= n;
            n2 >>>= 1;
        }
        return n3;
    }

    public static long pow(long l, int n) {
        long l2 = 1L;
        while (n > 0) {
            if ((n & 1) == 1) {
                l2 *= l;
            }
            l *= l;
            n >>>= 1;
        }
        return l2;
    }

    public static BigInteger randomize(BigInteger bigInteger) {
        if (sr == null) {
            sr = new SecureRandom();
        }
        return IntegerFunctions.randomize(bigInteger, sr);
    }

    /*
     * Enabled aggressive block sorting
     */
    public static BigInteger randomize(BigInteger bigInteger, SecureRandom secureRandom) {
        int n = bigInteger.bitLength();
        BigInteger bigInteger2 = BigInteger.valueOf((long)0L);
        SecureRandom secureRandom2 = secureRandom == null ? (sr != null ? sr : new SecureRandom()) : secureRandom;
        BigInteger bigInteger3 = bigInteger2;
        int n2 = 0;
        while (n2 < 20) {
            bigInteger3 = new BigInteger(n, (Random)secureRandom2);
            if (bigInteger3.compareTo(bigInteger) < 0) {
                return bigInteger3;
            }
            ++n2;
        }
        return bigInteger3.mod(bigInteger);
    }

    public static BigInteger reduceInto(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3) {
        return bigInteger.subtract(bigInteger2).mod(bigInteger3.subtract(bigInteger2)).add(bigInteger2);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static BigInteger ressol(BigInteger bigInteger, BigInteger bigInteger2) {
        if (bigInteger.compareTo(ZERO) < 0) {
            bigInteger = bigInteger.add(bigInteger2);
        }
        if (bigInteger.equals((Object)ZERO)) {
            return ZERO;
        }
        if (bigInteger2.equals((Object)TWO)) return bigInteger;
        if (bigInteger2.testBit(0) && bigInteger2.testBit(1)) {
            if (IntegerFunctions.jacobi(bigInteger, bigInteger2) != 1) throw new IllegalArgumentException("No quadratic residue: " + (Object)bigInteger + ", " + (Object)bigInteger2);
            return bigInteger.modPow(bigInteger2.add(ONE).shiftRight(2), bigInteger2);
        }
        BigInteger bigInteger3 = bigInteger2.subtract(ONE);
        long l = 0L;
        while (!bigInteger3.testBit(0)) {
            ++l;
            bigInteger3 = bigInteger3.shiftRight(1);
        }
        BigInteger bigInteger4 = bigInteger3.subtract(ONE).shiftRight(1);
        BigInteger bigInteger5 = bigInteger.modPow(bigInteger4, bigInteger2);
        BigInteger bigInteger6 = bigInteger5.multiply(bigInteger5).remainder(bigInteger2).multiply(bigInteger).remainder(bigInteger2);
        BigInteger bigInteger7 = bigInteger5.multiply(bigInteger).remainder(bigInteger2);
        if (bigInteger6.equals((Object)ONE)) {
            return bigInteger7;
        }
        BigInteger bigInteger8 = TWO;
        while (IntegerFunctions.jacobi(bigInteger8, bigInteger2) == 1) {
            bigInteger8 = bigInteger8.add(ONE);
        }
        BigInteger bigInteger9 = bigInteger8.modPow(bigInteger4.multiply(TWO).add(ONE), bigInteger2);
        BigInteger bigInteger10 = bigInteger7;
        long l2 = l;
        BigInteger bigInteger11 = bigInteger9;
        BigInteger bigInteger12 = bigInteger6;
        while (bigInteger12.compareTo(ONE) == 1) {
            long l3 = 0L;
            BigInteger bigInteger13 = bigInteger12;
            while (!bigInteger13.equals((Object)ONE)) {
                bigInteger13 = bigInteger13.multiply(bigInteger13).mod(bigInteger2);
                ++l3;
            }
            long l4 = l2 - l3;
            if (l4 == 0L) {
                throw new IllegalArgumentException("No quadratic residue: " + (Object)bigInteger + ", " + (Object)bigInteger2);
            }
            BigInteger bigInteger14 = ONE;
            for (long i = 0L; i < l4 - 1L; ++i) {
                bigInteger14 = bigInteger14.shiftLeft(1);
            }
            BigInteger bigInteger15 = bigInteger11.modPow(bigInteger14, bigInteger2);
            bigInteger10 = bigInteger10.multiply(bigInteger15).remainder(bigInteger2);
            bigInteger11 = bigInteger15.multiply(bigInteger15).remainder(bigInteger2);
            bigInteger12 = bigInteger12.multiply(bigInteger11).mod(bigInteger2);
            l2 = l3;
        }
        return bigInteger10;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static BigInteger squareRoot(BigInteger bigInteger) {
        BigInteger bigInteger2;
        int n;
        if (bigInteger.compareTo(ZERO) < 0) {
            throw new ArithmeticException("cannot extract root of negative number" + (Object)bigInteger + ".");
        }
        int n2 = bigInteger.bitLength();
        BigInteger bigInteger3 = ZERO;
        BigInteger bigInteger4 = ZERO;
        if ((n2 & 1) != 0) {
            BigInteger bigInteger5 = bigInteger3.add(ONE);
            n = n2 - 1;
            bigInteger2 = bigInteger5;
        } else {
            n = n2;
            bigInteger2 = bigInteger3;
        }
        while (n > 0) {
            BigInteger bigInteger6 = bigInteger4.multiply(FOUR);
            int n3 = n - 1;
            int n4 = bigInteger.testBit(n3) ? 2 : 0;
            int n5 = n3 - 1;
            int n6 = bigInteger.testBit(n5) ? 1 : 0;
            bigInteger4 = bigInteger6.add(BigInteger.valueOf((long)(n4 + n6)));
            BigInteger bigInteger7 = bigInteger2.multiply(FOUR).add(ONE);
            BigInteger bigInteger8 = bigInteger2.multiply(TWO);
            if (bigInteger4.compareTo(bigInteger7) != -1) {
                BigInteger bigInteger9 = bigInteger8.add(ONE);
                bigInteger4 = bigInteger4.subtract(bigInteger7);
                bigInteger2 = bigInteger9;
                n = n5;
                continue;
            }
            bigInteger2 = bigInteger8;
            n = n5;
        }
        return bigInteger2;
    }
}

