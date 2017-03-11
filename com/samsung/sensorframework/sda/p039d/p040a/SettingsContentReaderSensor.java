package com.samsung.sensorframework.sda.p039d.p040a;

import android.content.Context;
import android.os.Build.VERSION;
import android.provider.Settings.Global;
import android.provider.Settings.System;

/* renamed from: com.samsung.sensorframework.sda.d.a.o */
public class SettingsContentReaderSensor extends AbstractContentReaderSensor {
    private static SettingsContentReaderSensor Kd;

    public static SettingsContentReaderSensor aY(Context context) {
        if (Kd == null) {
            synchronized (lock) {
                if (Kd == null) {
                    Kd = new SettingsContentReaderSensor(context);
                }
            }
        }
        return Kd;
    }

    private SettingsContentReaderSensor(Context context) {
        super(context);
    }

    public void gY() {
        super.gY();
        Kd = null;
    }

    public int getSensorType() {
        return 5025;
    }

    protected String[] hj() {
        if (VERSION.SDK_INT >= 17) {
            return new String[]{System.CONTENT_URI.toString(), Global.CONTENT_URI.toString()};
        }
        return new String[]{System.CONTENT_URI.toString()};
    }

    protected String[] hk() {
        return new String[]{"name", "value"};
    }

    protected String he() {
        return "SettingsContentReaderSensor";
    }
}
