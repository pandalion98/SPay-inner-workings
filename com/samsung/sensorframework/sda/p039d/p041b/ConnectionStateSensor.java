package com.samsung.sensorframework.sda.p039d.p041b;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.sensorframework.sda.p036c.p038b.ConnectionStateProcessor;
import java.net.NetworkInterface;

/* renamed from: com.samsung.sensorframework.sda.d.b.d */
public class ConnectionStateSensor extends AbstractPushSensor {
    private static final String[] Jz;
    private static ConnectionStateSensor Kt;
    private static final Object lock;

    static {
        lock = new Object();
        Jz = new String[]{"android.permission.ACCESS_WIFI_STATE", "android.permission.ACCESS_NETWORK_STATE"};
    }

    public static ConnectionStateSensor bd(Context context) {
        if (Kt == null) {
            synchronized (lock) {
                if (Kt == null) {
                    Kt = new ConnectionStateSensor(context);
                }
            }
        }
        return Kt;
    }

    private ConnectionStateSensor(Context context) {
        super(context);
    }

    public void gY() {
        super.gY();
        Kt = null;
    }

    protected void m1620a(Context context, Intent intent) {
        if (this.Ji) {
            try {
                m1613a(((ConnectionStateProcessor) hi()).m1564a(System.currentTimeMillis(), this.Id.gS(), ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo(), ((WifiManager) context.getSystemService("wifi")).getConnectionInfo(), NetworkInterface.getNetworkInterfaces()));
                return;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        Log.m285d(he(), "logOnDataSensed() called while not sensing.");
    }

    protected String[] hb() {
        return Jz;
    }

    protected IntentFilter[] hC() {
        return new IntentFilter[]{new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")};
    }

    protected String he() {
        return "ConnectionStateSensor";
    }

    public int getSensorType() {
        return 5011;
    }

    protected boolean hc() {
        return true;
    }

    protected void hd() {
    }
}
