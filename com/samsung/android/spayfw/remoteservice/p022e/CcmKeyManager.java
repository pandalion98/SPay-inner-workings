package com.samsung.android.spayfw.remoteservice.p022e;

import android.util.Log;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.net.Socket;
import java.security.KeyStore;
import java.security.Principal;
import java.security.PrivateKey;
import javax.net.ssl.X509KeyManager;

/* renamed from: com.samsung.android.spayfw.remoteservice.e.a */
public class CcmKeyManager implements X509KeyManager {
    private String defaultClientAlias;

    public CcmKeyManager() {
        this.defaultClientAlias = null;
        this.defaultClientAlias = "Samsung default";
        Log.d("Spay_CcmKeyManager", "defaultClientAlias = " + this.defaultClientAlias);
    }

    public String chooseClientAlias(String[] strArr, Principal[] principalArr, Socket socket) {
        Log.d("Spay_CcmKeyManager", "chooseClientAlias");
        return this.defaultClientAlias;
    }

    public String chooseServerAlias(String str, Principal[] principalArr, Socket socket) {
        Log.d("Spay_CcmKeyManager", "chooseServerAlias");
        return null;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.security.cert.X509Certificate[] getCertificateChain(java.lang.String r7) {
        /*
        r6 = this;
        r2 = 0;
        r0 = "Spay_CcmKeyManager";
        r1 = "getCertificateChain";
        android.util.Log.d(r0, r1);
        r0 = "TimaKeyStore";
        r0 = java.security.KeyStore.getInstance(r0);	 Catch:{ KeyStoreException -> 0x00c7, NoSuchAlgorithmException -> 0x00d3, CertificateException -> 0x00df, IOException -> 0x00eb, IllegalArgumentException -> 0x00f7, Exception -> 0x0103 }
        r1 = "Spay_CcmKeyManager";
        r3 = new java.lang.StringBuilder;	 Catch:{ KeyStoreException -> 0x00c7, NoSuchAlgorithmException -> 0x00d3, CertificateException -> 0x00df, IOException -> 0x00eb, IllegalArgumentException -> 0x00f7, Exception -> 0x0103 }
        r3.<init>();	 Catch:{ KeyStoreException -> 0x00c7, NoSuchAlgorithmException -> 0x00d3, CertificateException -> 0x00df, IOException -> 0x00eb, IllegalArgumentException -> 0x00f7, Exception -> 0x0103 }
        r4 = "timaKeystore =";
        r3 = r3.append(r4);	 Catch:{ KeyStoreException -> 0x00c7, NoSuchAlgorithmException -> 0x00d3, CertificateException -> 0x00df, IOException -> 0x00eb, IllegalArgumentException -> 0x00f7, Exception -> 0x0103 }
        r3 = r3.append(r0);	 Catch:{ KeyStoreException -> 0x00c7, NoSuchAlgorithmException -> 0x00d3, CertificateException -> 0x00df, IOException -> 0x00eb, IllegalArgumentException -> 0x00f7, Exception -> 0x0103 }
        r3 = r3.toString();	 Catch:{ KeyStoreException -> 0x00c7, NoSuchAlgorithmException -> 0x00d3, CertificateException -> 0x00df, IOException -> 0x00eb, IllegalArgumentException -> 0x00f7, Exception -> 0x0103 }
        android.util.Log.d(r1, r3);	 Catch:{ KeyStoreException -> 0x00c7, NoSuchAlgorithmException -> 0x00d3, CertificateException -> 0x00df, IOException -> 0x00eb, IllegalArgumentException -> 0x00f7, Exception -> 0x0103 }
        if (r0 == 0) goto L_0x00b1;
    L_0x0028:
        r1 = "Spay_CcmKeyManager";
        r3 = "timaKeystore is not null";
        android.util.Log.d(r1, r3);	 Catch:{ KeyStoreException -> 0x00c7, NoSuchAlgorithmException -> 0x00d3, CertificateException -> 0x00df, IOException -> 0x00eb, IllegalArgumentException -> 0x00f7, Exception -> 0x0103 }
        r1 = 0;
        r3 = 0;
        r0.load(r1, r3);	 Catch:{ KeyStoreException -> 0x00c7, NoSuchAlgorithmException -> 0x00d3, CertificateException -> 0x00df, IOException -> 0x00eb, IllegalArgumentException -> 0x00f7, Exception -> 0x0103 }
        r0 = "PKCS11";
        r1 = "SECPkcs11";
        r0 = java.security.KeyStore.getInstance(r0, r1);	 Catch:{ KeyStoreException -> 0x00c7, NoSuchAlgorithmException -> 0x00d3, CertificateException -> 0x00df, IOException -> 0x00eb, IllegalArgumentException -> 0x00f7, Exception -> 0x0103 }
        r1 = "Spay_CcmKeyManager";
        r3 = new java.lang.StringBuilder;	 Catch:{ KeyStoreException -> 0x00c7, NoSuchAlgorithmException -> 0x00d3, CertificateException -> 0x00df, IOException -> 0x00eb, IllegalArgumentException -> 0x00f7, Exception -> 0x0103 }
        r3.<init>();	 Catch:{ KeyStoreException -> 0x00c7, NoSuchAlgorithmException -> 0x00d3, CertificateException -> 0x00df, IOException -> 0x00eb, IllegalArgumentException -> 0x00f7, Exception -> 0x0103 }
        r4 = "ks = ";
        r3 = r3.append(r4);	 Catch:{ KeyStoreException -> 0x00c7, NoSuchAlgorithmException -> 0x00d3, CertificateException -> 0x00df, IOException -> 0x00eb, IllegalArgumentException -> 0x00f7, Exception -> 0x0103 }
        r3 = r3.append(r0);	 Catch:{ KeyStoreException -> 0x00c7, NoSuchAlgorithmException -> 0x00d3, CertificateException -> 0x00df, IOException -> 0x00eb, IllegalArgumentException -> 0x00f7, Exception -> 0x0103 }
        r3 = r3.toString();	 Catch:{ KeyStoreException -> 0x00c7, NoSuchAlgorithmException -> 0x00d3, CertificateException -> 0x00df, IOException -> 0x00eb, IllegalArgumentException -> 0x00f7, Exception -> 0x0103 }
        android.util.Log.d(r1, r3);	 Catch:{ KeyStoreException -> 0x00c7, NoSuchAlgorithmException -> 0x00d3, CertificateException -> 0x00df, IOException -> 0x00eb, IllegalArgumentException -> 0x00f7, Exception -> 0x0103 }
        if (r0 == 0) goto L_0x00c3;
    L_0x0056:
        r1 = "Spay_CcmKeyManager";
        r3 = "ks is not null";
        android.util.Log.d(r1, r3);	 Catch:{ KeyStoreException -> 0x00c7, NoSuchAlgorithmException -> 0x00d3, CertificateException -> 0x00df, IOException -> 0x00eb, IllegalArgumentException -> 0x00f7, Exception -> 0x0103 }
        r1 = 0;
        r3 = 0;
        r0.load(r1, r3);	 Catch:{ KeyStoreException -> 0x00c7, NoSuchAlgorithmException -> 0x00d3, CertificateException -> 0x00df, IOException -> 0x00eb, IllegalArgumentException -> 0x00f7, Exception -> 0x0103 }
        r4 = r0.getCertificateChain(r7);	 Catch:{ KeyStoreException -> 0x00c7, NoSuchAlgorithmException -> 0x00d3, CertificateException -> 0x00df, IOException -> 0x00eb, IllegalArgumentException -> 0x00f7, Exception -> 0x0103 }
        r0 = "Spay_CcmKeyManager";
        r1 = new java.lang.StringBuilder;	 Catch:{ KeyStoreException -> 0x00c7, NoSuchAlgorithmException -> 0x00d3, CertificateException -> 0x00df, IOException -> 0x00eb, IllegalArgumentException -> 0x00f7, Exception -> 0x0103 }
        r1.<init>();	 Catch:{ KeyStoreException -> 0x00c7, NoSuchAlgorithmException -> 0x00d3, CertificateException -> 0x00df, IOException -> 0x00eb, IllegalArgumentException -> 0x00f7, Exception -> 0x0103 }
        r3 = "getCertificateChain alias = ";
        r1 = r1.append(r3);	 Catch:{ KeyStoreException -> 0x00c7, NoSuchAlgorithmException -> 0x00d3, CertificateException -> 0x00df, IOException -> 0x00eb, IllegalArgumentException -> 0x00f7, Exception -> 0x0103 }
        r1 = r1.append(r7);	 Catch:{ KeyStoreException -> 0x00c7, NoSuchAlgorithmException -> 0x00d3, CertificateException -> 0x00df, IOException -> 0x00eb, IllegalArgumentException -> 0x00f7, Exception -> 0x0103 }
        r1 = r1.toString();	 Catch:{ KeyStoreException -> 0x00c7, NoSuchAlgorithmException -> 0x00d3, CertificateException -> 0x00df, IOException -> 0x00eb, IllegalArgumentException -> 0x00f7, Exception -> 0x0103 }
        android.util.Log.d(r0, r1);	 Catch:{ KeyStoreException -> 0x00c7, NoSuchAlgorithmException -> 0x00d3, CertificateException -> 0x00df, IOException -> 0x00eb, IllegalArgumentException -> 0x00f7, Exception -> 0x0103 }
        if (r4 == 0) goto L_0x00ba;
    L_0x0080:
        r0 = r4.length;	 Catch:{ KeyStoreException -> 0x00c7, NoSuchAlgorithmException -> 0x00d3, CertificateException -> 0x00df, IOException -> 0x00eb, IllegalArgumentException -> 0x00f7, Exception -> 0x0103 }
        if (r0 == 0) goto L_0x00ba;
    L_0x0083:
        r0 = "Spay_CcmKeyManager";
        r1 = new java.lang.StringBuilder;	 Catch:{ KeyStoreException -> 0x00c7, NoSuchAlgorithmException -> 0x00d3, CertificateException -> 0x00df, IOException -> 0x00eb, IllegalArgumentException -> 0x00f7, Exception -> 0x0103 }
        r1.<init>();	 Catch:{ KeyStoreException -> 0x00c7, NoSuchAlgorithmException -> 0x00d3, CertificateException -> 0x00df, IOException -> 0x00eb, IllegalArgumentException -> 0x00f7, Exception -> 0x0103 }
        r3 = "tempChain = ";
        r1 = r1.append(r3);	 Catch:{ KeyStoreException -> 0x00c7, NoSuchAlgorithmException -> 0x00d3, CertificateException -> 0x00df, IOException -> 0x00eb, IllegalArgumentException -> 0x00f7, Exception -> 0x0103 }
        r3 = r4.toString();	 Catch:{ KeyStoreException -> 0x00c7, NoSuchAlgorithmException -> 0x00d3, CertificateException -> 0x00df, IOException -> 0x00eb, IllegalArgumentException -> 0x00f7, Exception -> 0x0103 }
        r1 = r1.append(r3);	 Catch:{ KeyStoreException -> 0x00c7, NoSuchAlgorithmException -> 0x00d3, CertificateException -> 0x00df, IOException -> 0x00eb, IllegalArgumentException -> 0x00f7, Exception -> 0x0103 }
        r1 = r1.toString();	 Catch:{ KeyStoreException -> 0x00c7, NoSuchAlgorithmException -> 0x00d3, CertificateException -> 0x00df, IOException -> 0x00eb, IllegalArgumentException -> 0x00f7, Exception -> 0x0103 }
        android.util.Log.d(r0, r1);	 Catch:{ KeyStoreException -> 0x00c7, NoSuchAlgorithmException -> 0x00d3, CertificateException -> 0x00df, IOException -> 0x00eb, IllegalArgumentException -> 0x00f7, Exception -> 0x0103 }
        r0 = r4.length;	 Catch:{ KeyStoreException -> 0x00c7, NoSuchAlgorithmException -> 0x00d3, CertificateException -> 0x00df, IOException -> 0x00eb, IllegalArgumentException -> 0x00f7, Exception -> 0x0103 }
        r1 = new java.security.cert.X509Certificate[r0];	 Catch:{ KeyStoreException -> 0x00c7, NoSuchAlgorithmException -> 0x00d3, CertificateException -> 0x00df, IOException -> 0x00eb, IllegalArgumentException -> 0x00f7, Exception -> 0x0103 }
        r0 = 0;
        r3 = r0;
    L_0x00a4:
        r0 = r4.length;	 Catch:{ KeyStoreException -> 0x00c7, NoSuchAlgorithmException -> 0x00d3, CertificateException -> 0x00df, IOException -> 0x00eb, IllegalArgumentException -> 0x00f7, Exception -> 0x0110 }
        if (r3 >= r0) goto L_0x00c5;
    L_0x00a7:
        r0 = r4[r3];	 Catch:{ KeyStoreException -> 0x00c7, NoSuchAlgorithmException -> 0x00d3, CertificateException -> 0x00df, IOException -> 0x00eb, IllegalArgumentException -> 0x00f7, Exception -> 0x0110 }
        r0 = (java.security.cert.X509Certificate) r0;	 Catch:{ KeyStoreException -> 0x00c7, NoSuchAlgorithmException -> 0x00d3, CertificateException -> 0x00df, IOException -> 0x00eb, IllegalArgumentException -> 0x00f7, Exception -> 0x0110 }
        r1[r3] = r0;	 Catch:{ KeyStoreException -> 0x00c7, NoSuchAlgorithmException -> 0x00d3, CertificateException -> 0x00df, IOException -> 0x00eb, IllegalArgumentException -> 0x00f7, Exception -> 0x0110 }
        r0 = r3 + 1;
        r3 = r0;
        goto L_0x00a4;
    L_0x00b1:
        r0 = "Spay_CcmKeyManager";
        r1 = "timaKeystore is null";
        android.util.Log.e(r0, r1);	 Catch:{ KeyStoreException -> 0x00c7, NoSuchAlgorithmException -> 0x00d3, CertificateException -> 0x00df, IOException -> 0x00eb, IllegalArgumentException -> 0x00f7, Exception -> 0x0103 }
        r0 = r2;
    L_0x00b9:
        return r0;
    L_0x00ba:
        r0 = "Spay_CcmKeyManager";
        r1 = "tempChain is null";
        android.util.Log.e(r0, r1);	 Catch:{ KeyStoreException -> 0x00c7, NoSuchAlgorithmException -> 0x00d3, CertificateException -> 0x00df, IOException -> 0x00eb, IllegalArgumentException -> 0x00f7, Exception -> 0x0103 }
        r0 = r2;
        goto L_0x00b9;
    L_0x00c3:
        r0 = r2;
        goto L_0x00b9;
    L_0x00c5:
        r0 = r1;
        goto L_0x00b9;
    L_0x00c7:
        r0 = move-exception;
        r1 = "Spay_CcmKeyManager";
        r3 = r0.getMessage();
        android.util.Log.e(r1, r3, r0);
        r0 = r2;
        goto L_0x00b9;
    L_0x00d3:
        r0 = move-exception;
        r1 = "Spay_CcmKeyManager";
        r3 = r0.getMessage();
        android.util.Log.e(r1, r3, r0);
        r0 = r2;
        goto L_0x00b9;
    L_0x00df:
        r0 = move-exception;
        r1 = "Spay_CcmKeyManager";
        r3 = r0.getMessage();
        android.util.Log.e(r1, r3, r0);
        r0 = r2;
        goto L_0x00b9;
    L_0x00eb:
        r0 = move-exception;
        r1 = "Spay_CcmKeyManager";
        r3 = r0.getMessage();
        android.util.Log.e(r1, r3, r0);
        r0 = r2;
        goto L_0x00b9;
    L_0x00f7:
        r0 = move-exception;
        r1 = "Spay_CcmKeyManager";
        r3 = r0.getMessage();
        android.util.Log.e(r1, r3, r0);
        r0 = r2;
        goto L_0x00b9;
    L_0x0103:
        r0 = move-exception;
        r1 = r0;
        r0 = r2;
    L_0x0106:
        r2 = "Spay_CcmKeyManager";
        r3 = r1.getMessage();
        android.util.Log.e(r2, r3, r1);
        goto L_0x00b9;
    L_0x0110:
        r0 = move-exception;
        r5 = r0;
        r0 = r1;
        r1 = r5;
        goto L_0x0106;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.remoteservice.e.a.getCertificateChain(java.lang.String):java.security.cert.X509Certificate[]");
    }

    public String[] getClientAliases(String str, Principal[] principalArr) {
        Log.d("Spay_CcmKeyManager", "getClientAliases");
        return new String[]{this.defaultClientAlias};
    }

    public String[] getServerAliases(String str, Principal[] principalArr) {
        Log.d("Spay_CcmKeyManager", "getServerAliases");
        return null;
    }

    public PrivateKey getPrivateKey(String str) {
        Log.d("Spay_CcmKeyManager", "getPrivateKey: alias = " + str);
        try {
            KeyStore instance = KeyStore.getInstance("TimaKeyStore");
            if (instance == null) {
                return null;
            }
            instance.load(null, null);
            instance = KeyStore.getInstance("PKCS11", "SECPkcs11");
            if (instance == null) {
                return null;
            }
            instance.load(null, null);
            return (PrivateKey) instance.getKey(str, BuildConfig.FLAVOR.toCharArray());
        } catch (Throwable e) {
            Log.e("Spay_CcmKeyManager", e.getMessage(), e);
            return null;
        } catch (Throwable e2) {
            Log.e("Spay_CcmKeyManager", e2.getMessage(), e2);
            return null;
        } catch (Throwable e22) {
            Log.e("Spay_CcmKeyManager", e22.getMessage(), e22);
            return null;
        } catch (Throwable e222) {
            Log.e("Spay_CcmKeyManager", e222.getMessage(), e222);
            return null;
        } catch (Throwable e2222) {
            Log.e("Spay_CcmKeyManager", e2222.getMessage(), e2222);
            return null;
        } catch (Throwable e22222) {
            Log.e("Spay_CcmKeyManager", e22222.getMessage(), e22222);
            return null;
        }
    }
}
