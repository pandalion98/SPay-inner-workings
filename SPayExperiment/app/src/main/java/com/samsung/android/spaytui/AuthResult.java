/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spaytui;

public class AuthResult {
    private String authType;
    private byte[] secObjData;

    AuthResult(byte[] arrby, String string) {
        this.secObjData = arrby;
        this.authType = string;
    }

    public String getAuthType() {
        return this.authType;
    }

    public byte[] getSecObjData() {
        return this.secObjData;
    }

    public void setAuthType(String string) {
        this.authType = string;
    }

    public void setSecObjData(byte[] arrby) {
        this.secObjData = arrby;
    }
}

