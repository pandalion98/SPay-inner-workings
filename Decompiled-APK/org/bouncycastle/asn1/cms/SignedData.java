package org.bouncycastle.asn1.cms;

import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.BERSequence;
import org.bouncycastle.asn1.BERSet;
import org.bouncycastle.asn1.BERTaggedObject;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class SignedData extends ASN1Object {
    private static final ASN1Integer VERSION_1;
    private static final ASN1Integer VERSION_3;
    private static final ASN1Integer VERSION_4;
    private static final ASN1Integer VERSION_5;
    private ASN1Set certificates;
    private boolean certsBer;
    private ContentInfo contentInfo;
    private ASN1Set crls;
    private boolean crlsBer;
    private ASN1Set digestAlgorithms;
    private ASN1Set signerInfos;
    private ASN1Integer version;

    static {
        VERSION_1 = new ASN1Integer(1);
        VERSION_3 = new ASN1Integer(3);
        VERSION_4 = new ASN1Integer(4);
        VERSION_5 = new ASN1Integer(5);
    }

    private SignedData(ASN1Sequence aSN1Sequence) {
        Enumeration objects = aSN1Sequence.getObjects();
        this.version = ASN1Integer.getInstance(objects.nextElement());
        this.digestAlgorithms = (ASN1Set) objects.nextElement();
        this.contentInfo = ContentInfo.getInstance(objects.nextElement());
        while (objects.hasMoreElements()) {
            ASN1Primitive aSN1Primitive = (ASN1Primitive) objects.nextElement();
            if (aSN1Primitive instanceof ASN1TaggedObject) {
                ASN1TaggedObject aSN1TaggedObject = (ASN1TaggedObject) aSN1Primitive;
                switch (aSN1TaggedObject.getTagNo()) {
                    case ECCurve.COORD_AFFINE /*0*/:
                        this.certsBer = aSN1TaggedObject instanceof BERTaggedObject;
                        this.certificates = ASN1Set.getInstance(aSN1TaggedObject, false);
                        break;
                    case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                        this.crlsBer = aSN1TaggedObject instanceof BERTaggedObject;
                        this.crls = ASN1Set.getInstance(aSN1TaggedObject, false);
                        break;
                    default:
                        throw new IllegalArgumentException("unknown tag value " + aSN1TaggedObject.getTagNo());
                }
            }
            this.signerInfos = (ASN1Set) aSN1Primitive;
        }
    }

    public SignedData(ASN1Set aSN1Set, ContentInfo contentInfo, ASN1Set aSN1Set2, ASN1Set aSN1Set3, ASN1Set aSN1Set4) {
        this.version = calculateVersion(contentInfo.getContentType(), aSN1Set2, aSN1Set3, aSN1Set4);
        this.digestAlgorithms = aSN1Set;
        this.contentInfo = contentInfo;
        this.certificates = aSN1Set2;
        this.crls = aSN1Set3;
        this.signerInfos = aSN1Set4;
        this.crlsBer = aSN1Set3 instanceof BERSet;
        this.certsBer = aSN1Set2 instanceof BERSet;
    }

    private ASN1Integer calculateVersion(ASN1ObjectIdentifier aSN1ObjectIdentifier, ASN1Set aSN1Set, ASN1Set aSN1Set2, ASN1Set aSN1Set3) {
        Object obj;
        Object obj2;
        Object obj3;
        Object obj4 = null;
        if (aSN1Set != null) {
            Enumeration objects = aSN1Set.getObjects();
            obj = null;
            obj2 = null;
            obj3 = null;
            while (objects.hasMoreElements()) {
                Object nextElement = objects.nextElement();
                if (nextElement instanceof ASN1TaggedObject) {
                    ASN1TaggedObject instance = ASN1TaggedObject.getInstance(nextElement);
                    if (instance.getTagNo() == 1) {
                        obj2 = 1;
                    } else if (instance.getTagNo() == 2) {
                        obj = 1;
                    } else if (instance.getTagNo() == 3) {
                        obj3 = 1;
                    }
                }
            }
        } else {
            obj = null;
            obj2 = null;
            obj3 = null;
        }
        if (obj3 != null) {
            return new ASN1Integer(5);
        }
        if (aSN1Set2 != null) {
            Enumeration objects2 = aSN1Set2.getObjects();
            while (objects2.hasMoreElements()) {
                if (objects2.nextElement() instanceof ASN1TaggedObject) {
                    obj4 = 1;
                }
            }
        }
        return obj4 != null ? VERSION_5 : obj != null ? VERSION_4 : obj2 != null ? VERSION_3 : checkForVersion3(aSN1Set3) ? VERSION_3 : !CMSObjectIdentifiers.data.equals(aSN1ObjectIdentifier) ? VERSION_3 : VERSION_1;
    }

    private boolean checkForVersion3(ASN1Set aSN1Set) {
        Enumeration objects = aSN1Set.getObjects();
        while (objects.hasMoreElements()) {
            if (SignerInfo.getInstance(objects.nextElement()).getVersion().getValue().intValue() == 3) {
                return true;
            }
        }
        return false;
    }

    public static SignedData getInstance(Object obj) {
        return obj instanceof SignedData ? (SignedData) obj : obj != null ? new SignedData(ASN1Sequence.getInstance(obj)) : null;
    }

    public ASN1Set getCRLs() {
        return this.crls;
    }

    public ASN1Set getCertificates() {
        return this.certificates;
    }

    public ASN1Set getDigestAlgorithms() {
        return this.digestAlgorithms;
    }

    public ContentInfo getEncapContentInfo() {
        return this.contentInfo;
    }

    public ASN1Set getSignerInfos() {
        return this.signerInfos;
    }

    public ASN1Integer getVersion() {
        return this.version;
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.version);
        aSN1EncodableVector.add(this.digestAlgorithms);
        aSN1EncodableVector.add(this.contentInfo);
        if (this.certificates != null) {
            if (this.certsBer) {
                aSN1EncodableVector.add(new BERTaggedObject(false, 0, this.certificates));
            } else {
                aSN1EncodableVector.add(new DERTaggedObject(false, 0, this.certificates));
            }
        }
        if (this.crls != null) {
            if (this.crlsBer) {
                aSN1EncodableVector.add(new BERTaggedObject(false, 1, this.crls));
            } else {
                aSN1EncodableVector.add(new DERTaggedObject(false, 1, this.crls));
            }
        }
        aSN1EncodableVector.add(this.signerInfos);
        return new BERSequence(aSN1EncodableVector);
    }
}
