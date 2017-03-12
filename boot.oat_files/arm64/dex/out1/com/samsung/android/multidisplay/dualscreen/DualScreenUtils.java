package com.samsung.android.multidisplay.dualscreen;

import com.samsung.android.dualscreen.DualScreen;

public class DualScreenUtils {
    public static final String TAG = "DualScreenUtils";

    private DualScreenUtils() {
    }

    public static DualScreen displayIdToScreen(int displayId) {
        if (displayId == DualScreen.MAIN.getDisplayId()) {
            return DualScreen.MAIN;
        }
        if (displayId == DualScreen.SUB.getDisplayId()) {
            return DualScreen.SUB;
        }
        if (displayId == DualScreen.FULL.getDisplayId()) {
            return DualScreen.FULL;
        }
        if (displayId == DualScreen.INPUT_METHOD.getDisplayId()) {
            return DualScreen.INPUT_METHOD;
        }
        if (displayId == DualScreen.EXTERNAL.getDisplayId()) {
            return DualScreen.EXTERNAL;
        }
        return DualScreen.UNKNOWN;
    }

    public static boolean hasCoupledTaskFlags(int flags) {
        if ((flags & 7) != 0) {
            return true;
        }
        return false;
    }

    public static int getCoupledTaskFlags() {
        return 7;
    }

    public static boolean isVirtualScreen(int displayId) {
        if (displayIdToScreen(displayId) == DualScreen.FULL) {
            return true;
        }
        return false;
    }

    public static int getSystemWindowLayer() {
        return 41000;
    }
}
