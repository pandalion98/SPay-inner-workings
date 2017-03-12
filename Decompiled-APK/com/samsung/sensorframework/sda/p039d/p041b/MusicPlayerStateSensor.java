package com.samsung.sensorframework.sda.p039d.p041b;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.samsung.sensorframework.sda.p036c.p038b.MusicPlayerStateProcessor;

/* renamed from: com.samsung.sensorframework.sda.d.b.e */
public class MusicPlayerStateSensor extends AbstractPushSensor {
    private static MusicPlayerStateSensor Ku;
    private static final Object lock;

    static {
        lock = new Object();
    }

    public static MusicPlayerStateSensor be(Context context) {
        if (Ku == null) {
            synchronized (lock) {
                if (Ku == null) {
                    Ku = new MusicPlayerStateSensor(context);
                }
            }
        }
        return Ku;
    }

    private MusicPlayerStateSensor(Context context) {
        super(context);
    }

    public void gY() {
        super.gY();
        Ku = null;
    }

    public String he() {
        return "MusicPlayerStateSensor";
    }

    public int getSensorType() {
        return 5022;
    }

    protected void m1621a(Context context, Intent intent) {
        m1613a(((MusicPlayerStateProcessor) super.hi()).m1565b(System.currentTimeMillis(), this.Id.gS(), intent));
    }

    protected IntentFilter[] hC() {
        IntentFilter[] intentFilterArr = new IntentFilter[1];
        IntentFilter intentFilter = new IntentFilter();
        intentFilterArr[0] = intentFilter;
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
        return intentFilterArr;
    }

    protected boolean hc() {
        return true;
    }

    protected void hd() {
    }
}
