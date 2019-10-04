/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.Intent
 *  android.content.IntentFilter
 *  android.net.ConnectivityManager
 *  android.net.NetworkInfo
 *  android.net.wifi.WifiInfo
 *  android.net.wifi.WifiManager
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.net.NetworkInterface
 *  java.util.Enumeration
 */
package com.samsung.sensorframework.sda.d.b;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import com.samsung.android.spayfw.b.c;
import com.samsung.sensorframework.sda.d.b.a;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class d
extends a {
    private static final String[] Jz;
    private static d Kt;
    private static final Object lock;

    static {
        lock = new Object();
        Jz = new String[]{"android.permission.ACCESS_WIFI_STATE", "android.permission.ACCESS_NETWORK_STATE"};
    }

    private d(Context context) {
        super(context);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static d bd(Context context) {
        if (Kt == null) {
            Object object;
            Object object2 = object = lock;
            synchronized (object2) {
                if (Kt == null) {
                    Kt = new d(context);
                }
            }
        }
        return Kt;
    }

    @Override
    protected void a(Context context, Intent intent) {
        if (this.Ji) {
            try {
                NetworkInfo networkInfo = ((ConnectivityManager)context.getSystemService("connectivity")).getActiveNetworkInfo();
                WifiInfo wifiInfo = ((WifiManager)context.getSystemService("wifi")).getConnectionInfo();
                Enumeration enumeration = NetworkInterface.getNetworkInterfaces();
                this.a(((com.samsung.sensorframework.sda.c.b.c)this.hi()).a(System.currentTimeMillis(), this.Id.gS(), networkInfo, wifiInfo, (Enumeration<NetworkInterface>)enumeration));
                return;
            }
            catch (Exception exception) {
                exception.printStackTrace();
                return;
            }
        }
        c.d(this.he(), "logOnDataSensed() called while not sensing.");
    }

    @Override
    public void gY() {
        super.gY();
        Kt = null;
    }

    @Override
    public int getSensorType() {
        return 5011;
    }

    @Override
    protected IntentFilter[] hC() {
        IntentFilter[] arrintentFilter = new IntentFilter[]{new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")};
        return arrintentFilter;
    }

    @Override
    protected String[] hb() {
        return Jz;
    }

    @Override
    protected boolean hc() {
        return true;
    }

    @Override
    protected void hd() {
    }

    @Override
    protected String he() {
        return "ConnectionStateSensor";
    }
}

