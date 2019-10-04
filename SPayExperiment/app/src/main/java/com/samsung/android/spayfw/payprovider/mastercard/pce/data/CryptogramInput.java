/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.samsung.android.spayfw.payprovider.mastercard.pce.data;

import com.mastercard.mobile_api.bytes.ByteArray;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.CryptogramType;

public class CryptogramInput {
    private boolean CVM_Entered;
    private ByteArray TVR;
    private ByteArray amountAuthorized;
    private ByteArray amountOther;
    private CryptogramType cryptoGramType;
    private boolean onlineAllowed;
    private ByteArray terminalCountryCode;
    private ByteArray trxCurrencyCode;
    private ByteArray trxDate;
    private ByteArray trxType;
    private ByteArray unpredictableNumber;

    public ByteArray getAmountAuthorized() {
        return this.amountAuthorized;
    }

    public ByteArray getAmountOther() {
        return this.amountOther;
    }

    public CryptogramType getCryptoGramType() {
        return this.cryptoGramType;
    }

    public ByteArray getTVR() {
        return this.TVR;
    }

    public ByteArray getTerminalCountryCode() {
        return this.terminalCountryCode;
    }

    public ByteArray getTrxCurrencyCode() {
        return this.trxCurrencyCode;
    }

    public ByteArray getTrxDate() {
        return this.trxDate;
    }

    public ByteArray getTrxType() {
        return this.trxType;
    }

    public ByteArray getUnpredictableNumber() {
        return this.unpredictableNumber;
    }

    public boolean isCVM_Entered() {
        return this.CVM_Entered;
    }

    public boolean isOnlineAllowed() {
        return this.onlineAllowed;
    }

    public void setAmountAuthorized(ByteArray byteArray) {
        this.amountAuthorized = byteArray;
    }

    public void setAmountOther(ByteArray byteArray) {
        this.amountOther = byteArray;
    }

    public void setCVM_Entered(boolean bl) {
        this.CVM_Entered = bl;
    }

    public void setCryptoGramType(CryptogramType cryptogramType) {
        this.cryptoGramType = cryptogramType;
    }

    public void setOnlineAllowed(boolean bl) {
        this.onlineAllowed = bl;
    }

    public void setTVR(ByteArray byteArray) {
        this.TVR = byteArray;
    }

    public void setTerminalCountryCode(ByteArray byteArray) {
        this.terminalCountryCode = byteArray;
    }

    public void setTrxCurrencyCode(ByteArray byteArray) {
        this.trxCurrencyCode = byteArray;
    }

    public void setTrxDate(ByteArray byteArray) {
        this.trxDate = byteArray;
    }

    public void setTrxType(ByteArray byteArray) {
        this.trxType = byteArray;
    }

    public void setUnpredictableNumber(ByteArray byteArray) {
        this.unpredictableNumber = byteArray;
    }
}

