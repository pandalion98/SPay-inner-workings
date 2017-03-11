package com.samsung.sensorframework.sdi.p046c;

import android.content.Context;
import com.google.android.gms.location.LocationStatusCodes;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spaytui.SPayTUIException;
import com.samsung.sensorframework.sda.SDAException;
import com.samsung.sensorframework.sda.SFSensorManager;
import com.samsung.sensorframework.sda.SensorDataListener;
import com.samsung.sensorframework.sda.p039d.SensorUtils;
import com.samsung.sensorframework.sda.p043f.DataAcquisitionUtils;
import com.samsung.sensorframework.sdi.exception.SDIException;
import com.samsung.sensorframework.sdi.p050g.SFUtils;
import com.samsung.sensorframework.sdm.datahandler.except.DataHandlerException;
import java.util.HashMap;

/* renamed from: com.samsung.sensorframework.sdi.c.a */
public class SDISensorHandler {
    private static final Object Ho;
    private static SDISensorHandler Lf;
    private SFSensorManager HQ;
    private SensorDataListener Kl;
    private HashMap<Integer, Integer> Lg;
    private final int[] Lh;
    private final int[] Li;
    private final int[] Lj;
    private Context context;

    static {
        Ho = new Object();
    }

    public static SDISensorHandler bq(Context context) {
        if (Lf == null) {
            synchronized (Ho) {
                if (Lf == null) {
                    Lf = new SDISensorHandler(context);
                }
            }
        }
        return Lf;
    }

    private SDISensorHandler(Context context) {
        this.Lh = new int[]{5002, 5011, 5004, 5038};
        this.Li = new int[]{5002, 5011};
        this.Lj = new int[]{5004, 5038};
        if (context == null) {
            throw new SDIException(9000, "context can not be null");
        }
        this.context = context;
        this.HQ = SFSensorManager.aN(context);
        if (this.HQ == null) {
            throw new SDIException(SPayTUIException.ERR_UNKNOWN, "sensorManager cannot be null.");
        }
        Log.m285d("SDISensorHandler", "instance created.");
    }

    public synchronized void hK() {
        Log.m285d("SDISensorHandler", "enableRadioSensors()");
        m1663a(this.Lj);
    }

    public synchronized void hL() {
        Log.m285d("SDISensorHandler", "enableBatteryAndConnectionSensors()");
        m1663a(this.Li);
    }

    private synchronized void m1663a(int[] iArr) {
        Log.m285d("SDISensorHandler", "enableSensors()");
        if (m1667c(iArr)) {
            Log.m286e("SDISensorHandler", "enableSensors() already enabled.");
        } else {
            hO();
            for (int i : iArr) {
                if (!ax(i)) {
                    aw(i);
                    ay(i);
                }
            }
        }
    }

    public synchronized void hM() {
        Log.m285d("SDISensorHandler", "disableAllSensors()");
        m1664b(this.Lh);
        if (this.Lg != null) {
            this.Lg.clear();
            this.Lg = null;
        }
        if (this.Kl != null) {
            if (this.Kl instanceof SFSensorDataListener) {
                ((SFSensorDataListener) this.Kl).hP();
            }
            this.Kl = null;
        }
    }

    public synchronized void hN() {
        Log.m285d("SDISensorHandler", "disableRadioSensors()");
        m1665d(this.Lj);
    }

    private synchronized void m1664b(int[] iArr) {
        Log.m285d("SDISensorHandler", "disableSensors()");
        m1665d(iArr);
    }

    private synchronized void hO() {
        try {
            Log.m285d("SDISensorHandler", "setGlobalConfig()");
            this.HQ.m1510b("INTENT_BROADCASTER_PERMISSION", "com.samsung.android.spayfw.permission.ACCESS_PF");
        } catch (SDAException e) {
            e.printStackTrace();
        }
    }

