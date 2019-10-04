/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.krcc.tzsvc;

import com.samsung.android.spayfw.payprovider.krcc.tzsvc.KrccCommands;
import com.samsung.android.spayfw.payprovider.krcc.tzsvc.KrccTAController;
import com.samsung.android.spaytzsvc.api.TACommands;
import com.samsung.android.spaytzsvc.api.TAInfo;

public class KrccTAInfo
extends TAInfo {
    public static final String CONFIG_LSI_PATH = "ffffffff000000000000000000000028.mp3";
    public static final String CONFIG_LSI_UUID = "ffffffff000000000000000000000028";
    public static final String CONFIG_QC_PATH = "krcc_pay.mp3";
    public static final String CONFIG_QC_PROCESS = "krcc_pay";
    public static final String CONFIG_QC_ROOT = "/firmware/image";
    public static final int SPAY_TA_TYPE_TEE_KRCC = 5;
    public static final boolean bUsesPinRandom = false;
    public static final boolean loadFromSystem = true;
    public static final KrccCommands mCommands = new KrccCommands();

    public KrccTAInfo() {
        super(5, "tee", KrccTAController.class, mCommands, CONFIG_LSI_UUID, CONFIG_LSI_PATH, CONFIG_QC_ROOT, CONFIG_QC_PROCESS, CONFIG_QC_PATH, false, true);
    }
}

