package com.samsung.sensorframework.sda.p033b.p034a;

import com.samsung.sensorframework.sda.p030a.SensorConfig;
import com.samsung.sensorframework.sda.p033b.SensorData;
import java.util.ArrayList;

/* renamed from: com.samsung.sensorframework.sda.b.a.c */
public class AccelerometerData extends SensorData {
    private ArrayList<float[]> Ig;
    private ArrayList<Long> Ih;

    public AccelerometerData(long j, SensorConfig sensorConfig) {
        super(j, sensorConfig);
    }

    public void m1513f(ArrayList<float[]> arrayList) {
        this.Ig = arrayList;
    }

    public void m1514g(ArrayList<Long> arrayList) {
        this.Ih = arrayList;
    }

    public int getSensorType() {
        return 5001;
    }
}
