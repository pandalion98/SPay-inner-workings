/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.asn1.ocsp;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1GeneralizedTime;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.ocsp.ResponderID;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.asn1.x509.X509Extensions;

public class ResponseData
extends ASN1Object {
    private static final ASN1Integer V1 = new ASN1Integer(0L);
    private ASN1GeneralizedTime producedAt;
    private ResponderID responderID;
    private Extensions responseExtensions;
    private ASN1Sequence responses;
    private ASN1Integer version;
    private boolean versionPresent;

    public ResponseData(ASN1Integer aSN1Integer, ResponderID responderID, ASN1GeneralizedTime aSN1GeneralizedTime, ASN1Sequence aSN1Sequence, Extensions extensions) {
        this.version = aSN1Integer;
        this.responderID = responderID;
        this.producedAt = aSN1GeneralizedTime;
        this.responses = aSN1Sequence;
        this.responseExtensions = extensions;
    }

    /*
     * Enabled aggressive block sorting
     */
    private ResponseData(ASN1Sequence aSN1Sequence) {
        int n2;
        block3 : {
            block4 : {
                block1 : {
                    block2 : {
                        if (!(aSN1Sequence.getObjectAt(0) instanceof ASN1TaggedObject)) break block1;
                        if (((ASN1TaggedObject)aSN1Sequence.getObjectAt(0)).getTagNo() != 0) break block2;
                        this.versionPresent = true;
                        this.version = ASN1Integer.getInstance((ASN1TaggedObject)aSN1Sequence.getObjectAt(0), true);
                        n2 = 1;
                        break block3;
                    }
                    this.version = V1;
                    break block4;
                }
                this.version = V1;
            }
            n2 = 0;
        }
        int n3 = n2 + 1;
        this.responderID = ResponderID.getInstance(aSN1Sequence.getObjectAt(n2));
        int n4 = n3 + 1;
        this.producedAt = ASN1GeneralizedTime.getInstance(aSN1Sequence.getObjectAt(n3));
        int n5 = n4 + 1;
        this.responses = (ASN1Sequence)aSN1Sequence.getObjectAt(n4);
        if (aSN1Sequence.size() > n5) {
            this.responseExtensions = Extensions.getInstance((ASN1TaggedObject)aSN1Sequence.getObjectAt(n5), true);
        }
    }

    public ResponseData(ResponderID responderID, ASN1GeneralizedTime aSN1GeneralizedTime, ASN1Sequence aSN1Sequence, Extensions extensions) {
        this(V1, responderID, aSN1GeneralizedTime, aSN1Sequence, extensions);
    }

    public ResponseData(ResponderID responderID, ASN1GeneralizedTime aSN1GeneralizedTime, ASN1Sequence aSN1Sequence, X509Extensions x509Extensions) {
        this(V1, responderID, ASN1GeneralizedTime.getInstance(aSN1GeneralizedTime), aSN1Sequence, Extensions.getInstance(x509Extensions));
    }

    public static ResponseData getInstance(Object object) {
        if (object instanceof ResponseData) {
            return (ResponseData)object;
        }
        if (object != null) {
            return new ResponseData(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public static ResponseData getInstance(ASN1TaggedObject aSN1TaggedObject, boolean bl) {
        return ResponseData.getInstance(ASN1Sequence.getInstance(aSN1TaggedObject, bl));
    }

    public ASN1GeneralizedTime getProducedAt() {
        return this.producedAt;
    }

    public ResponderID getResponderID() {
        return this.responderID;
    }

    public Extensions getResponseExtensions() {
        return this.responseExtensions;
    }

    public ASN1Sequence getResponses() {
        return this.responses;
    }

    public ASN1Integer getVersion() {
        return this.version;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        if (this.versionPresent || !this.version.equals(V1)) {
            aSN1EncodableVector.add(new DERTaggedObject(true, 0, this.version));
        }
        aSN1EncodableVector.add(this.responderID);
        aSN1EncodableVector.add(this.producedAt);
        aSN1EncodableVector.add(this.responses);
        if (this.responseExtensions != null) {
            aSN1EncodableVector.add(new DERTaggedObject(true, 1, this.responseExtensions));
        }
        return new DERSequence(aSN1EncodableVector);
    }
}

