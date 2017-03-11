package com.samsung.sensorframework.sda.p036c.p037a;

import android.content.Context;
import android.location.Location;
import com.samsung.sensorframework.sda.p030a.SensorConfig;
import com.samsung.sensorframework.sda.p033b.p034a.LocationData;
import com.samsung.sensorframework.sda.p036c.AbstractProcessor;

/* renamed from: com.samsung.sensorframework.sda.c.a.k */
public class LocationProcessor extends AbstractProcessor {
    public LocationProcessor(Context context, boolean z, boolean z2) {
        super(context, z, z2);
    }

    public LocationData m1551a(long j, Location location, SensorConfig sensorConfig, int i) {
        LocationData locationData = new LocationData(j, sensorConfig, i);
        if (this.Je) {
            locationData.m1521a(location);
        }
        return locationData;
    }
}
