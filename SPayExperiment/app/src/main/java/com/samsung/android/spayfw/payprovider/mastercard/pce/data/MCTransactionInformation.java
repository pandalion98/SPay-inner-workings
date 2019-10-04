/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.samsung.android.spayfw.payprovider.mastercard.pce.data;

import com.mastercard.mcbp.core.mcbpcards.ContextType;
import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.bytes.ByteArrayFactory;
import com.mastercard.mobile_api.utils.Utils;

public class MCTransactionInformation {
    private ByteArrayFactory baf = ByteArrayFactory.getInstance();
    private ByteArray mAmount = null;
    private byte mCID = 0;
    private ByteArray mCurrencyCode = null;
    private ByteArray mMccCategory = null;
    private ByteArray mMerchantNameAndLoc = null;
    private ByteArray mTransactionDate = null;
    private ByteArray mTransactionType = null;
    private ByteArray mUN = null;
    private ContextType result = null;

    public MCTransactionInformation() {
    }

    public MCTransactionInformation(ByteArray byteArray, ByteArray byteArray2, ByteArray byteArray3, ByteArray byteArray4, ByteArray byteArray5, ByteArray byteArray6, byte by, ContextType contextType) {
        this.mAmount = byteArray2;
        this.mCurrencyCode = byteArray3;
        this.mTransactionDate = byteArray4;
        this.mTransactionType = byteArray5;
        this.mUN = byteArray6;
        this.mCID = by;
        this.result = contextType;
    }

    public MCTransactionInformation(byte[] arrby, byte[] arrby2, byte[] arrby3, byte[] arrby4, byte[] arrby5, byte by, int n2) {
        this.mAmount = this.baf.getByteArray(arrby, arrby.length);
        this.mCurrencyCode = this.baf.getByteArray(arrby2, arrby2.length);
        this.mTransactionDate = this.baf.getByteArray(arrby3, arrby3.length);
        this.mTransactionType = this.baf.getByteArray(arrby4, arrby4.length);
        this.mUN = this.baf.getByteArray(arrby5, arrby5.length);
        this.mCID = by;
        this.setResult(n2);
    }

    public ByteArray getAmount() {
        return this.mAmount;
    }

    public byte getCid() {
        return this.mCID;
    }

    public ByteArray getCurrencyCode() {
        return this.mCurrencyCode;
    }

    public ByteArray getMccCategory() {
        return this.mMccCategory;
    }

    public ByteArray getMerchantNameAndLoc() {
        return this.mMerchantNameAndLoc;
    }

    public ContextType getResult() {
        return this.result;
    }

    public ByteArray getTransactionDate() {
        return this.mTransactionDate;
    }

    public ByteArray getTransactionType() {
        return this.mTransactionType;
    }

    public ByteArray getUN() {
        return this.mUN;
    }

    public void setAmount(ByteArray byteArray) {
        this.mAmount = byteArray;
    }

    public void setCid(byte by) {
        this.mCID = by;
    }

    public void setCurrencyCode(ByteArray byteArray) {
        this.mCurrencyCode = byteArray;
    }

    public void setMccCategory(ByteArray byteArray) {
        this.mMccCategory = byteArray;
    }

    public void setMerchantNameAndLoc(ByteArray byteArray) {
        this.mMerchantNameAndLoc = byteArray;
    }

    public void setResult(int n2) {
        switch (n2) {
            default: {
                this.result = null;
                return;
            }
            case 1: {
                this.result = ContextType.MCHIP_FIRST_TAP;
                return;
            }
            case 2: {
                this.result = ContextType.MCHIP_COMPLETED;
                return;
            }
            case 3: {
                this.result = ContextType.MAGSTRIPE_FIRST_TAP;
                return;
            }
            case 4: {
                this.result = ContextType.MAGSTRIPE_COMPLETED;
                return;
            }
            case 5: {
                this.result = ContextType.CONTEXT_CONFLICT;
                return;
            }
            case 6: 
        }
        this.result = ContextType.UNSUPPORTED_TRANSIT;
    }

    public void setResult(ContextType contextType) {
        this.result = contextType;
    }

    public void setTransactionDate(ByteArray byteArray) {
        this.mTransactionDate = byteArray;
    }

    public void setTransactionType(ByteArray byteArray) {
        this.mTransactionType = byteArray;
    }

    public void setUN(ByteArray byteArray) {
        this.mUN = byteArray;
    }

    public void wipe() {
        Utils.clearByteArray(this.mAmount);
        Utils.clearByteArray(this.mCurrencyCode);
        Utils.clearByteArray(this.mTransactionDate);
        Utils.clearByteArray(this.mTransactionType);
        Utils.clearByteArray(this.mUN);
        Utils.clearByteArray(this.mMccCategory);
        Utils.clearByteArray(this.mMerchantNameAndLoc);
        this.result = null;
    }
}

