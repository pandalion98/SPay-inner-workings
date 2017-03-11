package org.bouncycastle.asn1.esf;

import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.ocsp.BasicOCSPResponse;
import org.bouncycastle.asn1.x509.CertificateList;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class RevocationValues extends ASN1Object {
    private ASN1Sequence crlVals;
    private ASN1Sequence ocspVals;
    private OtherRevVals otherRevVals;

    private RevocationValues(ASN1Sequence aSN1Sequence) {
        if (aSN1Sequence.size() > 3) {
            throw new IllegalArgumentException("Bad sequence size: " + aSN1Sequence.size());
        }
        Enumeration objects = aSN1Sequence.getObjects();
        while (objects.hasMoreElements()) {
            DERTaggedObject dERTaggedObject = (DERTaggedObject) objects.nextElement();
            ASN1Sequence aSN1Sequence2;
            Enumeration objects2;
            switch (dERTaggedObject.getTagNo()) {
                case ECCurve.COORD_AFFINE /*0*/:
                    aSN1Sequence2 = (ASN1Sequence) dERTaggedObject.getObject();
                    objects2 = aSN1Sequence2.getObjects();
                    while (objects2.hasMoreElements()) {
                        CertificateList.getInstance(objects2.nextElement());
                    }
                    this.crlVals = aSN1Sequence2;
                    break;
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    aSN1Sequence2 = (ASN1Sequence) dERTaggedObject.getObject();
                    objects2 = aSN1Sequence2.getObjects();
                    while (objects2.hasMoreElements()) {
                        BasicOCSPResponse.getInstance(objects2.nextElement());
                    }
                    this.ocspVals = aSN1Sequence2;
                    break;
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                    this.otherRevVals = OtherRevVals.getInstance(dERTaggedObject.getObject());
                    break;
                default:
                    throw new IllegalArgumentException("invalid tag: " + dERTaggedObject.getTagNo());
            }
        }
    }

    public RevocationValues(CertificateList[] certificateListArr, BasicOCSPResponse[] basicOCSPResponseArr, OtherRevVals otherRevVals) {
        if (certificateListArr != null) {
            this.crlVals = new DERSequence((ASN1Encodable[]) certificateListArr);
        }
        if (basicOCSPResponseArr != null) {
            this.ocspVals = new DERSequence((ASN1Encodable[]) basicOCSPResponseArr);
        }
        this.otherRevVals = otherRevVals;
    }

    public static RevocationValues getInstance(Object obj) {
        return obj instanceof RevocationValues ? (RevocationValues) obj : obj != null ? new RevocationValues(ASN1Sequence.getInstance(obj)) : null;
    }

    public CertificateList[] getCrlVals() {
        int i = 0;
        if (this.crlVals == null) {
            return new CertificateList[0];
        }
        CertificateList[] certificateListArr = new CertificateList[this.crlVals.size()];
        while (i < certificateListArr.length) {
            certificateListArr[i] = CertificateList.getInstance(this.crlVals.getObjectAt(i));
            i++;
        }
        return certificateListArr;
    }

    public BasicOCSPResponse[] getOcspVals() {
        int i = 0;
        if (this.ocspVals == null) {
            return new BasicOCSPResponse[0];
        }
        BasicOCSPResponse[] basicOCSPResponseArr = new BasicOCSPResponse[this.ocspVals.size()];
        while (i < basicOCSPResponseArr.length) {
            basicOCSPResponseArr[i] = BasicOCSPResponse.getInstance(this.ocspVals.getObjectAt(i));
            i++;
        }
        return basicOCSPResponseArr;
    }

    public OtherRevVals getOtherRevVals() {
        return this.otherRevVals;
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        if (this.crlVals != null) {
            aSN1EncodableVector.add(new DERTaggedObject(true, 0, this.crlVals));
        }
        if (this.ocspVals != null) {
            aSN1EncodableVector.add(new DERTaggedObject(true, 1, this.ocspVals));
        }
        if (this.otherRevVals != null) {
            aSN1EncodableVector.add(new DERTaggedObject(true, 2, this.otherRevVals.toASN1Primitive()));
        }
        return new DERSequence(aSN1EncodableVector);
    }
}
