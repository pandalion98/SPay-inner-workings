/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.math.ec;

import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.PreCompInfo;

public class WTauNafPreCompInfo
implements PreCompInfo {
    protected ECPoint.F2m[] preComp = null;

    public ECPoint.F2m[] getPreComp() {
        return this.preComp;
    }

    public void setPreComp(ECPoint.F2m[] arrf2m) {
        this.preComp = arrf2m;
    }
}

