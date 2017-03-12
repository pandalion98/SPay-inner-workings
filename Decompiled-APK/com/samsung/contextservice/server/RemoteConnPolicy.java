package com.samsung.contextservice.server;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.samsung.android.spayfw.remoteservice.Request;
import com.samsung.contextservice.p029b.CSlog;
import java.util.LinkedList;

/* renamed from: com.samsung.contextservice.server.h */
abstract class RemoteConnPolicy<T extends Request> {
    private String GS;
    private String GT;
    private LinkedList<RequestBundle<T>> GU;
    private long GV;
    private long GW;
    private int GX;
    private Context mContext;
    private String mName;
    private int mType;

    protected abstract void execute();

    protected abstract long gt();

    protected abstract int gu();

    public RemoteConnPolicy(Context context, String str, int i, String str2, String str3) {
        this.GU = new LinkedList();
        this.GV = 0;
        this.GW = 0;
        this.GX = 0;
        if (context == null) {
            throw new Exception("context is null");
        }
        this.mContext = context;
        this.mName = str;
        this.mType = i;
        this.GS = str2;
        this.GT = str3;
        this.GV = bM(this.GS);
        bO(this.GT);
        CSlog.m1408d("RemoteConnPolicy", "init " + this.mName + " with lastPing=" + this.GV + ", lastCap=" + this.GW + ", usedCap=" + this.GX);
    }

    protected final long gw() {
        return this.GV;
    }

    public final void m1421a(RequestBundle requestBundle) {
        this.GU.add(requestBundle);
    }

    public final boolean isEmpty() {
        return this.GU.isEmpty();
    }

    public final RequestBundle gx() {
        return (RequestBundle) this.GU.removeLast();
    }

    public final void clear() {
        CSlog.m1408d("RemoteConnPolicy", "clearing the queue");
        this.GU.clear();
    }

    public final int size() {
        return this.GU.size();
    }

    public final void m1420C(long j) {
        m1419b(this.GS, j);
        bN(this.GT);
    }

    public final void m1422b(RequestBundle requestBundle) {
        m1421a(requestBundle);
        execute();
    }

    private void m1419b(String str, long j) {
        this.GV = j;
        Editor edit = this.mContext.getSharedPreferences("context_settings", 0).edit();
        edit.putLong(str, j);
        edit.apply();
    }

    private long bM(String str) {
        return this.mContext.getSharedPreferences("context_settings", 0).getLong(str, this.GV);
    }

    private void bN(String str) {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis >= this.GW && currentTimeMillis < this.GW + 86400000) {
            this.GX++;
            CSlog.m1408d("RemoteConnPolicy", "used cap is " + this.GX);
        } else if (currentTimeMillis >= this.GW + 86400000) {
            CSlog.m1408d("RemoteConnPolicy", "reset used cap and last cap time");
            this.GX++;
        } else {
            this.GX++;
        }
        Editor edit = this.mContext.getSharedPreferences("context_settings", 0).edit();
        edit.putInt(str + "usedcap", this.GX);
        edit.apply();
    }

    private void bO(String str) {
        SharedPreferences sharedPreferences = this.mContext.getSharedPreferences("context_settings", 0);
        this.GW = sharedPreferences.getLong(str + "lastcaptime", this.GW);
        this.GX = sharedPreferences.getInt(str + "usedcap", this.GX);
        if (this.GW == 0) {
            this.GW = System.currentTimeMillis();
            this.GX = 0;
            Editor edit = sharedPreferences.edit();
            edit.putLong(str + "lastcaptime", this.GW);
            edit.putInt(str + "usedcap", this.GX);
            edit.apply();
        }
    }

    public final boolean m1423e(long j, long j2) {
        long gw = j - gw();
        if (gw >= gt()) {
            return true;
        }
        CSlog.m1408d("RemoteConnPolicy", "No " + this.mName + " polling is needed, lastPing=" + gw() + ", diff=" + gw);
        return false;
    }

    public final boolean gy() {
        long currentTimeMillis = System.currentTimeMillis();
        String str = this.GT;
        if (this.GX < gu() && currentTimeMillis < this.GW + 86400000) {
            return false;
        }
        if (currentTimeMillis < this.GW + 86400000) {
            return true;
        }
        this.GW += 86400000;
        this.GX = 0;
        Editor edit = this.mContext.getSharedPreferences("context_settings", 0).edit();
        edit.putLong(str + "lastcaptime", this.GW);
        edit.putInt(str + "usedcap", this.GX);
        edit.apply();
        CSlog.m1408d("RemoteConnPolicy", "update last cap time to " + this.GW + " and used cap to " + this.GX);
        return false;
    }
}
