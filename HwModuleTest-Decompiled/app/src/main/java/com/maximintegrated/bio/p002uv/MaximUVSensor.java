package com.maximintegrated.bio.p002uv;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

/* renamed from: com.maximintegrated.bio.uv.MaximUVSensor */
public class MaximUVSensor implements SensorEventListener, LocationListener {
    private static final int IN_ACC_X = 4;
    private static final int IN_ACC_Y = 5;
    private static final int IN_ACC_Z = 6;
    private static final int IN_ALTITUDE = 3;
    private static final int IN_LATITUDE = 1;
    private static final int IN_LONGITUDE = 2;
    private static final int IN_MAG_X = 8;
    private static final int IN_MAG_Y = 9;
    private static final int IN_MAG_Z = 10;
    private static final int IN_PRESS = 7;
    private static final int IN_UVRAW = 0;
    private static final String LOGTAG = "MaximUV";
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 60000;
    private static final int OUT_UVA = 2;
    private static final int OUT_UVB = 3;
    private static final int OUT_UVINDEX = 1;
    private static final int OUT_UVINTENSITY = 4;
    private static final int OUT_UVRAW = 0;
    private static final String VERSION = "1.0";
    private static boolean isJniLoaded;
    private Sensor accSensor;
    private float[] algorithm_data_in = {0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
    private float[] algorithm_data_out = {0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
    private final Context context;
    private boolean isLocationAvailable = false;
    private Location location = null;
    private LocationManager locationManager;
    private Sensor magSensor;
    private Sensor pressSensor;
    private int sensor_type = 0;

    /* renamed from: sm */
    private SensorManager f4sm;
    private MaximUVSensorEvent uvEvent;
    private MaximUVSensorEventListener uvListener = null;
    private Sensor uvSensor;

    private native void runAlgorithm(float[] fArr, float[] fArr2);

    private native void startAlgorithm();

    private native void stopAlgorithm();

    public MaximUVSensor(Context context2, int sensor_type2) {
        Log.i(LOGTAG, "Version:1.0");
        this.context = context2;
        this.sensor_type = sensor_type2;
        this.f4sm = (SensorManager) context2.getSystemService("sensor");
        this.uvSensor = this.f4sm.getDefaultSensor(sensor_type2);
        String str = LOGTAG;
        StringBuilder sb = new StringBuilder("MaximUVSensor() - uvSensor : ");
        sb.append(this.uvSensor);
        Log.d(str, sb.toString());
        this.accSensor = this.f4sm.getDefaultSensor(1);
        String str2 = LOGTAG;
        StringBuilder sb2 = new StringBuilder("MaximUVSensor() - accSensor : ");
        sb2.append(this.accSensor);
        Log.d(str2, sb2.toString());
        this.pressSensor = this.f4sm.getDefaultSensor(6);
        String str3 = LOGTAG;
        StringBuilder sb3 = new StringBuilder("MaximUVSensor() - pressSensor : ");
        sb3.append(this.pressSensor);
        Log.d(str3, sb3.toString());
        this.magSensor = this.f4sm.getDefaultSensor(2);
        String str4 = LOGTAG;
        StringBuilder sb4 = new StringBuilder("MaximUVSensor() - magSensor : ");
        sb4.append(this.magSensor);
        Log.d(str4, sb4.toString());
        this.locationManager = (LocationManager) context2.getSystemService("location");
    }

    public Sensor getSensor() {
        return this.uvSensor;
    }

    public boolean isLocationAvailable() {
        if (this.locationManager != null) {
            boolean isGPSEnabled = this.locationManager.isProviderEnabled("gps");
            boolean isNetworkEnabled = this.locationManager.isProviderEnabled("network");
            this.location = this.locationManager.getLastKnownLocation("passive");
            if (this.location != null || isGPSEnabled || isNetworkEnabled) {
                this.isLocationAvailable = true;
                String str = LOGTAG;
                StringBuilder sb = new StringBuilder("Location information is available : ");
                sb.append(this.location);
                sb.append(",");
                sb.append(isGPSEnabled);
                sb.append(",");
                sb.append(isNetworkEnabled);
                Log.i(str, sb.toString());
            } else {
                this.isLocationAvailable = false;
                String str2 = LOGTAG;
                StringBuilder sb2 = new StringBuilder("Location information is unavailable : ");
                sb2.append(this.location);
                sb2.append(",");
                sb2.append(isGPSEnabled);
                sb2.append(",");
                sb2.append(isNetworkEnabled);
                Log.e(str2, sb2.toString());
            }
        } else {
            this.isLocationAvailable = false;
        }
        return this.isLocationAvailable;
    }

    public void registerListener(MaximUVSensorEventListener uvListener2) {
        this.location = null;
        this.uvListener = uvListener2;
        this.uvEvent = new MaximUVSensorEvent();
        if (isJniLoaded) {
            startAlgorithm();
        } else {
            Log.e(LOGTAG, "JNI not loaded calling startAlgorithm failed");
        }
        this.f4sm.registerListener(this, this.uvSensor, 3);
        this.f4sm.registerListener(this, this.accSensor, 0);
        this.f4sm.registerListener(this, this.pressSensor, 3);
        this.f4sm.registerListener(this, this.magSensor, 0);
        boolean isGPSEnabled = this.locationManager.isProviderEnabled("gps");
        boolean isNetworkEnabled = this.locationManager.isProviderEnabled("network");
        String str = LOGTAG;
        StringBuilder sb = new StringBuilder("MaximUVSensor registerListener() - isGPSEnabled : ");
        sb.append(isGPSEnabled);
        sb.append(", isNetworkEnabled : ");
        sb.append(isNetworkEnabled);
        Log.d(str, sb.toString());
        this.location = this.locationManager.getLastKnownLocation("passive");
        if (this.location != null) {
            Log.i(LOGTAG, "PASSIVE_PROVIDER provide location information");
        }
        if (this.location == null && (isGPSEnabled || isNetworkEnabled)) {
            Log.i(LOGTAG, "PASSIVE_PROVIDER DO NOT provide location information");
            if (isNetworkEnabled) {
                this.locationManager.requestLocationUpdates("network", MIN_TIME_BW_UPDATES, 10.0f, this);
                if (this.locationManager != null) {
                    this.location = this.locationManager.getLastKnownLocation("network");
                    if (this.location != null) {
                        Log.i(LOGTAG, "NETWORK_PROVIDER provide location information");
                    }
                }
            }
            if (isGPSEnabled && this.location == null) {
                Log.i(LOGTAG, "NETWORK_PROVIDER DO NOT provide location information");
                this.locationManager.requestLocationUpdates("gps", MIN_TIME_BW_UPDATES, 10.0f, this);
                if (this.locationManager != null) {
                    this.location = this.locationManager.getLastKnownLocation("gps");
                    if (this.location != null) {
                        Log.i(LOGTAG, "GPS_PROVIDER provide location information");
                    }
                }
            }
        }
        if (this.location != null) {
            return;
        }
        if (isNetworkEnabled || isGPSEnabled) {
            Log.i(LOGTAG, "No LastKnowLocation, waiting for LocationListner");
        } else {
            Log.i(LOGTAG, "No Location Service : algorithm will not run");
        }
    }

    public void unregisterListener(MaximUVSensorEventListener uvListener2) {
        this.f4sm.unregisterListener(this);
        if (this.locationManager != null) {
            this.locationManager.removeUpdates(this);
        }
        this.location = null;
        if (isJniLoaded) {
            stopAlgorithm();
        } else {
            Log.e(LOGTAG, "JNI not loaded calling stopAlgorithm failed");
        }
        this.uvListener = null;
        this.uvEvent = null;
    }

    public void onSensorChanged(SensorEvent event) {
        synchronized (this) {
            int type = event.sensor.getType();
            if (type != 6) {
                switch (type) {
                    case 1:
                        this.algorithm_data_in[4] = event.values[0];
                        this.algorithm_data_in[5] = event.values[1];
                        this.algorithm_data_in[6] = event.values[2];
                        break;
                    case 2:
                        this.algorithm_data_in[8] = event.values[0];
                        this.algorithm_data_in[9] = event.values[1];
                        this.algorithm_data_in[10] = event.values[2];
                        break;
                }
            } else {
                this.algorithm_data_in[7] = event.values[0];
            }
            if (event.sensor.getType() == this.sensor_type && this.uvListener != null) {
                this.algorithm_data_in[0] = event.values[0];
                if (this.location == null) {
                    Log.d(LOGTAG, "location null");
                    this.algorithm_data_in[1] = -1.0f;
                    this.algorithm_data_in[2] = -1.0f;
                    this.algorithm_data_in[3] = -1.0f;
                } else {
                    this.algorithm_data_in[1] = (float) this.location.getLatitude();
                    this.algorithm_data_in[2] = (float) this.location.getLongitude();
                    this.algorithm_data_in[3] = (float) this.location.getAltitude();
                }
                if (isJniLoaded) {
                    runAlgorithm(this.algorithm_data_in, this.algorithm_data_out);
                } else {
                    Log.e(LOGTAG, "JNI not loaded calling runAlgorithm failed");
                }
                this.uvEvent.RAW = (int) this.algorithm_data_out[0];
                this.uvEvent.UVA = (double) this.algorithm_data_out[2];
                this.uvEvent.UVB = (double) this.algorithm_data_out[3];
                this.uvEvent.UVIndex = (double) this.algorithm_data_out[1];
                this.uvEvent.UVIntensity = (double) this.algorithm_data_out[4];
                this.uvListener.onMaximUVSensorChanged(this.uvEvent);
            }
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onLocationChanged(Location location2) {
        Log.d(LOGTAG, "Location Updated");
        this.location = location2;
    }

    public void onProviderDisabled(String provider) {
        Log.d(LOGTAG, "onProviderDisabled called");
    }

    public void onProviderEnabled(String provider) {
        Log.d(LOGTAG, "onProviderEnabled called");
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d(LOGTAG, "onStatusChanged called");
    }

    static {
        try {
            System.loadLibrary("MxmUVSensor-jni");
            isJniLoaded = true;
        } catch (UnsatisfiedLinkError e) {
            isJniLoaded = false;
            Log.e(LOGTAG, "JNI File Not Found");
        }
    }
}
