/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 */
package com.samsung.contextservice.server;

import android.content.Context;
import com.samsung.android.spayfw.remoteservice.Request;
import com.samsung.contextservice.b.b;
import com.samsung.contextservice.server.c;
import com.samsung.contextservice.server.h;
import com.samsung.contextservice.server.i;
import com.samsung.contextservice.server.j;

public class a
extends h {
    public a(Context context, String string, long l2, int n2) {
        super(context, string, 2, "cachegap", "cachecap");
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    protected void execute() {
        a a2 = this;
        synchronized (a2) {
            boolean bl = this.e(System.currentTimeMillis(), this.gu());
            if (bl) {
                if (this.gy()) {
                    b.d("RemoteConnPolicy", "cache polling reach maximum, cap=" + this.gu());
                } else {
                    try {
                        while (!this.isEmpty()) {
                            i i2 = this.gx();
                            if (i2 == null) continue;
                            c c2 = (c)i2.getRequest();
                            Request.a a3 = i2.gz();
                            if (c2 == null || a3 == null) continue;
                            c2.a(a3);
                            b.d("RemoteConnPolicy", "Cache queue items number:" + this.size() + ", lastPing=" + this.gw());
                            this.clear();
                            break;
                        }
                    }
                    catch (Exception exception) {
                        b.a("RemoteConnPolicy", "Cache execute,", exception);
                    }
                }
            }
            return;
        }
    }

    @Override
    protected long gt() {
        try {
            long l2 = j.gB().getMinContextCallDelay();
            return l2;
        }
        catch (Exception exception) {
            b.c("RemoteConnPolicy", "cannot get min delay", exception);
            return 3600000L;
        }
    }

    @Override
    protected int gu() {
        try {
            int n2 = j.gB().getContextCacheQuotaPerDay();
            return n2;
        }
        catch (Exception exception) {
            b.c("RemoteConnPolicy", "cannot get cap", exception);
            return 2;
        }
    }
}

