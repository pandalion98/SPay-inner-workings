/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.NullPointerException
 *  java.lang.Object
 *  java.lang.String
 *  java.net.Proxy
 *  java.net.ProxySelector
 *  java.util.List
 *  javax.net.SocketFactory
 *  javax.net.ssl.HostnameVerifier
 *  javax.net.ssl.SSLSocketFactory
 */
package com.squareup.okhttp;

import com.squareup.okhttp.Authenticator;
import com.squareup.okhttp.CertificatePinner;
import com.squareup.okhttp.ConnectionSpec;
import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.internal.Util;
import java.net.Proxy;
import java.net.ProxySelector;
import java.util.List;
import javax.net.SocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;

public final class Address {
    final Authenticator authenticator;
    final CertificatePinner certificatePinner;
    final List<ConnectionSpec> connectionSpecs;
    final HostnameVerifier hostnameVerifier;
    final List<Protocol> protocols;
    final Proxy proxy;
    final ProxySelector proxySelector;
    final SocketFactory socketFactory;
    final SSLSocketFactory sslSocketFactory;
    final String uriHost;
    final int uriPort;

    public Address(String string, int n2, SocketFactory socketFactory, SSLSocketFactory sSLSocketFactory, HostnameVerifier hostnameVerifier, CertificatePinner certificatePinner, Authenticator authenticator, Proxy proxy, List<Protocol> list, List<ConnectionSpec> list2, ProxySelector proxySelector) {
        if (string == null) {
            throw new NullPointerException("uriHost == null");
        }
        if (n2 <= 0) {
            throw new IllegalArgumentException("uriPort <= 0: " + n2);
        }
        if (authenticator == null) {
            throw new IllegalArgumentException("authenticator == null");
        }
        if (list == null) {
            throw new IllegalArgumentException("protocols == null");
        }
        if (proxySelector == null) {
            throw new IllegalArgumentException("proxySelector == null");
        }
        this.proxy = proxy;
        this.uriHost = string;
        this.uriPort = n2;
        this.socketFactory = socketFactory;
        this.sslSocketFactory = sSLSocketFactory;
        this.hostnameVerifier = hostnameVerifier;
        this.certificatePinner = certificatePinner;
        this.authenticator = authenticator;
        this.protocols = Util.immutableList(list);
        this.connectionSpecs = Util.immutableList(list2);
        this.proxySelector = proxySelector;
    }

    public boolean equals(Object object) {
        boolean bl = object instanceof Address;
        boolean bl2 = false;
        if (bl) {
            Address address = (Address)object;
            boolean bl3 = Util.equal((Object)this.proxy, (Object)address.proxy);
            bl2 = false;
            if (bl3) {
                boolean bl4 = this.uriHost.equals((Object)address.uriHost);
                bl2 = false;
                if (bl4) {
                    int n2 = this.uriPort;
                    int n3 = address.uriPort;
                    bl2 = false;
                    if (n2 == n3) {
                        boolean bl5 = Util.equal((Object)this.sslSocketFactory, (Object)address.sslSocketFactory);
                        bl2 = false;
                        if (bl5) {
                            boolean bl6 = Util.equal((Object)this.hostnameVerifier, (Object)address.hostnameVerifier);
                            bl2 = false;
                            if (bl6) {
                                boolean bl7 = Util.equal(this.certificatePinner, address.certificatePinner);
                                bl2 = false;
                                if (bl7) {
                                    boolean bl8 = Util.equal(this.authenticator, address.authenticator);
                                    bl2 = false;
                                    if (bl8) {
                                        boolean bl9 = Util.equal(this.protocols, address.protocols);
                                        bl2 = false;
                                        if (bl9) {
                                            boolean bl10 = Util.equal(this.connectionSpecs, address.connectionSpecs);
                                            bl2 = false;
                                            if (bl10) {
                                                boolean bl11 = Util.equal((Object)this.proxySelector, (Object)address.proxySelector);
                                                bl2 = false;
                                                if (bl11) {
                                                    bl2 = true;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return bl2;
    }

    public Authenticator getAuthenticator() {
        return this.authenticator;
    }

    public CertificatePinner getCertificatePinner() {
        return this.certificatePinner;
    }

    public List<ConnectionSpec> getConnectionSpecs() {
        return this.connectionSpecs;
    }

    public HostnameVerifier getHostnameVerifier() {
        return this.hostnameVerifier;
    }

    public List<Protocol> getProtocols() {
        return this.protocols;
    }

    public Proxy getProxy() {
        return this.proxy;
    }

    public ProxySelector getProxySelector() {
        return this.proxySelector;
    }

    public SocketFactory getSocketFactory() {
        return this.socketFactory;
    }

    public SSLSocketFactory getSslSocketFactory() {
        return this.sslSocketFactory;
    }

    public String getUriHost() {
        return this.uriHost;
    }

    public int getUriPort() {
        return this.uriPort;
    }

    /*
     * Enabled aggressive block sorting
     */
    public int hashCode() {
        int n2 = this.proxy != null ? this.proxy.hashCode() : 0;
        int n3 = 31 * (31 * (31 * (n2 + 527) + this.uriHost.hashCode()) + this.uriPort);
        int n4 = this.sslSocketFactory != null ? this.sslSocketFactory.hashCode() : 0;
        int n5 = 31 * (n4 + n3);
        int n6 = this.hostnameVerifier != null ? this.hostnameVerifier.hashCode() : 0;
        int n7 = 31 * (n6 + n5);
        CertificatePinner certificatePinner = this.certificatePinner;
        int n8 = 0;
        if (certificatePinner != null) {
            n8 = this.certificatePinner.hashCode();
        }
        return 31 * (31 * (31 * (31 * (n7 + n8) + this.authenticator.hashCode()) + this.protocols.hashCode()) + this.connectionSpecs.hashCode()) + this.proxySelector.hashCode();
    }
}

