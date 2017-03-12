package com.americanexpress.sdkmodulelib.model;

public class TokenDataVersionResponse {
    TokenDataStatus tokenDataStatus;
    String tokenDataVersion;

    public String getTokenDataVersion() {
        return this.tokenDataVersion;
    }

    public void setTokenDataVersion(String str) {
        this.tokenDataVersion = str;
    }

    public TokenDataStatus getTokenDataStatus() {
        return this.tokenDataStatus;
    }

    public void setTokenDataStatus(TokenDataStatus tokenDataStatus) {
        this.tokenDataStatus = tokenDataStatus;
    }
}
