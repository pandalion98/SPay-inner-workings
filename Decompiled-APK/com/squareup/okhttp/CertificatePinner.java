package com.squareup.okhttp;

import com.squareup.okhttp.internal.Util;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.net.ssl.SSLPeerUnverifiedException;
import okio.ByteString;

public final class CertificatePinner {
    public static final CertificatePinner DEFAULT;
    private final Map<String, Set<ByteString>> hostnameToPins;

    public static final class Builder {
        private final Map<String, Set<ByteString>> hostnameToPins;

        public Builder() {
            this.hostnameToPins = new LinkedHashMap();
        }

        public Builder add(String str, String... strArr) {
            if (str == null) {
                throw new IllegalArgumentException("hostname == null");
            }
            Set linkedHashSet = new LinkedHashSet();
            Set set = (Set) this.hostnameToPins.put(str, Collections.unmodifiableSet(linkedHashSet));
            if (set != null) {
                linkedHashSet.addAll(set);
            }
            int length = strArr.length;
            int i = 0;
            while (i < length) {
                String str2 = strArr[i];
                if (str2.startsWith("sha1/")) {
                    ByteString decodeBase64 = ByteString.decodeBase64(str2.substring("sha1/".length()));
                    if (decodeBase64 == null) {
                        throw new IllegalArgumentException("pins must be base64: " + str2);
                    }
                    linkedHashSet.add(decodeBase64);
                    i++;
                } else {
                    throw new IllegalArgumentException("pins must start with 'sha1/': " + str2);
                }
            }
            return this;
        }

        public CertificatePinner build() {
            return new CertificatePinner();
        }
    }

    static {
        DEFAULT = new Builder().build();
    }

    private CertificatePinner(Builder builder) {
        this.hostnameToPins = Util.immutableMap(builder.hostnameToPins);
    }

    public void check(String str, List<Certificate> list) {
        int i = 0;
        Set<ByteString> findMatchingPins = findMatchingPins(str);
        if (findMatchingPins != null) {
            int size = list.size();
            int i2 = 0;
            while (i2 < size) {
                if (!findMatchingPins.contains(sha1((X509Certificate) list.get(i2)))) {
                    i2++;
                } else {
                    return;
                }
            }
            StringBuilder append = new StringBuilder().append("Certificate pinning failure!").append("\n  Peer certificate chain:");
            size = list.size();
            while (i < size) {
                X509Certificate x509Certificate = (X509Certificate) list.get(i);
                append.append("\n    ").append(pin(x509Certificate)).append(": ").append(x509Certificate.getSubjectDN().getName());
                i++;
            }
            append.append("\n  Pinned certificates for ").append(str).append(":");
            for (ByteString base64 : findMatchingPins) {
                append.append("\n    sha1/").append(base64.base64());
            }
            throw new SSLPeerUnverifiedException(append.toString());
        }
    }

    public void check(String str, Certificate... certificateArr) {
        check(str, Arrays.asList(certificateArr));
    }

    Set<ByteString> findMatchingPins(String str) {
        Collection collection;
        Set<ByteString> set = (Set) this.hostnameToPins.get(str);
        int indexOf = str.indexOf(46);
        if (indexOf != str.lastIndexOf(46)) {
            collection = (Set) this.hostnameToPins.get("*." + str.substring(indexOf + 1));
        } else {
            collection = null;
        }
        if (set == null && collection == null) {
            return null;
        }
        if (set != null && collection != null) {
            Set<ByteString> linkedHashSet = new LinkedHashSet();
            linkedHashSet.addAll(set);
            linkedHashSet.addAll(collection);
            return linkedHashSet;
        } else if (set == null) {
            return collection;
        } else {
            return set;
        }
    }

    public static String pin(Certificate certificate) {
        if (certificate instanceof X509Certificate) {
            return "sha1/" + sha1((X509Certificate) certificate).base64();
        }
        throw new IllegalArgumentException("Certificate pinning requires X509 certificates");
    }

    private static ByteString sha1(X509Certificate x509Certificate) {
        return Util.sha1(ByteString.of(x509Certificate.getPublicKey().getEncoded()));
    }
}
