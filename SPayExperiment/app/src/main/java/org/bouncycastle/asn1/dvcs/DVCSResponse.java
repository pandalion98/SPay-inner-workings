/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.Class
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.asn1.dvcs;

import java.io.IOException;
import org.bouncycastle.asn1.ASN1Choice;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.dvcs.DVCSCertInfo;
import org.bouncycastle.asn1.dvcs.DVCSErrorNotice;

public class DVCSResponse
extends ASN1Object
implements ASN1Choice {
    private DVCSCertInfo dvCertInfo;
    private DVCSErrorNotice dvErrorNote;

    public DVCSResponse(DVCSCertInfo dVCSCertInfo) {
        this.dvCertInfo = dVCSCertInfo;
    }

    public DVCSResponse(DVCSErrorNotice dVCSErrorNotice) {
        this.dvErrorNote = dVCSErrorNotice;
    }

    public static DVCSResponse getInstance(Object object) {
        if (object == null || object instanceof DVCSResponse) {
            return (DVCSResponse)object;
        }
        if (object instanceof byte[]) {
            try {
                DVCSResponse dVCSResponse = DVCSResponse.getInstance(ASN1Primitive.fromByteArray((byte[])object));
                return dVCSResponse;
            }
            catch (IOException iOException) {
                throw new IllegalArgumentException("failed to construct sequence from byte[]: " + iOException.getMessage());
            }
        }
        if (object instanceof ASN1Sequence) {
            return new DVCSResponse(DVCSCertInfo.getInstance(object));
        }
        if (object instanceof ASN1TaggedObject) {
            return new DVCSResponse(DVCSErrorNotice.getInstance(ASN1TaggedObject.getInstance(object), false));
        }
        throw new IllegalArgumentException("Couldn't convert from object to DVCSResponse: " + object.getClass().getName());
    }

    public static DVCSResponse getInstance(ASN1TaggedObject aSN1TaggedObject, boolean bl) {
        return DVCSResponse.getInstance(ASN1Sequence.getInstance(aSN1TaggedObject, bl));
    }

    public DVCSCertInfo getCertInfo() {
        return this.dvCertInfo;
    }

    public DVCSErrorNotice getErrorNotice() {
        return this.dvErrorNote;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        if (this.dvCertInfo != null) {
            return this.dvCertInfo.toASN1Primitive();
        }
        return new DERTaggedObject(0, this.dvErrorNote);
    }

    public String toString() {
        if (this.dvCertInfo != null) {
            return "DVCSResponse {\ndvCertInfo: " + this.dvCertInfo.toString() + "}\n";
        }
        if (this.dvErrorNote != null) {
            return "DVCSResponse {\ndvErrorNote: " + this.dvErrorNote.toString() + "}\n";
        }
        return null;
    }
}

