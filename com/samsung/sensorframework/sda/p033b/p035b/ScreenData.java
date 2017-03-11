package com.samsung.sensorframework.sda.p033b.p035b;

import com.samsung.sensorframework.sda.p030a.SensorConfig;
import com.samsung.sensorframework.sda.p033b.SensorData;

/* renamed from: com.samsung.sensorframework.sda.b.b.h */
public class ScreenData extends SensorData {
    private int IX;

    public ScreenData(long j, SensorConfig sensorConfig) {
        super(j, sensorConfig);
    }

    public void setStatus(int i) {
        this.IX = i;
    }

    public int getSensorType() {
        return 5008;
    }
}
