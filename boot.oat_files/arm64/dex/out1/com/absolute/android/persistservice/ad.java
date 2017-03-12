package com.absolute.android.persistservice;

import com.absolute.android.persistence.IABTLogIterator;
import com.absolute.android.persistence.IABTPersistenceLog.Stub;

public class ad extends Stub {
    private v a;

    ad(v vVar) {
        this.a = vVar;
    }

    public IABTLogIterator getIterator(int i) {
        return this.a.a(i);
    }

    public void logMessage(int i, String str, String str2) {
        this.a.a(i, str, str2);
    }

    public void setSize(int i, int i2) {
        this.a.a(i, i2);
    }

    public void clear() {
        this.a.b();
    }

    public int getNumberOfLogs() {
        return this.a.c();
    }

    public int getMaxLogSizeKB() {
        return this.a.d();
    }
}
