package com.samsung.android.contextaware.dataprovider.sensorhubprovider;

import com.samsung.android.contextaware.manager.ISensorHubResetObserver;

public interface ISensorHubResetObservable {
    public static final byte SENSORHUB_NONE = (byte) 0;
    public static final byte SENSORHUB_RESET = (byte) -43;

    void notifySensorHubResetObserver(int i);

    void registerSensorHubResetObserver(ISensorHubResetObserver iSensorHubResetObserver);

    void unregisterSensorHubResetObserver(ISensorHubResetObserver iSensorHubResetObserver);
}
