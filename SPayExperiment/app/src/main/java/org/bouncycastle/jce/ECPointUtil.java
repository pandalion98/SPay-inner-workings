/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.math.BigInteger
 *  java.security.spec.ECField
 *  java.security.spec.ECFieldF2m
 *  java.security.spec.ECFieldFp
 *  java.security.spec.ECPoint
 *  java.security.spec.EllipticCurve
 */
package org.bouncycastle.jce;

import java.math.BigInteger;
import java.security.spec.ECField;
import java.security.spec.ECFieldF2m;
import java.security.spec.ECFieldFp;
import java.security.spec.ECPoint;
import java.security.spec.EllipticCurve;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement;

public class ECPointUtil {
    /*
     * Enabled aggressive block sorting
     */
    public static ECPoint decodePoint(EllipticCurve ellipticCurve, byte[] arrby) {
        void var3_3;
        if (ellipticCurve.getField() instanceof ECFieldFp) {
            ECCurve.Fp fp = new ECCurve.Fp(((ECFieldFp)ellipticCurve.getField()).getP(), ellipticCurve.getA(), ellipticCurve.getB());
        } else {
            int[] arrn = ((ECFieldF2m)ellipticCurve.getField()).getMidTermsOfReductionPolynomial();
            if (arrn.length == 3) {
                ECCurve.F2m f2m = new ECCurve.F2m(((ECFieldF2m)ellipticCurve.getField()).getM(), arrn[2], arrn[1], arrn[0], ellipticCurve.getA(), ellipticCurve.getB());
            } else {
                ECCurve.F2m f2m = new ECCurve.F2m(((ECFieldF2m)ellipticCurve.getField()).getM(), arrn[0], ellipticCurve.getA(), ellipticCurve.getB());
            }
        }
        org.bouncycastle.math.ec.ECPoint eCPoint = var3_3.decodePoint(arrby);
        return new ECPoint(eCPoint.getAffineXCoord().toBigInteger(), eCPoint.getAffineYCoord().toBigInteger());
    }
}

