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
import com.samsung.sensorframework.sda.c.b.e;
import com.samsung.sensorframework.sda.d.b.a;

public class f
extends a {
    private static f Kv;
    private static final Object lock;

    static {
        lock = new Object();
    }

    private f(Context context) {
        super(context);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static f bf(Context context) {
        if (Kv == null) {
            Object object;
            Object object2 = object = lock;
            synchronized (object2) {
                if (Kv == null) {
                    Kv = new f(context);
                }
            }
        }
        return Kv;
    }

    @Override
    protected void a(Context context, Intent intent) {
        this.a(((e)super.hi()).c(System.currentTimeMillis(), this.Id.gS(), intent));
    }

    @Override
    public void gY() {
        super.gY();
        Kv = null;
    }

    @Override
    public int getSensorType() {
        return 5017;
    }

    @Override
    protected IntentFilter[] hC() {
        IntentFilter[] arrintentFilter = new IntentFilter[]{new IntentFilter("android.intent.action.PACKAGE_ADDED"), new IntentFilter("android.intent.action.PACKAGE_CHANGED"), new IntentFilter("android.intent.action.PACKAGE_FULLY_REMOVED"), new IntentFilter("android.intent.action.PACKAGE_REMOVED"), new IntentFilter("android.intent.action.PACKAGE_REPLACED")};
        for (int i2 = 0; i2 < arrintentFilter.length; ++i2) {
            arrintentFilter[i2].addDataScheme("package");
        }
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
        return "PackageSensor";
    }
}

