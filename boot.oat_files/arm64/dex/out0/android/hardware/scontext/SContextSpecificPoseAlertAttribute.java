package android.hardware.scontext;

import android.os.Bundle;
import android.util.Log;

public class SContextSpecificPoseAlertAttribute extends SContextAttribute {
    private static final String TAG = "SContextSpecificPoseAlertAttribute";
    private int mMaximumAngle = 90;
    private int mMinimumAngle = -90;
    private int mMovingThrs = 1;
    private int mRetentionTime = 1;

    SContextSpecificPoseAlertAttribute() {
        setAttribute();
    }

    public SContextSpecificPoseAlertAttribute(int retentionTime, int minimumAngle, int maximumAngle, int movingThrs) {
        this.mRetentionTime = retentionTime;
        this.mMinimumAngle = minimumAngle;
        this.mMaximumAngle = maximumAngle;
        this.mMovingThrs = movingThrs;
        setAttribute();
    }

    boolean checkAttribute() {
        if (this.mRetentionTime < 0) {
            Log.e(TAG, "The retention time is wrong.");
            return false;
        } else if (this.mMinimumAngle < -90 || this.mMinimumAngle > 90) {
            Log.e(TAG, "The minimum angle is wrong. The angle must be between -90 and 90.");
            return false;
        } else if (this.mMaximumAngle < -90 || this.mMaximumAngle > 90) {
            Log.e(TAG, "The maximum angle is wrong. The angle must be between -90 and 90.");
            return false;
        } else if (this.mMinimumAngle > this.mMaximumAngle) {
            Log.e(TAG, "The minimum angle must be less than the maximum angle.");
            return false;
        } else if (this.mMovingThrs >= 0) {
            return true;
        } else {
            Log.e(TAG, "The moving threshold is wrong.");
            return false;
        }
    }

    private void setAttribute() {
        Bundle attribute = new Bundle();
        attribute.putInt("retention_time", this.mRetentionTime);
        attribute.putInt("minimum_angle", this.mMinimumAngle);
        attribute.putInt("maximum_angle", this.mMaximumAngle);
        attribute.putInt("moving_thrs", this.mMovingThrs);
        super.setAttribute(28, attribute);
    }
}
