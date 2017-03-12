package com.americanexpress.mobilepayments.hceclient.utils.context;

import android.content.Context;
import com.samsung.android.spayfw.payprovider.amexv2.tzsvc.AmexTAControllerFactory;

public class AppContext {
    private static AppContext appContext;
    private Context context;

    private AppContext() {
    }

    private AppContext(Context context) {
        this.context = context;
    }

    public static synchronized AppContext createOnlyInstance(Context context) {
        AppContext appContext;
        synchronized (AppContext.class) {
            if (appContext == null && context != null) {
                appContext = new AppContext(context);
            }
            appContext = appContext;
        }
        return appContext;
    }

    public static synchronized AppContext getInstance() {
        AppContext appContext;
        synchronized (AppContext.class) {
            if (appContext == null) {
                appContext = new AppContext();
            }
            appContext = appContext;
        }
        return appContext;
    }

    public Context getContext() {
        return AmexTAControllerFactory.getApplicationContext();
    }
}
