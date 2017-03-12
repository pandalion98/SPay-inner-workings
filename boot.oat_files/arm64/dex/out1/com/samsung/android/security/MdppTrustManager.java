package com.samsung.android.security;

import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.content.Context;
import com.android.internal.R;
import com.android.org.conscrypt.TrustManagerImpl;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class MdppTrustManager implements X509TrustManager {
    private static final String NOTIFICATION_TEXT = " An error has been encountered while validating server's certificate chain";
    private static final String NOTIFICATION_TITLE = "Certificate chain error";
    private Context context;
    private TrustManagerImpl trustManager = null;

    public MdppTrustManager(Context context, KeyStore keyStore) {
        this.context = context;
        try {
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(keyStore);
            for (TrustManager manager : tmf.getTrustManagers()) {
                if (manager instanceof TrustManagerImpl) {
                    this.trustManager = (TrustManagerImpl) manager;
                    break;
                }
            }
            if (this.trustManager == null) {
                throw new IllegalArgumentException("Can't find TrustManagerImpl instance");
            }
        } catch (KeyStoreException kse) {
            throw new IllegalArgumentException(kse.getMessage());
        } catch (NoSuchAlgorithmException nsae) {
            throw new IllegalArgumentException(nsae.getMessage());
        }
    }

    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        this.trustManager.checkClientTrusted(chain, authType);
    }

    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        try {
            this.trustManager.checkServerTrusted(chain, authType);
        } catch (CertificateException exceprion) {
            showNotification(exceprion);
            throw exceprion;
        }
    }

    public X509Certificate[] getAcceptedIssuers() {
        return this.trustManager.getAcceptedIssuers();
    }

    private void showNotification(Exception e) {
        Builder builder = new Builder(this.context);
        builder.setSmallIcon(R.drawable.stat_sys_warning).setContentTitle(NOTIFICATION_TITLE).setContentText(NOTIFICATION_TEXT).setAutoCancel(true);
        ((NotificationManager) this.context.getSystemService("notification")).notify(1, builder.build());
    }
}
