/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.asn1.cms;

import org.bouncycastle.asn1.ASN1Choice;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.cms.TimeStampTokenEvidence;

public class Evidence
extends ASN1Object
implements ASN1Choice {
    private TimeStampTokenEvidence tstEvidence;

    private Evidence(ASN1TaggedObject aSN1TaggedObject) {
        if (aSN1TaggedObject.getTagNo() == 0) {
            this.tstEvidence = TimeStampTokenEvidence.getInstance(aSN1TaggedObject, false);
        }
    }

    public Evidence(TimeStampTokenEvidence timeStampTokenEvidence) {
        this.tstEvidence = timeStampTokenEvidence;
    }

    public static Evidence getInstance(Object object) {
        if (object == null || object instanceof Evidence) {
            return (Evidence)object;
        }
        if (object instanceof ASN1TaggedObject) {
            return new Evidence(ASN1TaggedObject.getInstance(object));
        }
        throw new IllegalArgumentException("unknown object in getInstance");
    }

    public TimeStampTokenEvidence getTstEvidence() {
        return this.tstEvidence;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        if (this.tstEvidence != null) {
            return new DERTaggedObject(false, 0, this.tstEvidence);
        }
        return null;
    }
}

