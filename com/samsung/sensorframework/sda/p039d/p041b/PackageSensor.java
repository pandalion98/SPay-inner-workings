package com.samsung.sensorframework.sda.p039d.p041b;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.samsung.sensorframework.sda.p036c.p038b.PackageProcessor;

/* renamed from: com.samsung.sensorframework.sda.d.b.f */
public class PackageSensor extends AbstractPushSensor {
    private static PackageSensor Kv;
    private static final Object lock;

    static {
        lock = new Object();
    }

    public static PackageSensor bf(Context context) {
        if (Kv == null) {
            synchronized (lock) {
                if (Kv == null) {
                    Kv = new PackageSensor(context);
                }
            }
        }
        return Kv;
    }

    private PackageSensor(Context context) {
        super(context);
    }

    public void gY() {
        super.gY();
        Kv = null;
    }

    public String he() {
        return "PackageSensor";
    }

    public int getSensorType() {
        return 5017;
    }

    protected void m1622a(Context context, Intent intent) {
        m1613a(((PackageProcessor) super.hi()).m1566c(System.currentTimeMillis(), this.Id.gS(), intent));
    }

    protected IntentFilter[] hC() {
        int i = 0;
        IntentFilter[] intentFilterArr = new IntentFilter[]{new IntentFilter("android.intent.action.PACKAGE_ADDED"), new IntentFilter("android.intent.action.PACKAGE_CHANGED"), new IntentFilter("android.intent.action.PACKAGE_FULLY_REMOVED"), new IntentFilter("android.intent.action.PACKAGE_REMOVED"), new IntentFilter("android.intent.action.PACKAGE_REPLACED")};
        while (i < intentFilterArr.length) {
            intentFilterArr[i].addDataScheme("package");
            i++;
        }
        return intentFilterArr;
    }

    protected boolean hc() {
        return true;
    }

    protected void hd() {
    }
}
