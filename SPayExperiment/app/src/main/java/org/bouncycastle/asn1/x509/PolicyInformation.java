/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;

public class PolicyInformation
extends ASN1Object {
    private ASN1ObjectIdentifier policyIdentifier;
    private ASN1Sequence policyQualifiers;

    public PolicyInformation(ASN1ObjectIdentifier aSN1ObjectIdentifier) {
        this.policyIdentifier = aSN1ObjectIdentifier;
    }

    public PolicyInformation(ASN1ObjectIdentifier aSN1ObjectIdentifier, ASN1Sequence aSN1Sequence) {
        this.policyIdentifier = aSN1ObjectIdentifier;
        this.policyQualifiers = aSN1Sequence;
    }

    private PolicyInformation(ASN1Sequence aSN1Sequence) {
        if (aSN1Sequence.size() < 1 || aSN1Sequence.size() > 2) {
            throw new IllegalArgumentException("Bad sequence size: " + aSN1Sequence.size());
        }
        this.policyIdentifier = ASN1ObjectIdentifier.getInstance(aSN1Sequence.getObjectAt(0));
        if (aSN1Sequence.size() > 1) {
            this.policyQualifiers = ASN1Sequence.getInstance(aSN1Sequence.getObjectAt(1));
        }
    }

    public static PolicyInformation getInstance(Object object) {
        if (object == null || object instanceof PolicyInformation) {
            return (PolicyInformation)object;
        }
        return new PolicyInformation(ASN1Sequence.getInstance(object));
    }

    public ASN1ObjectIdentifier getPolicyIdentifier() {
        return this.policyIdentifier;
    }

    public ASN1Sequence getPolicyQualifiers() {
        return this.policyQualifiers;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.policyIdentifier);
        if (this.policyQualifiers != null) {
            aSN1EncodableVector.add(this.policyQualifiers);
        }
        return new DERSequence(aSN1EncodableVector);
    }
}

