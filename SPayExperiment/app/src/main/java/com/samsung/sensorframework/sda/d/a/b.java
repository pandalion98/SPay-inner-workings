/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  java.lang.Integer
 *  java.lang.InterruptedException
 *  java.lang.Long
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 */
package com.samsung.sensorframework.sda.d.a;

import android.content.Context;
import com.samsung.android.spayfw.b.c;
import com.samsung.sensorframework.sda.SDAException;
import com.samsung.sensorframework.sda.d.a;
import com.samsung.sensorframework.sda.d.a.m;

public abstract class b
extends a
implements m {
    protected com.samsung.sensorframework.sda.b.a Ib;
    protected long Jn;

    public b(Context context) {
        super(context);
    }

    public b(Context context, Integer n2) {
        super(context, n2);
    }

    protected abstract com.samsung.sensorframework.sda.b.a hl();

    protected abstract void hm();

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public com.samsung.sensorframework.sda.b.a hn() {
        block15 : {
            block13 : {
                block14 : {
                    long l2;
                    this.ha();
                    if (this.Ji) {
                        throw new SDAException(8003, "sensor busy");
                    }
                    this.Ji = true;
                    this.Jn = System.currentTimeMillis();
                    if (!this.hc()) {
                        c.d(this.he(), "Sensing not started.");
                        this.Ji = false;
                        return null;
                    }
                    c.d(this.he(), "Sensing started.");
                    try {
                        Object object;
                        if (this.Id.bR("NUMBER_OF_SENSE_CYCLES")) break block13;
                        if (!this.Id.bR("SENSE_WINDOW_LENGTH_MILLIS")) break block14;
                        l2 = (Long)this.Id.getParameter("SENSE_WINDOW_LENGTH_MILLIS");
                        Object object2 = object = this.Jj;
                        synchronized (object2) {
                        }
                    }
                    catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                        break block15;
                    }
                    {
                        this.Jj.wait(l2);
                        break block15;
                    }
                }
                throw new SDAException(8005, "Invalid Sensor Config, window size or no. of cycles should in in the config");
            }
            while (this.Ji) {
                Object object;
                Object object3 = object = this.Jj;
                synchronized (object3) {
                    this.Jj.wait(500L);
                }
            }
        }
        this.hd();
        this.Ji = false;
        c.d(this.he(), "Sensing stopped.");
        this.hm();
        com.samsung.sensorframework.sda.b.a a2 = this.hl();
        if (a2 != null) {
            a2.b(this.Ib);
        }
        this.Ib = a2;
        return a2;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected void ho() {
        Object object;
        Object object2 = object = this.Jj;
        synchronized (object2) {
            this.Ji = false;
            this.Jj.notify();
            return;
        }
    }
}

