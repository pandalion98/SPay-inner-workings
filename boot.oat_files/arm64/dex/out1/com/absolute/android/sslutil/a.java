package com.absolute.android.sslutil;

import android.security.keystore.KeyProperties;
import com.absolute.android.crypt.HexUtilities;
import com.absolute.android.logutil.LogUtil;
import com.absolute.android.utils.ExceptionUtil;
import java.security.MessageDigest;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;

final class a implements X509TrustManager {
    final /* synthetic */ SslUtil a;
    private String b;
    private String c;

    public a(SslUtil sslUtil, String str, String str2) {
        this.a = sslUtil;
        this.b = str;
        this.c = str2;
        LogUtil.get().logMessage(3, "ABTTrustManager created... expectedCertSubjectName=" + str + " hostSPKIHash=" + str2);
    }

    public final void checkClientTrusted(X509Certificate[] x509CertificateArr, String str) {
        SslUtil.a.checkClientTrusted(x509CertificateArr, str);
    }

    public final void checkServerTrusted(X509Certificate[] x509CertificateArr, String str) {
        int i;
        boolean z;
        Exception e;
        X509Certificate[] a;
        X509Certificate[] acceptedIssuers;
        X509Certificate x509Certificate;
        int i2;
        Object obj;
        int i3;
        Object obj2 = 1;
        X509Certificate[] x509CertificateArr2 = new X509Certificate[x509CertificateArr.length];
        for (i = 0; i < x509CertificateArr.length; i++) {
            x509CertificateArr2[i] = x509CertificateArr[i];
        }
        try {
            X509Certificate x509Certificate2 = x509CertificateArr[0];
            String name = x509Certificate2.getSubjectDN().getName();
            LogUtil.get().logMessage(3, "checkServerTrusted cert subjectName = " + name + ", expected:  " + this.b);
            if (name.contains(this.b)) {
                LogUtil.get().logMessage(3, "checkServerTrusted: Got expected certificate subject name");
                if (this.c != null) {
                    byte[] encoded = x509Certificate2.getPublicKey().getEncoded();
                    if (encoded == null || HexUtilities.EncodeBytesAsHexString(MessageDigest.getInstance(KeyProperties.DIGEST_SHA256).digest(encoded)).compareToIgnoreCase(this.c) != 0) {
                        z = false;
                    } else {
                        LogUtil.get().logMessage(3, "checkServerTrusted: Cert Pinning worked, call checkServerTrusted");
                        try {
                            SslUtil.a.checkServerTrusted(x509CertificateArr, str);
                            z = true;
                        } catch (Exception e2) {
                            e = e2;
                            z = true;
                        }
                    }
                    try {
                        LogUtil.get().logMessage(3, "checkServerTrusted: certPinningSuccess=" + z);
                        if (!z) {
                            throw new CertificateException("CertPinning-bad value: Certificate checking failed");
                        }
                        return;
                    } catch (Exception e3) {
                        e = e3;
                        LogUtil.get().logMessage(3, "m_defaultTrustManager.checkServerTrusted exception - do our own checking");
                        if (z) {
                            a = a(x509CertificateArr2);
                            acceptedIssuers = getAcceptedIssuers();
                            for (i = 0; i < a.length; i++) {
                                x509Certificate = a[i];
                                for (i2 = 0; i2 < acceptedIssuers.length; i2++) {
                                    if (acceptedIssuers[i2].getSubjectX500Principal().equals(x509Certificate.getIssuerX500Principal())) {
                                        try {
                                            x509Certificate.verify(acceptedIssuers[i2].getPublicKey());
                                            obj = 1;
                                            break;
                                        } catch (Exception e4) {
                                        }
                                    }
                                }
                                obj = null;
                                if (obj != null) {
                                    try {
                                        if (i != a.length - 1) {
                                            obj = null;
                                            i3 = i;
                                            while (i3 < a.length - 1) {
                                                try {
                                                    a[i3 + 1].verify(a[i3].getPublicKey());
                                                    i3++;
                                                    i2 = 1;
                                                } catch (Exception e5) {
                                                    obj = null;
                                                }
                                            }
                                        } else {
                                            obj = 1;
                                        }
                                        if (obj != null) {
                                            break;
                                        }
                                    } catch (Exception e6) {
                                        throw new CertificateException(e6.getMessage());
                                    }
                                }
                            }
                            obj2 = null;
                            if (obj2 == null) {
                                throw new CertificateException("CheckServerTrusted: Root cert is not trusted");
                            }
                            return;
                        }
                        throw new CertificateException(e6.getMessage());
                    }
                }
                SslUtil.a.checkServerTrusted(x509CertificateArr, str);
                return;
            }
            throw new CertificateException("Bad subject value: Certificate checking failed");
        } catch (Exception e7) {
            e6 = e7;
            z = false;
            LogUtil.get().logMessage(3, "m_defaultTrustManager.checkServerTrusted exception - do our own checking");
            if (z) {
                a = a(x509CertificateArr2);
                acceptedIssuers = getAcceptedIssuers();
                for (i = 0; i < a.length; i++) {
                    x509Certificate = a[i];
                    for (i2 = 0; i2 < acceptedIssuers.length; i2++) {
                        if (acceptedIssuers[i2].getSubjectX500Principal().equals(x509Certificate.getIssuerX500Principal())) {
                            x509Certificate.verify(acceptedIssuers[i2].getPublicKey());
                            obj = 1;
                            break;
                        }
                    }
                    obj = null;
                    if (obj != null) {
                        if (i != a.length - 1) {
                            obj = 1;
                        } else {
                            obj = null;
                            i3 = i;
                            while (i3 < a.length - 1) {
                                a[i3 + 1].verify(a[i3].getPublicKey());
                                i3++;
                                i2 = 1;
                            }
                        }
                        if (obj != null) {
                            break;
                        }
                    }
                }
                obj2 = null;
                if (obj2 == null) {
                    throw new CertificateException("CheckServerTrusted: Root cert is not trusted");
                }
                return;
            }
            throw new CertificateException(e6.getMessage());
        }
    }

