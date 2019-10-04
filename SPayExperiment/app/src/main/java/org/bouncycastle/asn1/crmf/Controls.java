/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.asn1.crmf;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.crmf.AttributeTypeAndValue;

public class Controls
extends ASN1Object {
    private ASN1Sequence content;

    private Controls(ASN1Sequence aSN1Sequence) {
        this.content = aSN1Sequence;
    }

    public Controls(AttributeTypeAndValue attributeTypeAndValue) {
        this.content = new DERSequence(attributeTypeAndValue);
    }

    public Controls(AttributeTypeAndValue[] arrattributeTypeAndValue) {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        for (int i2 = 0; i2 < arrattributeTypeAndValue.length; ++i2) {
            aSN1EncodableVector.add(arrattributeTypeAndValue[i2]);
        }
        this.content = new DERSequence(aSN1EncodableVector);
    }

    public static Controls getInstance(Object object) {
        if (object instanceof Controls) {
            return (Controls)object;
        }
        if (object != null) {
            return new Controls(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.content;
    }

    public AttributeTypeAndValue[] toAttributeTypeAndValueArray() {
        AttributeTypeAndValue[] arrattributeTypeAndValue = new AttributeTypeAndValue[this.content.size()];
        for (int i2 = 0; i2 != arrattributeTypeAndValue.length; ++i2) {
            arrattributeTypeAndValue[i2] = AttributeTypeAndValue.getInstance(this.content.getObjectAt(i2));
        }
        return arrattributeTypeAndValue;
    }
}

