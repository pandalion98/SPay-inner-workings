/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.util.Log
 *  com.samsung.android.knox.ccm.SemClientCertificateManager
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.d;

import android.util.Log;
import com.samsung.android.knox.ccm.SemClientCertificateManager;

public class a {
    private static SemClientCertificateManager BA;

    public static final void fg() {
        Log.e((String)"CcmForSe", (String)"start ccmSelfCheck");
        if (BA == null) {
            BA = new SemClientCertificateManager();
        }
        boolean bl = BA.setDefaultClientCertificateManagerProfile();
        Log.d((String)"CcmForSe", (String)("setDefaultCCMProfile : " + bl));
    }
}

