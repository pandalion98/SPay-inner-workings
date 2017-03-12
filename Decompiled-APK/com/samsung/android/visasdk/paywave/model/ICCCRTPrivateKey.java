package com.samsung.android.visasdk.paywave.model;

public class ICCCRTPrivateKey {
    private String CoefDmodP;
    private String CoefDmodQ;
    private String CoefQinvModP;
    private String exponent;
    private String modulus;
    private String primeP;
    private String primeQ;

    public String getModulus() {
        return this.modulus;
    }

    public void setModulus(String str) {
        this.modulus = str;
    }

    public String getExponent() {
        return this.exponent;
    }

    public void setExponent(String str) {
        this.exponent = str;
    }

    public String getPrimeP() {
        return this.primeP;
    }

    public void setPrimeP(String str) {
        this.primeP = str;
    }

    public String getPrimeQ() {
        return this.primeQ;
    }

    public void setPrimeQ(String str) {
        this.primeQ = str;
    }

    public String getCoefDmodP() {
        return this.CoefDmodP;
    }

    public void setCoefDmodP(String str) {
        this.CoefDmodP = str;
    }

    public String getCoefDmodQ() {
        return this.CoefDmodQ;
    }

    public void setCoefDmodQ(String str) {
        this.CoefDmodQ = str;
    }

    public String getCoefQinvModP() {
        return this.CoefQinvModP;
    }

    public void setCoefQinvModP(String str) {
        this.CoefQinvModP = str;
    }
}
