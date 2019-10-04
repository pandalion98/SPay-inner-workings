/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.visasdk.paywave.model;

public class ICCCRTPrivateKey {
    private String CoefDmodP;
    private String CoefDmodQ;
    private String CoefQinvModP;
    private String exponent;
    private String modulus;
    private String primeP;
    private String primeQ;

    public String getCoefDmodP() {
        return this.CoefDmodP;
    }

    public String getCoefDmodQ() {
        return this.CoefDmodQ;
    }

    public String getCoefQinvModP() {
        return this.CoefQinvModP;
    }

    public String getExponent() {
        return this.exponent;
    }

    public String getModulus() {
        return this.modulus;
    }

    public String getPrimeP() {
        return this.primeP;
    }

    public String getPrimeQ() {
        return this.primeQ;
    }

    public void setCoefDmodP(String string) {
        this.CoefDmodP = string;
    }

    public void setCoefDmodQ(String string) {
        this.CoefDmodQ = string;
    }

    public void setCoefQinvModP(String string) {
        this.CoefQinvModP = string;
    }

    public void setExponent(String string) {
        this.exponent = string;
    }

    public void setModulus(String string) {
        this.modulus = string;
    }

    public void setPrimeP(String string) {
        this.primeP = string;
    }

    public void setPrimeQ(String string) {
        this.primeQ = string;
    }
}

