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

public class Holder
extends ASN1Object {
    public static final int V1_CERTIFICATE_HOLDER = 0;
    public static final int V2_CERTIFICATE_HOLDER = 1;
    IssuerSerial baseCertificateID;
    GeneralNames entityName;
    ObjectDigestInfo objectDigestInfo;
    private int version = 1;

    /*
     * Enabled aggressive block sorting
     */
    private Holder(ASN1Sequence aSN1Sequence) {
        if (aSN1Sequence.size() > 3) {
            throw new IllegalArgumentException("Bad sequence size: " + aSN1Sequence.size());
        }
        int n2 = 0;
        do {
            if (n2 == aSN1Sequence.size()) {
                this.version = 1;
                return;
            }
            ASN1TaggedObject aSN1TaggedObject = ASN1TaggedObject.getInstance(aSN1Sequence.getObjectAt(n2));
            switch (aSN1TaggedObject.getTagNo()) {
                default: {
                    throw new IllegalArgumentException("unknown tag in Holder");
                }
                case 0: {
                    this.baseCertificateID = IssuerSerial.getInstance(aSN1TaggedObject, false);
                    break;
                }
                case 1: {
                    this.entityName = GeneralNames.getInstance(aSN1TaggedObject, false);
                    break;
                }
                case 2: {
                    this.objectDigestInfo = ObjectDigestInfo.getInstance(aSN1TaggedObject, false);
                }
            }
            ++n2;
        } while (true);
    }

    /*
     * Enabled aggressive block sorting
     */
    private Holder(ASN1TaggedObject aSN1TaggedObject) {
        switch (aSN1TaggedObject.getTagNo()) {
            default: {
                throw new IllegalArgumentException("unknown tag in Holder");
            }
            case 0: {
                this.baseCertificateID = IssuerSerial.getInstance(aSN1TaggedObject, true);
                break;
            }
            case 1: {
                this.entityName = GeneralNames.getInstance(aSN1TaggedObject, true);
            }
        }
        this.version = 0;
    }

    public Holder(GeneralNames generalNames) {
        this(generalNames, 1);
    }

    public Holder(GeneralNames generalNames, int n2) {
        this.entityName = generalNames;
        this.version = n2;
    }

    public Holder(IssuerSerial issuerSerial) {
        this(issuerSerial, 1);
    }

    public Holder(IssuerSerial issuerSerial, int n2) {
        this.baseCertificateID = issuerSerial;
        this.version = n2;
    }

    public Holder(ObjectDigestInfo objectDigestInfo) {
        this.objectDigestInfo = objectDigestInfo;
    }

    public static Holder getInstance(Object object) {
        if (object instanceof Holder) {
            return (Holder)object;
        }
        if (object instanceof ASN1TaggedObject) {
            return new Holder(ASN1TaggedObject.getInstance(object));
        }
        if (object != null) {
            return new Holder(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public IssuerSerial getBaseCertificateID() {
        return this.baseCertificateID;
    }

    public GeneralNames getEntityName() {
        return this.entityName;
    }

    public ObjectDigestInfo getObjectDigestInfo() {
        return this.objectDigestInfo;
    }

    public int getVersion() {
        return this.version;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        if (this.version == 1) {
            ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
            if (this.baseCertificateID != null) {
                aSN1EncodableVector.add(new DERTaggedObject(false, 0, this.baseCertificateID));
            }
            if (this.entityName != null) {
                aSN1EncodableVector.add(new DERTaggedObject(false, 1, this.entityName));
            }
            if (this.objectDigestInfo != null) {
                aSN1EncodableVector.add(new DERTaggedObject(false, 2, this.objectDigestInfo));
            }
            return new DERSequence(aSN1EncodableVector);
        }
        if (this.entityName != null) {
            return new DERTaggedObject(true, 1, this.entityName);
        }
        return new DERTaggedObject(true, 0, this.baseCertificateID);
    }
}

