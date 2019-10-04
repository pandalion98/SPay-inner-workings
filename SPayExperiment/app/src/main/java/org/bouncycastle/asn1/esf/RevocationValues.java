/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Enumeration
 */
package org.bouncycastle.asn1.esf;

import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.esf.OtherRevVals;
import org.bouncycastle.asn1.ocsp.BasicOCSPResponse;
import org.bouncycastle.asn1.x509.CertificateList;

public class RevocationValues
extends ASN1Object {
    private ASN1Sequence crlVals;
    private ASN1Sequence ocspVals;
    private OtherRevVals otherRevVals;

    private RevocationValues(ASN1Sequence aSN1Sequence) {
        if (aSN1Sequence.size() > 3) {
            throw new IllegalArgumentException("Bad sequence size: " + aSN1Sequence.size());
        }
        Enumeration enumeration = aSN1Sequence.getObjects();
        block5 : while (enumeration.hasMoreElements()) {
            DERTaggedObject dERTaggedObject = (DERTaggedObject)enumeration.nextElement();
            switch (dERTaggedObject.getTagNo()) {
                default: {
                    throw new IllegalArgumentException("invalid tag: " + dERTaggedObject.getTagNo());
                }
                case 0: {
                    ASN1Sequence aSN1Sequence2 = (ASN1Sequence)dERTaggedObject.getObject();
                    Enumeration enumeration2 = aSN1Sequence2.getObjects();
                    while (enumeration2.hasMoreElements()) {
                        CertificateList.getInstance(enumeration2.nextElement());
                    }
                    this.crlVals = aSN1Sequence2;
                    continue block5;
                }
                case 1: {
                    ASN1Sequence aSN1Sequence3 = (ASN1Sequence)dERTaggedObject.getObject();
                    Enumeration enumeration3 = aSN1Sequence3.getObjects();
                    while (enumeration3.hasMoreElements()) {
                        BasicOCSPResponse.getInstance(enumeration3.nextElement());
                    }
                    this.ocspVals = aSN1Sequence3;
                    continue block5;
                }
                case 2: 
            }
            this.otherRevVals = OtherRevVals.getInstance(dERTaggedObject.getObject());
        }
    }

    public RevocationValues(CertificateList[] arrcertificateList, BasicOCSPResponse[] arrbasicOCSPResponse, OtherRevVals otherRevVals) {
        if (arrcertificateList != null) {
            this.crlVals = new DERSequence(arrcertificateList);
        }
        if (arrbasicOCSPResponse != null) {
            this.ocspVals = new DERSequence(arrbasicOCSPResponse);
        }
        this.otherRevVals = otherRevVals;
    }

    public static RevocationValues getInstance(Object object) {
        if (object instanceof RevocationValues) {
            return (RevocationValues)object;
        }
        if (object != null) {
            return new RevocationValues(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public CertificateList[] getCrlVals() {
        if (this.crlVals == null) {
            return new CertificateList[0];
        }
        CertificateList[] arrcertificateList = new CertificateList[this.crlVals.size()];
        for (int i2 = 0; i2 < arrcertificateList.length; ++i2) {
            arrcertificateList[i2] = CertificateList.getInstance(this.crlVals.getObjectAt(i2));
        }
        return arrcertificateList;
    }

    public BasicOCSPResponse[] getOcspVals() {
        if (this.ocspVals == null) {
            return new BasicOCSPResponse[0];
        }
        BasicOCSPResponse[] arrbasicOCSPResponse = new BasicOCSPResponse[this.ocspVals.size()];
        for (int i2 = 0; i2 < arrbasicOCSPResponse.length; ++i2) {
            arrbasicOCSPResponse[i2] = BasicOCSPResponse.getInstance(this.ocspVals.getObjectAt(i2));
        }
        return arrbasicOCSPResponse;
    }

    public OtherRevVals getOtherRevVals() {
        return this.otherRevVals;
    }

    @Override
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

