package com.absolute.android.persistservice;

import android.content.Context;

class q extends ac {
    static final /* synthetic */ boolean a = (!l.class.desiredAssertionStatus());
    final /* synthetic */ l b;

    q(l lVar, Context context, v vVar) {
        this.b = lVar;
        super(context, vVar, "abt-persistence-watchdog");
    }

    private void a(String str, WatchdogInfo watchdogInfo) {
        this.c.put(str, watchdogInfo);
        e();
    }

    private WatchdogInfo a(String str) {
        WatchdogInfo watchdogInfo = (WatchdogInfo) this.c.remove(str);
        e();
        return watchdogInfo;
    }

    private WatchdogInfo b(String str) {
        return (WatchdogInfo) this.c.get(str);
    }

    private void c(String str) {
        WatchdogInfo watchdogInfo = (WatchdogInfo) this.c.get(str);
        if (!a && watchdogInfo == null) {
            throw new AssertionError();
        } else if (watchdogInfo != null && watchdogInfo.a() != 0) {
            watchdogInfo.b();
            this.c.put(str, watchdogInfo);
            e();
        }
    }

    private void d(String str) {
        WatchdogInfo watchdogInfo = (WatchdogInfo) this.c.get(str);
        if (!a && watchdogInfo == null) {
            throw new AssertionError();
        } else if (watchdogInfo == null) {
        } else {
            if (watchdogInfo.a() != 0 || watchdogInfo.d() != 0) {
                watchdogInfo.b();
                watchdogInfo.e();
                this.c.put(str, watchdogInfo);
                e();
            }
        }
    }

    private void e(String str) {
        WatchdogInfo watchdogInfo = (WatchdogInfo) this.c.get(str);
        if (!a && watchdogInfo == null) {
            throw new AssertionError();
        } else if (watchdogInfo != null) {
            watchdogInfo.c();
            watchdogInfo.f();
            this.c.put(str, watchdogInfo);
            e();
        }
    }

    private void f(String str) {
        WatchdogInfo watchdogInfo = (WatchdogInfo) this.c.get(str);
        if (!a && watchdogInfo == null) {
            throw new AssertionError();
        } else if (watchdogInfo != null) {
            watchdogInfo.h();
            this.c.put(str, watchdogInfo);
            e();
        }
    }

    protected void c() {
    }

    public boolean i(String str) {
        q qVar = new q(this.b, this.d, this.e);
        try {
            qVar.k(str);
            if (equals(qVar)) {
                return false;
            }
            return true;
        } catch (Throwable th) {
            return true;
        }
    }
}
