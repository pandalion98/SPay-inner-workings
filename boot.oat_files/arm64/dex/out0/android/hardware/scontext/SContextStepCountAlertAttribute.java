package android.hardware.scontext;

import android.os.Bundle;
import android.util.Log;

public class SContextStepCountAlertAttribute extends SContextAttribute {
    private static final String TAG = "SContextStepCountAlertAttribute";
    private int mStepCount = 10;

    SContextStepCountAlertAttribute() {
        setAttribute();
    }

    public SContextStepCountAlertAttribute(int stepCount) {
        this.mStepCount = stepCount;
        setAttribute();
    }

    public int getStepCount() {
        return this.mStepCount;
    }

    boolean checkAttribute() {
        if (this.mStepCount >= 0) {
            return true;
        }
        Log.e(TAG, "The step count is wrong.");
        return false;
    }

    private void setAttribute() {
        Bundle attribute = new Bundle();
        attribute.putInt("step_count", this.mStepCount);
        super.setAttribute(3, attribute);
    }
}
