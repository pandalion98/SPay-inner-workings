package com.samsung.android.spayfw.p008e;

import android.content.Context;
import android.util.Log;
import com.samsung.android.spayfw.p003c.SdlNfcController;
import com.samsung.android.spayfw.p008e.p010b.Platformutils;

/* renamed from: com.samsung.android.spayfw.e.c */
public class NfcControllerWrapper {
    public static synchronized void ar(Context context) {
        synchronized (NfcControllerWrapper.class) {
            if (Platformutils.fT()) {
                Log.d("NfcControllerWrapper", "se device don't suppport this api");
            } else {
                Log.d("NfcControllerWrapper", "it's non se device");
                SdlNfcController.m301R(context);
            }
        }
    }

    public static void fi() {
        if (Platformutils.fT()) {
            Log.d("NfcControllerWrapper", "se device don't suppport this api");
            return;
        }
        Log.d("NfcControllerWrapper", "it's non se device");
        SdlNfcController.fi();
    }

    public static void fj() {
        if (Platformutils.fT()) {
            Log.d("NfcControllerWrapper", "se device don't suppport this api");
            return;
        }
        Log.d("NfcControllerWrapper", "it's non se device");
        SdlNfcController.fj();
    }
}
