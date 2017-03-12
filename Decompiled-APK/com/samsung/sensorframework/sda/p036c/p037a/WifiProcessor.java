package com.samsung.sensorframework.sda.p036c.p037a;

import android.content.Context;
import com.samsung.sensorframework.sda.p030a.SensorConfig;
import com.samsung.sensorframework.sda.p033b.p034a.WifiData;
import com.samsung.sensorframework.sda.p033b.p034a.WifiScanResult;
import com.samsung.sensorframework.sda.p036c.AbstractProcessor;
import java.util.ArrayList;

/* renamed from: com.samsung.sensorframework.sda.c.a.o */
public class WifiProcessor extends AbstractProcessor {
    public WifiProcessor(Context context, boolean z, boolean z2) {
        super(context, z, z2);
    }

    public WifiData m1560b(long j, ArrayList<WifiScanResult> arrayList, SensorConfig sensorConfig) {
        WifiData wifiData = new WifiData(j, sensorConfig);
        if (this.Je) {
            wifiData.m1524k(arrayList);
        }
        return wifiData;
    }
}
