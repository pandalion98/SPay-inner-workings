/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  org.bouncycastle.util.Strings
 */
package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DERPrintableString;
import org.bouncycastle.util.Strings;

public abstract class X509NameEntryConverter {
    protected boolean canBePrintable(String string) {
        return DERPrintableString.isPrintableString(string);
    }

    /*
     * Enabled aggressive block sorting
     */
    protected ASN1Primitive convertHexEncoded(String string, int n2) {
        String string2 = Strings.toLowerCase((String)string);
        byte[] arrby = new byte[(string2.length() - n2) / 2];
        int n3 = 0;
        while (n3 != arrby.length) {
            char c2 = string2.charAt(n2 + n3 * 2);
            char c3 = string2.charAt(1 + (n2 + n3 * 2));
            arrby[n3] = c2 < 'a' ? (byte)(c2 - 48 << 4) : (byte)(10 + (c2 - 97) << 4);
            arrby[n3] = c3 < 'a' ? (byte)(arrby[n3] | (byte)(c3 - 48)) : (byte)(arrby[n3] | (byte)(10 + (c3 - 97)));
            ++n3;
        }
        return new ASN1InputStream(arrby).readObject();
    }

    public abstract ASN1Primitive getConvertedValue(ASN1ObjectIdentifier var1, String var2);
}

