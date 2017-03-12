package android.sec.enterprise;

import android.sec.enterprise.EnterpriseDeviceManager.EDMProxyServiceHelper;
import android.sec.enterprise.content.SecContentProviderURI;
import android.util.Log;
import java.util.Collections;
import java.util.List;

public class ClientCertificateManager {
    private static String TAG = SecContentProviderURI.CLIENTCERTIFICATEMANAGERPOLICY;

    public boolean isCCMPolicyEnabledForCaller() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isCCMPolicyEnabledForCaller();
            }
        } catch (Exception e) {
            Log.d(TAG, "isCCMPolicyEnabledForPackage returning default value");
        }
        return false;
    }

    public long getSlotIdForCaller(String alias) {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.getSlotIdForCaller(alias);
            }
        } catch (Exception e) {
            Log.d(TAG, "getSlotIdForPackage returning default value");
        }
        return -1;
    }

    public boolean isCCMPolicyEnabledForPackage(String packageName) {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isCCMPolicyEnabledForPackage(packageName);
            }
        } catch (Exception e) {
            Log.d(TAG, "isCCMPolicyEnabledForPackage returning default value");
        }
        return false;
    }

    public long getSlotIdForPackage(String packageName, String alias) {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.getSlotIdForPackage(packageName, alias);
            }
        } catch (Exception e) {
            Log.d(TAG, "getSlotIdForPackage returning default value");
        }
        return -1;
    }

    public List<String> getAliasesForPackage(String packageName) {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.getAliasesForPackage(packageName);
            }
        } catch (Exception e) {
            Log.d(TAG, "getAliasesForPackage returning default value");
        }
        return Collections.emptyList();
    }

    public boolean isAccessControlMethodPassword() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isAccessControlMethodPassword();
            }
        } catch (Exception e) {
            Log.d(TAG, "isAccessControlMethodPassword returning default value");
        }
        return false;
    }

    public List<String> getAliasesForWiFi() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.getAliasesForWiFi();
            }
        } catch (Exception e) {
            Log.d(TAG, "getAliasesForWiFi returning default value");
        }
        return Collections.emptyList();
    }

    public List<String> getCertificateAliasesHavingPrivateKey() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.getCertificateAliasesHavingPrivateKey();
            }
        } catch (Exception e) {
            Log.d(TAG, "getCertificateAliasesHavingPrivateKey returning default value");
        }
        return Collections.emptyList();
    }
}
