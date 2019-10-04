/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.util.Log
 *  java.io.IOException
 *  java.io.InputStream
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.net.Socket
 *  java.security.Key
 *  java.security.KeyStore
 *  java.security.KeyStoreException
 *  java.security.NoSuchAlgorithmException
 *  java.security.Principal
 *  java.security.PrivateKey
 *  java.security.cert.Certificate
 *  java.security.cert.CertificateException
 *  java.security.cert.X509Certificate
 *  javax.net.ssl.X509KeyManager
 */
package com.samsung.android.spayfw.remoteservice.e;

import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509KeyManager;

public class a
implements X509KeyManager {
    private String defaultClientAlias = "Samsung default";

    public a() {
        Log.d((String)"Spay_CcmKeyManager", (String)("defaultClientAlias = " + this.defaultClientAlias));
    }

    public String chooseClientAlias(String[] arrstring, Principal[] arrprincipal, Socket socket) {
        Log.d((String)"Spay_CcmKeyManager", (String)"chooseClientAlias");
        return this.defaultClientAlias;
    }

    public String chooseServerAlias(String string, Principal[] arrprincipal, Socket socket) {
        Log.d((String)"Spay_CcmKeyManager", (String)"chooseServerAlias");
        return null;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public X509Certificate[] getCertificateChain(String var1_1) {
        Log.d((String)"Spay_CcmKeyManager", (String)"getCertificateChain");
        try {
            block12 : {
                block14 : {
                    block16 : {
                        block15 : {
                            block13 : {
                                var17_2 = KeyStore.getInstance((String)"TimaKeyStore");
                                Log.d((String)"Spay_CcmKeyManager", (String)("timaKeystore =" + (Object)var17_2));
                                if (var17_2 == null) break block13;
                                Log.d((String)"Spay_CcmKeyManager", (String)"timaKeystore is not null");
                                var17_2.load(null, null);
                                var20_3 = KeyStore.getInstance((String)"PKCS11", (String)"SECPkcs11");
                                Log.d((String)"Spay_CcmKeyManager", (String)("ks = " + (Object)var20_3));
                                if (var20_3 == null) break block14;
                                Log.d((String)"Spay_CcmKeyManager", (String)"ks is not null");
                                var20_3.load(null, null);
                                var23_4 = var20_3.getCertificateChain(var1_1);
                                Log.d((String)"Spay_CcmKeyManager", (String)("getCertificateChain alias = " + var1_1));
                                if (var23_4 == null || var23_4.length == 0) break block15;
                                break block16;
                            }
                            Log.e((String)"Spay_CcmKeyManager", (String)"timaKeystore is null");
                            return null;
                        }
                        Log.e((String)"Spay_CcmKeyManager", (String)"tempChain is null");
                        return null;
                    }
                    Log.d((String)"Spay_CcmKeyManager", (String)("tempChain = " + var23_4.toString()));
                    var27_5 = new X509Certificate[var23_4.length];
                    break block12;
                }
                return null;
            }
            try {
                for (var28_6 = 0; var28_6 < var23_4.length; ++var28_6) {
                    var27_5[var28_6] = (X509Certificate)var23_4[var28_6];
                }
                return var27_5;
            }
            catch (Exception var29_15) {
                var5_14 = var27_5;
                var4_13 = var29_15;
                ** GOTO lbl58
            }
        }
        catch (KeyStoreException var15_7) {
            Log.e((String)"Spay_CcmKeyManager", (String)var15_7.getMessage(), (Throwable)var15_7);
            return null;
        }
        catch (NoSuchAlgorithmException var13_8) {
            Log.e((String)"Spay_CcmKeyManager", (String)var13_8.getMessage(), (Throwable)var13_8);
            return null;
        }
        catch (CertificateException var11_9) {
            Log.e((String)"Spay_CcmKeyManager", (String)var11_9.getMessage(), (Throwable)var11_9);
            return null;
        }
        catch (IOException var9_10) {
            Log.e((String)"Spay_CcmKeyManager", (String)var9_10.getMessage(), (Throwable)var9_10);
            return null;
        }
        catch (IllegalArgumentException var7_11) {
            Log.e((String)"Spay_CcmKeyManager", (String)var7_11.getMessage(), (Throwable)var7_11);
            return null;
        }
        catch (Exception var3_12) {
            var4_13 = var3_12;
            var5_14 = null;
lbl58: // 2 sources:
            Log.e((String)"Spay_CcmKeyManager", (String)var4_13.getMessage(), (Throwable)var4_13);
            return var5_14;
        }
    }

    public String[] getClientAliases(String string, Principal[] arrprincipal) {
        Log.d((String)"Spay_CcmKeyManager", (String)"getClientAliases");
        String[] arrstring = new String[]{this.defaultClientAlias};
        return arrstring;
    }

    public PrivateKey getPrivateKey(String string) {
        PrivateKey privateKey;
        block9 : {
            Key key;
            Log.d((String)"Spay_CcmKeyManager", (String)("getPrivateKey: alias = " + string));
            KeyStore keyStore = KeyStore.getInstance((String)"TimaKeyStore");
            privateKey = null;
            if (keyStore == null) break block9;
            keyStore.load(null, null);
            KeyStore keyStore2 = KeyStore.getInstance((String)"PKCS11", (String)"SECPkcs11");
            privateKey = null;
            if (keyStore2 == null) break block9;
            try {
                keyStore2.load(null, null);
                key = keyStore2.getKey(string, "".toCharArray());
            }
            catch (KeyStoreException keyStoreException) {
                Log.e((String)"Spay_CcmKeyManager", (String)keyStoreException.getMessage(), (Throwable)keyStoreException);
                return null;
            }
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                Log.e((String)"Spay_CcmKeyManager", (String)noSuchAlgorithmException.getMessage(), (Throwable)noSuchAlgorithmException);
                return null;
            }
            catch (CertificateException certificateException) {
                Log.e((String)"Spay_CcmKeyManager", (String)certificateException.getMessage(), (Throwable)certificateException);
                return null;
            }
            catch (IOException iOException) {
                Log.e((String)"Spay_CcmKeyManager", (String)iOException.getMessage(), (Throwable)iOException);
                return null;
            }
            catch (IllegalArgumentException illegalArgumentException) {
                Log.e((String)"Spay_CcmKeyManager", (String)illegalArgumentException.getMessage(), (Throwable)illegalArgumentException);
                return null;
            }
            catch (Exception exception) {
                Log.e((String)"Spay_CcmKeyManager", (String)exception.getMessage(), (Throwable)exception);
                return null;
            }
            privateKey = (PrivateKey)key;
        }
        return privateKey;
    }

    public String[] getServerAliases(String string, Principal[] arrprincipal) {
        Log.d((String)"Spay_CcmKeyManager", (String)"getServerAliases");
        return null;
    }
}

