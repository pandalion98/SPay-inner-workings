package com.samsung.android.contextaware.utilbundle.autotest;

import android.content.Context;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import com.samsung.android.sensorhub.SensorHubManager;

class SensorHubOperationDebugging extends OperationDebugging {
    private final SensorHubManager mSensorHubManager;

    protected SensorHubOperationDebugging(Context context, int delayTime) {
        super(delayTime);
        this.mSensorHubManager = (SensorHubManager) context.getSystemService("sensorhub");
    }

    protected final void doDebugging(byte[] packet) {
        if (this.mSensorHubManager != null) {
            if (this.mSensorHubManager.SendSensorHubData(this.mSensorHubManager.getDefaultSensorHub(1), packet.length, packet) <= 0) {
                CaLogger.error("fail to send cmd to SensorHub");
            } else {
                CaLogger.error("success to send cmd to SensorHub");
            }
        }
    }
}
