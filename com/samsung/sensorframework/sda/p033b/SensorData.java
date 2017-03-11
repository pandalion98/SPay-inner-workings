package com.samsung.sensorframework.sda.p033b;

import com.samsung.sensorframework.sda.p030a.SensorConfig;

/* renamed from: com.samsung.sensorframework.sda.b.a */
public abstract class SensorData {
    private final long Ia;
    private SensorData Ib;
    protected boolean Ic;
    private SensorConfig Id;

    public abstract int getSensorType();

    public SensorData(long j, SensorConfig sensorConfig) {
        this.Ia = j;
        this.Id = sensorConfig;
        this.Ic = false;
    }

    public SensorData gT() {
        return this.Ib;
    }

    public void m1511b(SensorData sensorData) {
        if (sensorData != null) {
            sensorData.m1511b(null);
        }
        this.Ib = sensorData;
    }
}
