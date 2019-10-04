/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 */
package org.bouncycastle.asn1.icao;

import java.math.BigInteger;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERSet;
import org.bouncycastle.asn1.x509.Certificate;

public class CscaMasterList
extends ASN1Object {
    private Certificate[] certList;
    private ASN1Integer version = new ASN1Integer(0L);

    private CscaMasterList(ASN1Sequence aSN1Sequence) {
        if (aSN1Sequence == null || aSN1Sequence.size() == 0) {
            throw new IllegalArgumentException("null or empty sequence passed.");
        }
        if (aSN1Sequence.size() != 2) {
            throw new IllegalArgumentException("Incorrect sequence size: " + aSN1Sequence.size());
        }
        this.version = ASN1Integer.getInstance(aSN1Sequence.getObjectAt(0));
        ASN1Set aSN1Set = ASN1Set.getInstance(aSN1Sequence.getObjectAt(1));
        this.certList = new Certificate[aSN1Set.size()];
        for (int i2 = 0; i2 < this.certList.length; ++i2) {
            this.certList[i2] = Certificate.getInstance(aSN1Set.getObjectAt(i2));
        }
    }

    public CscaMasterList(Certificate[] arrcertificate) {
        this.certList = this.copyCertList(arrcertificate);
    }

    private Certificate[] copyCertList(Certificate[] arrcertificate) {
        Certificate[] arrcertificate2 = new Certificate[arrcertificate.length];
        for (int i2 = 0; i2 != arrcertificate2.length; ++i2) {
            arrcertificate2[i2] = arrcertificate[i2];
        }
        return arrcertificate2;
    }

    public static CscaMasterList getInstance(Object object) {
        if (object instanceof CscaMasterList) {
            return (CscaMasterList)object;
        }
        if (object != null) {
            return new CscaMasterList(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public Certificate[] getCertStructs() {
        return this.copyCertList(this.certList);
    }

    public int getVersion() {
        return this.version.getValue().intValue();
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.version);
        ASN1EncodableVector aSN1EncodableVector2 = new ASN1EncodableVector();
        for (int i2 = 0; i2 < this.certList.length; ++i2) {
            aSN1EncodableVector2.add(this.certList[i2]);
        }
        aSN1EncodableVector.add(new DERSet(aSN1EncodableVector2));
        return new DERSequence(aSN1EncodableVector);
    }
}

