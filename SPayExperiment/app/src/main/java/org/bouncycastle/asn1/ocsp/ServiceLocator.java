/*
 * Decompiled with CFR 0.0.
 */
package org.bouncycastle.asn1.ocsp;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x500.X500Name;

public class ServiceLocator
extends ASN1Object {
    X500Name issuer;
    ASN1Primitive locator;

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.issuer);
        if (this.locator != null) {
            aSN1EncodableVector.add(this.locator);
        }
        return new DERSequence(aSN1EncodableVector);
    }
}

