package com.samsung.sensorframework.sda.p033b.p035b;

import com.samsung.sensorframework.sda.p030a.SensorConfig;
import com.samsung.sensorframework.sda.p033b.SensorData;

/* renamed from: com.samsung.sensorframework.sda.b.b.e */
public class PackageData extends SensorData {
    public String IR;
    public String packageName;

    public PackageData(long j, SensorConfig sensorConfig) {
        super(j, sensorConfig);
    }

    public void setPackageName(String str) {
        this.packageName = str;
    }

    public void bY(String str) {
        this.IR = str;
    }

    public int getSensorType() {
        return 5017;
    }
}
