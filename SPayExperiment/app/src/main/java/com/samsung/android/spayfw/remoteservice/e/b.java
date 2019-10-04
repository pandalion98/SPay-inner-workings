/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 *  java.net.InetAddress
 *  java.net.Socket
 *  java.util.ArrayList
 *  java.util.Arrays
 *  java.util.Collection
 *  javax.net.ssl.SSLSocket
 *  javax.net.ssl.SSLSocketFactory
 */
package com.samsung.android.spayfw.remoteservice.e;

import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class b
extends SSLSocketFactory {
    private static b Ba;
    private static ArrayList<String> SECURE_CIPHER_SUITE_LIST;
    private static ArrayList<String> SECURE_PROTOCOL_LIST;
    private final SSLSocketFactory AZ;

    static {
        SECURE_CIPHER_SUITE_LIST = new ArrayList();
        SECURE_PROTOCOL_LIST = new ArrayList();
        SECURE_CIPHER_SUITE_LIST.add((Object)"TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256");
        SECURE_PROTOCOL_LIST.add((Object)"TLSv1.2");
    }

    private b(SSLSocketFactory sSLSocketFactory) {
        this.AZ = sSLSocketFactory;
    }

    public static b a(SSLSocketFactory sSLSocketFactory) {
        Class<b> class_ = b.class;
        synchronized (b.class) {
            if (Ba == null) {
                Ba = new b(sSLSocketFactory);
            }
            b b2 = Ba;
            // ** MonitorExit[var3_1] (shouldn't be in output)
            return b2;
        }
    }

    private String[] e(String[] arrstring) {
        if (arrstring == null || arrstring.length == 0) {
            return null;
        }
        ArrayList arrayList = new ArrayList((Collection)Arrays.asList((Object[])arrstring));
        arrayList.retainAll(SECURE_CIPHER_SUITE_LIST);
        return (String[])arrayList.toArray((Object[])new String[arrayList.size()]);
    }

    private String[] f(String[] arrstring) {
        if (arrstring == null || arrstring.length == 0) {
            return null;
        }
        ArrayList arrayList = new ArrayList((Collection)Arrays.asList((Object[])arrstring));
        arrayList.retainAll(SECURE_PROTOCOL_LIST);
        return (String[])arrayList.toArray((Object[])new String[arrayList.size()]);
    }

    public Socket createSocket(String string, int n2) {
        SSLSocket sSLSocket = (SSLSocket)this.AZ.createSocket(string, n2);
        sSLSocket.setEnabledCipherSuites(this.e(sSLSocket.getEnabledCipherSuites()));
        sSLSocket.setEnabledProtocols(this.f(sSLSocket.getEnabledProtocols()));
        return sSLSocket;
    }

    public Socket createSocket(String string, int n2, InetAddress inetAddress, int n3) {
        SSLSocket sSLSocket = (SSLSocket)this.AZ.createSocket(string, n2, inetAddress, n3);
        sSLSocket.setEnabledCipherSuites(this.e(sSLSocket.getEnabledCipherSuites()));
        sSLSocket.setEnabledProtocols(this.f(sSLSocket.getEnabledProtocols()));
        return sSLSocket;
    }

    public Socket createSocket(InetAddress inetAddress, int n2) {
        SSLSocket sSLSocket = (SSLSocket)this.AZ.createSocket(inetAddress, n2);
        sSLSocket.setEnabledCipherSuites(this.e(sSLSocket.getEnabledCipherSuites()));
        sSLSocket.setEnabledProtocols(this.f(sSLSocket.getEnabledProtocols()));
        return sSLSocket;
    }

    public Socket createSocket(InetAddress inetAddress, int n2, InetAddress inetAddress2, int n3) {
        SSLSocket sSLSocket = (SSLSocket)this.AZ.createSocket(inetAddress, n2, inetAddress2, n3);
        sSLSocket.setEnabledCipherSuites(this.e(sSLSocket.getEnabledCipherSuites()));
        sSLSocket.setEnabledProtocols(this.f(sSLSocket.getEnabledProtocols()));
        return sSLSocket;
    }

    public Socket createSocket(Socket socket, String string, int n2, boolean bl) {
        SSLSocket sSLSocket = (SSLSocket)this.AZ.createSocket(socket, string, n2, bl);
        sSLSocket.setEnabledCipherSuites(this.e(sSLSocket.getEnabledCipherSuites()));
        sSLSocket.setEnabledProtocols(this.f(sSLSocket.getEnabledProtocols()));
        return sSLSocket;
    }

    public String[] getDefaultCipherSuites() {
        return this.e(this.AZ.getDefaultCipherSuites());
    }

    public String[] getSupportedCipherSuites() {
        return this.e(this.AZ.getSupportedCipherSuites());
    }
}

