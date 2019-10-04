/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.math.ec;

import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.PreCompInfo;

public class FixedPointPreCompInfo
implements PreCompInfo {
    protected ECPoint[] preComp = null;
    protected int width = -1;

    public ECPoint[] getPreComp() {
        return this.preComp;
    }

    public int getWidth() {
        return this.width;
    }

    public void setPreComp(ECPoint[] arreCPoint) {
        this.preComp = arreCPoint;
    }

    public void setWidth(int n) {
        this.width = n;
    }
}

