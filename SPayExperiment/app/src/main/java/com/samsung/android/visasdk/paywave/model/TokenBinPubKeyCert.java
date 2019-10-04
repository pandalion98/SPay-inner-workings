/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.visasdk.paywave.model;

import com.samsung.android.visasdk.paywave.model.ExpirationDate;

public class TokenBinPubKeyCert {
    private String certificate;
    private ExpirationDate expirationDate;
    private String exponent;
    private String remainder;

    public String getCertificate() {
        return this.certificate;
    }

    public ExpirationDate getExpirationDate() {
        return this.expirationDate;
    }

    public String getExponent() {
        return this.exponent;
    }

    public String getRemainder() {
        return this.remainder;
    }

    public void setCertificate(String string) {
        this.certificate = string;
    }

    public void setExpirationDate(ExpirationDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public void setExponent(String string) {
        this.exponent = string;
    }

    public void setRemainder(String string) {
        this.remainder = string;
    }
}

