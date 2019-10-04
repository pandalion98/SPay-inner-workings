/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.String
 */
package com.samsung.android.spayfw.cncc;

import com.samsung.android.spayfw.cncc.CNCCCommands;
import com.samsung.android.spayfw.cncc.CNCCTAController;
import com.samsung.android.spaytzsvc.api.TACommands;
import com.samsung.android.spaytzsvc.api.TAInfo;

public class CNCCTAInfo
extends TAInfo {
    public static final String CONFIG_LSI_PATH = "ffffffff000000000000000000000032.mp3";
    public static final String CONFIG_LSI_UUID = "ffffffff000000000000000000000032";
    public static final String CONFIG_QC_PATH = "cncc_pay.mp3";
    public static final String CONFIG_QC_PROCESS = "cncc_pay";
    public static final String CONFIG_QC_ROOT = "/firmware/image";
    public static final int SPAY_TA_TYPE_TEE_CNCC = 9;
    public static final boolean bUsesPinRandom;
    public static final CNCCCommands mCommands;

    static {
        mCommands = new CNCCCommands();
    }

    public CNCCTAInfo() {
        super(9, "tee", CNCCTAController.class, mCommands, CONFIG_LSI_UUID, CONFIG_LSI_PATH, CONFIG_QC_ROOT, CONFIG_QC_PROCESS, CONFIG_QC_PATH, false);
    }
}

