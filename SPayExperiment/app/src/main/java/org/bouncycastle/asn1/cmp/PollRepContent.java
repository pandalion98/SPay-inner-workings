/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.asn1.cmp;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.cmp.PKIFreeText;

public class PollRepContent
extends ASN1Object {
    private ASN1Integer[] certReqId;
    private ASN1Integer[] checkAfter;
    private PKIFreeText[] reason;

    public PollRepContent(ASN1Integer aSN1Integer, ASN1Integer aSN1Integer2) {
        this(aSN1Integer, aSN1Integer2, null);
    }

    public PollRepContent(ASN1Integer aSN1Integer, ASN1Integer aSN1Integer2, PKIFreeText pKIFreeText) {
        this.certReqId = new ASN1Integer[1];
        this.checkAfter = new ASN1Integer[1];
        this.reason = new PKIFreeText[1];
        this.certReqId[0] = aSN1Integer;
        this.checkAfter[0] = aSN1Integer2;
        this.reason[0] = pKIFreeText;
    }

    private PollRepContent(ASN1Sequence aSN1Sequence) {
        this.certReqId = new ASN1Integer[aSN1Sequence.size()];
        this.checkAfter = new ASN1Integer[aSN1Sequence.size()];
        this.reason = new PKIFreeText[aSN1Sequence.size()];
        for (int i2 = 0; i2 != aSN1Sequence.size(); ++i2) {
            ASN1Sequence aSN1Sequence2 = ASN1Sequence.getInstance(aSN1Sequence.getObjectAt(i2));
            this.certReqId[i2] = ASN1Integer.getInstance(aSN1Sequence2.getObjectAt(0));
            this.checkAfter[i2] = ASN1Integer.getInstance(aSN1Sequence2.getObjectAt(1));
            if (aSN1Sequence2.size() <= 2) continue;
            this.reason[i2] = PKIFreeText.getInstance(aSN1Sequence2.getObjectAt(2));
        }
    }

    public static PollRepContent getInstance(Object object) {
        if (object instanceof PollRepContent) {
            return (PollRepContent)object;
        }
        if (object != null) {
            return new PollRepContent(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public ASN1Integer getCertReqId(int n2) {
        return this.certReqId[n2];
    }

    public ASN1Integer getCheckAfter(int n2) {
        return this.checkAfter[n2];
    }

    public PKIFreeText getReason(int n2) {
        return this.reason[n2];
    }

    public int size() {
        return this.certReqId.length;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        for (int i2 = 0; i2 != this.certReqId.length; ++i2) {
            ASN1EncodableVector aSN1EncodableVector2 = new ASN1EncodableVector();
            aSN1EncodableVector2.add(this.certReqId[i2]);
            aSN1EncodableVector2.add(this.checkAfter[i2]);
            if (this.reason[i2] != null) {
                aSN1EncodableVector2.add(this.reason[i2]);
            }
            aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector2));
        }
        return new DERSequence(aSN1EncodableVector);
    }
}

