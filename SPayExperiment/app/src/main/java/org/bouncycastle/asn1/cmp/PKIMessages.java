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
import org.bouncycastle.asn1.cmp.PKIMessage;

public class PKIMessages
extends ASN1Object {
    private ASN1Sequence content;

    private PKIMessages(ASN1Sequence aSN1Sequence) {
        this.content = aSN1Sequence;
    }

    public PKIMessages(PKIMessage pKIMessage) {
        this.content = new DERSequence(pKIMessage);
    }

    public PKIMessages(PKIMessage[] arrpKIMessage) {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        for (int i2 = 0; i2 < arrpKIMessage.length; ++i2) {
            aSN1EncodableVector.add(arrpKIMessage[i2]);
        }
        this.content = new DERSequence(aSN1EncodableVector);
    }

    public static PKIMessages getInstance(Object object) {
        if (object instanceof PKIMessages) {
            return (PKIMessages)object;
        }
        if (object != null) {
            return new PKIMessages(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.content;
    }

    public PKIMessage[] toPKIMessageArray() {
        PKIMessage[] arrpKIMessage = new PKIMessage[this.content.size()];
        for (int i2 = 0; i2 != arrpKIMessage.length; ++i2) {
            arrpKIMessage[i2] = PKIMessage.getInstance(this.content.getObjectAt(i2));
        }
        return arrpKIMessage;
    }
}

