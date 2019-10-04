/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.IssuerSerial;
import org.bouncycastle.asn1.x509.ObjectDigestInfo;

public class V2Form
extends ASN1Object {
    IssuerSerial baseCertificateID;
    GeneralNames issuerName;
    ObjectDigestInfo objectDigestInfo;

    /*
     * Enabled aggressive block sorting
     */
    public V2Form(ASN1Sequence aSN1Sequence) {
        int n2;
        if (aSN1Sequence.size() > 3) {
            throw new IllegalArgumentException("Bad sequence size: " + aSN1Sequence.size());
        }
        if (!(aSN1Sequence.getObjectAt(0) instanceof ASN1TaggedObject)) {
            this.issuerName = GeneralNames.getInstance(aSN1Sequence.getObjectAt(0));
            n2 = 1;
        } else {
            n2 = 0;
        }
        while (n2 != aSN1Sequence.size()) {
            ASN1TaggedObject aSN1TaggedObject = ASN1TaggedObject.getInstance(aSN1Sequence.getObjectAt(n2));
            if (aSN1TaggedObject.getTagNo() == 0) {
                this.baseCertificateID = IssuerSerial.getInstance(aSN1TaggedObject, false);
            } else {
                if (aSN1TaggedObject.getTagNo() != 1) {
                    throw new IllegalArgumentException("Bad tag number: " + aSN1TaggedObject.getTagNo());
                }
                this.objectDigestInfo = ObjectDigestInfo.getInstance(aSN1TaggedObject, false);
            }
            ++n2;
        }
        return;
    }

    public V2Form(GeneralNames generalNames) {
        this(generalNames, null, null);
    }

    public V2Form(GeneralNames generalNames, IssuerSerial issuerSerial) {
        this(generalNames, issuerSerial, null);
    }

    public V2Form(GeneralNames generalNames, IssuerSerial issuerSerial, ObjectDigestInfo objectDigestInfo) {
        this.issuerName = generalNames;
        this.baseCertificateID = issuerSerial;
        this.objectDigestInfo = objectDigestInfo;
    }

    public V2Form(GeneralNames generalNames, ObjectDigestInfo objectDigestInfo) {
        this(generalNames, null, objectDigestInfo);
    }

    public static V2Form getInstance(Object object) {
        if (object instanceof V2Form) {
            return (V2Form)object;
        }
        if (object != null) {
            return new V2Form(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public static V2Form getInstance(ASN1TaggedObject aSN1TaggedObject, boolean bl) {
        return V2Form.getInstance(ASN1Sequence.getInstance(aSN1TaggedObject, bl));
    }

    public IssuerSerial getBaseCertificateID() {
        return this.baseCertificateID;
    }

    public GeneralNames getIssuerName() {
        return this.issuerName;
    }

    public ObjectDigestInfo getObjectDigestInfo() {
        return this.objectDigestInfo;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        if (this.issuerName != null) {
            aSN1EncodableVector.add(this.issuerName);
        }
        if (this.baseCertificateID != null) {
            aSN1EncodableVector.add(new DERTaggedObject(false, 0, this.baseCertificateID));
        }
        if (this.objectDigestInfo != null) {
            aSN1EncodableVector.add(new DERTaggedObject(false, 1, this.objectDigestInfo));
        }
        return new DERSequence(aSN1EncodableVector);
    }
}

