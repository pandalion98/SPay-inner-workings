package android.sec.enterprise;

import android.content.Intent;
import android.sec.enterprise.EnterpriseDeviceManager.EDMProxyServiceHelper;
import android.sec.enterprise.content.SecContentProviderURI;
import android.util.Log;

public class ApplicationPolicy {
    public static final int NOTIFICATION_MODE_BLOCK_ALL = 2;
    public static final int NOTIFICATION_MODE_BLOCK_TEXT = 3;
    public static final int NOTIFICATION_MODE_BLOCK_TEXT_AND_SOUND = 4;
    private static String TAG = SecContentProviderURI.APPLICATIONPOLICY;

    public byte[] getApplicationIconFromDb(String packageName, int userId) {
        byte[] imageData = null;
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                imageData = lService.getApplicationIconFromDb(packageName, userId);
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-getApplicationIconFromDb returning default value");
        }
        return imageData;
    }

    public boolean isIntentDisabled(Intent intent) {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isIntentDisabled(intent);
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-isIntentDisabled returning default value");
        }
        return false;
    }

    public boolean isApplicationForceStopDisabled(String packageName, int userId, String errorType, String errorClass, String errorReason, boolean showMsg) {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isApplicationForceStopDisabled(packageName, userId, errorType, errorClass, errorReason, showMsg);
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-isApplicationForceStopDisabled returning default value");
        }
        return false;
    }

    public boolean getApplicationStateEnabled(String packageName, boolean showMsg) {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.getApplicationStateEnabled(packageName, showMsg);
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-getApplicationStateEnabled returning default value");
        }
        return true;
    }

    public boolean getApplicationStateEnabledAsUser(String packageName, boolean showMsg, int userId) {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.getApplicationStateEnabledAsUser(packageName, showMsg, userId);
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-getApplicationStateEnabledAsUser returning default value");
        }
        return true;
    }

    public boolean getAddHomeShorcutRequested() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.getAddHomeShorcutRequested();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-getAddHomeShorcutRequested returning default value");
        }
        return false;
    }

    public String getApplicationNameFromDb(String packageName, int userId) {
        String newName = null;
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                newName = lService.getApplicationNameFromDb(packageName, userId);
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-getApplicationNameFromDb returning default value");
        }
        return newName;
    }
}
