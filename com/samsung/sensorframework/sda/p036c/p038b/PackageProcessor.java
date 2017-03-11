package com.samsung.sensorframework.sda.p036c.p038b;

import android.content.Context;
import android.content.Intent;
import com.samsung.sensorframework.sda.p030a.SensorConfig;
import com.samsung.sensorframework.sda.p033b.p035b.PackageData;
import com.samsung.sensorframework.sda.p036c.AbstractProcessor;

/* renamed from: com.samsung.sensorframework.sda.c.b.e */
public class PackageProcessor extends AbstractProcessor {
    public PackageProcessor(Context context, boolean z, boolean z2) {
        super(context, z, z2);
    }

    public PackageData m1566c(long j, SensorConfig sensorConfig, Intent intent) {
        PackageData packageData = new PackageData(j, sensorConfig);
        packageData.bY(intent.getAction());
        packageData.setPackageName(intent.getData().getEncodedSchemeSpecificPart());
        return packageData;
    }
}
