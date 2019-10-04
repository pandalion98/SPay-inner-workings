/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.asn1.esf;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.esf.SigPolicyQualifierInfo;

public class SigPolicyQualifiers
extends ASN1Object {
    ASN1Sequence qualifiers;

    private SigPolicyQualifiers(ASN1Sequence aSN1Sequence) {
        this.qualifiers = aSN1Sequence;
    }

    public SigPolicyQualifiers(SigPolicyQualifierInfo[] arrsigPolicyQualifierInfo) {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        for (int i2 = 0; i2 < arrsigPolicyQualifierInfo.length; ++i2) {
            aSN1EncodableVector.add(arrsigPolicyQualifierInfo[i2]);
        }
        this.qualifiers = new DERSequence(aSN1EncodableVector);
    }

    public static SigPolicyQualifiers getInstance(Object object) {
        if (object instanceof SigPolicyQualifiers) {
            return (SigPolicyQualifiers)object;
        }
        if (object instanceof ASN1Sequence) {
            return new SigPolicyQualifiers(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public SigPolicyQualifierInfo getInfoAt(int n2) {
        return SigPolicyQualifierInfo.getInstance(this.qualifiers.getObjectAt(n2));
    }

    public int size() {
        return this.qualifiers.size();
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.qualifiers;
    }
}

