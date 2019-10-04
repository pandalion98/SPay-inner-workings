/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 */
package org.bouncycastle.math.ec;

import java.math.BigInteger;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.FixedPointPreCompInfo;
import org.bouncycastle.math.ec.PreCompInfo;

public class FixedPointUtil {
    public static final String PRECOMP_NAME = "bc_fixed_point";

    public static int getCombSize(ECCurve eCCurve) {
        BigInteger bigInteger = eCCurve.getOrder();
        if (bigInteger == null) {
            return 1 + eCCurve.getFieldSize();
        }
        return bigInteger.bitLength();
    }

    public static FixedPointPreCompInfo getFixedPointPreCompInfo(PreCompInfo preCompInfo) {
        if (preCompInfo != null && preCompInfo instanceof FixedPointPreCompInfo) {
            return (FixedPointPreCompInfo)preCompInfo;
        }
        return new FixedPointPreCompInfo();
    }

    public static FixedPointPreCompInfo precompute(ECPoint eCPoint, int n) {
        ECCurve eCCurve = eCPoint.getCurve();
        int n2 = 1 << n;
        FixedPointPreCompInfo fixedPointPreCompInfo = FixedPointUtil.getFixedPointPreCompInfo(eCCurve.getPreCompInfo(eCPoint, PRECOMP_NAME));
        ECPoint[] arreCPoint = fixedPointPreCompInfo.getPreComp();
        if (arreCPoint == null || arreCPoint.length < n2) {
            int n3 = (-1 + (n + FixedPointUtil.getCombSize(eCCurve))) / n;
            ECPoint[] arreCPoint2 = new ECPoint[n];
            arreCPoint2[0] = eCPoint;
            for (int i = 1; i < n; ++i) {
                arreCPoint2[i] = arreCPoint2[i - 1].timesPow2(n3);
            }
            eCCurve.normalizeAll(arreCPoint2);
            ECPoint[] arreCPoint3 = new ECPoint[n2];
            arreCPoint3[0] = eCCurve.getInfinity();
            for (int i = n - 1; i >= 0; --i) {
                int n4;
                ECPoint eCPoint2 = arreCPoint2[i];
                for (int j = n4 = 1 << i; j < n2; j += n4 << 1) {
                    arreCPoint3[j] = arreCPoint3[j - n4].add(eCPoint2);
                }
            }
            eCCurve.normalizeAll(arreCPoint3);
            fixedPointPreCompInfo.setPreComp(arreCPoint3);
            fixedPointPreCompInfo.setWidth(n);
            eCCurve.setPreCompInfo(eCPoint, PRECOMP_NAME, fixedPointPreCompInfo);
        }
        return fixedPointPreCompInfo;
    }
}

