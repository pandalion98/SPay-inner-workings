package org.bouncycastle.math.ec;

import org.bouncycastle.math.ec.ECPoint.F2m;

public class WTauNafPreCompInfo implements PreCompInfo {
    protected F2m[] preComp;

    public WTauNafPreCompInfo() {
        this.preComp = null;
    }

    public F2m[] getPreComp() {
        return this.preComp;
    }

    public void setPreComp(F2m[] f2mArr) {
        this.preComp = f2mArr;
    }
}
