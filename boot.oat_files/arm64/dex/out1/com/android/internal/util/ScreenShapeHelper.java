package com.android.internal.util;

import android.content.res.Resources;
import android.os.Build;
import android.os.SystemProperties;
import android.view.ViewRootImpl;
import com.android.internal.R;

public class ScreenShapeHelper {
    private static final boolean IS_EMULATOR = Build.HARDWARE.contains("goldfish");

    public static int getWindowOutsetBottomPx(Resources resources) {
        if (IS_EMULATOR) {
            return SystemProperties.getInt(ViewRootImpl.PROPERTY_EMULATOR_WIN_OUTSET_BOTTOM_PX, 0);
        }
        return resources.getInteger(R.integer.config_windowOutsetBottom);
    }
}
