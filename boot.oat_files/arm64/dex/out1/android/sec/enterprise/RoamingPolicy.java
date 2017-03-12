package android.sec.enterprise;

import android.sec.enterprise.EnterpriseDeviceManager.EDMProxyServiceHelper;
import android.sec.enterprise.content.SecContentProviderURI;
import android.util.Log;

public class RoamingPolicy {
    private static String TAG = SecContentProviderURI.ROAMINGPOLICY;

    public boolean isRoamingPushEnabled() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isRoamingPushEnabled();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-isRoamingPushEnabled returning default value");
        }
        return true;
    }

    public boolean isRoamingSyncEnabled() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isRoamingSyncEnabled();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-isRoamingSyncEnabled returning default value");
        }
        return true;
    }
}
