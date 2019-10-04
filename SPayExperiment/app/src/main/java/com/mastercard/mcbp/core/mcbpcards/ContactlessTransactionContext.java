/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.mastercard.mcbp.core.mcbpcards;

import com.mastercard.mcbp.core.mcbpcards.ContextType;
import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.bytes.ByteArrayFactory;
import com.mastercard.mobile_api.utils.Utils;

public class ContactlessTransactionContext {
    private ByteArray ATC;
    private ByteArray UN;
    private ByteArray amount;
    private ByteArrayFactory baf = ByteArrayFactory.getInstance();
    private byte cid;
    private ByteArray cryptoGram;
    private ByteArray currencyCode;
    private ContextType result;
    private ByteArray trxDate;
    private ByteArray trxType;

    public ContactlessTransactionContext() {
        this.ATC = null;
        this.amount = null;
        this.currencyCode = null;
        this.trxDate = null;
        this.trxType = null;
        this.UN = null;
        this.cryptoGram = null;
        this.cid = 0;
        this.result = null;
    }

    public ContactlessTransactionContext(ByteArray byteArray, ByteArray byteArray2, ByteArray byteArray3, ByteArray byteArray4, ByteArray byteArray5, ByteArray byteArray6, ByteArray byteArray7, byte by, ContextType contextType) {
        this.ATC = byteArray;
        this.amount = byteArray2;
        this.currencyCode = byteArray3;
        this.trxDate = byteArray4;
        this.trxType = byteArray5;
        this.UN = byteArray6;
        this.cryptoGram = byteArray7;
        this.cid = by;
        this.result = contextType;
    }

    public ContactlessTransactionContext(byte[] arrby, byte[] arrby2, byte[] arrby3, byte[] arrby4, byte[] arrby5, byte[] arrby6, byte[] arrby7, byte by, int n2) {
        this.ATC = this.baf.getByteArray(arrby, arrby.length);
        this.amount = this.baf.getByteArray(arrby2, arrby2.length);
        this.currencyCode = this.baf.getByteArray(arrby3, arrby3.length);
        this.trxDate = this.baf.getByteArray(arrby4, arrby4.length);
        this.trxType = this.baf.getByteArray(arrby5, arrby5.length);
        this.UN = this.baf.getByteArray(arrby6, arrby6.length);
        this.cryptoGram = this.baf.getByteArray(arrby7, arrby7.length);
        this.cid = by;
        this.setResult(n2);
    }

    public ByteArray getATC() {
        return this.ATC;
    }

    public ByteArray getAmount() {
        return this.amount;
    }

    public byte getCid() {
        return this.cid;
    }

    public ByteArray getCryptoGram() {
        return this.cryptoGram;
    }

    public ByteArray getCurrencyCode() {
        return this.currencyCode;
    }

    public ContextType getResult() {
        return this.result;
    }

    public ByteArray getTrxDate() {
        return this.trxDate;
    }

    public ByteArray getTrxType() {
        return this.trxType;
    }

    public ByteArray getUN() {
        return this.UN;
    }

    public void setATC(ByteArray byteArray) {
        this.ATC = byteArray;
    }

    public void setAmount(ByteArray byteArray) {
        this.amount = byteArray;
    }

    public void setCid(byte by) {
        this.cid = by;
    }

    public void setCryptoGram(ByteArray byteArray) {
        this.cryptoGram = byteArray;
    }

    public void setCurrencyCode(ByteArray byteArray) {
        this.currencyCode = byteArray;
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

    public void setTrxDate(ByteArray byteArray) {
        this.trxDate = byteArray;
    }

    public void setTrxType(ByteArray byteArray) {
        this.trxType = byteArray;
    }

    public void setUN(ByteArray byteArray) {
        this.UN = byteArray;
    }

    public void wipe() {
        Utils.clearByteArray(this.ATC);
        Utils.clearByteArray(this.amount);
        Utils.clearByteArray(this.currencyCode);
        Utils.clearByteArray(this.trxDate);
        Utils.clearByteArray(this.trxType);
        Utils.clearByteArray(this.UN);
        Utils.clearByteArray(this.cryptoGram);
        this.result = null;
    }
}

