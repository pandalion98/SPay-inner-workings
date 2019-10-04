/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.NullPointerException
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 *  java.security.SecureRandom
 *  org.bouncycastle.util.Arrays
 *  org.bouncycastle.util.BigIntegers
 *  org.bouncycastle.util.Strings
 */
package org.bouncycastle.crypto.agreement.jpake;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.BigIntegers;
import org.bouncycastle.util.Strings;

public class JPAKEUtil {
    static final BigInteger ONE;
    static final BigInteger ZERO;

    static {
        ZERO = BigInteger.valueOf((long)0L);
        ONE = BigInteger.valueOf((long)1L);
    }

    public static BigInteger calculateA(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, BigInteger bigInteger4) {
        return bigInteger3.modPow(bigInteger4, bigInteger);
    }

    public static BigInteger calculateGA(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, BigInteger bigInteger4) {
        return bigInteger2.multiply(bigInteger3).multiply(bigInteger4).mod(bigInteger);
    }

    public static BigInteger calculateGx(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3) {
        return bigInteger2.modPow(bigInteger3, bigInteger);
    }

    private static BigInteger calculateHashForZeroKnowledgeProof(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, String string, Digest digest) {
        digest.reset();
        JPAKEUtil.updateDigestIncludingSize(digest, bigInteger);
        JPAKEUtil.updateDigestIncludingSize(digest, bigInteger2);
        JPAKEUtil.updateDigestIncludingSize(digest, bigInteger3);
        JPAKEUtil.updateDigestIncludingSize(digest, string);
        byte[] arrby = new byte[digest.getDigestSize()];
        digest.doFinal(arrby, 0);
        return new BigInteger(arrby);
    }

    public static BigInteger calculateKeyingMaterial(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, BigInteger bigInteger4, BigInteger bigInteger5, BigInteger bigInteger6) {
        return bigInteger3.modPow(bigInteger4.multiply(bigInteger5).negate().mod(bigInteger2), bigInteger).multiply(bigInteger6).modPow(bigInteger4, bigInteger);
    }

    private static byte[] calculateMacKey(BigInteger bigInteger, Digest digest) {
        digest.reset();
        JPAKEUtil.updateDigest(digest, bigInteger);
        JPAKEUtil.updateDigest(digest, "JPAKE_KC");
        byte[] arrby = new byte[digest.getDigestSize()];
        digest.doFinal(arrby, 0);
        return arrby;
    }

    public static BigInteger calculateMacTag(String string, String string2, BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, BigInteger bigInteger4, BigInteger bigInteger5, Digest digest) {
        byte[] arrby = JPAKEUtil.calculateMacKey(bigInteger5, digest);
        HMac hMac = new HMac(digest);
        byte[] arrby2 = new byte[hMac.getMacSize()];
        hMac.init(new KeyParameter(arrby));
        JPAKEUtil.updateMac((Mac)hMac, "KC_1_U");
        JPAKEUtil.updateMac((Mac)hMac, string);
        JPAKEUtil.updateMac((Mac)hMac, string2);
        JPAKEUtil.updateMac((Mac)hMac, bigInteger);
        JPAKEUtil.updateMac((Mac)hMac, bigInteger2);
        JPAKEUtil.updateMac((Mac)hMac, bigInteger3);
        JPAKEUtil.updateMac((Mac)hMac, bigInteger4);
        hMac.doFinal(arrby2, 0);
        Arrays.fill((byte[])arrby, (byte)0);
        return new BigInteger(arrby2);
    }

    public static BigInteger calculateS(char[] arrc) {
        return new BigInteger(Strings.toUTF8ByteArray((char[])arrc));
    }

    public static BigInteger calculateX2s(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3) {
        return bigInteger2.multiply(bigInteger3).mod(bigInteger);
    }

    public static BigInteger[] calculateZeroKnowledgeProof(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, BigInteger bigInteger4, BigInteger bigInteger5, String string, Digest digest, SecureRandom secureRandom) {
        BigInteger[] arrbigInteger = new BigInteger[2];
        BigInteger bigInteger6 = BigIntegers.createRandomInRange((BigInteger)ZERO, (BigInteger)bigInteger2.subtract(ONE), (SecureRandom)secureRandom);
        BigInteger bigInteger7 = bigInteger3.modPow(bigInteger6, bigInteger);
        BigInteger bigInteger8 = JPAKEUtil.calculateHashForZeroKnowledgeProof(bigInteger3, bigInteger7, bigInteger4, string, digest);
        arrbigInteger[0] = bigInteger7;
        arrbigInteger[1] = bigInteger6.subtract(bigInteger5.multiply(bigInteger8)).mod(bigInteger2);
        return arrbigInteger;
    }

    public static BigInteger generateX1(BigInteger bigInteger, SecureRandom secureRandom) {
        return BigIntegers.createRandomInRange((BigInteger)ZERO, (BigInteger)bigInteger.subtract(ONE), (SecureRandom)secureRandom);
    }

