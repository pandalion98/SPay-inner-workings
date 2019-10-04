/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.HashSet
 *  java.util.Iterator
 *  java.util.List
 */
package com.samsung.sensorframework.sdi.d;

import android.content.Context;
import com.samsung.sensorframework.sda.f.a;
import com.samsung.sensorframework.sdi.d.c;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class d {
    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static List<String> a(Context context, List<String> list) {
        ArrayList<String> arrayList = null;
        com.samsung.android.spayfw.b.c.d("WifiProximityDetector", "getNearbyPoIs()");
        if (list == null || list.size() <= 0) {
            com.samsung.android.spayfw.b.c.d("WifiProximityDetector", "getNearbyPoIs() location is invalid or poiCache is null or empty");
            return null;
        }
        Iterator iterator = list.iterator();
        ArrayList arrayList2 = null;
        while (iterator.hasNext()) {
            ArrayList<String> arrayList3;
            ArrayList arrayList4;
            block10 : {
                String string = (String)iterator.next();
                if (string == null || string.length() <= 0) continue;
                try {
                    ArrayList<String> arrayList5 = c.cq(string);
                    if (arrayList5 != null && arrayList5.size() > 0) {
                        com.samsung.android.spayfw.b.c.d("WifiProximityDetector", "getNearbyPoIs() poiWifiSSIDs.size(): " + arrayList5.size());
                        if (arrayList == null) {
                            arrayList = a.bo(context);
                        }
                        if (arrayList != null && arrayList.size() > 0 && d.a(arrayList5, arrayList, 30)) {
                            String string2;
                            if (arrayList2 == null) {
                                arrayList2 = new ArrayList();
                            }
                            if ((string2 = c.a(string, "WIFI", 50.0)) != null && string2.length() > 0) {
                                arrayList2.add((Object)string2);
                            }
                            com.samsung.android.spayfw.b.c.d("WifiProximityDetector", "getNearbyPoIs() Wifi matched for POI: " + c.cr(string2));
                            arrayList3 = arrayList;
                            arrayList4 = arrayList2;
                            break block10;
                        }
                    } else {
                        com.samsung.android.spayfw.b.c.d("WifiProximityDetector", "getNearbyPoIs() poiWifiSSIDs is null or of zero length");
                    }
                    arrayList3 = arrayList;
                    arrayList4 = arrayList2;
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                    continue;
                }
            }
            arrayList2 = arrayList4;
            arrayList = arrayList3;
        }
        return arrayList2;
    }

    /*
     * Enabled aggressive block sorting
     */
    private static boolean a(ArrayList<String> arrayList, ArrayList<String> arrayList2, int n2) {
        HashSet<String> hashSet = d.l(arrayList);
        HashSet<String> hashSet2 = d.l(arrayList2);
        boolean bl = false;
        if (hashSet == null) return bl;
        int n3 = hashSet.size();
        bl = false;
        if (n3 <= 0) return bl;
        bl = false;
        if (hashSet2 == null) return bl;
        int n4 = hashSet2.size();
        bl = false;
        if (n4 <= 0) return bl;
        Iterator iterator = hashSet.iterator();
        int n5 = 0;
        while (iterator.hasNext()) {
            String string = (String)iterator.next();
            int n6 = string != null && string.length() > 0 && hashSet2.contains((Object)string) ? n5 + 1 : n5;
            n5 = n6;
        }
        bl = false;
        if (n5 <= 0) return bl;
        double d2 = 100.0 * (double)n5 / (double)hashSet.size();
        com.samsung.android.spayfw.b.c.d("WifiProximityDetector", "deviceWifiHashSet.size(): " + hashSet2.size() + " poiWifiHashSet.size(): " + hashSet.size() + " commonSSIDPercent: " + d2);
        double d3 = d2 DCMPL (double)n2;
        bl = false;
        if (d3 <= 0) return bl;
        return true;
    }

    private static HashSet<String> l(ArrayList<String> arrayList) {
        com.samsung.android.spayfw.b.c.d("WifiProximityDetector", "convertListToHashSetLowerCase()");
        if (arrayList == null) {
            return null;
        }
        HashSet hashSet = new HashSet();
        for (String string : arrayList) {
            if (string == null || string.length() <= 0) continue;
            hashSet.add((Object)string.toLowerCase());
        }
        return hashSet;
    }
}

