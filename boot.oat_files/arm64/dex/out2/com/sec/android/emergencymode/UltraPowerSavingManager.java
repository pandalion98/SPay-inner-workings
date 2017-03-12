package com.sec.android.emergencymode;

import android.content.Context;
import android.provider.Settings.System;

public class UltraPowerSavingManager {
    public static final String EXTRA_ENABLED = "enable";
    private static final String TAG = "UltraPowerSavingManager";
    public static final int ULTRA_POWERSAVING_MODE_NONE = 0;
    public static final int ULTRA_POWERSAVING_MODE_NORMAL = 1;
    public static final String ULTRA_POWERSAVING_SERVICE = "com.sec.android.emergencymode.UltraPowerSavingManager.ORDER_ULTRA_POWERSAVING_SERVICE";
    private static UltraPowerSavingManager sInstance = null;
    private Context mContext;

    public static synchronized UltraPowerSavingManager getInstance(Context context) {
        UltraPowerSavingManager ultraPowerSavingManager;
        synchronized (UltraPowerSavingManager.class) {
            if (context == null) {
                ultraPowerSavingManager = null;
            } else {
                if (sInstance == null) {
                    sInstance = new UltraPowerSavingManager(context);
                }
                ultraPowerSavingManager = sInstance;
            }
        }
        return ultraPowerSavingManager;
    }

    private UltraPowerSavingManager(Context context) {
        this.mContext = context;
    }

    public boolean isSupported() {
        return EmergencyManager.supportUltraPowerSavingMode();
    }

    public int getMode() {
        if (System.getInt(this.mContext.getContentResolver(), "emergency_mode", 0) == 1) {
            return 1;
        }
        return 0;
    }
}
