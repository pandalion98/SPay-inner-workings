package com.absolute.android.persistservice;

import android.os.SystemClock;
import java.io.Serializable;

public class WatchdogInfo implements Serializable {
    private static final long serialVersionUID = 1;
    private Integer m_consecutiveRestartCount = Integer.valueOf(0);
    private Long m_startTimestamp = Long.valueOf(0);
    private Integer m_totalRestarts = Integer.valueOf(0);

    WatchdogInfo() {
    }

    protected int a() {
        return this.m_consecutiveRestartCount.intValue();
    }

    protected void b() {
        this.m_consecutiveRestartCount = Integer.valueOf(0);
    }

    protected void c() {
        this.m_consecutiveRestartCount = Integer.valueOf(this.m_consecutiveRestartCount.intValue() + 1);
    }

    protected int d() {
        return this.m_totalRestarts.intValue();
    }

    protected void e() {
        this.m_totalRestarts = Integer.valueOf(0);
    }

    protected void f() {
        this.m_totalRestarts = Integer.valueOf(this.m_totalRestarts.intValue() + 1);
    }

    protected long g() {
        return this.m_startTimestamp.longValue();
    }

    protected void h() {
        this.m_startTimestamp = Long.valueOf(SystemClock.elapsedRealtime());
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Consecutive restarts = " + a() + " : ");
        stringBuilder.append("Total restarts = " + d() + " : ");
        if (a() > 0) {
            stringBuilder.append("Msec since last restart = " + (SystemClock.elapsedRealtime() - g()));
        }
        return stringBuilder.toString();
    }
}
