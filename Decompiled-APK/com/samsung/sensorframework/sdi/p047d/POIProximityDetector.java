package com.samsung.sensorframework.sdi.p047d;

import android.location.Location;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.sensorframework.sda.p033b.p034a.LocationData;
import com.samsung.sensorframework.sda.p043f.DataAcquisitionUtils;
import java.util.ArrayList;
import java.util.List;

/* renamed from: com.samsung.sensorframework.sdi.d.a */
public class POIProximityDetector {
    public static PoIProximityDataHolder m1691a(LocationData locationData, List<String> list, int i) {
        double d;
        List arrayList = new ArrayList();
        if (!DataAcquisitionUtils.m1648a(locationData) || list == null || list.size() <= 0) {
            Log.m285d("POIProximityDetector", "getNearbyPoIs() location is invalid or poiList is null or empty");
            d = Double.MAX_VALUE;
        } else {
            Location location = locationData.getLocation();
            d = Double.MAX_VALUE;
            for (String str : list) {
                double d2;
                String str2;
                if (str2 != null && str2.length() > 0) {
                    if (location != null) {
                        try {
                            Location cp = PoIUtils.cp(str2);
                            if (cp != null) {
                                double distanceTo = (double) location.distanceTo(cp);
                                if (distanceTo < d) {
                                    d = distanceTo;
                                }
                                double a = PoIUtils.m1693a(str2, (double) i);
                                if (distanceTo < a) {
                                    str2 = PoIUtils.m1694a(str2, "LAT_LON", distanceTo);
                                    if (str2 != null && str2.length() > 0) {
                                        arrayList.add(str2);
                                    }
                                    Log.m285d("POIProximityDetector", "getNearbyPoIs() User is near a POI location: " + PoIUtils.cr(str2) + ", Distance: " + distanceTo + " Trigger radius: " + a);
                                } else {
                                    Log.m285d("POIProximityDetector", "getNearbyPoIs() User is not in proximity to: " + PoIUtils.cr(str2) + ", Distance: " + distanceTo + " Trigger radius: " + a);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    d2 = d;
                    d = d2;
                }
                d2 = d;
                d = d2;
            }
        }
        return new PoIProximityDataHolder(arrayList, d);
    }
}
