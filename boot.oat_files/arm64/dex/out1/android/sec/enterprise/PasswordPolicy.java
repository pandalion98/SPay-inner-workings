package android.sec.enterprise;

import android.sec.enterprise.EnterpriseDeviceManager.EDMProxyServiceHelper;
import android.util.Log;

public class PasswordPolicy {
    public static final int PWD_CHANGE_NOT_ENFORCED = 0;
    private static String TAG = "PasswordPolicy";
    public static final String[] enforcePwdExceptions = new String[]{"com.android.settings.SubSettings", "com.android.settings.ChooseLockPassword", "com.google.android.gsf.update.SystemUpdateInstallDialog", "com.google.android.gsf.update.SystemUpdateDownloadDialog", "com.android.phone.EmergencyDialer", "com.android.phone.OutgoingCallBroadcaster", "com.android.phone.EmergencyOutgoingCallBroadcaster", "com.android.phone.InCallScreen", "com.android.internal.policy.impl.LockScreen", "com.android.internal.policy.impl.PatternUnlockScreen", "com.android.internal.policy.impl.PasswordUnlockScreen", "com.android.server.telecom.EmergencyCallActivity"};

    public int isChangeRequested() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isChangeRequested();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-isChangeRequested returning default value");
        }
        return 0;
    }

    public boolean isScreenLockPatternVisibilityEnabled() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isScreenLockPatternVisibilityEnabled();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-isScreenLockPatternVisibilityEnabled returning default value");
        }
        return true;
    }
}
