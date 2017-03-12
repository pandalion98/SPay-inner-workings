package org.bouncycastle.asn1.dvcs;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.cmp.PKIStatusInfo;
import org.bouncycastle.asn1.x509.DigestInfo;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.asn1.x509.PolicyInformation;

public class DVCSCertInfo extends ASN1Object {
    private static final int DEFAULT_VERSION = 1;
    private static final int TAG_CERTS = 3;
    private static final int TAG_DV_STATUS = 0;
    private static final int TAG_POLICY = 1;
    private static final int TAG_REQ_SIGNATURE = 2;
    private ASN1Sequence certs;
    private DVCSRequestInformation dvReqInfo;
    private PKIStatusInfo dvStatus;
    private Extensions extensions;
    private DigestInfo messageImprint;
    private PolicyInformation policy;
    private ASN1Set reqSignature;
    private DVCSTime responseTime;
    private ASN1Integer serialNumber;
    private int version;

    private DVCSCertInfo(ASN1Sequence aSN1Sequence) {
        int i = TAG_POLICY;
        this.version = TAG_POLICY;
        Object objectAt = aSN1Sequence.getObjectAt(TAG_DV_STATUS);
        try {
            this.version = ASN1Integer.getInstance(objectAt).getValue().intValue();
            i = TAG_REQ_SIGNATURE;
            objectAt = aSN1Sequence.getObjectAt(TAG_POLICY);
        } catch (IllegalArgumentException e) {
        }
        this.dvReqInfo = DVCSRequestInformation.getInstance(objectAt);
        int i2 = i + TAG_POLICY;
        this.messageImprint = DigestInfo.getInstance(aSN1Sequence.getObjectAt(i));
        i = i2 + TAG_POLICY;
        this.serialNumber = ASN1Integer.getInstance(aSN1Sequence.getObjectAt(i2));
        i2 = i + TAG_POLICY;
        this.responseTime = DVCSTime.getInstance(aSN1Sequence.getObjectAt(i));
        while (i2 < aSN1Sequence.size()) {
            i = i2 + TAG_POLICY;
            ASN1Encodable objectAt2 = aSN1Sequence.getObjectAt(i2);
            try {
                ASN1TaggedObject instance = ASN1TaggedObject.getInstance(objectAt2);
                switch (instance.getTagNo()) {
                    case TAG_DV_STATUS /*0*/:
                        this.dvStatus = PKIStatusInfo.getInstance(instance, false);
                        break;
                    case TAG_POLICY /*1*/:
                        this.policy = PolicyInformation.getInstance(ASN1Sequence.getInstance(instance, false));
                        break;
                    case TAG_REQ_SIGNATURE /*2*/:
                        this.reqSignature = ASN1Set.getInstance(instance, false);
                        break;
                    case TAG_CERTS /*3*/:
                        this.certs = ASN1Sequence.getInstance(instance, false);
                        break;
                }
                i2 = i;
            } catch (IllegalArgumentException e2) {
                try {
                    this.extensions = Extensions.getInstance(objectAt2);
                    i2 = i;
                } catch (IllegalArgumentException e3) {
                    i2 = i;
                }
            }
        }
    }

    public DVCSCertInfo(DVCSRequestInformation dVCSRequestInformation, DigestInfo digestInfo, ASN1Integer aSN1Integer, DVCSTime dVCSTime) {
        this.version = TAG_POLICY;
        this.dvReqInfo = dVCSRequestInformation;
        this.messageImprint = digestInfo;
        this.serialNumber = aSN1Integer;
        this.responseTime = dVCSTime;
    }

    public static DVCSCertInfo getInstance(Object obj) {
        return obj instanceof DVCSCertInfo ? (DVCSCertInfo) obj : obj != null ? new DVCSCertInfo(ASN1Sequence.getInstance(obj)) : null;
    }

    public static DVCSCertInfo getInstance(ASN1TaggedObject aSN1TaggedObject, boolean z) {
        return getInstance(ASN1Sequence.getInstance(aSN1TaggedObject, z));
    }

    private void setDvReqInfo(DVCSRequestInformation dVCSRequestInformation) {
        this.dvReqInfo = dVCSRequestInformation;
    }

    private void setMessageImprint(DigestInfo digestInfo) {
        this.messageImprint = digestInfo;
    }

    private void setVersion(int i) {
        this.version = i;
    }

    public TargetEtcChain[] getCerts() {
        return this.certs != null ? TargetEtcChain.arrayFromSequence(this.certs) : null;
    }

    public DVCSRequestInformation getDvReqInfo() {
        return this.dvReqInfo;
    }

    public PKIStatusInfo getDvStatus() {
        return this.dvStatus;
    }

    public Extensions getExtensions() {
        return this.extensions;
    }

    public DigestInfo getMessageImprint() {
        return this.messageImprint;
    }

    public PolicyInformation getPolicy() {
        return this.policy;
    }

    public ASN1Set getReqSignature() {
        return this.reqSignature;
    }

    public DVCSTime getResponseTime() {
        return this.responseTime;
    }

    public ASN1Integer getSerialNumber() {
        return this.serialNumber;
    }

    public int getVersion() {
        return this.version;
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        if (this.version != TAG_POLICY) {
            aSN1EncodableVector.add(new ASN1Integer((long) this.version));
        }
        aSN1EncodableVector.add(this.dvReqInfo);
        aSN1EncodableVector.add(this.messageImprint);
        aSN1EncodableVector.add(this.serialNumber);
        aSN1EncodableVector.add(this.responseTime);
        if (this.dvStatus != null) {
            aSN1EncodableVector.add(new DERTaggedObject(false, TAG_DV_STATUS, this.dvStatus));
        }
        if (this.policy != null) {
            aSN1EncodableVector.add(new DERTaggedObject(false, TAG_POLICY, this.policy));
        }
        if (this.reqSignature != null) {
            aSN1EncodableVector.add(new DERTaggedObject(false, TAG_REQ_SIGNATURE, this.reqSignature));
        }
        if (this.certs != null) {
            aSN1EncodableVector.add(new DERTaggedObject(false, TAG_CERTS, this.certs));
        }
        if (this.extensions != null) {
            aSN1EncodableVector.add(this.extensions);
        }
        return new DERSequence(aSN1EncodableVector);
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("DVCSCertInfo {\n");
        if (this.version != TAG_POLICY) {
            stringBuffer.append("version: " + this.version + "\n");
        }
        stringBuffer.append("dvReqInfo: " + this.dvReqInfo + "\n");
        stringBuffer.append("messageImprint: " + this.messageImprint + "\n");
        stringBuffer.append("serialNumber: " + this.serialNumber + "\n");
        stringBuffer.append("responseTime: " + this.responseTime + "\n");
        if (this.dvStatus != null) {
            stringBuffer.append("dvStatus: " + this.dvStatus + "\n");
        }
        if (this.policy != null) {
            stringBuffer.append("policy: " + this.policy + "\n");
        }
        if (this.reqSignature != null) {
            stringBuffer.append("reqSignature: " + this.reqSignature + "\n");
        }
        if (this.certs != null) {
            stringBuffer.append("certs: " + this.certs + "\n");
        }
        if (this.extensions != null) {
            stringBuffer.append("extensions: " + this.extensions + "\n");
        }
        stringBuffer.append("}\n");
        return stringBuffer.toString();
    }
}
