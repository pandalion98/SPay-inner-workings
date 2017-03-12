package com.samsung.sensorframework.sda.p033b.p034a;

import android.location.Location;
import com.samsung.sensorframework.sda.p030a.SensorConfig;
import com.samsung.sensorframework.sda.p033b.SensorData;

/* renamed from: com.samsung.sensorframework.sda.b.a.p */
public class LocationData extends SensorData {
    private int Im;
    private Location In;

    public LocationData(long j, SensorConfig sensorConfig, int i) {
        super(j, sensorConfig);
        this.Im = i;
    }

    public void m1521a(Location location) {
        this.In = location;
    }

    public Location getLocation() {
        return this.In;
    }

    public int getSensorType() {
        return this.Im;
    }

    public String toString() {
        if (this.In == null) {
            return "LocationData { null }";
        }
        return "LocationData { " + this.In.toString() + " }";
    }
}
