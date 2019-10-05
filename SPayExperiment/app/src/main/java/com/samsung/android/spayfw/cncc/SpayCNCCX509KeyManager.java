/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  java.lang.Object
 *  java.lang.String
 *  java.net.Socket
 *  java.security.Principal
 *  java.security.PrivateKey
 *  java.security.cert.X509Certificate
 *  javax.net.ssl.X509KeyManager
 */
package com.samsung.android.spayfw.cncc;

import android.content.Context;
import com.samsung.android.spayfw.b.Log;

import java.net.Socket;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509KeyManager;

public class SpayCNCCX509KeyManager
implements X509KeyManager {
    private static final String TAG = "SpayCNCCX509KeyManager";
    private String defaultClientAlias = "Samsung default";
    SpaySSLAdapter spayDCMAdapter = null;

    public SpayCNCCX509KeyManager(Context context) {
        this.spayDCMAdapter = SpaySSLAdapter.getInstance(context);
    }

    public static boolean isSupported(Context context) {
        return SpayDRKManager.isSupported(context);
    }

    public String chooseClientAlias(String[] arrstring, Principal[] arrprincipal, Socket socket) {
        Log.d(TAG, "chooseClientAlias");
        return this.defaultClientAlias;
    }

    public String chooseServerAlias(String string, Principal[] arrprincipal, Socket socket) {
        Log.d(TAG, "chooseServerAlias");
        return null;
    }

    public X509Certificate[] getCertificateChain(String string) {
        Log.d(TAG, "getCertificateChain");
        X509Certificate[] arrx509Certificate = this.spayDCMAdapter.getX509CertificateChain();
        Log.d(TAG, "obtained certificate chain from SpaySSLAdapter");
        if (arrx509Certificate == null) {
            Log.e(TAG, "Error: Returned Certificate Chain is NULL");
        }
        return arrx509Certificate;
    }

    public String[] getClientAliases(String string, Principal[] arrprincipal) {
        Log.d(TAG, "getClientAliases");
        String[] arrstring = new String[]{this.defaultClientAlias};
        return arrstring;
    }

    public PrivateKey getPrivateKey(String string) {
        Log.d(TAG, "getPrivateKey: alias = " + string);
        return this.spayDCMAdapter.getPrivateKey();
    }

    public String[] getServerAliases(String string, Principal[] arrprincipal) {
        Log.d(TAG, "getServerAliases");
        return null;
    }
}

