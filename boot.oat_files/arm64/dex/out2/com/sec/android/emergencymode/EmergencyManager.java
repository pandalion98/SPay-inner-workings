package com.sec.android.emergencymode;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.os.PersonaManager;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings.Global;
import android.provider.Settings.System;
import com.samsung.android.feature.FloatingFeature;
import com.samsung.cpp.CPPDbAdapter;
import com.sec.android.emergencymode.IEmergencyManager.Stub;

public class EmergencyManager {
    private static final boolean SERVICE_DBG = false;
    private static final String TAG = "EmergencyManager";
    private static boolean mIsLoadedFeatures;
    private static IEmergencyManager mService;
    private static boolean mSupport_BCM;
    private static boolean mSupport_EM;
    private static boolean mSupport_UPSM;
    private static EmergencyManager sInstance = null;
    private Context mContext;
    private final Handler mHandler;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null) {
                Elog.d(EmergencyManager.TAG, "onReceive : " + intent);
                boolean enabled;
                if (action.equals(EmergencyConstants.EMERGENCY_START_SERVICE_BY_ORDER) || action.equals(EmergencyConstants.EMERGENCY_START_SERVICE_BY_ORDER_OLD)) {
                    enabled = intent.getBooleanExtra(EmergencyConstants.EXTRA_EMERGENCY_START_SERVICE_ENABLE, false);
                    int flag = intent.getIntExtra(EmergencyConstants.EXTRA_EMERGENCY_START_SERVICE_FLAG, -1);
                    boolean skipdialog = intent.getBooleanExtra(EmergencyConstants.EXTRA_EMERGENCY_START_SERVICE_SKIPDIALOG, false);
                    if (flag == -1) {
                        return;
                    }
                    if ((flag != 2048 || EmergencyManager.mSupport_BCM) && (!(flag == 512 || flag == 1024) || EmergencyManager.mSupport_UPSM)) {
                        EmergencyManager.this.triggerEmergencyMode(enabled, flag, skipdialog);
                    } else {
                        Elog.d(EmergencyManager.TAG, "onReceive : trying to ON BCM|UPSM while BCM|UPMS not supported in this model. Flag = " + flag);
                    }
                } else if (action.equals("com.nttdocomo.android.epsmodecontrol.action.CHANGE_MODE")) {
                    if (EmergencyManager.isEmergencyMode(EmergencyManager.this.mContext)) {
                        enabled = false;
                    } else {
                        enabled = true;
                    }
                    EmergencyManager.this.triggerEmergencyMode(enabled, 16, false);
                }
            }
        }
    };
    private BroadcastReceiver mReceiverUPSM = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null) {
                Elog.d(EmergencyManager.TAG, "onReceive : " + intent);
                if (action.equals(UltraPowerSavingManager.ULTRA_POWERSAVING_SERVICE)) {
                    EmergencyManager.this.triggerEmergencyMode(intent.getBooleanExtra(UltraPowerSavingManager.EXTRA_ENABLED, false), 512, false);
                }
            }
        }
    };

    public static synchronized EmergencyManager getInstance(Context context) {
        EmergencyManager emergencyManager;
        synchronized (EmergencyManager.class) {
            if (context == null) {
                emergencyManager = null;
            } else {
                if (sInstance == null) {
                    sInstance = new EmergencyManager(new Handler(context.getMainLooper()), context);
                }
                emergencyManager = sInstance;
            }
        }
        return emergencyManager;
    }

    private EmergencyManager(Handler handler, Context context) {
        this.mHandler = handler;
        this.mContext = context;
        loadFloatingFeatures();
        ensureServiceConnected();
    }

    private void ensureServiceConnected() {
        try {
            if (mService == null) {
                mService = Stub.asInterface(ServiceManager.getService(EmergencyConstants.SERVICE_NAME));
            } else if (!mService.asBinder().isBinderAlive()) {
                Elog.d(TAG, "mService is not valid so retieve the service again.");
                mService = Stub.asInterface(ServiceManager.getService(EmergencyConstants.SERVICE_NAME));
            }
        } catch (Exception e) {
            Elog.d(TAG, "ensureServiceConnected e : " + e);
        }
    }

    private static void loadFloatingFeatures() {
        mSupport_UPSM = FloatingFeature.getInstance().getEnableStatus("SEC_FLOATING_FEATURE_COMMON_SUPPORT_ULTRA_POWER_SAVING");
        mSupport_EM = FloatingFeature.getInstance().getEnableStatus("SEC_FLOATING_FEATURE_COMMON_SUPPORT_SAFETYCARE");
        mSupport_BCM = FloatingFeature.getInstance().getEnableStatus("SEC_FLOATING_FEATURE_COMMON_SUPPORT_BATTERY_CONVERSING");
        mIsLoadedFeatures = true;
    }

    public void readyEmergencyMode() {
        if (isEmergencyMode(this.mContext)) {
            Elog.d(TAG, "This is emergency mode.");
            startService(null, false, -1, false);
        } else {
            Elog.d(TAG, "This is normal mode.");
            this.mContext.getContentResolver().query(EmergencyConstants.URI_UPDATE_TABLE, null, null, null, null, null);
            startService(EmergencyConstants.EMERGENCY_CHECK_ABNORMAL_STATE, false, -1, false);
        }
        registerReceiver();
    }

    private synchronized void startService(String action, boolean enabled, int flag, boolean skipdialog) {
        try {
            Intent intent = new Intent();
            if (flag == -1) {
                intent.putExtra(EmergencyConstants.EXTRA_CLEAR_BOOT_TIME, true);
            }
            if (action == null) {
                intent.putExtra(EmergencyConstants.EXTRA_INIT_FOR_EM_STATE, true);
            } else if (action.equals(EmergencyConstants.EMERGENCY_START_SERVICE_BY_ORDER)) {
                intent.setAction(action);
                intent.putExtra(EmergencyConstants.EXTRA_EMERGENCY_START_SERVICE_ENABLE, enabled);
                intent.putExtra(EmergencyConstants.EXTRA_EMERGENCY_START_SERVICE_FLAG, flag);
                intent.putExtra(EmergencyConstants.EXTRA_EMERGENCY_START_SERVICE_SKIPDIALOG, skipdialog);
            } else if (action.equals(EmergencyConstants.EMERGENCY_CHECK_ABNORMAL_STATE)) {
                intent.setAction(action);
            }
            intent.setComponent(new ComponentName(EmergencyConstants.EMERGENCY_SERVICE_PACKAGE, EmergencyConstants.EMERGENCY_SERVICE_STARTER));
            Elog.d(TAG, "Starting service: " + intent);
            this.mContext.startServiceAsUser(intent, UserHandle.OWNER);
        } catch (Exception e) {
            Elog.d(TAG, "startService e : " + e);
        }
    }

    private void stopService() {
        synchronized (EmergencyManager.class) {
            try {
                if (mService != null) {
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName(EmergencyConstants.EMERGENCY_SERVICE_PACKAGE, EmergencyConstants.EMERGENCY_SERVICE_STARTER));
                    Elog.d(TAG, "stopService: " + intent);
                    this.mContext.stopServiceAsUser(intent, UserHandle.OWNER);
                    mService = null;
                }
            } catch (Exception e) {
                Elog.d(TAG, "stopService e : " + e);
            }
        }
    }

    private void registerReceiver() {
        Elog.d(TAG, "registerReceiver");
        IntentFilter filter = new IntentFilter();
        filter.addAction(EmergencyConstants.EMERGENCY_START_SERVICE_BY_ORDER);
        filter.addAction(EmergencyConstants.EMERGENCY_START_SERVICE_BY_ORDER_OLD);
        String salesCode = SystemProperties.get("ro.csc.sales_code", "unknown");
        Elog.d(TAG, "registerReceiver Scode[" + salesCode + "]");
        if ("DCM".equalsIgnoreCase(salesCode)) {
            filter.addAction("com.nttdocomo.android.epsmodecontrol.action.CHANGE_MODE");
        }
        this.mContext.registerReceiver(this.mReceiver, filter, "com.sec.android.emergencymode.permission.LAUNCH_EMERGENCYMODE_SERVICE", null);
        IntentFilter filterupsm = new IntentFilter();
        filterupsm.addAction(UltraPowerSavingManager.ULTRA_POWERSAVING_SERVICE);
        this.mContext.registerReceiver(this.mReceiverUPSM, filterupsm, "com.samsung.android.permission.LAUNCH_ULTRAPOWERSAVING_SERVICE", null);
    }

    private void unregisterReceiver() {
        Elog.d(TAG, "unregisterReceiver");
        this.mContext.unregisterReceiver(this.mReceiver);
    }

    public static boolean isEmergencyMode(Context context) {
        return System.getInt(context.getContentResolver(), "emergency_mode", 0) == 1;
    }

    public static boolean isBatteryConservingMode(Context context) {
        return System.getInt(context.getContentResolver(), "battery_conserving_mode", 0) == 1;
    }

    public static boolean supportEmergencyMode() {
        if (!mIsLoadedFeatures) {
            loadFloatingFeatures();
        }
        return mSupport_EM;
    }

    public static boolean supportUltraPowerSavingMode() {
        if (!mIsLoadedFeatures) {
            loadFloatingFeatures();
        }
        return mSupport_UPSM;
    }

    public int getModeType() {
        if (System.getInt(this.mContext.getContentResolver(), "ultra_powersaving_mode", 0) == 1) {
            return 1;
        }
        if (System.getInt(this.mContext.getContentResolver(), "battery_conserving_mode", 0) == 1) {
            return 2;
        }
        if (System.getInt(this.mContext.getContentResolver(), "emergency_mode", 0) == 1) {
            return 0;
        }
        return -1;
    }

    public static boolean supportBatteryConversingMode() {
        if (!mIsLoadedFeatures) {
            loadFloatingFeatures();
        }
        return mSupport_BCM;
    }

    public static boolean supportGrayScreen() {
        boolean z;
        String str = TAG;
        StringBuilder append = new StringBuilder().append("support MDNIE [").append(true).append("]  AMOLED display [").append(true).append("]  supportGrayScreen [");
        if (1 == null || 1 == null) {
            z = false;
        } else {
            z = true;
        }
        Elog.d(str, append.append(z).append("]").toString());
        if (1 == null || 1 == null) {
            return false;
        }
        return true;
    }

    public boolean isEmergencyMode() {
        boolean z = false;
        if (mSupport_EM || mSupport_UPSM || mSupport_BCM) {
            ensureServiceConnected();
            if (mService != null) {
                try {
                    z = mService.isEmergencyMode();
                } catch (Exception e) {
                    Elog.d(TAG, "isEmergencyMode failed e : " + e);
                }
            }
        }
        return z;
    }

    public int getEmergencyState() {
        int i = -1;
        if (mSupport_EM || mSupport_UPSM || mSupport_BCM) {
            ensureServiceConnected();
            if (mService != null) {
                try {
                    i = mService.getEmergencyState();
                } catch (Exception e) {
                    Elog.d(TAG, "getEmergencyState failed e : " + e);
                }
            }
        }
        return i;
    }

    public boolean checkValidPackage(String pkgName, String actName, int allowFlag) {
        boolean z = true;
        if (!mSupport_EM && !mSupport_UPSM && !mSupport_BCM) {
            return false;
        }
        ensureServiceConnected();
        if (mService == null) {
            return z;
        }
        try {
            return mService.checkValidPackage(pkgName, actName, allowFlag);
        } catch (Exception e) {
            Elog.d(TAG, "checkValidPackage failed e : " + e);
            return z;
        }
    }

    public boolean checkValidIntentAction(String pkgName, String actName) {
        boolean z = true;
        if (!mSupport_EM && !mSupport_UPSM && !mSupport_BCM) {
            return false;
        }
        ensureServiceConnected();
        if (mService == null) {
            return z;
        }
        try {
            return mService.checkValidIntentAction(pkgName, actName);
        } catch (Exception e) {
            Elog.d(TAG, "checkValidIntentAction failed e : " + e);
            return z;
        }
    }

    public boolean checkInvalidProcess(String pkgName) {
        boolean z = false;
        if (mSupport_EM || mSupport_UPSM || mSupport_BCM) {
            ensureServiceConnected();
            if (mService != null) {
                try {
                    z = mService.checkInvalidProcess(pkgName);
                } catch (Exception e) {
                    Elog.d(TAG, "checkInvalidProcess failed e : " + e);
                }
            }
        }
        return z;
    }

    public boolean checkInvalidBroadcast(String pkgName, String action) {
        boolean z = false;
        if (mSupport_EM || mSupport_UPSM || mSupport_BCM) {
            ensureServiceConnected();
            if (mService != null) {
                try {
                    z = mService.checkInvalidBroadcast(pkgName, action);
                } catch (Exception e) {
                    Elog.d(TAG, "checkInvalidBroadcast failed e : " + e);
                }
            }
        }
        return z;
    }

    public boolean needMobileDataBlock() {
        boolean z = false;
        if (mSupport_EM || mSupport_UPSM || mSupport_BCM) {
            ensureServiceConnected();
            if (mService != null) {
                try {
                    z = mService.needMobileDataBlock();
                } catch (Exception e) {
                    Elog.d(TAG, "needMobileDataBlock failed e : " + e);
                }
            }
        }
        return z;
    }

    public boolean isScreenOn() {
        boolean z = false;
        if (mSupport_EM || mSupport_UPSM || mSupport_BCM) {
            ensureServiceConnected();
            if (mService != null) {
                try {
                    z = mService.isScreenOn();
                } catch (Exception e) {
                    Elog.d(TAG, "isScreenOn failed e : " + e);
                }
            }
        }
        return z;
    }

    public void setforceBlockUserPkg(boolean enabled, Context context) {
        if (mSupport_EM || mSupport_UPSM || mSupport_BCM) {
            ensureServiceConnected();
            if (mService != null) {
                try {
                    mService.setforceBlockUserPkg(enabled);
                } catch (Exception e) {
                    Elog.d(TAG, "setforceBlockUserPkg failed e : " + e);
                }
            }
        }
    }

    public boolean getforceBlockUserPkg() {
        boolean z = false;
        if (mSupport_EM || mSupport_UPSM || mSupport_BCM) {
            ensureServiceConnected();
            if (mService != null) {
                try {
                    z = mService.getforceBlockUserPkg();
                } catch (Exception e) {
                    Elog.d(TAG, "getforceBlockUserPkg failed e : " + e);
                }
            }
        }
        return z;
    }

    public boolean isModifying() {
        boolean z = false;
        if (mSupport_EM || mSupport_UPSM || mSupport_BCM) {
            ensureServiceConnected();
            if (mService != null) {
                try {
                    z = mService.isModifying();
                } catch (Exception e) {
                    Elog.d(TAG, "isModifying failed e : " + e);
                }
            }
        }
        return z;
    }

    public boolean canSetMode() {
        if (!mSupport_EM && !mSupport_UPSM && !mSupport_BCM) {
            return false;
        }
        boolean isDeviceProvisioned;
        UserManager mUserManager = (UserManager) this.mContext.getSystemService("user");
        boolean result = true;
        boolean modifying = false;
        boolean knoxUser = false;
        int currentUserId = 0;
        String reason = "";
        try {
            modifying = isModifying();
            currentUserId = ActivityManager.getCurrentUser();
            if ("2.0".equals(PersonaManager.getKnoxInfo().getString(CPPDbAdapter.KEY_VERSION))) {
                PersonaManager mPersona = (PersonaManager) this.mContext.getSystemService("persona");
                if (mPersona != null && mPersona.exists(currentUserId)) {
                    knoxUser = true;
                }
            }
        } catch (Exception e) {
            Elog.d(TAG, "canSetMode Exception : " + e);
        }
        if (Global.getInt(this.mContext.getContentResolver(), "device_provisioned", 0) != 0) {
            isDeviceProvisioned = true;
        } else {
            isDeviceProvisioned = false;
        }
        if (!isDeviceProvisioned) {
            result = false;
            reason = reason + "SETUP_WIZARD_UNFINISHED;";
        }
        if (modifying) {
            result = false;
            reason = reason + "LLM_ENABLING;";
        }
        if (!(currentUserId == 0 || knoxUser)) {
            result = false;
            reason = reason + "NOT_OWNER_" + currentUserId + ";";
        }
        if (result) {
            return result;
        }
        Elog.v(TAG, "not Allowed EmergencyMode due to " + reason);
        return result;
    }

    private void triggerEmergencyMode(boolean enabled, int flag, boolean skipdialog) {
        ensureServiceConnected();
        startService(EmergencyConstants.EMERGENCY_START_SERVICE_BY_ORDER, enabled, flag, skipdialog);
        Elog.d(TAG, "req trigger, start Service");
    }

    public boolean checkModeType(int type) {
        boolean z = false;
        if (mSupport_EM || mSupport_UPSM || mSupport_BCM) {
            ensureServiceConnected();
            if (mService != null) {
                try {
                    z = mService.checkModeType(type);
                } catch (Exception e) {
                    Elog.d(TAG, "checkModeType failed e : " + e);
                }
            }
        }
        return z;
    }

    public void setLocationProvider(boolean enable) {
        if (mSupport_EM || mSupport_UPSM || mSupport_BCM) {
            ensureServiceConnected();
            if (mService != null) {
                try {
                    mService.setLocationProvider(enable);
                } catch (Exception e) {
                    Elog.d(TAG, "setLocationProvider failed e : " + e);
                }
            }
        }
    }

    public boolean addAppToLauncher(String pkgName, boolean enabled) {
        boolean z = false;
        ensureServiceConnected();
        if (mService != null) {
            try {
                z = mService.addAppToLauncher(pkgName, enabled);
            } catch (Exception e) {
                Elog.d(TAG, "addAppToLauncher failed e : " + e);
            }
        }
        return z;
    }

    public int requestCallPrivileged(String number) {
        try {
            Intent callIntent = new Intent("android.intent.action.CALL_PRIVILEGED", Uri.fromParts("tel", number, null));
            callIntent.setFlags(268435456);
            this.mContext.startActivity(callIntent);
            Elog.d(TAG, "req call, success.");
            return 1;
        } catch (Exception e) {
            Elog.d(TAG, "req call, unknown Err : " + e);
            return -9;
        }
    }
}
