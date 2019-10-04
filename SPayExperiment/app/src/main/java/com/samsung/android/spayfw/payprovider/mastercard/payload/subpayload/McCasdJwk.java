/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.mastercard.payload.subpayload;

public class McCasdJwk {
    private String kty;
    private String[] x5c;

    public String getKty() {
        return this.kty;
    }

    public String[] getX5c() {
        return this.x5c;
    }

    public void setKty(String string) {
        this.kty = string;
    }

    public void setX5c(String[] arrstring) {
        this.x5c = arrstring;
    }
}

