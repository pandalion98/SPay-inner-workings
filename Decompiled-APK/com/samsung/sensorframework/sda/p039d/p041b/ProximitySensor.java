package com.samsung.sensorframework.sda.p039d.p041b;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import com.samsung.sensorframework.sda.p036c.p038b.ProximityProcessor;

/* renamed from: com.samsung.sensorframework.sda.d.b.i */
public class ProximitySensor extends AbstractPushSensor {
    private static ProximitySensor KE;
    private static final Object lock;
    private SensorEventListener KF;

    /* renamed from: com.samsung.sensorframework.sda.d.b.i.1 */
    class ProximitySensor implements SensorEventListener {
        final /* synthetic */ ProximitySensor KG;

        ProximitySensor(ProximitySensor proximitySensor) {
            this.KG = proximitySensor;
        }

        public void onSensorChanged(SensorEvent sensorEvent) {
            try {
                this.KG.m1613a(((ProximityProcessor) this.KG.hi()).m1568a(System.currentTimeMillis(), this.KG.Id.gS(), sensorEvent.values[0], sensorEvent.sensor.getMaximumRange()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    }

    static {
        lock = new Object();
    }

    public static ProximitySensor bi(Context context) {
        if (KE == null) {
            synchronized (lock) {
                if (KE == null) {
                    KE = new ProximitySensor(context);
                }
            }
        }
        return KE;
    }

    private ProximitySensor(Context context) {
        super(context);
        this.KF = new ProximitySensor(this);
    }

    public void gY() {
        super.gY();
        KE = null;
    }

    public String he() {
        return "ProximitySensor";
    }

    public int getSensorType() {
        return 5007;
    }

    protected void m1630a(Context context, Intent intent) {
    }

    protected IntentFilter[] hC() {
        return null;
    }

    protected boolean hc() {
        SensorManager sensorManager = (SensorManager) this.HR.getSystemService("sensor");
        return sensorManager.registerListener(this.KF, sensorManager.getDefaultSensor(8), 3);
    }

    protected void hd() {
        ((SensorManager) this.HR.getSystemService("sensor")).unregisterListener(this.KF);
    }
}
