package com.samsung.android.visasdk.facade.data;

import com.samsung.android.visasdk.paywave.model.TokenInfo;

public class ReplenishAckRequest {
    private TokenInfo tokenInfo;

    public TokenInfo getTokenInfo() {
        return this.tokenInfo;
    }

    public void setTokenInfo(TokenInfo tokenInfo) {
        this.tokenInfo = tokenInfo;
    }
}
