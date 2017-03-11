package com.samsung.sensorframework.sda.p033b.p035b;

import com.samsung.sensorframework.sda.p030a.SensorConfig;
import com.samsung.sensorframework.sda.p033b.SensorData;

/* renamed from: com.samsung.sensorframework.sda.b.b.g */
public class ProximityData extends SensorData {
    private float IU;
    private float IW;

    public ProximityData(long j, SensorConfig sensorConfig) {
        super(j, sensorConfig);
    }

    public void setDistance(float f) {
        this.IU = f;
    }

    public void m1529a(float f) {
        this.IW = f;
    }

    public int getSensorType() {
        return 5007;
    }
}
