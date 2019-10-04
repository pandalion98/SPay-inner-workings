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

public class DERVisibleString
extends ASN1Primitive
implements ASN1String {
    private byte[] string;

    public DERVisibleString(String string) {
        this.string = Strings.toByteArray((String)string);
    }

    DERVisibleString(byte[] arrby) {
        this.string = arrby;
    }

    public static DERVisibleString getInstance(Object object) {
        if (object == null || object instanceof DERVisibleString) {
            return (DERVisibleString)object;
        }
        if (object instanceof byte[]) {
            try {
                DERVisibleString dERVisibleString = (DERVisibleString)DERVisibleString.fromByteArray((byte[])object);
                return dERVisibleString;
            }
            catch (Exception exception) {
                throw new IllegalArgumentException("encoding error in getInstance: " + exception.toString());
            }
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + object.getClass().getName());
    }

    public static DERVisibleString getInstance(ASN1TaggedObject aSN1TaggedObject, boolean bl) {
        ASN1Primitive aSN1Primitive = aSN1TaggedObject.getObject();
        if (bl || aSN1Primitive instanceof DERVisibleString) {
            return DERVisibleString.getInstance(aSN1Primitive);
        }
        return new DERVisibleString(ASN1OctetString.getInstance(aSN1Primitive).getOctets());
    }

    @Override
    boolean asn1Equals(ASN1Primitive aSN1Primitive) {
        if (!(aSN1Primitive instanceof DERVisibleString)) {
            return false;
        }
        return Arrays.areEqual((byte[])this.string, (byte[])((DERVisibleString)aSN1Primitive).string);
    }

    @Override
    void encode(ASN1OutputStream aSN1OutputStream) {
        aSN1OutputStream.writeEncoded(26, this.string);
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

