package com.samsung.android.spayfw.payprovider.mastercard.tds.network;

import com.samsung.android.spayfw.p002b.Log;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class McTdsSniSocketfactory extends SSLSocketFactory {
    private static ArrayList<String> SECURE_CIPHER_SUITE_LIST = null;
    private static ArrayList<String> SECURE_PROTOCOL_LIST = null;
    private static final String TAG = "McTdsSniSocketfactory";
    private static McTdsSniSocketfactory sInstance;
    private static SSLContext sslContext;

    /* renamed from: com.samsung.android.spayfw.payprovider.mastercard.tds.network.McTdsSniSocketfactory.1 */
    class C05681 implements HandshakeCompletedListener {
        C05681() {
        }

        public void handshakeCompleted(HandshakeCompletedEvent handshakeCompletedEvent) {
            try {
                SSLSession session = handshakeCompletedEvent.getSession();
                Log.m285d(McTdsSniSocketfactory.TAG, "Established " + session.getProtocol() + " connection with " + session.getPeerHost() + " using " + session.getCipherSuite());
            } catch (NullPointerException e) {
                e.printStackTrace();
                Log.m286e(McTdsSniSocketfactory.TAG, "handshakeCompleted : " + e.getMessage());
            }
        }
    }

    static {
        SECURE_CIPHER_SUITE_LIST = new ArrayList();
        SECURE_PROTOCOL_LIST = new ArrayList();
        SECURE_CIPHER_SUITE_LIST.add("TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256");
        SECURE_CIPHER_SUITE_LIST.add("TLS_DHE_RSA_WITH_AES_128_GCM_SHA256");
        SECURE_CIPHER_SUITE_LIST.add("TLS_RSA_WITH_AES_256_CBC_SHA");
        SECURE_PROTOCOL_LIST.add("TLSv1.2");
    }

    private McTdsSniSocketfactory() {
        sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, null, null);
    }

    public static synchronized McTdsSniSocketfactory getInstance() {
        McTdsSniSocketfactory mcTdsSniSocketfactory;
        synchronized (McTdsSniSocketfactory.class) {
            if (sInstance == null) {
                try {
                    sInstance = new McTdsSniSocketfactory();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                    Log.m286e(TAG, "SSL Init Error: " + e.getMessage());
                } catch (KeyManagementException e2) {
                    e2.printStackTrace();
                    Log.m286e(TAG, "SSL Init Error: " + e2.getMessage());
                }
            }
            mcTdsSniSocketfactory = sInstance;
        }
        return mcTdsSniSocketfactory;
    }

    public String[] getDefaultCipherSuites() {
        return (String[]) SECURE_CIPHER_SUITE_LIST.toArray(new String[SECURE_CIPHER_SUITE_LIST.size()]);
    }

    public String[] getSupportedCipherSuites() {
        return (String[]) SECURE_PROTOCOL_LIST.toArray(new String[SECURE_PROTOCOL_LIST.size()]);
    }

    public Socket createSocket(Socket socket, String str, int i, boolean z) {
        try {
            SSLSocket sSLSocket = (SSLSocket) sslContext.getSocketFactory().createSocket(socket, str, i, z);
            enableCipherSuitesAndProtocols(sSLSocket);
            sSLSocket.addHandshakeCompletedListener(new C05681());
            return sSLSocket;
        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.m286e(TAG, "Unable to create socket : " + e.getMessage());
            throw new IOException(e.getMessage());
        }
    }

    public Socket createSocket(String str, int i) {
        SSLSocket sSLSocket = (SSLSocket) sslContext.getSocketFactory().createSocket(str, i);
        enableCipherSuitesAndProtocols(sSLSocket);
        return sSLSocket;
    }

    public Socket createSocket(String str, int i, InetAddress inetAddress, int i2) {
        SSLSocket sSLSocket = (SSLSocket) sslContext.getSocketFactory().createSocket(str, i, inetAddress, i2);
        enableCipherSuitesAndProtocols(sSLSocket);
        return sSLSocket;
    }

    public Socket createSocket(InetAddress inetAddress, int i) {
        SSLSocket sSLSocket = (SSLSocket) sslContext.getSocketFactory().createSocket(inetAddress, i);
        enableCipherSuitesAndProtocols(sSLSocket);
        return sSLSocket;
    }

    public Socket createSocket(InetAddress inetAddress, int i, InetAddress inetAddress2, int i2) {
        SSLSocket sSLSocket = (SSLSocket) sslContext.getSocketFactory().createSocket(inetAddress, i, inetAddress2, i2);
        enableCipherSuitesAndProtocols(sSLSocket);
        return sSLSocket;
    }

    private SSLSocket enableCipherSuitesAndProtocols(SSLSocket sSLSocket) {
        if (sSLSocket == null) {
            return null;
        }
        sSLSocket.setEnabledCipherSuites((String[]) SECURE_CIPHER_SUITE_LIST.toArray(new String[SECURE_CIPHER_SUITE_LIST.size()]));
        sSLSocket.setEnabledProtocols((String[]) SECURE_PROTOCOL_LIST.toArray(new String[SECURE_PROTOCOL_LIST.size()]));
        return sSLSocket;
    }
}
