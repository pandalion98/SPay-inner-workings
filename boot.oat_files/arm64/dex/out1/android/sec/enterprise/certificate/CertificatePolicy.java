package android.sec.enterprise.certificate;

import android.os.RemoteException;
import android.sec.enterprise.EnterpriseDeviceManager.EDMProxyServiceHelper;
import android.sec.enterprise.IEDMProxy;
import android.sec.enterprise.content.SecContentProviderURI;

public class CertificatePolicy {
    public static final String BROWSER_MODULE = "browser_module";
    public static final int CERTIFICATE_VALIDATED_SUCCESSFULLY = -1;
    public static final int CERT_ERROR_REVOKED = -206;
    public static final int CERT_ERROR_UNABLE_TO_CHECK_REVOCATION = -205;
    public static final String MSG_ADDITIONAL_CHECKER = "Additional certificate path checker failed.";
    public static final String MSG_CRL_DIST_COULD_NOT_BE_READ = "CRL distribution point extension could not be read";
    public static final String MSG_CRL_NOT_VALID = "No valid CRL found.";
    public static final String MSG_DIST_POINT_COULD_NOT_BE_READ = "Distribution points could not be read.";
    public static final String MSG_EXPIRED_CERT = ", expiration time";
    public static final String MSG_IS_REVOKED_CERT = "is revoked";
    public static final String MSG_NOT_YET_VALID_CERT = ", validation time";
    public static final String MSG_NO_ADDITIONAL_CRL_DECODED = "No additional CRL locations could be decoded from CRL distribution point extension.";
    public static final String MSG_REVOKED_CERT = "Certificate revocation after";
    public static final String MSG_UNABLE_CHECK_OCSP_STATUS = "OCSP check failed!";
    public static final String MSG_UNABLE_CHECK_REVOCATION_STATUS = "Certificate status could not be determined.";
    public static final String MSG_UNABLE_GET_CRL = "Unable to get CRL for certificate";
    public static final String PACKAGE_MODULE = "package_manager_module";
    public static final int SBROWSER_VERIFY_NO_TRUSTED_ROOT = 2;
    public static final int SBROWSER_VERIFY_REVOKED = 1;
    public static final int SBROWSER_VERIFY_UNABLE_TO_CHECK_REVOCATION = 0;
    private static String TAG = SecContentProviderURI.CERTIFICATEPOLICY;
    public static final String WIFI_MODULE = "wifi_module";

    public void notifyCertificateFailure(String module, String msg, boolean showMsg) {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                lService.notifyCertificateFailure(module, msg, showMsg);
            }
        } catch (Exception e) {
        }
    }

    public void notifyCertificateFailureAsUser(String module, String msg, boolean showMsg, int userId) {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                lService.notifyCertificateFailureAsUser(module, msg, showMsg, userId);
            }
        } catch (Exception e) {
        }
    }

    public boolean isCaCertificateTrustedAsUser(byte[] certBytes, boolean showMsg, int userId) {
        return isCaCertificateTrustedAsUser(certBytes, showMsg, true, userId);
    }

    public boolean isCaCertificateTrustedAsUser(byte[] certBytes, boolean showMsg, boolean checkTrusted, int userId) {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isCaCertificateTrustedAsUser(certBytes, showMsg, checkTrusted, userId);
            }
        } catch (RemoteException e) {
        }
        return true;
    }

    public boolean isRevocationCheckEnabled() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isRevocationCheckEnabled();
            }
        } catch (Exception e) {
        }
        return false;
    }

    public boolean isOcspCheckEnabled() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isOcspCheckEnabled();
            }
        } catch (Exception e) {
        }
        return false;
    }

    public void notifyCertificateRemovedAsUser(String subject, int userId) {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                lService.notifyCertificateRemovedAsUser(subject, userId);
            }
        } catch (Exception e) {
        }
    }

    public boolean isUserRemoveCertificatesAllowedAsUser(int userId) {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isUserRemoveCertificatesAllowedAsUser(userId);
            }
        } catch (Exception e) {
        }
        return true;
    }

    public int validateCertificateAtInstallAsUser(byte[] certBytes, int userId) {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.validateCertificateAtInstallAsUser(certBytes, userId);
            }
        } catch (Exception e) {
        }
        return -1;
    }
}
