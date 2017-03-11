package com.samsung.sensorframework.sda.p033b.p035b;

import com.samsung.sensorframework.sda.p030a.SensorConfig;
import com.samsung.sensorframework.sda.p033b.SensorData;

/* renamed from: com.samsung.sensorframework.sda.b.b.b */
public class ClipboardData extends SensorData {
    protected int IA;

    public ClipboardData(long j, SensorConfig sensorConfig) {
        super(j, sensorConfig);
    }

    public void aj(int i) {
        this.IA = i;
    }

    public int getSensorType() {
        return 5024;
    }
}
