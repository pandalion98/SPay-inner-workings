/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.samsung.android.spayfw.payprovider.mastercard.pce.data;

import com.mastercard.mobile_api.bytes.ByteArray;

public class MCFilter {
    protected ByteArray mBlAmount = null;
    protected ByteArray mBlCurrency = null;
    private boolean mBlExactAmount;
    protected ByteArray[] mBlMccList = null;

    public MCFilter(boolean bl, ByteArray byteArray, ByteArray byteArray2, ByteArray[] arrbyteArray) {
        this.mBlExactAmount = bl;
        this.mBlAmount = byteArray;
        this.mBlCurrency = byteArray2;
        this.mBlMccList = arrbyteArray;
    }

    public ByteArray getBlAmount() {
        return this.mBlAmount;
    }

    public ByteArray getBlCurrency() {
        return this.mBlCurrency;
    }

    public ByteArray[] getBlMccList() {
        return this.mBlMccList;
    }

    public boolean isBlExactAmount() {
        return this.mBlExactAmount;
    }

    public void setBlAmount(ByteArray byteArray) {
        this.mBlAmount = byteArray;
    }

    public void setBlCurrencyList(ByteArray byteArray) {
        this.mBlCurrency = byteArray;
    }

    public void setBlExactAmount(boolean bl) {
        this.mBlExactAmount = bl;
    }

    public void setBlMccList(ByteArray[] arrbyteArray) {
        this.mBlMccList = arrbyteArray;
    }
}

