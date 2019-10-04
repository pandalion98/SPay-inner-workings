/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.americanexpress.sdkmodulelib.model;

import com.americanexpress.sdkmodulelib.model.TokenDataStatus;

public class TokenStatusResponse {
    private TokenDataStatus tokenDataStatus;
    private String tokenStatus;

    public TokenDataStatus getTokenDataStatus() {
        return this.tokenDataStatus;
    }

    public String getTokenStatus() {
        return this.tokenStatus;
    }

    public void setTokenDataStatus(TokenDataStatus tokenDataStatus) {
        this.tokenDataStatus = tokenDataStatus;
    }

    public void setTokenStatus(String string) {
        this.tokenStatus = string;
    }
}

