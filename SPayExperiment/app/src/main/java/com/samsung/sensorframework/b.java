/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Thread
 *  java.util.List
 */
package com.samsung.sensorframework;

import android.content.Context;

import com.samsung.android.spayfw.b.Log;
import com.samsung.sensorframework.sdi.c.c;
import java.util.List;

public class b {
    private static b HN;
    private static final Object Ho;
    private a HO;
    private Context context;

    static {
        Ho = new Object();
    }

    private b(Context context) {
        Log.d("SFManager", "SFManager created");
        this.context = context;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static b aM(Context context) {
        if (HN == null) {
            Object object;
            Object object2 = object = Ho;
            synchronized (object2) {
                if (HN == null) {
                    HN = new b(context);
                }
            }
        }
        return HN;
    }

    public void a(a a2) {
        this.HO = a2;
    }

    public com.samsung.sensorframework.sda.b.a aa(int n2) {
        Log.d("SFManager", "getLastSensorData() sensorId: " + n2);
        c c2 = c.br(this.context);
        com.samsung.sensorframework.sda.b.a a2 = null;
        if (c2 != null) {
            a2 = c.br(this.context).aa(n2);
        }
        return a2;
    }

    public List<String> b(double d2, double d3, int n2) {
        Log.d("SFManager", "queryPoICache()");
        if (this.HO != null) {
            return this.HO.b(d2, d3, n2);
        }
        Log.d("SFManager", "queryPoICache() sfContextInterface is null");
        return null;
    }

    public String gG() {
        Log.d("SFManager", "queryContextSensingPolicy()");
        if (this.HO != null) {
            return this.HO.gG();
        }
        Log.d("SFManager", "queryContextSensingPolicy() sfContextInterface is null");
        return null;
    }

    public void gM() {
        new Thread(){

            public void run() {
                Log.d("SFManager", "startSFManager()");
                if (b.this.context != null) {
                    if (c.br(b.this.context) != null) {
                        c.br(b.this.context).hQ();
                        return;
                    }
                    Log.e("SFManager", "SensingController.getInstance() returned null");
                    return;
                }
                Log.d("SFManager", "startSFManager() context is null");
            }
        }.start();
    }

    public void gN() {
        Log.d("SFManager", "stopSFManager()");
        if (c.br(this.context) != null) {
            c.br(this.context).hR();
            return;
        }
        Log.e("SFManager", "stopSFManager() SensingController.getInstance() returned null");
    }

    public void i(List<String> list) {
        Log.d("SFManager", "onNearbyPoIs()");
        if (this.HO != null) {
            this.HO.i(list);
            return;
        }
        Log.d("SFManager", "onNearbyPoIs() sfContextInterface is null");
    }

    public void j(List<String> list) {
        Log.d("SFManager", "onExitPoIs()");
        if (this.HO != null) {
            this.HO.j(list);
            return;
        }
        Log.d("SFManager", "onExitPoIs() sfContextInterface is null");
    }

}

