package com.samsung.android.spayfw.payprovider.mastercard.pce.data;

import com.mastercard.mobile_api.utils.Date;

public class DSRPInputData {
    private int countryCode;
    private CryptogramType cryptogramType;
    private long currencyCode;
    private long otherAmount;
    private long transactionAmount;
    private Date transactionDate;
    private byte transactionType;
    private long unpredictableNumber;

    public long getTransactionAmount() {
        return this.transactionAmount;
    }

    public void setTransactionAmount(long j) {
        this.transactionAmount = j;
    }

    public long getOtherAmount() {
        return this.otherAmount;
    }

    public void setOtherAmount(long j) {
        this.otherAmount = j;
    }

    public long getCurrencyCode() {
        return this.currencyCode;
    }

    public void setCurrencyCode(long j) {
        this.currencyCode = j;
    }

    public byte getTransactionType() {
        return this.transactionType;
    }

    public void setTransactionType(byte b) {
        this.transactionType = b;
    }

    public long getUnpredictableNumber() {
        return this.unpredictableNumber;
    }

    public void setUnpredictableNumber(long j) {
        this.unpredictableNumber = j;
    }

    public CryptogramType getCryptogramType() {
        return this.cryptogramType;
    }

    public void setCryptogramType(CryptogramType cryptogramType) {
        this.cryptogramType = cryptogramType;
    }

    public byte getCryptogramTypeAsByte() {
        if (this.cryptogramType == CryptogramType.UCAF) {
            return (byte) 1;
        }
        if (this.cryptogramType == CryptogramType.DE55) {
            return (byte) 2;
        }
        return (byte) 0;
    }

    public Date getTransactionDate() {
        return this.transactionDate;
    }

    public void setTransactionDate(Date date) {
        this.transactionDate = date;
    }

    public byte getDateDay() {
        return (byte) this.transactionDate.getDay();
    }

    public byte getDateMonth() {
        return (byte) this.transactionDate.getMonth();
    }

    public char getDateYear() {
        return (char) this.transactionDate.getYear();
    }

    public int getCountryCode() {
        return this.countryCode;
    }

    public void setCountryCode(int i) {
        this.countryCode = i;
    }
}
