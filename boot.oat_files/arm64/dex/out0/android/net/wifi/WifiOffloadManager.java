package android.net.wifi;

import android.util.Log;

public class WifiOffloadManager {
    private static final String TAG = "WifiOffloadManager";
    IWifiOffloadManager mService;

    public WifiOffloadManager(IWifiOffloadManager service) {
        this.mService = service;
    }

    public void startTimer() {
        try {
            this.mService.startTimer();
        } catch (Exception e) {
            Log.e(TAG, "Exception while startDontUseWiFiPressedTimer " + e);
        }
    }

    public void checkAppNeedsMoveToFront(int taskID) {
        try {
            this.mService.checkAppNeedsMoveToFront(taskID);
        } catch (Exception e) {
            Log.e(TAG, "Exception in checkAppNeedsMoveToFront " + e);
        }
    }

    public void checkAppForWiFiOffloading(String pkgName) {
        try {
            this.mService.checkAppForWiFiOffloading(pkgName);
        } catch (Exception e) {
            Log.e(TAG, "Exception in checkAppForWiFiOffloading " + e);
        }
    }
}