    private synchronized void aw(int i) {
        if (i == 5002) {
            try {
                this.HQ.m1509a(5002, "BATTERY_INTENT_FILTERS", "BATTERY_INTENT_FILTER_LOW_OK");
            } catch (SDAException e) {
                e.printStackTrace();
            }
        } else if (i == 5004) {
            this.HQ.m1509a(5004, "LOCATION_ACCURACY", "LOCATION_ACCURACY_COARSE");
            this.HQ.m1509a(5004, "SENSE_WINDOW_LENGTH_MILLIS", Long.valueOf(20000));
            m1666c(5004, 1800000);
        } else if (i == 5010) {
            m1666c(5010, 1200000);
        }
    }

    public synchronized void m1666c(int i, long j) {
        Log.m285d("SDISensorHandler", "setDutyCyclingInterval(), interval: " + j + " Sensor: " + DataAcquisitionUtils.ap(i));
        try {
            long aA = ((long) SFUtils.aA(LocationStatusCodes.GEOFENCE_NOT_AVAILABLE)) + j;
            Log.m285d("SDISensorHandler", "setDutyCyclingInterval(), updated interval after adding additional random time: " + aA);
            this.HQ.m1509a(i, "POST_SENSE_SLEEP_LENGTH_MILLIS", Long.valueOf(aA));
        } catch (Exception e) {
            e.printStackTrace();
            try {
                this.HQ.m1509a(i, "POST_SENSE_SLEEP_LENGTH_MILLIS", Long.valueOf(1800000));
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public synchronized boolean m1667c(int[] iArr) {
        boolean z = false;
        synchronized (this) {
            for (int ax : iArr) {
                if (!ax(ax)) {
                    break;
                }
            }
            z = true;
        }
        return z;
    }

    public synchronized boolean ax(int i) {
        boolean z;
        if (this.Lg == null || this.Lg.size() <= 0 || !this.Lg.containsKey(Integer.valueOf(i))) {
            Log.m285d("SDISensorHandler", "isSubscribedToSensor() false");
            z = false;
        } else {
            Log.m285d("SDISensorHandler", "isSubscribedToSensor() true");
            z = true;
        }
        return z;
    }

    private synchronized void ay(int i) {
        Exception e;
        Log.m285d("SDISensorHandler", "subscribeToSensor()");
        if (this.Lg == null) {
            this.Lg = new HashMap();
        }
        if (this.Kl == null) {
            try {
                this.Kl = new SFSensorDataListener(this.context);
            } catch (DataHandlerException e2) {
                e = e2;
                e.printStackTrace();
                if (!ax(i)) {
                    this.Lg.put(Integer.valueOf(i), Integer.valueOf(this.HQ.m1508a(i, this.Kl)));
                    Log.m285d("SDISensorHandler", "subscribeToSensor() subscribed to sensor: " + SensorUtils.ap(i));
                }
            } catch (SDAException e3) {
                e = e3;
                e.printStackTrace();
                if (ax(i)) {
                    this.Lg.put(Integer.valueOf(i), Integer.valueOf(this.HQ.m1508a(i, this.Kl)));
                    Log.m285d("SDISensorHandler", "subscribeToSensor() subscribed to sensor: " + SensorUtils.ap(i));
                }
            }
        }
        try {
            if (ax(i)) {
                this.Lg.put(Integer.valueOf(i), Integer.valueOf(this.HQ.m1508a(i, this.Kl)));
                Log.m285d("SDISensorHandler", "subscribeToSensor() subscribed to sensor: " + SensorUtils.ap(i));
            }
        } catch (SDAException e4) {
            e4.printStackTrace();
        }
    }

    private synchronized void m1665d(int[] iArr) {
        Log.m285d("SDISensorHandler", "unsubscribeFromSensors()");
        for (int az : iArr) {
            az(az);
        }
    }

    private synchronized void az(int i) {
        if (ax(i)) {
            try {
                this.HQ.ac(((Integer) this.Lg.get(Integer.valueOf(i))).intValue());
                this.Lg.remove(Integer.valueOf(i));
                Log.m285d("SDISensorHandler", "unsubscribeFromSensor() unsubscribed from sensor: " + SensorUtils.ap(i));
            } catch (SDAException e) {
                e.printStackTrace();
            }
        }
    }
}
