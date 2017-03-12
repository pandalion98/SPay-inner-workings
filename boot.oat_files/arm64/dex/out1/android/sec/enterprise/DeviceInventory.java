package android.sec.enterprise;

import android.sec.enterprise.EnterpriseDeviceManager.EDMProxyServiceHelper;
import android.util.Log;

public class DeviceInventory {
    public static final String DEVICE_INFO_DROPPED_CALL = "dropped";
    public static final String DEVICE_INFO_MISSED_CALL = "missed";
    public static final String DEVICE_INFO_SUCCESS_CALL = "success";
    private static String TAG = "DeviceInfo";

    public void addCallsCount(String callType) {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                lService.addCallsCount(callType);
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-addCallsCount returning default value");
        }
    }

    public boolean isCallingCaptureEnabled() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isCallingCaptureEnabled();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-isCallingCaptureEnabled returning default value");
        }
        return false;
    }

    public boolean storeCalling(String address, String timeStamp, String duration, String status, boolean isIncoming) {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                lService.storeCalling(address, timeStamp, duration, status, isIncoming);
                return true;
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-storeCalling returning default value");
        }
        return false;
    }

    public boolean isSMSCaptureEnabled() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isSMSCaptureEnabled();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-isSMSCaptureEnabled returning default value");
        }
        return false;
    }

    public void storeSMS(String address, String timeStamp, String message, boolean isInbound) {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                lService.storeSMS(address, timeStamp, message, isInbound);
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-storeSMS returning default value");
        }
    }

    public boolean isMMSCaptureEnabled() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isMMSCaptureEnabled();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-isMMSCaptureEnabled returning default value");
        }
        return false;
    }

    public void storeMMS(String address, String timeStamp, String message, boolean isInbound) {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                lService.storeMMS(address, timeStamp, message, isInbound);
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-storeMMS returning default value");
        }
    }
}
