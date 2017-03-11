package com.samsung.android.spayfw.p008e;

import android.util.Log;
import com.samsung.android.spayfw.p003c.SdlClientCertificateManager;
import com.samsung.android.spayfw.p006d.SeClientCertificateManager;
import com.samsung.android.spayfw.p008e.p010b.Platformutils;

/* renamed from: com.samsung.android.spayfw.e.a */
public class ClientCertificateManagerWrapper {
    public static final void fg() {
        if (Platformutils.fT()) {
            Log.d("CcmWrapper", "it's se device");
            SeClientCertificateManager.fg();
            return;
        }
        Log.d("CcmWrapper", "it's non se device");
        SdlClientCertificateManager.fg();
    }
}
