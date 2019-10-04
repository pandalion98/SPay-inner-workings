/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Collection
 *  java.util.HashMap
 *  java.util.Iterator
 *  java.util.List
 */
package com.samsung.sensorframework.sdi.e;

import android.content.Context;
import com.samsung.sensorframework.sdi.d.c;
import com.samsung.sensorframework.sdi.exception.SDIException;
import com.samsung.sensorframework.sdi.f.a;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
public class b {
    private static final Object Ho = new Object();
    private static b LF;
    private HashMap<String, Integer> LG;
    private int LH;
    private a Lw;

    private b(Context context) {
        if (context == null) {
            throw new SDIException(9000, "context can not be null");
        }
        this.Lw = a.bu(context);
        if (this.Lw == null) {
            throw new SDIException(9001, "persistentStorage can not be null.");
        }
        this.cu(null);
        com.samsung.android.spayfw.b.c.d("TriggerQuota", "instance created");
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static b bt(Context context) {
        if (LF == null) {
            Object object;
            Object object2 = object = Ho;
            synchronized (object2) {
                if (LF == null) {
                    LF = new b(context);
                }
            }
        }
        return LF;
    }

    private String cx(String string) {
        return this.if() + "_" + string;
    }

    private int ie() {
        Iterator iterator = this.LG.values().iterator();
        int n2 = 0;
        while (iterator.hasNext()) {
            int n3 = (Integer)iterator.next();
            if (n3 < 0) {
                n3 = 0;
            }
            n2 += 1 - n3;
        }
        com.samsung.android.spayfw.b.c.d("TriggerQuota", "getTotalUsedQuota() totalUsedQuota: " + n2);
        return n2;
    }

    private String if() {
        return "TriggerQuota_" + com.samsung.sensorframework.sdi.g.a.ih();
    }

    public void cu(String string) {
        b b2 = this;
        synchronized (b2) {
            com.samsung.android.spayfw.b.c.d("TriggerQuota", "resetQuota()");
            this.LH = com.samsung.sensorframework.sdi.b.a.d(string, 4);
            com.samsung.android.spayfw.b.c.d("TriggerQuota", "resetQuota() serverTriggerGlobalQuota: " + this.LH);
            String string2 = this.if();
            this.LG = this.Lw.cy(string2);
            if (this.LG == null) {
                this.LG = new HashMap();
            }
            com.samsung.android.spayfw.b.c.d("TriggerQuota", "resetQuota() totalUsedQuota: " + this.ie());
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void cv(String string) {
        b b2 = this;
        synchronized (b2) {
            if (c.cs(string)) {
                com.samsung.android.spayfw.b.c.d("TriggerQuota", "Ignoring PoI trigger as it is a data sync PoI: " + string);
            } else {
                int n2;
                String string2 = c.cr(string);
                String string3 = this.cx(string2);
                if (!this.LG.containsKey((Object)string3)) {
                    this.LG.put((Object)string3, (Object)1);
                }
                int n3 = (n2 = ((Integer)this.LG.get((Object)string3)).intValue()) > 0 ? n2 - 1 : 0;
                this.LG.put((Object)string3, (Object)n3);
                this.Lw.set(string3, n3);
                com.samsung.android.spayfw.b.c.d("TriggerQuota", "PoIId: " + string2 + " Remaining quota: " + n3);
            }
            return;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean cw(String string) {
        b b2 = this;
        synchronized (b2) {
            if (string == null) return false;
            String string2 = c.cr(string);
            if (string2 == null) return false;
            String string3 = this.cx(string2);
            if (!this.LG.containsKey((Object)string3)) return false;
            int n2 = (Integer)this.LG.get((Object)string3);
            if (n2 > 0) return false;
            return true;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean id() {
        b b2 = this;
        synchronized (b2) {
            com.samsung.android.spayfw.b.c.d("TriggerQuota", "isGlobalQuotaEmpty()");
            int n2 = this.ie();
            com.samsung.android.spayfw.b.c.d("TriggerQuota", "isGlobalQuotaEmpty() totalUsedQuota: " + n2);
            int n3 = this.LH;
            if (n2 < n3) return false;
            return true;
        }
    }

    public void n(List<String> list) {
        b b2 = this;
        synchronized (b2) {
            Iterator iterator = list.iterator();
            while (iterator.hasNext()) {
                this.cv((String)iterator.next());
            }
            return;
        }
    }
}

