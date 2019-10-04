/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.String
 *  java.math.BigInteger
 */
package org.bouncycastle.math.ec;

import java.math.BigInteger;
import org.bouncycastle.math.ec.AbstractECMultiplier;
import org.bouncycastle.math.ec.ECAlgorithms;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.ECPointMap;
import org.bouncycastle.math.ec.endo.GLVEndomorphism;

public class GLVMultiplier
extends AbstractECMultiplier {
    protected final ECCurve curve;
    protected final GLVEndomorphism glvEndomorphism;

    public GLVMultiplier(ECCurve eCCurve, GLVEndomorphism gLVEndomorphism) {
        if (eCCurve == null || eCCurve.getOrder() == null) {
            throw new IllegalArgumentException("Need curve with known group order");
        }
        this.curve = eCCurve;
        this.glvEndomorphism = gLVEndomorphism;
    }

    @Override
    protected ECPoint multiplyPositive(ECPoint eCPoint, BigInteger bigInteger) {
        if (!this.curve.equals(eCPoint.getCurve())) {
            throw new IllegalStateException();
        }
        BigInteger bigInteger2 = eCPoint.getCurve().getOrder();
        BigInteger[] arrbigInteger = this.glvEndomorphism.decomposeScalar(bigInteger.mod(bigInteger2));
        BigInteger bigInteger3 = arrbigInteger[0];
        BigInteger bigInteger4 = arrbigInteger[1];
        ECPointMap eCPointMap = this.glvEndomorphism.getPointMap();
        if (this.glvEndomorphism.hasEfficientPointMap()) {
            return ECAlgorithms.implShamirsTrickWNaf(eCPoint, bigInteger3, eCPointMap, bigInteger4);
        }
        return ECAlgorithms.implShamirsTrickWNaf(eCPoint, bigInteger3, eCPointMap.map(eCPoint), bigInteger4);
    }
}

