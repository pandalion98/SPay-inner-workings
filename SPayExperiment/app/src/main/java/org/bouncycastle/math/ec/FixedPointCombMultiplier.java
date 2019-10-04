/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalStateException
 *  java.lang.String
 *  java.math.BigInteger
 */
package org.bouncycastle.math.ec;

import java.math.BigInteger;
import org.bouncycastle.math.ec.AbstractECMultiplier;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.FixedPointPreCompInfo;
import org.bouncycastle.math.ec.FixedPointUtil;

public class FixedPointCombMultiplier
extends AbstractECMultiplier {
    protected int getWidthForCombSize(int n) {
        if (n > 257) {
            return 6;
        }
        return 5;
    }

    @Override
    protected ECPoint multiplyPositive(ECPoint eCPoint, BigInteger bigInteger) {
        ECCurve eCCurve = eCPoint.getCurve();
        int n = FixedPointUtil.getCombSize(eCCurve);
        if (bigInteger.bitLength() > n) {
            throw new IllegalStateException("fixed-point comb doesn't support scalars larger than the curve order");
        }
        FixedPointPreCompInfo fixedPointPreCompInfo = FixedPointUtil.precompute(eCPoint, this.getWidthForCombSize(n));
        ECPoint[] arreCPoint = fixedPointPreCompInfo.getPreComp();
        int n2 = fixedPointPreCompInfo.getWidth();
        int n3 = (-1 + (n + n2)) / n2;
        ECPoint eCPoint2 = eCCurve.getInfinity();
        int n4 = -1 + n3 * n2;
        ECPoint eCPoint3 = eCPoint2;
        for (int i = 0; i < n3; ++i) {
            int n5 = 0;
            for (int j = n4 - i; j >= 0; j -= n3) {
                n5 <<= 1;
                if (!bigInteger.testBit(j)) continue;
                n5 |= 1;
            }
            ECPoint eCPoint4 = eCPoint3.twicePlus(arreCPoint[n5]);
            eCPoint3 = eCPoint4;
        }
        return eCPoint3;
    }
}

