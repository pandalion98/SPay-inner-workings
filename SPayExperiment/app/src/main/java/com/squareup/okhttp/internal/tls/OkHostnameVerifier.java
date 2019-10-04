/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.CharSequence
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.security.cert.Certificate
 *  java.security.cert.CertificateParsingException
 *  java.security.cert.X509Certificate
 *  java.util.ArrayList
 *  java.util.Collection
 *  java.util.Collections
 *  java.util.List
 *  java.util.Locale
 *  java.util.regex.Matcher
 *  java.util.regex.Pattern
 *  javax.net.ssl.HostnameVerifier
 *  javax.net.ssl.SSLException
 *  javax.net.ssl.SSLSession
 *  javax.security.auth.x500.X500Principal
 */
package com.squareup.okhttp.internal.tls;

import com.squareup.okhttp.internal.tls.DistinguishedNameParser;
import java.security.cert.Certificate;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.security.auth.x500.X500Principal;

public final class OkHostnameVerifier
implements HostnameVerifier {
    private static final int ALT_DNS_NAME = 2;
    private static final int ALT_IPA_NAME = 7;
    public static final OkHostnameVerifier INSTANCE = new OkHostnameVerifier();
    private static final Pattern VERIFY_AS_IP_ADDRESS = Pattern.compile((String)"([0-9a-fA-F]*:[0-9a-fA-F:.]*)|([\\d.]+)");

    private OkHostnameVerifier() {
    }

    public static List<String> allSubjectAltNames(X509Certificate x509Certificate) {
        List<String> list = OkHostnameVerifier.getSubjectAltNames(x509Certificate, 7);
        List<String> list2 = OkHostnameVerifier.getSubjectAltNames(x509Certificate, 2);
        ArrayList arrayList = new ArrayList(list.size() + list2.size());
        arrayList.addAll(list);
        arrayList.addAll(list2);
        return arrayList;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static List<String> getSubjectAltNames(X509Certificate x509Certificate, int n2) {
        ArrayList arrayList = new ArrayList();
        try {
            Collection collection = x509Certificate.getSubjectAlternativeNames();
            if (collection == null) {
                return Collections.emptyList();
            }
            for (List list : collection) {
                Integer n3;
                String string;
                if (list == null || list.size() < 2 || (n3 = (Integer)list.get(0)) == null || n3 != n2 || (string = (String)list.get(1)) == null) continue;
                arrayList.add((Object)string);
            }
            return arrayList;
        }
        catch (CertificateParsingException certificateParsingException) {
            return Collections.emptyList();
        }
    }

    static boolean verifyAsIpAddress(String string) {
        return VERIFY_AS_IP_ADDRESS.matcher((CharSequence)string).matches();
    }

    /*
     * Enabled aggressive block sorting
     */
    private boolean verifyHostName(String string, String string2) {
        block7 : {
            block6 : {
                String string3;
                int n2;
                String string4;
                if (string == null || string.length() == 0 || string.startsWith(".") || string.endsWith("..") || string2 == null || string2.length() == 0 || string2.startsWith(".") || string2.endsWith("..")) break block6;
                if (!string.endsWith(".")) {
                    string = string + '.';
                }
                if (!string2.endsWith(".")) {
                    string2 = string2 + '.';
                }
                if (!(string3 = string2.toLowerCase(Locale.US)).contains((CharSequence)"*")) {
                    return string.equals((Object)string3);
                }
                if (string3.startsWith("*.") && string3.indexOf(42, 1) == -1 && string.length() >= string3.length() && !"*.".equals((Object)string3) && string.endsWith(string4 = string3.substring(1)) && ((n2 = string.length() - string4.length()) <= 0 || string.lastIndexOf(46, n2 - 1) == -1)) break block7;
            }
            return false;
        }
        return true;
    }

    private boolean verifyHostName(String string, X509Certificate x509Certificate) {
        String string2;
        String string3 = string.toLowerCase(Locale.US);
        List<String> list = OkHostnameVerifier.getSubjectAltNames(x509Certificate, 2);
        int n2 = list.size();
        boolean bl = false;
        for (int i2 = 0; i2 < n2; ++i2) {
            if (this.verifyHostName(string3, (String)list.get(i2))) {
                return true;
            }
            bl = true;
        }
        if (!bl && (string2 = new DistinguishedNameParser(x509Certificate.getSubjectX500Principal()).findMostSpecific("cn")) != null) {
            return this.verifyHostName(string3, string2);
        }
        return false;
    }

    private boolean verifyIpAddress(String string, X509Certificate x509Certificate) {
        List<String> list = OkHostnameVerifier.getSubjectAltNames(x509Certificate, 7);
        int n2 = list.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            if (!string.equalsIgnoreCase((String)list.get(i2))) continue;
            return true;
        }
        return false;
    }

    public boolean verify(String string, X509Certificate x509Certificate) {
        if (OkHostnameVerifier.verifyAsIpAddress(string)) {
            return this.verifyIpAddress(string, x509Certificate);
        }
        return this.verifyHostName(string, x509Certificate);
    }

    public boolean verify(String string, SSLSession sSLSession) {
        try {
            boolean bl = this.verify(string, (X509Certificate)sSLSession.getPeerCertificates()[0]);
            return bl;
        }
        catch (SSLException sSLException) {
            return false;
        }
    }
}

