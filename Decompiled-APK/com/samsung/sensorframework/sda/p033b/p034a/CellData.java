package com.samsung.sensorframework.sda.p033b.p034a;

import com.samsung.sensorframework.sda.p030a.SensorConfig;
import com.samsung.sensorframework.sda.p033b.SensorData;
import java.util.ArrayList;
import java.util.HashMap;

/* renamed from: com.samsung.sensorframework.sda.b.a.i */
public class CellData extends SensorData {
    private ArrayList<HashMap<String, Object>> Ik;

    public CellData(long j, SensorConfig sensorConfig) {
        super(j, sensorConfig);
    }

    public void m1517j(ArrayList<HashMap<String, Object>> arrayList) {
        this.Ik = arrayList;
    }

    public int getSensorType() {
        return 5037;
    }
}
