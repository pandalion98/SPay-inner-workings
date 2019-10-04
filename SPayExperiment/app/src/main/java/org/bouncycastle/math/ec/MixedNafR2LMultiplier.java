/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.String
 *  java.math.BigInteger
 */
package org.bouncycastle.math.ec;

import java.math.BigInteger;
import org.bouncycastle.math.ec.AbstractECMultiplier;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.WNafUtil;

public class MixedNafR2LMultiplier
extends AbstractECMultiplier {
    protected int additionCoord;
    protected int doublingCoord;

    public MixedNafR2LMultiplier() {
        this(2, 4);
    }

    public MixedNafR2LMultiplier(int n, int n2) {
        this.additionCoord = n;
        this.doublingCoord = n2;
    }

    protected ECCurve configureCurve(ECCurve eCCurve, int n) {
        if (eCCurve.getCoordinateSystem() == n) {
            return eCCurve;
        }
        if (!eCCurve.supportsCoordinateSystem(n)) {
            throw new IllegalArgumentException("Coordinate system " + n + " not supported by this curve");
        }
        return eCCurve.configure().setCoordinateSystem(n).create();
    }

    @Override
    protected ECPoint multiplyPositive(ECPoint eCPoint, BigInteger bigInteger) {
        ECCurve eCCurve = eCPoint.getCurve();
        ECCurve eCCurve2 = this.configureCurve(eCCurve, this.additionCoord);
        ECCurve eCCurve3 = this.configureCurve(eCCurve, this.doublingCoord);
        int[] arrn = WNafUtil.generateCompactNaf(bigInteger);
        ECPoint eCPoint2 = eCCurve2.getInfinity();
        ECPoint eCPoint3 = eCCurve3.importPoint(eCPoint);
        ECPoint eCPoint4 = eCPoint2;
        ECPoint eCPoint5 = eCPoint3;
        int n = 0;
        for (int i = 0; i < arrn.length; ++i) {
            int n2 = arrn[i];
            int n3 = n2 >> 16;
            eCPoint5 = eCPoint5.timesPow2(n + (n2 & 65535));
            ECPoint eCPoint6 = eCCurve2.importPoint(eCPoint5);
            if (n3 < 0) {
                eCPoint6 = eCPoint6.negate();
            }
            eCPoint4 = eCPoint4.add(eCPoint6);
            n = 1;
        }
        return eCCurve.importPoint(eCPoint4);
    }
}

