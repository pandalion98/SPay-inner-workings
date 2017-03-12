package com.samsung.android.contextaware.dataprovider.sensorhubprovider.environmentsensorprovider;

import android.content.Context;
import android.os.Bundle;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubCmdProtocol;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.LibTypeProvider;
import com.samsung.android.contextaware.manager.ContextAwarePropertyBundle;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import com.samsung.android.fingerprint.FingerprintManager;

public abstract class EnvironmentSensorProvider extends LibTypeProvider {
    private int mLoggingStatus = 0;

    protected EnvironmentSensorProvider(int version, Context context, ISensorHubResetObservable observable) {
        super(version, context, null, observable);
    }

    protected final byte getInstLibType() {
        return ISensorHubCmdProtocol.TYPE_ENVIRONMENT_SENSOR_SERVICE;
    }

    public final <E> boolean setPropertyValue(int property, E value) {
        if (property != 13) {
            return false;
        }
        EnvironmentSensorHandler.getInstance().setInterval(((Integer) ((ContextAwarePropertyBundle) value).getValue()).intValue());
        CaLogger.info("setProperty (Interval) = " + Integer.toString(getInterval()));
        return true;
    }

    protected final int getInterval() {
        return EnvironmentSensorHandler.getInstance().getInterval();
    }

    protected final String getDisplayContents(Bundle bundle, String key) {
        if (key == null || key.isEmpty()) {
            CaLogger.error("key is null");
            return null;
        } else if (bundle == null || !bundle.containsKey(key)) {
            CaLogger.error("bundle is null");
            return null;
        } else {
            String loggingStatus = getContextValueNames()[0];
            StringBuffer str = new StringBuffer();
            if (key.equals(loggingStatus)) {
                try {
                    if (bundle.getString(key) == null) {
                        CaLogger.error("bundle.getString(key) is null");
                        return null;
                    }
                    str.append(getDisplayLoggingStatus(Integer.valueOf(bundle.getString(key)).intValue()));
                } catch (NumberFormatException e) {
                    CaLogger.exception(e);
                    return null;
                }
            }
            String[] strArray = bundle.getStringArray(key);
            if (strArray == null || strArray.length <= 0) {
                CaLogger.error("bundle.getStringArray(key) is null");
                return null;
            }
            for (String j : strArray) {
                str.append(j + ", ");
            }
            if (str.lastIndexOf(FingerprintManager.FINGER_PERMISSION_DELIMITER) > 0) {
                str.delete(str.lastIndexOf(FingerprintManager.FINGER_PERMISSION_DELIMITER), str.length());
            }
            return str.toString();
        }
    }

    protected final int getLoggingStatus() {
        return this.mLoggingStatus;
    }

    protected final void setLoggingStatus(int status) {
        this.mLoggingStatus = status;
    }

    private String getDisplayLoggingStatus(int status) {
        if (status == 2) {
            return "AP_SLEEP";
        }
        if (status == 1) {
            return "AP_WAKEUP";
        }
        return null;
    }
}
