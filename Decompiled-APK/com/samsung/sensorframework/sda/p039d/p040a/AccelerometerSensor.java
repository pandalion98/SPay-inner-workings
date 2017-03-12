package com.samsung.sensorframework.sda.p039d.p040a;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import com.samsung.sensorframework.sda.p033b.SensorData;
import com.samsung.sensorframework.sda.p033b.p034a.AccelerometerData;
import com.samsung.sensorframework.sda.p036c.p037a.AccelerometerProcessor;
import java.util.ArrayList;

/* renamed from: com.samsung.sensorframework.sda.d.a.c */
public class AccelerometerSensor extends AbstractPullSensor {
    private static AccelerometerSensor Jq;
    private static final Object lock;
    private ArrayList<float[]> Ig;
    private ArrayList<Long> Ih;
    private SensorEventListener Jo;
    private SensorManager Jp;
    private AccelerometerData Jr;

    /* renamed from: com.samsung.sensorframework.sda.d.a.c.1 */
    class AccelerometerSensor implements SensorEventListener {
        final /* synthetic */ AccelerometerSensor Js;

        AccelerometerSensor(AccelerometerSensor accelerometerSensor) {
            this.Js = accelerometerSensor;
        }

        public void onAccuracyChanged(Sensor sensor, int i) {
        }

        public void onSensorChanged(SensorEvent sensorEvent) {
            try {
                if (this.Js.Ji) {
                    synchronized (this.Js.Ig) {
                        if (this.Js.Ji) {
                            Object obj = new float[3];
                            for (int i = 0; i < 3; i++) {
                                obj[i] = sensorEvent.values[i];
                            }
                            this.Js.Ig.add(obj);
                            this.Js.Ih.add(Long.valueOf(System.currentTimeMillis()));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected /* synthetic */ SensorData hl() {
        return hp();
    }

    static {
        lock = new Object();
    }

    public static AccelerometerSensor aO(Context context) {
        if (Jq == null) {
            synchronized (lock) {
                if (Jq == null) {
                    Jq = new AccelerometerSensor(context);
                }
            }
        }
        return Jq;
    }

    private AccelerometerSensor(Context context) {
        super(context);
        this.Jp = (SensorManager) context.getSystemService("sensor");
        this.Jo = new AccelerometerSensor(this);
    }

    public void gY() {
        super.gY();
        Jq = null;
    }

    protected String he() {
        return "AccelerometerSensor";
    }

    public int getSensorType() {
        return 5001;
    }

    protected AccelerometerData hp() {
        return this.Jr;
    }

    protected void hm() {
        synchronized (this.Ig) {
            this.Jr = ((AccelerometerProcessor) hi()).m1532a(this.Jn, this.Ig, this.Ih, this.Id.gS());
        }
    }

    protected boolean hc() {
        int intValue;
        int i = 1;
        this.Ig = new ArrayList();
        this.Ih = new ArrayList();
        if (this.Id.bR("ACCELEROMETER_SAMPLING_DELAY")) {
            intValue = ((Integer) this.Id.getParameter("ACCELEROMETER_SAMPLING_DELAY")).intValue();
        } else {
            intValue = 1;
        }
        if (this.Id.bR("ACCELERATION_SENSOR_TYPE")) {
            i = ((Integer) this.Id.getParameter("ACCELERATION_SENSOR_TYPE")).intValue();
        }
        return this.Jp.registerListener(this.Jo, this.Jp.getDefaultSensor(i), intValue);
    }

    protected void hd() {
        this.Jp.unregisterListener(this.Jo);
    }
}
