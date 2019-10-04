/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.fraud;

import android.content.Context;
import com.samsung.android.spayfw.fraud.f;
import com.samsung.android.spayfw.fraud.g;

public class e {
    private static Context sContext;
    static boolean sIsInitialized;

    static {
        sIsInitialized = false;
    }

    public static f Y(String string) {
        if (!sIsInitialized) {
            throw new IllegalStateException("You must call FraudModule.initialize() before calling FraudModule.getRiskMachine().");
        }
        return new f(string);
    }

    public static Context getContext() {
        return sContext;
    }

    public static void initialize(Context context) {
        if (sIsInitialized) {
            return;
        }
        g.initialize(context);
        sContext = context;
        sIsInitialized = true;
    }
}

