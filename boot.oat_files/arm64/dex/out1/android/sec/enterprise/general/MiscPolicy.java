package android.sec.enterprise.general;

import android.sec.enterprise.EnterpriseDeviceManager.EDMProxyServiceHelper;
import android.sec.enterprise.IEDMProxy;
import android.sec.enterprise.content.SecContentProviderURI;
import android.util.Log;

public class MiscPolicy {
    String TAG = SecContentProviderURI.MISCPOLICY;

    public boolean isNFCStateChangeAllowed() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isNFCStateChangeAllowed();
            }
        } catch (Exception e) {
            Log.d(this.TAG, "Failed to Talking to MiscPolicyService", e);
        }
        return true;
    }
}
