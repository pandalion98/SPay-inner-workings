package com.samsung.android.spayfw.fraud;

import android.content.Context;

/* renamed from: com.samsung.android.spayfw.fraud.e */
public class FraudModule {
    private static Context sContext;
    static boolean sIsInitialized;

    static {
        sIsInitialized = false;
    }

    public static void initialize(Context context) {
        if (!sIsInitialized) {
            ModelCache.initialize(context);
            sContext = context;
            sIsInitialized = true;
        }
    }

    public static Context getContext() {
        return sContext;
    }

    public static FraudRiskMachine m746Y(String str) {
        if (sIsInitialized) {
            return new FraudRiskMachine(str);
        }
        throw new IllegalStateException("You must call FraudModule.initialize() before calling FraudModule.getRiskMachine().");
    }
}
