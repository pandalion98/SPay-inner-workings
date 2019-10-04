/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.amex.tzsvc;

import com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexCommands;
import com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexTAController;
import com.samsung.android.spaytzsvc.api.TACommands;
import com.samsung.android.spaytzsvc.api.TAInfo;

public class AmexTAInfo
extends TAInfo {
    public static final AmexCommands mCommands = new AmexCommands();

    public AmexTAInfo() {
        super(3, "tee", AmexTAController.class, mCommands, "ffffffff000000000000000000000026", "ffffffff000000000000000000000026.mp3", "/firmware/image", "aexp_pay", "aexp_pay.mp3", true);
    }
}

