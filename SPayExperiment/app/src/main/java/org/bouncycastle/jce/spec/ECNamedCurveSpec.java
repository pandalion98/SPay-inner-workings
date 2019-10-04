/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 *  java.math.BigInteger
 *  java.security.spec.ECField
 *  java.security.spec.ECFieldF2m
 *  java.security.spec.ECFieldFp
 *  java.security.spec.ECParameterSpec
 *  java.security.spec.ECPoint
 *  java.security.spec.EllipticCurve
 */
package org.bouncycastle.jce.spec;

import java.math.BigInteger;
import java.security.spec.ECField;
import java.security.spec.ECFieldF2m;
import java.security.spec.ECFieldFp;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.EllipticCurve;
import org.bouncycastle.math.ec.ECAlgorithms;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.field.FiniteField;

public class ECNamedCurveSpec
extends ECParameterSpec {
    private String name;

    public ECNamedCurveSpec(String string, EllipticCurve ellipticCurve, ECPoint eCPoint, BigInteger bigInteger) {
        super(ellipticCurve, eCPoint, bigInteger, 1);
        this.name = string;
    }

    public ECNamedCurveSpec(String string, EllipticCurve ellipticCurve, ECPoint eCPoint, BigInteger bigInteger, BigInteger bigInteger2) {
        super(ellipticCurve, eCPoint, bigInteger, bigInteger2.intValue());
        this.name = string;
    }

    public ECNamedCurveSpec(String string, ECCurve eCCurve, org.bouncycastle.math.ec.ECPoint eCPoint, BigInteger bigInteger) {
        super(ECNamedCurveSpec.convertCurve(eCCurve, null), ECNamedCurveSpec.convertPoint(eCPoint), bigInteger, 1);
        this.name = string;
    }

    public ECNamedCurveSpec(String string, ECCurve eCCurve, org.bouncycastle.math.ec.ECPoint eCPoint, BigInteger bigInteger, BigInteger bigInteger2) {
        super(ECNamedCurveSpec.convertCurve(eCCurve, null), ECNamedCurveSpec.convertPoint(eCPoint), bigInteger, bigInteger2.intValue());
        this.name = string;
    }

    public ECNamedCurveSpec(String string, ECCurve eCCurve, org.bouncycastle.math.ec.ECPoint eCPoint, BigInteger bigInteger, BigInteger bigInteger2, byte[] arrby) {
        super(ECNamedCurveSpec.convertCurve(eCCurve, arrby), ECNamedCurveSpec.convertPoint(eCPoint), bigInteger, bigInteger2.intValue());
        this.name = string;
    }

    private static EllipticCurve convertCurve(ECCurve eCCurve, byte[] arrby) {
        if (ECAlgorithms.isFpCurve(eCCurve)) {
            return new EllipticCurve((ECField)new ECFieldFp(eCCurve.getField().getCharacteristic()), eCCurve.getA().toBigInteger(), eCCurve.getB().toBigInteger(), arrby);
        }
        ECCurve.F2m f2m = (ECCurve.F2m)eCCurve;
        if (f2m.isTrinomial()) {
            int[] arrn = new int[]{f2m.getK1()};
            return new EllipticCurve((ECField)new ECFieldF2m(f2m.getM(), arrn), eCCurve.getA().toBigInteger(), eCCurve.getB().toBigInteger(), arrby);
        }
        int[] arrn = new int[]{f2m.getK3(), f2m.getK2(), f2m.getK1()};
        return new EllipticCurve((ECField)new ECFieldF2m(f2m.getM(), arrn), eCCurve.getA().toBigInteger(), eCCurve.getB().toBigInteger(), arrby);
    }

    private static ECPoint convertPoint(org.bouncycastle.math.ec.ECPoint eCPoint) {
        org.bouncycastle.math.ec.ECPoint eCPoint2 = eCPoint.normalize();
        return new ECPoint(eCPoint2.getAffineXCoord().toBigInteger(), eCPoint2.getAffineYCoord().toBigInteger());
    }

    public String getName() {
        return this.name;
    }
}

