package com.absolute.android.persistservice;

import android.content.Context;

public class u extends ac {
    private String a = "";

    u(Context context, v vVar, String str) {
        super(context, vVar, str + "-log-settings");
        this.a = str;
    }

    protected synchronized int a() {
        return ((Integer) this.c.get("numLogFiles")).intValue();
    }

    protected synchronized void a(int i) {
        this.c.put("numLogFiles", Integer.valueOf(i));
        e();
    }

    protected synchronized int b() {
        return ((Integer) this.c.get("maxLogSizeKB")).intValue();
    }

    protected synchronized void b(int i) {
        this.c.put("maxLogSizeKB", Integer.valueOf(i));
        e();
    }

    protected synchronized String d() {
        return (String) this.c.get("currentLogFile");
    }

    protected synchronized void a(String str) {
        this.c.put("currentLogFile", str);
        e();
    }

    protected void c() {
        this.e.b("Persisted Log Settings file " + this.f + " was not found. Initializing with defaults.");
        this.e.c("Initializing number of rotating log files to: 4");
        this.c.put("numLogFiles", Integer.valueOf(4));
        this.e.c("Initializing maximum size of each log file in kB to: 60");
        this.c.put("maxLogSizeKB", Integer.valueOf(60));
        this.c.put("currentLogFile", new String(v.a(this.a)));
    }

    public boolean i(String str) {
        u uVar = new u(this.d, this.e, this.a);
        try {
            uVar.k(str);
            if (equals(uVar)) {
                return false;
            }
            return true;
        } catch (Throwable th) {
            return true;
        }
    }
}
