package com.samsung.android.sdk.multiwindow;

import android.graphics.Rect;

public class SMultiWindowListener {

    public interface ExitListener {
        boolean onWindowExit();
    }

    public interface StateChangeListener2 {
        void onStateChanged(int i);
    }

    public interface StateChangeListener {
        void onModeChanged(boolean z);

        void onSizeChanged(Rect rect);

        void onZoneChanged(int i);
    }
}
