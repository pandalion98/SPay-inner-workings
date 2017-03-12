package android.hardware.display;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.hardware.display.DisplayManager.WfdAppState;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.DetailedState;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Message;
import android.os.SystemProperties;
import android.provider.Settings.Global;
import android.provider.Settings.System;
import android.util.Log;
import android.util.Slog;
import android.view.Display;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class SecWifiDisplayUtil {
    private final String GP_PACKAGE_NAME = "com.samsung.groupcast";
    private final String GP_RUNNING_STATE_CHECK_FILE = ".gp_running_check";
    private final String TAG = "SecWifiDisplayUtil";
    private Context mContext;
    private boolean mScanInProgress;
    private boolean mZeroSettingsConcept;

    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$android$hardware$display$DisplayManager$WfdAppState = new int[WfdAppState.values().length];

        static {
            try {
                $SwitchMap$android$hardware$display$DisplayManager$WfdAppState[WfdAppState.SETUP.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$android$hardware$display$DisplayManager$WfdAppState[WfdAppState.PAUSE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$android$hardware$display$DisplayManager$WfdAppState[WfdAppState.RESUME.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$android$hardware$display$DisplayManager$WfdAppState[WfdAppState.TEARDOWN.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    public SecWifiDisplayUtil(Context context) {
        this.mContext = context;
        try {
            PackageManager pm = this.mContext.getPackageManager();
            if (pm != null) {
                Bundle meta_data = pm.getApplicationInfo("com.android.settings", 128).metaData;
                Slog.d("SecWifiDisplayUtil", "Metadata value : " + meta_data.getString("settings_apk_name", "none"));
                if (meta_data.getString("settings_apk_name", "none").equals("SecSettings2")) {
                    this.mZeroSettingsConcept = true;
                }
            }
        } catch (Exception e) {
            Log.d("SecWifiDisplayUtil", "Cannot get Metadata value", e);
            this.mZeroSettingsConcept = false;
        }
    }

    public int checkExceptionalCase() {
        try {
            int ret;
            if (isGroupPlayRunning()) {
                ret = 7;
            } else if (isHotspotOn()) {
                ret = 1;
            } else if (isP2pConnected()) {
                ret = 2;
            } else if (isHDMIConnected()) {
                ret = 3;
            } else if (isSideSyncConnected() || isSideSyncAppRunning()) {
                ret = 4;
            } else if ((getCPUPowerSavingMode() == 1 && getPowerSavingMode() == 1) || (this.mZeroSettingsConcept && getPowerSavingMode() == 1)) {
                ret = 5;
            } else if (isLimitedContentsPlaying()) {
                ret = 6;
            } else if (isWifiIbssEnabled()) {
                ret = 8;
            } else if (isLiveBroadcastRunning()) {
                ret = 9;
            } else {
                ret = 0;
            }
            Slog.e("SecWifiDisplayUtil", "checkExceptionalCase ret value : " + ret);
            return ret;
        } catch (NullPointerException e) {
            Slog.e("SecWifiDisplayUtil", "exception occured while using isScreenMirroringAvailable");
            e.printStackTrace();
            return 0;
        }
    }

    private boolean isLiveBroadcastRunning() {
        Slog.e("SecWifiDisplayUtil", "checkExceptionalCase isLiveBroadcastRunning");
        if (SystemProperties.getInt("service.camera.recording.plb", 0) == 1) {
            return true;
        }
        return false;
    }

    private boolean isLimitedContentsPlaying() {
        return 1 == System.getInt(this.mContext.getContentResolver(), Global.WIFI_DISPLAY_LIMITEDCONTENS_PLAYING, 0);
    }

    private int getPowerSavingMode() {
        if (this.mZeroSettingsConcept) {
            return Global.getInt(this.mContext.getContentResolver(), Global.LOW_POWER_MODE, 0);
        }
        return System.getIntForUser(this.mContext.getContentResolver(), "psm_switch", 0, -2);
    }

    private int getCPUPowerSavingMode() {
        return System.getIntForUser(this.mContext.getContentResolver(), "psm_cpu_clock", 0, -2);
    }

    public boolean isGroupPlayRunning() {
        for (RunningTaskInfo task : ((ActivityManager) this.mContext.getSystemService(Context.ACTIVITY_SERVICE)).getRunningTasks(Integer.MAX_VALUE)) {
            if (task.baseActivity.getPackageName().equals("com.samsung.groupcast")) {
                try {
                    try {
                        this.mContext.createPackageContext("com.samsung.groupcast", 2).openFileInput(".gp_running_check").close();
                    } catch (IOException ie) {
                        ie.printStackTrace();
                    }
                    return true;
                } catch (NameNotFoundException ne) {
                    ne.printStackTrace();
                    return false;
                } catch (FileNotFoundException fe) {
                    fe.printStackTrace();
                    return false;
                }
            }
        }
        return false;
    }

    private boolean isSideSyncAppRunning() {
        List<RunningTaskInfo> a = ((ActivityManager) this.mContext.getSystemService(Context.ACTIVITY_SERVICE)).getRunningTasks(Integer.MAX_VALUE);
        PackageManager pack = this.mContext.getPackageManager();
        for (int i = 0; i < a.size(); i++) {
            String className = ((RunningTaskInfo) a.get(i)).topActivity.getClassName();
            if (className.contains("com.sec.android.sidesync30.ui")) {
                Slog.d("SecWifiDisplayUtil", "isSideSyncAppRunning className = " + className);
                return true;
            }
        }
        return false;
    }

    private boolean isSideSyncConnected() {
        int isSideSync = System.getInt(this.mContext.getContentResolver(), "sidesync_source_connect", 0);
        if (isSideSync == 0) {
            return false;
        }
        Slog.d("SecWifiDisplayUtil", "isSideSyncConnected : " + isSideSync);
        return true;
    }

    private boolean isHDMIConnected() throws NullPointerException {
        boolean isHDMIConnected = false;
        Display[] displays = ((DisplayManager) this.mContext.getSystemService(Context.DISPLAY_SERVICE)).getDisplays();
        int i = 0;
        while (i < displays.length) {
            if (displays[i] != null && displays[i].getType() == 2) {
                isHDMIConnected = true;
            }
            i++;
        }
        Slog.d("SecWifiDisplayUtil", "isHDMIConnected(): " + isHDMIConnected);
        return isHDMIConnected;
    }

    private boolean isWifiIbssEnabled() throws NullPointerException {
        return ((WifiManager) this.mContext.getSystemService("wifi")).isWifiIBSSEnabled();
    }

    private boolean isHotspotOn() throws NullPointerException {
        boolean isHotspotOn = false;
        switch (((WifiManager) this.mContext.getSystemService("wifi")).getWifiApState()) {
            case 12:
            case 13:
                isHotspotOn = true;
                break;
        }
        Slog.d("SecWifiDisplayUtil", "isHotSpotOn(): " + isHotspotOn);
        return isHotspotOn;
    }

    private boolean isP2pConnected() throws NullPointerException {
        boolean isP2pConntected;
        if (((ConnectivityManager) this.mContext.getSystemService(Context.CONNECTIVITY_SERVICE)).getNetworkInfo(13).getDetailedState() == DetailedState.CONNECTED) {
            isP2pConntected = true;
        } else {
            isP2pConntected = false;
        }
        Slog.d("SecWifiDisplayUtil", "isP2pConntected(): " + isP2pConntected);
        return isP2pConntected;
    }

    public void setActivityState(WfdAppState state) {
        if (!isWfdConnected()) {
            switch (AnonymousClass1.$SwitchMap$android$hardware$display$DisplayManager$WfdAppState[state.ordinal()]) {
                case 1:
                    Global.putInt(this.mContext.getContentResolver(), Global.WIFI_DISPLAY_ON, 1);
                    return;
                case 2:
                    this.mScanInProgress = isWfdScanning();
                    if (this.mScanInProgress) {
                        stopScanWifiDisplays();
                    }
                    stopPeriodicScanning(false);
                    return;
                case 3:
                    if (!isWfdConnected()) {
                        stopPeriodicScanning(true);
                        if (this.mScanInProgress) {
                            this.mScanInProgress = false;
                            scanWifiDisplays();
                            return;
                        }
                        return;
                    }
                    return;
                case 4:
                    if (!isWfdConnected() && isWfdStatusOn()) {
                        Global.putInt(this.mContext.getContentResolver(), Global.WIFI_DISPLAY_ON, 0);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    private boolean isWfdConnected() {
        if (((DisplayManager) this.mContext.getSystemService(Context.DISPLAY_SERVICE)).getWifiDisplayStatus().getActiveDisplayState() == 2) {
            return true;
        }
        return false;
    }

    private boolean isWfdScanning() {
        if (((DisplayManager) this.mContext.getSystemService(Context.DISPLAY_SERVICE)).getWifiDisplayStatus().getScanState() == 1) {
            return true;
        }
        return false;
    }

    private void scanWifiDisplays() {
        ((DisplayManager) this.mContext.getSystemService(Context.DISPLAY_SERVICE)).scanWifiDisplays();
    }

    private void stopScanWifiDisplays() {
        ((DisplayManager) this.mContext.getSystemService(Context.DISPLAY_SERVICE)).stopScanWifiDisplays();
    }

    private boolean isWfdStatusOn() {
        if (((DisplayManager) this.mContext.getSystemService(Context.DISPLAY_SERVICE)).getWifiDisplayStatus().getFeatureState() == 3) {
            return true;
        }
        return false;
    }

    private void stopPeriodicScanning(boolean enable) {
        WifiManager tWifiManager = (WifiManager) this.mContext.getSystemService("wifi");
        if (tWifiManager != null && tWifiManager.isWifiEnabled()) {
            Message msg = new Message();
            msg.what = 18;
            Bundle args = new Bundle();
            args.putBoolean("stop", enable);
            msg.obj = args;
            tWifiManager.callSECApi(msg);
        }
    }
}
