package com.absolute.android.sslutil;

import com.absolute.android.logutil.LogUtil;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;

final class b implements X509TrustManager {
    final /* synthetic */ SslUtil a;

    public b(SslUtil sslUtil) {
        this.a = sslUtil;
        LogUtil.get().logMessage(3, "EmptyTrustManager created...");
    }

    public final void checkClientTrusted(X509Certificate[] x509CertificateArr, String str) {
    }

    public final void checkServerTrusted(X509Certificate[] x509CertificateArr, String str) {
    }

    public final X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }
}
