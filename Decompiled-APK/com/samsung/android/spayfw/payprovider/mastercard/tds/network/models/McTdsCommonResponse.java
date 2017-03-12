package com.samsung.android.spayfw.payprovider.mastercard.tds.network.models;

public class McTdsCommonResponse {
    private String errorCode;
    private String errorDescription;
    private String responseHost;

    public String getResponseHost() {
        return this.responseHost;
    }

    public void setResponseHost(String str) {
        this.responseHost = str;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(String str) {
        this.errorCode = str;
    }

    public String getErrorDescription() {
        return this.errorDescription;
    }

    public void setErrorDescription(String str) {
        this.errorDescription = str;
    }
}
