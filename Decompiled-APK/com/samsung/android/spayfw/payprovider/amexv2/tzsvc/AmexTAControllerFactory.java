package com.samsung.android.spayfw.payprovider.amexv2.tzsvc;

import android.content.Context;
import android.util.Log;

/* renamed from: com.samsung.android.spayfw.payprovider.amexv2.tzsvc.d */
public class AmexTAControllerFactory {
    public static IAmexTAController cB() {
        Log.d("AmexTAControllerFactory", "getAmexTAController");
        return AmexTAController.cz();
    }

    public static Context getApplicationContext() {
        Log.d("AmexTAControllerFactory", "getApplicationContext");
        return AmexTAController.cz().getContext();
    }
}
