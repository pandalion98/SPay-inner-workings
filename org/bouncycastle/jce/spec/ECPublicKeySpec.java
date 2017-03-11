package org.bouncycastle.jce.spec;

import org.bouncycastle.math.ec.ECPoint;

public class ECPublicKeySpec extends ECKeySpec {
    private ECPoint f307q;

    public ECPublicKeySpec(ECPoint eCPoint, ECParameterSpec eCParameterSpec) {
        super(eCParameterSpec);
        if (eCPoint.getCurve() != null) {
            this.f307q = eCPoint.normalize();
        } else {
            this.f307q = eCPoint;
        }
    }

    public ECPoint getQ() {
        return this.f307q;
    }
}
