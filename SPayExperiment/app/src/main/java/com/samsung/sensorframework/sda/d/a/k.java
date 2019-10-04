/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.location.Location
 *  android.location.LocationListener
 *  android.location.LocationManager
 *  android.os.Bundle
 *  android.os.Handler
 *  android.os.Looper
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.SecurityException
 *  java.lang.String
 *  java.lang.Thread
 *  java.util.List
 */
package com.samsung.sensorframework.sda.d.a;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import com.samsung.android.spayfw.b.c;
import com.samsung.sensorframework.sda.b.a;
import com.samsung.sensorframework.sda.b.a.p;
import com.samsung.sensorframework.sda.d.a.b;
import java.util.List;

public class k
extends b {
    private static k JO;
    private static final String[] Jz;
    private static final Object lock;
    private LocationManager JL;
    private Location JM;
    private LocationListener JN;
    private p JP;

    static {
        lock = new Object();
        Jz = new String[]{"android.permission.ACCESS_FINE_LOCATION"};
    }

    private k(Context context) {
        super(context);
        this.JL = (LocationManager)context.getSystemService("location");
        this.JN = new LocationListener(){

            /*
             * Enabled aggressive block sorting
             */
            public void onLocationChanged(Location location) {
                if (k.this.Ji) {
                    if (location != null) {
                        c.d("LocationSensor", "onLocationChanged(), newLocation: " + location.toString());
                    } else {
                        c.d("LocationSensor", "onLocationChanged(), newLocation is null");
                    }
                    k.this.JM = location;
                    k.this.ho();
                }
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
    public static k aV(Context context) {
        if (JO == null) {
            Object object;
            Object object2 = object = lock;
            synchronized (object2) {
                if (JO == null) {
                    JO = new k(context);
                }
            }
        }
        return JO;
    }

    protected void cl(String string) {
        if (this.JL != null) {
            List list = this.JL.getAllProviders();
            if (list != null && list.contains((Object)string)) {
                if (this.gZ() != null) {
                    Looper looper = this.gZ().getLooper();
                    if (looper != null && looper.getThread() != null && looper.getThread().isAlive()) {
                        this.JL.requestSingleUpdate(string, this.JN, looper);
                        return;
                    }
                    c.d("LocationSensor", "requestLocationUpdates(), not requesting location updates as looper is null or looper thread is not alive");
                    return;
                }
                c.d("LocationSensor", "requestLocationUpdates(), not requesting location updates as getSensorHandler() is null");
                return;
            }
            c.d("LocationSensor", "requestLocationUpdates(), Not registering with " + string + " as it is unavailable");
            return;
        }
        c.d("LocationSensor", "requestLocationUpdates(), locationManager is null");
    }

    @Override
    public void gY() {
        super.gY();
        JO = null;
    }

    @Override
    public int getSensorType() {
        return 5004;
    }

    @Override
    protected String[] hb() {
        return Jz;
    }

    @Override
    protected boolean hc() {
        block3 : {
            String string;
            this.JM = null;
            try {
                this.cl("network");
                string = (String)this.Id.getParameter("LOCATION_ACCURACY");
                if (string == null) break block3;
            }
            catch (Exception exception) {
                exception.printStackTrace();
                return false;
            }
            if (!string.equals((Object)"LOCATION_ACCURACY_FINE")) break block3;
            this.cl("gps");
        }
        return true;
    }

    @Override
    protected void hd() {
        try {
            if (this.JL != null) {
                this.JL.removeUpdates(this.JN);
                return;
            }
            c.d("LocationSensor", "stopSensing(), locationManager is null");
            return;
        }
        catch (SecurityException securityException) {
            securityException.printStackTrace();
            return;
        }
    }

    @Override
    public String he() {
        return "LocationSensor";
    }

    @Override
    protected a hl() {
        return this.JP;
    }

    @Override
    protected void hm() {
        this.JP = ((com.samsung.sensorframework.sda.c.a.k)this.hi()).a(this.Jn, this.JM, this.Id.gS(), 5004);
    }

}

