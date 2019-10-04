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
import org.bouncycastle.asn1.StreamUtil;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Strings;

public class DERUTF8String
extends ASN1Primitive
implements ASN1String {
    private byte[] string;

    public DERUTF8String(String string) {
        this.string = Strings.toUTF8ByteArray((String)string);
    }

    DERUTF8String(byte[] arrby) {
        this.string = arrby;
    }

    public static DERUTF8String getInstance(Object object) {
        if (object == null || object instanceof DERUTF8String) {
            return (DERUTF8String)object;
        }
        if (object instanceof byte[]) {
            try {
                DERUTF8String dERUTF8String = (DERUTF8String)DERUTF8String.fromByteArray((byte[])object);
                return dERUTF8String;
            }
            catch (Exception exception) {
                throw new IllegalArgumentException("encoding error in getInstance: " + exception.toString());
            }
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + object.getClass().getName());
    }

    public static DERUTF8String getInstance(ASN1TaggedObject aSN1TaggedObject, boolean bl) {
        ASN1Primitive aSN1Primitive = aSN1TaggedObject.getObject();
        if (bl || aSN1Primitive instanceof DERUTF8String) {
            return DERUTF8String.getInstance(aSN1Primitive);
        }
        return new DERUTF8String(ASN1OctetString.getInstance(aSN1Primitive).getOctets());
    }

    @Override
    boolean asn1Equals(ASN1Primitive aSN1Primitive) {
        if (!(aSN1Primitive instanceof DERUTF8String)) {
            return false;
        }
        DERUTF8String dERUTF8String = (DERUTF8String)aSN1Primitive;
        return Arrays.areEqual((byte[])this.string, (byte[])dERUTF8String.string);
    }

    @Override
    void encode(ASN1OutputStream aSN1OutputStream) {
        aSN1OutputStream.writeEncoded(12, this.string);
    }

    @Override
    int encodedLength() {
        return 1 + StreamUtil.calculateBodyLength(this.string.length) + this.string.length;
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

