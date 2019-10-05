/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.annotation.SuppressLint
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
 *  java.util.HashMap
 */
package com.samsung.sensorframework.sda.d.a;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.samsung.android.spayfw.b.Log;
import com.samsung.sensorframework.sda.SDAException;
import com.samsung.sensorframework.sda.b.a;
import com.samsung.sensorframework.sda.b.a.m;
import com.samsung.sensorframework.sda.d.c;
import java.util.ArrayList;
import java.util.HashMap;

public class i
extends b {
    @SuppressLint(value={"UseSparseArrays"})
    private static final HashMap<Integer, i> JI = new HashMap();
    private ArrayList<float[]> Ig;
    private ArrayList<Long> Ih;
    private m JH;
    private SensorEventListener Jo;
    private SensorManager Jp;

    private i(Context context, int n2) {
        super(context, n2);
        this.Jp = (SensorManager)context.getSystemService("sensor");
        int n3 = this.aq(n2);
        if (this.Jp.getDefaultSensor(n3) == null) {
            Log.d(this.he(), "no sensor hardware available");
            throw new SDAException(8015, "sensor hardware not available on the phone");
        }
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
                    if (!i.this.Ji) return;
                    ArrayList arrayList2 = arrayList = i.this.Ig;
                    // MONITORENTER : arrayList2
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
                if (i.this.Ji) {
                    float[] arrf = new float[sensorEvent.values.length];
                    for (int i2 = 0; i2 < sensorEvent.values.length; ++i2) {
                        arrf[i2] = sensorEvent.values[i2];
                    }
                    i.this.Ig.add((Object)arrf);
                    i.this.Ih.add((Object)System.currentTimeMillis());
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
    public static i b(Context context, int n2) {
        HashMap<Integer, i> hashMap;
        HashMap<Integer, i> hashMap2 = hashMap = JI;
        synchronized (hashMap2) {
            if (JI.containsKey((Object)n2)) {
                return (i)JI.get((Object)n2);
            }
            i i2 = new i(context, n2);
            JI.put((Object)n2, (Object)i2);
            return i2;
        }
    }

    protected int aq(int n2) {
        switch (n2) {
            default: {
                throw new SDAException(8001, "unknown sensor type: " + n2);
            }
            case 5026: {
                return 13;
            }
            case 5027: {
                return 9;
            }
            case 5028: {
                return 4;
            }
            case 5029: {
                return 5;
            }
            case 5030: {
                return 2;
            }
            case 5031: {
                return 6;
            }
            case 5032: {
                return 12;
            }
            case 5033: 
        }
        return 11;
    }

    @Override
    public void gY() {
        super.gY();
        if (JI.containsKey((Object)this.Im)) {
            JI.remove((Object)this.Im);
        }
    }

    @Override
    public int getSensorType() {
        return this.Im;
    }

    @Override
    protected boolean hc() {
        this.Ig = new ArrayList();
        this.Ih = new ArrayList();
        int n2 = 1;
        if (this.Id.bR("SENSOR_SAMPLING_DELAY")) {
            n2 = (Integer)this.Id.getParameter("SENSOR_SAMPLING_DELAY");
        }
        try {
            boolean bl = this.Jp.registerListener(this.Jo, this.Jp.getDefaultSensor(this.aq(this.Im)), n2);
            return bl;
        }
        catch (SDAException sDAException) {
            sDAException.printStackTrace();
            return false;
        }
    }

    @Override
    protected void hd() {
        this.Jp.unregisterListener(this.Jo);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    protected String he() {
        String string = "";
        try {
            String string2;
            string = string2 = string + c.ap(this.Im) + "Sensor";
        }
        catch (SDAException sDAException) {
            sDAException.printStackTrace();
            return string + "(GenericAndroidSensor)";
        }
        do {
            return string + "(GenericAndroidSensor)";
            break;
        } while (true);
    }

    @Override
    protected /* synthetic */ a hl() {
        return this.hu();
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
            this.JH = ((com.samsung.sensorframework.sda.c.a.i)this.hi()).a(this.Jn, this.Ig, this.Ih, this.Id.gS(), this.Im);
            return;
        }
    }

    protected m hu() {
        return this.JH;
    }

}

