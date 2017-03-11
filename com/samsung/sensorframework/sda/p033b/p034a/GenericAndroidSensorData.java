package com.samsung.sensorframework.sda.p033b.p034a;

import com.samsung.sensorframework.sda.p030a.SensorConfig;
import com.samsung.sensorframework.sda.p033b.SensorData;
import java.util.ArrayList;

/* renamed from: com.samsung.sensorframework.sda.b.a.m */
public class GenericAndroidSensorData extends SensorData {
    private ArrayList<float[]> Ig;
    private ArrayList<Long> Ih;
    private int Im;

    public GenericAndroidSensorData(long j, SensorConfig sensorConfig, int i) {
        super(j, sensorConfig);
        this.Im = i;
    }

    public void m1519f(ArrayList<float[]> arrayList) {
        this.Ig = arrayList;
    }

    public void m1520g(ArrayList<Long> arrayList) {
        this.Ih = arrayList;
    }

    public int getSensorType() {
        return this.Im;
    }
}
