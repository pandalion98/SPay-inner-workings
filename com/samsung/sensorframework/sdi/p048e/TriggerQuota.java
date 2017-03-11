package com.samsung.sensorframework.sdi.p048e;

import android.content.Context;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spaytui.SPayTUIException;
import com.samsung.sensorframework.sdi.exception.SDIException;
import com.samsung.sensorframework.sdi.p045b.ConfigUtils;
import com.samsung.sensorframework.sdi.p047d.PoIUtils;
import com.samsung.sensorframework.sdi.p049f.PersistentStorage;
import com.samsung.sensorframework.sdi.p050g.SFUtils;
import java.util.HashMap;
import java.util.List;

/* renamed from: com.samsung.sensorframework.sdi.e.b */
public class TriggerQuota {
    private static final Object Ho;
    private static TriggerQuota LF;
    private HashMap<String, Integer> LG;
    private int LH;
    private PersistentStorage Lw;

    static {
        Ho = new Object();
    }

    public static TriggerQuota bt(Context context) {
        if (LF == null) {
            synchronized (Ho) {
                if (LF == null) {
                    LF = new TriggerQuota(context);
                }
            }
        }
        return LF;
    }

    private TriggerQuota(Context context) {
        if (context == null) {
            throw new SDIException(9000, "context can not be null");
        }
        this.Lw = PersistentStorage.bu(context);
        if (this.Lw == null) {
            throw new SDIException(SPayTUIException.ERR_UNKNOWN, "persistentStorage can not be null.");
        }
        cu(null);
        Log.m285d("TriggerQuota", "instance created");
    }

    public synchronized void m1704n(List<String> list) {
        for (String cv : list) {
            cv(cv);
        }
    }

    public synchronized void cv(String str) {
        if (PoIUtils.cs(str)) {
            Log.m285d("TriggerQuota", "Ignoring PoI trigger as it is a data sync PoI: " + str);
        } else {
            String cr = PoIUtils.cr(str);
            String cx = cx(cr);
            if (!this.LG.containsKey(cx)) {
                this.LG.put(cx, Integer.valueOf(1));
            }
            int intValue = ((Integer) this.LG.get(cx)).intValue();
            if (intValue > 0) {
                intValue--;
            } else {
                intValue = 0;
            }
            this.LG.put(cx, Integer.valueOf(intValue));
            this.Lw.set(cx, intValue);
            Log.m285d("TriggerQuota", "PoIId: " + cr + " Remaining quota: " + intValue);
        }
    }

    public synchronized boolean cw(String str) {
        boolean z;
        if (str != null) {
            String cr = PoIUtils.cr(str);
            if (cr != null) {
                cr = cx(cr);
                if (this.LG.containsKey(cr) && ((Integer) this.LG.get(cr)).intValue() <= 0) {
                    z = true;
                }
            }
        }
        z = false;
        return z;
    }

    public synchronized boolean id() {
        boolean z;
        Log.m285d("TriggerQuota", "isGlobalQuotaEmpty()");
        int ie = ie();
        Log.m285d("TriggerQuota", "isGlobalQuotaEmpty() totalUsedQuota: " + ie);
        if (ie >= this.LH) {
            z = true;
        } else {
            z = false;
        }
        return z;
    }

    private int ie() {
        int i = 0;
        for (Integer intValue : this.LG.values()) {
            int intValue2 = intValue.intValue();
            if (intValue2 < 0) {
                intValue2 = 0;
            }
            i = (1 - intValue2) + i;
        }
        Log.m285d("TriggerQuota", "getTotalUsedQuota() totalUsedQuota: " + i);
        return i;
    }

    public synchronized void cu(String str) {
        Log.m285d("TriggerQuota", "resetQuota()");
        this.LH = ConfigUtils.m1661d(str, 4);
        Log.m285d("TriggerQuota", "resetQuota() serverTriggerGlobalQuota: " + this.LH);
        this.LG = this.Lw.cy(m1703if());
        if (this.LG == null) {
            this.LG = new HashMap();
        }
        Log.m285d("TriggerQuota", "resetQuota() totalUsedQuota: " + ie());
    }

    private String cx(String str) {
        return m1703if() + "_" + str;
    }

    private String m1703if() {
        return "TriggerQuota_" + SFUtils.ih();
    }
}
