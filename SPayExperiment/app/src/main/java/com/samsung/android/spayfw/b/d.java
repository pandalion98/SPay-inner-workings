/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.b;

import com.samsung.android.spayfw.utils.h;

public abstract class d {
    public static final boolean DEBUG;
    protected int level;
    protected String oK;

    /*
     * Enabled aggressive block sorting
     */
    static {
        boolean bl = !h.fL();
        DEBUG = bl;
    }

    /*
     * Enabled aggressive block sorting
     */
    protected d(String string) {
        int n2 = DEBUG ? 1 : 4;
        this.level = n2;
        this.oK = string;
    }

    public abstract void a(int var1, String var2, String var3);

    /*
     * Enabled aggressive block sorting
     */
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null) {
            return false;
        }
        if (!(object instanceof d)) {
            return false;
        }
        d d2 = (d)object;
        if (this.level != d2.level) {
            return false;
        }
        if (this.oK == null) {
            if (d2.oK == null) return true;
            return false;
        }
        if (!this.oK.equals((Object)d2.oK)) return false;
        return true;
    }

    public void flush() {
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public int hashCode() {
        int n2;
        int n3 = 31 * (31 + this.level);
        if (this.oK == null) {
            n2 = 0;
            do {
                return n2 + n3;
                break;
            } while (true);
        }
        n2 = this.oK.hashCode();
        return n2 + n3;
    }

    public boolean isLoggable(int n2) {
        return this.level <= n2;
    }
}

