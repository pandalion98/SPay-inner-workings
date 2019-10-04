/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.System
 *  java.math.BigInteger
 */
package org.bouncycastle.asn1.eac;

import java.math.BigInteger;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERTaggedObject;

public class UnsignedInteger
extends ASN1Object {
    private int tagNo;
    private BigInteger value;

    public UnsignedInteger(int n2, BigInteger bigInteger) {
        this.tagNo = n2;
        this.value = bigInteger;
    }

    private UnsignedInteger(ASN1TaggedObject aSN1TaggedObject) {
        this.tagNo = aSN1TaggedObject.getTagNo();
        this.value = new BigInteger(1, ASN1OctetString.getInstance(aSN1TaggedObject, false).getOctets());
    }

    private byte[] convertValue() {
        byte[] arrby = this.value.toByteArray();
        if (arrby[0] == 0) {
            byte[] arrby2 = new byte[-1 + arrby.length];
            System.arraycopy((Object)arrby, (int)1, (Object)arrby2, (int)0, (int)arrby2.length);
            return arrby2;
        }
        return arrby;
    }

    public static UnsignedInteger getInstance(Object object) {
        if (object instanceof UnsignedInteger) {
            return (UnsignedInteger)object;
        }
        if (object != null) {
            return new UnsignedInteger(ASN1TaggedObject.getInstance(object));
        }
        return null;
    }

    public int getTagNo() {
        return this.tagNo;
    }

    public BigInteger getValue() {
        return this.value;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        return new DERTaggedObject(false, this.tagNo, new DEROctetString(this.convertValue()));
    }
}

