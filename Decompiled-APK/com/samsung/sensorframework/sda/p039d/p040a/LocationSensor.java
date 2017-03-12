package com.samsung.sensorframework.sda.p039d.p040a;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.sensorframework.sda.p033b.SensorData;
import com.samsung.sensorframework.sda.p033b.p034a.LocationData;
import com.samsung.sensorframework.sda.p036c.p037a.LocationProcessor;
import java.util.List;

/* renamed from: com.samsung.sensorframework.sda.d.a.k */
public class LocationSensor extends AbstractPullSensor {
    private static LocationSensor JO;
    private static final String[] Jz;
    private static final Object lock;
    private LocationManager JL;
    private Location JM;
    private LocationListener JN;
    private LocationData JP;

    /* renamed from: com.samsung.sensorframework.sda.d.a.k.1 */
    class LocationSensor implements LocationListener {
        final /* synthetic */ LocationSensor JQ;

        LocationSensor(LocationSensor locationSensor) {
            this.JQ = locationSensor;
        }

        public void onLocationChanged(Location location) {
            if (this.JQ.Ji) {
                if (location != null) {
                    Log.m285d("LocationSensor", "onLocationChanged(), newLocation: " + location.toString());
                } else {
                    Log.m285d("LocationSensor", "onLocationChanged(), newLocation is null");
                }
                this.JQ.JM = location;
                this.JQ.ho();
            }
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

    public static LocationSensor aV(Context context) {
        if (JO == null) {
            synchronized (lock) {
                if (JO == null) {
                    JO = new LocationSensor(context);
                }
            }
        }
        return JO;
    }

    private LocationSensor(Context context) {
        super(context);
        this.JL = (LocationManager) context.getSystemService("location");
        this.JN = new LocationSensor(this);
    }

    public void gY() {
        super.gY();
        JO = null;
    }

    protected String[] hb() {
        return Jz;
    }

    public String he() {
        return "LocationSensor";
    }

    public int getSensorType() {
        return 5004;
    }

    protected boolean hc() {
        this.JM = null;
        try {
            cl("network");
            String str = (String) this.Id.getParameter("LOCATION_ACCURACY");
            if (str != null && str.equals("LOCATION_ACCURACY_FINE")) {
                cl("gps");
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    protected void cl(String str) {
        if (this.JL != null) {
            List allProviders = this.JL.getAllProviders();
            if (allProviders == null || !allProviders.contains(str)) {
                Log.m285d("LocationSensor", "requestLocationUpdates(), Not registering with " + str + " as it is unavailable");
                return;
            } else if (gZ() != null) {
                Looper looper = gZ().getLooper();
                if (looper == null || looper.getThread() == null || !looper.getThread().isAlive()) {
                    Log.m285d("LocationSensor", "requestLocationUpdates(), not requesting location updates as looper is null or looper thread is not alive");
                    return;
                } else {
                    this.JL.requestSingleUpdate(str, this.JN, looper);
                    return;
                }
            } else {
                Log.m285d("LocationSensor", "requestLocationUpdates(), not requesting location updates as getSensorHandler() is null");
                return;
            }
        }
        Log.m285d("LocationSensor", "requestLocationUpdates(), locationManager is null");
    }

    protected void hd() {
        try {
            if (this.JL != null) {
                this.JL.removeUpdates(this.JN);
            } else {
                Log.m285d("LocationSensor", "stopSensing(), locationManager is null");
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    protected SensorData hl() {
        return this.JP;
    }

    protected void hm() {
        this.JP = ((LocationProcessor) hi()).m1551a(this.Jn, this.JM, this.Id.gS(), 5004);
    }
}
