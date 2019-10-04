/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayOutputStream
 *  java.io.IOException
 *  java.io.OutputStream
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.StringBuffer
 *  org.bouncycastle.util.Arrays
 */
package org.bouncycastle.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1OutputStream;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1String;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.StreamUtil;
import org.bouncycastle.util.Arrays;

public class DERUniversalString
extends ASN1Primitive
implements ASN1String {
    private static final char[] table = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private byte[] string;

    public DERUniversalString(byte[] arrby) {
        this.string = arrby;
    }

    public static DERUniversalString getInstance(Object object) {
        if (object == null || object instanceof DERUniversalString) {
            return (DERUniversalString)object;
        }
        if (object instanceof byte[]) {
            try {
                DERUniversalString dERUniversalString = (DERUniversalString)DERUniversalString.fromByteArray((byte[])object);
                return dERUniversalString;
            }
            catch (Exception exception) {
                throw new IllegalArgumentException("encoding error getInstance: " + exception.toString());
            }
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + object.getClass().getName());
    }

    public static DERUniversalString getInstance(ASN1TaggedObject aSN1TaggedObject, boolean bl) {
        ASN1Primitive aSN1Primitive = aSN1TaggedObject.getObject();
        if (bl || aSN1Primitive instanceof DERUniversalString) {
            return DERUniversalString.getInstance(aSN1Primitive);
        }
        return new DERUniversalString(((ASN1OctetString)aSN1Primitive).getOctets());
    }

    @Override
    boolean asn1Equals(ASN1Primitive aSN1Primitive) {
        if (!(aSN1Primitive instanceof DERUniversalString)) {
            return false;
        }
        return Arrays.areEqual((byte[])this.string, (byte[])((DERUniversalString)aSN1Primitive).string);
    }

    @Override
    void encode(ASN1OutputStream aSN1OutputStream) {
        aSN1OutputStream.writeEncoded(28, this.getOctets());
    }

    @Override
    int encodedLength() {
        return 1 + StreamUtil.calculateBodyLength(this.string.length) + this.string.length;
    }

    public byte[] getOctets() {
        return this.string;
    }

    @Override
    public String getString() {
        StringBuffer stringBuffer;
        stringBuffer = new StringBuffer("#");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ASN1OutputStream aSN1OutputStream = new ASN1OutputStream((OutputStream)byteArrayOutputStream);
        try {
            aSN1OutputStream.writeObject(this);
        }
        catch (IOException iOException) {
            throw new RuntimeException("internal error encoding BitString");
        }
        byte[] arrby = byteArrayOutputStream.toByteArray();
        for (int i2 = 0; i2 != arrby.length; ++i2) {
            stringBuffer.append(table[15 & arrby[i2] >>> 4]);
            stringBuffer.append(table[15 & arrby[i2]]);
        }
        return stringBuffer.toString();
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

