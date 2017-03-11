package com.samsung.android.spayfw.payprovider.mastercard.payload.subpayload;

public class McCardInfo {
    private String accountNumber;
    private McBillingAddress billingAddress;
    private String cardholderName;
    private String dataValidUntilTimestamp;
    private String expiryMonth;
    private String expiryYear;
    private String securityCode;
    private String source;

    public void setAccountNumber(String str) {
        this.accountNumber = str;
    }

    public void setExpiryMonth(String str) {
        this.expiryMonth = str;
    }

    public void setExpiryYear(String str) {
        this.expiryYear = str;
    }

    public void setSource(String str) {
        this.source = str;
    }

    public void setCardholderName(String str) {
        this.cardholderName = str;
    }

    public void setSecurityCode(String str) {
        this.securityCode = str;
    }

    public void setDataValidUntilTimestamp(String str) {
        this.dataValidUntilTimestamp = str;
    }

    public void setBillingAddress(McBillingAddress mcBillingAddress) {
        this.billingAddress = mcBillingAddress;
    }

    public String getAccountNumber() {
        return this.accountNumber;
    }

    public String getExpiryMonth() {
        return this.expiryMonth;
    }

    public String getExpiryYear() {
        return this.expiryYear;
    }

    public String getSource() {
        return this.source;
    }

    public String getCardholderName() {
        return this.cardholderName;
    }

    public String getSecurityCode() {
        return this.securityCode;
    }

    public McBillingAddress getBillingAddress() {
        return this.billingAddress;
    }

    public String getDataValidUntilTimestamp() {
        return this.dataValidUntilTimestamp;
    }

    public void refactorBillingAddress() {
        if (this.billingAddress != null) {
            this.billingAddress.trimBillingAddress();
        }
    }
}
