package android.hardware.scontext;

import android.os.Bundle;
import android.util.Log;

public class SContextDualDisplayAngleAttribute extends SContextAttribute {
    private static final String TAG = "SContextDualDisplayAngleAttribute";
    private int mOffAngle = 240;
    private int mOnAngle = 210;

    SContextDualDisplayAngleAttribute() {
        setAttribute();
    }

    public SContextDualDisplayAngleAttribute(int onAngle, int offAngle) {
        this.mOnAngle = onAngle;
        this.mOffAngle = offAngle;
        setAttribute();
    }

    boolean checkAttribute() {
        if (this.mOnAngle < 0 || this.mOnAngle > 360) {
            Log.d(TAG, "Value of onAngle is wrong!!");
            return false;
        } else if (this.mOffAngle < 0 || this.mOffAngle > 360) {
            Log.d(TAG, "Value of offAngle is wrong!!");
            return false;
        } else if (this.mOnAngle <= this.mOffAngle) {
            return true;
        } else {
            Log.d(TAG, "onAngle is above offAngle!!");
            return false;
        }
    }

    private void setAttribute() {
        Bundle attribute = new Bundle();
        attribute.putInt("onAngle", this.mOnAngle);
        attribute.putInt("offAngle", this.mOffAngle);
        Log.d(TAG, "onAngle : " + attribute.getInt("onAngle"));
        Log.d(TAG, "offAngle : " + attribute.getInt("offAngle"));
        super.setAttribute(45, attribute);
    }
}
