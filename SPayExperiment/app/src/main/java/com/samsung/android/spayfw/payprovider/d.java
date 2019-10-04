/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider;

import com.samsung.android.spayfw.payprovider.f;

public class d {
    private int mRequestType;
    private f oT;
    private int oU;
    private String oV;

    public d(int n2, int n3, f f2) {
        this.mRequestType = n2;
        this.oU = n3;
        this.oT = f2;
    }

    public void ar(String string) {
        this.oV = string;
    }

    public int ci() {
        return this.oU;
    }

    public String cj() {
        return this.oV;
    }

    public f ck() {
        return this.oT;
    }

    public int getRequestType() {
        return this.mRequestType;
    }
}

