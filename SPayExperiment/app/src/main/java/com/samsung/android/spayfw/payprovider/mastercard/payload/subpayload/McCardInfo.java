/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.mastercard.payload.subpayload;

import com.samsung.android.spayfw.payprovider.mastercard.payload.subpayload.McBillingAddress;

public class McCardInfo {
    private String accountNumber;
    private McBillingAddress billingAddress;
    private String cardholderName;
    private String dataValidUntilTimestamp;
    private String expiryMonth;
    private String expiryYear;
    private String securityCode;
    private String source;

    public String getAccountNumber() {
        return this.accountNumber;
    }

    public McBillingAddress getBillingAddress() {
        return this.billingAddress;
    }

    public String getCardholderName() {
        return this.cardholderName;
    }

    public String getDataValidUntilTimestamp() {
        return this.dataValidUntilTimestamp;
    }

    public String getExpiryMonth() {
        return this.expiryMonth;
    }

    public String getExpiryYear() {
        return this.expiryYear;
    }

    public String getSecurityCode() {
        return this.securityCode;
    }

    public String getSource() {
        return this.source;
    }

    public void refactorBillingAddress() {
        if (this.billingAddress != null) {
            this.billingAddress.trimBillingAddress();
        }
    }

    public void setAccountNumber(String string) {
        this.accountNumber = string;
    }

    public void setBillingAddress(McBillingAddress mcBillingAddress) {
        this.billingAddress = mcBillingAddress;
    }

    public void setCardholderName(String string) {
        this.cardholderName = string;
    }

    public void setDataValidUntilTimestamp(String string) {
        this.dataValidUntilTimestamp = string;
    }

    public void setExpiryMonth(String string) {
        this.expiryMonth = string;
    }

    public void setExpiryYear(String string) {
        this.expiryYear = string;
    }

    public void setSecurityCode(String string) {
        this.securityCode = string;
    }

    public void setSource(String string) {
        this.source = string;
    }
}

