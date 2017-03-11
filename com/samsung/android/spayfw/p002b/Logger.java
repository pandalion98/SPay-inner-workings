package com.samsung.android.spayfw.p002b;

import com.samsung.android.spayfw.utils.Utils;

/* renamed from: com.samsung.android.spayfw.b.d */
public abstract class Logger {
    public static final boolean DEBUG;
    protected int level;
    protected String oK;

    public abstract void m271a(int i, String str, String str2);

    static {
        DEBUG = !Utils.fL();
    }

    protected Logger(String str) {
        int i;
        if (DEBUG) {
            i = 1;
        } else {
            i = 4;
        }
        this.level = i;
        this.oK = str;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Logger)) {
            return false;
        }
        Logger logger = (Logger) obj;
        if (this.level != logger.level) {
            return false;
        }
        if (this.oK == null) {
            if (logger.oK != null) {
                return false;
            }
            return true;
        } else if (this.oK.equals(logger.oK)) {
            return true;
        } else {
            return false;
        }
    }

    public void flush() {
    }

    public int hashCode() {
        return (this.oK == null ? 0 : this.oK.hashCode()) + ((this.level + 31) * 31);
    }

    public boolean isLoggable(int i) {
        if (this.level <= i) {
            return true;
        }
        return false;
    }
}
