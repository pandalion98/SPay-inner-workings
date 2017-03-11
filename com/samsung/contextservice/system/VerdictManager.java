package com.samsung.contextservice.system;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import com.samsung.android.spayfw.appinterface.ActivationData;
import com.samsung.contextclient.data.Location;
import com.samsung.contextclient.data.Poi;
import com.samsung.contextservice.Manager;
import com.samsung.contextservice.p028a.PolicyDao;
import com.samsung.contextservice.p028a.RCacheDao;
import com.samsung.contextservice.p029b.CSlog;
import com.samsung.contextservice.p029b.GeoUtils;
import com.samsung.contextservice.server.ContextServerManager;
import com.samsung.contextservice.server.ServerListener;
import com.samsung.contextservice.server.models.PolicyResponseData;
import java.util.ArrayList;
import java.util.Iterator;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;

/* renamed from: com.samsung.contextservice.system.c */
public class VerdictManager extends Manager {
    private static VerdictManager HF;
    private RCacheDao HG;
    private PolicyDao HH;
    private ContextClientManager HI;
    private ContextServerManager Hc;
    private Context sContext;

    static {
        HF = null;
    }

    private VerdictManager(Context context) {
        super(context, "VerdictManager");
        this.sContext = null;
        this.HG = null;
        this.HH = null;
        this.Hc = null;
        this.HI = null;
        if (context != null) {
            this.sContext = context.getApplicationContext();
            this.HG = new RCacheDao(context);
            this.HH = new PolicyDao(context);
            this.Hc = (ContextServerManager) ManagerHub.m1472Z(2);
            this.HI = (ContextClientManager) ManagerHub.m1472Z(1);
        }
    }

    private void m1492a(ArrayList<Poi> arrayList, boolean z) {
        if (arrayList != null && arrayList.size() > 0) {
            ArrayList arrayList2 = new ArrayList();
            ArrayList arrayList3 = new ArrayList();
            ArrayList arrayList4 = new ArrayList();
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                Poi poi = (Poi) it.next();
                if (poi.getTrigger() != null) {
                    if (Poi.PURPOSE_DEAL.equalsIgnoreCase(poi.getTrigger().getPurpose())) {
                        CSlog.m1408d("VerdictManager", "Broadcast POI: " + poi.toString());
                        arrayList3.add(poi);
                    } else {
                        arrayList4.add(poi);
                        CSlog.m1408d("VerdictManager", "Trigger POI: " + poi.toString());
                    }
                }
            }
            if (z) {
                if (arrayList3.size() > 0) {
                    m1493b(arrayList3);
                }
                if (arrayList4.size() > 0) {
                    this.Hc.m1454b(arrayList4, null);
                }
            } else if (arrayList3.size() > 0) {
                m1494c(arrayList3);
            }
        } else if (arrayList == null) {
            CSlog.m1408d("VerdictManager", "no poi in the cache");
        } else {
            CSlog.m1408d("VerdictManager", "no poi is found");
        }
    }

    private void m1493b(ArrayList<Poi> arrayList) {
        if (arrayList != null && arrayList.size() > 0) {
            Intent intent = new Intent();
            intent.setAction("com.samsung.contextclient.ACTION_ON_POI_FOUND");
            intent.putExtra("pois", arrayList);
            ManagerHub.getContext().sendBroadcast(intent);
            CSlog.m1408d("VerdictManager", "send broadcast " + intent.toString());
        }
    }

    private void m1494c(ArrayList<Poi> arrayList) {
        if (arrayList != null && arrayList.size() > 0) {
            Intent intent = new Intent();
            intent.setAction("com.samsung.contextclient.ACTION_ON_POI_LOST");
            intent.putExtra("pois", arrayList);
            ManagerHub.getContext().sendBroadcast(intent);
            CSlog.m1408d("VerdictManager", "send broadcast " + intent.toString());
        }
    }

    public void m1499c(Message message) {
        if (message != null) {
            switch (message.what) {
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                    m1492a(message.getData().getParcelableArrayList("poi"), true);
                case F2m.PPB /*3*/:
                    Bundle data = message.getData();
                    CSlog.m1408d("VerdictManager", "search and notify:" + data.getDouble("x") + ", " + data.getDouble(ActivationData.YES) + ", " + data.getDouble("radius"));
                    m1492a(m1495a(data.getDouble("x"), data.getDouble(ActivationData.YES), data.getDouble("radius"), data.getDouble("poiradius")), true);
                case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                    m1492a(message.getData().getParcelableArrayList("poi"), false);
                default:
            }
        }
    }

    public static synchronized VerdictManager aF(Context context) {
        VerdictManager verdictManager;
        synchronized (VerdictManager.class) {
            if (HF == null) {
                HF = new VerdictManager(context);
            }
            verdictManager = HF;
        }
        return verdictManager;
    }

    public static synchronized VerdictManager aG(Context context) {
        VerdictManager verdictManager;
        synchronized (VerdictManager.class) {
            if (HF == null) {
                HF = new VerdictManager(context);
            }
            verdictManager = HF;
        }
        return verdictManager;
    }

    public ArrayList<Poi> m1495a(double d, double d2, double d3, double d4) {
        return m1496a(d, d2, d3, d4, null);
    }

    public ArrayList<Poi> m1496a(double d, double d2, double d3, double d4, FetchCacheListener fetchCacheListener) {
        if (d3 < 0.0d) {
            return null;
        }
        boolean a = this.HG.m1397a(null, true, GeoUtils.bQ(GeoUtils.m1416d(d, d2)));
        if (!a) {
            Location location = new Location(d, d2);
            location.setRadius(d3);
            m1500c(location, fetchCacheListener);
            CSlog.m1408d("VerdictManager", "searchPoisByGeo, no cache exists, fetching from server...");
        }
        if (a) {
            CSlog.m1408d("VerdictManager", "searchPoisByGeo, cache exists locally");
        } else {
            CSlog.m1408d("VerdictManager", "searchPoisByGeo, cache does not exist and search for deals and offers");
        }
        return this.HG.m1395a(d, d2, d3, d4, null);
    }

    public boolean m1497a(double d, double d2) {
        return bP(GeoUtils.m1416d(d, d2));
    }

    public boolean bP(String str) {
        if (str == null) {
            return true;
        }
        return this.HG.m1397a(null, true, GeoUtils.bQ(str));
    }

    public void m1501d(ArrayList<Poi> arrayList) {
        CSlog.m1408d("VerdictManager", "notifyCallerOnPoiFound");
        if (arrayList != null) {
            Message message = new Message();
            message.what = 2;
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("poi", arrayList);
            message.setData(bundle);
            getHandler().sendMessage(message);
        }
    }

    public void m1502e(ArrayList<Poi> arrayList) {
        CSlog.m1408d("VerdictManager", "notifyCallerOnPoiLost");
        if (arrayList != null) {
            Message message = new Message();
            message.what = 4;
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("poi", arrayList);
            message.setData(bundle);
            getHandler().sendMessage(message);
        }
    }

    public void m1498b(double d, double d2, double d3, double d4) {
        Message message = new Message();
        message.what = 3;
        Bundle bundle = new Bundle();
        bundle.putDouble("x", d);
        bundle.putDouble(ActivationData.YES, d2);
        bundle.putDouble("radius", d3);
        bundle.putDouble("poiradius", d4);
        message.setData(bundle);
        getHandler().sendMessage(message);
    }

    public void m1500c(Location location, ServerListener serverListener) {
        this.Hc.m1452b(location, serverListener);
    }

    public PolicyResponseData bI(String str) {
        return this.HH.bI(str);
    }
}
