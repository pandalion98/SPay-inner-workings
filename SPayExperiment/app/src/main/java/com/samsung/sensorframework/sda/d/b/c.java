/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.ClipData
 *  android.content.ClipboardManager
 *  android.content.ClipboardManager$OnPrimaryClipChangedListener
 *  android.content.Context
 *  android.content.Intent
 *  android.content.IntentFilter
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 */
package com.samsung.sensorframework.sda.d.b;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.samsung.sensorframework.sda.c.b.b;

public class c
extends com.samsung.sensorframework.sda.d.b.a {
    private static c Kp;
    private static final Object lock;
    private a Kq;
    private final ClipboardManager Kr;

    static {
        lock = new Object();
    }

    private c(Context context) {
        super(context);
        this.Kr = (ClipboardManager)this.HR.getSystemService("clipboard");
        this.Kq = new a();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static c bc(Context context) {
        if (Kp == null) {
            Object object;
            Object object2 = object = lock;
            synchronized (object2) {
                if (Kp == null) {
                    Kp = new c(context);
                }
            }
        }
        return Kp;
    }

    protected void a(ClipData clipData) {
        this.a(((b)super.hi()).a(System.currentTimeMillis(), this.Id.gS(), clipData));
    }

    @Override
    protected void a(Context context, Intent intent) {
    }

    @Override
    public void gY() {
        super.gY();
        Kp = null;
    }

    @Override
    public int getSensorType() {
        return 5024;
    }

    @Override
    protected IntentFilter[] hC() {
        return null;
    }

    @Override
    protected boolean hc() {
        this.Kr.addPrimaryClipChangedListener((ClipboardManager.OnPrimaryClipChangedListener)this.Kq);
        return true;
    }

    @Override
    protected void hd() {
        this.Kr.removePrimaryClipChangedListener((ClipboardManager.OnPrimaryClipChangedListener)this.Kq);
    }

    @Override
    public String he() {
        return "ClipboardSensor";
    }

    class a
    implements ClipboardManager.OnPrimaryClipChangedListener {
        a() {
        }

        public void onPrimaryClipChanged() {
            c.this.a(c.this.Kr.getPrimaryClip());
        }
    }

}

