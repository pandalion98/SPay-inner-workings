package com.samsung.sensorframework.sda.p036c.p037a;

import android.content.Context;
import com.samsung.sensorframework.sda.p030a.SensorConfig;
import com.samsung.sensorframework.sda.p033b.p034a.AccelerometerData;
import com.samsung.sensorframework.sda.p036c.AbstractProcessor;
import java.util.ArrayList;

/* renamed from: com.samsung.sensorframework.sda.c.a.a */
public class AccelerometerProcessor extends AbstractProcessor {
    public AccelerometerProcessor(Context context, boolean z, boolean z2) {
        super(context, z, z2);
    }

    public AccelerometerData m1532a(long j, ArrayList<float[]> arrayList, ArrayList<Long> arrayList2, SensorConfig sensorConfig) {
        AccelerometerData accelerometerData = new AccelerometerData(j, sensorConfig);
        if (this.Je) {
            accelerometerData.m1513f(arrayList);
            accelerometerData.m1514g(arrayList2);
        }
        return accelerometerData;
    }
}
