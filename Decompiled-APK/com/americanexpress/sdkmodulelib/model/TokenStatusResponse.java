package com.americanexpress.sdkmodulelib.model;

public class TokenStatusResponse {
    private TokenDataStatus tokenDataStatus;
    private String tokenStatus;

    public String getTokenStatus() {
        return this.tokenStatus;
    }

    public void setTokenStatus(String str) {
        this.tokenStatus = str;
    }

    public TokenDataStatus getTokenDataStatus() {
        return this.tokenDataStatus;
    }

    public void setTokenDataStatus(TokenDataStatus tokenDataStatus) {
        this.tokenDataStatus = tokenDataStatus;
    }
}
