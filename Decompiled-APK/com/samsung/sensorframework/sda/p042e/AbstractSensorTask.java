package com.samsung.sensorframework.sda.p042e;

import com.samsung.android.spayfw.p002b.Log;
import com.samsung.sensorframework.sda.SDAException;
import com.samsung.sensorframework.sda.SensorDataListener;
import com.samsung.sensorframework.sda.p033b.SensorData;
import com.samsung.sensorframework.sda.p039d.SensorInterface;
import com.samsung.sensorframework.sda.p039d.SensorUtils;
import java.lang.Thread.State;
import java.util.ArrayList;
import java.util.Iterator;
import org.bouncycastle.asn1.eac.EACTags;

/* renamed from: com.samsung.sensorframework.sda.e.a */
public abstract class AbstractSensorTask extends Thread {
    protected SensorInterface KM;
    protected final Object KN;
    protected final ArrayList<SensorDataListener> KO;
    protected int state;

    public AbstractSensorTask(SensorInterface sensorInterface) {
        this.KN = new Object();
        this.state = 6122;
        this.KM = sensorInterface;
        this.KO = new ArrayList();
        if (this.KM != null) {
            setName("SensorTaskThread:" + this.KM.getSensorType());
        }
    }

    protected String he() {
        try {
            return "SensorTask:" + SensorUtils.ap(this.KM.getSensorType());
        } catch (SDAException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getSensorType() {
        return this.KM.getSensorType();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean m1641c(com.samsung.sensorframework.sda.SensorDataListener r5) {
        /*
        r4 = this;
        r0 = 0;
        r1 = r4.he();
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "registerSensorDataListener() listener: ";
        r2 = r2.append(r3);
        r2 = r2.append(r5);
        r2 = r2.toString();
        com.samsung.android.spayfw.p002b.Log.m285d(r1, r2);
        r2 = r4.KO;
        monitor-enter(r2);
        r1 = r0;
    L_0x001f:
        r3 = r4.KO;	 Catch:{ all -> 0x003f }
        r3 = r3.size();	 Catch:{ all -> 0x003f }
        if (r1 >= r3) goto L_0x0034;
    L_0x0027:
        r3 = r4.KO;	 Catch:{ all -> 0x003f }
        r3 = r3.get(r1);	 Catch:{ all -> 0x003f }
        if (r3 != r5) goto L_0x0031;
    L_0x002f:
        monitor-exit(r2);	 Catch:{ all -> 0x003f }
    L_0x0030:
        return r0;
    L_0x0031:
        r1 = r1 + 1;
        goto L_0x001f;
    L_0x0034:
        r0 = r4.KO;	 Catch:{ all -> 0x003f }
        r0.add(r5);	 Catch:{ all -> 0x003f }
        r4.startTask();	 Catch:{ all -> 0x003f }
        r0 = 1;
        monitor-exit(r2);	 Catch:{ all -> 0x003f }
        goto L_0x0030;
    L_0x003f:
        r0 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x003f }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.sensorframework.sda.e.a.c(com.samsung.sensorframework.sda.b):boolean");
    }

    protected void m1640c(SensorData sensorData) {
        synchronized (this.KO) {
            Iterator it = this.KO.iterator();
            while (it.hasNext()) {
                SensorDataListener sensorDataListener = (SensorDataListener) it.next();
                if (sensorData != null) {
                    sensorDataListener.m1530a(sensorData);
                } else {
                    Log.m285d(he(), "sensorData is null");
                }
            }
        }
    }

    public void m1642d(SensorDataListener sensorDataListener) {
        Object obj = null;
        synchronized (this.KO) {
            this.KO.remove(sensorDataListener);
            if (this.KO.isEmpty()) {
                obj = 1;
            }
        }
        if (obj != null) {
            hD();
        }
    }

    public void startTask() {
        if (this.state == 6122) {
            this.state = 6123;
            super.start();
        }
    }

    private void hD() {
        Log.m285d(he(), "stopTask(), state: " + this.state);
        if (this.state == 6123) {
            synchronized (this.KN) {
                this.state = 6124;
                this.KN.notify();
            }
        } else {
            this.state = 6124;
        }
        hE();
        Log.m285d(he(), "stopTask(), finished");
    }

    private void hE() {
        Log.m285d(he(), "waitUntilThreadTerminates()");
        int i = 1;
        while (i <= EACTags.COMPATIBLE_TAG_ALLOCATION_AUTHORITY) {
            try {
                Thread.sleep(1000);
                if (getState() == State.TERMINATED) {
                    Log.m285d(he(), "waitUntilThreadTerminates() thread terminated");
                    return;
                }
                i++;
            } catch (InterruptedException e) {
            }
        }
    }

    public boolean isStopped() {
        if (this.state == 6124) {
            return true;
        }
        return false;
    }
}
