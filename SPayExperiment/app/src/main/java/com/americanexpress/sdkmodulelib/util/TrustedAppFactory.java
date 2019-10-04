/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 */
package com.americanexpress.sdkmodulelib.util;

import com.americanexpress.sdkmodulelib.util.TrustedApp;
import com.americanexpress.sdkmodulelib.util.TrustedAppImpl;

public class TrustedAppFactory {
    private static boolean mockTrustedApp = false;
    private static TrustedApp trustedApp = null;

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static TrustedApp getTrustedApp() {
        if (mockTrustedApp) {
            if (trustedApp != null) return trustedApp;
            trustedApp = (TrustedApp)Class.forName((String)"com.americanexpress.sdkmodule.test.MockTrustedAppImpl").newInstance();
            return trustedApp;
        }
        if (trustedApp != null) return trustedApp;
        Class<TrustedAppFactory> class_ = TrustedAppFactory.class;
        synchronized (TrustedAppFactory.class) {
            trustedApp = new TrustedAppImpl();
            // ** MonitorExit[var1] (shouldn't be in output)
            return trustedApp;
        }
    }

    public static boolean isMockTrustedApp() {
        return mockTrustedApp;
    }
}

