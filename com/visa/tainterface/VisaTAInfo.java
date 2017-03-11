package com.visa.tainterface;

import com.samsung.android.spaytui.SpayTuiTAInfo;
import com.samsung.android.spaytzsvc.api.TAInfo;

/* renamed from: com.visa.tainterface.c */
public class VisaTAInfo extends TAInfo {
    public static final VisaCommands MV;

    static {
        MV = new VisaCommands();
    }

    public VisaTAInfo() {
        super(1, TAInfo.SPAY_TA_TECH_TEE, VisaTAController.class, MV, "ffffffff00000000000000000000001c", "ffffffff00000000000000000000001c.mp3", SpayTuiTAInfo.TA_INFO_QC_ROOT, "visa_pay", "visa_pay.mp3", true);
    }
}
