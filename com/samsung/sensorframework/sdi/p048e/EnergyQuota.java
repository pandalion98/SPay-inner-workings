package com.samsung.sensorframework.sdi.p048e;

import android.content.Context;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spaytui.SPayTUIException;
import com.samsung.sensorframework.sdi.exception.SDIException;
import com.samsung.sensorframework.sdi.p045b.ConfigUtils;
import com.samsung.sensorframework.sdi.p049f.PersistentStorage;
import com.samsung.sensorframework.sdi.p050g.SFUtils;

/* renamed from: com.samsung.sensorframework.sdi.e.a */
public class EnergyQuota {
    private static final Object Ho;
    private static EnergyQuota LD;
    private int LE;
    private PersistentStorage Lw;

    static {
        Ho = new Object();
    }

    public static EnergyQuota bs(Context context) {
        if (LD == null) {
            synchronized (Ho) {
                if (LD == null) {
                    LD = new EnergyQuota(context);
                }
            }
        }
        return LD;
    }

    private EnergyQuota(Context context) {
        if (context == null) {
            throw new SDIException(9000, "context can not be null");
        }
        this.Lw = PersistentStorage.bu(context);
        if (this.Lw == null) {
            throw new SDIException(SPayTUIException.ERR_UNKNOWN, "persistentStorage can not be null.");
        }
        cu(null);
        Log.m285d("EnergyQuota", "instance created.");
    }

    public synchronized void ia() {
        if (this.LE > 0) {
            this.LE--;
        } else {
            this.LE = 0;
        }
        this.Lw.set(ic(), this.LE);
        Log.m285d("EnergyQuota", "Remaining quota: " + this.LE);
    }

    public synchronized boolean ib() {
        boolean z;
        if (this.LE <= 0) {
            Log.m285d("EnergyQuota", "isQuotaEmpty() returning true");
            z = true;
        } else {
            Log.m285d("EnergyQuota", "isQuotaEmpty() returning false");
            z = false;
        }
        return z;
    }

    public synchronized void cu(String str) {
        Log.m285d("EnergyQuota", "resetQuota()");
        int e = ConfigUtils.m1662e(str, 50);
        String ic = ic();
        if (this.Lw.contains(ic)) {
            this.LE = this.Lw.get(ic, e);
        } else {
            this.LE = e;
        }
        Log.m285d("EnergyQuota", "resetQuota() remainingQuota: " + this.LE);
    }

    private String ic() {
        return "EnergyQuota_" + SFUtils.ih();
    }
}
