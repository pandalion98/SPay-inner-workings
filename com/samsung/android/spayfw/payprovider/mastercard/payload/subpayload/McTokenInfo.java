package com.samsung.android.spayfw.payprovider.mastercard.payload.subpayload;

public class McTokenInfo {
    private String tokenExpiry;

    public void setTokenExpiry(String str) {
        this.tokenExpiry = str;
    }

    public String getTokenExpiry() {
        return this.tokenExpiry;
    }
}
