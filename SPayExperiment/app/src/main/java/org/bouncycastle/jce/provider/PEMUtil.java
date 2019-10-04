/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.io.InputStream
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuffer
 */
package org.bouncycastle.jce.provider;

import java.io.IOException;
import java.io.InputStream;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.util.encoders.Base64;

public class PEMUtil {
    private final String _footer1;
    private final String _footer2;
    private final String _header1;
    private final String _header2;

    PEMUtil(String string) {
        this._header1 = "-----BEGIN " + string + "-----";
        this._header2 = "-----BEGIN X509 " + string + "-----";
        this._footer1 = "-----END " + string + "-----";
        this._footer2 = "-----END X509 " + string + "-----";
    }

    private String readLine(InputStream inputStream) {
        int n;
        StringBuffer stringBuffer = new StringBuffer();
        do {
            if ((n = inputStream.read()) != 13 && n != 10 && n >= 0) {
                if (n == 13) continue;
                stringBuffer.append((char)n);
                continue;
            }
            if (n < 0 || stringBuffer.length() != 0) break;
        } while (true);
        if (n < 0) {
            return null;
        }
        return stringBuffer.toString();
    }

    ASN1Sequence readPEMObject(InputStream inputStream) {
        block4 : {
            String string;
            ASN1Primitive aSN1Primitive;
            StringBuffer stringBuffer = new StringBuffer();
            while ((string = this.readLine(inputStream)) != null && !string.startsWith(this._header1) && !string.startsWith(this._header2)) {
            }
            do {
                String string2;
                if ((string2 = this.readLine(inputStream)) == null || string2.startsWith(this._footer1) || string2.startsWith(this._footer2)) {
                    if (stringBuffer.length() != 0) {
                        aSN1Primitive = new ASN1InputStream(Base64.decode(stringBuffer.toString())).readObject();
                        if (aSN1Primitive instanceof ASN1Sequence) break;
                        throw new IOException("malformed PEM data encountered");
                    }
                    break block4;
                }
                stringBuffer.append(string2);
            } while (true);
            return (ASN1Sequence)aSN1Primitive;
        }
        return null;
    }
}

