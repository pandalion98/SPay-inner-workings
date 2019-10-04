/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.math.ec;

import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.PreCompInfo;

public class WNafPreCompInfo
implements PreCompInfo {
    protected ECPoint[] preComp = null;
    protected ECPoint[] preCompNeg = null;
    protected ECPoint twice = null;

    public ECPoint[] getPreComp() {
        return this.preComp;
    }

    public ECPoint[] getPreCompNeg() {
        return this.preCompNeg;
    }

    public ECPoint getTwice() {
        return this.twice;
    }

    public void setPreComp(ECPoint[] arreCPoint) {
        this.preComp = arreCPoint;
    }

    public void setPreCompNeg(ECPoint[] arreCPoint) {
        this.preCompNeg = arreCPoint;
    }

    public void setTwice(ECPoint eCPoint) {
        this.twice = eCPoint;
    }
}

