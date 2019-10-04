/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.asn1.pkcs;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;

public class CRLBag
extends ASN1Object {
    private ASN1ObjectIdentifier crlId;
    private ASN1Encodable crlValue;

    public CRLBag(ASN1ObjectIdentifier aSN1ObjectIdentifier, ASN1Encodable aSN1Encodable) {
        this.crlId = aSN1ObjectIdentifier;
        this.crlValue = aSN1Encodable;
    }

    private CRLBag(ASN1Sequence aSN1Sequence) {
        this.crlId = (ASN1ObjectIdentifier)aSN1Sequence.getObjectAt(0);
        this.crlValue = ((DERTaggedObject)aSN1Sequence.getObjectAt(1)).getObject();
    }

    public static CRLBag getInstance(Object object) {
        if (object instanceof CRLBag) {
            return (CRLBag)object;
        }
        if (object != null) {
            return new CRLBag(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public ASN1Encodable getCRLValue() {
        return this.crlValue;
    }

    public ASN1ObjectIdentifier getcrlId() {
        return this.crlId;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.crlId);
        aSN1EncodableVector.add(new DERTaggedObject(0, this.crlValue));
        return new DERSequence(aSN1EncodableVector);
    }
}

