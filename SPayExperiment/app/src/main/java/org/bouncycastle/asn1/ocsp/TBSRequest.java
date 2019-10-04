/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.asn1.ocsp;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.X509Extensions;

public class TBSRequest
extends ASN1Object {
    private static final ASN1Integer V1 = new ASN1Integer(0L);
    Extensions requestExtensions;
    ASN1Sequence requestList;
    GeneralName requestorName;
    ASN1Integer version;
    boolean versionSet;

    /*
     * Enabled aggressive block sorting
     */
    private TBSRequest(ASN1Sequence aSN1Sequence) {
        int n2;
        block4 : {
            block5 : {
                block2 : {
                    block3 : {
                        if (!(aSN1Sequence.getObjectAt(0) instanceof ASN1TaggedObject)) break block2;
                        if (((ASN1TaggedObject)aSN1Sequence.getObjectAt(0)).getTagNo() != 0) break block3;
                        this.versionSet = true;
                        this.version = ASN1Integer.getInstance((ASN1TaggedObject)aSN1Sequence.getObjectAt(0), true);
                        n2 = 1;
                        break block4;
                    }
                    this.version = V1;
                    break block5;
                }
                this.version = V1;
            }
            n2 = 0;
        }
        if (aSN1Sequence.getObjectAt(n2) instanceof ASN1TaggedObject) {
            int n3 = n2 + 1;
            this.requestorName = GeneralName.getInstance((ASN1TaggedObject)aSN1Sequence.getObjectAt(n2), true);
            n2 = n3;
        }
        int n4 = n2 + 1;
        this.requestList = (ASN1Sequence)aSN1Sequence.getObjectAt(n2);
        if (aSN1Sequence.size() == n4 + 1) {
            this.requestExtensions = Extensions.getInstance((ASN1TaggedObject)aSN1Sequence.getObjectAt(n4), true);
        }
    }

    public TBSRequest(GeneralName generalName, ASN1Sequence aSN1Sequence, Extensions extensions) {
        this.version = V1;
        this.requestorName = generalName;
        this.requestList = aSN1Sequence;
        this.requestExtensions = extensions;
    }

    public TBSRequest(GeneralName generalName, ASN1Sequence aSN1Sequence, X509Extensions x509Extensions) {
        this.version = V1;
        this.requestorName = generalName;
        this.requestList = aSN1Sequence;
        this.requestExtensions = Extensions.getInstance(x509Extensions);
    }

    public static TBSRequest getInstance(Object object) {
        if (object instanceof TBSRequest) {
            return (TBSRequest)object;
        }
        if (object != null) {
            return new TBSRequest(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public static TBSRequest getInstance(ASN1TaggedObject aSN1TaggedObject, boolean bl) {
        return TBSRequest.getInstance(ASN1Sequence.getInstance(aSN1TaggedObject, bl));
    }

    public Extensions getRequestExtensions() {
        return this.requestExtensions;
    }

    public ASN1Sequence getRequestList() {
        return this.requestList;
    }

    public GeneralName getRequestorName() {
        return this.requestorName;
    }

    public ASN1Integer getVersion() {
        return this.version;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        if (!this.version.equals(V1) || this.versionSet) {
            aSN1EncodableVector.add(new DERTaggedObject(true, 0, this.version));
        }
        if (this.requestorName != null) {
            aSN1EncodableVector.add(new DERTaggedObject(true, 1, this.requestorName));
        }
        aSN1EncodableVector.add(this.requestList);
        if (this.requestExtensions != null) {
            aSN1EncodableVector.add(new DERTaggedObject(true, 2, this.requestExtensions));
        }
        return new DERSequence(aSN1EncodableVector);
    }
}

