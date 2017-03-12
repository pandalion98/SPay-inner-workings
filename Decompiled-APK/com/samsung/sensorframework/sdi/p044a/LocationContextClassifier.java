package com.samsung.sensorframework.sdi.p044a;

import android.location.Location;
import com.samsung.sensorframework.sda.p033b.p034a.LocationData;

/* renamed from: com.samsung.sensorframework.sdi.a.c */
public class LocationContextClassifier {
    public static boolean m1659b(LocationData locationData) {
        LocationData locationData2;
        Location location;
        Location location2;
        if (locationData != null) {
            locationData2 = (LocationData) locationData.gT();
            location = locationData.getLocation();
        } else {
            location = null;
            locationData2 = null;
        }
        if (locationData2 != null) {
            location2 = locationData2.getLocation();
        } else {
            location2 = null;
        }
        if (LocationContextClassifier.m1658a(location, location2)) {
            return false;
        }
        return true;
    }

    private static boolean m1658a(Location location, Location location2) {
        if (location != null && location2 != null && location.distanceTo(location2) < 50.0f) {
            return true;
        }
        if (location == null && location2 == null) {
            return true;
        }
        return false;
    }
}
