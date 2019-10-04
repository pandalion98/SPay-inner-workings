/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 */
package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.asn1.x509.PolicyInformation;

public class CertificatePolicies
extends ASN1Object {
    private final PolicyInformation[] policyInformation;

    private CertificatePolicies(ASN1Sequence aSN1Sequence) {
        this.policyInformation = new PolicyInformation[aSN1Sequence.size()];
        for (int i2 = 0; i2 != aSN1Sequence.size(); ++i2) {
            this.policyInformation[i2] = PolicyInformation.getInstance(aSN1Sequence.getObjectAt(i2));
        }
    }

    public CertificatePolicies(PolicyInformation policyInformation) {
        this.policyInformation = new PolicyInformation[]{policyInformation};
    }

    public CertificatePolicies(PolicyInformation[] arrpolicyInformation) {
        this.policyInformation = arrpolicyInformation;
    }

    public static CertificatePolicies fromExtensions(Extensions extensions) {
        return CertificatePolicies.getInstance(extensions.getExtensionParsedValue(Extension.certificatePolicies));
    }

    public static CertificatePolicies getInstance(Object object) {
        if (object instanceof CertificatePolicies) {
            return (CertificatePolicies)object;
        }
        if (object != null) {
            return new CertificatePolicies(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public static CertificatePolicies getInstance(ASN1TaggedObject aSN1TaggedObject, boolean bl) {
        return CertificatePolicies.getInstance(ASN1Sequence.getInstance(aSN1TaggedObject, bl));
    }

    public PolicyInformation getPolicyInformation(ASN1ObjectIdentifier aSN1ObjectIdentifier) {
        for (int i2 = 0; i2 != this.policyInformation.length; ++i2) {
            if (!aSN1ObjectIdentifier.equals(this.policyInformation[i2].getPolicyIdentifier())) continue;
            return this.policyInformation[i2];
        }
        return null;
    }

    public PolicyInformation[] getPolicyInformation() {
        PolicyInformation[] arrpolicyInformation = new PolicyInformation[this.policyInformation.length];
        System.arraycopy((Object)this.policyInformation, (int)0, (Object)arrpolicyInformation, (int)0, (int)this.policyInformation.length);
        return arrpolicyInformation;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        return new DERSequence(this.policyInformation);
    }

    public String toString() {
        String string = null;
        for (int i2 = 0; i2 < this.policyInformation.length; ++i2) {
            if (string != null) {
                string = string + ", ";
            }
            String string2 = string + this.policyInformation[i2];
            string = string2;
        }
        return "CertificatePolicies: " + string;
    }
}

