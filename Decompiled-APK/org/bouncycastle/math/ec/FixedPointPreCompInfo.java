package org.bouncycastle.math.ec;

public class FixedPointPreCompInfo implements PreCompInfo {
    protected ECPoint[] preComp;
    protected int width;

    public FixedPointPreCompInfo() {
        this.preComp = null;
        this.width = -1;
    }

    public ECPoint[] getPreComp() {
        return this.preComp;
    }

    public int getWidth() {
        return this.width;
    }

    public void setPreComp(ECPoint[] eCPointArr) {
        this.preComp = eCPointArr;
    }

    public void setWidth(int i) {
        this.width = i;
    }
}
