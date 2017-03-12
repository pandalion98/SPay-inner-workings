package com.samsung.android.sdk.dualscreen;

import com.samsung.android.dualscreen.DualScreen;

public class SDualScreenListener {

    public interface ExpandRequestListener {
        void onExpandRequested(int i);
    }

    public interface ScreenChangeListener {
        void onScreenChanged(DualScreen dualScreen);
    }

    public interface ScreenFocusChangeListener {
        void onScreenFocusChanged(DualScreen dualScreen);
    }

    public interface ShrinkRequestListener {
        void onShrinkRequested(DualScreen dualScreen, int i);
    }
}
