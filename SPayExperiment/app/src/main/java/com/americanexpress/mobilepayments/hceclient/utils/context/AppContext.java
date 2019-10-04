/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  java.lang.Class
 *  java.lang.Object
 */
package com.americanexpress.mobilepayments.hceclient.utils.context;

import android.content.Context;
import com.samsung.android.spayfw.payprovider.amexv2.tzsvc.d;

public class AppContext {
    private static AppContext appContext;
    private Context context;

    private AppContext() {
    }

    private AppContext(Context context) {
        this.context = context;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static AppContext createOnlyInstance(Context context) {
        Class<AppContext> class_ = AppContext.class;
        synchronized (AppContext.class) {
            if (appContext != null) return appContext;
            if (context == null) return appContext;
            appContext = new AppContext(context);
            return appContext;
        }
    }

    public static AppContext getInstance() {
        Class<AppContext> class_ = AppContext.class;
        synchronized (AppContext.class) {
            if (appContext == null) {
                appContext = new AppContext();
            }
            AppContext appContext = AppContext.appContext;
            // ** MonitorExit[var2] (shouldn't be in output)
            return appContext;
        }
    }

    public Context getContext() {
        return d.getApplicationContext();
    }
}

