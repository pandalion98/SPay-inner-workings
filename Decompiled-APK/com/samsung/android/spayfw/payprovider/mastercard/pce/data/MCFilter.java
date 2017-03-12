package com.samsung.android.spayfw.payprovider.mastercard.pce.data;

import com.mastercard.mobile_api.bytes.ByteArray;

public class MCFilter {
    protected ByteArray mBlAmount;
    protected ByteArray mBlCurrency;
    private boolean mBlExactAmount;
    protected ByteArray[] mBlMccList;

    public MCFilter(boolean z, ByteArray byteArray, ByteArray byteArray2, ByteArray[] byteArrayArr) {
        this.mBlAmount = null;
        this.mBlCurrency = null;
        this.mBlMccList = null;
        this.mBlExactAmount = z;
        this.mBlAmount = byteArray;
        this.mBlCurrency = byteArray2;
        this.mBlMccList = byteArrayArr;
    }

    public boolean isBlExactAmount() {
        return this.mBlExactAmount;
    }

    public void setBlExactAmount(boolean z) {
        this.mBlExactAmount = z;
    }

    public ByteArray getBlCurrency() {
        return this.mBlCurrency;
    }

    public void setBlCurrencyList(ByteArray byteArray) {
        this.mBlCurrency = byteArray;
    }

    public ByteArray getBlAmount() {
        return this.mBlAmount;
    }

    public void setBlAmount(ByteArray byteArray) {
        this.mBlAmount = byteArray;
    }

    public ByteArray[] getBlMccList() {
        return this.mBlMccList;
    }

    public void setBlMccList(ByteArray[] byteArrayArr) {
        this.mBlMccList = byteArrayArr;
    }
}
