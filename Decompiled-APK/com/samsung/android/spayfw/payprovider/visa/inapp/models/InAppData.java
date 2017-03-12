package com.samsung.android.spayfw.payprovider.visa.inapp.models;

public class InAppData {
    private String amount;
    private String cryptogram;
    private String currency_code;
    private String eci_indicator;
    private String tokenPAN;
    private String tokenPanExpiration;
    private String utc;

    public String getEci_indicator() {
        return this.eci_indicator;
    }

    public void setEci_indicator(String str) {
        this.eci_indicator = str;
    }

    public String getCryptogram() {
        return this.cryptogram;
    }

    public void setCryptogram(String str) {
        this.cryptogram = str;
    }

    public String getTokenPAN() {
        return this.tokenPAN;
    }

    public void setTokenPAN(String str) {
        this.tokenPAN = str;
    }

    public String getTokenPANExpiration() {
        return this.tokenPanExpiration;
    }

    public void setTokenPANExpiration(String str) {
        this.tokenPanExpiration = str;
    }

    public void setAmount(String str) {
        this.amount = str;
    }

    public void setCurrency_code(String str) {
        this.currency_code = str;
    }

    public void setUtc(String str) {
        this.utc = str;
    }
}
