/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.util.Enumeration
 */
package org.bouncycastle.asn1.cms;

import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.cms.TimeStampAndCRL;

public class TimeStampTokenEvidence
extends ASN1Object {
    private TimeStampAndCRL[] timeStampAndCRLs;

    private TimeStampTokenEvidence(ASN1Sequence aSN1Sequence) {
        this.timeStampAndCRLs = new TimeStampAndCRL[aSN1Sequence.size()];
        int n2 = 0;
        Enumeration enumeration = aSN1Sequence.getObjects();
        while (enumeration.hasMoreElements()) {
            TimeStampAndCRL[] arrtimeStampAndCRL = this.timeStampAndCRLs;
            int n3 = n2 + 1;
            arrtimeStampAndCRL[n2] = TimeStampAndCRL.getInstance(enumeration.nextElement());
            n2 = n3;
        }
    }

    public TimeStampTokenEvidence(TimeStampAndCRL timeStampAndCRL) {
        this.timeStampAndCRLs = new TimeStampAndCRL[1];
        this.timeStampAndCRLs[0] = timeStampAndCRL;
    }

    public TimeStampTokenEvidence(TimeStampAndCRL[] arrtimeStampAndCRL) {
        this.timeStampAndCRLs = arrtimeStampAndCRL;
    }

    public static TimeStampTokenEvidence getInstance(Object object) {
        if (object instanceof TimeStampTokenEvidence) {
            return (TimeStampTokenEvidence)object;
        }
        if (object != null) {
            return new TimeStampTokenEvidence(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public static TimeStampTokenEvidence getInstance(ASN1TaggedObject aSN1TaggedObject, boolean bl) {
        return TimeStampTokenEvidence.getInstance(ASN1Sequence.getInstance(aSN1TaggedObject, bl));
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        for (int i2 = 0; i2 != this.timeStampAndCRLs.length; ++i2) {
            aSN1EncodableVector.add(this.timeStampAndCRLs[i2]);
        }
        return new DERSequence(aSN1EncodableVector);
    }

    public TimeStampAndCRL[] toTimeStampAndCRLArray() {
        return this.timeStampAndCRLs;
    }
}

