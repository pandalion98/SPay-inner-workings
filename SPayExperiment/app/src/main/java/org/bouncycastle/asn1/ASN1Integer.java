/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 *  org.bouncycastle.util.Arrays
 */
package org.bouncycastle.asn1;

import java.math.BigInteger;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1OutputStream;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.StreamUtil;
import org.bouncycastle.util.Arrays;

public class ASN1Integer
extends ASN1Primitive {
    byte[] bytes;

    public ASN1Integer(long l2) {
        this.bytes = BigInteger.valueOf((long)l2).toByteArray();
    }

    public ASN1Integer(BigInteger bigInteger) {
        this.bytes = bigInteger.toByteArray();
    }

    public ASN1Integer(byte[] arrby) {
        this(arrby, true);
    }

    ASN1Integer(byte[] arrby, boolean bl) {
        if (bl) {
            arrby = Arrays.clone((byte[])arrby);
        }
        this.bytes = arrby;
    }

    public static ASN1Integer getInstance(Object object) {
        if (object == null || object instanceof ASN1Integer) {
            return (ASN1Integer)object;
        }
        if (object instanceof byte[]) {
            try {
                ASN1Integer aSN1Integer = (ASN1Integer)ASN1Integer.fromByteArray((byte[])object);
                return aSN1Integer;
            }
            catch (Exception exception) {
                throw new IllegalArgumentException("encoding error in getInstance: " + exception.toString());
            }
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + object.getClass().getName());
    }

    public static ASN1Integer getInstance(ASN1TaggedObject aSN1TaggedObject, boolean bl) {
        ASN1Primitive aSN1Primitive = aSN1TaggedObject.getObject();
        if (bl || aSN1Primitive instanceof ASN1Integer) {
            return ASN1Integer.getInstance(aSN1Primitive);
        }
        return new ASN1Integer(ASN1OctetString.getInstance(aSN1TaggedObject.getObject()).getOctets());
    }

    @Override
    boolean asn1Equals(ASN1Primitive aSN1Primitive) {
        if (!(aSN1Primitive instanceof ASN1Integer)) {
            return false;
        }
        ASN1Integer aSN1Integer = (ASN1Integer)aSN1Primitive;
        return Arrays.areEqual((byte[])this.bytes, (byte[])aSN1Integer.bytes);
    }

    @Override
    void encode(ASN1OutputStream aSN1OutputStream) {
        aSN1OutputStream.writeEncoded(2, this.bytes);
    }

    @Override
    int encodedLength() {
        return 1 + StreamUtil.calculateBodyLength(this.bytes.length) + this.bytes.length;
    }

    public BigInteger getPositiveValue() {
        return new BigInteger(1, this.bytes);
    }

    public BigInteger getValue() {
        return new BigInteger(this.bytes);
    }

    @Override
    public int hashCode() {
        int n2 = 0;
        for (int i2 = 0; i2 != this.bytes.length; ++i2) {
            n2 ^= (255 & this.bytes[i2]) << i2 % 4;
        }
        return n2;
    }

    @Override
    boolean isConstructed() {
        return false;
    }

    public String toString() {
        return this.getValue().toString();
    }
}

