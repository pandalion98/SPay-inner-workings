package com.samsung.sensorframework.sdi.p046c;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.sensorframework.SFManager;
import com.samsung.sensorframework.sda.p033b.SensorData;
import com.samsung.sensorframework.sda.p033b.p034a.LocationData;
import com.samsung.sensorframework.sda.p033b.p035b.BatteryData;
import com.samsung.sensorframework.sda.p033b.p035b.ConnectionStateData;
import com.samsung.sensorframework.sda.p043f.DataAcquisitionUtils;
import com.samsung.sensorframework.sdi.p044a.DailySensingAlarm;
import com.samsung.sensorframework.sdi.p044a.DynamicDutyCycling;
import com.samsung.sensorframework.sdi.p045b.ConfigUtils;
import com.samsung.sensorframework.sdi.p047d.POIProximityDetector;
import com.samsung.sensorframework.sdi.p047d.PoIProximityDataHolder;
import com.samsung.sensorframework.sdi.p047d.PoIUtils;
import com.samsung.sensorframework.sdi.p047d.WifiProximityDetector;
import com.samsung.sensorframework.sdi.p048e.EnergyQuota;
import com.samsung.sensorframework.sdi.p048e.TriggerQuota;
import com.samsung.sensorframework.sdi.p049f.PersistentStorage;
import com.samsung.sensorframework.sdi.p050g.SFUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/* renamed from: com.samsung.sensorframework.sdi.c.c */
public class SensingController {
    private static final Object Ho;
    private static SensingController Lo;
    private SFManager Hp;
    private HashSet<String> LA;
    private PendingIntent Lp;
    private SDISensorHandler Lq;
    private DynamicDutyCycling Lr;
    private EnergyQuota Ls;
    private TriggerQuota Lt;
    private DailySensingAlarm Lv;
    private PersistentStorage Lw;
    private List<String> Lx;
    private int Ly;
    private LocationData Lz;
    private Context context;

    static {
        Ho = new Object();
    }

    public static SensingController br(Context context) {
        if (Lo == null) {
            synchronized (Ho) {
                if (Lo == null) {
                    Lo = new SensingController(context);
                }
            }
        }
        return Lo;
    }

    private SensingController(Context context) {
        Log.m285d("SensingController", "SensingController created");
        Log.m287i("SensingController", "created");
        this.context = context;
    }