    public static BigInteger generateX2(BigInteger bigInteger, SecureRandom secureRandom) {
        return BigIntegers.createRandomInRange((BigInteger)ONE, (BigInteger)bigInteger.subtract(ONE), (SecureRandom)secureRandom);
    }

    private static byte[] intToByteArray(int n2) {
        byte[] arrby = new byte[]{(byte)(n2 >>> 24), (byte)(n2 >>> 16), (byte)(n2 >>> 8), (byte)n2};
        return arrby;
    }

    private static void updateDigest(Digest digest, String string) {
        byte[] arrby = Strings.toUTF8ByteArray((String)string);
        digest.update(arrby, 0, arrby.length);
        Arrays.fill((byte[])arrby, (byte)0);
    }

    private static void updateDigest(Digest digest, BigInteger bigInteger) {
        byte[] arrby = BigIntegers.asUnsignedByteArray((BigInteger)bigInteger);
        digest.update(arrby, 0, arrby.length);
        Arrays.fill((byte[])arrby, (byte)0);
    }

    private static void updateDigestIncludingSize(Digest digest, String string) {
        byte[] arrby = Strings.toUTF8ByteArray((String)string);
        digest.update(JPAKEUtil.intToByteArray(arrby.length), 0, 4);
        digest.update(arrby, 0, arrby.length);
        Arrays.fill((byte[])arrby, (byte)0);
    }

    private static void updateDigestIncludingSize(Digest digest, BigInteger bigInteger) {
        byte[] arrby = BigIntegers.asUnsignedByteArray((BigInteger)bigInteger);
        digest.update(JPAKEUtil.intToByteArray(arrby.length), 0, 4);
        digest.update(arrby, 0, arrby.length);
        Arrays.fill((byte[])arrby, (byte)0);
    }

    private static void updateMac(Mac mac, String string) {
        byte[] arrby = Strings.toUTF8ByteArray((String)string);
        mac.update(arrby, 0, arrby.length);
        Arrays.fill((byte[])arrby, (byte)0);
    }

    private static void updateMac(Mac mac, BigInteger bigInteger) {
        byte[] arrby = BigIntegers.asUnsignedByteArray((BigInteger)bigInteger);
        mac.update(arrby, 0, arrby.length);
        Arrays.fill((byte[])arrby, (byte)0);
    }

    public static void validateGa(BigInteger bigInteger) {
        if (bigInteger.equals((Object)ONE)) {
            throw new CryptoException("ga is equal to 1.  It should not be.  The chances of this happening are on the order of 2^160 for a 160-bit q.  Try again.");
        }
    }

    public static void validateGx4(BigInteger bigInteger) {
        if (bigInteger.equals((Object)ONE)) {
            throw new CryptoException("g^x validation failed.  g^x should not be 1.");
        }
    }

    public static void validateMacTag(String string, String string2, BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, BigInteger bigInteger4, BigInteger bigInteger5, Digest digest, BigInteger bigInteger6) {
        if (!JPAKEUtil.calculateMacTag(string2, string, bigInteger3, bigInteger4, bigInteger, bigInteger2, bigInteger5, digest).equals((Object)bigInteger6)) {
            throw new CryptoException("Partner MacTag validation failed. Therefore, the password, MAC, or digest algorithm of each participant does not match.");
        }
    }

    public static void validateNotNull(Object object, String string) {
        if (object == null) {
            throw new NullPointerException(string + " must not be null");
        }
    }

    public static void validateParticipantIdsDiffer(String string, String string2) {
        if (string.equals((Object)string2)) {
            throw new CryptoException("Both participants are using the same participantId (" + string + "). This is not allowed. " + "Each participant must use a unique participantId.");
        }
    }

    public static void validateParticipantIdsEqual(String string, String string2) {
        if (!string.equals((Object)string2)) {
            throw new CryptoException("Received payload from incorrect partner (" + string2 + "). Expected to receive payload from " + string + ".");
        }
    }

    public static void validateZeroKnowledgeProof(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, BigInteger bigInteger4, BigInteger[] arrbigInteger, String string, Digest digest) {
        BigInteger bigInteger5 = arrbigInteger[0];
        BigInteger bigInteger6 = arrbigInteger[1];
        BigInteger bigInteger7 = JPAKEUtil.calculateHashForZeroKnowledgeProof(bigInteger3, bigInteger5, bigInteger4, string, digest);
        if (bigInteger4.compareTo(ZERO) != 1 || bigInteger4.compareTo(bigInteger) != -1 || bigInteger4.modPow(bigInteger2, bigInteger).compareTo(ONE) != 0 || bigInteger3.modPow(bigInteger6, bigInteger).multiply(bigInteger4.modPow(bigInteger7, bigInteger)).mod(bigInteger).compareTo(bigInteger5) != 0) {
            throw new CryptoException("Zero-knowledge proof validation failed");
        }
    }
}

