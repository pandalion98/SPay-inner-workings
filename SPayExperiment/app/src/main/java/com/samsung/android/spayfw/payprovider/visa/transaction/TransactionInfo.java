/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.visa.transaction;

public class TransactionInfo {
    public static final String VISA_TRANSACTIONSTATUS_APPROVED = "1";
    public static final String VISA_TRANSACTIONSTATUS_DECLINED = "3";
    public static final String VISA_TRANSACTIONSTATUS_PENDING = "0";
    public static final String VISA_TRANSACTIONSTATUS_REFUNDED = "2";
    public static final String VISA_TRANSACTIONTYPE_PURCHASE = "0";
    public static final String VISA_TRANSACTIONTYPE_REFUND = "1";
    private String amount;
    private String currencyCode;
    private Integer industryCatgCode;
    private String industryCatgName;
    private Integer industryCode;
    private String industryName;
    private String merchantName;
    private String transactionDate;
    private String transactionID;
    private String transactionStatus;
    private String transactionType;
    private String vProvisionedTokenID;

    public String getAmount() {
        return this.amount;
    }

    public String getCurrencyCode() {
        return this.currencyCode;
    }

    public Integer getIndustryCatgCode() {
        return this.industryCatgCode;
    }

    public String getIndustryCatgName() {
        return this.industryCatgName;
    }

    public Integer getIndustryCode() {
        return this.industryCode;
    }

    public String getIndustryName() {
        return this.industryName;
    }

    public String getMerchantName() {
        return this.merchantName;
    }

    public String getTransactionDate() {
        return this.transactionDate;
    }

    public String getTransactionID() {
        return this.transactionID;
    }

    public String getTransactionStatus() {
        return this.transactionStatus;
    }

    public String getTransactionType() {
        return this.transactionType;
    }

    public String getVProvisionedTokenID() {
        return this.vProvisionedTokenID;
    }

    public void setAmount(String string) {
        this.amount = string;
    }

    public void setCurrencyCode(String string) {
        this.currencyCode = string;
    }

    public void setIndustryCatgCode(Integer n2) {
        this.industryCatgCode = n2;
    }

    public void setIndustryCatgName(String string) {
        this.industryCatgName = string;
    }

    public void setIndustryCode(Integer n2) {
        this.industryCode = n2;
    }

    public void setIndustryName(String string) {
        this.industryName = string;
    }

    public void setMerchantName(String string) {
        this.merchantName = string;
    }

    public void setTransactionDate(String string) {
        this.transactionDate = string;
    }

    public void setTransactionID(String string) {
        this.transactionID = string;
    }

    public void setTransactionStatus(String string) {
        this.transactionStatus = string;
    }

    public void setTransactionType(String string) {
        this.transactionType = string;
    }

    public void setVProvisionedTokenID(String string) {
        this.vProvisionedTokenID = string;
    }

    public String toString() {
        return "TransactionInfo [vProvisionedTokenID=" + this.vProvisionedTokenID + ", transactionID=" + this.transactionID + ", transactionType=" + this.transactionType + ", transactionDate=" + this.transactionDate + ", currencyCode=" + this.currencyCode + ", merchantName=" + this.merchantName + ", amount=" + this.amount + ", transactionStatus=" + this.transactionStatus + ", industryCatgCode=" + (Object)this.industryCatgCode + ", industryCatgName=" + this.industryCatgName + ", industryCode=" + (Object)this.industryCode + ", industryName=" + this.industryName + "]";
    }
}

