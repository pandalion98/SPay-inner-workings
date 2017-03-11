package com.samsung.sensorframework.sda.p036c.p038b;

import android.content.Context;
import com.samsung.sensorframework.sda.p030a.SensorConfig;
import com.samsung.sensorframework.sda.p033b.p035b.ProximityData;
import com.samsung.sensorframework.sda.p036c.AbstractProcessor;

/* renamed from: com.samsung.sensorframework.sda.c.b.g */
public class ProximityProcessor extends AbstractProcessor {
    public ProximityProcessor(Context context, boolean z, boolean z2) {
        super(context, z, z2);
    }

    public ProximityData m1568a(long j, SensorConfig sensorConfig, float f, float f2) {
        ProximityData proximityData = new ProximityData(j, sensorConfig);
        if (this.Je) {
            proximityData.setDistance(f);
            proximityData.m1529a(f2);
        }
        return proximityData;
    }
}
