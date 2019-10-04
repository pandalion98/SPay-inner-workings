/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.security.Principal
 *  java.security.PublicKey
 *  java.security.cert.Certificate
 *  java.security.cert.X509Certificate
 *  java.util.Arrays
 *  java.util.Collection
 *  java.util.Collections
 *  java.util.Iterator
 *  java.util.LinkedHashMap
 *  java.util.LinkedHashSet
 *  java.util.List
 *  java.util.Map
 *  java.util.Set
 *  javax.net.ssl.SSLPeerUnverifiedException
 */
package com.squareup.okhttp;

import com.squareup.okhttp.internal.Util;
import java.security.Principal;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.net.ssl.SSLPeerUnverifiedException;
import okio.ByteString;

public final class CertificatePinner {
    public static final CertificatePinner DEFAULT = new Builder().build();
    private final Map<String, Set<ByteString>> hostnameToPins;

    private CertificatePinner(Builder builder) {
        this.hostnameToPins = Util.immutableMap(builder.hostnameToPins);
    }

    public static String pin(Certificate certificate) {
        if (!(certificate instanceof X509Certificate)) {
            throw new IllegalArgumentException("Certificate pinning requires X509 certificates");
        }
        return "sha1/" + CertificatePinner.sha1((X509Certificate)certificate).base64();
    }

    private static ByteString sha1(X509Certificate x509Certificate) {
        return Util.sha1(ByteString.of(x509Certificate.getPublicKey().getEncoded()));
    }

    /*
     * Enabled aggressive block sorting
     */
    public void check(String string, List<Certificate> list) {
        int n2 = 0;
        Set<ByteString> set = this.findMatchingPins(string);
        if (set == null) return;
        {
            int n3 = list.size();
            for (int i2 = 0; i2 < n3; ++i2) {
                if (set.contains((Object)CertificatePinner.sha1((X509Certificate)list.get(i2)))) return;
                {
                    continue;
                }
            }
            StringBuilder stringBuilder = new StringBuilder().append("Certificate pinning failure!").append("\n  Peer certificate chain:");
            int n4 = list.size();
            while (n2 < n4) {
                X509Certificate x509Certificate = (X509Certificate)list.get(n2);
                stringBuilder.append("\n    ").append(CertificatePinner.pin((Certificate)x509Certificate)).append(": ").append(x509Certificate.getSubjectDN().getName());
                ++n2;
            }
            stringBuilder.append("\n  Pinned certificates for ").append(string).append(":");
            Iterator iterator = set.iterator();
            do {
                if (!iterator.hasNext()) {
                    throw new SSLPeerUnverifiedException(stringBuilder.toString());
                }
                ByteString byteString = (ByteString)iterator.next();
                stringBuilder.append("\n    sha1/").append(byteString.base64());
            } while (true);
        }
    }

    public /* varargs */ void check(String string, Certificate ... arrcertificate) {
        this.check(string, (List<Certificate>)Arrays.asList((Object[])arrcertificate));
    }

    /*
     * Enabled aggressive block sorting
     */
    Set<ByteString> findMatchingPins(String string) {
        Set set = (Set)this.hostnameToPins.get((Object)string);
        int n2 = string.indexOf(46);
        Set set2 = n2 != string.lastIndexOf(46) ? (Set)this.hostnameToPins.get((Object)("*." + string.substring(n2 + 1))) : null;
        if (set == null && set2 == null) {
            return null;
        }
        if (set != null && set2 != null) {
            LinkedHashSet linkedHashSet = new LinkedHashSet();
            linkedHashSet.addAll((Collection)set);
            linkedHashSet.addAll((Collection)set2);
            return linkedHashSet;
        }
        if (set != null) return set;
        return set2;
    }

    public static final class Builder {
        private final Map<String, Set<ByteString>> hostnameToPins = new LinkedHashMap();

        public /* varargs */ Builder add(String string, String ... arrstring) {
            if (string == null) {
                throw new IllegalArgumentException("hostname == null");
            }
            LinkedHashSet linkedHashSet = new LinkedHashSet();
            Set set = (Set)this.hostnameToPins.put((Object)string, (Object)Collections.unmodifiableSet((Set)linkedHashSet));
            if (set != null) {
                linkedHashSet.addAll((Collection)set);
            }
            for (String string2 : arrstring) {
                if (!string2.startsWith("sha1/")) {
                    throw new IllegalArgumentException("pins must start with 'sha1/': " + string2);
                }
                ByteString byteString = ByteString.decodeBase64(string2.substring("sha1/".length()));
                if (byteString == null) {
                    throw new IllegalArgumentException("pins must be base64: " + string2);
                }
                linkedHashSet.add((Object)byteString);
            }
            return this;
        }

        public CertificatePinner build() {
            return new CertificatePinner(this);
        }
    }

}

