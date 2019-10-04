/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.Intent
 *  android.content.IntentFilter
 *  android.hardware.Sensor
 *  android.hardware.SensorEvent
 *  android.hardware.SensorEventListener
 *  android.hardware.SensorManager
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 */
package com.samsung.sensorframework.sda.d.b;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import com.samsung.sensorframework.sda.a.c;
import com.samsung.sensorframework.sda.c.b.g;
import com.samsung.sensorframework.sda.d.b.a;

public class i
extends a {
    private static i KE;
    private static final Object lock;
    private SensorEventListener KF = new SensorEventListener(){

        public void onAccuracyChanged(Sensor sensor, int n2) {
        }

        public void onSensorChanged(SensorEvent sensorEvent) {
            try {
                float f2 = sensorEvent.values[0];
                float f3 = sensorEvent.sensor.getMaximumRange();
                com.samsung.sensorframework.sda.b.b.g g2 = ((g)i.this.hi()).a(System.currentTimeMillis(), i.this.Id.gS(), f2, f3);
                i.this.a(g2);
                return;
            }
            catch (Exception exception) {
                exception.printStackTrace();
                return;
            }
        }
    };

    static {
        lock = new Object();
    }

    private i(Context context) {
        super(context);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static i bi(Context context) {
        if (KE == null) {
            Object object;
            Object object2 = object = lock;
            synchronized (object2) {
                if (KE == null) {
                    KE = new i(context);
                }
            }
        }
        return KE;
    }

    @Override
    protected void a(Context context, Intent intent) {
    }

    @Override
    public void gY() {
        super.gY();
        KE = null;
    }

    @Override
    public int getSensorType() {
        return 5007;
    }

    @Override
    protected IntentFilter[] hC() {
        return null;
    }

    @Override
    protected boolean hc() {
        SensorManager sensorManager = (SensorManager)this.HR.getSystemService("sensor");
        return sensorManager.registerListener(this.KF, sensorManager.getDefaultSensor(8), 3);
    }

    @Override
    protected void hd() {
        ((SensorManager)this.HR.getSystemService("sensor")).unregisterListener(this.KF);
    }

    @Override
    public String he() {
        return "ProximitySensor";
    }

}

