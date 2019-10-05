/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.BroadcastReceiver
 *  android.content.Context
 *  android.content.Intent
 *  android.content.IntentFilter
 *  android.net.wifi.ScanResult
 *  android.net.wifi.WifiManager
 *  android.os.Handler
 *  java.lang.Exception
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.Iterator
 *  java.util.List
 */
package com.samsung.sensorframework.sda.d.a;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import com.samsung.android.spayfw.b.Log;
import com.samsung.sensorframework.sda.b.a;
import com.samsung.sensorframework.sda.b.a.y;
import com.samsung.sensorframework.sda.b.a.z;
import com.samsung.sensorframework.sda.c.a.o;

import java.util.ArrayList;
import java.util.Iterator;

public class q
extends b {
    private static final String[] Jz;
    private static q Ki;
    private static final Object lock;
    private int Jw;
    private WifiManager Kf;
    private BroadcastReceiver Kg;
    private ArrayList<z> Kh;
    private y Kj;

    static {
        lock = new Object();
        Jz = new String[]{"android.permission.ACCESS_WIFI_STATE", "android.permission.ACCESS_NETWORK_STATE", "android.permission.CHANGE_WIFI_STATE"};
    }

    private q(Context context) {
        super(context);
        this.Kf = (WifiManager)context.getSystemService("wifi");
        this.Kg = new BroadcastReceiver(){

            public void onReceive(Context context, Intent intent) {
                Iterator iterator = q.this.Kf.getScanResults().iterator();
                while (iterator.hasNext()) {
                    z z2 = new z((ScanResult)iterator.next());
                    q.this.Kh.add((Object)z2);
                }
                q.this.Jw = -1 + q.this.Jw;
                if (q.this.Jw > 0 && q.this.Kf.isWifiEnabled()) {
                    q.this.Kf.startScan();
                    return;
                }
                q.this.ho();
            }
        };
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static q ba(Context context) {
        if (Ki == null) {
            Object object;
            Object object2 = object = lock;
            synchronized (object2) {
                if (Ki == null) {
                    Ki = new q(context);
                }
            }
        }
        return Ki;
    }

    @Override
    public void gY() {
        super.gY();
        Ki = null;
    }

    @Override
    public int getSensorType() {
        return 5010;
    }

    protected y hB() {
        return this.Kj;
    }

    @Override
    protected String[] hb() {
        return Jz;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    protected boolean hc() {
        try {
            this.Kh = null;
            if (!this.Kf.isWifiEnabled()) {
                return false;
            }
            this.Kh = new ArrayList();
            this.Jw = (Integer)this.Id.getParameter("NUMBER_OF_SENSE_CYCLES");
            String string = com.samsung.sensorframework.sda.a.b.gO() != null ? com.samsung.sensorframework.sda.a.b.gO().gR() : null;
            Log.d(this.he(), " intentBroadcasterPermission: " + string);
            this.HR.registerReceiver(this.Kg, new IntentFilter("android.net.wifi.SCAN_RESULTS"), string, this.gZ());
            this.Kf.startScan();
            return true;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    @Override
    protected void hd() {
        try {
            this.HR.unregisterReceiver(this.Kg);
            return;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return;
        }
    }

    @Override
    protected String he() {
        return "WifiSensor";
    }

    @Override
    protected /* synthetic */ a hl() {
        return this.hB();
    }

    @Override
    protected void hm() {
        this.Kj = ((o)this.hi()).b(this.Jn, this.Kh, this.Id.gS());
    }

}

