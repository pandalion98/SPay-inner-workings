package com.samsung.sensorframework.sda.p033b.p035b;

import com.samsung.sensorframework.sda.p030a.SensorConfig;
import com.samsung.sensorframework.sda.p033b.SensorData;

/* renamed from: com.samsung.sensorframework.sda.b.b.a */
public class BatteryData extends SensorData {
    private int Iw;
    private int Ix;
    private int Iy;
    private int Iz;
    private String action;
    private int level;
    private int scale;
    private int status;

    public BatteryData(long j, SensorConfig sensorConfig) {
        super(j, sensorConfig);
    }

    public void bT(String str) {
        this.action = str;
    }

    public String getAction() {
        return this.action;
    }

    public void ad(int i) {
        this.level = i;
    }

    public void ae(int i) {
        this.Iy = i;
    }

    public void af(int i) {
        this.scale = i;
    }

    public void ag(int i) {
        this.Iw = i;
    }

    public void ah(int i) {
        this.Ix = i;
    }

    public void setStatus(int i) {
        this.status = i;
    }

    public void ai(int i) {
        this.Iz = i;
    }

    public int getSensorType() {
        return 5002;
    }
}
