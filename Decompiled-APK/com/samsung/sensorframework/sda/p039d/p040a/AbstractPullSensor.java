package com.samsung.sensorframework.sda.p039d.p040a;

import android.content.Context;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.sensorframework.sda.SDAException;
import com.samsung.sensorframework.sda.p033b.SensorData;
import com.samsung.sensorframework.sda.p039d.AbstractSensor;

/* renamed from: com.samsung.sensorframework.sda.d.a.b */
public abstract class AbstractPullSensor extends AbstractSensor implements PullSensor {
    protected SensorData Ib;
    protected long Jn;

    protected abstract SensorData hl();

    protected abstract void hm();

    public AbstractPullSensor(Context context) {
        super(context);
    }

    public AbstractPullSensor(Context context, Integer num) {
        super(context, num);
    }

    public SensorData hn() {
        ha();
        if (this.Ji) {
            throw new SDAException(8003, "sensor busy");
        }
        this.Ji = true;
        this.Jn = System.currentTimeMillis();
        SensorData sensorData = null;
        if (hc()) {
            Log.m285d(he(), "Sensing started.");
            try {
                if (this.Id.bR("NUMBER_OF_SENSE_CYCLES")) {
                    while (this.Ji) {
                        synchronized (this.Jj) {
                            this.Jj.wait(500);
                        }
                    }
                    hd();
                    this.Ji = false;
                    Log.m285d(he(), "Sensing stopped.");
                    hm();
                    sensorData = hl();
                    if (sensorData != null) {
                        sensorData.m1511b(this.Ib);
                    }
                    this.Ib = sensorData;
                } else if (this.Id.bR("SENSE_WINDOW_LENGTH_MILLIS")) {
                    long longValue = ((Long) this.Id.getParameter("SENSE_WINDOW_LENGTH_MILLIS")).longValue();
                    synchronized (this.Jj) {
                        this.Jj.wait(longValue);
                    }
                    hd();
                    this.Ji = false;
                    Log.m285d(he(), "Sensing stopped.");
                    hm();
                    sensorData = hl();
                    if (sensorData != null) {
                        sensorData.m1511b(this.Ib);
                    }
                    this.Ib = sensorData;
                } else {
                    throw new SDAException(8005, "Invalid Sensor Config, window size or no. of cycles should in in the config");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            Log.m285d(he(), "Sensing not started.");
            this.Ji = false;
        }
        return sensorData;
    }

    protected void ho() {
        synchronized (this.Jj) {
            this.Ji = false;
            this.Jj.notify();
        }
    }
}
