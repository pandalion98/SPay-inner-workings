package android.sec.enterprise;

import android.sec.enterprise.EnterpriseDeviceManager.EDMProxyServiceHelper;
import android.sec.enterprise.content.SecContentProviderURI;
import android.util.Log;

public class LocationPolicy {
    private static String TAG = SecContentProviderURI.LOCATIONPOLICY;

    public boolean isLocationProviderBlocked(String SProvider) {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isLocationProviderBlocked(SProvider);
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-isLocationProviderBlocked returning default value");
        }
        return false;
    }
}
