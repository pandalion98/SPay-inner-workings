/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.location.Location
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.Arrays
 *  java.util.Collection
 *  java.util.HashSet
 *  java.util.Iterator
 *  java.util.List
 *  org.json.JSONArray
 *  org.json.JSONObject
 */
package com.samsung.sensorframework.sdi.d;

import android.content.Context;
import android.location.Location;

import com.samsung.android.spayfw.b.Log;
import com.samsung.sensorframework.b;
import com.samsung.sensorframework.sda.b.a.p;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class c {
    public static boolean C(String string, String string2) {
        String string3 = c.cr(string);
        String string4 = c.cr(string2);
        if (string3 != null && string3.length() > 0 && string4 != null && string4.length() > 0 && string3.equals((Object)string4)) {
            Log.d("PoIUtils", "areSamePoIs() returning: true");
            return true;
        }
        Log.d("PoIUtils", "areSamePoIs() returning: false");
        return false;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static double a(String string, double d2) {
        Log.d("PoIUtils", "getTriggerRadius() defaultTriggerRadius: " + d2);
        if (string != null) {
            try {
                JSONObject jSONObject;
                JSONObject jSONObject2 = new JSONObject(string);
                if (jSONObject2.has("trigger") && (jSONObject = (JSONObject)jSONObject2.get("trigger")) != null && jSONObject.has("radius")) {
                    d2 = jSONObject.getDouble("radius");
                    Log.d("PoIUtils", "getTriggerRadius() triggerRadius in Cache: " + d2);
                }
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        Log.d("PoIUtils", "getTriggerRadius() returning: " + d2);
        return d2;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public static String a(String var0, String var1_1, double var2_2) {
        if (var0 == null) ** GOTO lbl13
        try {
            Log.d("PoIUtils", "addProximityInfoToPoiJson() poiJsonStr: " + var0 + " sensorUsed: " + var1_1 + " distance: " + var2_2);
            var5_3 = new JSONObject();
            var5_3.put("distance", var2_2);
            var5_3.put("sensorUsed", (Object)var1_1);
            var8_4 = new JSONObject(var0);
            if (var8_4.has("proximityInfo")) {
                var8_4.remove("proximityInfo");
            }
            var8_4.put("proximityInfo", (Object)var5_3);
            Log.d("PoIUtils", "addProximityInfoToPoiJson() returning: " + var8_4.toString());
            return var8_4.toString();
lbl13: // 1 sources:
            Log.d("PoIUtils", "addProximityInfoToPoiJson() poiJsonStr is null");
        }
        catch (Exception var4_5) {
            var4_5.printStackTrace();
        }
        Log.d("PoIUtils", "addProximityInfoToPoiJson() returning: " + var0);
        return var0;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public static List<String> a(Context var0, p var1_1, int var2_2) {
        Log.d("PoIUtils", "queryPoICache() called");
        var3_3 = var1_1.getLocation();
        if (var3_3 == null) return null;
        Log.d("PoIUtils", "queryPoICache() location: " + (Object)var3_3);
        var4_4 = b.aM(var0).b(var3_3.getLatitude(), var3_3.getLongitude(), var2_2);
        if (var4_4 == null) return var4_4;
        var5_5 = new ArrayList();
        var6_6 = var4_4.iterator();
        do {
            block6 : {
                if (!var6_6.hasNext()) ** GOTO lbl21
                var10_7 = (String)var6_6.next();
                Log.d("PoIUtils", "queryPoICache() Entry: " + var10_7);
                var12_11 = var15_13 = c.cr(var10_7);
                var13_12 = var16_14 = c.cp(var10_7);
                break block6;
                catch (Exception var11_9) {
                    block7 : {
                        var12_11 = null;
                        break block7;
lbl21: // 1 sources:
                        var7_15 = var5_5.iterator();
                        while (var7_15.hasNext() != false) {
                            var8_16 = (String)var7_15.next();
                            Log.d("PoIUtils", "queryPoICache() Removing poi from cache: " + var8_16);
                            var4_4.remove((Object)var8_16);
                        }
                        return var4_4;
                        catch (Exception var11_10) {}
                    }
                    var11_8.printStackTrace();
                    var13_12 = null;
                }
            }
            if (var12_11 != null && var12_11.length() != 0 && var13_12 != null) continue;
            var5_5.add((Object)var10_7);
        } while (true);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public static List<String> a(List<String> var0, List<String> var1_1) {
        if (var0 == null || var1_1 == null) ** GOTO lbl25
        try {
            Log.d("PoIUtils", "mergeLocationPoiListIntoWiFiPoiList() locPoiJsonList.size(): " + var0.size() + " wifiPoiJsonList.size(): " + var1_1.size());
            var3_2 = new ArrayList();
            var4_3 = new HashSet();
            for (String var10_5 : var1_1) {
                var11_6 = c.cr(var10_5);
                if (var11_6 == null || var11_6.length() <= 0) continue;
                var4_3.add((Object)var11_6);
                var3_2.add((Object)var10_5);
            }
            var6_8 = var0.iterator();
            do {
                if (!var6_8.hasNext()) {
                    Log.d("PoIUtils", "mergeLocationPoiListIntoWiFiPoiList() returning mergedList.size(): " + var3_2.size());
                    return var3_2;
                }
                var7_9 = (String)var6_8.next();
                var8_10 = c.cr(var7_9);
                if (var8_10 == null || var8_10.length() <= 0 || var4_3.contains((Object)var8_10)) continue;
                var3_2.add((Object)var7_9);
            } while (true);
        }
        catch (Exception var2_7) {
            block6 : {
                var2_7.printStackTrace();
                break block6;
lbl25: // 1 sources:
                Log.d("PoIUtils", "mergeLocationPoiListIntoWiFiPoiList() wifiPoiJsonList or locPoiJsonList is null");
            }
            Log.d("PoIUtils", "mergeLocationPoiListIntoWiFiPoiList() returning null");
            return null;
        }
    }

    public static boolean a(String string, Collection<String> collection) {
        if (collection != null && collection.size() > 0) {
            Iterator iterator = collection.iterator();
            while (iterator.hasNext()) {
                if (!c.C(string, (String)iterator.next())) continue;
                Log.d("PoIUtils", "isPoIInCollection() returning: true");
                return true;
            }
        }
        Log.d("PoIUtils", "isPoIInCollection() returning: false");
        return false;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static Location cp(String string) {
        if (string == null) return null;
        try {
            JSONObject jSONObject = new JSONObject(string);
            Location location = new Location("network");
            if (!jSONObject.has("location")) return null;
            JSONObject jSONObject2 = (JSONObject)jSONObject.get("location");
            if (jSONObject2 != null && jSONObject2.has("latitude") && jSONObject2.has("longitude")) {
                location.setLatitude(jSONObject2.getDouble("latitude"));
                location.setLongitude(jSONObject2.getDouble("longitude"));
                return location;
            }
            Log.d("PoIUtils", "No location info in poiJsonStr: " + string);
            return null;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public static ArrayList<String> cq(String string) {
        ArrayList arrayList;
        block10 : {
            block9 : {
                if (string != null) {
                    JSONArray jSONArray;
                    JSONObject jSONObject;
                    int n2;
                    JSONObject jSONObject2 = new JSONObject(string);
                    if (!jSONObject2.has("sensorData") || (jSONObject = (JSONObject)jSONObject2.get("sensorData")) == null) break block9;
                    if (!jSONObject.has("wifiSignatures") || (jSONArray = jSONObject.getJSONArray("wifiSignatures")) == null) break block9;
                    try {
                        if (jSONArray.length() <= 0) break block9;
                        arrayList = new ArrayList();
                        n2 = 0;
                    }
                    catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    do {
                        block11 : {
                            String string2;
                            if (n2 >= jSONArray.length()) break block10;
                            JSONObject jSONObject3 = jSONArray.getJSONObject(n2);
                            if (jSONObject3 == null) break block11;
                            if (!jSONObject3.has("bssid") || (string2 = jSONObject3.getString("bssid")) == null) break block11;
                            if (string2.length() <= 0) break block11;
                            arrayList.add((Object)string2);
                        }
                        ++n2;
                    } while (true);
                }
            }
            arrayList = null;
        }
        return arrayList;
    }

    public static String cr(String string) {
        if (string != null) {
            try {
                JSONObject jSONObject = new JSONObject(string);
                if (jSONObject.has("id")) {
                    String string2 = jSONObject.getString("id");
                    return string2;
                }
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    public static boolean cs(String string) {
        if (c.a(string, 100.0) >= 10000.0) {
            Log.d("PoIUtils", "isDataSyncPoI() returning: true");
            return true;
        }
        Log.d("PoIUtils", "isDataSyncPoI() returning: false");
        return false;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public static String ct(String var0) {
        var1_1 = Arrays.asList((Object[])com.samsung.sensorframework.sdi.b.b.Le);
        if (var0 != null) {
            try {
                Log.d("PoIUtils", "removeOptionalFieldsFromPoiJson() poiJsonStr: " + var0);
                var3_2 = new JSONObject(var0);
                var4_3 = var3_2.keys();
                if (var4_3 == null) ** GOTO lbl27
                var5_4 = new ArrayList();
                while (var4_3.hasNext()) {
                    var9_5 = (String)var4_3.next();
                    if (var1_1.contains((Object)var9_5)) continue;
                    var5_4.add((Object)var9_5);
                }
                var6_7 = var5_4.iterator();
                do {
                    if (!var6_7.hasNext()) {
                        Log.d("PoIUtils", "removeOptionalFieldsFromPoiJson() returning: " + var3_2.toString());
                        return var3_2.toString();
                    }
                    var7_8 = (String)var6_7.next();
                    if (!var3_2.has(var7_8)) continue;
                    var3_2.remove(var7_8);
                } while (true);
            }
            catch (Exception var2_6) {
                var2_6.printStackTrace();
            }
        } else {
            Log.d("PoIUtils", "removeOptionalFieldsFromPoiJson() poiJsonStr is null");
        }
lbl27: // 3 sources:
        Log.d("PoIUtils", "removeOptionalFieldsFromPoiJson() returning: " + var0);
        return var0;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public static List<String> l(List<String> var0) {
        if (var0 == null) ** GOTO lbl18
        try {
            if (var0.size() > 0) {
                Log.d("PoIUtils", "removeDuplicatesInPoiJsonList() poiJsonList.size(): " + var0.size());
                var2_1 = new ArrayList();
                var3_2 = new HashSet();
                for (String var5_4 : var0) {
                    if (var5_4 == null || var5_4.length() <= 0 || (var6_5 = c.cr(var5_4)) == null || var3_2.contains((Object)var6_5)) continue;
                    var2_1.add((Object)var5_4);
                    var3_2.add((Object)var6_5);
                }
                Log.d("PoIUtils", "removeDuplicatesInPoiJsonList() returnList.size(): " + var2_1.size());
                return var2_1;
            }
            ** GOTO lbl18
        }
        catch (Exception var1_6) {
            block5 : {
                var1_6.printStackTrace();
                break block5;
lbl18: // 2 sources:
                Log.d("PoIUtils", "removeDuplicatesInPoiJsonList() poiJsonList is null or empty");
            }
            Log.d("PoIUtils", "removeDuplicatesInPoiJsonList() returning original list");
            return var0;
        }
    }

    public static List<String> m(List<String> list) {
        if (list != null && list.size() > 0) {
            Log.d("PoIUtils", "removeOptionalFieldsFromPoiJsonList() poiJsonList.size(): " + list.size());
            ArrayList arrayList = new ArrayList();
            Iterator iterator = list.iterator();
            while (iterator.hasNext()) {
                arrayList.add((Object)c.ct((String)iterator.next()));
            }
            return arrayList;
        }
        Log.d("PoIUtils", "removeOptionalFieldsFromPoiJsonList() poiJsonList is null or empty");
        return list;
    }
}

