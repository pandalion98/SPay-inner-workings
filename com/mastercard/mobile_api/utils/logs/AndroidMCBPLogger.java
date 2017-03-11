package com.mastercard.mobile_api.utils.logs;

import android.util.Log;

public class AndroidMCBPLogger implements Logger {
    private static final boolean DEBUG_LOG = true;
    String tag;

    public AndroidMCBPLogger(String str) {
        this.tag = str;
    }

    public AndroidMCBPLogger(Object obj) {
        if (obj == null) {
            this.tag = "DefaultLog";
        } else {
            this.tag = obj.getClass().getName();
        }
    }

    public void m139i(String str) {
        Log.i(this.tag, str);
    }

    public void m138e(String str) {
        Log.e(this.tag, str);
    }

    public void m137d(String str) {
        Log.d(this.tag, str);
    }
}
