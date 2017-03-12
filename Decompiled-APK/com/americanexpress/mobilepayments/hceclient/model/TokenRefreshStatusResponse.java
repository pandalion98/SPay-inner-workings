package com.americanexpress.mobilepayments.hceclient.model;

public class TokenRefreshStatusResponse extends TokenOperationStatus {
    private String clientVersion;
    private int lupcCount;
    private long lupcRefreshCheckBack;
    private int maxATC;
    private boolean refreshRequired;
    private String tokenDataVersion;
    private String tokenState;

    public String getTokenState() {
        return this.tokenState;
    }

    public void setTokenState(String str) {
        this.tokenState = str;
    }

    public int getLupcCount() {
        return this.lupcCount;
    }

    public void setLupcCount(int i) {
        this.lupcCount = i;
    }

    public boolean isRefreshRequired() {
        return this.refreshRequired;
    }

    public void setRefreshRequired(boolean z) {
        this.refreshRequired = z;
    }

    public int getMaxATC() {
        return this.maxATC;
    }

    public void setMaxATC(int i) {
        this.maxATC = i;
    }

    public long getLupcRefreshCheckBack() {
        return this.lupcRefreshCheckBack;
    }

    public void setLupcRefreshCheckBack(long j) {
        this.lupcRefreshCheckBack = j;
    }

    public String getTokenDataVersion() {
        return this.tokenDataVersion;
    }

    public void setTokenDataVersion(String str) {
        this.tokenDataVersion = str;
    }

    public String getClientVersion() {
        return this.clientVersion;
    }

    public void setClientVersion(String str) {
        this.clientVersion = str;
    }

    public String toString() {
        return "TokenRefreshStatusResponse{lupcCount=" + this.lupcCount + ", refreshRequired=" + this.refreshRequired + ", maxATC=" + this.maxATC + ", lupcRefreshCheckBack=" + this.lupcRefreshCheckBack + ", tokenDataVersion='" + this.tokenDataVersion + '\'' + ", clientVersion='" + this.clientVersion + '\'' + ", tokenState='" + this.tokenState + '\'' + '}';
    }
}
