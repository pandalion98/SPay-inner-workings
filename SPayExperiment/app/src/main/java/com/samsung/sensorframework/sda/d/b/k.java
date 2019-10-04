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
import com.samsung.sensorframework.sda.b.b.h;
import com.samsung.sensorframework.sda.c.b.i;
import com.samsung.sensorframework.sda.d.b.a;

public class k
extends a {
    private static k KH;
    private static Object lock;

    static {
        lock = new Object();
    }

    private k(Context context) {
        super(context);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static k bj(Context context) {
        if (KH == null) {
            Object object;
            Object object2 = object = lock;
            synchronized (object2) {
                if (KH == null) {
                    KH = new k(context);
                }
            }
        }
        return KH;
    }

    @Override
    protected void a(Context context, Intent intent) {
        this.a(((i)super.hi()).d(System.currentTimeMillis(), this.Id.gS(), intent));
    }

    @Override
    public void gY() {
        super.gY();
        KH = null;
    }

    @Override
    public int getSensorType() {
        return 5008;
    }

    @Override
    protected IntentFilter[] hC() {
        IntentFilter[] arrintentFilter = new IntentFilter[]{new IntentFilter("android.intent.action.SCREEN_ON"), new IntentFilter("android.intent.action.SCREEN_OFF")};
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
        return "ScreenSensor";
    }
}

