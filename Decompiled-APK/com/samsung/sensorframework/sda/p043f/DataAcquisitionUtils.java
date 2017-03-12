package com.samsung.sensorframework.sda.p043f;

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
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.sensorframework.sda.p030a.GlobalConfig;
import com.samsung.sensorframework.sda.p033b.p034a.LocationData;
import com.samsung.sensorframework.sda.p039d.SensorUtils;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/* renamed from: com.samsung.sensorframework.sda.f.a */
public class DataAcquisitionUtils {

    /* renamed from: com.samsung.sensorframework.sda.f.a.a */
    class DataAcquisitionUtils extends BroadcastReceiver {
        boolean KU;
        final /* synthetic */ WifiManager KV;
        final /* synthetic */ ArrayList KW;

        /* renamed from: com.samsung.sensorframework.sda.f.a.a.1 */
        class DataAcquisitionUtils extends Thread {
            final /* synthetic */ DataAcquisitionUtils KX;

            DataAcquisitionUtils(DataAcquisitionUtils dataAcquisitionUtils) {
                this.KX = dataAcquisitionUtils;
            }

            public void run() {
                List<ScanResult> scanResults = this.KX.KV.getScanResults();
                if (scanResults != null) {
                    for (ScanResult scanResult : scanResults) {
                        if (scanResult != null) {
                            this.KX.KW.add(scanResult.BSSID);
                        }
                    }
                }
                this.KX.KU = true;
            }
        }

        DataAcquisitionUtils(WifiManager wifiManager, ArrayList arrayList) {
            this.KV = wifiManager;
            this.KW = arrayList;
            this.KU = false;
        }

        public void onReceive(Context context, Intent intent) {
            new DataAcquisitionUtils(this).start();
        }

        public boolean hJ() {
            return this.KU;
        }
    }

    public static boolean bl(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
            Log.m285d("DataAcquisitionUtils", "isConnectToNetwork() false");
            return false;
        }
        Log.m285d("DataAcquisitionUtils", "isConnectToNetwork() true");
        return true;
    }

    public static boolean m1649c(Context context, int i) {
        int bm = DataAcquisitionUtils.bm(context);
        Log.m285d("DataAcquisitionUtils", "isBatteryLow() batteryPercent: " + bm);
        if (bm <= i) {
            Log.m285d("DataAcquisitionUtils", "isBatteryLow() returning true as battery <= " + i);
            return true;
        }
        Log.m285d("DataAcquisitionUtils", "isBatteryLow() returning false as battery > " + i);
        return false;
    }

    public static int bm(Context context) {
        int i = 100;
        int i2 = -1;
        Intent registerReceiver = context.registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        if (registerReceiver != null) {
            i2 = registerReceiver.getIntExtra("level", -1);
            i = registerReceiver.getIntExtra("scale", 100);
        }
        i = (int) ((((float) i2) / ((float) i)) * 100.0f);
        Log.m285d("DataAcquisitionUtils", "getCurrentBatteryLevel() batteryPercent: " + i);
        return i;
    }

    public static String m1647G(long j) {
        return new SimpleDateFormat("HH:mm:ss:SSS MMM-dd-yyyy Z z", Locale.US).format(Long.valueOf(j));
    }

    public static LocationData bn(Context context) {
        LocationData locationData;
        if (context != null) {
            LocationManager locationManager = (LocationManager) context.getSystemService("location");
            if (locationManager != null) {
                Location lastKnownLocation;
                try {
                    lastKnownLocation = locationManager.getLastKnownLocation("passive");
                } catch (SecurityException e) {
                    Log.m285d("DataAcquisitionUtils", "getLastKnownLocation() SecurityException");
                    lastKnownLocation = null;
                }
                if (lastKnownLocation != null) {
                    locationData = new LocationData(lastKnownLocation.getTime(), SensorUtils.ao(5004), 5004);
                    locationData.m1521a(lastKnownLocation);
                    if (locationData != null) {
                        Log.m285d("DataAcquisitionUtils", "getLastKnownLocation() returning a null location");
                    } else {
                        Log.m285d("DataAcquisitionUtils", "getLastKnownLocation() returning a not null location");
                    }
                    return locationData;
                }
            }
        }
        locationData = null;
        if (locationData != null) {
            Log.m285d("DataAcquisitionUtils", "getLastKnownLocation() returning a not null location");
        } else {
            Log.m285d("DataAcquisitionUtils", "getLastKnownLocation() returning a null location");
        }
        return locationData;
    }

    public static boolean m1648a(LocationData locationData) {
        if (locationData != null) {
            Location location = locationData.getLocation();
            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                if (!(latitude == 0.0d && longitude == 0.0d)) {
                    Log.m285d("DataAcquisitionUtils", "isValidLocation() true");
                    return true;
                }
            }
        }
        Log.m285d("DataAcquisitionUtils", "isValidLocation() false");
        return false;
    }

    public static String ap(int i) {
        try {
            return SensorUtils.ap(i);
        } catch (Exception e) {
            e.printStackTrace();
            return "Unknown";
        }
    }

    public static ArrayList<String> bo(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
        if (wifiManager == null || !wifiManager.isWifiEnabled()) {
            return null;
        }
        String gR;
        ArrayList<String> arrayList = new ArrayList();
        BroadcastReceiver dataAcquisitionUtils = new DataAcquisitionUtils(wifiManager, arrayList);
        if (GlobalConfig.gO() != null) {
            gR = GlobalConfig.gO().gR();
        } else {
            gR = null;
        }
        Log.m285d("DataAcquisitionUtils", " intentBroadcasterPermission: " + gR);
        context.registerReceiver(dataAcquisitionUtils, new IntentFilter("android.net.wifi.SCAN_RESULTS"), gR, null);
        wifiManager.startScan();
        for (int i = 0; i < 15; i++) {
            if (dataAcquisitionUtils.hJ()) {
                return arrayList;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
