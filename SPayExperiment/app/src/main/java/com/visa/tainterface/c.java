/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.String
 */
package com.visa.tainterface;

import com.samsung.android.spaytzsvc.api.TACommands;
import com.samsung.android.spaytzsvc.api.TAInfo;
import com.visa.tainterface.VisaCommands;
import com.visa.tainterface.VisaTAController;

public class c
extends TAInfo {
    public static final VisaCommands MV = new VisaCommands();

    public c() {
        super(1, "tee", VisaTAController.class, MV, "ffffffff00000000000000000000001c", "ffffffff00000000000000000000001c.mp3", "/firmware/image", "visa_pay", "visa_pay.mp3", true);
    }
}

