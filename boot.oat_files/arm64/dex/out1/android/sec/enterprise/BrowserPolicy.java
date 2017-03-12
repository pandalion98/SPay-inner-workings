package android.sec.enterprise;

import android.sec.enterprise.EnterpriseDeviceManager.EDMProxyServiceHelper;
import android.sec.enterprise.content.SecContentProviderURI;
import android.util.Log;

public class BrowserPolicy {
    private static String TAG = SecContentProviderURI.BROWSERPOLICY;

    public class BrowserSetting {
        public static final String ACTION_CLEAR_HTTP_BROWSER_PROXY = "mdm.intent.action.clear.http.proxy";
        public static final String ACTION_SET_HTTP_BROWSER_PROXY = "mdm.intent.action.set.http.proxy";
        public static final int BROWSER_AUTOFILL_SETTING = 4;
        public static final int BROWSER_COOKIES_SETTING = 2;
        public static final int BROWSER_FORCEFRAUDWARNING_SETTING = 8;
        public static final int BROWSER_JAVASCRIPT_SETTING = 16;
        public static final int BROWSER_POPUP_SETTING = 1;
        public static final String EXTRA_HTTP_PROXY_BROWSER_SERVER = "mdm.intent.extra.http.proxy.server";
    }

    public boolean getCookiesSetting() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.getBrowserSettingStatus(2);
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-getCookiesSetting returning default value");
        }
        return true;
    }

    public boolean getAutoFillSetting() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.getBrowserSettingStatus(4);
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-getAutoFillSetting returning default value");
        }
        return true;
    }

    public boolean getJavaScriptSetting() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.getBrowserSettingStatus(16);
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-getJavaScriptSetting returning default value");
        }
        return true;
    }

    public boolean getPopupsSetting() {
        boolean z = true;
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                z = lService.getBrowserSettingStatus(1);
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-getPopupsSetting returning default value");
        }
        return z;
    }
}
