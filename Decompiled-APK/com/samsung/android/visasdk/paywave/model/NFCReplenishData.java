package com.samsung.android.visasdk.paywave.model;

public class NFCReplenishData {
    private String ATC;
    private String CDCVMverified;
    private String CVRexceededVelocity;
    private String ConsumerDeviceState;
    private String ReadyPayState;
    private String api;
    private String encKeyInfo;
    private String encTokenInfo;

    public void setEncKeyInfo(String str) {
        this.encKeyInfo = str;
    }

    public String getEncKeyInfo() {
        return this.encKeyInfo;
    }

    public void setATC(String str) {
        this.ATC = str;
    }

    public String getATC() {
        return this.ATC;
    }

    public void setConsumerDeviceState(String str) {
        this.ConsumerDeviceState = str;
    }

    public String getConsumerDeviceState() {
        return this.ConsumerDeviceState;
    }

    public void setEncTokenInfo(String str) {
        this.encTokenInfo = str;
    }

    public String getEncTokenInfo() {
        return this.encTokenInfo;
    }

    public void setReadyPayState(String str) {
        this.ReadyPayState = str;
    }

    public String getReadyPayState() {
        return this.ReadyPayState;
    }

    public void setCVRexceededVelocity(String str) {
        this.CVRexceededVelocity = str;
    }

    public String getCVRexceededVelocity() {
        return this.CVRexceededVelocity;
    }

    public void setApi(String str) {
        this.api = str;
    }

    public String getApi() {
        return this.api;
    }

    public void setCDCVMverified(String str) {
        this.CDCVMverified = str;
    }

    public String getCDCVMverified() {
        return this.CDCVMverified;
    }
}
