package android.sec.enterprise;

import android.net.wifi.WifiConfiguration;
import android.sec.enterprise.EnterpriseDeviceManager.EDMProxyServiceHelper;
import android.sec.enterprise.content.SecContentProviderURI;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class WifiPolicy {
    public static final String ACTION_ENABLE_NETWORK = "edm.intent.action.enable";
    public static final String ENGINE_ID_SECPKCS11 = "secpkcs11";
    public static final String ENGINE_ID_UCMENGINE = "ucsengine";
    public static final int SECURITY_LEVEL_EAP_AKA = 25;
    public static final int SECURITY_LEVEL_EAP_AKA_CCKM = 27;
    public static final int SECURITY_LEVEL_EAP_AKA_FT = 26;
    public static final int SECURITY_LEVEL_EAP_AKA_PRIME = 28;
    public static final int SECURITY_LEVEL_EAP_AKA_PRIME_CCKM = 30;
    public static final int SECURITY_LEVEL_EAP_AKA_PRIME_FT = 29;
    public static final int SECURITY_LEVEL_EAP_FAST = 4;
    public static final int SECURITY_LEVEL_EAP_FAST_CCKM = 18;
    public static final int SECURITY_LEVEL_EAP_FAST_FT = 17;
    public static final int SECURITY_LEVEL_EAP_LEAP = 3;
    public static final int SECURITY_LEVEL_EAP_LEAP_CCKM = 16;
    public static final int SECURITY_LEVEL_EAP_LEAP_FT = 15;
    public static final int SECURITY_LEVEL_EAP_PEAP = 5;
    public static final int SECURITY_LEVEL_EAP_PEAP_CCKM = 10;
    public static final int SECURITY_LEVEL_EAP_PEAP_FT = 9;
    public static final int SECURITY_LEVEL_EAP_PWD = 19;
    public static final int SECURITY_LEVEL_EAP_PWD_CCKM = 21;
    public static final int SECURITY_LEVEL_EAP_PWD_FT = 20;
    public static final int SECURITY_LEVEL_EAP_SIM = 22;
    public static final int SECURITY_LEVEL_EAP_SIM_CCKM = 24;
    public static final int SECURITY_LEVEL_EAP_SIM_FT = 23;
    public static final int SECURITY_LEVEL_EAP_TLS = 7;
    public static final int SECURITY_LEVEL_EAP_TLS_CCKM = 14;
    public static final int SECURITY_LEVEL_EAP_TLS_FT = 13;
    public static final int SECURITY_LEVEL_EAP_TTLS = 6;
    public static final int SECURITY_LEVEL_EAP_TTLS_CCKM = 12;
    public static final int SECURITY_LEVEL_EAP_TTLS_FT = 11;
    public static final int SECURITY_LEVEL_FT_PSK = 8;
    public static final int SECURITY_LEVEL_HIGHEST = 30;
    public static final int SECURITY_LEVEL_OPEN = 0;
    public static final int SECURITY_LEVEL_UNKNOWN = -1;
    public static final int SECURITY_LEVEL_WEP = 1;
    public static final int SECURITY_LEVEL_WPA = 2;
    private static String TAG = SecContentProviderURI.WIFIPOLICY;

    public List<String> getNetworkSSIDList() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.getNetworkSSIDList();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-getNetworkSSID returning default value");
        }
        return new ArrayList(0);
    }

    public List<String> getBlockedNetworks() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.getBlockedNetworks();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-getBlockedNetworks returning default value");
        }
        return new ArrayList(0);
    }

    public boolean getAllowUserPolicyChanges() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.getAllowUserPolicyChanges();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-getAllowUserPolicyChanges returning default value");
        }
        return true;
    }

    public boolean getAllowUserProfiles(boolean showMsg, int userId) {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.getAllowUserProfiles(showMsg, userId);
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-getAllowUserProfiles returning default value");
        }
        return true;
    }

    public boolean getPromptCredentialsEnabled() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.getPromptCredentialsEnabled();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-getPromptCredentialsEnabled returning default value");
        }
        return true;
    }

    public int getMinimumRequiredSecurity() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.getMinimumRequiredSecurity();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-getMinimumRequiredSecurity returning default value");
        }
        return 0;
    }

    public void edmAddOrUpdate(WifiConfiguration config, String netSSID) {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                lService.edmAddOrUpdate(config, netSSID);
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-edmAddOrUpdate returning default value");
        }
    }

    public boolean removeNetworkConfiguration(String ssid) {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.removeNetworkConfiguration(ssid);
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-removeNetworkConfiguration returning default value");
        }
        return false;
    }

    public boolean isWifiStateChangeAllowed() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isWifiStateChangeAllowed();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-isWifiStateChangeAllowed returning default value");
        }
        return true;
    }

    public boolean getAutomaticConnectionToWifi() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.getAutomaticConnectionToWifi();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-getAutomaticConnectionToWifi returning default value");
        }
        return true;
    }

    public List<String> getWifiSsidRestrictionList(int type) {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.getWifiSsidRestrictionList(type);
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-getWifiSsidRestrictionList returning default value");
        }
        return new ArrayList();
    }

    public static int getLinkSecurity(WifiConfiguration enterpriseNetwork) {
        if (enterpriseNetwork != null) {
            if (enterpriseNetwork.allowedKeyManagement.get(0)) {
                if (enterpriseNetwork.allowedAuthAlgorithms.get(0)) {
                    return 1;
                }
                return 0;
            } else if (enterpriseNetwork.allowedKeyManagement.get(1)) {
                return 2;
            } else {
                if (enterpriseNetwork.allowedKeyManagement.get(8)) {
                    return 8;
                }
                if (enterpriseNetwork.allowedKeyManagement.get(2)) {
                    int eap = enterpriseNetwork.enterpriseConfig.getEapMethod();
                    if (eap == 0) {
                        return 5;
                    }
                    if (eap == 2) {
                        return 6;
                    }
                    if (eap == 1) {
                        return 7;
                    }
                    if (eap == 8) {
                        return 3;
                    }
                    if (eap == 7) {
                        return 4;
                    }
                    if (eap == 3) {
                        return 19;
                    }
                    if (eap == 5) {
                        return 25;
                    }
                    if (eap == 6) {
                        return 28;
                    }
                    if (eap == 4) {
                        return 22;
                    }
                } else if (enterpriseNetwork.allowedKeyManagement.get(9)) {
                    int eapType = enterpriseNetwork.enterpriseConfig.getEapMethod();
                    if (eapType == 0) {
                        return 9;
                    }
                    if (eapType == 2) {
                        return 11;
                    }
                    if (eapType == 1) {
                        return 13;
                    }
                    if (eapType == 8) {
                        return 15;
                    }
                    if (eapType == 7) {
                        return 17;
                    }
                    if (eapType == 3) {
                        return 20;
                    }
                    if (eapType == 5) {
                        return 26;
                    }
                    if (eapType == 6) {
                        return 29;
                    }
                    if (eapType == 4) {
                        return 23;
                    }
                } else if (enterpriseNetwork.allowedKeyManagement.get(7)) {
                    int eapMethod = enterpriseNetwork.enterpriseConfig.getEapMethod();
                    if (eapMethod == 0) {
                        return 10;
                    }
                    if (eapMethod == 2) {
                        return 12;
                    }
                    if (eapMethod == 1) {
                        return 14;
                    }
                    if (eapMethod == 8) {
                        return 16;
                    }
                    if (eapMethod == 7) {
                        return 18;
                    }
                    if (eapMethod == 3) {
                        return 21;
                    }
                    if (eapMethod == 5) {
                        return 27;
                    }
                    if (eapMethod == 6) {
                        return 30;
                    }
                    if (eapMethod == 4) {
                        return 24;
                    }
                }
            }
        }
        return -1;
    }

    public static int getSecurityLevel(int security) {
        switch (security) {
            case 0:
                return 1;
            case 1:
                return 2;
            case 2:
            case 8:
                return 3;
            case 3:
            case 15:
            case 16:
            case 19:
            case 20:
            case 21:
                return 4;
            case 4:
            case 5:
            case 9:
            case 10:
            case 17:
            case 18:
                return 5;
            case 6:
            case 7:
            case 11:
            case 12:
            case 13:
            case 14:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
                return 6;
            default:
                return 1;
        }
    }
}
