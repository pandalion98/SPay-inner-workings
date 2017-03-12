package org.bouncycastle.math.ec;

public class WNafPreCompInfo implements PreCompInfo {
    protected ECPoint[] preComp;
    protected ECPoint[] preCompNeg;
    protected ECPoint twice;

    public WNafPreCompInfo() {
        this.preComp = null;
        this.preCompNeg = null;
        this.twice = null;
    }

    public ECPoint[] getPreComp() {
        return this.preComp;
    }

    public ECPoint[] getPreCompNeg() {
        return this.preCompNeg;
    }

    public ECPoint getTwice() {
        return this.twice;
    }

    public void setPreComp(ECPoint[] eCPointArr) {
        this.preComp = eCPointArr;
    }

    public void setPreCompNeg(ECPoint[] eCPointArr) {
        this.preCompNeg = eCPointArr;
    }

    public void setTwice(ECPoint eCPoint) {
        this.twice = eCPoint;
    }
}
