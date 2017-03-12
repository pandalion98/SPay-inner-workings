package com.samsung.sensorframework.sda.p033b.p034a;

import com.samsung.sensorframework.sda.p030a.SensorConfig;
import com.samsung.sensorframework.sda.p033b.SensorData;
import java.util.ArrayList;

/* renamed from: com.samsung.sensorframework.sda.b.a.d */
public class BluetoothData extends SensorData {
    private ArrayList<SDABluetoothDevice> Ii;
    private ArrayList<SDABluetoothDevice> Ij;

    public BluetoothData(long j, SensorConfig sensorConfig) {
        super(j, sensorConfig);
    }

    public void m1515h(ArrayList<SDABluetoothDevice> arrayList) {
        this.Ii = arrayList;
    }

    public void m1516i(ArrayList<SDABluetoothDevice> arrayList) {
        this.Ij = arrayList;
    }

    public int getSensorType() {
        return 5003;
    }
}
