/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.Intent
 *  android.os.Bundle
 *  android.os.Handler
 *  android.os.Message
 *  java.io.Serializable
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.Iterator
 */
package com.samsung.contextservice.system;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.samsung.contextclient.data.Location;
import com.samsung.contextclient.data.Poi;
import com.samsung.contextclient.data.PoiTrigger;
import com.samsung.contextservice.a;
import com.samsung.contextservice.a.d;
import com.samsung.contextservice.b.b;
import com.samsung.contextservice.server.ServerListener;
import com.samsung.contextservice.server.e;
import com.samsung.contextservice.server.models.PolicyResponseData;
import com.samsung.contextservice.system.FetchCacheListener;
import com.samsung.contextservice.system.ManagerHub;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class c
extends a {
    private static c HF = null;
    private d HG = null;
    private com.samsung.contextservice.a.c HH = null;
    private com.samsung.contextservice.system.a HI = null;
    private e Hc = null;
    private Context sContext = null;

    private c(Context context) {
        super(context, "VerdictManager");
        if (context != null) {
            this.sContext = context.getApplicationContext();
            this.HG = new d(context);
            this.HH = new com.samsung.contextservice.a.c(context);
            this.Hc = (e)ManagerHub.Z(2);
            this.HI = (com.samsung.contextservice.system.a)ManagerHub.Z(1);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private void a(ArrayList<Poi> arrayList, boolean bl) {
        Iterator iterator;
        ArrayList arrayList2;
        ArrayList arrayList3;
        if (arrayList != null && arrayList.size() > 0) {
            new ArrayList();
            arrayList3 = new ArrayList();
            arrayList2 = new ArrayList();
            iterator = arrayList.iterator();
        } else {
            if (arrayList == null) {
                b.d("VerdictManager", "no poi in the cache");
                return;
            }
            b.d("VerdictManager", "no poi is found");
            return;
        }
        while (iterator.hasNext()) {
            Poi poi = (Poi)iterator.next();
            if (poi.getTrigger() == null) continue;
            if ("DEALS".equalsIgnoreCase(poi.getTrigger().getPurpose())) {
                b.d("VerdictManager", "Broadcast POI: " + poi.toString());
                arrayList3.add((Object)poi);
                continue;
            }
            arrayList2.add((Object)poi);
            b.d("VerdictManager", "Trigger POI: " + poi.toString());
        }
        if (bl) {
            if (arrayList3.size() > 0) {
                this.b((ArrayList<Poi>)arrayList3);
            }
            if (arrayList2.size() <= 0) return;
            {
                this.Hc.b((ArrayList<Poi>)arrayList2, null);
                return;
            }
        } else {
            if (arrayList3.size() <= 0) return;
            {
                this.c((ArrayList<Poi>)arrayList3);
                return;
            }
        }
    }

    public static c aF(Context context) {
        Class<c> class_ = c.class;
        synchronized (c.class) {
            if (HF == null) {
                HF = new c(context);
            }
            c c2 = HF;
            // ** MonitorExit[var3_1] (shouldn't be in output)
            return c2;
        }
    }

    public static c aG(Context context) {
        Class<c> class_ = c.class;
        synchronized (c.class) {
            if (HF == null) {
                HF = new c(context);
            }
            c c2 = HF;
            // ** MonitorExit[var3_1] (shouldn't be in output)
            return c2;
        }
    }

    private void b(ArrayList<Poi> arrayList) {
        if (arrayList == null || arrayList.size() <= 0) {
            return;
        }
        Intent intent = new Intent();
        intent.setAction("com.samsung.contextclient.ACTION_ON_POI_FOUND");
        intent.putExtra("pois", arrayList);
        ManagerHub.getContext().sendBroadcast(intent);
        b.d("VerdictManager", "send broadcast " + intent.toString());
    }

    private void c(ArrayList<Poi> arrayList) {
        if (arrayList == null || arrayList.size() <= 0) {
            return;
        }
        Intent intent = new Intent();
        intent.setAction("com.samsung.contextclient.ACTION_ON_POI_LOST");
        intent.putExtra("pois", arrayList);
        ManagerHub.getContext().sendBroadcast(intent);
        b.d("VerdictManager", "send broadcast " + intent.toString());
    }

    public ArrayList<Poi> a(double d2, double d3, double d4, double d5) {
        return this.a(d2, d3, d4, d5, null);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public ArrayList<Poi> a(double d2, double d3, double d4, double d5, FetchCacheListener fetchCacheListener) {
        if (d4 < 0.0) {
            return null;
        }
        ArrayList<String> arrayList = com.samsung.contextservice.b.d.bQ(com.samsung.contextservice.b.d.d(d2, d3));
        boolean bl = this.HG.a(null, true, arrayList);
        if (!bl) {
            Location location = new Location(d2, d3);
            location.setRadius(d4);
            this.c(location, fetchCacheListener);
            b.d("VerdictManager", "searchPoisByGeo, no cache exists, fetching from server...");
        }
        if (bl) {
            b.d("VerdictManager", "searchPoisByGeo, cache exists locally");
            do {
                return this.HG.a(d2, d3, d4, d5, null);
                break;
            } while (true);
        }
        b.d("VerdictManager", "searchPoisByGeo, cache does not exist and search for deals and offers");
        return this.HG.a(d2, d3, d4, d5, null);
    }

    public boolean a(double d2, double d3) {
        return this.bP(com.samsung.contextservice.b.d.d(d2, d3));
    }

    public void b(double d2, double d3, double d4, double d5) {
        Message message = new Message();
        message.what = 3;
        Bundle bundle = new Bundle();
        bundle.putDouble("x", d2);
        bundle.putDouble("Y", d3);
        bundle.putDouble("radius", d4);
        bundle.putDouble("poiradius", d5);
        message.setData(bundle);
        this.getHandler().sendMessage(message);
    }

    public PolicyResponseData bI(String string) {
        return this.HH.bI(string);
    }

    public boolean bP(String string) {
        if (string == null) {
            return true;
        }
        ArrayList<String> arrayList = com.samsung.contextservice.b.d.bQ(string);
        return this.HG.a(null, true, arrayList);
    }

    @Override
    public void c(Message message) {
        if (message == null) {
            return;
        }
        switch (message.what) {
            default: {
                return;
            }
            case 2: {
                this.a((ArrayList<Poi>)message.getData().getParcelableArrayList("poi"), true);
                return;
            }
            case 3: {
                Bundle bundle = message.getData();
                b.d("VerdictManager", "search and notify:" + bundle.getDouble("x") + ", " + bundle.getDouble("Y") + ", " + bundle.getDouble("radius"));
                this.a(this.a(bundle.getDouble("x"), bundle.getDouble("Y"), bundle.getDouble("radius"), bundle.getDouble("poiradius")), true);
                return;
            }
            case 4: 
        }
        this.a((ArrayList<Poi>)message.getData().getParcelableArrayList("poi"), false);
    }

    public void c(Location location, ServerListener serverListener) {
        this.Hc.b(location, serverListener);
    }

    public void d(ArrayList<Poi> arrayList) {
        b.d("VerdictManager", "notifyCallerOnPoiFound");
        if (arrayList == null) {
            return;
        }
        Message message = new Message();
        message.what = 2;
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("poi", arrayList);
        message.setData(bundle);
        this.getHandler().sendMessage(message);
    }

    public void e(ArrayList<Poi> arrayList) {
        b.d("VerdictManager", "notifyCallerOnPoiLost");
        if (arrayList == null) {
            return;
        }
        Message message = new Message();
        message.what = 4;
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("poi", arrayList);
        message.setData(bundle);
        this.getHandler().sendMessage(message);
    }
}

