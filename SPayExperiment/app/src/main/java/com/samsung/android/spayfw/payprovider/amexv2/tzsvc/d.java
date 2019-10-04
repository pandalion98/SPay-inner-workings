/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.util.Log
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.amexv2.tzsvc;

import android.content.Context;
import android.util.Log;
import com.samsung.android.spayfw.payprovider.amexv2.tzsvc.c;
import com.samsung.android.spayfw.payprovider.amexv2.tzsvc.f;

public class d {
    public static f cB() {
        Log.d((String)"AmexTAControllerFactory", (String)"getAmexTAController");
        return c.cz();
    }

    public static Context getApplicationContext() {
        Log.d((String)"AmexTAControllerFactory", (String)"getApplicationContext");
        return c.cz().getContext();
    }
}

