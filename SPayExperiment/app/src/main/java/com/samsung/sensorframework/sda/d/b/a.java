/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.BroadcastReceiver
 *  android.content.Context
 *  android.content.Intent
 *  android.content.IntentFilter
 *  android.os.Handler
 *  java.lang.IllegalArgumentException
 *  java.lang.String
 */
package com.samsung.sensorframework.sda.d.b;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import com.samsung.android.spayfw.b.c;
import com.samsung.sensorframework.sda.SDAException;
import com.samsung.sensorframework.sda.b;
import com.samsung.sensorframework.sda.d.b.j;

public abstract class a
extends com.samsung.sensorframework.sda.d.a
implements j {
    protected b Kl;
    protected BroadcastReceiver Km = new BroadcastReceiver(){

        public void onReceive(Context context, Intent intent) {
            if (a.this.Ji) {
                a.this.a(context, intent);
                return;
            }
            c.d(a.this.he(), "BroadcastReceiver.onReceive() called while not sensing.");
        }
    };

    public a(Context context) {
        super(context);
    }

    protected abstract void a(Context var1, Intent var2);

    protected void a(com.samsung.sensorframework.sda.b.a a2) {
        if (this.Kl != null) {
            this.Kl.a(a2);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void a(b b2) {
        this.ha();
        if (this.Ji) {
            c.d(this.he(), "sensing already sensing");
            throw new SDAException(8003, "sensor already sensing");
        }
        this.Kl = b2;
        this.hc();
        IntentFilter[] arrintentFilter = this.hC();
        if (arrintentFilter == null || arrintentFilter.length <= 0) {
            c.d(this.he(), "getIntentFilters() returned null");
        } else {
            com.samsung.sensorframework.sda.a.b b3 = com.samsung.sensorframework.sda.a.b.gO();
            String string = null;
            if (b3 != null) {
                string = com.samsung.sensorframework.sda.a.b.gO().gR();
            }
            c.d(this.he(), " intentBroadcasterPermission: " + string);
            for (IntentFilter intentFilter : arrintentFilter) {
                if (intentFilter != null && intentFilter.countActions() > 0) {
                    for (int i2 = 0; i2 < intentFilter.countActions(); ++i2) {
                        c.d(this.he(), "Registering receiver for: " + intentFilter.getAction(i2));
                    }
                    this.HR.registerReceiver(this.Km, intentFilter, string, this.gZ());
                    continue;
                }
                c.d(this.he(), "Intent filter is null or countActions() is zero");
            }
        }
        this.Ji = true;
        c.d(this.he(), "Sensing started.");
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void b(b b2) {
        this.ha();
        if (!this.Ji) {
            c.d(this.he(), "sensor not sensing");
            throw new SDAException(8004, "sensor not sensing");
        }
        this.hd();
        try {
            IntentFilter[] arrintentFilter = this.hC();
            if (arrintentFilter != null && arrintentFilter.length > 0) {
                this.HR.unregisterReceiver(this.Km);
            }
        }
        catch (IllegalArgumentException illegalArgumentException) {}
        this.Ji = false;
        c.d(this.he(), "Sensing stopped.");
    }

    protected abstract IntentFilter[] hC();

}

