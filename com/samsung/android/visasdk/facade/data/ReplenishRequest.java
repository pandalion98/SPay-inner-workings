package com.samsung.android.visasdk.facade.data;

import com.samsung.android.visasdk.paywave.model.TokenInfo;

public class ReplenishRequest {
    private String encryptionMetaData;
    private Signature signature;
    private TokenInfo tokenInfo;

    public String getEncryptionMetaData() {
        return this.encryptionMetaData;
    }

    public void setEncryptionMetaData(String str) {
        this.encryptionMetaData = str;
    }

    public Signature getSignature() {
        return this.signature;
    }

    public void setSignature(Signature signature) {
        this.signature = signature;
    }

    public void setTokenInfo(TokenInfo tokenInfo) {
        this.tokenInfo = tokenInfo;
    }

    public TokenInfo getTokenInfo() {
        return this.tokenInfo;
    }
}
