package com.samsung.sensorframework.sda.p039d.p041b;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.sensorframework.sda.p033b.SensorData;
import com.samsung.sensorframework.sda.p036c.p037a.LocationProcessor;
import com.samsung.sensorframework.sda.p043f.DataAcquisitionUtils;
import java.util.List;

/* renamed from: com.samsung.sensorframework.sda.d.b.g */
public class PassiveLocationSensor extends AbstractPushSensor {
    private static final String[] Jz;
    private static PassiveLocationSensor Kw;
    private static final Object lock;
    private LocationManager JL;
    private LocationListener Kx;

    /* renamed from: com.samsung.sensorframework.sda.d.b.g.1 */
    class PassiveLocationSensor implements LocationListener {
        final /* synthetic */ PassiveLocationSensor Ky;

        PassiveLocationSensor(PassiveLocationSensor passiveLocationSensor) {
            this.Ky = passiveLocationSensor;
        }

        public void onLocationChanged(Location location) {
            if (location != null) {
                Log.m285d("PassiveLocationSensor", "onLocationChanged(), newLocation: " + location.toString());
                this.Ky.m1624b(location);
                return;
            }
            Log.m285d("PassiveLocationSensor", "onLocationChanged(), newLocation is null");
        }

        public void onProviderDisabled(String str) {
        }

        public void onProviderEnabled(String str) {
        }

        public void onStatusChanged(String str, int i, Bundle bundle) {
        }
    }

    static {
        lock = new Object();
        Jz = new String[]{"android.permission.ACCESS_FINE_LOCATION"};
    }

    public static PassiveLocationSensor bg(Context context) {
        if (Kw == null) {
            synchronized (lock) {
                if (Kw == null) {
                    Kw = new PassiveLocationSensor(context);
                }
            }
        }
        return Kw;
    }

    public void gY() {
        super.gY();
        Kw = null;
    }

    private PassiveLocationSensor(Context context) {
        super(context);
        this.JL = (LocationManager) this.HR.getSystemService("location");
        this.Kx = new PassiveLocationSensor(this);
    }

    public String he() {
        return "PassiveLocationSensor";
    }

    protected String[] hb() {
        return Jz;
    }

    public int getSensorType() {
        return 5038;
    }

    protected void m1623a(Context context, Intent intent) {
    }

    protected IntentFilter[] hC() {
        return null;
    }

    protected void m1624b(Location location) {
        if (location != null) {
            SensorData a = ((LocationProcessor) hi()).m1551a(location.getTime(), location, this.Id.gS(), 5038);
            if (DataAcquisitionUtils.m1648a(a)) {
                m1613a(a);
            } else {
                Log.m285d(he(), "processLocationData() invalid location");
            }
        }
    }

    protected boolean hc() {
        cl("passive");
        return true;
    }

    protected void hd() {
        try {
            if (this.JL != null) {
                this.JL.removeUpdates(this.Kx);
            } else {
                Log.m285d("PassiveLocationSensor", "stopSensing(), locationManager is null");
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    protected void cl(String str) {
        if (this.JL != null) {
            List allProviders = this.JL.getAllProviders();
            if (allProviders == null || !allProviders.contains(str)) {
                Log.m285d("PassiveLocationSensor", "requestLocationUpdates(), Not registering with " + str + " as it is unavailable");
                return;
            } else if (gZ() != null) {
                Looper looper = gZ().getLooper();
                if (looper == null || looper.getThread() == null || !looper.getThread().isAlive()) {
                    Log.m285d("PassiveLocationSensor", "requestLocationUpdates(), not requesting location updates as looper is null or looper thread is not alive");
                    return;
                }
                this.JL.requestLocationUpdates(str, 300000, 10.0f, this.Kx, looper);
                return;
            } else {
                Log.m285d("PassiveLocationSensor", "requestLocationUpdates(), not requesting location updates as getSensorHandler() is null");
                return;
            }
        }
        Log.m285d("PassiveLocationSensor", "requestLocationUpdates(), locationManager is null");
    }
}
