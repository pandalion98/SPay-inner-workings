/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.plcc.tzsvc;

public class ExtractCardDetailResult {
    private String barcodeContent;
    private String cardNumber;
    private int errorCode;
    private String extraContent;
    private String imgSessionKey;
    private String pin;

    public String getBarcodeContent() {
        return this.barcodeContent;
    }

    public String getCardnumber() {
        return this.cardNumber;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public String getExtraContent() {
        return this.extraContent;
    }

    public String getImgSessionKey() {
        return this.imgSessionKey;
    }

    public String getPin() {
        return this.pin;
    }

    public void setBarcodeContent(String string) {
        this.barcodeContent = string;
    }

    public void setCardnumber(String string) {
        this.cardNumber = string;
    }

    public void setErrorCode(int n2) {
        this.errorCode = n2;
    }

    public void setExtraContent(String string) {
        this.extraContent = string;
    }

    public void setImgSessionKey(String string) {
        this.imgSessionKey = string;
    }

    public void setPin(String string) {
        this.pin = string;
    }

    public String toString() {
        return "ExtractCardDetailResult{cardNumber='" + this.cardNumber + '\'' + ", pin='" + this.pin + '\'' + ", barcodeContent='" + this.barcodeContent + '\'' + ", errorCode=" + this.errorCode + ", imgSessionKey='" + this.imgSessionKey + '\'' + ", extraContent='" + this.extraContent + '\'' + '}';
    }
}

