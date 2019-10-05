/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.InterruptedException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Thread
 *  java.lang.Thread$State
 *  java.util.ArrayList
 *  java.util.Iterator
 */
package com.samsung.sensorframework.sda.e;

import com.samsung.android.spayfw.b.Log;
import com.samsung.sensorframework.sda.SDAException;
import com.samsung.sensorframework.sda.b;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class a
extends Thread {
    protected com.samsung.sensorframework.sda.d.b KM;
    protected final Object KN = new Object();
    protected final ArrayList<b> KO;
    protected int state = 6122;

    public a(com.samsung.sensorframework.sda.d.b b2) {
        this.KM = b2;
        this.KO = new ArrayList();
        if (this.KM != null) {
            this.setName("SensorTaskThread:" + this.KM.getSensorType());
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void hD() {
        Log.d(this.he(), "stopTask(), state: " + this.state);
        if (this.state == 6123) {
            Object object;
            Object object2 = object = this.KN;
            synchronized (object2) {
                this.state = 6124;
                this.KN.notify();
            }
        } else {
            this.state = 6124;
        }
        this.hE();
        Log.d(this.he(), "stopTask(), finished");
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private void hE() {
        Log.d(this.he(), "waitUntilThreadTerminates()");
        int n2 = 1;
        while (n2 <= 120) {
            try {
                Thread.sleep((long)1000L);
                if (this.getState() == Thread.State.TERMINATED) {
                    Log.d(this.he(), "waitUntilThreadTerminates() thread terminated");
                    return;
                }
            }
            catch (InterruptedException interruptedException) {
                // empty catch block
            }
            ++n2;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected void c(com.samsung.sensorframework.sda.b.a a2) {
        ArrayList<b> arrayList;
        ArrayList<b> arrayList2 = arrayList = this.KO;
        synchronized (arrayList2) {
            Iterator iterator = this.KO.iterator();
            while (iterator.hasNext()) {
                b b2 = (b)iterator.next();
                if (a2 != null) {
                    b2.a(a2);
                    continue;
                }
                Log.d(this.he(), "sensorData is null");
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean c(b b2) {
        ArrayList<b> arrayList;
        Log.d(this.he(), "registerSensorDataListener() listener: " + b2);
        ArrayList<b> arrayList2 = arrayList = this.KO;
        synchronized (arrayList2) {
            int n2 = 0;
            do {
                if (n2 >= this.KO.size()) {
                    this.KO.add((Object)b2);
                    this.startTask();
                    return true;
                }
                if (this.KO.get(n2) == b2) {
                    return false;
                }
                ++n2;
            } while (true);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public void d(b b2) {
        ArrayList<b> arrayList;
        ArrayList<b> arrayList2 = arrayList = this.KO;
        // MONITORENTER : arrayList2
        this.KO.remove((Object)b2);
        boolean bl = this.KO.isEmpty();
        boolean bl2 = false;
        if (bl) {
            bl2 = true;
        }
        // MONITOREXIT : arrayList2
        if (!bl2) return;
        this.hD();
    }

    public int getSensorType() {
        return this.KM.getSensorType();
    }

    protected String he() {
        try {
            String string = com.samsung.sensorframework.sda.d.c.ap(this.KM.getSensorType());
            String string2 = "SensorTask:" + string;
            return string2;
        }
        catch (SDAException sDAException) {
            sDAException.printStackTrace();
            return null;
        }
    }

    public boolean isStopped() {
        return this.state == 6124;
    }

    public void startTask() {
        if (this.state == 6122) {
            this.state = 6123;
            super.start();
        }
    }
}

