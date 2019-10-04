/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 */
package com.samsung.android.spayfw.payprovider.amex.models;

public class AmexTransactionData {
    private String displayUntil;
    private String merchantName;
    private String merchantZipCode;
    private String tokenRefId;
    private String transactionAmount;
    private String transactionCurrency;
    private String transactionDetailUrl;
    private String transactionIdentifier;
    private String transactionStatus;
    private String transactionTimestamp;
    private String transactionType;

    public String getDisplayUntil() {
        return this.displayUntil;
    }

    public String getMerchantName() {
        return this.merchantName;
    }

    public String getMerchantZipCode() {
        return this.merchantZipCode;
    }

    public String getTokenRefId() {
        return this.tokenRefId;
    }

    public String getTransactionAmount() {
        return this.transactionAmount;
    }

    public String getTransactionCurrency() {
        return this.transactionCurrency;
    }

    public String getTransactionDetailUrl() {
        return this.transactionDetailUrl;
    }

    public String getTransactionIdentifier() {
        return this.transactionIdentifier;
    }

    public String getTransactionStatus() {
        return this.transactionStatus;
    }

    public String getTransactionTimestamp() {
        return this.transactionTimestamp;
    }

    public String getTransactionType() {
        return this.transactionType;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("AmexTransactionData [displayUntil=").append(this.displayUntil).append(", merchantName=").append(this.merchantName).append(", merchantZipCode=").append(this.merchantZipCode).append(", tokenRefId=").append(this.tokenRefId).append(", transactionAmount=").append(this.transactionAmount).append(", transactionCurrency=").append(this.transactionCurrency).append(", transactionDetailUrl=").append(this.transactionDetailUrl).append(", transactionIdentifier=").append(this.transactionIdentifier).append(", transactionStatus=").append(this.transactionStatus).append(", transactionTimestamp=").append(this.transactionTimestamp).append(", transactionType=").append(this.transactionType).append("]");
        return stringBuilder.toString();
    }
}

