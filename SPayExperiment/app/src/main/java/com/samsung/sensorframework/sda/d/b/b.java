/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.Intent
 *  android.content.IntentFilter
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 */
package com.samsung.sensorframework.sda.d.b;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.samsung.sensorframework.sda.a.c;
import com.samsung.sensorframework.sda.d.b.a;

public class b
extends a {
    private static b Ko;
    private static final Object lock;

    static {
        lock = new Object();
    }

    private b(Context context) {
        super(context);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static b bb(Context context) {
        if (Ko == null) {
            Object object;
            Object object2 = object = lock;
            synchronized (object2) {
                if (Ko == null) {
                    Ko = new b(context);
                }
            }
        }
        return Ko;
    }

    @Override
    protected void a(Context context, Intent intent) {
        this.a(((com.samsung.sensorframework.sda.c.b.a)this.hi()).a(System.currentTimeMillis(), this.Id.gS(), intent));
    }

    @Override
    public void gY() {
        super.gY();
        Ko = null;
    }

    @Override
    public int getSensorType() {
        return 5002;
    }

    @Override
    protected IntentFilter[] hC() {
        String string = "BATTERY_INTENT_FILTER_ALL";
        if (this.Id.bR("BATTERY_INTENT_FILTERS")) {
            string = (String)this.Id.getParameter("BATTERY_INTENT_FILTERS");
        }
        if (string != null && string.equals((Object)"BATTERY_INTENT_FILTER_LOW_OK")) {
            IntentFilter[] arrintentFilter = new IntentFilter[]{new IntentFilter("android.intent.action.BATTERY_LOW"), new IntentFilter("android.intent.action.BATTERY_OKAY")};
            return arrintentFilter;
        }
        IntentFilter[] arrintentFilter = new IntentFilter[]{new IntentFilter("android.intent.action.BATTERY_CHANGED"), new IntentFilter("android.intent.action.BATTERY_LOW"), new IntentFilter("android.intent.action.BATTERY_OKAY"), new IntentFilter("android.intent.action.ACTION_POWER_CONNECTED"), new IntentFilter("android.intent.action.ACTION_POWER_DISCONNECTED")};
        return arrintentFilter;
    }

    @Override
    protected boolean hc() {
        return true;
    }

    @Override
    protected void hd() {
    }

    @Override
    public String he() {
        return "BatterySensor";
    }
}

