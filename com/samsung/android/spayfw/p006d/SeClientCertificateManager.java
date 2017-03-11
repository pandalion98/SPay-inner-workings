package com.samsung.android.spayfw.p006d;

import android.util.Log;
import com.samsung.android.knox.ccm.SemClientCertificateManager;

/* renamed from: com.samsung.android.spayfw.d.a */
public class SeClientCertificateManager {
    private static SemClientCertificateManager BA;

    public static final void fg() {
        Log.e("CcmForSe", "start ccmSelfCheck");
        if (BA == null) {
            BA = new SemClientCertificateManager();
        }
        Log.d("CcmForSe", "setDefaultCCMProfile : " + BA.setDefaultClientCertificateManagerProfile());
    }
}
