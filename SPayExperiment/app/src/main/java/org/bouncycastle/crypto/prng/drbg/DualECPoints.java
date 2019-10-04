/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  org.bouncycastle.math.ec.ECCurve
 *  org.bouncycastle.math.ec.ECPoint
 */
package org.bouncycastle.crypto.prng.drbg;

import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;

public class DualECPoints {
    private final int cofactor;
    private final ECPoint p;
    private final ECPoint q;
    private final int securityStrength;

    public DualECPoints(int n2, ECPoint eCPoint, ECPoint eCPoint2, int n3) {
        if (!eCPoint.getCurve().equals(eCPoint2.getCurve())) {
            throw new IllegalArgumentException("points need to be on the same curve");
        }
        this.securityStrength = n2;
        this.p = eCPoint;
        this.q = eCPoint2;
        this.cofactor = n3;
    }

    private static int log2(int n2) {
        int n3 = 0;
        while ((n2 >>= 1) != 0) {
            ++n3;
        }
        return n3;
    }

    public int getCofactor() {
        return this.cofactor;
    }

    public int getMaxOutlen() {
        return 8 * ((this.p.getCurve().getFieldSize() - (13 + DualECPoints.log2(this.cofactor))) / 8);
    }

    public ECPoint getP() {
        return this.p;
    }

    public ECPoint getQ() {
        return this.q;
    }

    public int getSecurityStrength() {
        return this.securityStrength;
    }

    public int getSeedLen() {
        return this.p.getCurve().getFieldSize();
    }
}

