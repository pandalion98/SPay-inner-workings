package com.samsung.android.spayfw.payprovider.amex.tzsvc;

import com.samsung.android.spaytui.SpayTuiTAInfo;
import com.samsung.android.spaytzsvc.api.TAInfo;

public class AmexTAInfo extends TAInfo {
    public static final AmexCommands mCommands;

    static {
        mCommands = new AmexCommands();
    }

    public AmexTAInfo() {
        super(3, TAInfo.SPAY_TA_TECH_TEE, AmexTAController.class, mCommands, "ffffffff000000000000000000000026", "ffffffff000000000000000000000026.mp3", SpayTuiTAInfo.TA_INFO_QC_ROOT, "aexp_pay", "aexp_pay.mp3", true);
    }
}
