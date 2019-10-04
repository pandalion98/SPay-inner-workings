/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.sensorframework.sdi.e;

import android.content.Context;
import com.samsung.android.spayfw.b.c;
import com.samsung.sensorframework.sdi.exception.SDIException;

public class a {
    private static final Object Ho = new Object();
    private static a LD;
    private int LE;
    private com.samsung.sensorframework.sdi.f.a Lw;

    private a(Context context) {
        if (context == null) {
            throw new SDIException(9000, "context can not be null");
        }
        this.Lw = com.samsung.sensorframework.sdi.f.a.bu(context);
        if (this.Lw == null) {
            throw new SDIException(9001, "persistentStorage can not be null.");
        }
        this.cu(null);
        c.d("EnergyQuota", "instance created.");
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static a bs(Context context) {
        if (LD == null) {
            Object object;
            Object object2 = object = Ho;
            synchronized (object2) {
                if (LD == null) {
                    LD = new a(context);
                }
            }
        }
        return LD;
    }

    private String ic() {
        return "EnergyQuota_" + com.samsung.sensorframework.sdi.g.a.ih();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void cu(String string) {
        a a2 = this;
        synchronized (a2) {
            c.d("EnergyQuota", "resetQuota()");
            int n2 = com.samsung.sensorframework.sdi.b.a.e(string, 50);
            String string2 = this.ic();
            this.LE = this.Lw.contains(string2) ? this.Lw.get(string2, n2) : n2;
            c.d("EnergyQuota", "resetQuota() remainingQuota: " + this.LE);
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void ia() {
        a a2 = this;
        synchronized (a2) {
            this.LE = this.LE > 0 ? -1 + this.LE : 0;
            this.Lw.set(this.ic(), this.LE);
            c.d("EnergyQuota", "Remaining quota: " + this.LE);
            return;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean ib() {
        a a2 = this;
        synchronized (a2) {
            block4 : {
                if (this.LE > 0) break block4;
                c.d("EnergyQuota", "isQuotaEmpty() returning true");
                return true;
            }
            c.d("EnergyQuota", "isQuotaEmpty() returning false");
            return false;
        }
    }
}

