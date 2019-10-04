/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.hardware.Sensor
 *  android.hardware.SensorEvent
 *  android.hardware.SensorEventListener
 *  android.hardware.SensorManager
 *  java.lang.Exception
 *  java.lang.Integer
 *  java.lang.Long
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.util.ArrayList
 */
package com.samsung.sensorframework.sda.d.a;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import com.samsung.sensorframework.sda.b.a;
import com.samsung.sensorframework.sda.d.a.b;
import java.util.ArrayList;

public class c
extends b {
    private static c Jq;
    private static final Object lock;
    private ArrayList<float[]> Ig;
    private ArrayList<Long> Ih;
    private SensorEventListener Jo;
    private SensorManager Jp;
    private com.samsung.sensorframework.sda.b.a.c Jr;

    static {
        lock = new Object();
    }

    private c(Context context) {
        super(context);
        this.Jp = (SensorManager)context.getSystemService("sensor");
        this.Jo = new SensorEventListener(){

            public void onAccuracyChanged(Sensor sensor, int n2) {
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             * Converted monitor instructions to comments
             * Lifted jumps to return sites
             */
            public void onSensorChanged(SensorEvent sensorEvent) {
                try {
                    ArrayList arrayList;
                    if (!c.this.Ji) return;
                    ArrayList arrayList2 = arrayList = c.this.Ig;
                    // MONITORENTER : arrayList2
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
                if (c.this.Ji) {
                    float[] arrf = new float[3];
                    for (int i2 = 0; i2 < 3; ++i2) {
                        arrf[i2] = sensorEvent.values[i2];
                    }
                    c.this.Ig.add((Object)arrf);
                    c.this.Ih.add((Object)System.currentTimeMillis());
                }
                // MONITOREXIT : arrayList2
                return;
            }
        };
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static c aO(Context context) {
        if (Jq == null) {
            Object object;
            Object object2 = object = lock;
            synchronized (object2) {
                if (Jq == null) {
                    Jq = new c(context);
                }
            }
        }
        return Jq;
    }

    @Override
    public void gY() {
        super.gY();
        Jq = null;
    }

    @Override
    public int getSensorType() {
        return 5001;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    protected boolean hc() {
        int n2 = 1;
        this.Ig = new ArrayList();
        this.Ih = new ArrayList();
        int n3 = this.Id.bR("ACCELEROMETER_SAMPLING_DELAY") ? (Integer)this.Id.getParameter("ACCELEROMETER_SAMPLING_DELAY") : n2;
        if (this.Id.bR("ACCELERATION_SENSOR_TYPE")) {
            n2 = (Integer)this.Id.getParameter("ACCELERATION_SENSOR_TYPE");
        }
        return this.Jp.registerListener(this.Jo, this.Jp.getDefaultSensor(n2), n3);
    }

    @Override
    protected void hd() {
        this.Jp.unregisterListener(this.Jo);
    }

    @Override
    protected String he() {
        return "AccelerometerSensor";
    }

    @Override
    protected /* synthetic */ a hl() {
        return this.hp();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    protected void hm() {
        ArrayList<float[]> arrayList;
        ArrayList<float[]> arrayList2 = arrayList = this.Ig;
        synchronized (arrayList2) {
            this.Jr = ((com.samsung.sensorframework.sda.c.a.a)this.hi()).a(this.Jn, this.Ig, this.Ih, this.Id.gS());
            return;
        }
    }

    protected com.samsung.sensorframework.sda.b.a.c hp() {
        return this.Jr;
    }

}

