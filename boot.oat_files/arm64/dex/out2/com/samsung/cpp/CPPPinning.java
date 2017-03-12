package com.samsung.cpp;

import android.content.Context;
import android.os.ConditionVariable;
import android.util.Base64;
import android.util.Log;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class CPPPinning {
    private static final String BEGIN = "-----BEGIN CERTIFICATE-----";
    private static final String END = "-----END CERTIFICATE-----";
    private static final String MASTER_PINNING_SERVER_CERT = "-----BEGIN CERTIFICATE-----\nMIIDjTCCAnWgAwIBAgIJAJB9dHQSUU1ZMA0GCSqGSIb3DQEBCwUAMF0xCzAJBgNV\nBAYTAktSMRIwEAYDVQQIDAlLeXVuZ2tpZG8xDjAMBgNVBAcMBVN1d29uMRwwGgYD\nVQQKDBNTYW1zdW5nIEVsZWN0cm9uaWNzMQwwCgYDVQQLDANCMkIwHhcNMTUwNzIx\nMDkwMTA0WhcNMzUwNzE2MDkwMTA0WjBdMQswCQYDVQQGEwJLUjESMBAGA1UECAwJ\nS3l1bmdraWRvMQ4wDAYDVQQHDAVTdXdvbjEcMBoGA1UECgwTU2Ftc3VuZyBFbGVj\ndHJvbmljczEMMAoGA1UECwwDQjJCMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIB\nCgKCAQEAuA3G8qE4Uljz/+oXTC/mhYs/y0ly+Qq2RCE+cYeLA7Hpmb0PRbzjk6RV\ngXs1hbpmaau9DZxzJGif4dyM6D+b1H9RIjWSQrJ6BxoeckDyLohw8n3zHF63anA4\nmvLs4WKh44jlgrW+fAnQ8QCTSeDybvqGzYDOrfEdaQqLXjaJhIMfNa2TegQlXy+6\nT0A8kIiYza9mbohf+HfhhPajDDG92Weuf8mzi+/sW6faY/sNmQ3meD3Yxv3S0AAh\nbodO0aopJE8n9/21S4HL1B582WCCkCmBZ2HaIZ5+PXYb+iXKzfhYMTNCrCLb3Cwz\n8QGEECj7hcyqxT4Ht/dy9dryiPHTBQIDAQABo1AwTjAdBgNVHQ4EFgQUV/4SiqEp\nt7q+OTouCWhIRs8gV3gwHwYDVR0jBBgwFoAUV/4SiqEpt7q+OTouCWhIRs8gV3gw\nDAYDVR0TBAUwAwEB/zANBgkqhkiG9w0BAQsFAAOCAQEALjxOBO7i78pjCcGR8/yB\nIFk/Hb5dAU4N3Yb0FtaocIUg0KxOQSGwlUsynT1Ttrd4OGsshTaWuOGcXtvgC+UI\nSL2ygnNPsgU29ZjVGE82gnmf0dU2I5nzNXQF5XsAlYa/EY4q9YlLuSXnO3t1WvRG\nzl9Qu5QrGWre3FgzNPKkWdopsNDcd52BLS5OHyBMOqygt95kmLae/+76FYa8qsTB\n86JCAqW6QNWTgJ9t59CJ/lsgjV8IKguBRuulmwUrvz5QYKz2Yqwnd24to8lNj6qz\nWps5mc3b+fuJWDhSQznjL4JTXnPZ17rG7jVTVzv1lOZBfY+BJjVOetPEZLCaJw+X\nkQ==\n-----END CERTIFICATE-----";
    private static final String MASTER_PINNING_SERVER_URL = "https://s3-us-west-2.amazonaws.com/knoxservices/pinning-prod/service/cell/leafcert";
    public static final String PINNING_EXCEPTION_STRING = "[pinning]";
    private static final String TAG = "CPPPinning";
    private static final String TRUST_PREFERENCE = "pintrust";
    private static final String TRUST_PREFERENCE_KEY = "shelf";
    private static CPPPinning instance = null;
    private static CPPPinningResponse mResponse;
    private ConditionVariable cv = null;
    private Context mContext = null;

    class CustomTrustManager implements X509TrustManager {
        private String endpointUrl = null;
        private List<String> trustedCertificates = null;

        public CustomTrustManager(String url, List<String> listOfTrustedCertificate) {
            this.endpointUrl = url;
            this.trustedCertificates = listOfTrustedCertificate;
        }

        public X509Certificate[] getAcceptedIssuers() {
            Log.d(CPPPinning.TAG, "CustomTrustManager[endpointUrl:" + this.endpointUrl + "] --getAcceptedIssuers--");
            return new X509Certificate[0];
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            Log.d(CPPPinning.TAG, "CustomTrustManager[endpointUrl:" + this.endpointUrl + "] --check CLIENT Trusted--");
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            Log.d(CPPPinning.TAG, "CustomTrustManager[endpointUrl:" + this.endpointUrl + "] --check SERVER Trusted--");
            if (this.trustedCertificates == null || this.trustedCertificates.isEmpty()) {
                Log.d(CPPPinning.TAG, "CustomTrustManager[endpointUrl:" + this.endpointUrl + "] --check SERVER Trusted-- SERVER IS NOT " + "TRUSTED. NO CERT FOR " + this.endpointUrl);
                throw new CertificateException("[pinning]No trusted certificate for : " + this.endpointUrl);
            } else if (chain.length == 0) {
                Log.d(CPPPinning.TAG, "CustomTrustManager[endpointUrl:" + this.endpointUrl + "] --check SERVER Trusted-- ...... SERVER DOES NOT PROVIDE A CERTIFICATE CHAIN!");
                throw new IllegalArgumentException("[pinning]This server does not provide a certificate chain");
            } else {
                Log.d(CPPPinning.TAG, "WITH LEAF 509");
                boolean matchFound = false;
                PublicKey pubkeyOfLeaf = chain[0].getPublicKey();
                Log.d(CPPPinning.TAG, "@checkServerTrusted pubkeyOfLeaf : " + pubkeyOfLeaf);
                Log.d(CPPPinning.TAG, "Comparing Pub Key");
                for (String each : this.trustedCertificates) {
                    X509Certificate tempTrustedX509 = CPPPinning.pemToX509(each);
                    PublicKey tempTrustedPubKey = tempTrustedX509.getPublicKey();
                    Log.d(CPPPinning.TAG, "@checkServerTrusted tempTrustedX509 : " + tempTrustedX509);
                    Log.d(CPPPinning.TAG, "@checkServerTrusted tempTrustedPubKey : " + tempTrustedPubKey);
                    if (pubkeyOfLeaf.equals(tempTrustedPubKey)) {
                        Log.d(CPPPinning.TAG, "@checkServerTrusted matchFound");
                        matchFound = true;
                        break;
                    }
                }
                if (matchFound) {
                    try {
                        Log.d(CPPPinning.TAG, "CustomTrustManager[endpointUrl:" + this.endpointUrl + "] --check SERVER Trusted-- ...... performing " + "customary SSL/TLS checks...");
                        TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
                        tmf.init((KeyStore) null);
                        for (TrustManager trustManager : tmf.getTrustManagers()) {
                            ((X509TrustManager) trustManager).checkServerTrusted(chain, authType);
                        }
                        Log.d(CPPPinning.TAG, "CustomTrustManager[endpointUrl:" + this.endpointUrl + "] --check SERVER Trusted-- ...... SERVER IS TRUSTED");
                        return;
                    } catch (Exception e) {
                        Log.d(CPPPinning.TAG, "CustomTrustManager[endpointUrl:" + this.endpointUrl + "] --check SERVER Trusted-- exception when performing" + " customary SSL/TLS check! : " + e.getMessage());
                        Log.d(CPPPinning.TAG, "CustomTrustManager[endpointUrl:" + this.endpointUrl + "] --check SERVER Trusted-- ...... SERVER IS NOT TRUSTED! failed " + "customary SSL/TLS check!");
                        throw new CertificateException("[pinning]Server certificate does not pass SSL/TLS check");
                    }
                }
                Log.d(CPPPinning.TAG, "CustomTrustManager[endpointUrl:" + this.endpointUrl + "] --check SERVER Trusted-- ...... SERVER IS NOT TRUSTED!");
                throw new CertificateException("[pinning]This server does not have the correct certificate");
            }
        }
    }

    public static CPPPinning getInstance(Context ctx) {
        Log.d(TAG, "@getInstance");
        if (instance == null) {
            instance = new CPPPinning(ctx);
        }
        return instance;
    }

    private CPPPinning(Context ctx) {
        Log.d(TAG, "@Pinning constructor");
        setDefaultCerts();
        this.mContext = ctx;
    }

    public TrustManager[] getTrustManagers(String endpointUrl) {
        Log.d(TAG, "@getTrustManagers");
        CPPPinningResponse pinnedCertsStore = readPinnedCerts();
        List<String> listOfCertsTrusted = null;
        if (endpointUrl.startsWith("https://")) {
            endpointUrl = endpointUrl.replace("https://", "");
        } else if (endpointUrl.startsWith("http://")) {
            endpointUrl = endpointUrl.replace("http://", "");
        }
        endpointUrl = endpointUrl.split("/")[0] + ":443";
        Log.d(TAG, "@getTrustManagers - " + endpointUrl);
        TrustManager[] result = new TrustManager[1];
        if (pinnedCertsStore != null) {
            for (ResponseData eachEntry : pinnedCertsStore.getData()) {
                if (eachEntry.URL.contains(endpointUrl)) {
                    listOfCertsTrusted = new ArrayList(eachEntry.CA);
                    Log.d(TAG, "@getTrustManagers listOfCertsTrusted : " + listOfCertsTrusted);
                    break;
                }
            }
        }
        Log.d(TAG, "@getTrustManagers - pinned cert store has not been formed yet!");
        if (listOfCertsTrusted == null) {
            Log.d(TAG, "@getTrustManagers - no pinned certificate found for : " + endpointUrl);
            Log.d(TAG, "@getTrustManagers - strict mode applied for : " + endpointUrl);
            result[0] = new CustomTrustManager(endpointUrl, null);
        } else {
            Log.d(TAG, "@getTrustManagers - found pinned certificates for : " + endpointUrl);
            result[0] = new CustomTrustManager(endpointUrl, listOfCertsTrusted);
        }
        Log.d(TAG, "@getTrustManagers - done");
        return result;
    }

    private CPPPinningResponse readPinnedCerts() {
        Log.d(TAG, "@readPinnedCerts");
        if (mResponse != null) {
            Log.d(TAG, "@readPinnedCerts - done.");
            List<ResponseData> dataList = mResponse.getData();
            if (!dataList.isEmpty()) {
                for (ResponseData each : dataList) {
                    for (String eachCA : each.CA) {
                        Log.d(TAG, "@readPinnedCerts CA : " + eachCA);
                    }
                    for (String eachUrl : each.URL) {
                        Log.d(TAG, "@readPinnedCerts URL : " + eachUrl);
                    }
                }
            }
            return mResponse;
        }
        Log.d(TAG, "@readPinnedCerts - done. Null.");
        return null;
    }

    public void setDefaultCerts() {
        mResponse = new CPPPinningResponse();
        ResponseData cert1 = new ResponseData();
        ResponseData cert2 = new ResponseData();
        cert1.CA = new ArrayList();
        cert1.URL = new ArrayList();
        cert2.CA = new ArrayList();
        cert2.URL = new ArrayList();
        cert1.CA.add("-----BEGIN CERTIFICATE-----MIIEzTCCA7WgAwIBAgIQPw43HyOH/HxLE6TVs4IPyTANBgkqhkiG9w0BAQsFADBBMQswCQYDVQQGEwJVUzEVMBMGA1UEChMMdGhhd3RlLCBJbmMuMRswGQYDVQQDExJ0aGF3dGUgU1NMIENBIC0gRzIwHhcNMTUxMDEzMDAwMDAwWhcNMTcwMTExMjM1OTU5WjCBhTELMAkGA1UEBhMCS1IxETAPBgNVBAgTCEd5ZW9uZ2dpMQ4wDAYDVQQHFAVTdXdvbjEkMCIGA1UEChQbU0FNU1VORyBFTEVDVFJPTklDUyBDTywgTFREMRYwFAYDVQQLFA1CMkIgUiZEIEdyb3VwMRUwEwYDVQQDFAwqLnNlY2IyYi5jb20wggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQDMWwc2IA5gKJbDWo/hu1+Xa3vvz4UJijh/8pRQ7HLxhjs6IhO8iI1b36mvCcwV1fi2OHxpxDkodeaVUVt5PohL3dflSf98Zd5af0TtRhY0wwCam1Tz712dtnYWfv29rsg7SBMw4fMXvqajxPZkJl1354xwERfu4kgg0YT5MpaPD0u5PWJ2g/Ghm0zGpITewx+1994CJFyo73s3J+L1Ji8conqstfAzjZ/gmxzhoc3yZjk/+rqFRyVBIaLmNDdrUSsxoPEz+KXjKtTh9dsLkHYddfDpKWlmPPvjxnMRqDlS0nQ/9HF/5kdpj5NWT2O8bZNvIYaahQJRNLp1CtA9ZHpLAgMBAAGjggF6MIIBdjAjBgNVHREEHDAaggwqLnNlY2IyYi5jb22CCnNlY2IyYi5jb20wCQYDVR0TBAIwADBuBgNVHSAEZzBlMGMGBmeBDAECAjBZMCYGCCsGAQUFBwIBFhpodHRwczovL3d3dy50aGF3dGUuY29tL2NwczAvBggrBgEFBQcCAjAjDCFodHRwczovL3d3dy50aGF3dGUuY29tL3JlcG9zaXRvcnkwDgYDVR0PAQH/BAQDAgWgMB8GA1UdIwQYMBaAFMJPSFf80U+awF04fQ4F29kutVJgMCsGA1UdHwQkMCIwIKAeoByGGmh0dHA6Ly90ai5zeW1jYi5jb20vdGouY3JsMB0GA1UdJQQWMBQGCCsGAQUFBwMBBggrBgEFBQcDAjBXBggrBgEFBQcBAQRLMEkwHwYIKwYBBQUHMAGGE2h0dHA6Ly90ai5zeW1jZC5jb20wJgYIKwYBBQUHMAKGGmh0dHA6Ly90ai5zeW1jYi5jb20vdGouY3J0MA0GCSqGSIb3DQEBCwUAA4IBAQAUvTU+Hv62Vmsi7H1eAz/dsvbkydCGybrs4DM8DLnWSvfT9/CNqTc7fWDahlwxf4WtDfq06ow0b/bR+EiyFdkLh2jcwHPqr6xZvwWoWgu/4KGDB7n1qVIIfx43AX2L30K8DxDdrA75oZ8sxoz0mGdqhyQNXihJmhDUXd4wIeFtAsavG+l6t57IFMfp1cpF+UB4d8fvOKCn8av4d2YsdyN8og1VKQnSvwolehDesl9GeSfifjziLjg25QnE29PYC/3+6YzJ+a9tF0sVcfJRDHzGnSVRn+iD7djIenXdlgm0Kn4p4PjLvrNQs0gbcvbF9cdjC84P/fsSQx6XtMA4jZYk-----END CERTIFICATE-----");
        cert1.URL.add("prod-celltw.secb2b.com:443");
        cert1.URL.add("eu-prod-celltw.secb2b.com:443");
        cert1.URL.add("us-prod-celltw.secb2b.com:443");
        cert1.URL.add("gslb.secb2b.com:443");
        cert2.CA.add("-----BEGIN CERTIFICATE-----MIIEyzCCA7OgAwIBAgIQDtgbCKB1PO++vF1RYTaWQzANBgkqhkiG9w0BAQsFADBBMQswCQYDVQQGEwJVUzEVMBMGA1UEChMMdGhhd3RlLCBJbmMuMRswGQYDVQQDExJ0aGF3dGUgU1NMIENBIC0gRzIwHhcNMTUwNTA1MDAwMDAwWhcNMTcwNzAzMjM1OTU5WjB+MQswCQYDVQQGEwJDTjEPMA0GA1UECAwG5YyX5LqsMQ8wDQYDVQQHDAbljJfkuqwxMzAxBgNVBAoMKuWMl+S6rOS4ieaYn+mAmuS/oeaKgOacr+eglOeptuaciemZkOWFrOWPuDEYMBYGA1UEAwwPKi5zZWNiMmIuY29tLmNuMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoz0bUIlWLyx7FdaJjd+1SjsX4ySfQGG/PYM43Pf+9xETudttzK5DunnfxPo89r+oCWAfuWho6p9VbLWNiSvU8vivO0cOF1QiL9qGlT2q7p9NDrIrjPe3Ms9ZA5W08vIb+9MCJdVgZTt3FGRb4t2YKjhbB2X+DusVzNRaSWqZQKDA7ALrk0ZahlVTuF7hIK7TO103j1iQbymV7oUt1ohQ2tklA1zlOR/wLSdrsLdM1pQfr7r64gUUiRIZ7T7nYTeYT2c4DktjJxDl4UM3QSG8aA6t/J/zgMNS1W9u4ri6x1MUC+VpYVAThS8Kldl2Y3sqTe8sHMu8r/BHnscXdwIebwIDAQABo4IBgDCCAXwwKQYDVR0RBCIwIIIPKi5zZWNiMmIuY29tLmNugg1zZWNiMmIuY29tLmNuMAkGA1UdEwQCMAAwbgYDVR0gBGcwZTBjBgZngQwBAgIwWTAmBggrBgEFBQcCARYaaHR0cHM6Ly93d3cudGhhd3RlLmNvbS9jcHMwLwYIKwYBBQUHAgIwIwwhaHR0cHM6Ly93d3cudGhhd3RlLmNvbS9yZXBvc2l0b3J5MA4GA1UdDwEB/wQEAwIFoDAfBgNVHSMEGDAWgBTCT0hX/NFPmsBdOH0OBdvZLrVSYDArBgNVHR8EJDAiMCCgHqAchhpodHRwOi8vdGouc3ltY2IuY29tL3RqLmNybDAdBgNVHSUEFjAUBggrBgEFBQcDAQYIKwYBBQUHAwIwVwYIKwYBBQUHAQEESzBJMB8GCCsGAQUFBzABhhNodHRwOi8vdGouc3ltY2QuY29tMCYGCCsGAQUFBzAChhpodHRwOi8vdGouc3ltY2IuY29tL3RqLmNydDANBgkqhkiG9w0BAQsFAAOCAQEAnG/ZLTMspoflf1aWJ1FtS6YQKtZDScYeR5Z4klQkPQafJBv0KNWGhJtQLbP/JwF89wlbU1UzYzVoWRUXDREINqxWngRH7fHjejep9GdkelsB6Zp4FlswFnH0pTiOzw+36o6h5I5swTbUcQ7x2jdG3k61bSkFDRHt0WZBlx0OARi1t1n0T/ZHcgfOSQ0SUveaTfA77RqnXimRX8MzOZtO+U+u/LWWyrbm0drrGMSODRFj1o0bUJOqUKUOR6qGBoAon9vfLBfXOlTSfA1nXT9JmTPPkYHhv4V5ph2LamZ/kz1jwQ/mP2KXaPsxe78nLKEsXAGdfs2Hho5svhqhiI/nwQ==-----END CERTIFICATE-----");
        cert2.URL.add("cn-prod-celltw.secb2b.com.cn:443");
        cert2.URL.add("china-gslb.secb2b.com.cn:443");
        mResponse.data.add(cert1);
        mResponse.data.add(cert2);
    }

    public static X509Certificate pemToX509(String pemCert) {
        Log.d(TAG, "@pemToX509");
        X509Certificate cert = null;
        if (pemCert == null) {
            Log.e(TAG, "@pemToX509 - Cert String is Null!");
            return null;
        }
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            byte[] byteArray = readPemBytes(pemCert);
            if (byteArray != null) {
                cert = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(byteArray));
            }
            return cert;
        } catch (Throwable e) {
            Log.d(TAG, "@pemToX509 - Exception: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private static byte[] readPemBytes(String str) throws IOException {
        String line = new BufferedReader(new StringReader(str)).readLine();
        if (line == null) {
            return null;
        }
        if (line.startsWith(BEGIN)) {
            line = line.substring(BEGIN.length());
        }
        if (line.endsWith(END) || line.contains(END)) {
            line = line.substring(0, line.indexOf(END));
        }
        return Base64.decode(line.trim(), 0);
    }
}
