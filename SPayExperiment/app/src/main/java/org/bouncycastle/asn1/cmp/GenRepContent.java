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
import org.bouncycastle.asn1.cmp.InfoTypeAndValue;

public class GenRepContent
extends ASN1Object {
    private ASN1Sequence content;

    private GenRepContent(ASN1Sequence aSN1Sequence) {
        this.content = aSN1Sequence;
    }

    public GenRepContent(InfoTypeAndValue infoTypeAndValue) {
        this.content = new DERSequence(infoTypeAndValue);
    }

    public GenRepContent(InfoTypeAndValue[] arrinfoTypeAndValue) {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        for (int i2 = 0; i2 < arrinfoTypeAndValue.length; ++i2) {
            aSN1EncodableVector.add(arrinfoTypeAndValue[i2]);
        }
        this.content = new DERSequence(aSN1EncodableVector);
    }

    public static GenRepContent getInstance(Object object) {
        if (object instanceof GenRepContent) {
            return (GenRepContent)object;
        }
        if (object != null) {
            return new GenRepContent(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.content;
    }

    public InfoTypeAndValue[] toInfoTypeAndValueArray() {
        InfoTypeAndValue[] arrinfoTypeAndValue = new InfoTypeAndValue[this.content.size()];
        for (int i2 = 0; i2 != arrinfoTypeAndValue.length; ++i2) {
            arrinfoTypeAndValue[i2] = InfoTypeAndValue.getInstance(this.content.getObjectAt(i2));
        }
        return arrinfoTypeAndValue;
    }
}

