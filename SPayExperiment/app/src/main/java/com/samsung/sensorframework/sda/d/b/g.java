/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.Intent
 *  android.content.IntentFilter
 *  android.location.Location
 *  android.location.LocationListener
 *  android.location.LocationManager
 *  android.os.Bundle
 *  android.os.Handler
 *  android.os.Looper
 *  java.lang.Object
 *  java.lang.SecurityException
 *  java.lang.String
 *  java.lang.Thread
 *  java.util.List
 */
package com.samsung.sensorframework.sda.d.b;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import com.samsung.android.spayfw.b.c;
import com.samsung.sensorframework.sda.b.a.p;
import com.samsung.sensorframework.sda.c.a.k;
import com.samsung.sensorframework.sda.d.b.a;
import java.util.List;

public class g
extends a {
    private static final String[] Jz;
    private static g Kw;
    private static final Object lock;
    private LocationManager JL;
    private LocationListener Kx;

    static {
        lock = new Object();
        Jz = new String[]{"android.permission.ACCESS_FINE_LOCATION"};
    }

    private g(Context context) {
        super(context);
        this.JL = (LocationManager)this.HR.getSystemService("location");
        this.Kx = new LocationListener(){

            public void onLocationChanged(Location location) {
                if (location != null) {
                    c.d("PassiveLocationSensor", "onLocationChanged(), newLocation: " + location.toString());
                    g.this.b(location);
                    return;
                }
                c.d("PassiveLocationSensor", "onLocationChanged(), newLocation is null");
            }

            public void onProviderDisabled(String string) {
            }

            public void onProviderEnabled(String string) {
            }

            public void onStatusChanged(String string, int n2, Bundle bundle) {
            }
        };
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static g bg(Context context) {
        if (Kw == null) {
            Object object;
            Object object2 = object = lock;
            synchronized (object2) {
                if (Kw == null) {
                    Kw = new g(context);
                }
            }
        }
        return Kw;
    }

    @Override
    protected void a(Context context, Intent intent) {
    }

    protected void b(Location location) {
        block3 : {
            block2 : {
                if (location == null) break block2;
                p p2 = ((k)this.hi()).a(location.getTime(), location, this.Id.gS(), 5038);
                if (!com.samsung.sensorframework.sda.f.a.a(p2)) break block3;
                this.a(p2);
            }
            return;
        }
        c.d(this.he(), "processLocationData() invalid location");
    }

    protected void cl(String string) {
        if (this.JL != null) {
            List list = this.JL.getAllProviders();
            if (list != null && list.contains((Object)string)) {
                if (this.gZ() != null) {
                    Looper looper = this.gZ().getLooper();
                    if (looper != null && looper.getThread() != null && looper.getThread().isAlive()) {
                        this.JL.requestLocationUpdates(string, 300000L, 10.0f, this.Kx, looper);
                        return;
                    }
                    c.d("PassiveLocationSensor", "requestLocationUpdates(), not requesting location updates as looper is null or looper thread is not alive");
                    return;
                }
                c.d("PassiveLocationSensor", "requestLocationUpdates(), not requesting location updates as getSensorHandler() is null");
                return;
            }
            c.d("PassiveLocationSensor", "requestLocationUpdates(), Not registering with " + string + " as it is unavailable");
            return;
        }
        c.d("PassiveLocationSensor", "requestLocationUpdates(), locationManager is null");
    }

    @Override
    public void gY() {
        super.gY();
        Kw = null;
    }

    @Override
    public int getSensorType() {
        return 5038;
    }

    @Override
    protected IntentFilter[] hC() {
        return null;
    }

    @Override
    protected String[] hb() {
        return Jz;
    }

    @Override
    protected boolean hc() {
        this.cl("passive");
        return true;
    }

    @Override
    protected void hd() {
        try {
            if (this.JL != null) {
                this.JL.removeUpdates(this.Kx);
                return;
            }
            c.d("PassiveLocationSensor", "stopSensing(), locationManager is null");
            return;
        }
        catch (SecurityException securityException) {
            securityException.printStackTrace();
            return;
        }
    }

    @Override
    public String he() {
        return "PassiveLocationSensor";
    }

}

