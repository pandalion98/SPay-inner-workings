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

public class DERPrintableString
extends ASN1Primitive
implements ASN1String {
    private byte[] string;

    public DERPrintableString(String string) {
        this(string, false);
    }

    public DERPrintableString(String string, boolean bl) {
        if (bl && !DERPrintableString.isPrintableString(string)) {
            throw new IllegalArgumentException("string contains illegal characters");
        }
        this.string = Strings.toByteArray((String)string);
    }

    DERPrintableString(byte[] arrby) {
        this.string = arrby;
    }

    public static DERPrintableString getInstance(Object object) {
        if (object == null || object instanceof DERPrintableString) {
            return (DERPrintableString)object;
        }
        if (object instanceof byte[]) {
            try {
                DERPrintableString dERPrintableString = (DERPrintableString)DERPrintableString.fromByteArray((byte[])object);
                return dERPrintableString;
            }
            catch (Exception exception) {
                throw new IllegalArgumentException("encoding error in getInstance: " + exception.toString());
            }
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + object.getClass().getName());
    }

    public static DERPrintableString getInstance(ASN1TaggedObject aSN1TaggedObject, boolean bl) {
        ASN1Primitive aSN1Primitive = aSN1TaggedObject.getObject();
        if (bl || aSN1Primitive instanceof DERPrintableString) {
            return DERPrintableString.getInstance(aSN1Primitive);
        }
        return new DERPrintableString(ASN1OctetString.getInstance(aSN1Primitive).getOctets());
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public static boolean isPrintableString(String var0) {
        var1_1 = -1 + var0.length();
        block3 : while (var1_1 >= 0) {
            var2_2 = var0.charAt(var1_1);
            if (var2_2 > '') {
                return false;
            }
            if ('a' <= var2_2 && var2_2 <= 'z' || 'A' <= var2_2 && var2_2 <= 'Z' || '0' <= var2_2 && var2_2 <= '9') ** GOTO lbl-1000
            switch (var2_2) {
                case ' ': 
                case '\'': 
                case '(': 
                case ')': 
                case '+': 
                case ',': 
                case '-': 
                case '.': 
                case '/': 
                case ':': 
                case '=': 
                case '?': lbl-1000: // 2 sources:
                {
                    --var1_1;
                    continue block3;
                }
            }
        }
        return true;
        return false;
    }

    @Override
    boolean asn1Equals(ASN1Primitive aSN1Primitive) {
        if (!(aSN1Primitive instanceof DERPrintableString)) {
            return false;
        }
        DERPrintableString dERPrintableString = (DERPrintableString)aSN1Primitive;
        return Arrays.areEqual((byte[])this.string, (byte[])dERPrintableString.string);
    }

    @Override
    void encode(ASN1OutputStream aSN1OutputStream) {
        aSN1OutputStream.writeEncoded(19, this.string);
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

