/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Long
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider;

public class f {
    private String mF;
    private String oZ;

    public f(long l2) {
        this.oZ = String.valueOf((long)l2);
    }

    public f(String string) {
        this.oZ = string;
    }

    public long cm() {
        return Long.parseLong((String)this.oZ);
    }

    public String cn() {
        if (this.oZ != null) {
            return String.valueOf((Object)this.oZ);
        }
        return null;
    }

    public String getTrTokenId() {
        return this.mF;
    }

    public void setTrTokenId(String string) {
        this.mF = string;
    }
}

