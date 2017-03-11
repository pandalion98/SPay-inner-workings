package com.samsung.sensorframework.sda.p033b.p034a;

import com.samsung.sensorframework.sda.p030a.SensorConfig;
import com.samsung.sensorframework.sda.p033b.SensorData;

/* renamed from: com.samsung.sensorframework.sda.b.a.q */
public class MicrophoneData extends SensorData {
    private double[] Io;
    private long[] Ip;
    private String Iq;

    public MicrophoneData(long j, SensorConfig sensorConfig) {
        super(j, sensorConfig);
    }

    public void m1522a(double[] dArr) {
        this.Io = dArr;
    }

    public void m1523a(long[] jArr) {
        this.Ip = jArr;
    }

    public void bS(String str) {
        this.Iq = str;
    }

    public int getSensorType() {
        return 5005;
    }
}
