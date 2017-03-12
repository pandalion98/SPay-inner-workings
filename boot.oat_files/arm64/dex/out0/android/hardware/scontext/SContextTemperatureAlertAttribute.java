package android.hardware.scontext;

import android.os.Bundle;
import android.util.Log;

@Deprecated
public class SContextTemperatureAlertAttribute extends SContextAttribute {
    private static final String TAG = "SContextTemperatureAlertAttribute";
    private int mHighTemperature = 127;
    private boolean mIsIncluding = true;
    private int mLowTemperature = 70;

    SContextTemperatureAlertAttribute() {
        setAttribute();
    }

    public SContextTemperatureAlertAttribute(int lowTemperature, int highTemperature, boolean isIncluding) {
        this.mLowTemperature = lowTemperature;
        this.mHighTemperature = highTemperature;
        this.mIsIncluding = isIncluding;
        setAttribute();
    }

    boolean checkAttribute() {
        if (this.mLowTemperature < SContextConstants.TEMPERATURE_ALERT_MINUS_INFINITY || this.mLowTemperature > 127) {
            Log.e(TAG, "The low temperature is wrong.");
            return false;
        } else if (this.mHighTemperature < SContextConstants.TEMPERATURE_ALERT_MINUS_INFINITY || this.mHighTemperature > 127) {
            Log.e(TAG, "The high temperature is wrong.");
            return false;
        } else if (this.mLowTemperature <= this.mHighTemperature) {
            return true;
        } else {
            Log.e(TAG, "The low temperature must be less than the high temperature.");
            return false;
        }
    }

    private void setAttribute() {
        Bundle attribute = new Bundle();
        attribute.putInt("low_temperature", this.mLowTemperature);
        attribute.putInt("high_temperature", this.mLowTemperature);
        attribute.putBoolean("including", this.mIsIncluding);
        super.setAttribute(23, attribute);
    }
}
