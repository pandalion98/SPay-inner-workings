/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.io.InputStream
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuffer
 */
package org.bouncycastle.jcajce.provider.asymmetric.x509;

import java.io.IOException;
import java.io.InputStream;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.util.encoders.Base64;

class PEMUtil {
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

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    ASN1Sequence readPEMObject(InputStream inputStream) {
        String string;
        StringBuffer stringBuffer = new StringBuffer();
        while ((string = this.readLine(inputStream)) != null && !string.startsWith(this._header1) && !string.startsWith(this._header2)) {
        }
        do {
            String string2;
            if ((string2 = this.readLine(inputStream)) == null || string2.startsWith(this._footer1) || string2.startsWith(this._footer2)) {
                if (stringBuffer.length() == 0) return null;
                return ASN1Sequence.getInstance(Base64.decode(stringBuffer.toString()));
            }
            stringBuffer.append(string2);
        } while (true);
        catch (Exception exception) {
            throw new IOException("malformed PEM data encountered");
        }
    }
}

