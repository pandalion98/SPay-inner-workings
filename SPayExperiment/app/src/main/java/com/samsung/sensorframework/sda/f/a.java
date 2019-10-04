/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.BroadcastReceiver
 *  android.content.Context
 *  android.content.Intent
 *  android.content.IntentFilter
 *  android.location.Location
 *  android.location.LocationManager
 *  android.net.ConnectivityManager
 *  android.net.NetworkInfo
 *  android.net.wifi.ScanResult
 *  android.net.wifi.WifiManager
 *  android.os.Handler
 *  java.lang.Exception
 *  java.lang.InterruptedException
 *  java.lang.Object
 *  java.lang.SecurityException
 *  java.lang.String
 *  java.lang.Thread
 *  java.text.SimpleDateFormat
 *  java.util.ArrayList
 *  java.util.List
 *  java.util.Locale
 */
package com.samsung.sensorframework.sda.f;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;
import com.samsung.android.spayfw.b.c;
import com.samsung.sensorframework.sda.a.b;
import com.samsung.sensorframework.sda.b.a.p;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class a {
    public static String G(long l2) {
        return new SimpleDateFormat("HH:mm:ss:SSS MMM-dd-yyyy Z z", Locale.US).format((Object)l2);
    }

    public static boolean a(p p2) {
        Location location;
        if (p2 != null && (location = p2.getLocation()) != null) {
            double d2 = location.getLatitude();
            double d3 = location.getLongitude();
            if (d2 != 0.0 || d3 != 0.0) {
                c.d("DataAcquisitionUtils", "isValidLocation() true");
                return true;
            }
        }
        c.d("DataAcquisitionUtils", "isValidLocation() false");
        return false;
    }

    public static String ap(int n2) {
        try {
            String string = com.samsung.sensorframework.sda.d.c.ap(n2);
            return string;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return "Unknown";
        }
    }

    public static boolean bl(Context context) {
        NetworkInfo networkInfo = ((ConnectivityManager)context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            c.d("DataAcquisitionUtils", "isConnectToNetwork() true");
            return true;
        }
        c.d("DataAcquisitionUtils", "isConnectToNetwork() false");
        return false;
    }

    public static int bm(Context context) {
        int n2 = 100;
        int n3 = -1;
        Intent intent = context.registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        if (intent != null) {
            n3 = intent.getIntExtra("level", n3);
            n2 = intent.getIntExtra("scale", n2);
        }
        int n4 = (int)(100.0f * ((float)n3 / (float)n2));
        c.d("DataAcquisitionUtils", "getCurrentBatteryLevel() batteryPercent: " + n4);
        return n4;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public static p bn(Context var0) {
        if (var0 == null || (var2_1 = (LocationManager)var0.getSystemService("location")) == null) ** GOTO lbl-1000
        try {
            var4_3 = var5_2 = var2_1.getLastKnownLocation("passive");
        }
        catch (SecurityException var3_5) {
            c.d("DataAcquisitionUtils", "getLastKnownLocation() SecurityException");
            var4_3 = null;
        }
        if (var4_3 != null) {
            var1_4 = new p(var4_3.getTime(), com.samsung.sensorframework.sda.d.c.ao(5004), 5004);
            var1_4.a(var4_3);
        } else lbl-1000: // 2 sources:
        {
            var1_4 = null;
        }
        if (var1_4 == null) {
            c.d("DataAcquisitionUtils", "getLastKnownLocation() returning a null location");
            return var1_4;
        }
        c.d("DataAcquisitionUtils", "getLastKnownLocation() returning a not null location");
        return var1_4;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static ArrayList<String> bo(Context context) {
        WifiManager wifiManager = (WifiManager)context.getSystemService("wifi");
        if (wifiManager != null && wifiManager.isWifiEnabled()) {
            ArrayList arrayList = new ArrayList();
            a a2 = new a(wifiManager, arrayList);
            String string = b.gO() != null ? b.gO().gR() : null;
            c.d("DataAcquisitionUtils", " intentBroadcasterPermission: " + string);
            context.registerReceiver((BroadcastReceiver)a2, new IntentFilter("android.net.wifi.SCAN_RESULTS"), string, null);
            wifiManager.startScan();
            for (int i2 = 0; i2 < 15; ++i2) {
                if (a2.hJ()) {
                    return arrayList;
                }
                try {
                    Thread.sleep((long)1000L);
                    continue;
                }
                catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
        }
        return null;
    }

    public static boolean c(Context context, int n2) {
        int n3 = a.bm(context);
        c.d("DataAcquisitionUtils", "isBatteryLow() batteryPercent: " + n3);
        if (n3 <= n2) {
            c.d("DataAcquisitionUtils", "isBatteryLow() returning true as battery <= " + n2);
            return true;
        }
        c.d("DataAcquisitionUtils", "isBatteryLow() returning false as battery > " + n2);
        return false;
    }

    class a
    extends BroadcastReceiver {
        boolean KU = false;
        final /* synthetic */ ArrayList KW;

        a() {
            this.KW = var2_2;
        }

        public boolean hJ() {
            return this.KU;
        }

        public void onReceive(Context context, Intent intent) {
            new Thread(){

                public void run() {
                    List list = KV.getScanResults();
                    if (list != null) {
                        for (ScanResult scanResult : list) {
                            if (scanResult == null) continue;
                            a.this.KW.add((Object)scanResult.BSSID);
                        }
                    }
                    a.this.KU = true;
                }
            }.start();
        }

    }

}

