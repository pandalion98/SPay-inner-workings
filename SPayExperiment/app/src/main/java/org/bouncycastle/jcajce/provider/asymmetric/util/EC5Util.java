/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 *  java.security.spec.ECField
 *  java.security.spec.ECFieldF2m
 *  java.security.spec.ECFieldFp
 *  java.security.spec.ECParameterSpec
 *  java.security.spec.ECPoint
 *  java.security.spec.EllipticCurve
 *  java.util.Enumeration
 *  java.util.HashMap
 *  java.util.Map
 */
package org.bouncycastle.jcajce.provider.asymmetric.util;

import java.math.BigInteger;
import java.security.spec.ECField;
import java.security.spec.ECFieldF2m;
import java.security.spec.ECFieldFp;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.EllipticCurve;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import org.bouncycastle.asn1.x9.ECNamedCurveTable;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.ec.CustomNamedCurves;
import org.bouncycastle.jcajce.provider.asymmetric.util.ECUtil;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.jce.spec.ECNamedCurveSpec;
import org.bouncycastle.math.ec.ECAlgorithms;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.field.FiniteField;

public class EC5Util {
    private static Map customCurves = new HashMap();

    static {
        Enumeration enumeration = CustomNamedCurves.getNames();
        while (enumeration.hasMoreElements()) {
            String string = (String)enumeration.nextElement();
            X9ECParameters x9ECParameters = ECNamedCurveTable.getByName(string);
            if (x9ECParameters == null) continue;
            customCurves.put((Object)x9ECParameters.getCurve(), (Object)CustomNamedCurves.getByName(string).getCurve());
        }
    }

    public static EllipticCurve convertCurve(ECCurve eCCurve, byte[] arrby) {
        if (ECAlgorithms.isFpCurve(eCCurve)) {
            return new EllipticCurve((ECField)new ECFieldFp(eCCurve.getField().getCharacteristic()), eCCurve.getA().toBigInteger(), eCCurve.getB().toBigInteger(), null);
        }
        ECCurve.F2m f2m = (ECCurve.F2m)eCCurve;
        if (f2m.isTrinomial()) {
            int[] arrn = new int[]{f2m.getK1()};
            return new EllipticCurve((ECField)new ECFieldF2m(f2m.getM(), arrn), eCCurve.getA().toBigInteger(), eCCurve.getB().toBigInteger(), null);
        }
        int[] arrn = new int[]{f2m.getK3(), f2m.getK2(), f2m.getK1()};
        return new EllipticCurve((ECField)new ECFieldF2m(f2m.getM(), arrn), eCCurve.getA().toBigInteger(), eCCurve.getB().toBigInteger(), null);
    }

    public static ECCurve convertCurve(EllipticCurve ellipticCurve) {
        ECField eCField = ellipticCurve.getField();
        BigInteger bigInteger = ellipticCurve.getA();
        BigInteger bigInteger2 = ellipticCurve.getB();
        if (eCField instanceof ECFieldFp) {
            ECCurve.Fp fp = new ECCurve.Fp(((ECFieldFp)eCField).getP(), bigInteger, bigInteger2);
            if (customCurves.containsKey((Object)fp)) {
                return (ECCurve)customCurves.get((Object)fp);
            }
            return fp;
        }
        ECFieldF2m eCFieldF2m = (ECFieldF2m)eCField;
        int n = eCFieldF2m.getM();
        int[] arrn = ECUtil.convertMidTerms(eCFieldF2m.getMidTermsOfReductionPolynomial());
        return new ECCurve.F2m(n, arrn[0], arrn[1], arrn[2], bigInteger, bigInteger2);
    }

    public static org.bouncycastle.math.ec.ECPoint convertPoint(ECParameterSpec eCParameterSpec, ECPoint eCPoint, boolean bl) {
        return EC5Util.convertPoint(EC5Util.convertCurve(eCParameterSpec.getCurve()), eCPoint, bl);
    }

    public static org.bouncycastle.math.ec.ECPoint convertPoint(ECCurve eCCurve, ECPoint eCPoint, boolean bl) {
        return eCCurve.createPoint(eCPoint.getAffineX(), eCPoint.getAffineY(), bl);
    }

    public static ECParameterSpec convertSpec(EllipticCurve ellipticCurve, org.bouncycastle.jce.spec.ECParameterSpec eCParameterSpec) {
        if (eCParameterSpec instanceof ECNamedCurveParameterSpec) {
            return new ECNamedCurveSpec(((ECNamedCurveParameterSpec)eCParameterSpec).getName(), ellipticCurve, new ECPoint(eCParameterSpec.getG().getAffineXCoord().toBigInteger(), eCParameterSpec.getG().getAffineYCoord().toBigInteger()), eCParameterSpec.getN(), eCParameterSpec.getH());
        }
        return new ECParameterSpec(ellipticCurve, new ECPoint(eCParameterSpec.getG().getAffineXCoord().toBigInteger(), eCParameterSpec.getG().getAffineYCoord().toBigInteger()), eCParameterSpec.getN(), eCParameterSpec.getH().intValue());
    }

    public static org.bouncycastle.jce.spec.ECParameterSpec convertSpec(ECParameterSpec eCParameterSpec, boolean bl) {
        ECCurve eCCurve = EC5Util.convertCurve(eCParameterSpec.getCurve());
        return new org.bouncycastle.jce.spec.ECParameterSpec(eCCurve, EC5Util.convertPoint(eCCurve, eCParameterSpec.getGenerator(), bl), eCParameterSpec.getOrder(), BigInteger.valueOf((long)eCParameterSpec.getCofactor()), eCParameterSpec.getCurve().getSeed());
    }
}

