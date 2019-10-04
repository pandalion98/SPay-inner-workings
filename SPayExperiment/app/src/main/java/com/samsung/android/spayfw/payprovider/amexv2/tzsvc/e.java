/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Build
 *  java.lang.Class
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.amexv2.tzsvc;

import android.os.Build;
import com.samsung.android.spayfw.payprovider.amexv2.tzsvc.AmexCommands;
import com.samsung.android.spayfw.payprovider.amexv2.tzsvc.c;
import com.samsung.android.spaytzsvc.api.TACommands;
import com.samsung.android.spaytzsvc.api.TAInfo;

public class e
extends TAInfo {
    private static String PINRANDOMFILE_POSTFIX;
    private static final boolean bQC;
    public static final AmexCommands rU;

    static {
        bQC = Build.BOARD.matches("(?i)msm[a-z0-9]*");
        PINRANDOMFILE_POSTFIX = ".dat";
        rU = new AmexCommands();
    }

    public e() {
        super(3, "tee", c.class, rU, "ffffffff000000000000000000000026", "ffffffff000000000000000000000026_v2.mp3", "/firmware/image", "aexp_pay", "aexp_payv2.mp3", true);
    }

    @Override
    public String getPinRandomFileName() {
        if (bQC) {
            String string = "aexp_pay" + PINRANDOMFILE_POSTFIX;
            com.samsung.android.spayfw.b.c.d("AmexTAInfo", "File name for QC is " + string);
            return string;
        }
        String string = "ffffffff000000000000000000000026".substring(-2 + "ffffffff000000000000000000000026".length(), "ffffffff000000000000000000000026".length()) + PINRANDOMFILE_POSTFIX;
        com.samsung.android.spayfw.b.c.d("AmexTAInfo", "File name for Tbase is " + string);
        return string;
    }
}

