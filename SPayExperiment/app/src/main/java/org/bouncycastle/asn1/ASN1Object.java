/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayOutputStream
 *  java.io.OutputStream
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.asn1;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1OutputStream;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DEROutputStream;
import org.bouncycastle.asn1.DLOutputStream;
import org.bouncycastle.util.Encodable;

public abstract class ASN1Object
implements ASN1Encodable,
Encodable {
    protected static boolean hasEncodedTagValue(Object object, int n2) {
        boolean bl = object instanceof byte[];
        boolean bl2 = false;
        if (bl) {
            byte by = ((byte[])object)[0];
            bl2 = false;
            if (by == n2) {
                bl2 = true;
            }
        }
        return bl2;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof ASN1Encodable)) {
            return false;
        }
        ASN1Encodable aSN1Encodable = (ASN1Encodable)object;
        return this.toASN1Primitive().equals(aSN1Encodable.toASN1Primitive());
    }

    @Override
    public byte[] getEncoded() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        new ASN1OutputStream((OutputStream)byteArrayOutputStream).writeObject(this);
        return byteArrayOutputStream.toByteArray();
    }

    public byte[] getEncoded(String string) {
        if (string.equals((Object)"DER")) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            new DEROutputStream((OutputStream)byteArrayOutputStream).writeObject(this);
            return byteArrayOutputStream.toByteArray();
        }
        if (string.equals((Object)"DL")) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            new DLOutputStream((OutputStream)byteArrayOutputStream).writeObject(this);
            return byteArrayOutputStream.toByteArray();
        }
        return this.getEncoded();
    }

    public int hashCode() {
        return this.toASN1Primitive().hashCode();
    }

    public ASN1Primitive toASN1Object() {
        return this.toASN1Primitive();
    }

    @Override
    public abstract ASN1Primitive toASN1Primitive();
}

