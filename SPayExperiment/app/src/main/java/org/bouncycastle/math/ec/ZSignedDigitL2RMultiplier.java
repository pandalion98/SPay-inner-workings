/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.math.BigInteger
 */
package org.bouncycastle.math.ec;

import java.math.BigInteger;
import org.bouncycastle.math.ec.AbstractECMultiplier;
import org.bouncycastle.math.ec.ECPoint;

public class ZSignedDigitL2RMultiplier
extends AbstractECMultiplier {
    /*
     * Enabled aggressive block sorting
     */
    @Override
    protected ECPoint multiplyPositive(ECPoint eCPoint, BigInteger bigInteger) {
        ECPoint eCPoint2 = eCPoint.normalize();
        ECPoint eCPoint3 = eCPoint2.negate();
        int n = bigInteger.bitLength();
        int n2 = bigInteger.getLowestSetBit();
        ECPoint eCPoint4 = eCPoint2;
        int n3;
        while ((n3 = n - 1) > n2) {
            ECPoint eCPoint5 = bigInteger.testBit(n3) ? eCPoint2 : eCPoint3;
            eCPoint4 = eCPoint4.twicePlus(eCPoint5);
            n = n3;
        }
        return eCPoint4.timesPow2(n2);
    }
}

