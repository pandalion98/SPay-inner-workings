package com.samsung.sensorframework.sda.p042e;

import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.PaymentNetworkProvider;
import com.samsung.sensorframework.sda.SDAException;
import com.samsung.sensorframework.sda.SensorDataListener;
import com.samsung.sensorframework.sda.p033b.SensorData;
import com.samsung.sensorframework.sda.p039d.SensorInterface;
import com.samsung.sensorframework.sda.p039d.p041b.PushSensor;

/* renamed from: com.samsung.sensorframework.sda.e.c */
public class PushSensorTask extends AbstractSensorTask implements SensorDataListener {
    public PushSensorTask(SensorInterface sensorInterface) {
        super(sensorInterface);
    }

    public void run() {
        Exception e;
        long j;
        while (this.state == 6123) {
            try {
                if (this.KM.hf()) {
                    synchronized (this.KN) {
                        if (this.state == 6123) {
                            this.KN.wait();
                        }
                    }
                } else {
                    ((PushSensor) this.KM).m1608a(this);
                }
            } catch (InterruptedException e2) {
            } catch (SDAException e3) {
                e = e3;
                e.printStackTrace();
                try {
                    if (e instanceof SecurityException) {
                        j = 300000;
                    } else if ((e instanceof SDAException) || ((SDAException) e).getErrorCode() != 8000) {
                        j = PaymentNetworkProvider.NFC_WAIT_TIME;
                    } else {
                        j = 300000;
                    }
                    Thread.sleep(j);
                } catch (Exception e4) {
                    e4.printStackTrace();
                }
            } catch (SecurityException e5) {
                e4 = e5;
                e4.printStackTrace();
                if (e4 instanceof SecurityException) {
                    j = 300000;
                } else {
                    if (e4 instanceof SDAException) {
                    }
                    j = PaymentNetworkProvider.NFC_WAIT_TIME;
                }
                Thread.sleep(j);
            }
        }
        if (this.KM.hf()) {
            try {
                ((PushSensor) this.KM).m1609b(this);
            } catch (SDAException e6) {
                e4 = e6;
                e4.printStackTrace();
                this.KM.gY();
                Log.m285d(he(), "Stopped PushSensorTask.");
            } catch (SecurityException e7) {
                e4 = e7;
                e4.printStackTrace();
                this.KM.gY();
                Log.m285d(he(), "Stopped PushSensorTask.");
            }
        }
        this.KM.gY();
        Log.m285d(he(), "Stopped PushSensorTask.");
    }

    public void m1644a(SensorData sensorData) {
        super.m1640c(sensorData);
    }
}
