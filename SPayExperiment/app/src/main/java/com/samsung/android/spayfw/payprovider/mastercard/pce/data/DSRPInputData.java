/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.samsung.android.spayfw.payprovider.mastercard.pce.data;

import com.mastercard.mobile_api.utils.Date;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.CryptogramType;

public class DSRPInputData {
    private int countryCode;
    private CryptogramType cryptogramType;
    private long currencyCode;
    private long otherAmount;
    private long transactionAmount;
    private Date transactionDate;
    private byte transactionType;
    private long unpredictableNumber;

    public int getCountryCode() {
        return this.countryCode;
    }

    public CryptogramType getCryptogramType() {
        return this.cryptogramType;
    }

    public byte getCryptogramTypeAsByte() {
        if (this.cryptogramType == CryptogramType.UCAF) {
            return 1;
        }
        if (this.cryptogramType == CryptogramType.DE55) {
            return 2;
        }
        return 0;
    }

    public long getCurrencyCode() {
        return this.currencyCode;
    }

    public byte getDateDay() {
        return (byte)this.transactionDate.getDay();
    }

    public byte getDateMonth() {
        return (byte)this.transactionDate.getMonth();
    }

    public char getDateYear() {
        return (char)this.transactionDate.getYear();
    }

    public long getOtherAmount() {
        return this.otherAmount;
    }

    public long getTransactionAmount() {
        return this.transactionAmount;
    }

    public Date getTransactionDate() {
        return this.transactionDate;
    }

    public byte getTransactionType() {
        return this.transactionType;
    }

    public long getUnpredictableNumber() {
        return this.unpredictableNumber;
    }

    public void setCountryCode(int n2) {
        this.countryCode = n2;
    }

    public void setCryptogramType(CryptogramType cryptogramType) {
        this.cryptogramType = cryptogramType;
    }

    public void setCurrencyCode(long l2) {
        this.currencyCode = l2;
    }

    public void setOtherAmount(long l2) {
        this.otherAmount = l2;
    }

    public void setTransactionAmount(long l2) {
        this.transactionAmount = l2;
    }

    public void setTransactionDate(Date date) {
        this.transactionDate = date;
    }

    public void setTransactionType(byte by) {
        this.transactionType = by;
    }

    public void setUnpredictableNumber(long l2) {
        this.unpredictableNumber = l2;
    }
}

