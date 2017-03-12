package org.bouncycastle.asn1.dvcs;

import java.math.BigInteger;
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
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.PolicyInformation;

public class DVCSRequestInformation extends ASN1Object {
    private static final int DEFAULT_VERSION = 1;
    private static final int TAG_DATA_LOCATIONS = 3;
    private static final int TAG_DVCS = 2;
    private static final int TAG_EXTENSIONS = 4;
    private static final int TAG_REQUESTER = 0;
    private static final int TAG_REQUEST_POLICY = 1;
    private GeneralNames dataLocations;
    private GeneralNames dvcs;
    private Extensions extensions;
    private BigInteger nonce;
    private PolicyInformation requestPolicy;
    private DVCSTime requestTime;
    private GeneralNames requester;
    private ServiceType service;
    private int version;

    private DVCSRequestInformation(ASN1Sequence aSN1Sequence) {
        int i = TAG_REQUEST_POLICY;
        this.version = TAG_REQUEST_POLICY;
        if (aSN1Sequence.getObjectAt(TAG_REQUESTER) instanceof ASN1Integer) {
            this.version = ASN1Integer.getInstance(aSN1Sequence.getObjectAt(TAG_REQUESTER)).getValue().intValue();
        } else {
            this.version = TAG_REQUEST_POLICY;
            i = TAG_REQUESTER;
        }
        int i2 = i + TAG_REQUEST_POLICY;
        this.service = ServiceType.getInstance(aSN1Sequence.getObjectAt(i));
        for (i = i2; i < aSN1Sequence.size(); i += TAG_REQUEST_POLICY) {
            ASN1Encodable objectAt = aSN1Sequence.getObjectAt(i);
            if (objectAt instanceof ASN1Integer) {
                this.nonce = ASN1Integer.getInstance(objectAt).getValue();
            } else if (objectAt instanceof ASN1GeneralizedTime) {
                this.requestTime = DVCSTime.getInstance(objectAt);
            } else if (objectAt instanceof ASN1TaggedObject) {
                ASN1TaggedObject instance = ASN1TaggedObject.getInstance(objectAt);
                switch (instance.getTagNo()) {
                    case TAG_REQUESTER /*0*/:
                        this.requester = GeneralNames.getInstance(instance, false);
                        break;
                    case TAG_REQUEST_POLICY /*1*/:
                        this.requestPolicy = PolicyInformation.getInstance(ASN1Sequence.getInstance(instance, false));
                        break;
                    case TAG_DVCS /*2*/:
                        this.dvcs = GeneralNames.getInstance(instance, false);
                        break;
                    case TAG_DATA_LOCATIONS /*3*/:
                        this.dataLocations = GeneralNames.getInstance(instance, false);
                        break;
                    case TAG_EXTENSIONS /*4*/:
                        this.extensions = Extensions.getInstance(instance, false);
                        break;
                    default:
                        break;
                }
            } else {
                this.requestTime = DVCSTime.getInstance(objectAt);
            }
        }
    }

    public static DVCSRequestInformation getInstance(Object obj) {
        return obj instanceof DVCSRequestInformation ? (DVCSRequestInformation) obj : obj != null ? new DVCSRequestInformation(ASN1Sequence.getInstance(obj)) : null;
    }

    public static DVCSRequestInformation getInstance(ASN1TaggedObject aSN1TaggedObject, boolean z) {
        return getInstance(ASN1Sequence.getInstance(aSN1TaggedObject, z));
    }

    public GeneralNames getDVCS() {
        return this.dvcs;
    }

    public GeneralNames getDataLocations() {
        return this.dataLocations;
    }

    public Extensions getExtensions() {
        return this.extensions;
    }

    public BigInteger getNonce() {
        return this.nonce;
    }

    public PolicyInformation getRequestPolicy() {
        return this.requestPolicy;
    }

    public DVCSTime getRequestTime() {
        return this.requestTime;
    }

    public GeneralNames getRequester() {
        return this.requester;
    }

    public ServiceType getService() {
        return this.service;
    }

    public int getVersion() {
        return this.version;
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        if (this.version != TAG_REQUEST_POLICY) {
            aSN1EncodableVector.add(new ASN1Integer((long) this.version));
        }
        aSN1EncodableVector.add(this.service);
        if (this.nonce != null) {
            aSN1EncodableVector.add(new ASN1Integer(this.nonce));
        }
        if (this.requestTime != null) {
            aSN1EncodableVector.add(this.requestTime);
        }
        int[] iArr = new int[]{TAG_REQUESTER, TAG_REQUEST_POLICY, TAG_DVCS, TAG_DATA_LOCATIONS, TAG_EXTENSIONS};
        ASN1Encodable[] aSN1EncodableArr = new ASN1Encodable[]{this.requester, this.requestPolicy, this.dvcs, this.dataLocations, this.extensions};
        for (int i = TAG_REQUESTER; i < iArr.length; i += TAG_REQUEST_POLICY) {
            int i2 = iArr[i];
            ASN1Encodable aSN1Encodable = aSN1EncodableArr[i];
            if (aSN1Encodable != null) {
                aSN1EncodableVector.add(new DERTaggedObject(false, i2, aSN1Encodable));
            }
        }
        return new DERSequence(aSN1EncodableVector);
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("DVCSRequestInformation {\n");
        if (this.version != TAG_REQUEST_POLICY) {
            stringBuffer.append("version: " + this.version + "\n");
        }
        stringBuffer.append("service: " + this.service + "\n");
        if (this.nonce != null) {
            stringBuffer.append("nonce: " + this.nonce + "\n");
        }
        if (this.requestTime != null) {
            stringBuffer.append("requestTime: " + this.requestTime + "\n");
        }
        if (this.requester != null) {
            stringBuffer.append("requester: " + this.requester + "\n");
        }
        if (this.requestPolicy != null) {
            stringBuffer.append("requestPolicy: " + this.requestPolicy + "\n");
        }
        if (this.dvcs != null) {
            stringBuffer.append("dvcs: " + this.dvcs + "\n");
        }
        if (this.dataLocations != null) {
            stringBuffer.append("dataLocations: " + this.dataLocations + "\n");
        }
        if (this.extensions != null) {
            stringBuffer.append("extensions: " + this.extensions + "\n");
        }
        stringBuffer.append("}\n");
        return stringBuffer.toString();
    }
}
