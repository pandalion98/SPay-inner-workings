package android.hardware.scontext;

import android.os.Bundle;
import android.util.Log;

public class SContextFlatMotionForTableModeAttribute extends SContextAttribute {
    private static final String TAG = "SContextFlatMotionForTableModeAttribute";
    private int mDuration = 500;

    SContextFlatMotionForTableModeAttribute() {
        setAttribute();
    }

    public SContextFlatMotionForTableModeAttribute(int duration) {
        this.mDuration = duration;
        setAttribute();
    }

    boolean checkAttribute() {
        if (this.mDuration >= 0) {
            return true;
        }
        Log.e(TAG, "The duration is wrong.");
        return false;
    }

    private void setAttribute() {
        Bundle attribute = new Bundle();
        attribute.putInt("duration", this.mDuration);
        super.setAttribute(36, attribute);
    }
}
