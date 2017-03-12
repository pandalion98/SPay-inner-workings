package android.hardware.scontext;

import android.os.Bundle;
import android.util.Log;

public class SContextAutoBrightnessAttribute extends SContextAttribute {
    private static int MODE_CONFIGURATION = 1;
    private static int MODE_DEVICE_MODE = 0;
    private static final String TAG = "SContextAutoBrightnessAttribute";
    private int mDeviceMode = 0;
    private byte[] mLuminanceTable = null;
    private int mMode = -1;

    SContextAutoBrightnessAttribute() {
        setAttribute();
    }

    public SContextAutoBrightnessAttribute(int deviceMode) {
        this.mDeviceMode = deviceMode;
        this.mMode = MODE_DEVICE_MODE;
        setAttribute();
    }

    public SContextAutoBrightnessAttribute(byte[] luminanceTable) {
        this.mLuminanceTable = luminanceTable;
        this.mMode = MODE_CONFIGURATION;
        setAttribute();
    }

    boolean checkAttribute() {
        if (this.mMode == MODE_DEVICE_MODE) {
            if (this.mDeviceMode < 0 || this.mDeviceMode > 2) {
                Log.e(TAG, "The device mode is wrong.");
                return false;
            }
        } else if (this.mMode == MODE_CONFIGURATION && this.mLuminanceTable == null) {
            Log.e(TAG, "The luminance configration data is null.");
            return false;
        }
        return true;
    }

    private void setAttribute() {
        Bundle attribute = new Bundle();
        attribute.putInt("mode", this.mMode);
        if (this.mMode == MODE_CONFIGURATION) {
            attribute.putByteArray("luminance_config_data", this.mLuminanceTable);
        } else if (this.mMode == MODE_DEVICE_MODE) {
            attribute.putInt("device_mode", this.mDeviceMode);
        }
        super.setAttribute(39, attribute);
    }
}
