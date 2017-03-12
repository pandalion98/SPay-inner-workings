package android.sec.enterprise;

import android.sec.enterprise.EnterpriseDeviceManager.EDMProxyServiceHelper;
import android.sec.enterprise.content.SecContentProviderURI;
import android.util.Log;

public class FirewallPolicy {
    private static String TAG = SecContentProviderURI.FIREWALLPOLICY;

    public boolean isUrlBlocked(String url) {
        boolean ret = false;
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                ret = lService.isUrlBlocked(url);
            }
        } catch (Exception e) {
            Log.d(TAG, "Exception...");
        }
        return ret;
    }
}
