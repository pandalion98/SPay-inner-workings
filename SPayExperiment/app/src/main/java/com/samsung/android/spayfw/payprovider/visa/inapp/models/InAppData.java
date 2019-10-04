/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.visa.inapp.models;

public class InAppData {
    private String amount;
    private String cryptogram;
    private String currency_code;
    private String eci_indicator;
    private String tokenPAN;
    private String tokenPanExpiration;
    private String utc;

    public String getCryptogram() {
        return this.cryptogram;
    }

    public String getEci_indicator() {
        return this.eci_indicator;
    }

    public String getTokenPAN() {
        return this.tokenPAN;
    }

    public String getTokenPANExpiration() {
        return this.tokenPanExpiration;
    }

    public void setAmount(String string) {
        this.amount = string;
    }

    public void setCryptogram(String string) {
        this.cryptogram = string;
    }

    public void setCurrency_code(String string) {
        this.currency_code = string;
    }

    public void setEci_indicator(String string) {
        this.eci_indicator = string;
    }

    public void setTokenPAN(String string) {
        this.tokenPAN = string;
    }

    public void setTokenPANExpiration(String string) {
        this.tokenPanExpiration = string;
    }

    public void setUtc(String string) {
        this.utc = string;
    }
}

