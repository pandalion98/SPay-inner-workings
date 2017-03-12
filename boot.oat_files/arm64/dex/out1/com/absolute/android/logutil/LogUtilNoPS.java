package com.absolute.android.logutil;

import android.content.Context;
import android.util.Log;

public class LogUtilNoPS extends LogUtil {
    private static final boolean b = false;
    private static final boolean c = true;

    public void start(Context context) {
    }

    public void logMessage(int i, String str) {
        switch (i) {
            case 2:
                Log.v(a, str);
                return;
            case 4:
                Log.i(a, str);
                return;
            case 5:
                Log.w(a, str);
                return;
            case 6:
                Log.e(a, str);
                return;
            default:
                return;
        }
    }
}
