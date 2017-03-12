package com.samsung.android.spayfw.cncc;

import android.content.Context;
import com.samsung.android.spayfw.p002b.Log;
import java.net.Socket;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509KeyManager;

public class SpayCNCCX509KeyManager implements X509KeyManager {
    private static final String TAG = "SpayCNCCX509KeyManager";
    private String defaultClientAlias;
    SpaySSLAdapter spayDCMAdapter;

    public SpayCNCCX509KeyManager(Context context) {
        this.defaultClientAlias = "Samsung default";
        this.spayDCMAdapter = null;
        this.spayDCMAdapter = SpaySSLAdapter.getInstance(context);
    }

    public String chooseClientAlias(String[] strArr, Principal[] principalArr, Socket socket) {
        Log.m285d(TAG, "chooseClientAlias");
        return this.defaultClientAlias;
    }

    public String chooseServerAlias(String str, Principal[] principalArr, Socket socket) {
        Log.m285d(TAG, "chooseServerAlias");
        return null;
    }

    public X509Certificate[] getCertificateChain(String str) {
        Log.m285d(TAG, "getCertificateChain");
        X509Certificate[] x509CertificateChain = this.spayDCMAdapter.getX509CertificateChain();
        Log.m285d(TAG, "obtained certificate chain from SpaySSLAdapter");
        if (x509CertificateChain == null) {
            Log.m286e(TAG, "Error: Returned Certificate Chain is NULL");
        }
        return x509CertificateChain;
    }

    public String[] getClientAliases(String str, Principal[] principalArr) {
        Log.m285d(TAG, "getClientAliases");
        return new String[]{this.defaultClientAlias};
    }

    public PrivateKey getPrivateKey(String str) {
        Log.m285d(TAG, "getPrivateKey: alias = " + str);
        return this.spayDCMAdapter.getPrivateKey();
    }

    public String[] getServerAliases(String str, Principal[] principalArr) {
        Log.m285d(TAG, "getServerAliases");
        return null;
    }

    public static boolean isSupported(Context context) {
        return SpayDRKManager.isSupported(context);
    }
}
