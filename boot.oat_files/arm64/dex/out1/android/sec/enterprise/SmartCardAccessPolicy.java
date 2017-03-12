package android.sec.enterprise;

import android.sec.enterprise.EnterpriseDeviceManager.EDMProxyServiceHelper;
import android.util.Log;

public class SmartCardAccessPolicy {
    private static String TAG = "SmartCardAccessPolicy";

    public boolean isPackageWhitelistedFromBTSecureAccess(String package_name) {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isPackageWhitelistedFromBTSecureAccess(package_name);
            }
        } catch (Exception e) {
            Log.d(TAG, "isPackageWhitelistedFromBTSecureAccess returning default value");
        }
        return true;
    }

    public boolean isPackageWhitelistedFromBTSecureAccessUid(int calling_Uid) {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isPackageWhitelistedFromBTSecureAccessUid(calling_Uid);
            }
        } catch (Exception e) {
            Log.d(TAG, "isPackageWhitelistedFromBTSecureAccessUid returning default value");
        }
        return true;
    }
}
