package android.view;

import android.view.ViewTreeObserver.OnPreDrawListener;

public class HapticPreDrawListener implements OnPreDrawListener {
    public boolean mSkipHapticCalls = false;

    public boolean onPreDraw() {
        this.mSkipHapticCalls = false;
        return true;
    }
}
