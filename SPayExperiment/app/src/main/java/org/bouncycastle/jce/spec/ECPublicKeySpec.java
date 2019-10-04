/*
 * Decompiled with CFR 0.0.
 */
package org.bouncycastle.jce.spec;

import org.bouncycastle.jce.spec.ECKeySpec;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;

public class ECPublicKeySpec
extends ECKeySpec {
    private ECPoint q;

    public ECPublicKeySpec(ECPoint eCPoint, ECParameterSpec eCParameterSpec) {
        super(eCParameterSpec);
        if (eCPoint.getCurve() != null) {
            this.q = eCPoint.normalize();
            return;
        }
        this.q = eCPoint;
    }

    public ECPoint getQ() {
        return this.q;
    }
}

