package org.bouncycastle.asn1.ua;

import java.math.BigInteger;
import java.util.Random;
import org.bouncycastle.math.ec.ECConstants;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.ECPoint;

public abstract class DSTU4145PointEncoder {
    public static ECPoint decodePoint(ECCurve eCCurve, byte[] bArr) {
        ECFieldElement sqrt;
        ECFieldElement fromBigInteger = eCCurve.fromBigInteger(BigInteger.valueOf((long) (bArr[bArr.length - 1] & 1)));
        ECFieldElement fromBigInteger2 = eCCurve.fromBigInteger(new BigInteger(1, bArr));
        if (!trace(fromBigInteger2).equals(eCCurve.getA())) {
            fromBigInteger2 = fromBigInteger2.addOne();
        }
        if (fromBigInteger2.isZero()) {
            sqrt = eCCurve.getB().sqrt();
        } else {
            sqrt = solveQuadraticEquation(eCCurve, fromBigInteger2.square().invert().multiply(eCCurve.getB()).add(eCCurve.getA()).add(fromBigInteger2));
            if (sqrt != null) {
                if (!trace(sqrt).equals(fromBigInteger)) {
                    sqrt = sqrt.addOne();
                }
                sqrt = fromBigInteger2.multiply(sqrt);
            } else {
                sqrt = null;
            }
        }
        if (sqrt != null) {
            return eCCurve.createPoint(fromBigInteger2.toBigInteger(), sqrt.toBigInteger());
        }
        throw new IllegalArgumentException("Invalid point compression");
    }

    public static byte[] encodePoint(ECPoint eCPoint) {
        ECPoint normalize = eCPoint.normalize();
        ECFieldElement affineXCoord = normalize.getAffineXCoord();
        byte[] encoded = affineXCoord.getEncoded();
        if (!affineXCoord.isZero()) {
            int length;
            if (trace(normalize.getAffineYCoord().divide(affineXCoord)).isOne()) {
                length = encoded.length - 1;
                encoded[length] = (byte) (encoded[length] | 1);
            } else {
                length = encoded.length - 1;
                encoded[length] = (byte) (encoded[length] & 254);
            }
        }
        return encoded;
    }

    private static ECFieldElement solveQuadraticEquation(ECCurve eCCurve, ECFieldElement eCFieldElement) {
        if (eCFieldElement.isZero()) {
            return eCFieldElement;
        }
        ECFieldElement eCFieldElement2;
        ECFieldElement fromBigInteger = eCCurve.fromBigInteger(ECConstants.ZERO);
        Random random = new Random();
        int fieldSize = eCFieldElement.getFieldSize();
        do {
            ECFieldElement fromBigInteger2 = eCCurve.fromBigInteger(new BigInteger(fieldSize, random));
            int i = 1;
            ECFieldElement eCFieldElement3 = eCFieldElement;
            eCFieldElement2 = fromBigInteger;
            while (i <= fieldSize - 1) {
                eCFieldElement3 = eCFieldElement3.square();
                ECFieldElement add = eCFieldElement2.square().add(eCFieldElement3.multiply(fromBigInteger2));
                eCFieldElement3 = eCFieldElement3.add(eCFieldElement);
                i++;
                eCFieldElement2 = add;
            }
            if (!eCFieldElement3.isZero()) {
                return null;
            }
        } while (eCFieldElement2.square().add(eCFieldElement2).isZero());
        return eCFieldElement2;
    }

    private static ECFieldElement trace(ECFieldElement eCFieldElement) {
        ECFieldElement eCFieldElement2 = eCFieldElement;
        for (int i = 1; i < eCFieldElement.getFieldSize(); i++) {
            eCFieldElement2 = eCFieldElement2.square().add(eCFieldElement);
        }
        return eCFieldElement2;
    }
}
