package android.hardware.scontext;

import android.os.Bundle;
import android.util.Log;

public class SContextAutoRotationAttribute extends SContextAttribute {
    private static final String TAG = "SContextAutoRotationAttribute";
    private int mDeviceType = 0;

    SContextAutoRotationAttribute() {
        setAttribute();
    }

    public SContextAutoRotationAttribute(int deviceType) {
        this.mDeviceType = deviceType;
        setAttribute();
    }

    boolean checkAttribute() {
        if (this.mDeviceType >= 0 && this.mDeviceType <= 4) {
            return true;
        }
        Log.e(TAG, "The device type is wrong.");
        return false;
    }

    private void setAttribute() {
        Bundle attribute = new Bundle();
        attribute.putInt("device_type", this.mDeviceType);
        super.setAttribute(6, attribute);
    }
}
