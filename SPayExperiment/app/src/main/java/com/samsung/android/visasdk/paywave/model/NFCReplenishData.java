/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
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

    public String getATC() {
        return this.ATC;
    }

    public String getApi() {
        return this.api;
    }

    public String getCDCVMverified() {
        return this.CDCVMverified;
    }

    public String getCVRexceededVelocity() {
        return this.CVRexceededVelocity;
    }

    public String getConsumerDeviceState() {
        return this.ConsumerDeviceState;
    }

    public String getEncKeyInfo() {
        return this.encKeyInfo;
    }

    public String getEncTokenInfo() {
        return this.encTokenInfo;
    }

    public String getReadyPayState() {
        return this.ReadyPayState;
    }

    public void setATC(String string) {
        this.ATC = string;
    }

    public void setApi(String string) {
        this.api = string;
    }

    public void setCDCVMverified(String string) {
        this.CDCVMverified = string;
    }

    public void setCVRexceededVelocity(String string) {
        this.CVRexceededVelocity = string;
    }

    public void setConsumerDeviceState(String string) {
        this.ConsumerDeviceState = string;
    }

    public void setEncKeyInfo(String string) {
        this.encKeyInfo = string;
    }

    public void setEncTokenInfo(String string) {
        this.encTokenInfo = string;
    }

    public void setReadyPayState(String string) {
        this.ReadyPayState = string;
    }
}

