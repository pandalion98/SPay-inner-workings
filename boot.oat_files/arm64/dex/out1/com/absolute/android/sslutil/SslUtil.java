package com.absolute.android.sslutil;

import android.content.Context;
import com.absolute.android.logutil.LogUtil;
import com.absolute.android.utils.ExceptionUtil;
import java.security.SecureRandom;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class SslUtil {
    private static X509TrustManager a;

    public SslUtil(Context context, String str) {
        LogUtil.init(context, str);
        try {
            if (a == null) {
                TrustManagerFactory instance = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                instance.init(null);
                for (TrustManager trustManager : instance.getTrustManagers()) {
                    if (trustManager instanceof X509TrustManager) {
                        a = (X509TrustManager) trustManager;
                        return;
                    }
                }
            }
        } catch (Throwable e) {
            LogUtil.get().logMessage(6, "Exception setting up defaultSSLKeyStore or defaultTrustManager : " + ExceptionUtil.getExceptionMessage(e));
        }
    }

    public void trustOurHost(String str, String str2, boolean z) {
        try {
            SSLContext instance = SSLContext.getInstance("TLS");
            if (z) {
                instance.init(null, new TrustManager[]{new b(this)}, new SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(instance.getSocketFactory());
            } else if (a != null) {
                instance.init(null, new TrustManager[]{new a(this, str, str2)}, new SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(instance.getSocketFactory());
            } else {
                LogUtil.get().logMessage(6, "TrustOurHost: defaultTrustManager null. Cant perform custom cert checking.");
            }
        } catch (Throwable e) {
            LogUtil.get().logMessage(6, "trustOurHost: " + ExceptionUtil.getExceptionMessage(e));
        }
    }
}
