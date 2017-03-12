package org.bouncycastle.pqc.crypto.mceliece;

import java.math.BigInteger;
import org.bouncycastle.pqc.math.linearalgebra.BigIntUtils;
import org.bouncycastle.pqc.math.linearalgebra.GF2Vector;
import org.bouncycastle.pqc.math.linearalgebra.IntegerFunctions;

final class Conversions {
    private static final BigInteger ONE;
    private static final BigInteger ZERO;

    static {
        ZERO = BigInteger.valueOf(0);
        ONE = BigInteger.valueOf(1);
    }

    private Conversions() {
    }

    public static byte[] decode(int i, int i2, GF2Vector gF2Vector) {
        if (gF2Vector.getLength() == i && gF2Vector.getHammingWeight() == i2) {
            int[] vecArray = gF2Vector.getVecArray();
            BigInteger binomial = IntegerFunctions.binomial(i, i2);
            BigInteger bigInteger = ZERO;
            int i3 = i;
            int i4 = i2;
            for (int i5 = 0; i5 < i; i5++) {
                binomial = binomial.multiply(BigInteger.valueOf((long) (i3 - i4))).divide(BigInteger.valueOf((long) i3));
                i3--;
                if ((vecArray[i5 >> 5] & (1 << (i5 & 31))) != 0) {
                    bigInteger = bigInteger.add(binomial);
                    i4--;
                    binomial = i3 == i4 ? ONE : binomial.multiply(BigInteger.valueOf((long) (i4 + 1))).divide(BigInteger.valueOf((long) (i3 - i4)));
                }
            }
            return BigIntUtils.toMinimalByteArray(bigInteger);
        }
        throw new IllegalArgumentException("vector has wrong length or hamming weight");
    }

    public static GF2Vector encode(int i, int i2, byte[] bArr) {
        if (i < i2) {
            throw new IllegalArgumentException("n < t");
        }
        BigInteger binomial = IntegerFunctions.binomial(i, i2);
        BigInteger bigInteger = new BigInteger(1, bArr);
        if (bigInteger.compareTo(binomial) >= 0) {
            throw new IllegalArgumentException("Encoded number too large.");
        }
        GF2Vector gF2Vector = new GF2Vector(i);
        int i3 = i;
        int i4 = i2;
        for (int i5 = 0; i5 < i; i5++) {
            binomial = binomial.multiply(BigInteger.valueOf((long) (i3 - i4))).divide(BigInteger.valueOf((long) i3));
            i3--;
            if (binomial.compareTo(bigInteger) <= 0) {
                gF2Vector.setBit(i5);
                bigInteger = bigInteger.subtract(binomial);
                i4--;
                binomial = i3 == i4 ? ONE : binomial.multiply(BigInteger.valueOf((long) (i4 + 1))).divide(BigInteger.valueOf((long) (i3 - i4)));
            }
        }
        return gF2Vector;
    }

    public static byte[] signConversion(int i, int i2, byte[] bArr) {
        int i3 = 8;
        if (i < i2) {
            throw new IllegalArgumentException("n < t");
        }
        int i4;
        int i5;
        BigInteger binomial = IntegerFunctions.binomial(i, i2);
        int bitLength = binomial.bitLength() - 1;
        int i6 = bitLength >> 3;
        bitLength &= 7;
        if (bitLength == 0) {
            i4 = 8;
            i5 = i6 - 1;
        } else {
            i4 = bitLength;
            i5 = i6;
        }
        i6 = i >> 3;
        bitLength = i & 7;
        if (bitLength == 0) {
            bitLength = i6 - 1;
        } else {
            i3 = bitLength;
            bitLength = i6;
        }
        Object obj = new byte[(bitLength + 1)];
        if (bArr.length < obj.length) {
            System.arraycopy(bArr, 0, obj, 0, bArr.length);
            for (i3 = bArr.length; i3 < obj.length; i3++) {
                obj[i3] = null;
            }
        } else {
            System.arraycopy(bArr, 0, obj, 0, bitLength);
            obj[bitLength] = (byte) (((1 << i3) - 1) & bArr[bitLength]);
        }
        BigInteger bigInteger = ZERO;
        i3 = i2;
        int i7 = i;
        BigInteger bigInteger2 = binomial;
        for (int i8 = 0; i8 < i; i8++) {
            bigInteger2 = bigInteger2.multiply(new BigInteger(Integer.toString(i7 - i3))).divide(new BigInteger(Integer.toString(i7)));
            i7--;
            if (((byte) (obj[i8 >>> 3] & (1 << (i8 & 7)))) != null) {
                bigInteger = bigInteger.add(bigInteger2);
                i3--;
                bigInteger2 = i7 == i3 ? ONE : bigInteger2.multiply(new BigInteger(Integer.toString(i3 + 1))).divide(new BigInteger(Integer.toString(i7 - i3)));
            }
        }
        Object obj2 = new byte[(i5 + 1)];
        Object toByteArray = bigInteger.toByteArray();
        if (toByteArray.length < obj2.length) {
            System.arraycopy(toByteArray, 0, obj2, 0, toByteArray.length);
            for (i3 = toByteArray.length; i3 < obj2.length; i3++) {
                obj2[i3] = null;
            }
        } else {
            System.arraycopy(toByteArray, 0, obj2, 0, i5);
            obj2[i5] = (byte) (toByteArray[i5] & ((1 << i4) - 1));
        }
        return obj2;
    }
}
