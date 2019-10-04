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
import org.bouncycastle.asn1.crmf.CertReqMsg;

public class CertReqMessages
extends ASN1Object {
    private ASN1Sequence content;

    private CertReqMessages(ASN1Sequence aSN1Sequence) {
        this.content = aSN1Sequence;
    }

    public CertReqMessages(CertReqMsg certReqMsg) {
        this.content = new DERSequence(certReqMsg);
    }

    public CertReqMessages(CertReqMsg[] arrcertReqMsg) {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        for (int i2 = 0; i2 < arrcertReqMsg.length; ++i2) {
            aSN1EncodableVector.add(arrcertReqMsg[i2]);
        }
        this.content = new DERSequence(aSN1EncodableVector);
    }

    public static CertReqMessages getInstance(Object object) {
        if (object instanceof CertReqMessages) {
            return (CertReqMessages)object;
        }
        if (object != null) {
            return new CertReqMessages(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.content;
    }

    public CertReqMsg[] toCertReqMsgArray() {
        CertReqMsg[] arrcertReqMsg = new CertReqMsg[this.content.size()];
        for (int i2 = 0; i2 != arrcertReqMsg.length; ++i2) {
            arrcertReqMsg[i2] = CertReqMsg.getInstance(this.content.getObjectAt(i2));
        }
        return arrcertReqMsg;
    }
}

