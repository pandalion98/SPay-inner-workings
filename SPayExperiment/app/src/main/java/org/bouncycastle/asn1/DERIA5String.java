/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.NullPointerException
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

public class DERIA5String
extends ASN1Primitive
implements ASN1String {
    private byte[] string;

    public DERIA5String(String string) {
        this(string, false);
    }

    public DERIA5String(String string, boolean bl) {
        if (string == null) {
            throw new NullPointerException("string cannot be null");
        }
        if (bl && !DERIA5String.isIA5String(string)) {
            throw new IllegalArgumentException("string contains illegal characters");
        }
        this.string = Strings.toByteArray((String)string);
    }

    DERIA5String(byte[] arrby) {
        this.string = arrby;
    }

    public static DERIA5String getInstance(Object object) {
        if (object == null || object instanceof DERIA5String) {
            return (DERIA5String)object;
        }
        if (object instanceof byte[]) {
            try {
                DERIA5String dERIA5String = (DERIA5String)DERIA5String.fromByteArray((byte[])object);
                return dERIA5String;
            }
            catch (Exception exception) {
                throw new IllegalArgumentException("encoding error in getInstance: " + exception.toString());
            }
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + object.getClass().getName());
    }

    public static DERIA5String getInstance(ASN1TaggedObject aSN1TaggedObject, boolean bl) {
        ASN1Primitive aSN1Primitive = aSN1TaggedObject.getObject();
        if (bl || aSN1Primitive instanceof DERIA5String) {
            return DERIA5String.getInstance(aSN1Primitive);
        }
        return new DERIA5String(((ASN1OctetString)aSN1Primitive).getOctets());
    }

    public static boolean isIA5String(String string) {
        for (int i2 = -1 + string.length(); i2 >= 0; --i2) {
            if (string.charAt(i2) <= '') continue;
            return false;
        }
        return true;
    }

    @Override
    boolean asn1Equals(ASN1Primitive aSN1Primitive) {
        if (!(aSN1Primitive instanceof DERIA5String)) {
            return false;
        }
        DERIA5String dERIA5String = (DERIA5String)aSN1Primitive;
        return Arrays.areEqual((byte[])this.string, (byte[])dERIA5String.string);
    }

    @Override
    void encode(ASN1OutputStream aSN1OutputStream) {
        aSN1OutputStream.writeEncoded(22, this.string);
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
        return Strings.fromByteArray((byte[])this.string);
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

