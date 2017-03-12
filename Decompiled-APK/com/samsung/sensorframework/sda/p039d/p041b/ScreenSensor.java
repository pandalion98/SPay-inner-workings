package com.samsung.sensorframework.sda.p039d.p041b;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.samsung.sensorframework.sda.p036c.p038b.ScreenProcessor;

/* renamed from: com.samsung.sensorframework.sda.d.b.k */
public class ScreenSensor extends AbstractPushSensor {
    private static ScreenSensor KH;
    private static Object lock;

    static {
        lock = new Object();
    }

    public static ScreenSensor bj(Context context) {
        if (KH == null) {
            synchronized (lock) {
                if (KH == null) {
                    KH = new ScreenSensor(context);
                }
            }
        }
        return KH;
    }

    public void gY() {
        super.gY();
        KH = null;
    }

    private ScreenSensor(Context context) {
        super(context);
    }

    public String he() {
        return "ScreenSensor";
    }

    public int getSensorType() {
        return 5008;
    }

    protected void m1631a(Context context, Intent intent) {
        m1613a(((ScreenProcessor) super.hi()).m1570d(System.currentTimeMillis(), this.Id.gS(), intent));
    }

    protected IntentFilter[] hC() {
        return new IntentFilter[]{new IntentFilter("android.intent.action.SCREEN_ON"), new IntentFilter("android.intent.action.SCREEN_OFF")};
    }

    protected boolean hc() {
        return true;
    }

    protected void hd() {
    }
}
