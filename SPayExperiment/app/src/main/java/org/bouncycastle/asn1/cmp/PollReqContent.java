/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.asn1.cmp;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;

public class PollReqContent
extends ASN1Object {
    private ASN1Sequence content;

    public PollReqContent(ASN1Integer aSN1Integer) {
        this(new DERSequence(new DERSequence(aSN1Integer)));
    }

    private PollReqContent(ASN1Sequence aSN1Sequence) {
        this.content = aSN1Sequence;
    }

    public static PollReqContent getInstance(Object object) {
        if (object instanceof PollReqContent) {
            return (PollReqContent)object;
        }
        if (object != null) {
            return new PollReqContent(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    private static ASN1Integer[] sequenceToASN1IntegerArray(ASN1Sequence aSN1Sequence) {
        ASN1Integer[] arraSN1Integer = new ASN1Integer[aSN1Sequence.size()];
        for (int i2 = 0; i2 != arraSN1Integer.length; ++i2) {
            arraSN1Integer[i2] = ASN1Integer.getInstance(aSN1Sequence.getObjectAt(i2));
        }
        return arraSN1Integer;
    }

    public ASN1Integer[][] getCertReqIds() {
        ASN1Integer[][] arraSN1Integer = new ASN1Integer[this.content.size()][];
        for (int i2 = 0; i2 != arraSN1Integer.length; ++i2) {
            arraSN1Integer[i2] = PollReqContent.sequenceToASN1IntegerArray((ASN1Sequence)this.content.getObjectAt(i2));
        }
        return arraSN1Integer;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.content;
    }
}

