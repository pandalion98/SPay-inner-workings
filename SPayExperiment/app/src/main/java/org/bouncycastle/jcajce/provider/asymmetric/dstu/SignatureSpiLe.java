/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.Exception
 *  java.lang.String
 *  java.security.SignatureException
 */
package org.bouncycastle.jcajce.provider.asymmetric.dstu;

import java.io.IOException;
import java.security.SignatureException;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.jcajce.provider.asymmetric.dstu.SignatureSpi;

public class SignatureSpiLe
extends SignatureSpi {
    @Override
    protected byte[] engineSign() {
        byte[] arrby = ASN1OctetString.getInstance(super.engineSign()).getOctets();
        this.reverseBytes(arrby);
        try {
            byte[] arrby2 = new DEROctetString(arrby).getEncoded();
            return arrby2;
        }
        catch (Exception exception) {
            throw new SignatureException(exception.toString());
        }
    }

    @Override
    protected boolean engineVerify(byte[] arrby) {
        byte[] arrby2;
        try {
            arrby2 = ((ASN1OctetString)ASN1OctetString.fromByteArray(arrby)).getOctets();
        }
        catch (IOException iOException) {
            throw new SignatureException("error decoding signature bytes.");
        }
        this.reverseBytes(arrby2);
        try {
            boolean bl = super.engineVerify(new DEROctetString(arrby2).getEncoded());
            return bl;
        }
        catch (SignatureException signatureException) {
            throw signatureException;
        }
        catch (Exception exception) {
            throw new SignatureException(exception.toString());
        }
    }

    void reverseBytes(byte[] arrby) {
        for (int i2 = 0; i2 < arrby.length / 2; ++i2) {
            byte by = arrby[i2];
            arrby[i2] = arrby[-1 + arrby.length - i2];
            arrby[-1 + arrby.length - i2] = by;
        }
    }
}

