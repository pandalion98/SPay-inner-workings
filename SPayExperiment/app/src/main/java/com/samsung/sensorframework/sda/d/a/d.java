/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.bluetooth.BluetoothAdapter
 *  android.bluetooth.BluetoothDevice
 *  android.content.BroadcastReceiver
 *  android.content.Context
 *  android.content.Intent
 *  android.content.IntentFilter
 *  android.os.Parcelable
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.util.ArrayList
 *  java.util.HashMap
 *  java.util.Set
 */
package com.samsung.sensorframework.sda.d.a;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Parcelable;
import com.samsung.android.spayfw.b.c;
import com.samsung.sensorframework.sda.b.a;
import com.samsung.sensorframework.sda.b.a.r;
import com.samsung.sensorframework.sda.d.a.b;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class d
extends b {
    private static d Jx;
    private static final String[] Jz;
    private static final Object lock;
    private ArrayList<r> Jt;
    private HashMap<String, r> Ju;
    private BluetoothAdapter Jv = BluetoothAdapter.getDefaultAdapter();
    private int Jw;
    private com.samsung.sensorframework.sda.b.a.d Jy;

    static {
        lock = new Object();
        Jz = new String[]{"android.permission.BLUETOOTH", "android.permission.BLUETOOTH_ADMIN"};
    }

    private d(Context context) {
        super(context);
        if (this.Jv == null) {
            c.d("BluetoothSensor", "Device does not support Bluetooth");
            return;
        }
        this.Ju = new HashMap();
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver(){

            /*
             * Enabled aggressive block sorting
             */
            public void onReceive(Context context, Intent intent) {
                String string = intent.getAction();
                if ("android.bluetooth.device.action.FOUND".equals((Object)string)) {
                    String string2 = ((BluetoothDevice)intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE")).getAddress();
                    String string3 = ((BluetoothDevice)intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE")).getName();
                    short s2 = intent.getShortExtra("android.bluetooth.device.extra.RSSI", (short)-32768);
                    r r2 = new r(System.currentTimeMillis(), string2, string3, s2);
                    if (d.this.Jt == null || d.this.Jt.contains((Object)r2)) return;
                    {
                        d.this.Jt.add((Object)r2);
                        return;
                    }
                } else {
                    if ("android.bluetooth.adapter.action.DISCOVERY_FINISHED".equals((Object)string)) {
                        d.this.Jw = -1 + d.this.Jw;
                        if (d.this.Jw > 0) {
                            d.this.Jv.startDiscovery();
                            return;
                        }
                        d.this.ho();
                        return;
                    }
                    if ("android.bluetooth.device.action.ACL_CONNECTED".equals((Object)string)) {
                        String string4 = ((BluetoothDevice)intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE")).getAddress();
                        String string5 = ((BluetoothDevice)intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE")).getName();
                        short s3 = intent.getShortExtra("android.bluetooth.device.extra.RSSI", (short)-32768);
                        if (string4 == null || string4.length() <= 0) return;
                        {
                            r r3 = new r(System.currentTimeMillis(), string4, string5, s3);
                            d.this.Ju.put((Object)string4.toUpperCase(), (Object)r3);
                            return;
                        }
                    } else {
                        String string6;
                        if (!"android.bluetooth.device.action.ACL_DISCONNECTED".equals((Object)string) || (string6 = ((BluetoothDevice)intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE")).getAddress()) == null || string6.length() <= 0 || !d.this.Ju.containsKey((Object)string6.toUpperCase())) return;
                        {
                            d.this.Ju.remove((Object)string6.toUpperCase());
                            return;
                        }
                    }
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter("android.bluetooth.device.action.FOUND");
        IntentFilter intentFilter2 = new IntentFilter("android.bluetooth.adapter.action.DISCOVERY_FINISHED");
        IntentFilter intentFilter3 = new IntentFilter("android.bluetooth.device.action.ACL_CONNECTED");
        IntentFilter intentFilter4 = new IntentFilter("android.bluetooth.device.action.ACL_DISCONNECTED");
        this.HR.registerReceiver(broadcastReceiver, intentFilter);
        this.HR.registerReceiver(broadcastReceiver, intentFilter2);
        this.HR.registerReceiver(broadcastReceiver, intentFilter3);
        this.HR.registerReceiver(broadcastReceiver, intentFilter4);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static d aP(Context context) {
        if (Jx == null) {
            Object object;
            Object object2 = object = lock;
            synchronized (object2) {
                if (Jx == null) {
                    Jx = new d(context);
                }
            }
        }
        return Jx;
    }

    @Override
    public void gY() {
        super.gY();
        Jx = null;
    }

    @Override
    public int getSensorType() {
        return 5003;
    }

    @Override
    protected String[] hb() {
        return Jz;
    }

    @Override
    protected boolean hc() {
        this.Jt = null;
        if (!this.Jv.isEnabled()) {
            return false;
        }
        this.Jt = new ArrayList();
        this.Jw = (Integer)this.Id.getParameter("NUMBER_OF_SENSE_CYCLES");
        this.Jv.startDiscovery();
        return true;
    }

    @Override
    protected void hd() {
        if (this.Jv != null) {
            this.Jv.cancelDiscovery();
        }
    }

    @Override
    protected String he() {
        return "BluetoothSensor";
    }

    @Override
    protected /* synthetic */ a hl() {
        return this.hq();
    }

    @Override
    protected void hm() {
        this.Jy = ((com.samsung.sensorframework.sda.c.a.c)this.hi()).b(this.Jn, this.Jt, this.hr(), this.Id.gS());
    }

    protected com.samsung.sensorframework.sda.b.a.d hq() {
        return this.Jy;
    }

    protected ArrayList<r> hr() {
        ArrayList arrayList = new ArrayList();
        for (String string : this.Ju.keySet()) {
            arrayList.add((Object)((r)this.Ju.get((Object)string)).gV());
        }
        return arrayList;
    }

}

