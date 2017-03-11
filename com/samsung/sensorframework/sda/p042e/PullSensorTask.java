package com.samsung.sensorframework.sda.p042e;

import android.os.SystemClock;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.sensorframework.sda.SDAException;
import com.samsung.sensorframework.sda.p039d.SensorInterface;
import com.samsung.sensorframework.sda.p039d.SensorUtils;
import com.samsung.sensorframework.sda.p039d.p040a.PullSensor;

/* renamed from: com.samsung.sensorframework.sda.e.b */
public class PullSensorTask extends AbstractSensorTask {
    public PullSensorTask(SensorInterface sensorInterface) {
        super(sensorInterface);
    }

    public void run() {
        long longValue;
        Exception e;
        while (this.state == 6123) {
            try {
                Log.m285d(he(), "Pulling from: " + SensorUtils.ap(this.KM.getSensorType()));
                m1640c(((PullSensor) this.KM).hn());
                longValue = ((Long) this.KM.cj("POST_SENSE_SLEEP_LENGTH_MILLIS")).longValue();
                synchronized (this.KN) {
                    if (this.state == 6123) {
                        m1643a(this.KN, longValue);
                    }
                }
            } catch (InterruptedException e2) {
            } catch (SDAException e3) {
                e = e3;
                e.printStackTrace();
                try {
                    if (e instanceof SecurityException) {
                        longValue = 300000;
                    } else if ((e instanceof SDAException) || ((SDAException) e).getErrorCode() != 8000) {
                        longValue = 300000;
                    } else {
                        longValue = 300000;
                    }
                    Thread.sleep(longValue);
                } catch (Exception e4) {
                    e4.printStackTrace();
                }
            } catch (SecurityException e5) {
                e4 = e5;
                e4.printStackTrace();
                if (e4 instanceof SecurityException) {
                    longValue = 300000;
                } else {
                    if (e4 instanceof SDAException) {
                    }
                    longValue = 300000;
                }
                Thread.sleep(longValue);
            }
        }
        this.KM.gY();
        Log.m285d(he(), "Stopped PullSensorTask.");
    }

    protected void m1643a(Object obj, long j) {
        Log.m285d(he(), "waitElapsedRealtime() original sleep duration: " + j);
        long j2 = j;
        while (j > 0 && this.state == 6123) {
            long min = Math.min(15000, j);
            long elapsedRealtime = SystemClock.elapsedRealtime();
            obj.wait(min);
            elapsedRealtime = j - (SystemClock.elapsedRealtime() - elapsedRealtime);
            try {
                min = ((Long) this.KM.cj("POST_SENSE_SLEEP_LENGTH_MILLIS")).longValue();
                if (min != j2) {
                    Log.m285d(he(), "waitElapsedRealtime() Sleep duration config has changed, Current config: " + j2 + " Time remaining in current config: " + elapsedRealtime + " Updated config: " + min);
                    try {
                        Log.m285d(he(), "waitElapsedRealtime() sleeping for: " + min);
                        elapsedRealtime = min;
                    } catch (SDAException e) {
                        elapsedRealtime = min;
                    }
                } else {
                    min = elapsedRealtime;
                    elapsedRealtime = j2;
                }
            } catch (SDAException e2) {
                min = elapsedRealtime;
                elapsedRealtime = j2;
            }
            j = min;
            j2 = elapsedRealtime;
        }
    }
}
