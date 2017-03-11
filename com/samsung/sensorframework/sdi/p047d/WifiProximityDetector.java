package com.samsung.sensorframework.sdi.p047d;

import android.content.Context;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.sensorframework.sda.p043f.DataAcquisitionUtils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/* renamed from: com.samsung.sensorframework.sdi.d.d */
public class WifiProximityDetector {
    public static List<String> m1700a(Context context, List<String> list) {
        ArrayList arrayList = null;
        Log.m285d("WifiProximityDetector", "getNearbyPoIs()");
        if (list == null || list.size() <= 0) {
            Log.m285d("WifiProximityDetector", "getNearbyPoIs() location is invalid or poiCache is null or empty");
            return null;
        }
        List<String> list2 = null;
        for (String str : list) {
            String str2;
            if (str2 != null && str2.length() > 0) {
                try {
                    ArrayList arrayList2;
                    ArrayList cq = PoIUtils.cq(str2);
                    if (cq == null || cq.size() <= 0) {
                        Log.m285d("WifiProximityDetector", "getNearbyPoIs() poiWifiSSIDs is null or of zero length");
                        arrayList2 = arrayList;
                        arrayList = list2;
                    } else {
                        Log.m285d("WifiProximityDetector", "getNearbyPoIs() poiWifiSSIDs.size(): " + cq.size());
                        if (arrayList == null) {
                            arrayList = DataAcquisitionUtils.bo(context);
                        }
                        if (arrayList != null && arrayList.size() > 0 && WifiProximityDetector.m1701a(cq, arrayList, 30)) {
                            ArrayList arrayList3;
                            if (list2 == null) {
                                arrayList3 = new ArrayList();
                            }
                            str2 = PoIUtils.m1694a(str2, "WIFI", 50.0d);
                            if (str2 != null && str2.length() > 0) {
                                arrayList3.add(str2);
                            }
                            Log.m285d("WifiProximityDetector", "getNearbyPoIs() Wifi matched for POI: " + PoIUtils.cr(str2));
                            arrayList2 = arrayList;
                            arrayList = arrayList3;
                        }
                        arrayList2 = arrayList;
                        arrayList = list2;
                    }
                    list2 = arrayList;
                    arrayList = arrayList2;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return list2;
    }

    private static HashSet<String> m1702l(ArrayList<String> arrayList) {
        Log.m285d("WifiProximityDetector", "convertListToHashSetLowerCase()");
        if (arrayList == null) {
            return null;
        }
        HashSet<String> hashSet = new HashSet();
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            if (str != null && str.length() > 0) {
                hashSet.add(str.toLowerCase());
            }
        }
        return hashSet;
    }

    private static boolean m1701a(ArrayList<String> arrayList, ArrayList<String> arrayList2, int i) {
        HashSet l = WifiProximityDetector.m1702l(arrayList);
        HashSet l2 = WifiProximityDetector.m1702l(arrayList2);
        if (l == null || l.size() <= 0 || l2 == null || l2.size() <= 0) {
            return false;
        }
        Iterator it = l.iterator();
        int i2 = 0;
        while (it.hasNext()) {
            int i3;
            String str = (String) it.next();
            if (str == null || str.length() <= 0 || !l2.contains(str)) {
                i3 = i2;
            } else {
                i3 = i2 + 1;
            }
            i2 = i3;
        }
        if (i2 <= 0) {
            return false;
        }
        double size = (((double) i2) * 100.0d) / ((double) l.size());
        Log.m285d("WifiProximityDetector", "deviceWifiHashSet.size(): " + l2.size() + " poiWifiHashSet.size(): " + l.size() + " commonSSIDPercent: " + size);
        if (size > ((double) i)) {
            return true;
        }
        return false;
    }
}
