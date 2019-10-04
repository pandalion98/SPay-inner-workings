/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.visasdk.facade.data;

public class ProvisionAckRequest {
    private String api;
    private String failureReason;
    private String provisioningStatus;

    public String getApi() {
        return this.api;
    }

    public String getFailureReason() {
        return this.failureReason;
    }

    public String getProvisioningStatus() {
        return this.provisioningStatus;
    }

    public void setApi(String string) {
        this.api = string;
    }

    public void setFailureReason(String string) {
        this.failureReason = string;
    }

    public void setProvisioningStatus(String string) {
        this.provisioningStatus = string;
    }
}

