package com.samsung.android.spayfw.payprovider.amexv2.tzsvc;

import android.os.Build;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spaytui.SpayTuiTAInfo;
import com.samsung.android.spaytzsvc.api.TAInfo;

/* renamed from: com.samsung.android.spayfw.payprovider.amexv2.tzsvc.e */
public class AmexTAInfo extends TAInfo {
    private static String PINRANDOMFILE_POSTFIX;
    private static final boolean bQC;
    public static final AmexCommands rU;

    static {
        bQC = Build.BOARD.matches("(?i)msm[a-z0-9]*");
        PINRANDOMFILE_POSTFIX = ".dat";
        rU = new AmexCommands();
    }

    public AmexTAInfo() {
        super(3, TAInfo.SPAY_TA_TECH_TEE, AmexTAController.class, rU, "ffffffff000000000000000000000026", "ffffffff000000000000000000000026_v2.mp3", SpayTuiTAInfo.TA_INFO_QC_ROOT, "aexp_pay", "aexp_payv2.mp3", true);
    }

    public String getPinRandomFileName() {
        if (bQC) {
            String str = "aexp_pay" + PINRANDOMFILE_POSTFIX;
            Log.m285d("AmexTAInfo", "File name for QC is " + str);
            return str;
        }
        str = "ffffffff000000000000000000000026".substring("ffffffff000000000000000000000026".length() - 2, "ffffffff000000000000000000000026".length()) + PINRANDOMFILE_POSTFIX;
        Log.m285d("AmexTAInfo", "File name for Tbase is " + str);
        return str;
    }
}
