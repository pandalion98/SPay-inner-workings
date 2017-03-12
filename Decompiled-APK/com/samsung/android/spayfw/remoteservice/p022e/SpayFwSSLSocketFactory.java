package com.samsung.android.spayfw.remoteservice.p022e;

import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/* renamed from: com.samsung.android.spayfw.remoteservice.e.b */
public class SpayFwSSLSocketFactory extends SSLSocketFactory {
    private static SpayFwSSLSocketFactory Ba;
    private static ArrayList<String> SECURE_CIPHER_SUITE_LIST;
    private static ArrayList<String> SECURE_PROTOCOL_LIST;
    private final SSLSocketFactory AZ;

    static {
        SECURE_CIPHER_SUITE_LIST = new ArrayList();
        SECURE_PROTOCOL_LIST = new ArrayList();
        SECURE_CIPHER_SUITE_LIST.add("TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256");
        SECURE_PROTOCOL_LIST.add("TLSv1.2");
    }

    public static synchronized SpayFwSSLSocketFactory m1187a(SSLSocketFactory sSLSocketFactory) {
        SpayFwSSLSocketFactory spayFwSSLSocketFactory;
        synchronized (SpayFwSSLSocketFactory.class) {
            if (Ba == null) {
                Ba = new SpayFwSSLSocketFactory(sSLSocketFactory);
            }
            spayFwSSLSocketFactory = Ba;
        }
        return spayFwSSLSocketFactory;
    }

    private SpayFwSSLSocketFactory(SSLSocketFactory sSLSocketFactory) {
        this.AZ = sSLSocketFactory;
    }

    private String[] m1188e(String[] strArr) {
        if (strArr == null || strArr.length == 0) {
            return null;
        }
        ArrayList arrayList = new ArrayList(Arrays.asList(strArr));
        arrayList.retainAll(SECURE_CIPHER_SUITE_LIST);
        return (String[]) arrayList.toArray(new String[arrayList.size()]);
    }

    private String[] m1189f(String[] strArr) {
        if (strArr == null || strArr.length == 0) {
            return null;
        }
        ArrayList arrayList = new ArrayList(Arrays.asList(strArr));
        arrayList.retainAll(SECURE_PROTOCOL_LIST);
        return (String[]) arrayList.toArray(new String[arrayList.size()]);
    }

    public Socket createSocket(InetAddress inetAddress, int i) {
        SSLSocket sSLSocket = (SSLSocket) this.AZ.createSocket(inetAddress, i);
        sSLSocket.setEnabledCipherSuites(m1188e(sSLSocket.getEnabledCipherSuites()));
        sSLSocket.setEnabledProtocols(m1189f(sSLSocket.getEnabledProtocols()));
        return sSLSocket;
    }

    public Socket createSocket(InetAddress inetAddress, int i, InetAddress inetAddress2, int i2) {
        SSLSocket sSLSocket = (SSLSocket) this.AZ.createSocket(inetAddress, i, inetAddress2, i2);
        sSLSocket.setEnabledCipherSuites(m1188e(sSLSocket.getEnabledCipherSuites()));
        sSLSocket.setEnabledProtocols(m1189f(sSLSocket.getEnabledProtocols()));
        return sSLSocket;
    }

    public Socket createSocket(Socket socket, String str, int i, boolean z) {
        SSLSocket sSLSocket = (SSLSocket) this.AZ.createSocket(socket, str, i, z);
        sSLSocket.setEnabledCipherSuites(m1188e(sSLSocket.getEnabledCipherSuites()));
        sSLSocket.setEnabledProtocols(m1189f(sSLSocket.getEnabledProtocols()));
        return sSLSocket;
    }

    public Socket createSocket(String str, int i) {
        SSLSocket sSLSocket = (SSLSocket) this.AZ.createSocket(str, i);
        sSLSocket.setEnabledCipherSuites(m1188e(sSLSocket.getEnabledCipherSuites()));
        sSLSocket.setEnabledProtocols(m1189f(sSLSocket.getEnabledProtocols()));
        return sSLSocket;
    }

    public Socket createSocket(String str, int i, InetAddress inetAddress, int i2) {
        SSLSocket sSLSocket = (SSLSocket) this.AZ.createSocket(str, i, inetAddress, i2);
        sSLSocket.setEnabledCipherSuites(m1188e(sSLSocket.getEnabledCipherSuites()));
        sSLSocket.setEnabledProtocols(m1189f(sSLSocket.getEnabledProtocols()));
        return sSLSocket;
    }

    public String[] getDefaultCipherSuites() {
        return m1188e(this.AZ.getDefaultCipherSuites());
    }

    public String[] getSupportedCipherSuites() {
        return m1188e(this.AZ.getSupportedCipherSuites());
    }
}
