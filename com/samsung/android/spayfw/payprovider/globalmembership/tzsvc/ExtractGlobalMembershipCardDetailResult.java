package com.samsung.android.spayfw.payprovider.globalmembership.tzsvc;

/* renamed from: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.a */
public class ExtractGlobalMembershipCardDetailResult {
    private String barcodeContent;
    private String barcodeType;
    private String cardnumber;
    private int errorCode;
    private String imgSessionKey;
    private String numericValue;
    private String pin;

    public String toString() {
        return "CardDetail  cardnumber [" + this.cardnumber + "] pin " + this.pin + " barcodeContent [" + this.barcodeContent + "] barcodeType [" + this.barcodeType + "] numericValue [" + this.numericValue + "] errorCode [" + this.errorCode + "] imgSessionKey [" + this.imgSessionKey + "]";
    }

    public String getCardnumber() {
        return this.cardnumber;
    }

    public void setCardnumber(String str) {
        this.cardnumber = str;
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

    public String getBarcodeType() {
        return this.barcodeType;
    }

    public void setBarcodeType(String str) {
        this.barcodeType = str;
    }

    public String getNumericValue() {
        return this.numericValue;
    }

    public void setNumericValue(String str) {
        this.numericValue = str;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(int i) {
        this.errorCode = i;
    }
}
