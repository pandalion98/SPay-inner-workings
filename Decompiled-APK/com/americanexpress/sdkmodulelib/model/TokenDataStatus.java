package com.americanexpress.sdkmodulelib.model;

public class TokenDataStatus {
    private String detailCode;
    private String detailMessage;
    private String reasonCode;

    public String getReasonCode() {
        return this.reasonCode;
    }

    public void setReasonCode(String str) {
        this.reasonCode = str;
    }

    public String getDetailCode() {
        return this.detailCode;
    }

    public void setDetailCode(String str) {
        this.detailCode = str;
    }

    public String getDetailMessage() {
        return this.detailMessage;
    }

    public void setDetailMessage(String str) {
        this.detailMessage = str;
    }
}
