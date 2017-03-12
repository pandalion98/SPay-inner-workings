package com.samsung.android.spayfw.payprovider.mastercard.pce.data;

import com.mastercard.mobile_api.bytes.ByteArray;

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

    public CryptogramType getCryptoGramType() {
        return this.cryptoGramType;
    }

    public void setCryptoGramType(CryptogramType cryptogramType) {
        this.cryptoGramType = cryptogramType;
    }

    public boolean isCVM_Entered() {
        return this.CVM_Entered;
    }

    public void setCVM_Entered(boolean z) {
        this.CVM_Entered = z;
    }

    public ByteArray getAmountAuthorized() {
        return this.amountAuthorized;
    }

    public void setAmountAuthorized(ByteArray byteArray) {
        this.amountAuthorized = byteArray;
    }

    public ByteArray getAmountOther() {
        return this.amountOther;
    }

    public void setAmountOther(ByteArray byteArray) {
        this.amountOther = byteArray;
    }

    public ByteArray getTerminalCountryCode() {
        return this.terminalCountryCode;
    }

    public void setTerminalCountryCode(ByteArray byteArray) {
        this.terminalCountryCode = byteArray;
    }

    public ByteArray getTVR() {
        return this.TVR;
    }

    public void setTVR(ByteArray byteArray) {
        this.TVR = byteArray;
    }

    public ByteArray getTrxCurrencyCode() {
        return this.trxCurrencyCode;
    }

    public void setTrxCurrencyCode(ByteArray byteArray) {
        this.trxCurrencyCode = byteArray;
    }

    public ByteArray getTrxDate() {
        return this.trxDate;
    }

    public void setTrxDate(ByteArray byteArray) {
        this.trxDate = byteArray;
    }

    public ByteArray getTrxType() {
        return this.trxType;
    }

    public void setTrxType(ByteArray byteArray) {
        this.trxType = byteArray;
    }

    public ByteArray getUnpredictableNumber() {
        return this.unpredictableNumber;
    }

    public void setUnpredictableNumber(ByteArray byteArray) {
        this.unpredictableNumber = byteArray;
    }

    public boolean isOnlineAllowed() {
        return this.onlineAllowed;
    }

    public void setOnlineAllowed(boolean z) {
        this.onlineAllowed = z;
    }
}
