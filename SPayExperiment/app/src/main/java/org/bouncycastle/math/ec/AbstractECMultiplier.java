/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.math.BigInteger
 */
package org.bouncycastle.math.ec;

import java.math.BigInteger;
import org.bouncycastle.math.ec.ECAlgorithms;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECMultiplier;
import org.bouncycastle.math.ec.ECPoint;

public abstract class AbstractECMultiplier
implements ECMultiplier {
    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public ECPoint multiply(ECPoint eCPoint, BigInteger bigInteger) {
        int n = bigInteger.signum();
        if (n == 0 || eCPoint.isInfinity()) {
            return eCPoint.getCurve().getInfinity();
        }
        ECPoint eCPoint2 = this.multiplyPositive(eCPoint, bigInteger.abs());
        if (n > 0) {
            do {
                return ECAlgorithms.validatePoint(eCPoint2);
                break;
            } while (true);
        }
        eCPoint2 = eCPoint2.negate();
        return ECAlgorithms.validatePoint(eCPoint2);
    }

    protected abstract ECPoint multiplyPositive(ECPoint var1, BigInteger var2);
}

