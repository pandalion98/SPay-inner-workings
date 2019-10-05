/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.location.Location
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.Iterator
 *  java.util.List
 */
package com.samsung.sensorframework.sdi.d;

import com.samsung.android.spayfw.b.Log;
import com.samsung.sensorframework.sda.b.a.p;

import java.util.ArrayList;
import java.util.List;

public class a {
    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public static b a(p var0, List<String> var1_1, int var2_2) {
        var3_3 = new ArrayList();
        if (!com.samsung.sensorframework.sda.f.a.a(var0) || var1_1 == null || var1_1.size() <= 0) {
            Log.d("POIProximityDetector", "getNearbyPoIs() location is invalid or poiList is null or empty");
            var4_6 = Double.MAX_VALUE;
            return new b((List<String>)var3_3, var4_6);
        }
        var6_4 = var0.getLocation();
        var7_5 = var1_1.iterator();
        var4_6 = Double.MAX_VALUE;
        while (var7_5.hasNext() != false) {
            block10 : {
                block8 : {
                    block9 : {
                        var8_7 = (String)var7_5.next();
                        if (var8_7 == null || var8_7.length() <= 0) break block9;
                        if (var6_4 == null) break block8;
                        var12_10 = c.cp(var8_7);
                        if (var12_10 == null) break block8;
                        var13_11 = var6_4.distanceTo(var12_10);
                        if (var13_11 < var4_6) {
                            var4_6 = var13_11;
                        }
                        var15_12 = c.a(var8_7, var2_2);
                        if (!(var13_11 < var15_12)) ** GOTO lbl28
                        try {
                            var17_13 = c.a(var8_7, "LAT_LON", var13_11);
                            if (var17_13 != null && var17_13.length() > 0) {
                                var3_3.add((Object)var17_13);
                            }
                            Log.d("POIProximityDetector", "getNearbyPoIs() User is near a POI location: " + c.cr(var17_13) + ", Distance: " + var13_11 + " Trigger radius: " + var15_12);
                            break block8;
lbl28: // 1 sources:
                            Log.d("POIProximityDetector", "getNearbyPoIs() User is not in proximity to: " + c.cr(var8_7) + ", Distance: " + var13_11 + " Trigger radius: " + var15_12);
                            break block8;
                        }
                        catch (Exception var11_9) {
                            var11_9.printStackTrace();
                        }
                    }
                    var9_8 = var4_6;
                    break block10;
                }
                var9_8 = var4_6;
            }
            var4_6 = var9_8;
        }
        return new b((List<String>)var3_3, var4_6);
    }
}

