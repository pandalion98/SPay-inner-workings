package android.hardware.scontext;

import android.os.Bundle;
import android.util.Log;

public class SContextShakeMotionAttribute extends SContextAttribute {
    private static final String TAG = "SContextShakeMotionAttribute";
    private int mDuration = 800;
    private int mStrength = 2;

    SContextShakeMotionAttribute() {
        setAttribute();
    }

    public SContextShakeMotionAttribute(int strength, int duration) {
        this.mStrength = strength;
        this.mDuration = duration;
        setAttribute();
    }

    boolean checkAttribute() {
        if (this.mStrength < 0) {
            Log.e(TAG, "The strength is wrong.");
            return false;
        } else if (this.mDuration >= 0) {
            return true;
        } else {
            Log.e(TAG, "The duration is wrong.");
            return false;
        }
    }

    private void setAttribute() {
        Bundle attribute = new Bundle();
        attribute.putInt("strength", this.mStrength);
        attribute.putInt("duration", this.mDuration);
        super.setAttribute(12, attribute);
    }
}
