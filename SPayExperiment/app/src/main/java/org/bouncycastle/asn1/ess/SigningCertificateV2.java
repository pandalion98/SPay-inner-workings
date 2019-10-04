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
import org.bouncycastle.asn1.ess.ESSCertIDv2;
import org.bouncycastle.asn1.x509.PolicyInformation;

public class SigningCertificateV2
extends ASN1Object {
    ASN1Sequence certs;
    ASN1Sequence policies;

    private SigningCertificateV2(ASN1Sequence aSN1Sequence) {
        if (aSN1Sequence.size() < 1 || aSN1Sequence.size() > 2) {
            throw new IllegalArgumentException("Bad sequence size: " + aSN1Sequence.size());
        }
        this.certs = ASN1Sequence.getInstance(aSN1Sequence.getObjectAt(0));
        if (aSN1Sequence.size() > 1) {
            this.policies = ASN1Sequence.getInstance(aSN1Sequence.getObjectAt(1));
        }
    }

    public SigningCertificateV2(ESSCertIDv2 eSSCertIDv2) {
        this.certs = new DERSequence(eSSCertIDv2);
    }

    public SigningCertificateV2(ESSCertIDv2[] arreSSCertIDv2) {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        for (int i2 = 0; i2 < arreSSCertIDv2.length; ++i2) {
            aSN1EncodableVector.add(arreSSCertIDv2[i2]);
        }
        this.certs = new DERSequence(aSN1EncodableVector);
    }

    public SigningCertificateV2(ESSCertIDv2[] arreSSCertIDv2, PolicyInformation[] arrpolicyInformation) {
        int n2 = 0;
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        for (int i2 = 0; i2 < arreSSCertIDv2.length; ++i2) {
            aSN1EncodableVector.add(arreSSCertIDv2[i2]);
        }
        this.certs = new DERSequence(aSN1EncodableVector);
        if (arrpolicyInformation != null) {
            ASN1EncodableVector aSN1EncodableVector2 = new ASN1EncodableVector();
            while (n2 < arrpolicyInformation.length) {
                aSN1EncodableVector2.add(arrpolicyInformation[n2]);
                ++n2;
            }
            this.policies = new DERSequence(aSN1EncodableVector2);
        }
    }

    public static SigningCertificateV2 getInstance(Object object) {
        if (object == null || object instanceof SigningCertificateV2) {
            return (SigningCertificateV2)object;
        }
        if (object instanceof ASN1Sequence) {
            return new SigningCertificateV2((ASN1Sequence)object);
        }
        return null;
    }

    public ESSCertIDv2[] getCerts() {
        ESSCertIDv2[] arreSSCertIDv2 = new ESSCertIDv2[this.certs.size()];
        for (int i2 = 0; i2 != this.certs.size(); ++i2) {
            arreSSCertIDv2[i2] = ESSCertIDv2.getInstance(this.certs.getObjectAt(i2));
        }
        return arreSSCertIDv2;
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

