package com.samsung.sensorframework.sda.p033b.p034a;

import com.samsung.sensorframework.sda.p030a.SensorConfig;
import com.samsung.sensorframework.sda.p033b.SensorData;
import java.util.ArrayList;

/* renamed from: com.samsung.sensorframework.sda.b.a.y */
public class WifiData extends SensorData {
    private ArrayList<WifiScanResult> Iu;

    public WifiData(long j, SensorConfig sensorConfig) {
        super(j, sensorConfig);
    }

    public void m1524k(ArrayList<WifiScanResult> arrayList) {
        this.Iu = arrayList;
    }

    public int getSensorType() {
        return 5010;
    }

    public String toString() {
        if (this.Iu != null) {
            return "WifiData { Size: " + this.Iu.size() + " }";
        }
        return "WifiData { null }";
    }
}
