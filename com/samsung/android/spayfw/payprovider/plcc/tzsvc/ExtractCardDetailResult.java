package com.samsung.android.spayfw.payprovider.plcc.tzsvc;

public class ExtractCardDetailResult {
    private String barcodeContent;
    private String cardNumber;
    private int errorCode;
    private String extraContent;
    private String imgSessionKey;
    private String pin;

    public String getCardnumber() {
        return this.cardNumber;
    }

    public void setCardnumber(String str) {
        this.cardNumber = str;
    }

    public String getPin() {
        return this.pin;
    }

    public void setPin(String str) {
        this.pin = str;
    }

    public String getBarcodeContent() {
        return this.barcodeContent;
    }

    public void setBarcodeContent(String str) {
        this.barcodeContent = str;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(int i) {
        this.errorCode = i;
    }

    public String getImgSessionKey() {
        return this.imgSessionKey;
    }

    public void setImgSessionKey(String str) {
        this.imgSessionKey = str;
    }

    public String getExtraContent() {
        return this.extraContent;
    }

    public void setExtraContent(String str) {
        this.extraContent = str;
    }

    public String toString() {
        return "ExtractCardDetailResult{cardNumber='" + this.cardNumber + '\'' + ", pin='" + this.pin + '\'' + ", barcodeContent='" + this.barcodeContent + '\'' + ", errorCode=" + this.errorCode + ", imgSessionKey='" + this.imgSessionKey + '\'' + ", extraContent='" + this.extraContent + '\'' + '}';
    }
}
