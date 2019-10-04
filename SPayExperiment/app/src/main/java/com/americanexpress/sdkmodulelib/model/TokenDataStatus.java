/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.americanexpress.sdkmodulelib.model;

public class TokenDataStatus {
    private String detailCode;
    private String detailMessage;
    private String reasonCode;

    public String getDetailCode() {
        return this.detailCode;
    }

    public String getDetailMessage() {
        return this.detailMessage;
    }

    public String getReasonCode() {
        return this.reasonCode;
    }

    public void setDetailCode(String string) {
        this.detailCode = string;
    }

    public void setDetailMessage(String string) {
        this.detailMessage = string;
    }

    public void setReasonCode(String string) {
        this.reasonCode = string;
    }
}

