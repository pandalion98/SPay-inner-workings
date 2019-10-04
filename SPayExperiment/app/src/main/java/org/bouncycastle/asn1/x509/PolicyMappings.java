/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Enumeration
 *  java.util.Hashtable
 */
package org.bouncycastle.asn1.x509;

import java.util.Enumeration;
import java.util.Hashtable;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x509.CertPolicyId;

public class PolicyMappings
extends ASN1Object {
    ASN1Sequence seq = null;

    public PolicyMappings(Hashtable hashtable) {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        Enumeration enumeration = hashtable.keys();
        while (enumeration.hasMoreElements()) {
            String string = (String)enumeration.nextElement();
            String string2 = (String)hashtable.get((Object)string);
            ASN1EncodableVector aSN1EncodableVector2 = new ASN1EncodableVector();
            aSN1EncodableVector2.add(new ASN1ObjectIdentifier(string));
            aSN1EncodableVector2.add(new ASN1ObjectIdentifier(string2));
            aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector2));
        }
        this.seq = new DERSequence(aSN1EncodableVector);
    }

    private PolicyMappings(ASN1Sequence aSN1Sequence) {
        this.seq = aSN1Sequence;
    }

    public PolicyMappings(CertPolicyId certPolicyId, CertPolicyId certPolicyId2) {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(certPolicyId);
        aSN1EncodableVector.add(certPolicyId2);
        this.seq = new DERSequence(new DERSequence(aSN1EncodableVector));
    }

    public PolicyMappings(CertPolicyId[] arrcertPolicyId, CertPolicyId[] arrcertPolicyId2) {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        for (int i2 = 0; i2 != arrcertPolicyId.length; ++i2) {
            ASN1EncodableVector aSN1EncodableVector2 = new ASN1EncodableVector();
            aSN1EncodableVector2.add(arrcertPolicyId[i2]);
            aSN1EncodableVector2.add(arrcertPolicyId2[i2]);
            aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector2));
        }
        this.seq = new DERSequence(aSN1EncodableVector);
    }

    public static PolicyMappings getInstance(Object object) {
        if (object instanceof PolicyMappings) {
            return (PolicyMappings)object;
        }
        if (object != null) {
            return new PolicyMappings(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.seq;
    }
}

