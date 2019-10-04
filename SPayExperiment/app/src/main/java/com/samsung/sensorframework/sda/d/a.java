/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Handler
 *  android.os.HandlerThread
 *  android.os.Looper
 *  java.lang.Boolean
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Arrays
 */
package com.samsung.sensorframework.sda.d;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import com.samsung.sensorframework.sda.SDAException;
import com.samsung.sensorframework.sda.d.b;
import com.samsung.sensorframework.sda.d.c;
import java.util.Arrays;

public abstract class a
implements b {
    protected final Context HR;
    protected final com.samsung.sensorframework.sda.a.c Id;
    protected final int Im;
    protected boolean Ji;
    protected final Object Jj;
    protected Handler Jk;
    protected HandlerThread Jl;

    public a(Context context) {
        this(context, null);
    }

    /*
     * Enabled aggressive block sorting
     */
    public a(Context context, Integer n2) {
        this.HR = context;
        this.Jj = new Object();
        if (n2 != null) {
            this.Id = c.ao(n2);
            this.Im = n2;
        } else {
            this.Id = c.ao(this.getSensorType());
            this.Im = this.getSensorType();
        }
        this.Jl = new HandlerThread("SensorHandlerThread:" + this.Im);
        this.Jl.start();
        this.Jk = new Handler(this.Jl.getLooper());
    }

    private boolean hg() {
        if (this.Id.bR("RAW_DATA")) {
            return (Boolean)this.Id.getParameter("RAW_DATA");
        }
        return true;
    }

    private boolean hh() {
        if (this.Id.bR("EXTRACT_FEATURES")) {
            return (Boolean)this.Id.getParameter("EXTRACT_FEATURES");
        }
        return false;
    }

    @Override
    public void c(String string, Object object) {
        if (!this.Id.bR(string)) {
            throw new SDAException(8005, "Invalid sensor config, key: " + string + " value: " + object);
        }
        this.Id.setParameter(string, object);
    }

    protected boolean ci(String string) {
        return this.HR.checkCallingOrSelfPermission(string) == 0;
    }

    @Override
    public Object cj(String string) {
        if (this.Id.bR(string)) {
            return this.Id.getParameter(string);
        }
        throw new SDAException(8005, "Invalid sensor config, key: " + string);
    }

    @Override
    public void gY() {
        com.samsung.android.spayfw.b.c.d(this.he(), "destroySensor()");
        this.Jk = null;
        if (this.Jl != null && this.Jl.isAlive()) {
            this.Jl.quit();
        }
        this.Jl = null;
    }

    protected Handler gZ() {
        return this.Jk;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    protected boolean h(String[] arrstring) {
        if (arrstring == null) return true;
        if (arrstring.length == 0) {
            return true;
        }
        int n2 = arrstring.length;
        int n3 = 0;
        while (n3 < n2) {
            boolean bl = this.ci(arrstring[n3]);
            boolean bl2 = false;
            if (!bl) return bl2;
            ++n3;
        }
        return true;
    }

    protected void ha() {
        if (!this.h(this.hb())) {
            String string = "Null";
            if (this.hb() != null) {
                string = Arrays.toString((Object[])this.hb());
            }
            throw new SDAException(8000, "Sensor: " + c.ap(this.getSensorType()) + "; Required permissions not granted: " + string);
        }
    }

    protected String[] hb() {
        return null;
    }

    protected abstract boolean hc();

    protected abstract void hd();

    protected abstract String he();

    @Override
    public boolean hf() {
        return this.Ji;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected com.samsung.sensorframework.sda.c.a hi() {
        try {
            boolean bl = this.hg();
            boolean bl2 = this.hh();
            if (bl) return com.samsung.sensorframework.sda.c.a.a(this.HR, this.getSensorType(), bl, bl2);
            if (bl2) return com.samsung.sensorframework.sda.c.a.a(this.HR, this.getSensorType(), bl, bl2);
            throw new SDAException(8007, "No data requested from processor");
        }
        catch (SDAException sDAException) {
            sDAException.printStackTrace();
            return null;
        }
    }
}

