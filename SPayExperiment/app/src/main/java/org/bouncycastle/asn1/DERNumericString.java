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

public class DERNumericString
extends ASN1Primitive
implements ASN1String {
    private byte[] string;

    public DERNumericString(String string) {
        this(string, false);
    }

    public DERNumericString(String string, boolean bl) {
        if (bl && !DERNumericString.isNumericString(string)) {
            throw new IllegalArgumentException("string contains illegal characters");
        }
        this.string = Strings.toByteArray((String)string);
    }

    DERNumericString(byte[] arrby) {
        this.string = arrby;
    }

    public static DERNumericString getInstance(Object object) {
        if (object == null || object instanceof DERNumericString) {
            return (DERNumericString)object;
        }
        if (object instanceof byte[]) {
            try {
                DERNumericString dERNumericString = (DERNumericString)DERNumericString.fromByteArray((byte[])object);
                return dERNumericString;
            }
            catch (Exception exception) {
                throw new IllegalArgumentException("encoding error in getInstance: " + exception.toString());
            }
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + object.getClass().getName());
    }

    public static DERNumericString getInstance(ASN1TaggedObject aSN1TaggedObject, boolean bl) {
        ASN1Primitive aSN1Primitive = aSN1TaggedObject.getObject();
        if (bl || aSN1Primitive instanceof DERNumericString) {
            return DERNumericString.getInstance(aSN1Primitive);
        }
        return new DERNumericString(ASN1OctetString.getInstance(aSN1Primitive).getOctets());
    }

    /*
     * Enabled aggressive block sorting
     */
    public static boolean isNumericString(String string) {
        int n2 = -1 + string.length();
        while (n2 >= 0) {
            char c2 = string.charAt(n2);
            if (c2 > '' || ('0' > c2 || c2 > '9') && c2 != ' ') {
                return false;
            }
            --n2;
        }
        return true;
    }

    @Override
    boolean asn1Equals(ASN1Primitive aSN1Primitive) {
        if (!(aSN1Primitive instanceof DERNumericString)) {
            return false;
        }
        DERNumericString dERNumericString = (DERNumericString)aSN1Primitive;
        return Arrays.areEqual((byte[])this.string, (byte[])dERNumericString.string);
    }

    @Override
    void encode(ASN1OutputStream aSN1OutputStream) {
        aSN1OutputStream.writeEncoded(18, this.string);
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

