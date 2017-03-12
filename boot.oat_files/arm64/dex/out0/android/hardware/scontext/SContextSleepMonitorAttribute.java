package android.hardware.scontext;

import android.os.Bundle;
import android.util.Log;

@Deprecated
public class SContextSleepMonitorAttribute extends SContextAttribute {
    private static final String TAG = "SContextSleepMonitorAttribute";
    private int mSamplingInterval = 100;
    private int mSensibility = 80;

    SContextSleepMonitorAttribute() {
        setAttribute();
    }

    public SContextSleepMonitorAttribute(int sensibility, int samplingInterval) {
        this.mSensibility = sensibility;
        this.mSamplingInterval = samplingInterval;
        setAttribute();
    }

    public int getSensibility() {
        return this.mSensibility;
    }

    public int getSamplingInterval() {
        return this.mSamplingInterval;
    }

    boolean checkAttribute() {
        if (this.mSensibility < 0) {
            Log.e(TAG, "The sensibility is wrong.");
            return false;
        } else if (this.mSamplingInterval >= 0) {
            return true;
        } else {
            Log.e(TAG, "The sampling interval is wrong.");
            return false;
        }
    }

    private void setAttribute() {
        Bundle attribute = new Bundle();
        attribute.putInt("sensibility", this.mSensibility);
        attribute.putInt("sampling_interval", this.mSamplingInterval);
        super.setAttribute(29, attribute);
    }
}
