package com.samsung.contextservice.system;

import android.content.Context;
import android.location.Location;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.contextclient.data.Poi;
import com.samsung.contextservice.server.models.PolicyResponseData;
import com.samsung.sensorframework.SFContextInterface;
import com.samsung.sensorframework.SFManager;
import com.samsung.sensorframework.sda.p033b.SensorData;
import com.samsung.sensorframework.sda.p033b.p034a.LocationData;
import com.samsung.sensorframework.sda.p043f.DataAcquisitionUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/* renamed from: com.samsung.contextservice.system.b */
public class ContextSFManager implements SFContextInterface {
    private static ContextSFManager Hn;
    private static final Object Ho;
    protected SFManager Hp;
    protected VerdictManager Hq;
    protected Context context;

    static {
        Ho = new Object();
    }

    public static ContextSFManager aB(Context context) {
        if (Hn == null) {
            synchronized (Ho) {
                if (Hn == null) {
                    Hn = new ContextSFManager(context);
                }
            }
        }
        return Hn;
    }

    private ContextSFManager(Context context) {
        this.context = context;
        Log.m285d("ContextSFManager", "instance created");
    }

    public static void aC(Context context) {
        ContextSFManager aB = ContextSFManager.aB(context);
        if (aB != null) {
            aB.gE();
        }
    }

    public static void aD(Context context) {
        ContextSFManager aB = ContextSFManager.aB(context);
        if (aB != null) {
            aB.gF();
        }
    }

    public void gE() {
        Log.m285d("ContextSFManager", "startContextSFManager()");
        try {
            if (this.context != null) {
                this.Hp = SFManager.aM(this.context);
                if (this.Hp != null) {
                    this.Hp.gM();
                    this.Hp.m1504a((SFContextInterface) this);
                } else {
                    Log.m285d("ContextSFManager", "startContextSFManager() sfManager is null");
                }
                this.Hq = VerdictManager.aG(this.context);
                return;
            }
            Log.m285d("ContextSFManager", "startContextSFManager() context is null");
        } catch (Exception e) {
            Log.m286e("ContextSFManager", "error in startContextSFManager()");
            e.printStackTrace();
        }
    }

    public void gF() {
        Log.m285d("ContextSFManager", "stopContextSFManager()");
        try {
            if (this.Hp != null) {
                this.Hp.gN();
            } else {
                Log.m285d("ContextSFManager", "stopContextSFManager() sfManager is null");
            }
        } catch (Exception e) {
            Log.m286e("ContextSFManager", "error in disabling sensor services");
            e.printStackTrace();
        }
    }

    public List<String> m1489b(double d, double d2, int i) {
        Log.m285d("ContextSFManager", "queryPoICache()");
        if (this.Hq != null) {
            List<Poi> arrayList = new ArrayList();
            Collection a = this.Hq.m1495a(d, d2, (double) i, Poi.RADIUS_SMALL);
            if (a != null) {
                arrayList.addAll(a);
            }
            a = this.Hq.m1495a(d, d2, Poi.RADIUS_MEDIUM, Poi.RADIUS_MEDIUM);
            if (a != null) {
                arrayList.addAll(a);
            }
            if (arrayList.size() <= 0) {
                return null;
            }
            List<String> arrayList2 = new ArrayList();
            for (Poi poi : arrayList) {
                if (poi != null) {
                    String toJson = poi.toJson();
                    if (toJson != null && toJson.length() > 0) {
                        arrayList2.add(toJson);
                    }
                }
            }
            return arrayList2;
        }
        Log.m285d("ContextSFManager", "queryPoICache() verdictManager is null");
        return null;
    }

    public String gG() {
        PolicyResponseData bI;
        Log.m285d("ContextSFManager", "queryContextSensingPolicy()");
        if (this.Hq != null) {
            bI = this.Hq.bI("ALL_POLICIES");
        } else {
            Log.m285d("ContextSFManager", "queryContextSensingPolicy() verdictManager is null");
            bI = null;
        }
        if (bI != null) {
            return bI.toJson();
        }
        return null;
    }

    private ArrayList<Poi> m1488h(List<String> list) {
        ArrayList<Poi> arrayList = new ArrayList();
        if (list == null || list.size() <= 0) {
            Log.m285d("ContextSFManager", "convertPoIJsonToPoIObjectList() poiJsonList size is zero or null");
        } else {
            Log.m285d("ContextSFManager", "convertPoIJsonToPoIObjectList() size: " + list.size());
            for (String str : list) {
                if (str == null || str.length() <= 0) {
                    Log.m285d("ContextSFManager", "convertPoIJsonToPoIObjectList() jsonStr is null");
                } else {
                    Poi toPoiFromJson = Poi.toPoiFromJson(str);
                    if (toPoiFromJson != null) {
                        arrayList.add(toPoiFromJson);
                        Log.m285d("ContextSFManager", "convertPoIJsonToPoIObjectList() Poi: " + toPoiFromJson.getId());
                    } else {
                        Log.m285d("ContextSFManager", "convertPoIJsonToPoIObjectList() poi is null");
                    }
                }
            }
        }
        return arrayList;
    }

    public void m1490i(List<String> list) {
        ArrayList h = m1488h(list);
        if (h == null || h.size() <= 0) {
            Log.m285d("ContextSFManager", "onNearbyPoIs() poisToNotify size is zero or null");
        } else if (this.Hq != null) {
            Log.m285d("ContextSFManager", "onNearbyPoIs() calling verdictManager.notifyCallerWithPoi()");
            this.Hq.m1501d(h);
        } else {
            Log.m285d("ContextSFManager", "onNearbyPoIs() verdictManager is null");
        }
    }

    public void m1491j(List<String> list) {
        ArrayList h = m1488h(list);
        if (h == null || h.size() <= 0) {
            Log.m285d("ContextSFManager", "onExitPoIs() poisToNotify size is zero or null");
        } else if (this.Hq != null) {
            Log.m285d("ContextSFManager", "onNearbyPoIs() calling verdictManager.onPoiLostBroadcast()");
            this.Hq.m1502e(h);
        } else {
            Log.m285d("ContextSFManager", "onExitPoIs() verdictManager is null");
        }
    }

    public Location getLastLocation() {
        Location location;
        Log.m285d("ContextSFManager", "getLastLocation()");
        if (this.Hp != null) {
            SensorData aa = this.Hp.aa(5004);
            if (aa != null && (aa instanceof LocationData)) {
                LocationData locationData = (LocationData) aa;
                if (locationData != null) {
                    location = locationData.getLocation();
                }
            }
            location = null;
        } else {
            Log.m285d("ContextSFManager", "getLastLocation() sfManager is null");
            location = null;
        }
        if (location == null && this.context != null) {
            LocationData bn = DataAcquisitionUtils.bn(this.context);
            if (bn != null) {
                location = bn.getLocation();
            }
        }
        if (location == null) {
            Log.m285d("ContextSFManager", "getLastLocation() returning null location");
        } else {
            Log.m285d("ContextSFManager", "getLastLocation() returning a not null location");
        }
        return location;
    }
}
