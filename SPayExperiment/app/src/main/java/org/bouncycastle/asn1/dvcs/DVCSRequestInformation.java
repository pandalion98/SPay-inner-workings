/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuffer
 *  java.math.BigInteger
 */
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
import org.bouncycastle.asn1.dvcs.DVCSTime;
import org.bouncycastle.asn1.dvcs.ServiceType;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.PolicyInformation;

public class DVCSRequestInformation
extends ASN1Object {
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

    /*
     * Enabled aggressive block sorting
     */
    private DVCSRequestInformation(ASN1Sequence aSN1Sequence) {
        int n2 = 1;
        this.version = n2;
        if (aSN1Sequence.getObjectAt(0) instanceof ASN1Integer) {
            this.version = ASN1Integer.getInstance(aSN1Sequence.getObjectAt(0)).getValue().intValue();
        } else {
            this.version = n2;
            n2 = 0;
        }
        int n3 = n2 + 1;
        this.service = ServiceType.getInstance(aSN1Sequence.getObjectAt(n2));
        int n4 = n3;
        while (n4 < aSN1Sequence.size()) {
            ASN1Encodable aSN1Encodable = aSN1Sequence.getObjectAt(n4);
            if (aSN1Encodable instanceof ASN1Integer) {
                this.nonce = ASN1Integer.getInstance(aSN1Encodable).getValue();
            } else if (aSN1Encodable instanceof ASN1GeneralizedTime) {
                this.requestTime = DVCSTime.getInstance(aSN1Encodable);
            } else if (aSN1Encodable instanceof ASN1TaggedObject) {
                ASN1TaggedObject aSN1TaggedObject = ASN1TaggedObject.getInstance(aSN1Encodable);
                switch (aSN1TaggedObject.getTagNo()) {
                    default: {
                        break;
                    }
                    case 0: {
                        this.requester = GeneralNames.getInstance(aSN1TaggedObject, false);
                        break;
                    }
                    case 1: {
                        this.requestPolicy = PolicyInformation.getInstance(ASN1Sequence.getInstance(aSN1TaggedObject, false));
                        break;
                    }
                    case 2: {
                        this.dvcs = GeneralNames.getInstance(aSN1TaggedObject, false);
                        break;
                    }
                    case 3: {
                        this.dataLocations = GeneralNames.getInstance(aSN1TaggedObject, false);
                        break;
                    }
                    case 4: {
                        this.extensions = Extensions.getInstance(aSN1TaggedObject, false);
                        break;
                    }
                }
            } else {
                this.requestTime = DVCSTime.getInstance(aSN1Encodable);
            }
            ++n4;
        }
        return;
    }

    public static DVCSRequestInformation getInstance(Object object) {
        if (object instanceof DVCSRequestInformation) {
            return (DVCSRequestInformation)object;
        }
        if (object != null) {
            return new DVCSRequestInformation(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public static DVCSRequestInformation getInstance(ASN1TaggedObject aSN1TaggedObject, boolean bl) {
        return DVCSRequestInformation.getInstance(ASN1Sequence.getInstance(aSN1TaggedObject, bl));
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

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        if (this.version != 1) {
            aSN1EncodableVector.add(new ASN1Integer(this.version));
        }
        aSN1EncodableVector.add(this.service);
        if (this.nonce != null) {
            aSN1EncodableVector.add(new ASN1Integer(this.nonce));
        }
        if (this.requestTime != null) {
            aSN1EncodableVector.add(this.requestTime);
        }
        int[] arrn = new int[]{0, 1, 2, 3, 4};
        ASN1Encodable[] arraSN1Encodable = new ASN1Encodable[]{this.requester, this.requestPolicy, this.dvcs, this.dataLocations, this.extensions};
        for (int i2 = 0; i2 < arrn.length; ++i2) {
            int n2 = arrn[i2];
            ASN1Encodable aSN1Encodable = arraSN1Encodable[i2];
            if (aSN1Encodable == null) continue;
            aSN1EncodableVector.add(new DERTaggedObject(false, n2, aSN1Encodable));
        }
        return new DERSequence(aSN1EncodableVector);
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("DVCSRequestInformation {\n");
        if (this.version != 1) {
            stringBuffer.append("version: " + this.version + "\n");
        }
        stringBuffer.append("service: " + this.service + "\n");
        if (this.nonce != null) {
            stringBuffer.append("nonce: " + (Object)this.nonce + "\n");
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

