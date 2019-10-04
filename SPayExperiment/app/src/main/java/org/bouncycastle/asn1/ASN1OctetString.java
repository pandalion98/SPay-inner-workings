/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayInputStream
 *  java.io.IOException
 *  java.io.InputStream
 *  java.lang.Class
 *  java.lang.IllegalArgumentException
 *  java.lang.NullPointerException
 *  java.lang.Object
 *  java.lang.String
 *  org.bouncycastle.util.Arrays
 *  org.bouncycastle.util.encoders.Hex
 */
package org.bouncycastle.asn1;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1OctetStringParser;
import org.bouncycastle.asn1.ASN1OutputStream;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.BEROctetString;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.encoders.Hex;

public abstract class ASN1OctetString
extends ASN1Primitive
implements ASN1OctetStringParser {
    byte[] string;

    public ASN1OctetString(byte[] arrby) {
        if (arrby == null) {
            throw new NullPointerException("string cannot be null");
        }
        this.string = arrby;
    }

    public static ASN1OctetString getInstance(Object object) {
        ASN1Primitive aSN1Primitive;
        if (object == null || object instanceof ASN1OctetString) {
            return (ASN1OctetString)object;
        }
        if (object instanceof byte[]) {
            try {
                ASN1OctetString aSN1OctetString = ASN1OctetString.getInstance(ASN1Primitive.fromByteArray((byte[])object));
                return aSN1OctetString;
            }
            catch (IOException iOException) {
                throw new IllegalArgumentException("failed to construct OCTET STRING from byte[]: " + iOException.getMessage());
            }
        }
        if (object instanceof ASN1Encodable && (aSN1Primitive = ((ASN1Encodable)object).toASN1Primitive()) instanceof ASN1OctetString) {
            return (ASN1OctetString)aSN1Primitive;
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + object.getClass().getName());
    }

    public static ASN1OctetString getInstance(ASN1TaggedObject aSN1TaggedObject, boolean bl) {
        ASN1Primitive aSN1Primitive = aSN1TaggedObject.getObject();
        if (bl || aSN1Primitive instanceof ASN1OctetString) {
            return ASN1OctetString.getInstance(aSN1Primitive);
        }
        return BEROctetString.fromSequence(ASN1Sequence.getInstance(aSN1Primitive));
    }

    @Override
    boolean asn1Equals(ASN1Primitive aSN1Primitive) {
        if (!(aSN1Primitive instanceof ASN1OctetString)) {
            return false;
        }
        ASN1OctetString aSN1OctetString = (ASN1OctetString)aSN1Primitive;
        return Arrays.areEqual((byte[])this.string, (byte[])aSN1OctetString.string);
    }

    @Override
    abstract void encode(ASN1OutputStream var1);

    @Override
    public ASN1Primitive getLoadedObject() {
        return this.toASN1Primitive();
    }

    @Override
    public InputStream getOctetStream() {
        return new ByteArrayInputStream(this.string);
    }

    public byte[] getOctets() {
        return this.string;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode((byte[])this.getOctets());
    }

    public ASN1OctetStringParser parser() {
        return this;
    }

    @Override
    ASN1Primitive toDERObject() {
        return new DEROctetString(this.string);
    }

    @Override
    ASN1Primitive toDLObject() {
        return new DEROctetString(this.string);
    }

    public String toString() {
        return "#" + new String(Hex.encode((byte[])this.string));
    }
}

