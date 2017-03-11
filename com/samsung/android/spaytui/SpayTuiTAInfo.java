package com.samsung.android.spaytui;

import com.samsung.android.spaytzsvc.api.TAInfo;

public class SpayTuiTAInfo extends TAInfo {
    public static final int SPAY_TA_TYPE_TEE_TUI = 257;
    public static final String TA_INFO_LSI_PATH = "ffffffff000000000000000000000029.mp3";
    public static final String TA_INFO_LSI_UUID = "ffffffff000000000000000000000029";
    public static final String TA_INFO_QC_PATH = "pay_auth.mp3";
    public static final String TA_INFO_QC_PROCESS = "pay_auth";
    public static final String TA_INFO_QC_ROOT = "/firmware/image";
    public static final boolean bUsesPinRandom = false;
    public static final SpayTuiTACommands mCommands;

    static {
        mCommands = new SpayTuiTACommands();
    }

    public SpayTuiTAInfo() {
        super((int) SPAY_TA_TYPE_TEE_TUI, TAInfo.SPAY_TA_TECH_TEE, SpayTuiTAController.class, mCommands, TA_INFO_LSI_UUID, TA_INFO_LSI_PATH, TA_INFO_QC_ROOT, TA_INFO_QC_PROCESS, TA_INFO_QC_PATH, false);
    }
}
