/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 *  java.math.BigInteger
 */
package org.bouncycastle.jce.spec;

import java.math.BigInteger;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;

public class ECNamedCurveParameterSpec
extends ECParameterSpec {
    private String name;

    public ECNamedCurveParameterSpec(String string, ECCurve eCCurve, ECPoint eCPoint, BigInteger bigInteger) {
        super(eCCurve, eCPoint, bigInteger);
        this.name = string;
    }

    public ECNamedCurveParameterSpec(String string, ECCurve eCCurve, ECPoint eCPoint, BigInteger bigInteger, BigInteger bigInteger2) {
        super(eCCurve, eCPoint, bigInteger, bigInteger2);
        this.name = string;
    }

    public ECNamedCurveParameterSpec(String string, ECCurve eCCurve, ECPoint eCPoint, BigInteger bigInteger, BigInteger bigInteger2, byte[] arrby) {
        super(eCCurve, eCPoint, bigInteger, bigInteger2, arrby);
        this.name = string;
    }

    public String getName() {
        return this.name;
    }
}

