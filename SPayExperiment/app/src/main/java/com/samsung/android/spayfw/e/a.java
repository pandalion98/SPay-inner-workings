/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.util.Log
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.e;

import android.util.Log;

public class a {
    public static final void fg() {
        if (com.samsung.android.spayfw.e.b.a.fT()) {
            Log.d((String)"CcmWrapper", (String)"it's se device");
            com.samsung.android.spayfw.d.a.fg();
            return;
        }
        Log.d((String)"CcmWrapper", (String)"it's non se device");
        com.samsung.android.spayfw.c.a.fg();
    }
}

