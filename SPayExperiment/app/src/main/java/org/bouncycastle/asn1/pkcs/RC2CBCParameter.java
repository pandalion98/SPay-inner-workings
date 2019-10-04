/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.math.BigInteger
 */
package org.bouncycastle.asn1.pkcs;

import java.math.BigInteger;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;

public class RC2CBCParameter
extends ASN1Object {
    ASN1OctetString iv;
    ASN1Integer version;

    public RC2CBCParameter(int n2, byte[] arrby) {
        this.version = new ASN1Integer(n2);
        this.iv = new DEROctetString(arrby);
    }

    private RC2CBCParameter(ASN1Sequence aSN1Sequence) {
        if (aSN1Sequence.size() == 1) {
            this.version = null;
            this.iv = (ASN1OctetString)aSN1Sequence.getObjectAt(0);
            return;
        }
        this.version = (ASN1Integer)aSN1Sequence.getObjectAt(0);
        this.iv = (ASN1OctetString)aSN1Sequence.getObjectAt(1);
    }

    public RC2CBCParameter(byte[] arrby) {
        this.version = null;
        this.iv = new DEROctetString(arrby);
    }

    public static RC2CBCParameter getInstance(Object object) {
        if (object instanceof RC2CBCParameter) {
            return (RC2CBCParameter)object;
        }
        if (object != null) {
            return new RC2CBCParameter(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public byte[] getIV() {
        return this.iv.getOctets();
    }

    public BigInteger getRC2ParameterVersion() {
        if (this.version == null) {
            return null;
        }
        return this.version.getValue();
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        if (this.version != null) {
            aSN1EncodableVector.add(this.version);
        }
        aSN1EncodableVector.add(this.iv);
        return new DERSequence(aSN1EncodableVector);
    }
}

