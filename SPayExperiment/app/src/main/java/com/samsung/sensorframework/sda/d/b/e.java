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
import com.samsung.sensorframework.sda.c.b.d;
import com.samsung.sensorframework.sda.d.b.a;

public class e
extends a {
    private static e Ku;
    private static final Object lock;

    static {
        lock = new Object();
    }

    private e(Context context) {
        super(context);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static e be(Context context) {
        if (Ku == null) {
            Object object;
            Object object2 = object = lock;
            synchronized (object2) {
                if (Ku == null) {
                    Ku = new e(context);
                }
            }
        }
        return Ku;
    }

    @Override
    protected void a(Context context, Intent intent) {
        this.a(((d)super.hi()).b(System.currentTimeMillis(), this.Id.gS(), intent));
    }

    @Override
    public void gY() {
        super.gY();
        Ku = null;
    }

    @Override
    public int getSensorType() {
        return 5022;
    }

    @Override
    protected IntentFilter[] hC() {
        IntentFilter intentFilter = new IntentFilter();
        IntentFilter[] arrintentFilter = new IntentFilter[]{intentFilter};
        intentFilter.addAction("com.android.music.metachanged");
        intentFilter.addAction("com.android.music.playstatechanged");
        intentFilter.addAction("com.android.music.playbackcomplete");
        intentFilter.addAction("com.android.music.queuechanged");
        intentFilter.addAction("fm.last.android.metachanged");
        intentFilter.addAction("com.sec.android.app.music.metachanged");
        intentFilter.addAction("com.nullsoft.winamp.metachanged");
        intentFilter.addAction("com.amazon.mp3.metachanged");
        intentFilter.addAction("com.miui.player.metachanged");
        intentFilter.addAction("com.real.IMP.metachanged");
        intentFilter.addAction("com.sonyericsson.music.metachanged");
        intentFilter.addAction("com.rdio.android.metachanged");
        intentFilter.addAction("com.samsung.sec.android.MusicPlayer.metachanged");
        intentFilter.addAction("com.andrew.apollo.metachanged");
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
        return "MusicPlayerStateSensor";
    }
}

