/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 *  java.security.Principal
 *  java.security.cert.Certificate
 *  java.security.cert.X509Certificate
 *  java.util.Collections
 *  java.util.List
 *  javax.net.ssl.SSLPeerUnverifiedException
 *  javax.net.ssl.SSLSession
 *  javax.security.auth.x500.X500Principal
 */
package com.squareup.okhttp;

import com.squareup.okhttp.internal.Util;
import java.security.Principal;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.List;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.security.auth.x500.X500Principal;

public final class Handshake {
    private final String cipherSuite;
    private final List<Certificate> localCertificates;
    private final List<Certificate> peerCertificates;

    private Handshake(String string, List<Certificate> list, List<Certificate> list2) {
        this.cipherSuite = string;
        this.peerCertificates = list;
        this.localCertificates = list2;
    }

    public static Handshake get(String string, List<Certificate> list, List<Certificate> list2) {
        if (string == null) {
            throw new IllegalArgumentException("cipherSuite == null");
        }
        return new Handshake(string, Util.immutableList(list), Util.immutableList(list2));
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static Handshake get(SSLSession sSLSession) {
        Certificate[] arrcertificate;
        List<Certificate> list;
        String string = sSLSession.getCipherSuite();
        if (string == null) {
            throw new IllegalStateException("cipherSuite == null");
        }
        try {
            Certificate[] arrcertificate2;
            arrcertificate = arrcertificate2 = sSLSession.getPeerCertificates();
        }
        catch (SSLPeerUnverifiedException sSLPeerUnverifiedException) {
            arrcertificate = null;
        }
        List<Certificate> list2 = arrcertificate != null ? Util.immutableList(arrcertificate) : Collections.emptyList();
        Certificate[] arrcertificate3 = sSLSession.getLocalCertificates();
        if (arrcertificate3 != null) {
            list = Util.immutableList(arrcertificate3);
            return new Handshake(string, list2, list);
        }
        list = Collections.emptyList();
        return new Handshake(string, list2, list);
    }

    public String cipherSuite() {
        return this.cipherSuite;
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean equals(Object object) {
        block3 : {
            block2 : {
                if (!(object instanceof Handshake)) break block2;
                Handshake handshake = (Handshake)object;
                if (this.cipherSuite.equals((Object)handshake.cipherSuite) && this.peerCertificates.equals(handshake.peerCertificates) && this.localCertificates.equals(handshake.localCertificates)) break block3;
            }
            return false;
        }
        return true;
    }

    public int hashCode() {
        return 31 * (31 * (527 + this.cipherSuite.hashCode()) + this.peerCertificates.hashCode()) + this.localCertificates.hashCode();
    }

    public List<Certificate> localCertificates() {
        return this.localCertificates;
    }

    public Principal localPrincipal() {
        if (!this.localCertificates.isEmpty()) {
            return ((X509Certificate)this.localCertificates.get(0)).getSubjectX500Principal();
        }
        return null;
    }

    public List<Certificate> peerCertificates() {
        return this.peerCertificates;
    }

    public Principal peerPrincipal() {
        if (!this.peerCertificates.isEmpty()) {
            return ((X509Certificate)this.peerCertificates.get(0)).getSubjectX500Principal();
        }
        return null;
    }
}

