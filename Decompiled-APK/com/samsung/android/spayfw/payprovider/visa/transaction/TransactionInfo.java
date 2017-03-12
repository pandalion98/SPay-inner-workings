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

    public String getVProvisionedTokenID() {
        return this.vProvisionedTokenID;
    }

    public void setVProvisionedTokenID(String str) {
        this.vProvisionedTokenID = str;
    }

    public String getTransactionID() {
        return this.transactionID;
    }

    public void setTransactionID(String str) {
        this.transactionID = str;
    }

    public String getTransactionType() {
        return this.transactionType;
    }

    public void setTransactionType(String str) {
        this.transactionType = str;
    }

    public String getTransactionDate() {
        return this.transactionDate;
    }

    public void setTransactionDate(String str) {
        this.transactionDate = str;
    }

    public String getCurrencyCode() {
        return this.currencyCode;
    }

    public void setCurrencyCode(String str) {
        this.currencyCode = str;
    }

    public String getMerchantName() {
        return this.merchantName;
    }

    public void setMerchantName(String str) {
        this.merchantName = str;
    }

    public String getAmount() {
        return this.amount;
    }

    public void setAmount(String str) {
        this.amount = str;
    }

    public String getTransactionStatus() {
        return this.transactionStatus;
    }

    public void setTransactionStatus(String str) {
        this.transactionStatus = str;
    }

    public Integer getIndustryCatgCode() {
        return this.industryCatgCode;
    }

    public void setIndustryCatgCode(Integer num) {
        this.industryCatgCode = num;
    }

    public String getIndustryCatgName() {
        return this.industryCatgName;
    }

    public void setIndustryCatgName(String str) {
        this.industryCatgName = str;
    }

    public Integer getIndustryCode() {
        return this.industryCode;
    }

    public void setIndustryCode(Integer num) {
        this.industryCode = num;
    }

    public String getIndustryName() {
        return this.industryName;
    }

    public void setIndustryName(String str) {
        this.industryName = str;
    }

    public String toString() {
        return "TransactionInfo [vProvisionedTokenID=" + this.vProvisionedTokenID + ", transactionID=" + this.transactionID + ", transactionType=" + this.transactionType + ", transactionDate=" + this.transactionDate + ", currencyCode=" + this.currencyCode + ", merchantName=" + this.merchantName + ", amount=" + this.amount + ", transactionStatus=" + this.transactionStatus + ", industryCatgCode=" + this.industryCatgCode + ", industryCatgName=" + this.industryCatgName + ", industryCode=" + this.industryCode + ", industryName=" + this.industryName + "]";
    }
}
