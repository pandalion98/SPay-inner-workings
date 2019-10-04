/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.util.Log
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.e;

import android.content.Context;
import android.util.Log;
import com.samsung.android.spayfw.e.b.a;

public class c {
    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void ar(Context context) {
        Class<c> class_ = c.class;
        synchronized (c.class) {
            if (a.fT()) {
                Log.d((String)"NfcControllerWrapper", (String)"se device don't suppport this api");
            } else {
                Log.d((String)"NfcControllerWrapper", (String)"it's non se device");
                com.samsung.android.spayfw.c.c.R(context);
            }
            // ** MonitorExit[var5_1] (shouldn't be in output)
            return;
        }
    }

    public static void fi() {
        if (a.fT()) {
            Log.d((String)"NfcControllerWrapper", (String)"se device don't suppport this api");
            return;
        }
        Log.d((String)"NfcControllerWrapper", (String)"it's non se device");
        com.samsung.android.spayfw.c.c.fi();
    }

    public static void fj() {
        if (a.fT()) {
            Log.d((String)"NfcControllerWrapper", (String)"se device don't suppport this api");
            return;
        }
        Log.d((String)"NfcControllerWrapper", (String)"it's non se device");
        com.samsung.android.spayfw.c.c.fj();
    }
}

