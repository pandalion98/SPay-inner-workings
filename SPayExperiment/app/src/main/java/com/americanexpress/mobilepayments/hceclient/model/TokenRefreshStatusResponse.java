/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 */
package com.americanexpress.mobilepayments.hceclient.model;

import com.americanexpress.mobilepayments.hceclient.model.TokenOperationStatus;

public class TokenRefreshStatusResponse
extends TokenOperationStatus {
    private String clientVersion;
    private int lupcCount;
    private long lupcRefreshCheckBack;
    private int maxATC;
    private boolean refreshRequired;
    private String tokenDataVersion;
    private String tokenState;

    public String getClientVersion() {
        return this.clientVersion;
    }

    public int getLupcCount() {
        return this.lupcCount;
    }

    public long getLupcRefreshCheckBack() {
        return this.lupcRefreshCheckBack;
    }

    public int getMaxATC() {
        return this.maxATC;
    }

    public String getTokenDataVersion() {
        return this.tokenDataVersion;
    }

    public String getTokenState() {
        return this.tokenState;
    }

    public boolean isRefreshRequired() {
        return this.refreshRequired;
    }

    public void setClientVersion(String string) {
        this.clientVersion = string;
    }

    public void setLupcCount(int n2) {
        this.lupcCount = n2;
    }

    public void setLupcRefreshCheckBack(long l2) {
        this.lupcRefreshCheckBack = l2;
    }

    public void setMaxATC(int n2) {
        this.maxATC = n2;
    }

    public void setRefreshRequired(boolean bl) {
        this.refreshRequired = bl;
    }

    public void setTokenDataVersion(String string) {
        this.tokenDataVersion = string;
    }

    public void setTokenState(String string) {
        this.tokenState = string;
    }

    public String toString() {
        return "TokenRefreshStatusResponse{lupcCount=" + this.lupcCount + ", refreshRequired=" + this.refreshRequired + ", maxATC=" + this.maxATC + ", lupcRefreshCheckBack=" + this.lupcRefreshCheckBack + ", tokenDataVersion='" + this.tokenDataVersion + '\'' + ", clientVersion='" + this.clientVersion + '\'' + ", tokenState='" + this.tokenState + '\'' + '}';
    }
}

