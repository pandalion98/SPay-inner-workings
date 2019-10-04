/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.ClassCastException
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.asn1;

import java.io.IOException;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1OutputStream;

public abstract class ASN1Primitive
extends ASN1Object {
    ASN1Primitive() {
    }

    public static ASN1Primitive fromByteArray(byte[] arrby) {
        ASN1InputStream aSN1InputStream = new ASN1InputStream(arrby);
        try {
            ASN1Primitive aSN1Primitive = aSN1InputStream.readObject();
            return aSN1Primitive;
        }
        catch (ClassCastException classCastException) {
            throw new IOException("cannot recognise object in stream");
        }
    }

    abstract boolean asn1Equals(ASN1Primitive var1);

    abstract void encode(ASN1OutputStream var1);

    abstract int encodedLength();

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public final boolean equals(Object object) {
        return this == object || object instanceof ASN1Encodable && this.asn1Equals(((ASN1Encodable)object).toASN1Primitive());
    }

    @Override
    public abstract int hashCode();

    abstract boolean isConstructed();

    @Override
    public ASN1Primitive toASN1Primitive() {
        return this;
    }

    ASN1Primitive toDERObject() {
        return this;
    }

    ASN1Primitive toDLObject() {
        return this;
    }
}

