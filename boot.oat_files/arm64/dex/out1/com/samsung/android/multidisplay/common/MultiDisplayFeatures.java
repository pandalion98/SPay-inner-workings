package com.samsung.android.multidisplay.common;

import android.os.Debug;

public class MultiDisplayFeatures {
    public static final boolean DEBUG_MULTIDISPLAY = false;
    public static final boolean MULTIDISPLAY_ENABLED = false;
    public static final boolean SAFE_DEBUG;

    static {
        boolean z = true;
        if (Debug.isProductShip() == 1) {
            z = false;
        }
        SAFE_DEBUG = z;
    }
}
