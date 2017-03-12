package com.samsung.android.spayfw.payprovider.discover.tzsvc;

import com.samsung.android.spaytui.SpayTuiTAInfo;
import com.samsung.android.spaytzsvc.api.TAInfo;

/* renamed from: com.samsung.android.spayfw.payprovider.discover.tzsvc.c */
public class DcTAInfo extends TAInfo {
    public static final DcTACommands yW;

    static {
        yW = new DcTACommands();
    }

    public DcTAInfo() {
        super(8, TAInfo.SPAY_TA_TECH_TEE, DcTAController.class, yW, "ffffffff000000000000000000000031", "ffffffff000000000000000000000031.mp3", SpayTuiTAInfo.TA_INFO_QC_ROOT, "dc_pay", "dc_pay.mp3", true);
    }
}
