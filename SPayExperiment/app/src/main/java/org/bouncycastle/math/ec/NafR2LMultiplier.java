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

public class NafR2LMultiplier
extends AbstractECMultiplier {
    /*
     * Enabled aggressive block sorting
     */
    @Override
    protected ECPoint multiplyPositive(ECPoint eCPoint, BigInteger bigInteger) {
        int n = 0;
        int[] arrn = WNafUtil.generateCompactNaf(bigInteger);
        ECPoint eCPoint2 = eCPoint.getCurve().getInfinity();
        int n2 = 0;
        while (n < arrn.length) {
            int n3 = arrn[n];
            int n4 = n3 >> 16;
            eCPoint = eCPoint.timesPow2(n2 + (n3 & 65535));
            ECPoint eCPoint3 = n4 < 0 ? eCPoint.negate() : eCPoint;
            eCPoint2 = eCPoint2.add(eCPoint3);
            n2 = 1;
            ++n;
        }
        return eCPoint2;
    }
}

