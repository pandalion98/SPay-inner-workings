package android.net.wifi.hs20.cert;

import android.content.Context;
import android.util.Log;
import com.android.org.conscrypt.TrustManagerImpl;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HotspotDMCertificateManager {
    private static final String HS20ESTKeyStorePath = "/data/misc/wifi/hs20/HSClientCertESTKeyStore_";
    public static final String HS20_KEYSTORE_PATH = "/data/misc/wifi/hs20/";
    private static final String OSU_SERVER = "OSU";
    private static final String TAG = "HotspotDMCertificateManager";
    private HotspotDMValidationParameters hs20ValidationParameters;
    private String keyPass;
    private KeyStore ks;
    private int mCertID = 0;
    private Context mContext;

    public class CertTrustManager implements X509TrustManager {
        private String HS20_CA_KEYSTORE = "HS20CAKeyStore.bks";
        TrustManagerImpl tm;

        public void checkClientTrusted(X509Certificate[] arg0, String arg1) {
            Log.d(HotspotDMCertificateManager.TAG, "[checkClientTrusted] " + arg1);
        }

        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
            CertificateException e;
            IOException e2;
            Exception e3;
            Throwable th;
            FileInputStream fis = null;
            Log.d(HotspotDMCertificateManager.TAG, "[checkServerTrusted] X509Certificate amount:" + arg0.length + ", cryptography: " + arg1);
            int i = 0;
            while (i < arg0.length) {
                try {
                    Log.d(HotspotDMCertificateManager.TAG, "X509Certificate: " + arg0[i]);
                    Log.d(HotspotDMCertificateManager.TAG, "====================");
                    arg0[i].checkValidity();
                    i++;
                } catch (CertificateException e4) {
                    e = e4;
                } catch (IOException e5) {
                    e2 = e5;
                } catch (Exception e6) {
                    e3 = e6;
                } catch (Throwable th2) {
                    th = th2;
                }
            }
            File trustAnchors = new File(HotspotDMCertificateManager.HS20_KEYSTORE_PATH, this.HS20_CA_KEYSTORE);
            HotspotDMCertificateManager.this.ks = KeyStore.getInstance(KeyStore.getDefaultType());
            Log.i(HotspotDMCertificateManager.TAG, "fetchTrustManagerFactory:loading trust managers");
            FileInputStream fis2 = new FileInputStream(trustAnchors);
            try {
                HotspotDMCertificateManager.this.ks.load(fis2, HotspotDMCertificateManager.this.keyPass.toCharArray());
                fis2.close();
                this.tm = new TrustManagerImpl(HotspotDMCertificateManager.this.ks);
                this.tm.checkServerTrusted(arg0, arg0[0].getPublicKey().getAlgorithm());
                if (!new HotspotDMCertificateValidation(arg0, HotspotDMCertificateManager.this.hs20ValidationParameters).validate()) {
                    Log.i(HotspotDMCertificateManager.TAG, "hs20certificate validation also failed");
                    throw new CertificateException("Passpoint Certificate Validations are failed");
                } else if (fis2 != null) {
                    try {
                        fis2.close();
                    } catch (IOException e22) {
                        e22.printStackTrace();
                    }
                }
            } catch (CertificateException e7) {
                e = e7;
                fis = fis2;
                e.printStackTrace();
                throw e;
            } catch (IOException e8) {
                e22 = e8;
                fis = fis2;
                e22.printStackTrace();
                throw new RuntimeException(e22);
            } catch (Exception e9) {
                e3 = e9;
                fis = fis2;
                e3.printStackTrace();
                throw new RuntimeException(e3);
            } catch (Throwable th3) {
                th = th3;
                fis = fis2;
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e222) {
                        e222.printStackTrace();
                    }
                }
                throw th;
            }
        }

        public X509Certificate[] getAcceptedIssuers() {
            Log.d(HotspotDMCertificateManager.TAG, "[getAcceptedIssuers] ");
            return this.tm.getAcceptedIssuers();
        }
    }

    public HotspotDMCertificateManager() {
        try {
            this.ks = KeyStore.getInstance(KeyStore.getDefaultType());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setParams(HotspotDMValidationParameters parameters) {
        this.hs20ValidationParameters = parameters;
    }

    public void setKeyPass(String keypass) {
        this.keyPass = keypass;
    }

    public KeyManager[] fetchKeyManFac(String serverType) throws Exception {
        IOException e;
        Throwable th;
        if (OSU_SERVER.equals(serverType)) {
            return null;
        }
        Log.i(TAG, "fetchKeyManFac(int credentialID)");
        File clientKeyStore = new File(HS20ESTKeyStorePath + this.mCertID + ".p12");
        FileInputStream fis = null;
        char[] password = this.keyPass.toCharArray();
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        try {
            if (clientKeyStore.exists()) {
                KeyStore keyStore = KeyStore.getInstance("PKCS12");
                FileInputStream fis2 = new FileInputStream(clientKeyStore);
                try {
                    keyStore.load(fis2, password);
                    keyManagerFactory.init(keyStore, password);
                    fis = fis2;
                } catch (IOException e2) {
                    e = e2;
                    fis = fis2;
                    try {
                        Log.i(TAG, "got an exception ");
                        e.printStackTrace();
                        if (fis != null) {
                            try {
                                fis.close();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                        return keyManagerFactory.getKeyManagers();
                    } catch (Throwable th2) {
                        th = th2;
                        if (fis != null) {
                            try {
                                fis.close();
                            } catch (IOException ex2) {
                                ex2.printStackTrace();
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    fis = fis2;
                    if (fis != null) {
                        fis.close();
                    }
                    throw th;
                }
            }
            keyManagerFactory.init(null, null);
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ex22) {
                    ex22.printStackTrace();
                }
            }
        } catch (IOException e3) {
            e = e3;
            Log.i(TAG, "got an exception ");
            e.printStackTrace();
            if (fis != null) {
                fis.close();
            }
            return keyManagerFactory.getKeyManagers();
        }
        return keyManagerFactory.getKeyManagers();
    }

    public void setCertID(int id) {
        this.mCertID = id;
    }

    public TrustManager[] fetchTrustManagerFactory() {
        Log.i(TAG, "fetchTrustManagerFactory");
        return new TrustManager[]{new CertTrustManager()};
    }
}
