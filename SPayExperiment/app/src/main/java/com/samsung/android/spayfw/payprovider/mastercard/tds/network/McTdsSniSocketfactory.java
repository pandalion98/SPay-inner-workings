/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.Class
 *  java.lang.NullPointerException
 *  java.lang.Object
 *  java.lang.String
 *  java.net.InetAddress
 *  java.net.Socket
 *  java.security.KeyManagementException
 *  java.security.NoSuchAlgorithmException
 *  java.security.SecureRandom
 *  java.util.ArrayList
 *  javax.net.ssl.HandshakeCompletedEvent
 *  javax.net.ssl.HandshakeCompletedListener
 *  javax.net.ssl.KeyManager
 *  javax.net.ssl.SSLContext
 *  javax.net.ssl.SSLSession
 *  javax.net.ssl.SSLSocket
 *  javax.net.ssl.SSLSocketFactory
 *  javax.net.ssl.TrustManager
 */
package com.samsung.android.spayfw.payprovider.mastercard.tds.network;

import com.samsung.android.spayfw.b.Log;

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

public class McTdsSniSocketfactory
extends SSLSocketFactory {
    private static ArrayList<String> SECURE_CIPHER_SUITE_LIST = new ArrayList();
    private static ArrayList<String> SECURE_PROTOCOL_LIST = new ArrayList();
    private static final String TAG = "McTdsSniSocketfactory";
    private static McTdsSniSocketfactory sInstance;
    private static SSLContext sslContext;

    static {
        SECURE_CIPHER_SUITE_LIST.add((Object)"TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256");
        SECURE_CIPHER_SUITE_LIST.add((Object)"TLS_DHE_RSA_WITH_AES_128_GCM_SHA256");
        SECURE_CIPHER_SUITE_LIST.add((Object)"TLS_RSA_WITH_AES_256_CBC_SHA");
        SECURE_PROTOCOL_LIST.add((Object)"TLSv1.2");
    }

    private McTdsSniSocketfactory() {
        sslContext = SSLContext.getInstance((String)"TLS");
        sslContext.init(null, null, null);
    }

    private SSLSocket enableCipherSuitesAndProtocols(SSLSocket sSLSocket) {
        if (sSLSocket == null) {
            return null;
        }
        sSLSocket.setEnabledCipherSuites((String[])SECURE_CIPHER_SUITE_LIST.toArray((Object[])new String[SECURE_CIPHER_SUITE_LIST.size()]));
        sSLSocket.setEnabledProtocols((String[])SECURE_PROTOCOL_LIST.toArray((Object[])new String[SECURE_PROTOCOL_LIST.size()]));
        return sSLSocket;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static McTdsSniSocketfactory getInstance() {
        Class<McTdsSniSocketfactory> class_ = McTdsSniSocketfactory.class;
        synchronized (McTdsSniSocketfactory.class) {
            McTdsSniSocketfactory mcTdsSniSocketfactory = sInstance;
            if (mcTdsSniSocketfactory != null) return sInstance;
            try {
                sInstance = new McTdsSniSocketfactory();
                return sInstance;
            }
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                noSuchAlgorithmException.printStackTrace();
                Log.e(TAG, "SSL Init Error: " + noSuchAlgorithmException.getMessage());
                return sInstance;
            }
            catch (KeyManagementException keyManagementException) {
                keyManagementException.printStackTrace();
                Log.e(TAG, "SSL Init Error: " + keyManagementException.getMessage());
            }
            return sInstance;
        }
    }

    public Socket createSocket(String string, int n2) {
        SSLSocket sSLSocket = (SSLSocket)sslContext.getSocketFactory().createSocket(string, n2);
        this.enableCipherSuitesAndProtocols(sSLSocket);
        return sSLSocket;
    }

    public Socket createSocket(String string, int n2, InetAddress inetAddress, int n3) {
        SSLSocket sSLSocket = (SSLSocket)sslContext.getSocketFactory().createSocket(string, n2, inetAddress, n3);
        this.enableCipherSuitesAndProtocols(sSLSocket);
        return sSLSocket;
    }

    public Socket createSocket(InetAddress inetAddress, int n2) {
        SSLSocket sSLSocket = (SSLSocket)sslContext.getSocketFactory().createSocket(inetAddress, n2);
        this.enableCipherSuitesAndProtocols(sSLSocket);
        return sSLSocket;
    }

    public Socket createSocket(InetAddress inetAddress, int n2, InetAddress inetAddress2, int n3) {
        SSLSocket sSLSocket = (SSLSocket)sslContext.getSocketFactory().createSocket(inetAddress, n2, inetAddress2, n3);
        this.enableCipherSuitesAndProtocols(sSLSocket);
        return sSLSocket;
    }

    public Socket createSocket(Socket socket, String string, int n2, boolean bl) {
        try {
            SSLSocket sSLSocket = (SSLSocket)sslContext.getSocketFactory().createSocket(socket, string, n2, bl);
            this.enableCipherSuitesAndProtocols(sSLSocket);
            sSLSocket.addHandshakeCompletedListener(new HandshakeCompletedListener(){

                public void handshakeCompleted(HandshakeCompletedEvent handshakeCompletedEvent) {
                    try {
                        SSLSession sSLSession = handshakeCompletedEvent.getSession();
                        Log.d(McTdsSniSocketfactory.TAG, "Established " + sSLSession.getProtocol() + " connection with " + sSLSession.getPeerHost() + " using " + sSLSession.getCipherSuite());
                        return;
                    }
                    catch (NullPointerException nullPointerException) {
                        nullPointerException.printStackTrace();
                        Log.e(McTdsSniSocketfactory.TAG, "handshakeCompleted : " + nullPointerException.getMessage());
                        return;
                    }
                }
            });
            return sSLSocket;
        }
        catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
            Log.e(TAG, "Unable to create socket : " + nullPointerException.getMessage());
            throw new IOException(nullPointerException.getMessage());
        }
    }

    public String[] getDefaultCipherSuites() {
        return (String[])SECURE_CIPHER_SUITE_LIST.toArray((Object[])new String[SECURE_CIPHER_SUITE_LIST.size()]);
    }

    public String[] getSupportedCipherSuites() {
        return (String[])SECURE_PROTOCOL_LIST.toArray((Object[])new String[SECURE_PROTOCOL_LIST.size()]);
    }

}

