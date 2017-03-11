package com.samsung.sensorframework.sda.p039d.p041b;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.samsung.sensorframework.sda.p036c.p038b.BatteryProcessor;

/* renamed from: com.samsung.sensorframework.sda.d.b.b */
public class BatterySensor extends AbstractPushSensor {
    private static BatterySensor Ko;
    private static final Object lock;

    static {
        lock = new Object();
    }

    public static BatterySensor bb(Context context) {
        if (Ko == null) {
            synchronized (lock) {
                if (Ko == null) {
                    Ko = new BatterySensor(context);
                }
            }
        }
        return Ko;
    }

    private BatterySensor(Context context) {
        super(context);
    }

    public void gY() {
        super.gY();
        Ko = null;
    }

    public String he() {
        return "BatterySensor";
    }

    public int getSensorType() {
        return 5002;
    }

    protected void m1616a(Context context, Intent intent) {
        m1613a(((BatteryProcessor) hi()).m1561a(System.currentTimeMillis(), this.Id.gS(), intent));
    }

    protected IntentFilter[] hC() {
        String str = "BATTERY_INTENT_FILTER_ALL";
        if (this.Id.bR("BATTERY_INTENT_FILTERS")) {
            str = (String) this.Id.getParameter("BATTERY_INTENT_FILTERS");
        }
        if (str == null || !str.equals("BATTERY_INTENT_FILTER_LOW_OK")) {
            return new IntentFilter[]{new IntentFilter("android.intent.action.BATTERY_CHANGED"), new IntentFilter("android.intent.action.BATTERY_LOW"), new IntentFilter("android.intent.action.BATTERY_OKAY"), new IntentFilter("android.intent.action.ACTION_POWER_CONNECTED"), new IntentFilter("android.intent.action.ACTION_POWER_DISCONNECTED")};
        }
        return new IntentFilter[]{new IntentFilter("android.intent.action.BATTERY_LOW"), new IntentFilter("android.intent.action.BATTERY_OKAY")};
    }

    protected boolean hc() {
        return true;
    }

    protected void hd() {
    }
}
