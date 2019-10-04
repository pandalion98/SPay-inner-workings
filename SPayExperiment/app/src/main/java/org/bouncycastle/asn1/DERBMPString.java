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
 */
package org.bouncycastle.asn1;

import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1OutputStream;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1String;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.StreamUtil;
import org.bouncycastle.util.Arrays;

public class DERBMPString
extends ASN1Primitive
implements ASN1String {
    private char[] string;

    public DERBMPString(String string) {
        this.string = string.toCharArray();
    }

    DERBMPString(byte[] arrby) {
        char[] arrc = new char[arrby.length / 2];
        for (int i2 = 0; i2 != arrc.length; ++i2) {
            arrc[i2] = (char)(arrby[i2 * 2] << 8 | 255 & arrby[1 + i2 * 2]);
        }
        this.string = arrc;
    }

    DERBMPString(char[] arrc) {
        this.string = arrc;
    }

    public static DERBMPString getInstance(Object object) {
        if (object == null || object instanceof DERBMPString) {
            return (DERBMPString)object;
        }
        if (object instanceof byte[]) {
            try {
                DERBMPString dERBMPString = (DERBMPString)DERBMPString.fromByteArray((byte[])object);
                return dERBMPString;
            }
            catch (Exception exception) {
                throw new IllegalArgumentException("encoding error in getInstance: " + exception.toString());
            }
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + object.getClass().getName());
    }

    public static DERBMPString getInstance(ASN1TaggedObject aSN1TaggedObject, boolean bl) {
        ASN1Primitive aSN1Primitive = aSN1TaggedObject.getObject();
        if (bl || aSN1Primitive instanceof DERBMPString) {
            return DERBMPString.getInstance(aSN1Primitive);
        }
        return new DERBMPString(ASN1OctetString.getInstance(aSN1Primitive).getOctets());
    }

    @Override
    protected boolean asn1Equals(ASN1Primitive aSN1Primitive) {
        if (!(aSN1Primitive instanceof DERBMPString)) {
            return false;
        }
        DERBMPString dERBMPString = (DERBMPString)aSN1Primitive;
        return Arrays.areEqual((char[])this.string, (char[])dERBMPString.string);
    }

    @Override
    void encode(ASN1OutputStream aSN1OutputStream) {
        aSN1OutputStream.write(30);
        aSN1OutputStream.writeLength(2 * this.string.length);
        for (int i2 = 0; i2 != this.string.length; ++i2) {
            char c2 = this.string[i2];
            aSN1OutputStream.write((byte)(c2 >> 8));
            aSN1OutputStream.write((byte)c2);
        }
    }

    @Override
    int encodedLength() {
        return 1 + StreamUtil.calculateBodyLength(2 * this.string.length) + 2 * this.string.length;
    }

    @Override
    public String getString() {
        return new String(this.string);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode((char[])this.string);
    }

    @Override
    boolean isConstructed() {
        return false;
    }

    public String toString() {
        return this.getString();
    }
}

