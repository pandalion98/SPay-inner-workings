package android.net.http;

import com.android.org.conscrypt.TrustManagerImpl;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import javax.net.ssl.X509TrustManager;

public class X509TrustManagerExtensions {
    final TrustManagerImpl mDelegate;

    public X509TrustManagerExtensions(X509TrustManager tm) throws IllegalArgumentException {
        if (tm instanceof TrustManagerImpl) {
            this.mDelegate = (TrustManagerImpl) tm;
        } else {
            this.mDelegate = null;
            throw new IllegalArgumentException("tm is an instance of " + tm.getClass().getName() + " which is not a supported type of X509TrustManager");
        }
    }

    public List<X509Certificate> checkServerTrusted(X509Certificate[] chain, String authType, String host) throws CertificateException {
        return this.mDelegate.checkServerTrusted(chain, authType, host);
    }

    public boolean isUserAddedCertificate(X509Certificate cert) {
        return this.mDelegate.isUserAddedCertificate(cert);
    }
}
