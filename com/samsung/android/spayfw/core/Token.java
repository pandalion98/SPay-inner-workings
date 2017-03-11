package com.samsung.android.spayfw.core;

import com.samsung.android.spayfw.payprovider.ProviderTokenKey;

/* renamed from: com.samsung.android.spayfw.core.q */
public class Token {
    private String kd;
    private String kf;
    private ProviderTokenKey kg;
    private String mTokenId;

    public String getTokenId() {
        return this.mTokenId;
    }

    public void setTokenId(String str) {
        this.mTokenId = str;
    }

    public String getTokenStatus() {
        return this.kd;
    }

    public void setTokenStatus(String str) {
        this.kd = str;
    }

    public String aP() {
        return this.kf;
    }

    public void m662H(String str) {
        this.kf = str;
    }

    public ProviderTokenKey aQ() {
        return this.kg;
    }

    public void m663c(ProviderTokenKey providerTokenKey) {
        this.kg = providerTokenKey;
    }
}
