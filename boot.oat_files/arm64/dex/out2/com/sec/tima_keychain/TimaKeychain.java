package com.sec.tima_keychain;

import android.os.RemoteException;
import android.os.ServiceManager;
import android.sec.enterprise.ClientCertificateManager;
import android.sec.enterprise.EnterpriseDeviceManager;
import android.sec.enterprise.TimaKeystore;
import android.service.tima.ITimaService;
import android.service.tima.ITimaService.Stub;
import android.util.Log;
import com.sec.smartcard.openssl.OpenSSLHelper;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TimaKeychain {
    private static final String TAG = "TIMAKeyChain";
    private static final String TIMA_SERVICE = "tima";
    private static Object mLock = new Object();

    public static boolean isTimaKeystoreAndCCMEnabledForCaller() {
        boolean isCCMEnabled = false;
        boolean isTimaKeystoreEnabled = false;
        Log.d(TAG, "isTimaKeystoreAndCCMEnabled called");
        try {
            ITimaService mTimaService = Stub.asInterface(ServiceManager.getService(TIMA_SERVICE));
            if (mTimaService != null && mTimaService.getTimaVersion().equals("3.0")) {
                ClientCertificateManager ccm = EnterpriseDeviceManager.getInstance().getClientCertificateManager();
                if (ccm != null) {
                    if (!ccm.isCCMPolicyEnabledForCaller() || ccm.isAccessControlMethodPassword()) {
                        isCCMEnabled = false;
                    } else {
                        isCCMEnabled = true;
                    }
                }
                TimaKeystore timaKeystore = EnterpriseDeviceManager.getInstance().getTimaKeystore();
                if (timaKeystore != null) {
                    isTimaKeystoreEnabled = timaKeystore.isTimaKeystoreEnabled();
                }
            }
        } catch (RemoteException re) {
            Log.e(TAG, "RemoteException", re);
        }
        if (isCCMEnabled && isTimaKeystoreEnabled) {
            return true;
        }
        return false;
    }

    public static boolean isTimaKeystoreAndCCMEnabledForPackage(String packageName) {
        boolean isCCMEnabled = false;
        boolean isTimaKeystoreEnabled = false;
        Log.d(TAG, "isTimaKeystoreAndCCMEnabledForPackage called");
        if (packageName == null || "".equals(packageName)) {
            Log.e(TAG, "isTimaKeystoreAndCCMEnabledForPackage received empty/null package name");
        } else {
            try {
                ITimaService mTimaService = Stub.asInterface(ServiceManager.getService(TIMA_SERVICE));
                if (mTimaService != null && mTimaService.getTimaVersion().equals("3.0")) {
                    ClientCertificateManager ccm = EnterpriseDeviceManager.getInstance().getClientCertificateManager();
                    if (ccm != null) {
                        if (!ccm.isCCMPolicyEnabledForPackage(packageName) || ccm.isAccessControlMethodPassword()) {
                            isCCMEnabled = false;
                        } else {
                            isCCMEnabled = true;
                        }
                    }
                    TimaKeystore timaKeystore = EnterpriseDeviceManager.getInstance().getTimaKeystore();
                    if (timaKeystore != null) {
                        isTimaKeystoreEnabled = timaKeystore.isTimaKeystoreEnabledForPackage(packageName);
                    }
                }
            } catch (RemoteException re) {
                Log.e(TAG, "RemoteException", re);
            }
            Log.d(TAG, "isCCMEnabled : " + isCCMEnabled);
            Log.d(TAG, "isTimaKeystoreEnabled : " + isTimaKeystoreEnabled);
        }
        if (isCCMEnabled && isTimaKeystoreEnabled) {
            return true;
        }
        return false;
    }

    public static X509Certificate[] getCertificateChainFromTimaKeystore(String alias) {
        X509Certificate[] x509CertChain;
        synchronized (mLock) {
            x509CertChain = null;
            Log.d(TAG, "getCertificateChainFromTimaKeystore called");
            if (alias == null || "".equals(alias)) {
                Log.e(TAG, "getCertificateChainFromTimaKeystore received empty/null alias");
            } else {
                try {
                    KeyStore.getInstance("TimaKeyStore").load(null, null);
                    KeyStore secpkcs11Ks = KeyStore.getInstance("PKCS11", "SECPkcs11");
                    secpkcs11Ks.load(null, null);
                    Certificate[] certChain = secpkcs11Ks.getCertificateChain(alias);
                    if (certChain != null) {
                        x509CertChain = new X509Certificate[certChain.length];
                        for (int i = 0; i < certChain.length; i++) {
                            x509CertChain[i] = (X509Certificate) certChain[i];
                        }
                    }
                } catch (NoSuchProviderException e) {
                    Log.e(TAG, "java.security.NoSuchProviderException", e);
                } catch (KeyStoreException e2) {
                    Log.e(TAG, "KeyStoreException", e2);
                } catch (IOException ioe) {
                    Log.e(TAG, "IOException", ioe);
                } catch (NoSuchAlgorithmException ae) {
                    Log.e(TAG, "NoSuchAlgorithmException", ae);
                } catch (CertificateException ce) {
                    Log.e(TAG, "CertificateException", ce);
                }
            }
        }
        return x509CertChain;
    }

    public static PrivateKey getPrivateKeyFromOpenSSL(String alias) {
        try {
            KeyStore.getInstance("TimaKeyStore").load(null, null);
            KeyStore.getInstance("PKCS11", "SECPkcs11").load(null, null);
            Log.d(TAG, "getPrivateKeyFromOpenSSL called");
            if (alias == null || "".equals(alias)) {
                Log.e(TAG, "getPrivateKeyFromOpenSSL received empty/null alias");
                return null;
            }
            OpenSSLHelper opensslHelper = new OpenSSLHelper();
            if (opensslHelper.registerEngine(alias)) {
                return opensslHelper.getPrivateKey(alias);
            }
            Log.e(TAG, "Unable to register openssl engine");
            return null;
        } catch (NoSuchProviderException e) {
            Log.e(TAG, "java.security.NoSuchProviderException", e);
            return null;
        } catch (KeyStoreException e2) {
            Log.e(TAG, "KeyStoreException", e2);
            return null;
        } catch (IOException ioe) {
            Log.e(TAG, "IOException", ioe);
            return null;
        } catch (NoSuchAlgorithmException ae) {
            Log.e(TAG, "NoSuchAlgorithmException", ae);
            return null;
        } catch (CertificateException ce) {
            Log.e(TAG, "CertificateException", ce);
            return null;
        }
    }

    public static List<String> getAliasListFromTimaKeystore(String packageName) {
        Exception e;
        boolean isPrivKeyAliasesExist = true;
        List<String> aliasList = Collections.emptyList();
        Log.d(TAG, "getAliasListFromTimaKeystore with package");
        if (packageName == null || "".equals(packageName)) {
            Log.e(TAG, "getAliasListFromTimaKeystore received empty/null packageName");
            return Collections.emptyList();
        }
        try {
            ClientCertificateManager ccm = EnterpriseDeviceManager.getInstance().getClientCertificateManager();
            List<String> aliasesHavingPrvKey = ccm.getCertificateAliasesHavingPrivateKey();
            List<String> ccmAliases = ccm.getAliasesForPackage(packageName);
            List<String> wifiAliases = ccm.getAliasesForWiFi();
            if (ccmAliases == null) {
                return aliasList;
            }
            boolean isWifiAliasesExist;
            List<String> aliasList2 = new ArrayList(ccmAliases);
            if (wifiAliases != null) {
                try {
                    if (!wifiAliases.isEmpty()) {
                        isWifiAliasesExist = true;
                        if (aliasesHavingPrvKey == null || aliasesHavingPrvKey.isEmpty()) {
                            isPrivKeyAliasesExist = false;
                        }
                        if (isPrivKeyAliasesExist) {
                            Log.d(TAG, "all the aliases not valid since doenst have private key pair");
                            return aliasList2;
                        }
                        if (isWifiAliasesExist || isPrivKeyAliasesExist) {
                            for (String tempAlias : ccmAliases) {
                                if (isWifiAliasesExist && wifiAliases.contains(tempAlias)) {
                                    aliasList2.remove(tempAlias);
                                }
                                if (isPrivKeyAliasesExist && !aliasesHavingPrvKey.contains(tempAlias)) {
                                    aliasList2.remove(tempAlias);
                                }
                            }
                        }
                        return aliasList2;
                    }
                } catch (Exception e2) {
                    e = e2;
                    aliasList = aliasList2;
                    Log.e(TAG, "Exception", e);
                    e.printStackTrace();
                    return aliasList;
                }
            }
            isWifiAliasesExist = false;
            isPrivKeyAliasesExist = false;
            if (isPrivKeyAliasesExist) {
                for (String tempAlias2 : ccmAliases) {
                    aliasList2.remove(tempAlias2);
                    aliasList2.remove(tempAlias2);
                }
                return aliasList2;
            }
            Log.d(TAG, "all the aliases not valid since doenst have private key pair");
            return aliasList2;
        } catch (Exception e3) {
            e = e3;
            Log.e(TAG, "Exception", e);
            e.printStackTrace();
            return aliasList;
        }
    }
}
