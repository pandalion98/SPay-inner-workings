package android.sec.enterprise;

import android.sec.enterprise.EnterpriseDeviceManager.EDMProxyServiceHelper;
import android.util.Log;

public class TimaKeystore {
    private static String TAG = "TimaKeystore";

    public boolean isTimaKeystoreEnabled() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isTimaKeystoreEnabled();
            }
        } catch (Exception e) {
            Log.d(TAG, "isTimaKeystoreEnabled returning default value");
        }
        return false;
    }

    public boolean isTimaKeystoreEnabledForPackage(String packageName) {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isTimaKeystoreEnabledForPackage(packageName);
            }
        } catch (Exception e) {
            Log.d(TAG, "isTimaKeystoreEnabledForPackage returning default value");
        }
        return false;
    }
}
