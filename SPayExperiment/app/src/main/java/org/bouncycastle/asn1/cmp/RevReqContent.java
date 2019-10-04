/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.asn1.cmp;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.cmp.RevDetails;

public class RevReqContent
extends ASN1Object {
    private ASN1Sequence content;

    private RevReqContent(ASN1Sequence aSN1Sequence) {
        this.content = aSN1Sequence;
    }

    public RevReqContent(RevDetails revDetails) {
        this.content = new DERSequence(revDetails);
    }

    public RevReqContent(RevDetails[] arrrevDetails) {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        for (int i2 = 0; i2 != arrrevDetails.length; ++i2) {
            aSN1EncodableVector.add(arrrevDetails[i2]);
        }
        this.content = new DERSequence(aSN1EncodableVector);
    }

    public static RevReqContent getInstance(Object object) {
        if (object instanceof RevReqContent) {
            return (RevReqContent)object;
        }
        if (object != null) {
            return new RevReqContent(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.content;
    }

    public RevDetails[] toRevDetailsArray() {
        RevDetails[] arrrevDetails = new RevDetails[this.content.size()];
        for (int i2 = 0; i2 != arrrevDetails.length; ++i2) {
            arrrevDetails[i2] = RevDetails.getInstance(this.content.getObjectAt(i2));
        }
        return arrrevDetails;
    }
}

