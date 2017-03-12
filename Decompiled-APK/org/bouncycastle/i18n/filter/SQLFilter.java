package org.bouncycastle.i18n.filter;

import com.google.android.gms.location.places.Place;
import org.bouncycastle.asn1.eac.EACTags;
import org.bouncycastle.crypto.tls.CipherSuite;
import org.bouncycastle.crypto.tls.NamedCurve;

public class SQLFilter implements Filter {
    public String doFilter(String str) {
        StringBuffer stringBuffer = new StringBuffer(str);
        int i = 0;
        while (i < stringBuffer.length()) {
            switch (stringBuffer.charAt(i)) {
                case NamedCurve.sect283r1 /*10*/:
                    stringBuffer.replace(i, i + 1, "\\n");
                    i++;
                    break;
                case NamedCurve.sect571k1 /*13*/:
                    stringBuffer.replace(i, i + 1, "\\r");
                    i++;
                    break;
                case Place.TYPE_ESTABLISHMENT /*34*/:
                    stringBuffer.replace(i, i + 1, "\\\"");
                    i++;
                    break;
                case Place.TYPE_FUNERAL_HOME /*39*/:
                    stringBuffer.replace(i, i + 1, "\\'");
                    i++;
                    break;
                case CipherSuite.TLS_DHE_PSK_WITH_NULL_SHA /*45*/:
                    stringBuffer.replace(i, i + 1, "\\-");
                    i++;
                    break;
                case CipherSuite.TLS_RSA_WITH_AES_128_CBC_SHA /*47*/:
                    stringBuffer.replace(i, i + 1, "\\/");
                    i++;
                    break;
                case CipherSuite.TLS_RSA_WITH_NULL_SHA256 /*59*/:
                    stringBuffer.replace(i, i + 1, "\\;");
                    i++;
                    break;
                case CipherSuite.TLS_RSA_WITH_AES_256_CBC_SHA256 /*61*/:
                    stringBuffer.replace(i, i + 1, "\\=");
                    i++;
                    break;
                case EACTags.TAG_LIST /*92*/:
                    stringBuffer.replace(i, i + 1, "\\\\");
                    i++;
                    break;
                default:
                    break;
            }
            i++;
        }
        return stringBuffer.toString();
    }

    public String doFilterUrl(String str) {
        return doFilter(str);
    }
}
