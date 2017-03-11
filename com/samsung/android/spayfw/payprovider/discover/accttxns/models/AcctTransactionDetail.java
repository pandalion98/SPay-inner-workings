package com.samsung.android.spayfw.payprovider.discover.accttxns.models;

public class AcctTransactionDetail {
    String amount;
    String authorizationStatus;
    String currencyCode;
    String mccCode;
    String mccDescription;
    String merchantCity;
    String merchantCountry;
    String merchantName;
    String merchantPostalCode;
    String merchantState;
    String rawMerchantName;
    String txnExpiryTimestamp;
    String txnTimestamp;
    String txnType;

    public String getAuthorizationStatus() {
        return this.authorizationStatus;
    }

    public String getTransactionType() {
        return this.txnType;
    }

    public String getAmount() {
        return this.amount;
    }

    public String getCurrencyCode() {
        return this.currencyCode;
    }

    public String getTransactionTimestamp() {
        return this.txnTimestamp;
    }

    public String getTransactionExpiryTimestamp() {
        return this.txnExpiryTimestamp;
    }

    public String getRawMerchantName() {
        return this.rawMerchantName;
    }

    public String getMerchantName() {
        return this.merchantName;
    }

    public String getMccCode() {
        return this.mccCode;
    }

    public String getMccDescription() {
        return this.mccDescription;
    }

    public String getMerchantPostalCode() {
        return this.merchantPostalCode;
    }

    public String getMerchantCity() {
        return this.merchantCity;
    }

    public String getMerchantState() {
        return this.merchantState;
    }

    public String getMerchantCountry() {
        return this.merchantCountry;
    }
}
