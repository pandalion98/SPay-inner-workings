package com.absolute.android.persistservice;

import android.content.Context;
import com.absolute.android.persistence.ABTPersistenceManager;
import com.absolute.android.utils.FileUtil;

public class x extends ac {
    x(Context context, v vVar, String str) {
        super(context, vVar, str);
    }

    protected synchronized int a() {
        return ((Integer) this.c.get("state")).intValue();
    }

    protected synchronized void a(int i) {
        this.c.put("state", Integer.valueOf(i));
        e();
    }

    protected synchronized String b() {
        return (String) this.c.get("buildFingerPrint");
    }

    protected synchronized void a(String str) {
        this.c.put("buildFingerPrint", str);
        e();
    }

    protected synchronized String d() {
        return (String) this.c.get("deviceId");
    }

    protected synchronized void b(String str) {
        this.c.put("deviceId", str);
        e();
    }

    protected void c() {
        this.e.b("Persistence Settings file " + this.f + " was not found. Initializing with defaults.");
        this.e.c("Initializing persistence state to: " + ABTPersistenceManager.stateToString(1));
        this.c.put("state", Integer.valueOf(1));
        this.c.put("buildFingerPrint", "");
        this.c.put("deviceId", "");
    }

    public boolean i(String str) {
        x xVar = new x(this.d, this.e, FileUtil.getFilename(str));
        try {
            xVar.k(str);
            if (equals(xVar)) {
                return false;
            }
            return true;
        } catch (Throwable th) {
            return true;
        }
    }
}
