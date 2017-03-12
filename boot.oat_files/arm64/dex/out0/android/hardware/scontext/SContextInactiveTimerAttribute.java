package android.hardware.scontext;

import android.media.tv.TvContract;
import android.os.Bundle;
import android.util.Log;

public class SContextInactiveTimerAttribute extends SContextAttribute {
    private static final String TAG = "SContextInactiveTimerAttribute";
    private int mAlertCount = 1;
    private int mDeviceType = 1;
    private int mDuration = 3600;
    private int mEndTime = 1500;
    private int mStartTime = 1500;

    SContextInactiveTimerAttribute() {
        setAttribute();
    }

    public SContextInactiveTimerAttribute(int deviceType, int duration, int alertCount, int startTime, int endTime) {
        this.mDeviceType = deviceType;
        this.mDuration = duration;
        this.mAlertCount = alertCount;
        this.mStartTime = startTime;
        this.mEndTime = endTime;
        setAttribute();
    }

    boolean checkAttribute() {
        if (this.mDeviceType != 1 && this.mDeviceType != 2) {
            Log.e(TAG, "The deivce type is wrong.");
            return false;
        } else if (this.mDuration < 0) {
            Log.e(TAG, "The duration is wrong.");
            return false;
        } else if (this.mAlertCount < 0) {
            Log.e(TAG, "The alert count is wrong.");
            return false;
        } else if (this.mStartTime < 0) {
            Log.e(TAG, "The start time is wrong.");
            return false;
        } else if (this.mEndTime >= 0) {
            return true;
        } else {
            Log.e(TAG, "The end time is wrong.");
            return false;
        }
    }

    private void setAttribute() {
        Bundle attribute = new Bundle();
        attribute.putInt("device_type", this.mDeviceType);
        attribute.putInt("duration", this.mDuration);
        attribute.putInt("alert_count", this.mAlertCount);
        attribute.putInt(TvContract.PARAM_START_TIME, this.mStartTime);
        attribute.putInt(TvContract.PARAM_END_TIME, this.mEndTime);
        super.setAttribute(35, attribute);
    }
}
