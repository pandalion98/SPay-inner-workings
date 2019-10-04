/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.math.BigInteger
 */
package org.bouncycastle.math.ec;

import java.math.BigInteger;
import org.bouncycastle.math.ec.AbstractECMultiplier;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.WNafUtil;

public class NafL2RMultiplier
extends AbstractECMultiplier {
    /*
     * Enabled aggressive block sorting
     */
    @Override
    protected ECPoint multiplyPositive(ECPoint eCPoint, BigInteger bigInteger) {
        int[] arrn = WNafUtil.generateCompactNaf(bigInteger);
        ECPoint eCPoint2 = eCPoint.normalize();
        ECPoint eCPoint3 = eCPoint2.negate();
        ECPoint eCPoint4 = eCPoint.getCurve().getInfinity();
        int n = arrn.length;
        ECPoint eCPoint5 = eCPoint4;
        int n2;
        while ((n2 = n - 1) >= 0) {
            int n3 = arrn[n2];
            int n4 = n3 >> 16;
            int n5 = 65535 & n3;
            ECPoint eCPoint6 = n4 < 0 ? eCPoint3 : eCPoint2;
            eCPoint5 = eCPoint5.twicePlus(eCPoint6).timesPow2(n5);
            n = n2;
        }
        return eCPoint5;
    }
}

