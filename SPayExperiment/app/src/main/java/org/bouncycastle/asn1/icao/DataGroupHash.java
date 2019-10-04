/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.math.BigInteger
 *  java.util.Enumeration
 */
package org.bouncycastle.asn1.icao;

import java.math.BigInteger;
import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;

public class DataGroupHash
extends ASN1Object {
    ASN1OctetString dataGroupHashValue;
    ASN1Integer dataGroupNumber;

    public DataGroupHash(int n2, ASN1OctetString aSN1OctetString) {
        this.dataGroupNumber = new ASN1Integer(n2);
        this.dataGroupHashValue = aSN1OctetString;
    }

    private DataGroupHash(ASN1Sequence aSN1Sequence) {
        Enumeration enumeration = aSN1Sequence.getObjects();
        this.dataGroupNumber = ASN1Integer.getInstance(enumeration.nextElement());
        this.dataGroupHashValue = ASN1OctetString.getInstance(enumeration.nextElement());
    }

    public static DataGroupHash getInstance(Object object) {
        if (object instanceof DataGroupHash) {
            return (DataGroupHash)object;
        }
        if (object != null) {
            return new DataGroupHash(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public ASN1OctetString getDataGroupHashValue() {
        return this.dataGroupHashValue;
    }

    public int getDataGroupNumber() {
        return this.dataGroupNumber.getValue().intValue();
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.dataGroupNumber);
        aSN1EncodableVector.add(this.dataGroupHashValue);
        return new DERSequence(aSN1EncodableVector);
    }
}

