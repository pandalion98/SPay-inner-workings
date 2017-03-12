package com.samsung.sensorframework.sda.p036c.p037a;

import android.content.Context;
import com.samsung.sensorframework.sda.p030a.SensorConfig;
import com.samsung.sensorframework.sda.p033b.p034a.GenericAndroidSensorData;
import com.samsung.sensorframework.sda.p036c.AbstractProcessor;
import java.util.ArrayList;

/* renamed from: com.samsung.sensorframework.sda.c.a.i */
public class GenericAndroidSensorProcessor extends AbstractProcessor {
    public GenericAndroidSensorProcessor(Context context, boolean z, boolean z2) {
        super(context, z, z2);
    }

    public GenericAndroidSensorData m1547a(long j, ArrayList<float[]> arrayList, ArrayList<Long> arrayList2, SensorConfig sensorConfig, int i) {
        GenericAndroidSensorData genericAndroidSensorData = new GenericAndroidSensorData(j, sensorConfig, i);
        if (this.Je) {
            genericAndroidSensorData.m1519f(arrayList);
            genericAndroidSensorData.m1520g(arrayList2);
        }
        return genericAndroidSensorData;
    }
}
