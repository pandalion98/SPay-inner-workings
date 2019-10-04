/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.core;

import com.samsung.android.spayfw.payprovider.f;

public class q {
    private String kd;
    private String kf;
    private f kg;
    private String mTokenId;

    public void H(String string) {
        this.kf = string;
    }

    public String aP() {
        return this.kf;
    }

    public f aQ() {
        return this.kg;
    }

    public void c(f f2) {
        this.kg = f2;
    }

    public String getTokenId() {
        return this.mTokenId;
    }

    public String getTokenStatus() {
        return this.kd;
    }

    public void setTokenId(String string) {
        this.mTokenId = string;
    }

    public void setTokenStatus(String string) {
        this.kd = string;
    }
}

