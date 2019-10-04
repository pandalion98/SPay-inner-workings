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

public class ZSignedDigitR2LMultiplier
extends AbstractECMultiplier {
    /*
     * Enabled aggressive block sorting
     */
    @Override
    protected ECPoint multiplyPositive(ECPoint eCPoint, BigInteger bigInteger) {
        ECPoint eCPoint2 = eCPoint.getCurve().getInfinity();
        int n = bigInteger.bitLength();
        int n2 = bigInteger.getLowestSetBit();
        ECPoint eCPoint3 = eCPoint.timesPow2(n2);
        ECPoint eCPoint4 = eCPoint2;
        int n3;
        while ((n3 = n2 + 1) < n) {
            ECPoint eCPoint5 = bigInteger.testBit(n3) ? eCPoint3 : eCPoint3.negate();
            ECPoint eCPoint6 = eCPoint4.add(eCPoint5);
            eCPoint3 = eCPoint3.twice();
            eCPoint4 = eCPoint6;
            n2 = n3;
        }
        return eCPoint4.add(eCPoint3);
    }
}

