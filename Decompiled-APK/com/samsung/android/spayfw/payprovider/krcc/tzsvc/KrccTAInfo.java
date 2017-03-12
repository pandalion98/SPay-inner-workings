package com.samsung.android.spayfw.payprovider.krcc.tzsvc;

import com.samsung.android.spaytzsvc.api.TAInfo;

public class KrccTAInfo extends TAInfo {
    public static final String CONFIG_LSI_PATH = "ffffffff000000000000000000000028.mp3";
    public static final String CONFIG_LSI_UUID = "ffffffff000000000000000000000028";
    public static final String CONFIG_QC_PATH = "krcc_pay.mp3";
    public static final String CONFIG_QC_PROCESS = "krcc_pay";
    public static final String CONFIG_QC_ROOT = "/firmware/image";
    public static final int SPAY_TA_TYPE_TEE_KRCC = 5;
    public static final boolean bUsesPinRandom = false;
    public static final boolean loadFromSystem = true;
    public static final KrccCommands mCommands;

    static {
        mCommands = new KrccCommands();
    }

    public KrccTAInfo() {
        super(SPAY_TA_TYPE_TEE_KRCC, TAInfo.SPAY_TA_TECH_TEE, KrccTAController.class, mCommands, CONFIG_LSI_UUID, CONFIG_LSI_PATH, CONFIG_QC_ROOT, CONFIG_QC_PROCESS, CONFIG_QC_PATH, bUsesPinRandom, loadFromSystem);
    }
}
