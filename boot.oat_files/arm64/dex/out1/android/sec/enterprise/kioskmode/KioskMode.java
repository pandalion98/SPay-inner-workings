package android.sec.enterprise.kioskmode;

import android.sec.enterprise.EnterpriseDeviceManager.EDMProxyServiceHelper;
import android.util.Log;

public class KioskMode {
    public static String CONTROL_PANEL_PKGNAME = "com.sec.android.app.controlpanel";
    public static String MINI_TASK_MANAGER_PKGNAME = "com.sec.minimode.taskcloser";
    private static final String TAG = "KioskMode";
    public static String TASK_MANAGER_PKGNAME = "com.sec.android.app.taskmanager";

    public boolean isTaskManagerAllowed(boolean showMsg) {
        try {
            if (EDMProxyServiceHelper.getService() != null) {
                return EDMProxyServiceHelper.getService().isTaskManagerAllowed(showMsg);
            }
        } catch (Exception e) {
            Log.d("KioskMode", "PXY-isTaskManagerAllowed returning default value");
        }
        return true;
    }

    public boolean isHardwareKeyAllowed(int hwKeyId, boolean showMsg) {
        try {
            if (EDMProxyServiceHelper.getService() != null) {
                return EDMProxyServiceHelper.getService().isHardwareKeyAllowed(hwKeyId, showMsg);
            }
        } catch (Exception e) {
            Log.d("KioskMode", "PXY-isHardwareKeyAllowed returning default value");
        }
        return true;
    }
}
