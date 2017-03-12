package android.hardware.scontext;

import android.net.wifi.hs20.WifiHs20DBStore.PolicySubscriptionUpdate;
import android.os.Bundle;
import android.util.Log;

@Deprecated
public class SContextEnvironmentAttribute extends SContextAttribute {
    private static final String TAG = "SContextEnvironmentAttribute";
    private int mSensorType = 1;
    private int mUpdateInterval = 60;

    SContextEnvironmentAttribute() {
        setAttribute();
    }

    public SContextEnvironmentAttribute(int sensorType, int updateInterval) {
        this.mSensorType = sensorType;
        this.mUpdateInterval = updateInterval;
        setAttribute();
    }

    boolean checkAttribute() {
        if (this.mSensorType != 1) {
            Log.e(TAG, "The sensor type is wrong.");
            return false;
        } else if (this.mUpdateInterval >= 0) {
            return true;
        } else {
            Log.e(TAG, "The update interval is wrong.");
            return false;
        }
    }

    private void setAttribute() {
        Bundle attribute = new Bundle();
        attribute.putInt("sensor_type", this.mSensorType);
        attribute.putInt(PolicySubscriptionUpdate.UPDATE_INTERVAL, this.mUpdateInterval);
        super.setAttribute(8, attribute);
    }
}
