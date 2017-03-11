package com.samsung.android.spayfw.payprovider.mastercard.tds.network.models;

public class McTdsRegistrationCodeResponse extends McTdsCommonResponse {
    private String registrationCode1;

    public String getRegistrationCode1() {
        return this.registrationCode1;
    }

    public void setRegistrationCode1(String str) {
        this.registrationCode1 = str;
    }
}
