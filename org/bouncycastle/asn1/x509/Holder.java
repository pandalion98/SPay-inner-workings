package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;

public class Holder extends ASN1Object {
    public static final int V1_CERTIFICATE_HOLDER = 0;
    public static final int V2_CERTIFICATE_HOLDER = 1;
    IssuerSerial baseCertificateID;
    GeneralNames entityName;
    ObjectDigestInfo objectDigestInfo;
    private int version;

    private Holder(ASN1Sequence aSN1Sequence) {
        this.version = V2_CERTIFICATE_HOLDER;
        if (aSN1Sequence.size() > 3) {
            throw new IllegalArgumentException("Bad sequence size: " + aSN1Sequence.size());
        }
        for (int i = V1_CERTIFICATE_HOLDER; i != aSN1Sequence.size(); i += V2_CERTIFICATE_HOLDER) {
            ASN1TaggedObject instance = ASN1TaggedObject.getInstance(aSN1Sequence.getObjectAt(i));
            switch (instance.getTagNo()) {
                case V1_CERTIFICATE_HOLDER /*0*/:
                    this.baseCertificateID = IssuerSerial.getInstance(instance, false);
                    break;
                case V2_CERTIFICATE_HOLDER /*1*/:
                    this.entityName = GeneralNames.getInstance(instance, false);
                    break;
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                    this.objectDigestInfo = ObjectDigestInfo.getInstance(instance, false);
                    break;
                default:
                    throw new IllegalArgumentException("unknown tag in Holder");
            }
        }
        this.version = V2_CERTIFICATE_HOLDER;
    }

    private Holder(ASN1TaggedObject aSN1TaggedObject) {
        this.version = V2_CERTIFICATE_HOLDER;
        switch (aSN1TaggedObject.getTagNo()) {
            case V1_CERTIFICATE_HOLDER /*0*/:
                this.baseCertificateID = IssuerSerial.getInstance(aSN1TaggedObject, true);
                break;
            case V2_CERTIFICATE_HOLDER /*1*/:
                this.entityName = GeneralNames.getInstance(aSN1TaggedObject, true);
                break;
            default:
                throw new IllegalArgumentException("unknown tag in Holder");
        }
        this.version = V1_CERTIFICATE_HOLDER;
    }

    public Holder(GeneralNames generalNames) {
        this(generalNames, (int) V2_CERTIFICATE_HOLDER);
    }

    public Holder(GeneralNames generalNames, int i) {
        this.version = V2_CERTIFICATE_HOLDER;
        this.entityName = generalNames;
        this.version = i;
    }

    public Holder(IssuerSerial issuerSerial) {
        this(issuerSerial, (int) V2_CERTIFICATE_HOLDER);
    }

    public Holder(IssuerSerial issuerSerial, int i) {
        this.version = V2_CERTIFICATE_HOLDER;
        this.baseCertificateID = issuerSerial;
        this.version = i;
    }

    public Holder(ObjectDigestInfo objectDigestInfo) {
        this.version = V2_CERTIFICATE_HOLDER;
        this.objectDigestInfo = objectDigestInfo;
    }

    public static Holder getInstance(Object obj) {
        return obj instanceof Holder ? (Holder) obj : obj instanceof ASN1TaggedObject ? new Holder(ASN1TaggedObject.getInstance(obj)) : obj != null ? new Holder(ASN1Sequence.getInstance(obj)) : null;
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

    public ASN1Primitive toASN1Primitive() {
        if (this.version != V2_CERTIFICATE_HOLDER) {
            return this.entityName != null ? new DERTaggedObject(true, V2_CERTIFICATE_HOLDER, this.entityName) : new DERTaggedObject(true, V1_CERTIFICATE_HOLDER, this.baseCertificateID);
        } else {
            ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
            if (this.baseCertificateID != null) {
                aSN1EncodableVector.add(new DERTaggedObject(false, V1_CERTIFICATE_HOLDER, this.baseCertificateID));
            }
            if (this.entityName != null) {
                aSN1EncodableVector.add(new DERTaggedObject(false, V2_CERTIFICATE_HOLDER, this.entityName));
            }
            if (this.objectDigestInfo != null) {
                aSN1EncodableVector.add(new DERTaggedObject(false, 2, this.objectDigestInfo));
            }
            return new DERSequence(aSN1EncodableVector);
        }
    }
}
