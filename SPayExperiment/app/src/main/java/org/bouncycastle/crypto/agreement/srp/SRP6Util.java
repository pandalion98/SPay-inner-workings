/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.math.BigInteger
 *  java.security.SecureRandom
 *  org.bouncycastle.util.BigIntegers
 */
package org.bouncycastle.crypto.agreement.srp;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.util.BigIntegers;

public class SRP6Util {
    private static BigInteger ONE;
    private static BigInteger ZERO;

    static {
        ZERO = BigInteger.valueOf((long)0L);
        ONE = BigInteger.valueOf((long)1L);
    }

    public static BigInteger calculateK(Digest digest, BigInteger bigInteger, BigInteger bigInteger2) {
        return SRP6Util.hashPaddedPair(digest, bigInteger, bigInteger, bigInteger2);
    }

    public static BigInteger calculateKey(Digest digest, BigInteger bigInteger, BigInteger bigInteger2) {
        byte[] arrby = SRP6Util.getPadded(bigInteger2, (7 + bigInteger.bitLength()) / 8);
        digest.update(arrby, 0, arrby.length);
        byte[] arrby2 = new byte[digest.getDigestSize()];
        digest.doFinal(arrby2, 0);
        return new BigInteger(1, arrby2);
    }

    public static BigInteger calculateM1(Digest digest, BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, BigInteger bigInteger4) {
        return SRP6Util.hashPaddedTriplet(digest, bigInteger, bigInteger2, bigInteger3, bigInteger4);
    }

    public static BigInteger calculateM2(Digest digest, BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, BigInteger bigInteger4) {
        return SRP6Util.hashPaddedTriplet(digest, bigInteger, bigInteger2, bigInteger3, bigInteger4);
    }

    public static BigInteger calculateU(Digest digest, BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3) {
        return SRP6Util.hashPaddedPair(digest, bigInteger, bigInteger2, bigInteger3);
    }

    public static BigInteger calculateX(Digest digest, BigInteger bigInteger, byte[] arrby, byte[] arrby2, byte[] arrby3) {
        byte[] arrby4 = new byte[digest.getDigestSize()];
        digest.update(arrby2, 0, arrby2.length);
        digest.update((byte)58);
        digest.update(arrby3, 0, arrby3.length);
        digest.doFinal(arrby4, 0);
        digest.update(arrby, 0, arrby.length);
        digest.update(arrby4, 0, arrby4.length);
        digest.doFinal(arrby4, 0);
        return new BigInteger(1, arrby4);
    }

    public static BigInteger generatePrivateValue(Digest digest, BigInteger bigInteger, BigInteger bigInteger2, SecureRandom secureRandom) {
        int n2 = Math.min((int)256, (int)(bigInteger.bitLength() / 2));
        return BigIntegers.createRandomInRange((BigInteger)ONE.shiftLeft(n2 - 1), (BigInteger)bigInteger.subtract(ONE), (SecureRandom)secureRandom);
    }

    private static byte[] getPadded(BigInteger bigInteger, int n2) {
        byte[] arrby = BigIntegers.asUnsignedByteArray((BigInteger)bigInteger);
        if (arrby.length < n2) {
            byte[] arrby2 = new byte[n2];
            System.arraycopy((Object)arrby, (int)0, (Object)arrby2, (int)(n2 - arrby.length), (int)arrby.length);
            return arrby2;
        }
        return arrby;
    }

    private static BigInteger hashPaddedPair(Digest digest, BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3) {
        int n2 = (7 + bigInteger.bitLength()) / 8;
        byte[] arrby = SRP6Util.getPadded(bigInteger2, n2);
        byte[] arrby2 = SRP6Util.getPadded(bigInteger3, n2);
        digest.update(arrby, 0, arrby.length);
        digest.update(arrby2, 0, arrby2.length);
        byte[] arrby3 = new byte[digest.getDigestSize()];
        digest.doFinal(arrby3, 0);
        return new BigInteger(1, arrby3);
    }

    private static BigInteger hashPaddedTriplet(Digest digest, BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, BigInteger bigInteger4) {
        int n2 = (7 + bigInteger.bitLength()) / 8;
        byte[] arrby = SRP6Util.getPadded(bigInteger2, n2);
        byte[] arrby2 = SRP6Util.getPadded(bigInteger3, n2);
        byte[] arrby3 = SRP6Util.getPadded(bigInteger4, n2);
        digest.update(arrby, 0, arrby.length);
        digest.update(arrby2, 0, arrby2.length);
        digest.update(arrby3, 0, arrby3.length);
        byte[] arrby4 = new byte[digest.getDigestSize()];
        digest.doFinal(arrby4, 0);
        return new BigInteger(1, arrby4);
    }

    public static BigInteger validatePublicValue(BigInteger bigInteger, BigInteger bigInteger2) {
        BigInteger bigInteger3 = bigInteger2.mod(bigInteger);
        if (bigInteger3.equals((Object)ZERO)) {
            throw new CryptoException("Invalid public value: 0");
        }
        return bigInteger3;
    }
}

