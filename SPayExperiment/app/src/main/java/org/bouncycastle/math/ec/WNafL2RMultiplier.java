/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Math
 *  java.math.BigInteger
 */
package org.bouncycastle.math.ec;

import java.math.BigInteger;
import org.bouncycastle.math.ec.AbstractECMultiplier;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.LongArray;
import org.bouncycastle.math.ec.WNafPreCompInfo;
import org.bouncycastle.math.ec.WNafUtil;

public class WNafL2RMultiplier
extends AbstractECMultiplier {
    protected int getWindowSize(int n) {
        return WNafUtil.getWindowSize(n);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    protected ECPoint multiplyPositive(ECPoint eCPoint, BigInteger bigInteger) {
        ECPoint eCPoint2;
        int n = Math.max((int)2, (int)Math.min((int)16, (int)this.getWindowSize(bigInteger.bitLength())));
        WNafPreCompInfo wNafPreCompInfo = WNafUtil.precompute(eCPoint, n, true);
        ECPoint[] arreCPoint = wNafPreCompInfo.getPreComp();
        ECPoint[] arreCPoint2 = wNafPreCompInfo.getPreCompNeg();
        int[] arrn = WNafUtil.generateCompactWindowNaf(n, bigInteger);
        ECPoint eCPoint3 = eCPoint.getCurve().getInfinity();
        int n2 = arrn.length;
        if (n2 > 1) {
            ECPoint eCPoint4;
            int n3 = n2 - 1;
            int n4 = arrn[n3];
            int n5 = n4 >> 16;
            int n6 = n4 & 65535;
            int n7 = Math.abs((int)n5);
            ECPoint[] arreCPoint3 = n5 < 0 ? arreCPoint2 : arreCPoint;
            if (n7 << 2 < 1 << n) {
                byte by = LongArray.bitLengths[n7];
                int n8 = n - by;
                int n9 = n7 ^ 1 << by - 1;
                int n10 = -1 + (1 << n - 1);
                int n11 = 1 + (n9 << n8);
                eCPoint4 = arreCPoint3[n10 >>> 1].add(arreCPoint3[n11 >>> 1]);
                n6 -= n8;
            } else {
                eCPoint4 = arreCPoint3[n7 >>> 1];
            }
            eCPoint2 = eCPoint4.timesPow2(n6);
            n2 = n3;
        } else {
            eCPoint2 = eCPoint3;
        }
        while (n2 > 0) {
            int n12 = n2 - 1;
            int n13 = arrn[n12];
            int n14 = n13 >> 16;
            int n15 = n13 & 65535;
            int n16 = Math.abs((int)n14);
            ECPoint[] arreCPoint4 = n14 < 0 ? arreCPoint2 : arreCPoint;
            eCPoint2 = eCPoint2.twicePlus(arreCPoint4[n16 >>> 1]).timesPow2(n15);
            n2 = n12;
        }
        return eCPoint2;
    }
}