    public synchronized void hQ() {
        if (this.context != null) {
            Log.m285d("SensingController", "startSensingController()");
            try {
                this.Hp = SFManager.aM(this.context);
                this.Lq = SDISensorHandler.bq(this.context);
                this.Lr = new DynamicDutyCycling(this.context);
                this.Ls = EnergyQuota.bs(this.context);
                this.Lt = TriggerQuota.bt(this.context);
                this.Lv = new DailySensingAlarm();
                this.Lw = PersistentStorage.bu(this.context);
                this.context.registerReceiver(this.Lv, new IntentFilter("spayfw.sensorframework.DailySensingAlarm"), "com.samsung.android.spayfw.permission.ACCESS_PF", null);
                this.Lp = cn("spayfw.sensorframework.DailySensingAlarm");
                hS();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.m285d("SensingController", "startSensingController() context is null");
        }
    }

    public synchronized void hR() {
        Log.m285d("SensingController", "stopSensingController()");
        DailySensingAlarm.m1652b(this.context, this.Lp);
        this.context.unregisterReceiver(this.Lv);
        hM();
    }

    private PendingIntent cn(String str) {
        PendingIntent broadcast = PendingIntent.getBroadcast(this.context, 0, new Intent(str), 268435456);
        DailySensingAlarm.m1651a(this.context, broadcast);
        return broadcast;
    }

    public synchronized void m1689b(Intent intent) {
        String action = intent.getAction();
        if (action == null || !action.equals("spayfw.sensorframework.DailySensingAlarm")) {
            Log.m285d("SensingController", "onDailyAlarmTrigger() disabling sensing, unknown intent action: " + action);
            hM();
        } else {
            Log.m285d("SensingController", "onDailyAlarmTrigger() sensing start alarm");
            Log.m287i("SensingController", "start");
            this.Lp = cn("spayfw.sensorframework.DailySensingAlarm");
            this.Lw.removeAll();
            hM();
            hS();
        }
    }

    private boolean co(String str) {
        boolean z = false;
        try {
            z = ConfigUtils.cm(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return z;
    }

    private void hS() {
        String bp = ConfigUtils.bp(this.context);
        if (co(bp)) {
            Log.m285d("SensingController", "initializeAndStartSensing() context sensing enabled on the server");
            reset(bp);
            if (hX()) {
                hL();
                hW();
                return;
            }
            hM();
            return;
        }
        Log.m285d("SensingController", "initializeAndStartSensing() context sensing disabled on the server");
        Log.m287i("SensingController", "policy off");
    }

    private void reset(String str) {
        Log.m285d("SensingController", "reset()");
        this.Ls.cu(str);
        this.Lt.cu(str);
        this.Ly = 5;
        this.Lx = null;
        this.LA = this.Lw.m1705a("poiEnterGeofenceSet", new HashSet());
        if (this.LA == null) {
            this.LA = new HashSet();
        }
    }

    public synchronized void m1690c(LocationData locationData) {
        try {
            Log.m285d("SensingController", "onLocationDataReceived() " + locationData.toString());
            m1684g(locationData);
            m1681d(locationData);
            if (DataAcquisitionUtils.m1648a(locationData)) {
                m1683f(locationData);
                m1682e(locationData);
                if (this.Lx != null && this.Lx.size() > 0) {
                    hT();
                    hU();
                    PoIProximityDataHolder a = POIProximityDetector.m1691a(locationData, this.Lx, 100);
                    m1679a(a);
                    m1680c(locationData, a);
                }
            }
            if (!hX()) {
                hM();
            }
        } catch (Exception e) {
            e.printStackTrace();
            m1686K(1800000);
        }
    }

    private boolean m1681d(LocationData locationData) {
        Log.m285d("SensingController", "processLocationData()");
        this.Lz = locationData;
        if (DataAcquisitionUtils.m1648a(locationData)) {
            this.Ly = 5;
            return true;
        }
        if (this.Ly > 0) {
            this.Ly--;
        }
        Log.m285d("SensingController", "processLocationData() locationRetryCount: " + this.Ly);
        if (this.Ly <= 0) {
            hM();
        } else {
            m1686K(900000);
        }
        return false;
    }

    private void m1682e(LocationData locationData) {
        Log.m285d("SensingController", "updateCache()");
        this.Lx = PoIUtils.m1695a(this.context, locationData, 8000);
        this.Lx = PoIUtils.m1698l(this.Lx);
        if (this.Lx == null || this.Lx.size() == 0) {
            Log.m285d("SensingController", "updateCache() poiCache is null or empty");
            m1686K(1200000);
            return;
        }
        Log.m285d("SensingController", "updateCache() poiCache size: " + this.Lx.size());
    }

    private void m1683f(LocationData locationData) {
        Log.m285d("SensingController", "detectPoIExitEvent()");
        if (this.LA == null || this.LA.size() <= 0) {
            Log.m285d("SensingController", "poiEnterHashSet is empty");
            return;
        }
        Log.m285d("SensingController", "detectPoIExitEvent() poiEnterHashSet.size(): " + this.LA.size());
        PoIProximityDataHolder a = POIProximityDetector.m1691a(locationData, new ArrayList(this.LA), 100);
        Collection hZ;
        if (a != null) {
            hZ = a.hZ();
        } else {
            hZ = null;
        }
        Object arrayList = new ArrayList();
        Iterator it = this.LA.iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            if (!(str == null || str.length() <= 0 || PoIUtils.m1697a(str, r1))) {
                Log.m285d("SensingController", "detectPoIExitEvent() exit event: " + PoIUtils.cr(str));
                arrayList.add(str);
            }
        }
        if (arrayList.size() > 0) {
            Log.m287i("SensingController", "exit trigger");
            this.Hp.m1507j(arrayList);
            Iterator it2 = arrayList.iterator();
            while (it2.hasNext()) {
                this.LA.remove((String) it2.next());
            }
            this.Lw.m1706a("poiEnterGeofenceSet", this.LA);
        }
    }

    private void m1679a(PoIProximityDataHolder poIProximityDataHolder) {
        Log.m285d("SensingController", "detectPoIEnterEvent()");
        if (poIProximityDataHolder == null || poIProximityDataHolder.hZ() == null || poIProximityDataHolder.hZ().size() <= 0) {
            Log.m285d("SensingController", "detectPoIEnterEvent() not in proximity to any PoIs");
            return;
        }
        List hZ = poIProximityDataHolder.hZ();
        if (DataAcquisitionUtils.bl(this.context)) {
            Log.m285d("SensingController", "detectPoIEnterEvent() device connected to a network");
            if (this.Lt.id()) {
                Log.m285d("SensingController", "detectPoIEnterEvent() global trigger quota is empty, so not sending trigger");
                return;
            }
            Log.m285d("SensingController", "detectPoIEnterEvent() global trigger quota is not empty");
            if (SFUtils.m1708f(9, 20)) {
                List<String> m;
                Log.m285d("SensingController", "detectPoIEnterEvent() Location based Poi enter trigger; locationEnterPoiList.size() " + hZ.size());
                List a = WifiProximityDetector.m1700a(this.context, hZ);
                if (a != null && a.size() > 0) {
                    Log.m285d("SensingController", "detectPoIEnterEvent() Wi-Fi based Poi enter trigger; wifiEnterPoIList.size() " + a.size());
                    a = PoIUtils.m1696a(hZ, a);
                    if (a != null && a.size() > 0) {
                        Log.m285d("SensingController", "detectPoIEnterEvent() Merged Poi enter trigger; enterPoiList.size() " + a.size());
                        m = PoIUtils.m1699m(a);
                        if (m != null || m.size() <= 0) {
                            Log.m285d("SensingController", "enterPoiList is null or empty");
                            return;
                        }
                        Log.m285d("SensingController", "detectPoIEnterEvent() sending trigger, enterPoIList.size(): " + m.size());
                        for (String cr : m) {
                            Log.m285d("SensingController", "detectPoIEnterEvent() enter event: " + PoIUtils.cr(cr));
                        }
                        Log.m287i("SensingController", "enter trigger");
                        if (this.LA != null) {
                            this.LA.addAll(m);
                            this.Lw.m1706a("poiEnterGeofenceSet", this.LA);
                        } else {
                            Log.m285d("SensingController", "poiEnterHashSet is null");
                        }
                        this.Hp.m1506i(m);
                        m1685k(m);
                        return;
                    }
                }
                a = hZ;
                m = PoIUtils.m1699m(a);
                if (m != null) {
                }
                Log.m285d("SensingController", "enterPoiList is null or empty");
                return;
            }
            Log.m285d("SensingController", "detectPoIEnterEvent() current time is not in trigger time range");
            return;
        }
        Log.m285d("SensingController", "detectPoIEnterEvent() not connected to a network, therefore, not sending trigger");
    }

    private void m1685k(List<String> list) {
        Log.m285d("SensingController", "updateTriggerQuota()");
        if (list != null && list.size() > 0) {
            Log.m285d("SensingController", "updateTriggerQuota() pois size: " + list.size());
            this.Lt.m1704n(list);
        }
    }

    private void hT() {
        if (this.Lx != null) {
            String str;
            ArrayList arrayList = new ArrayList();
            for (String str2 : this.Lx) {
                if (this.Lt.cw(str2)) {
                    Log.m285d("SensingController", "removePoIsFromCacheWithEmptyTriggerQuota() PoI to be removed: " + PoIUtils.cr(str2));
                    arrayList.add(str2);
                }
            }
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                str2 = (String) it.next();
                Log.m285d("SensingController", "removePoIsFromCacheWithEmptyTriggerQuota() Removing poi from cache: " + PoIUtils.cr(str2));
                this.Lx.remove(str2);
            }
        }
    }

    private void hU() {
        if (this.Lx != null && this.Lx.size() > 0 && this.LA != null && this.LA.size() > 0) {
            String str;
            ArrayList arrayList = new ArrayList();
            for (String str2 : this.Lx) {
                if (PoIUtils.m1697a(str2, this.LA)) {
                    Log.m285d("SensingController", "removePoIsInEnterSetFromCache() PoI to be removed: " + PoIUtils.cr(str2));
                    arrayList.add(str2);
                }
            }
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                str2 = (String) it.next();
                Log.m285d("SensingController", "removePoIsInEnterSetFromCache() Removing poi from cache: " + PoIUtils.cr(str2));
                this.Lx.remove(str2);
            }
        }
    }

