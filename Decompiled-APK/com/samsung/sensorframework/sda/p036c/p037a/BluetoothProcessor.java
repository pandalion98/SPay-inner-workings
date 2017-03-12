package com.samsung.sensorframework.sda.p036c.p037a;

import android.content.Context;
import com.samsung.sensorframework.sda.p030a.SensorConfig;
import com.samsung.sensorframework.sda.p033b.p034a.BluetoothData;
import com.samsung.sensorframework.sda.p033b.p034a.SDABluetoothDevice;
import com.samsung.sensorframework.sda.p036c.AbstractProcessor;
import java.util.ArrayList;

/* renamed from: com.samsung.sensorframework.sda.c.a.c */
public class BluetoothProcessor extends AbstractProcessor {
    public BluetoothProcessor(Context context, boolean z, boolean z2) {
        super(context, z, z2);
    }

    public BluetoothData m1534b(long j, ArrayList<SDABluetoothDevice> arrayList, ArrayList<SDABluetoothDevice> arrayList2, SensorConfig sensorConfig) {
        BluetoothData bluetoothData = new BluetoothData(j, sensorConfig);
        if (this.Je) {
            bluetoothData.m1515h(arrayList);
            bluetoothData.m1516i(arrayList2);
        }
        return bluetoothData;
    }
}
