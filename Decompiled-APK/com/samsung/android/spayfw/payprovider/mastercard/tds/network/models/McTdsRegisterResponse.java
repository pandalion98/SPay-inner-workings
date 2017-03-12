package com.samsung.android.spayfw.payprovider.mastercard.tds.network.models;

public class McTdsRegisterResponse extends McTdsCommonResponse {
    private String authenticationCode;
    private String tdsUrl;

    public String getAuthenticationCode() {
        return this.authenticationCode;
    }

    public void setAuthenticationCode(String str) {
        this.authenticationCode = str;
    }

    public String getTdsUrl() {
        return this.tdsUrl;
    }

    public void setTdsUrl(String str) {
        this.tdsUrl = str;
    }
}
