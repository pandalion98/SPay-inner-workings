package android.hardware.scontext;

import android.os.Bundle;
import android.util.Log;

public class SContextDevicePhysicalContextMonitorAttribute extends SContextAttribute {
    private static int DEVICE_PHYSICAL_CONTEXT_MONITOR_DATA = 1;
    private static int DEVICE_PHYSICAL_CONTEXT_MONITOR_MODE = 2;
    private static final String TAG = "SContextDevicePhysicalContextMonitorAttribute";
    private int mData = DEVICE_PHYSICAL_CONTEXT_MONITOR_DATA;
    private int mMode = DEVICE_PHYSICAL_CONTEXT_MONITOR_MODE;

    SContextDevicePhysicalContextMonitorAttribute() {
        setAttribute();
    }

    public SContextDevicePhysicalContextMonitorAttribute(int mode, int data) {
        this.mMode = mode;
        this.mData = data;
        setAttribute();
    }

    boolean checkAttribute() {
        if (this.mMode < 0) {
            Log.d(TAG, "Mode value is wrong!!");
            return false;
        } else if (this.mData >= 0) {
            return true;
        } else {
            Log.d(TAG, "Data value is wrong!!");
            return false;
        }
    }

    private void setAttribute() {
        Bundle attribute = new Bundle();
        attribute.putInt("dpcm_mode", this.mMode);
        attribute.putInt("dpcm_data", this.mData);
        super.setAttribute(51, attribute);
    }
}
