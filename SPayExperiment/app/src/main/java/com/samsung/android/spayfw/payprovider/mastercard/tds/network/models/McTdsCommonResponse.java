/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.mastercard.tds.network.models;

public class McTdsCommonResponse {
    private String errorCode;
    private String errorDescription;
    private String responseHost;

    public String getErrorCode() {
        return this.errorCode;
    }

    public String getErrorDescription() {
        return this.errorDescription;
    }

    public String getResponseHost() {
        return this.responseHost;
    }

    public void setErrorCode(String string) {
        this.errorCode = string;
    }

    public void setErrorDescription(String string) {
        this.errorDescription = string;
    }

    public void setResponseHost(String string) {
        this.responseHost = string;
    }
}

