/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.math.BigInteger
 *  java.security.SecureRandom
 *  java.util.Random
 */
package org.bouncycastle.util;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

public final class BigIntegers {
    private static final int MAX_ITERATIONS = 1000;
    private static final BigInteger ZERO = BigInteger.valueOf((long)0L);

    public static byte[] asUnsignedByteArray(int n, BigInteger bigInteger) {
        int n2;
        byte[] arrby = bigInteger.toByteArray();
        if (arrby.length == n) {
            return arrby;
        }
        byte by = arrby[0];
        int n3 = 0;
        if (by == 0) {
            n3 = 1;
        }
        if ((n2 = arrby.length - n3) > n) {
            throw new IllegalArgumentException("standard length exceeded for value");
        }
        byte[] arrby2 = new byte[n];
        System.arraycopy((Object)arrby, (int)n3, (Object)arrby2, (int)(arrby2.length - n2), (int)n2);
        return arrby2;
    }

    public static byte[] asUnsignedByteArray(BigInteger bigInteger) {
        byte[] arrby = bigInteger.toByteArray();
        if (arrby[0] == 0) {
            byte[] arrby2 = new byte[-1 + arrby.length];
            System.arraycopy((Object)arrby, (int)1, (Object)arrby2, (int)0, (int)arrby2.length);
            return arrby2;
        }
        return arrby;
    }

    public static BigInteger createRandomInRange(BigInteger bigInteger, BigInteger bigInteger2, SecureRandom secureRandom) {
        block8 : {
            block7 : {
                block6 : {
                    int n = bigInteger.compareTo(bigInteger2);
                    if (n < 0) break block6;
                    if (n > 0) {
                        throw new IllegalArgumentException("'min' may not be greater than 'max'");
                    }
                    break block7;
                }
                if (bigInteger.bitLength() <= bigInteger2.bitLength() / 2) break block8;
                bigInteger = BigIntegers.createRandomInRange(ZERO, bigInteger2.subtract(bigInteger), secureRandom).add(bigInteger);
            }
            return bigInteger;
        }
        for (int i = 0; i < 1000; ++i) {
            BigInteger bigInteger3 = new BigInteger(bigInteger2.bitLength(), (Random)secureRandom);
            if (bigInteger3.compareTo(bigInteger) < 0 || bigInteger3.compareTo(bigInteger2) > 0) continue;
            return bigInteger3;
        }
        return new BigInteger(-1 + bigInteger2.subtract(bigInteger).bitLength(), (Random)secureRandom).add(bigInteger);
    }

    public static BigInteger fromUnsignedByteArray(byte[] arrby) {
        return new BigInteger(1, arrby);
    }

    public static BigInteger fromUnsignedByteArray(byte[] arrby, int n, int n2) {
        if (n != 0 || n2 != arrby.length) {
            byte[] arrby2 = new byte[n2];
            System.arraycopy((Object)arrby, (int)n, (Object)arrby2, (int)0, (int)n2);
            arrby = arrby2;
        }
        return new BigInteger(1, arrby);
    }
}

