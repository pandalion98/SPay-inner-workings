package android.hardware.scontext;

import android.os.Bundle;
import android.util.Log;

public class SContextHallSensorAttribute extends SContextAttribute {
    private static final String TAG = "SContextHallSensorAttribute";
    private int mDisplayStatus = 0;

    SContextHallSensorAttribute() {
        setAttribute();
    }

    public SContextHallSensorAttribute(int displayStatus) {
        this.mDisplayStatus = displayStatus;
        setAttribute();
        Log.d(TAG, "constructor + " + displayStatus);
    }

    public int getDisplayStatus() {
        return this.mDisplayStatus;
    }

    boolean checkAttribute() {
        if (this.mDisplayStatus >= 0) {
            return true;
        }
        Log.e(TAG, "The display status is wrong.");
        return false;
    }

    private void setAttribute() {
        Bundle attribute = new Bundle();
        attribute.putInt("display_status", this.mDisplayStatus);
        Log.d(TAG, "hall sensor status   + " + attribute.getInt("display_status"));
        super.setAttribute(43, attribute);
    }
}
