/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  org.bouncycastle.math.ec.ECCurve
 *  org.bouncycastle.math.ec.ECPoint
 */
package org.bouncycastle.asn1.x9;

import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;

public class X9ECPoint
extends ASN1Object {
    ECPoint p;

    public X9ECPoint(ECCurve eCCurve, ASN1OctetString aSN1OctetString) {
        this.p = eCCurve.decodePoint(aSN1OctetString.getOctets());
    }

    public X9ECPoint(ECPoint eCPoint) {
        this.p = eCPoint.normalize();
    }

    public ECPoint getPoint() {
        return this.p;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        return new DEROctetString(this.p.getEncoded());
    }
}

