/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.asn1.ess;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.ess.OtherCertID;
import org.bouncycastle.asn1.x509.PolicyInformation;

public class OtherSigningCertificate
extends ASN1Object {
    ASN1Sequence certs;
    ASN1Sequence policies;

    private OtherSigningCertificate(ASN1Sequence aSN1Sequence) {
        if (aSN1Sequence.size() < 1 || aSN1Sequence.size() > 2) {
            throw new IllegalArgumentException("Bad sequence size: " + aSN1Sequence.size());
        }
        this.certs = ASN1Sequence.getInstance(aSN1Sequence.getObjectAt(0));
        if (aSN1Sequence.size() > 1) {
            this.policies = ASN1Sequence.getInstance(aSN1Sequence.getObjectAt(1));
        }
    }

    public OtherSigningCertificate(OtherCertID otherCertID) {
        this.certs = new DERSequence(otherCertID);
    }

    public static OtherSigningCertificate getInstance(Object object) {
        if (object instanceof OtherSigningCertificate) {
            return (OtherSigningCertificate)object;
        }
        if (object != null) {
            return new OtherSigningCertificate(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public OtherCertID[] getCerts() {
        OtherCertID[] arrotherCertID = new OtherCertID[this.certs.size()];
        for (int i2 = 0; i2 != this.certs.size(); ++i2) {
            arrotherCertID[i2] = OtherCertID.getInstance(this.certs.getObjectAt(i2));
        }
        return arrotherCertID;
    }

    public PolicyInformation[] getPolicies() {
        if (this.policies == null) {
            return null;
        }
        PolicyInformation[] arrpolicyInformation = new PolicyInformation[this.policies.size()];
        for (int i2 = 0; i2 != this.policies.size(); ++i2) {
            arrpolicyInformation[i2] = PolicyInformation.getInstance(this.policies.getObjectAt(i2));
        }
        return arrpolicyInformation;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.certs);
        if (this.policies != null) {
            aSN1EncodableVector.add(this.policies);
        }
        return new DERSequence(aSN1EncodableVector);
    }
}