    private void m1680c(LocationData locationData, PoIProximityDataHolder poIProximityDataHolder) {
        Log.m285d("SensingController", "updateDutyCyclingInterval()");
        this.Lr.m1656a(locationData, poIProximityDataHolder);
    }

    private void m1684g(LocationData locationData) {
        Log.m285d("SensingController", "updateEnergyQuota()");
        if (locationData != null && locationData.getSensorType() == 5004) {
            this.Ls.ia();
        }
        if (this.Ls.ib()) {
            Log.m285d("SensingController", "updateEnergyQuota() energy quota is empty");
            hM();
        }
    }

    private boolean hV() {
        if (DataAcquisitionUtils.m1649c(this.context, 15) || !DataAcquisitionUtils.bl(this.context)) {
            Log.m285d("SensingController", "isBatteryAndConnectionOK() returning false");
            return false;
        }
        Log.m285d("SensingController", "isBatteryAndConnectionOK() returning true");
        return true;
    }

    private void hW() {
        try {
            Log.m285d("SensingController", "batteryAndConnectionCheck()");
            if (!hX()) {
                hM();
            } else if (hV()) {
                Log.m285d("SensingController", "batteryAndConnectionCheck() Battery and Network connection OK");
                hK();
            } else {
                Log.m285d("SensingController", "batteryAndConnectionCheck() Battery low OR no Network connection");
                hN();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void m1687a(BatteryData batteryData) {
        if (batteryData != null) {
            Log.m285d("SensingController", "onBatteryDataReceived() Action: " + batteryData.getAction());
            if (this.Lq.ax(batteryData.getSensorType())) {
                Log.m285d("SensingController", "onBatteryDataReceived() subscribed to battery sensor");
                hW();
            } else {
                Log.m285d("SensingController", "onBatteryDataReceived() not subscribed to battery sensor");
            }
        } else {
            Log.m285d("SensingController", "onBatteryDataReceived() batteryData is null");
        }
    }

    public synchronized void m1688a(ConnectionStateData connectionStateData) {
        if (connectionStateData != null) {
            Log.m285d("SensingController", "onConnectivityChanged() " + connectionStateData.toString());
            if (this.Lq.ax(connectionStateData.getSensorType())) {
                Log.m285d("SensingController", "onConnectivityChanged() subscribed to connectionstate sensor");
                hW();
            } else {
                Log.m285d("SensingController", "onConnectivityChanged() not subscribed to connectionstate sensor");
            }
        } else {
            Log.m285d("SensingController", "onConnectivityChanged() is null");
        }
    }

    private boolean hX() {
        Log.m285d("SensingController", "canContinueSensingForToday()");
        if (!SFUtils.m1708f(0, 22) || this.Ls.ib() || this.Lt.id()) {
            Log.m285d("SensingController", "canContinueSensingForToday() false");
            return false;
        }
        Log.m285d("SensingController", "canContinueSensingForToday() true");
        return true;
    }

    private synchronized void hL() {
        Log.m285d("SensingController", "enableBatteryAndConnectionSensors()");
        this.Lq.hL();
    }

    private synchronized void hK() {
        Log.m285d("SensingController", "enableRadioSensors()");
        this.Lq.hK();
    }

    public synchronized void m1686K(long j) {
        Log.m285d("SensingController", "setLocationDutyCycling() sleepInterval: " + j);
        if (this.Lq.ax(5004)) {
            Log.m285d("SensingController", "setLocationDutyCycling() subscribed to location sensor, so updating sleep interval");
            this.Lq.m1666c(5004, j);
        } else {
            Log.m285d("SensingController", "setLocationDutyCycling() not subscribed to location sensor, so not updating sleep interval");
        }
    }

    private synchronized void hM() {
        Log.m285d("SensingController", "disableAllSensors()");
        this.Lq.hM();
    }

    private synchronized void hN() {
        Log.m285d("SensingController", "disableRadioSensors()");
        this.Lq.hN();
    }

    public SensorData aa(int i) {
        Log.m285d("SensingController", "getLastSensorData()");
        if (i != 5004) {
            return null;
        }
        SensorData sensorData = this.Lz;
        if (sensorData == null || sensorData.getLocation() == null) {
            Log.m285d("SensingController", "getLastSensorData() calling DataAcquisitionUtils.getLastKnownLocation()");
            return DataAcquisitionUtils.bn(this.context);
        }
        Log.m285d("SensingController", "getLastSensorData() lastLocationData is not null");
        return sensorData;
    }
}
