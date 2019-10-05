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
 *  java.util.Collection
 *  java.util.List
 */
package com.samsung.contextservice.system;

import android.content.Context;
import android.location.Location;

import com.samsung.android.spayfw.b.Log;
import com.samsung.contextclient.data.Poi;
import com.samsung.contextservice.server.models.PolicyResponseData;
import com.samsung.sensorframework.a;
import com.samsung.sensorframework.sda.b.a.p;
import java.util.ArrayList;
import java.util.List;

public class b
implements a {
    private static b Hn;
    private static final Object Ho;
    protected com.samsung.sensorframework.b Hp;
    protected c Hq;
    protected Context context;

    static {
        Ho = new Object();
    }

    private b(Context context) {
        this.context = context;
        Log.d("ContextSFManager", "instance created");
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static b aB(Context context) {
        if (Hn == null) {
            Object object;
            Object object2 = object = Ho;
            synchronized (object2) {
                if (Hn == null) {
                    Hn = new b(context);
                }
            }
        }
        return Hn;
    }

    public static void aC(Context context) {
        b b2 = b.aB(context);
        if (b2 != null) {
            b2.gE();
        }
    }

    public static void aD(Context context) {
        b b2 = b.aB(context);
        if (b2 != null) {
            b2.gF();
        }
    }

    private ArrayList<Poi> h(List<String> list) {
        ArrayList arrayList = new ArrayList();
        if (list != null && list.size() > 0) {
            Log.d("ContextSFManager", "convertPoIJsonToPoIObjectList() size: " + list.size());
            for (String string : list) {
                if (string != null && string.length() > 0) {
                    Poi poi = Poi.toPoiFromJson(string);
                    if (poi != null) {
                        arrayList.add((Object)poi);
                        Log.d("ContextSFManager", "convertPoIJsonToPoIObjectList() Poi: " + poi.getId());
                        continue;
                    }
                    Log.d("ContextSFManager", "convertPoIJsonToPoIObjectList() poi is null");
                    continue;
                }
                Log.d("ContextSFManager", "convertPoIJsonToPoIObjectList() jsonStr is null");
            }
        } else {
            Log.d("ContextSFManager", "convertPoIJsonToPoIObjectList() poiJsonList size is zero or null");
        }
        return arrayList;
    }

    @Override
    public List<String> b(double d2, double d3, int n2) {
        ArrayList arrayList;
        block5 : {
            block4 : {
                block3 : {
                    ArrayList<Poi> arrayList2;
                    Log.d("ContextSFManager", "queryPoICache()");
                    if (this.Hq == null) break block3;
                    ArrayList arrayList3 = new ArrayList();
                    ArrayList<Poi> arrayList4 = this.Hq.a(d2, d3, n2, 500.0);
                    if (arrayList4 != null) {
                        arrayList3.addAll(arrayList4);
                    }
                    if ((arrayList2 = this.Hq.a(d2, d3, 80467.0, 80467.0)) != null) {
                        arrayList3.addAll(arrayList2);
                    }
                    if (arrayList3.size() <= 0) break block4;
                    arrayList = new ArrayList();
                    for (Poi poi : arrayList3) {
                        String string;
                        if (poi == null || (string = poi.toJson()) == null || string.length() <= 0) continue;
                        arrayList.add((Object)string);
                    }
                    break block5;
                }
                Log.d("ContextSFManager", "queryPoICache() verdictManager is null");
            }
            return null;
        }
        return arrayList;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void gE() {
        Log.d("ContextSFManager", "startContextSFManager()");
        try {
            if (this.context != null) {
                this.Hp = com.samsung.sensorframework.b.aM(this.context);
                if (this.Hp != null) {
                    this.Hp.gM();
                    this.Hp.a(this);
                } else {
                    Log.d("ContextSFManager", "startContextSFManager() sfManager is null");
                }
                this.Hq = c.aG(this.context);
                return;
            }
            Log.d("ContextSFManager", "startContextSFManager() context is null");
            return;
        }
        catch (Exception exception) {
            Log.e("ContextSFManager", "error in startContextSFManager()");
            exception.printStackTrace();
            return;
        }
    }

    public void gF() {
        Log.d("ContextSFManager", "stopContextSFManager()");
        try {
            if (this.Hp != null) {
                this.Hp.gN();
                return;
            }
            Log.d("ContextSFManager", "stopContextSFManager() sfManager is null");
            return;
        }
        catch (Exception exception) {
            Log.e("ContextSFManager", "error in disabling sensor services");
            exception.printStackTrace();
            return;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public String gG() {
        Log.d("ContextSFManager", "queryContextSensingPolicy()");
        if (this.Hq != null) {
            PolicyResponseData policyResponseData = this.Hq.bI("ALL_POLICIES");
            String string = null;
            if (policyResponseData == null) return string;
            return policyResponseData.toJson();
        }
        Log.d("ContextSFManager", "queryContextSensingPolicy() verdictManager is null");
        return null;
    }

    /*
     * Enabled aggressive block sorting
     */
    public Location getLastLocation() {
        p p2;
        Location location;
        block4 : {
            block3 : {
                block2 : {
                    p p3;
                    Log.d("ContextSFManager", "getLastLocation()");
                    if (this.Hp == null) break block2;
                    com.samsung.sensorframework.sda.b.a a2 = this.Hp.aa(5004);
                    if (a2 == null || !(a2 instanceof p) || (p3 = (p)a2) == null) break block3;
                    location = p3.getLocation();
                    break block4;
                }
                Log.d("ContextSFManager", "getLastLocation() sfManager is null");
            }
            location = null;
        }
        if (location == null && this.context != null && (p2 = com.samsung.sensorframework.sda.f.a.bn(this.context)) != null) {
            location = p2.getLocation();
        }
        if (location == null) {
            Log.d("ContextSFManager", "getLastLocation() returning null location");
            return location;
        }
        Log.d("ContextSFManager", "getLastLocation() returning a not null location");
        return location;
    }

    @Override
    public void i(List<String> list) {
        ArrayList<Poi> arrayList = this.h(list);
        if (arrayList != null && arrayList.size() > 0) {
            if (this.Hq != null) {
                Log.d("ContextSFManager", "onNearbyPoIs() calling verdictManager.notifyCallerWithPoi()");
                this.Hq.d(arrayList);
                return;
            }
            Log.d("ContextSFManager", "onNearbyPoIs() verdictManager is null");
            return;
        }
        Log.d("ContextSFManager", "onNearbyPoIs() poisToNotify size is zero or null");
    }

    @Override
    public void j(List<String> list) {
        ArrayList<Poi> arrayList = this.h(list);
        if (arrayList != null && arrayList.size() > 0) {
            if (this.Hq != null) {
                Log.d("ContextSFManager", "onNearbyPoIs() calling verdictManager.onPoiLostBroadcast()");
                this.Hq.e(arrayList);
                return;
            }
            Log.d("ContextSFManager", "onExitPoIs() verdictManager is null");
            return;
        }
        Log.d("ContextSFManager", "onExitPoIs() poisToNotify size is zero or null");
    }
}

