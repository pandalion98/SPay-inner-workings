package android.sec.enterprise;

import android.sec.enterprise.EnterpriseDeviceManager.EDMProxyServiceHelper;
import android.sec.enterprise.content.SecContentProviderURI;
import android.util.Log;

public class DeviceAccountPolicy {
    private static String TAG = SecContentProviderURI.SECURITYPOLICY;

    public boolean isAccountRemovalAllowed(String type, String account, boolean showMsg) {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isAccountRemovalAllowed(type, account, showMsg);
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-isAccountRemovalAllowed returning default value");
        }
        return true;
    }

    public boolean isAccountAdditionAllowed(String type, String account, boolean showMsg) {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isAccountAdditionAllowed(type, account, showMsg);
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-isAccountAdditionAllowed returning default value");
        }
        return true;
    }
}
