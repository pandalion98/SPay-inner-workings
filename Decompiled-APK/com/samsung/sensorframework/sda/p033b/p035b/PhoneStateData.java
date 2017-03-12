package com.samsung.sensorframework.sda.p033b.p035b;

import com.samsung.sensorframework.sda.p030a.SensorConfig;
import com.samsung.sensorframework.sda.p033b.SensorData;

/* renamed from: com.samsung.sensorframework.sda.b.b.f */
public class PhoneStateData extends SensorData {
    private int IT;
    private String data;
    private String number;

    public PhoneStateData(long j, SensorConfig sensorConfig) {
        super(j, sensorConfig);
    }

    public void setNumber(String str) {
        this.number = str;
    }

    public void setEventType(int i) {
        this.IT = i;
    }

    public void setData(String str) {
        this.data = str;
    }

    public int getSensorType() {
        return 5006;
    }
}
