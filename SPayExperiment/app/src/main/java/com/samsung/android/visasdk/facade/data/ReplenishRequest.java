/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.visasdk.facade.data;

import com.samsung.android.visasdk.facade.data.Signature;
import com.samsung.android.visasdk.paywave.model.TokenInfo;

public class ReplenishRequest {
    private String encryptionMetaData;
    private Signature signature;
    private TokenInfo tokenInfo;

    public String getEncryptionMetaData() {
        return this.encryptionMetaData;
    }

    public Signature getSignature() {
        return this.signature;
    }

    public TokenInfo getTokenInfo() {
        return this.tokenInfo;
    }

    public void setEncryptionMetaData(String string) {
        this.encryptionMetaData = string;
    }

    public void setSignature(Signature signature) {
        this.signature = signature;
    }

    public void setTokenInfo(TokenInfo tokenInfo) {
        this.tokenInfo = tokenInfo;
    }
}

