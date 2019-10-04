/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.americanexpress.sdkmodulelib.model;

import com.americanexpress.sdkmodulelib.model.TokenDataStatus;

public class TokenDataVersionResponse {
    TokenDataStatus tokenDataStatus;
    String tokenDataVersion;

    public TokenDataStatus getTokenDataStatus() {
        return this.tokenDataStatus;
    }

    public String getTokenDataVersion() {
        return this.tokenDataVersion;
    }

    public void setTokenDataStatus(TokenDataStatus tokenDataStatus) {
        this.tokenDataStatus = tokenDataStatus;
    }

    public void setTokenDataVersion(String string) {
        this.tokenDataVersion = string;
    }
}

