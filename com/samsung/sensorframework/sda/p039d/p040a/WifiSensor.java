package com.samsung.sensorframework.sda.p039d.p040a;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.sensorframework.sda.p030a.GlobalConfig;
import com.samsung.sensorframework.sda.p033b.SensorData;
import com.samsung.sensorframework.sda.p033b.p034a.WifiData;
import com.samsung.sensorframework.sda.p033b.p034a.WifiScanResult;
import com.samsung.sensorframework.sda.p036c.p037a.WifiProcessor;
import java.util.ArrayList;

/* renamed from: com.samsung.sensorframework.sda.d.a.q */
public class WifiSensor extends AbstractPullSensor {
    private static final String[] Jz;
    private static WifiSensor Ki;
    private static final Object lock;
    private int Jw;
    private WifiManager Kf;
    private BroadcastReceiver Kg;
    private ArrayList<WifiScanResult> Kh;
    private WifiData Kj;

    /* renamed from: com.samsung.sensorframework.sda.d.a.q.1 */
    class WifiSensor extends BroadcastReceiver {
        final /* synthetic */ WifiSensor Kk;

        WifiSensor(WifiSensor wifiSensor) {
            this.Kk = wifiSensor;
        }

        public void onReceive(Context context, Intent intent) {
            for (ScanResult wifiScanResult : this.Kk.Kf.getScanResults()) {
                this.Kk.Kh.add(new WifiScanResult(wifiScanResult));
            }
            this.Kk.Jw = this.Kk.Jw - 1;
            if (this.Kk.Jw <= 0 || !this.Kk.Kf.isWifiEnabled()) {
                this.Kk.ho();
            } else {
                this.Kk.Kf.startScan();
            }
        }
    }

    protected /* synthetic */ SensorData hl() {
        return hB();
    }

    static {
        lock = new Object();
        Jz = new String[]{"android.permission.ACCESS_WIFI_STATE", "android.permission.ACCESS_NETWORK_STATE", "android.permission.CHANGE_WIFI_STATE"};
    }

    public static WifiSensor ba(Context context) {
        if (Ki == null) {
            synchronized (lock) {
                if (Ki == null) {
                    Ki = new WifiSensor(context);
                }
            }
        }
        return Ki;
    }

    private WifiSensor(Context context) {
        super(context);
        this.Kf = (WifiManager) context.getSystemService("wifi");
        this.Kg = new WifiSensor(this);
    }

    public void gY() {
        super.gY();
        Ki = null;
    }

    protected String[] hb() {
        return Jz;
    }

    protected String he() {
        return "WifiSensor";
    }

    public int getSensorType() {
        return 5010;
    }

    protected WifiData hB() {
        return this.Kj;
    }

    protected void hm() {
        this.Kj = ((WifiProcessor) hi()).m1560b(this.Jn, this.Kh, this.Id.gS());
    }

    protected boolean hc() {
        try {
            this.Kh = null;
            if (!this.Kf.isWifiEnabled()) {
                return false;
            }
            String gR;
            this.Kh = new ArrayList();
            this.Jw = ((Integer) this.Id.getParameter("NUMBER_OF_SENSE_CYCLES")).intValue();
            if (GlobalConfig.gO() != null) {
                gR = GlobalConfig.gO().gR();
            } else {
                gR = null;
            }
            Log.m285d(he(), " intentBroadcasterPermission: " + gR);
            this.HR.registerReceiver(this.Kg, new IntentFilter("android.net.wifi.SCAN_RESULTS"), gR, gZ());
            this.Kf.startScan();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    protected void hd() {
        try {
            this.HR.unregisterReceiver(this.Kg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
