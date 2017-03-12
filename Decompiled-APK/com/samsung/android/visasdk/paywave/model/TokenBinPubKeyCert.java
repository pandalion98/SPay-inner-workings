package com.samsung.android.visasdk.paywave.model;

public class TokenBinPubKeyCert {
    private String certificate;
    private ExpirationDate expirationDate;
    private String exponent;
    private String remainder;

    public String getCertificate() {
        return this.certificate;
    }

    public void setCertificate(String str) {
        this.certificate = str;
    }

    public String getExponent() {
        return this.exponent;
    }

    public void setExponent(String str) {
        this.exponent = str;
    }

    public String getRemainder() {
        return this.remainder;
    }

    public void setRemainder(String str) {
        this.remainder = str;
    }

    public ExpirationDate getExpirationDate() {
        return this.expirationDate;
    }

    public void setExpirationDate(ExpirationDate expirationDate) {
        this.expirationDate = expirationDate;
    }
}
