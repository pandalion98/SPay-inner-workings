/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 */
package org.bouncycastle.asn1.x509;

import java.io.IOException;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DERGeneralizedTime;
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.DERPrintableString;
import org.bouncycastle.asn1.DERUTF8String;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.asn1.x509.X509NameEntryConverter;

public class X509DefaultEntryConverter
extends X509NameEntryConverter {
    @Override
    public ASN1Primitive getConvertedValue(ASN1ObjectIdentifier aSN1ObjectIdentifier, String string) {
        if (string.length() != 0 && string.charAt(0) == '#') {
            try {
                ASN1Primitive aSN1Primitive = this.convertHexEncoded(string, 1);
                return aSN1Primitive;
            }
            catch (IOException iOException) {
                throw new RuntimeException("can't recode value for oid " + aSN1ObjectIdentifier.getId());
            }
        }
        if (string.length() != 0 && string.charAt(0) == '\\') {
            string = string.substring(1);
        }
        if (aSN1ObjectIdentifier.equals(X509Name.EmailAddress) || aSN1ObjectIdentifier.equals(X509Name.DC)) {
            return new DERIA5String(string);
        }
        if (aSN1ObjectIdentifier.equals(X509Name.DATE_OF_BIRTH)) {
            return new DERGeneralizedTime(string);
        }
        if (aSN1ObjectIdentifier.equals(X509Name.C) || aSN1ObjectIdentifier.equals(X509Name.SN) || aSN1ObjectIdentifier.equals(X509Name.DN_QUALIFIER) || aSN1ObjectIdentifier.equals(X509Name.TELEPHONE_NUMBER)) {
            return new DERPrintableString(string);
        }
        return new DERUTF8String(string);
    }
}

