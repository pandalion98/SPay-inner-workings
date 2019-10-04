/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.String
 */
package com.samsung.android.spaytui;

import com.samsung.android.spaytui.SpayTuiTACommands;
import com.samsung.android.spaytui.SpayTuiTAController;
import com.samsung.android.spaytzsvc.api.TACommands;
import com.samsung.android.spaytzsvc.api.TAInfo;

public class SpayTuiTAInfo
extends TAInfo {
    public static final int SPAY_TA_TYPE_TEE_TUI = 257;
    public static final String TA_INFO_LSI_PATH = "ffffffff000000000000000000000029.mp3";
    public static final String TA_INFO_LSI_UUID = "ffffffff000000000000000000000029";
    public static final String TA_INFO_QC_PATH = "pay_auth.mp3";
    public static final String TA_INFO_QC_PROCESS = "pay_auth";
    public static final String TA_INFO_QC_ROOT = "/firmware/image";
    public static final boolean bUsesPinRandom;
    public static final SpayTuiTACommands mCommands;

    static {
        mCommands = new SpayTuiTACommands();
    }

    public SpayTuiTAInfo() {
        super(257, "tee", SpayTuiTAController.class, mCommands, TA_INFO_LSI_UUID, TA_INFO_LSI_PATH, TA_INFO_QC_ROOT, TA_INFO_QC_PROCESS, TA_INFO_QC_PATH, false);
    }
}

