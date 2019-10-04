/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.discover.tzsvc;

import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTACommands;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.b;
import com.samsung.android.spaytzsvc.api.TACommands;
import com.samsung.android.spaytzsvc.api.TAInfo;

public class c
extends TAInfo {
    public static final DcTACommands yW = new DcTACommands();

    public c() {
        super(8, "tee", b.class, yW, "ffffffff000000000000000000000031", "ffffffff000000000000000000000031.mp3", "/firmware/image", "dc_pay", "dc_pay.mp3", true);
    }
}

