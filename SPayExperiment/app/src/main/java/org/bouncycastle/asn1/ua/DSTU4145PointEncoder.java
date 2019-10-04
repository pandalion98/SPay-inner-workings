/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 *  java.util.Random
 *  org.bouncycastle.math.ec.ECCurve
 *  org.bouncycastle.math.ec.ECFieldElement
 *  org.bouncycastle.math.ec.ECPoint
 */
package org.bouncycastle.asn1.ua;

import java.math.BigInteger;
import java.util.Random;
import org.bouncycastle.math.ec.ECConstants;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.ECPoint;

public abstract class DSTU4145PointEncoder {
    /*
     * Enabled aggressive block sorting
     */
    public static ECPoint decodePoint(ECCurve eCCurve, byte[] arrby) {
        ECFieldElement eCFieldElement;
        ECFieldElement eCFieldElement2 = eCCurve.fromBigInteger(BigInteger.valueOf((long)(1 & arrby[-1 + arrby.length])));
        ECFieldElement eCFieldElement3 = eCCurve.fromBigInteger(new BigInteger(1, arrby));
        if (!DSTU4145PointEncoder.trace(eCFieldElement3).equals((Object)eCCurve.getA())) {
            eCFieldElement3 = eCFieldElement3.addOne();
        }
        if (eCFieldElement3.isZero()) {
            eCFieldElement = eCCurve.getB().sqrt();
        } else {
            ECFieldElement eCFieldElement4 = DSTU4145PointEncoder.solveQuadraticEquation(eCCurve, eCFieldElement3.square().invert().multiply(eCCurve.getB()).add(eCCurve.getA()).add(eCFieldElement3));
            if (eCFieldElement4 != null) {
                if (!DSTU4145PointEncoder.trace(eCFieldElement4).equals((Object)eCFieldElement2)) {
                    eCFieldElement4 = eCFieldElement4.addOne();
                }
                eCFieldElement = eCFieldElement3.multiply(eCFieldElement4);
            } else {
                eCFieldElement = null;
            }
        }
        if (eCFieldElement == null) {
            throw new IllegalArgumentException("Invalid point compression");
        }
        return eCCurve.createPoint(eCFieldElement3.toBigInteger(), eCFieldElement.toBigInteger());
    }

    public static byte[] encodePoint(ECPoint eCPoint) {
        byte[] arrby;
        block3 : {
            block2 : {
                ECPoint eCPoint2 = eCPoint.normalize();
                ECFieldElement eCFieldElement = eCPoint2.getAffineXCoord();
                arrby = eCFieldElement.getEncoded();
                if (eCFieldElement.isZero()) break block2;
                if (!DSTU4145PointEncoder.trace(eCPoint2.getAffineYCoord().divide(eCFieldElement)).isOne()) break block3;
                int n2 = -1 + arrby.length;
                arrby[n2] = (byte)(1 | arrby[n2]);
            }
            return arrby;
        }
        int n3 = -1 + arrby.length;
        arrby[n3] = (byte)(254 & arrby[n3]);
        return arrby;
    }

    private static ECFieldElement solveQuadraticEquation(ECCurve eCCurve, ECFieldElement eCFieldElement) {
        ECFieldElement eCFieldElement2;
        if (eCFieldElement.isZero()) {
            return eCFieldElement;
        }
        ECFieldElement eCFieldElement3 = eCCurve.fromBigInteger(ECConstants.ZERO);
        Random random = new Random();
        int n2 = eCFieldElement.getFieldSize();
        do {
            ECFieldElement eCFieldElement4 = eCCurve.fromBigInteger(new BigInteger(n2, random));
            ECFieldElement eCFieldElement5 = eCFieldElement;
            eCFieldElement2 = eCFieldElement3;
            for (int i2 = 1; i2 <= n2 - 1; ++i2) {
                ECFieldElement eCFieldElement6 = eCFieldElement5.square();
                ECFieldElement eCFieldElement7 = eCFieldElement2.square().add(eCFieldElement6.multiply(eCFieldElement4));
                eCFieldElement5 = eCFieldElement6.add(eCFieldElement);
                eCFieldElement2 = eCFieldElement7;
            }
            if (eCFieldElement5.isZero()) continue;
            return null;
        } while (eCFieldElement2.square().add(eCFieldElement2).isZero());
        return eCFieldElement2;
    }

    private static ECFieldElement trace(ECFieldElement eCFieldElement) {
        ECFieldElement eCFieldElement2 = eCFieldElement;
        for (int i2 = 1; i2 < eCFieldElement.getFieldSize(); ++i2) {
            eCFieldElement2 = eCFieldElement2.square().add(eCFieldElement);
        }
        return eCFieldElement2;
    }
}

