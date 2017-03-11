package com.samsung.android.visasdk.facade.data;

public class ProvisionAckRequest {
    private String api;
    private String failureReason;
    private String provisioningStatus;

    public String getApi() {
        return this.api;
    }

    public void setApi(String str) {
        this.api = str;
    }

    public String getFailureReason() {
        return this.failureReason;
    }

    public void setFailureReason(String str) {
        this.failureReason = str;
    }

    public String getProvisioningStatus() {
        return this.provisioningStatus;
    }

    public void setProvisioningStatus(String str) {
        this.provisioningStatus = str;
    }
}
