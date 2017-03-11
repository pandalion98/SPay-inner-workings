package com.samsung.sensorframework.sda.p039d.p040a;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import com.samsung.sensorframework.sda.SDAException;
import com.samsung.sensorframework.sda.p033b.SensorData;
import com.samsung.sensorframework.sda.p033b.p034a.GenericAndroidSensorData;
import com.samsung.sensorframework.sda.p036c.p037a.GenericAndroidSensorProcessor;
import com.samsung.sensorframework.sda.p039d.SensorUtils;
import java.util.ArrayList;
import java.util.HashMap;

/* renamed from: com.samsung.sensorframework.sda.d.a.i */
public class GenericAndroidSensor extends AbstractPullSensor {
    @SuppressLint({"UseSparseArrays"})
    private static final HashMap<Integer, GenericAndroidSensor> JI;
    private ArrayList<float[]> Ig;
    private ArrayList<Long> Ih;
    private GenericAndroidSensorData JH;
    private SensorEventListener Jo;
    private SensorManager Jp;

    /* renamed from: com.samsung.sensorframework.sda.d.a.i.1 */
    class GenericAndroidSensor implements SensorEventListener {
        final /* synthetic */ GenericAndroidSensor JJ;

        GenericAndroidSensor(GenericAndroidSensor genericAndroidSensor) {
            this.JJ = genericAndroidSensor;
        }

        public void onAccuracyChanged(Sensor sensor, int i) {
        }

        public void onSensorChanged(SensorEvent sensorEvent) {
            try {
                if (this.JJ.Ji) {
                    synchronized (this.JJ.Ig) {
                        if (this.JJ.Ji) {
                            Object obj = new float[sensorEvent.values.length];
                            for (int i = 0; i < sensorEvent.values.length; i++) {
                                obj[i] = sensorEvent.values[i];
                            }
                            this.JJ.Ig.add(obj);
                            this.JJ.Ih.add(Long.valueOf(System.currentTimeMillis()));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected /* synthetic */ SensorData hl() {
        return hu();
    }

    static {
        JI = new HashMap();
    }

    public static GenericAndroidSensor m1593b(Context context, int i) {
        GenericAndroidSensor genericAndroidSensor;
        synchronized (JI) {
            if (JI.containsKey(Integer.valueOf(i))) {
                genericAndroidSensor = (GenericAndroidSensor) JI.get(Integer.valueOf(i));
            } else {
                genericAndroidSensor = new GenericAndroidSensor(context, i);
                JI.put(Integer.valueOf(i), genericAndroidSensor);
            }
        }
        return genericAndroidSensor;
    }

    private GenericAndroidSensor(Context context, int i) {
        super(context, Integer.valueOf(i));
        this.Jp = (SensorManager) context.getSystemService("sensor");
        if (this.Jp.getDefaultSensor(aq(i)) == null) {
            Log.m285d(he(), "no sensor hardware available");
            throw new SDAException(8015, "sensor hardware not available on the phone");
        } else {
            this.Jo = new GenericAndroidSensor(this);
        }
    }

    public void gY() {
        super.gY();
        if (JI.containsKey(Integer.valueOf(this.Im))) {
            JI.remove(Integer.valueOf(this.Im));
        }
    }

    protected int aq(int i) {
        switch (i) {
            case 5026:
                return 13;
            case 5027:
                return 9;
            case 5028:
                return 4;
            case 5029:
                return 5;
            case 5030:
                return 2;
            case 5031:
                return 6;
            case 5032:
                return 12;
            case 5033:
                return 11;
            default:
                throw new SDAException(8001, "unknown sensor type: " + i);
        }
    }

    protected String he() {
        String str = BuildConfig.FLAVOR;
        try {
            str = str + SensorUtils.ap(this.Im) + "Sensor";
        } catch (SDAException e) {
            e.printStackTrace();
        }
        return str + "(GenericAndroidSensor)";
    }

    public int getSensorType() {
        return this.Im;
    }

    protected GenericAndroidSensorData hu() {
        return this.JH;
    }

    protected void hm() {
        synchronized (this.Ig) {
            this.JH = ((GenericAndroidSensorProcessor) hi()).m1547a(this.Jn, this.Ig, this.Ih, this.Id.gS(), this.Im);
        }
    }

    protected boolean hc() {
        this.Ig = new ArrayList();
        this.Ih = new ArrayList();
        int i = 1;
        if (this.Id.bR("SENSOR_SAMPLING_DELAY")) {
            i = ((Integer) this.Id.getParameter("SENSOR_SAMPLING_DELAY")).intValue();
        }
        try {
            return this.Jp.registerListener(this.Jo, this.Jp.getDefaultSensor(aq(this.Im)), i);
        } catch (SDAException e) {
            e.printStackTrace();
            return false;
        }
    }

    protected void hd() {
        this.Jp.unregisterListener(this.Jo);
    }
}
