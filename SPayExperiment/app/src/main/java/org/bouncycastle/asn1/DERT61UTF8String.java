/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  org.bouncycastle.util.Arrays
 *  org.bouncycastle.util.Strings
 */
package org.bouncycastle.asn1;

import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1OutputStream;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1String;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERT61String;
import org.bouncycastle.asn1.StreamUtil;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Strings;

public class DERT61UTF8String
extends ASN1Primitive
implements ASN1String {
    private byte[] string;

    public DERT61UTF8String(String string) {
        this(Strings.toUTF8ByteArray((String)string));
    }

    public DERT61UTF8String(byte[] arrby) {
        this.string = arrby;
    }

    public static DERT61UTF8String getInstance(Object object) {
        if (object instanceof DERT61String) {
            return new DERT61UTF8String(((DERT61String)object).getOctets());
        }
        if (object == null || object instanceof DERT61UTF8String) {
            return (DERT61UTF8String)object;
        }
        if (object instanceof byte[]) {
            try {
                DERT61UTF8String dERT61UTF8String = new DERT61UTF8String(((DERT61String)DERT61UTF8String.fromByteArray((byte[])object)).getOctets());
                return dERT61UTF8String;
            }
            catch (Exception exception) {
                throw new IllegalArgumentException("encoding error in getInstance: " + exception.toString());
            }
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + object.getClass().getName());
    }

    public static DERT61UTF8String getInstance(ASN1TaggedObject aSN1TaggedObject, boolean bl) {
        ASN1Primitive aSN1Primitive = aSN1TaggedObject.getObject();
        if (bl || aSN1Primitive instanceof DERT61String || aSN1Primitive instanceof DERT61UTF8String) {
            return DERT61UTF8String.getInstance(aSN1Primitive);
        }
        return new DERT61UTF8String(ASN1OctetString.getInstance(aSN1Primitive).getOctets());
    }

    @Override
    boolean asn1Equals(ASN1Primitive aSN1Primitive) {
        if (!(aSN1Primitive instanceof DERT61UTF8String)) {
            return false;
        }
        return Arrays.areEqual((byte[])this.string, (byte[])((DERT61UTF8String)aSN1Primitive).string);
    }

    @Override
    void encode(ASN1OutputStream aSN1OutputStream) {
        aSN1OutputStream.writeEncoded(20, this.string);
    }

    @Override
    int encodedLength() {
        return 1 + StreamUtil.calculateBodyLength(this.string.length) + this.string.length;
    }

    public byte[] getOctets() {
        return Arrays.clone((byte[])this.string);
    }

    @Override
    public String getString() {
        return Strings.fromUTF8ByteArray((byte[])this.string);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode((byte[])this.string);
    }

    @Override
    boolean isConstructed() {
        return false;
    }

    public String toString() {
        return this.getString();
    }
}

