package com.samsung.sensorframework.sdi.p047d;

import android.content.Context;
import android.location.Location;
import com.samsung.android.spayfw.appinterface.PushMessage;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.sensorframework.SFManager;
import com.samsung.sensorframework.sda.p033b.p034a.LocationData;
import com.samsung.sensorframework.sdi.p045b.SDIConfig;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/* renamed from: com.samsung.sensorframework.sdi.d.c */
public class PoIUtils {
    public static Location cp(String str) {
        if (str != null) {
            try {
                JSONObject jSONObject = new JSONObject(str);
                Location location = new Location("network");
                if (jSONObject.has("location")) {
                    jSONObject = (JSONObject) jSONObject.get("location");
                    if (jSONObject != null && jSONObject.has("latitude") && jSONObject.has("longitude")) {
                        location.setLatitude(jSONObject.getDouble("latitude"));
                        location.setLongitude(jSONObject.getDouble("longitude"));
                        return location;
                    }
                    Log.m285d("PoIUtils", "No location info in poiJsonStr: " + str);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static ArrayList<String> cq(String str) {
        if (str != null) {
            try {
                JSONObject jSONObject = new JSONObject(str);
                if (jSONObject.has("sensorData")) {
                    jSONObject = (JSONObject) jSONObject.get("sensorData");
                    if (jSONObject != null && jSONObject.has("wifiSignatures")) {
                        JSONArray jSONArray = jSONObject.getJSONArray("wifiSignatures");
                        if (jSONArray != null && jSONArray.length() > 0) {
                            ArrayList<String> arrayList = new ArrayList();
                            for (int i = 0; i < jSONArray.length(); i++) {
                                JSONObject jSONObject2 = jSONArray.getJSONObject(i);
                                if (jSONObject2 != null && jSONObject2.has("bssid")) {
                                    String string = jSONObject2.getString("bssid");
                                    if (string != null && string.length() > 0) {
                                        arrayList.add(string);
                                    }
                                }
                            }
                            return arrayList;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static List<String> m1695a(Context context, LocationData locationData, int i) {
        String cr;
        Location cp;
        Exception e;
        Log.m285d("PoIUtils", "queryPoICache() called");
        Location location = locationData.getLocation();
        if (location == null) {
            return null;
        }
        Log.m285d("PoIUtils", "queryPoICache() location: " + location);
        List<String> b = SFManager.aM(context).m1505b(location.getLatitude(), location.getLongitude(), i);
        if (b != null) {
            String str;
            ArrayList arrayList = new ArrayList();
            for (String str2 : b) {
                Log.m285d("PoIUtils", "queryPoICache() Entry: " + str2);
                try {
                    cr = PoIUtils.cr(str2);
                    try {
                        cp = PoIUtils.cp(str2);
                    } catch (Exception e2) {
                        e = e2;
                        e.printStackTrace();
                        cp = null;
                        if (cr != null) {
                        }
                        arrayList.add(str2);
                    }
                } catch (Exception e3) {
                    e = e3;
                    cr = null;
                    e.printStackTrace();
                    cp = null;
                    if (cr != null) {
                    }
                    arrayList.add(str2);
                }
                if (cr != null || cr.length() == 0 || r2 == null) {
                    arrayList.add(str2);
                }
            }
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                str2 = (String) it.next();
                Log.m285d("PoIUtils", "queryPoICache() Removing poi from cache: " + str2);
                b.remove(str2);
            }
        }
        return b;
    }

    public static List<String> m1698l(List<String> list) {
        if (list != null) {
            try {
                if (list.size() > 0) {
                    Log.m285d("PoIUtils", "removeDuplicatesInPoiJsonList() poiJsonList.size(): " + list.size());
                    ArrayList arrayList = new ArrayList();
                    HashSet hashSet = new HashSet();
                    for (String str : list) {
                        if (str != null && str.length() > 0) {
                            String cr = PoIUtils.cr(str);
                            if (!(cr == null || hashSet.contains(cr))) {
                                arrayList.add(str);
                                hashSet.add(cr);
                            }
                        }
                    }
                    Log.m285d("PoIUtils", "removeDuplicatesInPoiJsonList() returnList.size(): " + arrayList.size());
                    return arrayList;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.m285d("PoIUtils", "removeDuplicatesInPoiJsonList() poiJsonList is null or empty");
        Log.m285d("PoIUtils", "removeDuplicatesInPoiJsonList() returning original list");
        return list;
    }

    public static String cr(String str) {
        if (str != null) {
            try {
                JSONObject jSONObject = new JSONObject(str);
                if (jSONObject.has(PushMessage.JSON_KEY_ID)) {
                    return jSONObject.getString(PushMessage.JSON_KEY_ID);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static boolean m1692C(String str, String str2) {
        String cr = PoIUtils.cr(str);
        String cr2 = PoIUtils.cr(str2);
        if (cr == null || cr.length() <= 0 || cr2 == null || cr2.length() <= 0 || !cr.equals(cr2)) {
            Log.m285d("PoIUtils", "areSamePoIs() returning: false");
            return false;
        }
        Log.m285d("PoIUtils", "areSamePoIs() returning: true");
        return true;
    }

    public static boolean m1697a(String str, Collection<String> collection) {
        if (collection != null && collection.size() > 0) {
            for (String C : collection) {
                if (PoIUtils.m1692C(str, C)) {
                    Log.m285d("PoIUtils", "isPoIInCollection() returning: true");
                    return true;
                }
            }
        }
        Log.m285d("PoIUtils", "isPoIInCollection() returning: false");
        return false;
    }

    public static double m1693a(String str, double d) {
        Log.m285d("PoIUtils", "getTriggerRadius() defaultTriggerRadius: " + d);
        if (str != null) {
            try {
                JSONObject jSONObject = new JSONObject(str);
                if (jSONObject.has("trigger")) {
                    jSONObject = (JSONObject) jSONObject.get("trigger");
                    if (jSONObject != null && jSONObject.has("radius")) {
                        d = jSONObject.getDouble("radius");
                        Log.m285d("PoIUtils", "getTriggerRadius() triggerRadius in Cache: " + d);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.m285d("PoIUtils", "getTriggerRadius() returning: " + d);
        return d;
    }

    public static boolean cs(String str) {
        if (PoIUtils.m1693a(str, 100.0d) >= 10000.0d) {
            Log.m285d("PoIUtils", "isDataSyncPoI() returning: true");
            return true;
        }
        Log.m285d("PoIUtils", "isDataSyncPoI() returning: false");
        return false;
    }

    public static String m1694a(String str, String str2, double d) {
        if (str != null) {
            try {
                Log.m285d("PoIUtils", "addProximityInfoToPoiJson() poiJsonStr: " + str + " sensorUsed: " + str2 + " distance: " + d);
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("distance", d);
                jSONObject.put("sensorUsed", str2);
                JSONObject jSONObject2 = new JSONObject(str);
                if (jSONObject2.has("proximityInfo")) {
                    jSONObject2.remove("proximityInfo");
                }
                jSONObject2.put("proximityInfo", jSONObject);
                Log.m285d("PoIUtils", "addProximityInfoToPoiJson() returning: " + jSONObject2.toString());
                return jSONObject2.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.m285d("PoIUtils", "addProximityInfoToPoiJson() poiJsonStr is null");
            Log.m285d("PoIUtils", "addProximityInfoToPoiJson() returning: " + str);
            return str;
        }
    }

    public static List<String> m1699m(List<String> list) {
        if (list == null || list.size() <= 0) {
            Log.m285d("PoIUtils", "removeOptionalFieldsFromPoiJsonList() poiJsonList is null or empty");
            return list;
        }
        Log.m285d("PoIUtils", "removeOptionalFieldsFromPoiJsonList() poiJsonList.size(): " + list.size());
        ArrayList arrayList = new ArrayList();
        for (String ct : list) {
            arrayList.add(PoIUtils.ct(ct));
        }
        return arrayList;
    }

    public static String ct(String str) {
        List asList = Arrays.asList(SDIConfig.Le);
        if (str != null) {
            try {
                Log.m285d("PoIUtils", "removeOptionalFieldsFromPoiJson() poiJsonStr: " + str);
                JSONObject jSONObject = new JSONObject(str);
                Iterator keys = jSONObject.keys();
                if (keys != null) {
                    String str2;
                    ArrayList arrayList = new ArrayList();
                    while (keys.hasNext()) {
                        str2 = (String) keys.next();
                        if (!asList.contains(str2)) {
                            arrayList.add(str2);
                        }
                    }
                    Iterator it = arrayList.iterator();
                    while (it.hasNext()) {
                        str2 = (String) it.next();
                        if (jSONObject.has(str2)) {
                            jSONObject.remove(str2);
                        }
                    }
                    Log.m285d("PoIUtils", "removeOptionalFieldsFromPoiJson() returning: " + jSONObject.toString());
                    return jSONObject.toString();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.m285d("PoIUtils", "removeOptionalFieldsFromPoiJson() poiJsonStr is null");
        }
        Log.m285d("PoIUtils", "removeOptionalFieldsFromPoiJson() returning: " + str);
        return str;
    }

    public static List<String> m1696a(List<String> list, List<String> list2) {
        if (list == null || list2 == null) {
            Log.m285d("PoIUtils", "mergeLocationPoiListIntoWiFiPoiList() wifiPoiJsonList or locPoiJsonList is null");
        } else {
            try {
                String cr;
                Log.m285d("PoIUtils", "mergeLocationPoiListIntoWiFiPoiList() locPoiJsonList.size(): " + list.size() + " wifiPoiJsonList.size(): " + list2.size());
                ArrayList arrayList = new ArrayList();
                HashSet hashSet = new HashSet();
                for (String str : list2) {
                    cr = PoIUtils.cr(str);
                    if (cr != null && cr.length() > 0) {
                        hashSet.add(cr);
                        arrayList.add(str);
                    }
                }
                for (String str2 : list) {
                    cr = PoIUtils.cr(str2);
                    if (!(cr == null || cr.length() <= 0 || hashSet.contains(cr))) {
                        arrayList.add(str2);
                    }
                }
                Log.m285d("PoIUtils", "mergeLocationPoiListIntoWiFiPoiList() returning mergedList.size(): " + arrayList.size());
                return arrayList;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.m285d("PoIUtils", "mergeLocationPoiListIntoWiFiPoiList() returning null");
        return null;
    }
}
