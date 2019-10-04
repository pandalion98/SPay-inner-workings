/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.discover.tzsvc;

import com.samsung.android.spaytzsvc.api.TAStruct;
import javolution.io.Struct;

public class d
extends TAStruct {
    Struct.Unsigned32 return_code = new Struct.Unsigned32();
    Struct.UTF8String yY = new Struct.UTF8String(1024);

    public String eA() {
        return this.yY.get();
    }

    public long getReturnCode() {
        return this.return_code.get();
    }

    public boolean validate() {
        return this.return_code.get() == 0L;
    }
}

