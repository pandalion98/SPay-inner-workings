/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.SharedPreferences
 *  android.content.SharedPreferences$Editor
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.util.LinkedList
 */
package com.samsung.contextservice.server;

import android.content.Context;
import android.content.SharedPreferences;
import com.samsung.android.spayfw.remoteservice.Request;
import com.samsung.contextservice.b.b;
import com.samsung.contextservice.server.i;
import java.util.LinkedList;

abstract class h<T extends Request> {
    private String GS;
    private String GT;
    private LinkedList<i<T>> GU = new LinkedList();
    private long GV = 0L;
    private long GW = 0L;
    private int GX = 0;
    private Context mContext;
    private String mName;
    private int mType;

    public h(Context context, String string, int n2, String string2, String string3) {
        if (context == null) {
            throw new Exception("context is null");
        }
        this.mContext = context;
        this.mName = string;
        this.mType = n2;
        this.GS = string2;
        this.GT = string3;
        this.GV = this.bM(this.GS);
        this.bO(this.GT);
        b.d("RemoteConnPolicy", "init " + this.mName + " with lastPing=" + this.GV + ", lastCap=" + this.GW + ", usedCap=" + this.GX);
    }

    private void b(String string, long l2) {
        this.GV = l2;
        SharedPreferences.Editor editor = this.mContext.getSharedPreferences("context_settings", 0).edit();
        editor.putLong(string, l2);
        editor.apply();
    }

    private long bM(String string) {
        return this.mContext.getSharedPreferences("context_settings", 0).getLong(string, this.GV);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void bN(String string) {
        long l2 = System.currentTimeMillis();
        if (l2 >= this.GW && l2 < 86400000L + this.GW) {
            this.GX = 1 + this.GX;
            b.d("RemoteConnPolicy", "used cap is " + this.GX);
        } else if (l2 >= 86400000L + this.GW) {
            b.d("RemoteConnPolicy", "reset used cap and last cap time");
            this.GX = 1 + this.GX;
        } else {
            this.GX = 1 + this.GX;
        }
        SharedPreferences.Editor editor = this.mContext.getSharedPreferences("context_settings", 0).edit();
        editor.putInt(string + "usedcap", this.GX);
        editor.apply();
    }

    private void bO(String string) {
        SharedPreferences sharedPreferences = this.mContext.getSharedPreferences("context_settings", 0);
        this.GW = sharedPreferences.getLong(string + "lastcaptime", this.GW);
        this.GX = sharedPreferences.getInt(string + "usedcap", this.GX);
        if (this.GW == 0L) {
            this.GW = System.currentTimeMillis();
            this.GX = 0;
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putLong(string + "lastcaptime", this.GW);
            editor.putInt(string + "usedcap", this.GX);
            editor.apply();
        }
    }

    public final void C(long l2) {
        this.b(this.GS, l2);
        this.bN(this.GT);
    }

    public final void a(i i2) {
        this.GU.add((Object)i2);
    }

    public final void b(i i2) {
        this.a(i2);
        this.execute();
    }

    public final void clear() {
        b.d("RemoteConnPolicy", "clearing the queue");
        this.GU.clear();
    }

    public final boolean e(long l2, long l3) {
        long l4 = l2 - this.gw();
        if (l4 < this.gt()) {
            b.d("RemoteConnPolicy", "No " + this.mName + " polling is needed, lastPing=" + this.gw() + ", diff=" + l4);
            return false;
        }
        return true;
    }

    protected abstract void execute();

    protected abstract long gt();

    protected abstract int gu();

    protected final long gw() {
        return this.GV;
    }

    public final i gx() {
        return (i)this.GU.removeLast();
    }

    public final boolean gy() {
        long l2 = System.currentTimeMillis();
        String string = this.GT;
        if (this.GX < this.gu() && l2 < 86400000L + this.GW) {
            return false;
        }
        if (l2 >= 86400000L + this.GW) {
            this.GW = 86400000L + this.GW;
            this.GX = 0;
            SharedPreferences.Editor editor = this.mContext.getSharedPreferences("context_settings", 0).edit();
            editor.putLong(string + "lastcaptime", this.GW);
            editor.putInt(string + "usedcap", this.GX);
            editor.apply();
            b.d("RemoteConnPolicy", "update last cap time to " + this.GW + " and used cap to " + this.GX);
            return false;
        }
        return true;
    }

    public final boolean isEmpty() {
        return this.GU.isEmpty();
    }

    public final int size() {
        return this.GU.size();
    }
}

