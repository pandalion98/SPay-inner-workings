package android.hardware.scontext;

import android.os.Bundle;
import android.util.Log;

public class SContextEnvironmentAdaptiveDisplayAttribute extends SContextAttribute {
    private static final String TAG = "SContextEnvironmentAdaptiveDisplayAttribute";
    private float mColorThreshold = 0.07f;
    private int mDuration = 35;

    SContextEnvironmentAdaptiveDisplayAttribute() {
        setAttribute();
    }

    public SContextEnvironmentAdaptiveDisplayAttribute(float colorThreshold, int duration) {
        this.mColorThreshold = colorThreshold;
        this.mDuration = duration;
        setAttribute();
    }

    boolean checkAttribute() {
        if (this.mColorThreshold < 0.0f) {
            Log.e(TAG, "The color threshold value is wrong.");
            return false;
        } else if (this.mDuration >= 0 && this.mDuration <= 255) {
            return true;
        } else {
            Log.e(TAG, "The duration value is wrong.");
            return false;
        }
    }

    private void setAttribute() {
        Bundle attribute = new Bundle();
        attribute.putFloat("color_threshold", this.mColorThreshold);
        attribute.putInt("duration", this.mDuration);
        Log.d(TAG, "setAttribute() mColorThreshold : " + attribute.getFloat("color_threshold"));
        Log.d(TAG, "setAttribute() mDuration : " + attribute.getInt("duration"));
        super.setAttribute(44, attribute);
    }
}
