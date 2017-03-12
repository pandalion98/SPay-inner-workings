package com.sec.tima.keystore.util;

import android.os.SystemProperties;

public class Utility {
    public static final String CHIPNAME = SystemProperties.get("ro.chipname");
    private static Utility INSTANCE = null;
    public static final String PRODUCT_NAME = SystemProperties.get("ro.product.name");
    public static final String SDK_VERSION = SystemProperties.get("ro.build.version.sdk");
    private final String[] SDK_21_MODELS = new String[]{"ZERO"};
    private final String[] SDK_22_MODELS = new String[]{"ZERO", "NOBLE", "ZEN"};
    private final String[] SDK_23_MODELS = new String[]{"ZERO", "NOBLE", "ZEN"};
    private boolean mIsEnabled = false;

    Utility() {
        if (PRODUCT_NAME != null && SDK_VERSION != null) {
            if (SDK_VERSION.equals("21")) {
                checkModels(this.SDK_21_MODELS);
            } else if (SDK_VERSION.equals("22")) {
                checkModels(this.SDK_22_MODELS);
            } else if (SDK_VERSION.equals("23")) {
                checkModels(this.SDK_23_MODELS);
            }
        }
    }

    public static boolean isFipsTimaEnabled() {
        if (INSTANCE == null) {
            INSTANCE = new Utility();
            if (INSTANCE == null) {
                return false;
            }
        }
        return INSTANCE.mIsEnabled;
    }

    private void checkModels(String[] modelNames) {
        for (String model : modelNames) {
            if (PRODUCT_NAME.toLowerCase().startsWith(model.toLowerCase())) {
                this.mIsEnabled = true;
                return;
            }
        }
    }
}
