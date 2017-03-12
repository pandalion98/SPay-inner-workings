package com.samsung.android.telephony;

import android.telephony.TelephonyManager;

public class TelephonyWrapper {
    static final String LOG_TAG = "TelephonyWrapper";

    private TelephonyWrapper() {
    }

    public boolean needsOtaServiceProvisioning() {
        return TelephonyManager.getDefault().needsOtaServiceProvisioning();
    }
}
