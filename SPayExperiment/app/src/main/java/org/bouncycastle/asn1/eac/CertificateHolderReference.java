/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.UnsupportedEncodingException
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.asn1.eac;

import java.io.UnsupportedEncodingException;

public class CertificateHolderReference {
    private static final String ReferenceEncoding = "ISO-8859-1";
    private String countryCode;
    private String holderMnemonic;
    private String sequenceNumber;

    public CertificateHolderReference(String string, String string2, String string3) {
        this.countryCode = string;
        this.holderMnemonic = string2;
        this.sequenceNumber = string3;
    }

    CertificateHolderReference(byte[] arrby) {
        try {
            String string = new String(arrby, ReferenceEncoding);
            this.countryCode = string.substring(0, 2);
            this.holderMnemonic = string.substring(2, -5 + string.length());
            this.sequenceNumber = string.substring(-5 + string.length());
            return;
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            throw new IllegalStateException(unsupportedEncodingException.toString());
        }
    }

    public String getCountryCode() {
        return this.countryCode;
    }

    public byte[] getEncoded() {
        String string = this.countryCode + this.holderMnemonic + this.sequenceNumber;
        try {
            byte[] arrby = string.getBytes(ReferenceEncoding);
            return arrby;
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            throw new IllegalStateException(unsupportedEncodingException.toString());
        }
    }

    public String getHolderMnemonic() {
        return this.holderMnemonic;
    }

    public String getSequenceNumber() {
        return this.sequenceNumber;
    }
}

