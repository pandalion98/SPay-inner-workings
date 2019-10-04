/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.util.Log
 *  java.lang.Object
 *  java.lang.String
 *  javax.net.ssl.X509KeyManager
 */
package com.samsung.android.spayfw.e;

import android.util.Log;
import com.samsung.android.spayfw.e.b.a;
import javax.net.ssl.X509KeyManager;

public class b {
    public static X509KeyManager fh() {
        if (a.fT()) {
            Log.e((String)"DcmKeyManagerWrapper", (String)"it's se device, should not comming here");
            return null;
        }
        Log.d((String)"DcmKeyManagerWrapper", (String)"it's non se device, use DcmKeyManager");
        return com.samsung.android.spayfw.c.b.fh();
    }
}

