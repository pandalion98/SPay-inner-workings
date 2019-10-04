/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.globalmembership.tzsvc;

public class a {
    private String barcodeContent;
    private String barcodeType;
    private String cardnumber;
    private int errorCode;
    private String imgSessionKey;
    private String numericValue;
    private String pin;

    public String getBarcodeContent() {
        return this.barcodeContent;
    }

    public String getBarcodeType() {
        return this.barcodeType;
    }

    public String getCardnumber() {
        return this.cardnumber;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public String getNumericValue() {
        return this.numericValue;
    }

    public String getPin() {
        return this.pin;
    }

    public void setBarcodeContent(String string) {
        this.barcodeContent = string;
    }

    public void setBarcodeType(String string) {
        this.barcodeType = string;
    }

    public void setCardnumber(String string) {
        this.cardnumber = string;
    }

    public void setErrorCode(int n2) {
        this.errorCode = n2;
    }

    public void setNumericValue(String string) {
        this.numericValue = string;
    }

    public void setPin(String string) {
        this.pin = string;
    }

    public String toString() {
        return "CardDetail  cardnumber [" + this.cardnumber + "] pin " + this.pin + " barcodeContent [" + this.barcodeContent + "] barcodeType [" + this.barcodeType + "] numericValue [" + this.numericValue + "] errorCode [" + this.errorCode + "] imgSessionKey [" + this.imgSessionKey + "]";
    }
}

