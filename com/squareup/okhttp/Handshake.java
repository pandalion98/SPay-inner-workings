package com.squareup.okhttp;

import com.squareup.okhttp.internal.Util;
import java.security.Principal;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.List;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;

public final class Handshake {
    private final String cipherSuite;
    private final List<Certificate> localCertificates;
    private final List<Certificate> peerCertificates;

    private Handshake(String str, List<Certificate> list, List<Certificate> list2) {
        this.cipherSuite = str;
        this.peerCertificates = list;
        this.localCertificates = list2;
    }

    public static Handshake get(SSLSession sSLSession) {
        String cipherSuite = sSLSession.getCipherSuite();
        if (cipherSuite == null) {
            throw new IllegalStateException("cipherSuite == null");
        }
        Object[] peerCertificates;
        List immutableList;
        List immutableList2;
        try {
            peerCertificates = sSLSession.getPeerCertificates();
        } catch (SSLPeerUnverifiedException e) {
            peerCertificates = null;
        }
        if (peerCertificates != null) {
            immutableList = Util.immutableList(peerCertificates);
        } else {
            immutableList = Collections.emptyList();
        }
        Object[] localCertificates = sSLSession.getLocalCertificates();
        if (localCertificates != null) {
            immutableList2 = Util.immutableList(localCertificates);
        } else {
            immutableList2 = Collections.emptyList();
        }
        return new Handshake(cipherSuite, immutableList, immutableList2);
    }

    public static Handshake get(String str, List<Certificate> list, List<Certificate> list2) {
        if (str != null) {
            return new Handshake(str, Util.immutableList((List) list), Util.immutableList((List) list2));
        }
        throw new IllegalArgumentException("cipherSuite == null");
    }

    public String cipherSuite() {
        return this.cipherSuite;
    }

    public List<Certificate> peerCertificates() {
        return this.peerCertificates;
    }

    public Principal peerPrincipal() {
        return !this.peerCertificates.isEmpty() ? ((X509Certificate) this.peerCertificates.get(0)).getSubjectX500Principal() : null;
    }

    public List<Certificate> localCertificates() {
        return this.localCertificates;
    }

    public Principal localPrincipal() {
        return !this.localCertificates.isEmpty() ? ((X509Certificate) this.localCertificates.get(0)).getSubjectX500Principal() : null;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Handshake)) {
            return false;
        }
        Handshake handshake = (Handshake) obj;
        if (this.cipherSuite.equals(handshake.cipherSuite) && this.peerCertificates.equals(handshake.peerCertificates) && this.localCertificates.equals(handshake.localCertificates)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return ((((this.cipherSuite.hashCode() + 527) * 31) + this.peerCertificates.hashCode()) * 31) + this.localCertificates.hashCode();
    }
}