    public final X509Certificate[] getAcceptedIssuers() {
        return SslUtil.a.getAcceptedIssuers();
    }

    private static X509Certificate[] a(X509Certificate[] x509CertificateArr) {
        Object obj = 1;
        if (x509CertificateArr != null) {
            try {
                int i;
                X509Certificate[] x509CertificateArr2 = new X509Certificate[x509CertificateArr.length];
                for (i = 0; i < x509CertificateArr.length; i++) {
                    x509CertificateArr2[i] = x509CertificateArr[i];
                }
                if (x509CertificateArr.length > 1) {
                    int i2 = 0;
                    while (i2 < x509CertificateArr.length) {
                        i = 0;
                        while (i < x509CertificateArr.length && (i2 == i || !x509CertificateArr[i2].getIssuerDN().equals(x509CertificateArr[i].getSubjectDN()))) {
                            i++;
                        }
                        if (i == x509CertificateArr.length) {
                            x509CertificateArr2[0] = x509CertificateArr[i2];
                            break;
                        }
                        i2++;
                    }
                    obj = null;
                    if (obj != null) {
                        for (i = 0; i < x509CertificateArr2.length - 1; i++) {
                            for (int i3 = 0; i3 < x509CertificateArr.length; i3++) {
                                if (x509CertificateArr2[i].getSubjectDN().equals(x509CertificateArr[i3].getIssuerDN())) {
                                    x509CertificateArr2[i + 1] = x509CertificateArr[i3];
                                    break;
                                }
                            }
                        }
                        return x509CertificateArr2;
                    }
                }
                x509CertificateArr = x509CertificateArr2;
            } catch (Throwable e) {
                LogUtil.get().logMessage(6, "Exception on reorderCerts: " + ExceptionUtil.getExceptionMessage(e));
                return null;
            }
        }
        x509CertificateArr = null;
        return x509CertificateArr;
    }
}
