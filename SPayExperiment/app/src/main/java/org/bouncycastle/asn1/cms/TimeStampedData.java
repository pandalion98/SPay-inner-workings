/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.asn1.cms;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.BERSequence;
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.cms.Evidence;
import org.bouncycastle.asn1.cms.MetaData;

public class TimeStampedData
extends ASN1Object {
    private ASN1OctetString content;
    private DERIA5String dataUri;
    private MetaData metaData;
    private Evidence temporalEvidence;
    private ASN1Integer version;

    /*
     * Enabled aggressive block sorting
     */
    private TimeStampedData(ASN1Sequence aSN1Sequence) {
        int n2;
        this.version = ASN1Integer.getInstance(aSN1Sequence.getObjectAt(0));
        if (aSN1Sequence.getObjectAt(1) instanceof DERIA5String) {
            n2 = 2;
            this.dataUri = DERIA5String.getInstance(aSN1Sequence.getObjectAt(1));
        } else {
            n2 = 1;
        }
        if (aSN1Sequence.getObjectAt(n2) instanceof MetaData || aSN1Sequence.getObjectAt(n2) instanceof ASN1Sequence) {
            int n3 = n2 + 1;
            this.metaData = MetaData.getInstance(aSN1Sequence.getObjectAt(n2));
            n2 = n3;
        }
        if (aSN1Sequence.getObjectAt(n2) instanceof ASN1OctetString) {
            int n4 = n2 + 1;
            this.content = ASN1OctetString.getInstance(aSN1Sequence.getObjectAt(n2));
            n2 = n4;
        }
        this.temporalEvidence = Evidence.getInstance(aSN1Sequence.getObjectAt(n2));
    }

    public TimeStampedData(DERIA5String dERIA5String, MetaData metaData, ASN1OctetString aSN1OctetString, Evidence evidence) {
        this.version = new ASN1Integer(1L);
        this.dataUri = dERIA5String;
        this.metaData = metaData;
        this.content = aSN1OctetString;
        this.temporalEvidence = evidence;
    }

    public static TimeStampedData getInstance(Object object) {
        if (object == null || object instanceof TimeStampedData) {
            return (TimeStampedData)object;
        }
        return new TimeStampedData(ASN1Sequence.getInstance(object));
    }

    public ASN1OctetString getContent() {
        return this.content;
    }

    public DERIA5String getDataUri() {
        return this.dataUri;
    }

    public MetaData getMetaData() {
        return this.metaData;
    }

    public Evidence getTemporalEvidence() {
        return this.temporalEvidence;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.version);
        if (this.dataUri != null) {
            aSN1EncodableVector.add(this.dataUri);
        }
        if (this.metaData != null) {
            aSN1EncodableVector.add(this.metaData);
        }
        if (this.content != null) {
            aSN1EncodableVector.add(this.content);
        }
        aSN1EncodableVector.add(this.temporalEvidence);
        return new BERSequence(aSN1EncodableVector);
    }
}

