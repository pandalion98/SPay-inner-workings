/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.util.Log
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 */
package com.mastercard.mobile_api.utils.logs;

import android.util.Log;
import com.mastercard.mobile_api.utils.logs.Logger;

public class AndroidMCBPLogger
implements Logger {
    private static final boolean DEBUG_LOG = true;
    String tag;

    public AndroidMCBPLogger(Object object) {
        if (object == null) {
            this.tag = "DefaultLog";
            return;
        }
        this.tag = object.getClass().getName();
    }

    public AndroidMCBPLogger(String string) {
        this.tag = string;
    }

    @Override
    public void d(String string) {
        Log.d((String)this.tag, (String)string);
    }

    @Override
    public void e(String string) {
        Log.e((String)this.tag, (String)string);
    }

    @Override
    public void i(String string) {
        Log.i((String)this.tag, (String)string);
    }
}

