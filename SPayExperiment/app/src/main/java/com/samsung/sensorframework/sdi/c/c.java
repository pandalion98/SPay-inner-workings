/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.app.PendingIntent
 *  android.content.BroadcastReceiver
 *  android.content.Context
 *  android.content.Intent
 *  android.content.IntentFilter
 *  android.location.Location
 *  android.os.Handler
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.Collection
 *  java.util.HashSet
 *  java.util.List
 *  java.util.Set
 */
package com.samsung.sensorframework.sdi.c;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Handler;
import com.samsung.sensorframework.b;
import com.samsung.sensorframework.sda.b.a.p;
import com.samsung.sensorframework.sdi.c.a;
import com.samsung.sensorframework.sdi.d.d;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class c {
    private static final Object Ho = new Object();
    private static c Lo;
    private b Hp;
    private HashSet<String> LA;
    private PendingIntent Lp;
    private a Lq;
    private com.samsung.sensorframework.sdi.a.b Lr;
    private com.samsung.sensorframework.sdi.e.a Ls;
    private com.samsung.sensorframework.sdi.e.b Lt;
    private com.samsung.sensorframework.sdi.a.a Lv;
    private com.samsung.sensorframework.sdi.f.a Lw;
    private List<String> Lx;
    private int Ly;
    private p Lz;
    private Context context;

    private c(Context context) {
        com.samsung.android.spayfw.b.c.d("SensingController", "SensingController created");
        com.samsung.android.spayfw.b.c.i("SensingController", "created");
        this.context = context;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private void a(com.samsung.sensorframework.sdi.d.b var1_1) {
        block9 : {
            com.samsung.android.spayfw.b.c.d("SensingController", "detectPoIEnterEvent()");
            if (var1_1 == null || var1_1.hZ() == null || var1_1.hZ().size() <= 0) break block9;
            var2_2 = var1_1.hZ();
            if (!com.samsung.sensorframework.sda.f.a.bl(this.context)) {
                com.samsung.android.spayfw.b.c.d("SensingController", "detectPoIEnterEvent() not connected to a network, therefore, not sending trigger");
                return;
            }
            com.samsung.android.spayfw.b.c.d("SensingController", "detectPoIEnterEvent() device connected to a network");
            if (this.Lt.id()) {
                com.samsung.android.spayfw.b.c.d("SensingController", "detectPoIEnterEvent() global trigger quota is empty, so not sending trigger");
                return;
            }
            com.samsung.android.spayfw.b.c.d("SensingController", "detectPoIEnterEvent() global trigger quota is not empty");
            if (!com.samsung.sensorframework.sdi.g.a.f(9, 20)) {
                com.samsung.android.spayfw.b.c.d("SensingController", "detectPoIEnterEvent() current time is not in trigger time range");
                return;
            }
            com.samsung.android.spayfw.b.c.d("SensingController", "detectPoIEnterEvent() Location based Poi enter trigger; locationEnterPoiList.size() " + var2_2.size());
            var3_3 = d.a(this.context, var2_2);
            if (var3_3 == null || var3_3.size() <= 0) ** GOTO lbl-1000
            com.samsung.android.spayfw.b.c.d("SensingController", "detectPoIEnterEvent() Wi-Fi based Poi enter trigger; wifiEnterPoIList.size() " + var3_3.size());
            var4_4 = com.samsung.sensorframework.sdi.d.c.a(var2_2, var3_3);
            if (var4_4 != null && var4_4.size() > 0) {
                com.samsung.android.spayfw.b.c.d("SensingController", "detectPoIEnterEvent() Merged Poi enter trigger; enterPoiList.size() " + var4_4.size());
            } else lbl-1000: // 2 sources:
            {
                var4_4 = var2_2;
            }
            if ((var5_5 = com.samsung.sensorframework.sdi.d.c.m(var4_4)) != null && var5_5.size() > 0) {
                com.samsung.android.spayfw.b.c.d("SensingController", "detectPoIEnterEvent() sending trigger, enterPoIList.size(): " + var5_5.size());
                for (String var8_7 : var5_5) {
                    com.samsung.android.spayfw.b.c.d("SensingController", "detectPoIEnterEvent() enter event: " + com.samsung.sensorframework.sdi.d.c.cr(var8_7));
                }
                com.samsung.android.spayfw.b.c.i("SensingController", "enter trigger");
                if (this.LA != null) {
                    this.LA.addAll(var5_5);
                    this.Lw.a("poiEnterGeofenceSet", this.LA);
                } else {
                    com.samsung.android.spayfw.b.c.d("SensingController", "poiEnterHashSet is null");
                }
                this.Hp.i(var5_5);
                this.k(var5_5);
                return;
            }
            com.samsung.android.spayfw.b.c.d("SensingController", "enterPoiList is null or empty");
            return;
        }
        com.samsung.android.spayfw.b.c.d("SensingController", "detectPoIEnterEvent() not in proximity to any PoIs");
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static c br(Context context) {
        if (Lo == null) {
            Object object;
            Object object2 = object = Ho;
            synchronized (object2) {
                if (Lo == null) {
                    Lo = new c(context);
                }
            }
        }
        return Lo;
    }

    private void c(p p2, com.samsung.sensorframework.sdi.d.b b2) {
        com.samsung.android.spayfw.b.c.d("SensingController", "updateDutyCyclingInterval()");
        this.Lr.a(p2, b2);
    }

    private PendingIntent cn(String string) {
        Intent intent = new Intent(string);
        PendingIntent pendingIntent = PendingIntent.getBroadcast((Context)this.context, (int)0, (Intent)intent, (int)268435456);
        com.samsung.sensorframework.sdi.a.a.a(this.context, pendingIntent);
        return pendingIntent;
    }

    private boolean co(String string) {
        try {
            boolean bl = com.samsung.sensorframework.sdi.b.a.cm(string);
            return bl;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private boolean d(p p2) {
        com.samsung.android.spayfw.b.c.d("SensingController", "processLocationData()");
        this.Lz = p2;
        if (com.samsung.sensorframework.sda.f.a.a(p2)) {
            this.Ly = 5;
            return true;
        }
        if (this.Ly > 0) {
            this.Ly = -1 + this.Ly;
        }
        com.samsung.android.spayfw.b.c.d("SensingController", "processLocationData() locationRetryCount: " + this.Ly);
        if (this.Ly <= 0) {
            this.hM();
            do {
                return false;
                break;
            } while (true);
        }
        this.K(900000L);
        return false;
    }

    private void e(p p2) {
        com.samsung.android.spayfw.b.c.d("SensingController", "updateCache()");
        this.Lx = com.samsung.sensorframework.sdi.d.c.a(this.context, p2, 8000);
        this.Lx = com.samsung.sensorframework.sdi.d.c.l(this.Lx);
        if (this.Lx == null || this.Lx.size() == 0) {
            com.samsung.android.spayfw.b.c.d("SensingController", "updateCache() poiCache is null or empty");
            this.K(1200000L);
            return;
        }
        com.samsung.android.spayfw.b.c.d("SensingController", "updateCache() poiCache size: " + this.Lx.size());
    }

    /*
     * Enabled aggressive block sorting
     */
    private void f(p p2) {
        com.samsung.android.spayfw.b.c.d("SensingController", "detectPoIExitEvent()");
        if (this.LA != null && this.LA.size() > 0) {
            com.samsung.android.spayfw.b.c.d("SensingController", "detectPoIExitEvent() poiEnterHashSet.size(): " + this.LA.size());
            com.samsung.sensorframework.sdi.d.b b2 = com.samsung.sensorframework.sdi.d.a.a(p2, (List<String>)new ArrayList(this.LA), 100);
            List<String> list = b2 != null ? b2.hZ() : null;
            ArrayList arrayList = new ArrayList();
            for (String string : this.LA) {
                if (string == null || string.length() <= 0 || com.samsung.sensorframework.sdi.d.c.a(string, list)) continue;
                com.samsung.android.spayfw.b.c.d("SensingController", "detectPoIExitEvent() exit event: " + com.samsung.sensorframework.sdi.d.c.cr(string));
                arrayList.add((Object)string);
            }
            if (arrayList.size() > 0) {
                com.samsung.android.spayfw.b.c.i("SensingController", "exit trigger");
                this.Hp.j((List<String>)arrayList);
                for (String string : arrayList) {
                    this.LA.remove((Object)string);
                }
                this.Lw.a("poiEnterGeofenceSet", this.LA);
            }
            return;
        }
        com.samsung.android.spayfw.b.c.d("SensingController", "poiEnterHashSet is empty");
    }

    private void g(p p2) {
        com.samsung.android.spayfw.b.c.d("SensingController", "updateEnergyQuota()");
        if (p2 != null && p2.getSensorType() == 5004) {
            this.Ls.ia();
        }
        if (this.Ls.ib()) {
            com.samsung.android.spayfw.b.c.d("SensingController", "updateEnergyQuota() energy quota is empty");
            this.hM();
        }
    }

    private void hK() {
        c c2 = this;
        synchronized (c2) {
            com.samsung.android.spayfw.b.c.d("SensingController", "enableRadioSensors()");
            this.Lq.hK();
            return;
        }
    }

    private void hL() {
        c c2 = this;
        synchronized (c2) {
            com.samsung.android.spayfw.b.c.d("SensingController", "enableBatteryAndConnectionSensors()");
            this.Lq.hL();
            return;
        }
    }

    private void hM() {
        c c2 = this;
        synchronized (c2) {
            com.samsung.android.spayfw.b.c.d("SensingController", "disableAllSensors()");
            this.Lq.hM();
            return;
        }
    }

    private void hN() {
        c c2 = this;
        synchronized (c2) {
            com.samsung.android.spayfw.b.c.d("SensingController", "disableRadioSensors()");
            this.Lq.hN();
            return;
        }
    }

    private void hS() {
        String string = com.samsung.sensorframework.sdi.b.a.bp(this.context);
        if (this.co(string)) {
            com.samsung.android.spayfw.b.c.d("SensingController", "initializeAndStartSensing() context sensing enabled on the server");
            this.reset(string);
            if (this.hX()) {
                this.hL();
                this.hW();
                return;
            }
            this.hM();
            return;
        }
        com.samsung.android.spayfw.b.c.d("SensingController", "initializeAndStartSensing() context sensing disabled on the server");
        com.samsung.android.spayfw.b.c.i("SensingController", "policy off");
    }

    private void hT() {
        if (this.Lx != null) {
            ArrayList arrayList = new ArrayList();
            for (String string : this.Lx) {
                if (!this.Lt.cw(string)) continue;
                com.samsung.android.spayfw.b.c.d("SensingController", "removePoIsFromCacheWithEmptyTriggerQuota() PoI to be removed: " + com.samsung.sensorframework.sdi.d.c.cr(string));
                arrayList.add((Object)string);
            }
            for (String string : arrayList) {
                com.samsung.android.spayfw.b.c.d("SensingController", "removePoIsFromCacheWithEmptyTriggerQuota() Removing poi from cache: " + com.samsung.sensorframework.sdi.d.c.cr(string));
                this.Lx.remove((Object)string);
            }
        }
    }

    private void hU() {
        if (this.Lx != null && this.Lx.size() > 0 && this.LA != null && this.LA.size() > 0) {
            ArrayList arrayList = new ArrayList();
            for (String string : this.Lx) {
                if (!com.samsung.sensorframework.sdi.d.c.a(string, this.LA)) continue;
                com.samsung.android.spayfw.b.c.d("SensingController", "removePoIsInEnterSetFromCache() PoI to be removed: " + com.samsung.sensorframework.sdi.d.c.cr(string));
                arrayList.add((Object)string);
            }
            for (String string : arrayList) {
                com.samsung.android.spayfw.b.c.d("SensingController", "removePoIsInEnterSetFromCache() Removing poi from cache: " + com.samsung.sensorframework.sdi.d.c.cr(string));
                this.Lx.remove((Object)string);
            }
        }
    }

    private boolean hV() {
        if (!com.samsung.sensorframework.sda.f.a.c(this.context, 15) && com.samsung.sensorframework.sda.f.a.bl(this.context)) {
            com.samsung.android.spayfw.b.c.d("SensingController", "isBatteryAndConnectionOK() returning true");
            return true;
        }
        com.samsung.android.spayfw.b.c.d("SensingController", "isBatteryAndConnectionOK() returning false");
        return false;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void hW() {
        try {
            com.samsung.android.spayfw.b.c.d("SensingController", "batteryAndConnectionCheck()");
            if (this.hX()) {
                if (this.hV()) {
                    com.samsung.android.spayfw.b.c.d("SensingController", "batteryAndConnectionCheck() Battery and Network connection OK");
                    this.hK();
                    return;
                }
                com.samsung.android.spayfw.b.c.d("SensingController", "batteryAndConnectionCheck() Battery low OR no Network connection");
                this.hN();
                return;
            }
            this.hM();
            return;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return;
        }
    }

    private boolean hX() {
        com.samsung.android.spayfw.b.c.d("SensingController", "canContinueSensingForToday()");
        if (com.samsung.sensorframework.sdi.g.a.f(0, 22) && !this.Ls.ib() && !this.Lt.id()) {
            com.samsung.android.spayfw.b.c.d("SensingController", "canContinueSensingForToday() true");
            return true;
        }
        com.samsung.android.spayfw.b.c.d("SensingController", "canContinueSensingForToday() false");
        return false;
    }

    private void k(List<String> list) {
        com.samsung.android.spayfw.b.c.d("SensingController", "updateTriggerQuota()");
        if (list != null && list.size() > 0) {
            com.samsung.android.spayfw.b.c.d("SensingController", "updateTriggerQuota() pois size: " + list.size());
            this.Lt.n(list);
        }
    }

    private void reset(String string) {
        com.samsung.android.spayfw.b.c.d("SensingController", "reset()");
        this.Ls.cu(string);
        this.Lt.cu(string);
        this.Ly = 5;
        this.Lx = null;
        this.LA = this.Lw.a("poiEnterGeofenceSet", (Set<String>)new HashSet());
        if (this.LA == null) {
            this.LA = new HashSet();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void K(long l2) {
        c c2 = this;
        synchronized (c2) {
            com.samsung.android.spayfw.b.c.d("SensingController", "setLocationDutyCycling() sleepInterval: " + l2);
            if (this.Lq.ax(5004)) {
                com.samsung.android.spayfw.b.c.d("SensingController", "setLocationDutyCycling() subscribed to location sensor, so updating sleep interval");
                this.Lq.c(5004, l2);
            } else {
                com.samsung.android.spayfw.b.c.d("SensingController", "setLocationDutyCycling() not subscribed to location sensor, so not updating sleep interval");
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void a(com.samsung.sensorframework.sda.b.b.a a2) {
        c c2 = this;
        synchronized (c2) {
            if (a2 != null) {
                com.samsung.android.spayfw.b.c.d("SensingController", "onBatteryDataReceived() Action: " + a2.getAction());
                if (this.Lq.ax(a2.getSensorType())) {
                    com.samsung.android.spayfw.b.c.d("SensingController", "onBatteryDataReceived() subscribed to battery sensor");
                    this.hW();
                } else {
                    com.samsung.android.spayfw.b.c.d("SensingController", "onBatteryDataReceived() not subscribed to battery sensor");
                }
            } else {
                com.samsung.android.spayfw.b.c.d("SensingController", "onBatteryDataReceived() batteryData is null");
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void a(com.samsung.sensorframework.sda.b.b.c c2) {
        c c3 = this;
        synchronized (c3) {
            if (c2 != null) {
                com.samsung.android.spayfw.b.c.d("SensingController", "onConnectivityChanged() " + c2.toString());
                if (this.Lq.ax(c2.getSensorType())) {
                    com.samsung.android.spayfw.b.c.d("SensingController", "onConnectivityChanged() subscribed to connectionstate sensor");
                    this.hW();
                } else {
                    com.samsung.android.spayfw.b.c.d("SensingController", "onConnectivityChanged() not subscribed to connectionstate sensor");
                }
            } else {
                com.samsung.android.spayfw.b.c.d("SensingController", "onConnectivityChanged() is null");
            }
            return;
        }
    }

    public com.samsung.sensorframework.sda.b.a aa(int n2) {
        com.samsung.android.spayfw.b.c.d("SensingController", "getLastSensorData()");
        if (n2 == 5004) {
            p p2 = this.Lz;
            if (p2 != null && p2.getLocation() != null) {
                com.samsung.android.spayfw.b.c.d("SensingController", "getLastSensorData() lastLocationData is not null");
                return p2;
            }
            com.samsung.android.spayfw.b.c.d("SensingController", "getLastSensorData() calling DataAcquisitionUtils.getLastKnownLocation()");
            return com.samsung.sensorframework.sda.f.a.bn(this.context);
        }
        return null;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void b(Intent intent) {
        c c2 = this;
        synchronized (c2) {
            String string = intent.getAction();
            if (string != null && string.equals((Object)"spayfw.sensorframework.DailySensingAlarm")) {
                com.samsung.android.spayfw.b.c.d("SensingController", "onDailyAlarmTrigger() sensing start alarm");
                com.samsung.android.spayfw.b.c.i("SensingController", "start");
                this.Lp = this.cn("spayfw.sensorframework.DailySensingAlarm");
                this.Lw.removeAll();
                this.hM();
                this.hS();
            } else {
                com.samsung.android.spayfw.b.c.d("SensingController", "onDailyAlarmTrigger() disabling sensing, unknown intent action: " + string);
                this.hM();
            }
            return;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public void c(p p2) {
        c c2 = this;
        synchronized (c2) {
            try {
                com.samsung.android.spayfw.b.c.d("SensingController", "onLocationDataReceived() " + p2.toString());
                this.g(p2);
                this.d(p2);
                if (com.samsung.sensorframework.sda.f.a.a(p2)) {
                    this.f(p2);
                    this.e(p2);
                    if (this.Lx != null && this.Lx.size() > 0) {
                        this.hT();
                        this.hU();
                        com.samsung.sensorframework.sdi.d.b b2 = com.samsung.sensorframework.sdi.d.a.a(p2, this.Lx, 100);
                        this.a(b2);
                        this.c(p2, b2);
                    }
                }
                if (!this.hX()) {
                    this.hM();
                }
                do {
                    return;
                    break;
                } while (true);
            }
            catch (Exception exception) {
                exception.printStackTrace();
                this.K(1800000L);
                return;
            }
            finally {
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void hQ() {
        c c2 = this;
        synchronized (c2) {
            block6 : {
                block5 : {
                    if (this.context == null) break block5;
                    com.samsung.android.spayfw.b.c.d("SensingController", "startSensingController()");
                    try {
                        this.Hp = b.aM(this.context);
                        this.Lq = a.bq(this.context);
                        this.Lr = new com.samsung.sensorframework.sdi.a.b(this.context);
                        this.Ls = com.samsung.sensorframework.sdi.e.a.bs(this.context);
                        this.Lt = com.samsung.sensorframework.sdi.e.b.bt(this.context);
                        this.Lv = new com.samsung.sensorframework.sdi.a.a();
                        this.Lw = com.samsung.sensorframework.sdi.f.a.bu(this.context);
                    }
                    catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    this.context.registerReceiver((BroadcastReceiver)this.Lv, new IntentFilter("spayfw.sensorframework.DailySensingAlarm"), "com.samsung.android.spayfw.permission.ACCESS_PF", null);
                    this.Lp = this.cn("spayfw.sensorframework.DailySensingAlarm");
                    this.hS();
                    break block6;
                    break block6;
                }
                com.samsung.android.spayfw.b.c.d("SensingController", "startSensingController() context is null");
            }
            return;
        }
    }

    public void hR() {
        c c2 = this;
        synchronized (c2) {
            com.samsung.android.spayfw.b.c.d("SensingController", "stopSensingController()");
            com.samsung.sensorframework.sdi.a.a.b(this.context, this.Lp);
            this.context.unregisterReceiver((BroadcastReceiver)this.Lv);
            this.hM();
            return;
        }
    }
}

