package android.sec.enterprise;

import android.sec.enterprise.EnterpriseDeviceManager.EDMProxyServiceHelper;
import android.sec.enterprise.content.SecContentProviderURI;
import android.util.Log;

public class DateTimePolicy {
    public static final long NOT_SET_LONG = 0;
    private static String TAG = SecContentProviderURI.DATETIMEPOLICY;

    public boolean isDateTimeChangeEnabled() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isDateTimeChangeEnabled();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-isDateTimeChangeEnabled returning default value");
        }
        return true;
    }
}
