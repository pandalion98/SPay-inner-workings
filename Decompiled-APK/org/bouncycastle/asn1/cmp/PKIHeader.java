package org.bouncycastle.asn1.cmp;

import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1GeneralizedTime;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;

public class PKIHeader extends ASN1Object {
    public static final int CMP_1999 = 1;
    public static final int CMP_2000 = 2;
    public static final GeneralName NULL_NAME;
    private PKIFreeText freeText;
    private ASN1Sequence generalInfo;
    private ASN1GeneralizedTime messageTime;
    private AlgorithmIdentifier protectionAlg;
    private ASN1Integer pvno;
    private ASN1OctetString recipKID;
    private ASN1OctetString recipNonce;
    private GeneralName recipient;
    private GeneralName sender;
    private ASN1OctetString senderKID;
    private ASN1OctetString senderNonce;
    private ASN1OctetString transactionID;

    static {
        NULL_NAME = new GeneralName(X500Name.getInstance(new DERSequence()));
    }

    public PKIHeader(int i, GeneralName generalName, GeneralName generalName2) {
        this(new ASN1Integer((long) i), generalName, generalName2);
    }

    private PKIHeader(ASN1Integer aSN1Integer, GeneralName generalName, GeneralName generalName2) {
        this.pvno = aSN1Integer;
        this.sender = generalName;
        this.recipient = generalName2;
    }

    private PKIHeader(ASN1Sequence aSN1Sequence) {
        Enumeration objects = aSN1Sequence.getObjects();
        this.pvno = ASN1Integer.getInstance(objects.nextElement());
        this.sender = GeneralName.getInstance(objects.nextElement());
        this.recipient = GeneralName.getInstance(objects.nextElement());
        while (objects.hasMoreElements()) {
            ASN1TaggedObject aSN1TaggedObject = (ASN1TaggedObject) objects.nextElement();
            switch (aSN1TaggedObject.getTagNo()) {
                case ECCurve.COORD_AFFINE /*0*/:
                    this.messageTime = ASN1GeneralizedTime.getInstance(aSN1TaggedObject, true);
                    break;
                case CMP_1999 /*1*/:
                    this.protectionAlg = AlgorithmIdentifier.getInstance(aSN1TaggedObject, true);
                    break;
                case CMP_2000 /*2*/:
                    this.senderKID = ASN1OctetString.getInstance(aSN1TaggedObject, true);
                    break;
                case F2m.PPB /*3*/:
                    this.recipKID = ASN1OctetString.getInstance(aSN1TaggedObject, true);
                    break;
                case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                    this.transactionID = ASN1OctetString.getInstance(aSN1TaggedObject, true);
                    break;
                case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                    this.senderNonce = ASN1OctetString.getInstance(aSN1TaggedObject, true);
                    break;
                case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
                    this.recipNonce = ASN1OctetString.getInstance(aSN1TaggedObject, true);
                    break;
                case ECCurve.COORD_SKEWED /*7*/:
                    this.freeText = PKIFreeText.getInstance(aSN1TaggedObject, true);
                    break;
                case X509KeyUsage.keyAgreement /*8*/:
                    this.generalInfo = ASN1Sequence.getInstance(aSN1TaggedObject, true);
                    break;
                default:
                    throw new IllegalArgumentException("unknown tag number: " + aSN1TaggedObject.getTagNo());
            }
        }
    }

    private void addOptional(ASN1EncodableVector aSN1EncodableVector, int i, ASN1Encodable aSN1Encodable) {
        if (aSN1Encodable != null) {
            aSN1EncodableVector.add(new DERTaggedObject(true, i, aSN1Encodable));
        }
    }

    public static PKIHeader getInstance(Object obj) {
        return obj instanceof PKIHeader ? (PKIHeader) obj : obj != null ? new PKIHeader(ASN1Sequence.getInstance(obj)) : null;
    }

    public PKIFreeText getFreeText() {
        return this.freeText;
    }

    public InfoTypeAndValue[] getGeneralInfo() {
        if (this.generalInfo == null) {
            return null;
        }
        InfoTypeAndValue[] infoTypeAndValueArr = new InfoTypeAndValue[this.generalInfo.size()];
        for (int i = 0; i < infoTypeAndValueArr.length; i += CMP_1999) {
            infoTypeAndValueArr[i] = InfoTypeAndValue.getInstance(this.generalInfo.getObjectAt(i));
        }
        return infoTypeAndValueArr;
    }

    public ASN1GeneralizedTime getMessageTime() {
        return this.messageTime;
    }

    public AlgorithmIdentifier getProtectionAlg() {
        return this.protectionAlg;
    }

    public ASN1Integer getPvno() {
        return this.pvno;
    }

    public ASN1OctetString getRecipKID() {
        return this.recipKID;
    }

    public ASN1OctetString getRecipNonce() {
        return this.recipNonce;
    }

    public GeneralName getRecipient() {
        return this.recipient;
    }

    public GeneralName getSender() {
        return this.sender;
    }

    public ASN1OctetString getSenderKID() {
        return this.senderKID;
    }

    public ASN1OctetString getSenderNonce() {
        return this.senderNonce;
    }

    public ASN1OctetString getTransactionID() {
        return this.transactionID;
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.pvno);
        aSN1EncodableVector.add(this.sender);
        aSN1EncodableVector.add(this.recipient);
        addOptional(aSN1EncodableVector, 0, this.messageTime);
        addOptional(aSN1EncodableVector, CMP_1999, this.protectionAlg);
        addOptional(aSN1EncodableVector, CMP_2000, this.senderKID);
        addOptional(aSN1EncodableVector, 3, this.recipKID);
        addOptional(aSN1EncodableVector, 4, this.transactionID);
        addOptional(aSN1EncodableVector, 5, this.senderNonce);
        addOptional(aSN1EncodableVector, 6, this.recipNonce);
        addOptional(aSN1EncodableVector, 7, this.freeText);
        addOptional(aSN1EncodableVector, 8, this.generalInfo);
        return new DERSequence(aSN1EncodableVector);
    }
}
