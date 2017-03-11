package org.bouncycastle.asn1;

import java.io.IOException;

public abstract class ASN1Primitive extends ASN1Object {
    ASN1Primitive() {
    }

    public static ASN1Primitive fromByteArray(byte[] bArr) {
        try {
            return new ASN1InputStream(bArr).readObject();
        } catch (ClassCastException e) {
            throw new IOException("cannot recognise object in stream");
        }
    }

    abstract boolean asn1Equals(ASN1Primitive aSN1Primitive);

    abstract void encode(ASN1OutputStream aSN1OutputStream);

    abstract int encodedLength();

    public final boolean equals(Object obj) {
        return this == obj ? true : (obj instanceof ASN1Encodable) && asn1Equals(((ASN1Encodable) obj).toASN1Primitive());
    }

    public abstract int hashCode();

    abstract boolean isConstructed();

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
