/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  java.lang.Exception
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.util.ArrayList
 */
package com.samsung.contextservice.server;

import android.content.Context;
import com.samsung.android.spayfw.remoteservice.Request;
import com.samsung.contextservice.b.b;
import com.samsung.contextservice.server.g;
import com.samsung.contextservice.server.h;
import com.samsung.contextservice.server.i;
import java.util.ArrayList;

class f
extends h<g> {
    private final ArrayList<Integer> GP = new ArrayList();
    private final int GQ;
    private final long GR;

    public f(Context context, String string, long l2, int n2) {
        super(context, string, 1, "policygap", "policycap");
        this.GR = l2;
        this.GQ = n2;
        b.d("RemoteConnPolicy", "cap=" + this.GQ + ", minDelay=" + this.GR);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    protected void execute() {
        f f2 = this;
        synchronized (f2) {
            boolean bl = this.e(System.currentTimeMillis(), this.gu());
            if (bl) {
                if (this.gy()) {
                    b.d("RemoteConnPolicy", "policy polling reach maximum, cap=" + this.gu());
                } else {
                    try {
                        while (!this.isEmpty()) {
                            i i2 = this.gx();
                            if (i2 == null) continue;
                            g g2 = (g)i2.getRequest();
                            Request.a a2 = i2.gz();
                            if (g2 == null || a2 == null) continue;
                            g2.a(a2);
                            b.d("RemoteConnPolicy", "policy queue items number:" + this.size() + ", lastPing=" + this.gw());
                            this.clear();
                            break;
                        }
                    }
                    catch (Exception exception) {
                        b.a("RemoteConnPolicy", "policy execute,", exception);
                    }
                }
            }
            return;
        }
    }

    @Override
    protected long gt() {
        return this.GR;
    }

    @Override
    protected int gu() {
        return this.GQ;
    }
}

