package com.mastercard.mcbp.core.mcbpcards.profile;

import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.utils.Utils;

public class DC_CP_BL {
    private int CVM_ResetTimeout;
    private ByteArray applicationLifeCycleData;
    private ByteArray cardLayoutDescription;
    private CardholderValidators cardholderValidators;
    private int dualTapResetTimeout;
    private MChipCVM_IssuerOptions mChipCVM_IssuerOptions;
    private MagstripeCVM_IssuerOptions magstripeCVM_IssuerOptions;
    private ByteArray securityWord;

    public ByteArray getSecurityWord() {
        return this.securityWord;
    }

    public void setSecurityWord(ByteArray byteArray) {
        this.securityWord = byteArray;
    }

    public int getDualTapResetTimeout() {
        return this.dualTapResetTimeout;
    }

    public void setDualTapResetTimeout(int i) {
        this.dualTapResetTimeout = i;
    }

    public MChipCVM_IssuerOptions getmChipCVM_IssuerOptions() {
        return this.mChipCVM_IssuerOptions;
    }

    public void setmChipCVM_IssuerOptions(MChipCVM_IssuerOptions mChipCVM_IssuerOptions) {
        this.mChipCVM_IssuerOptions = mChipCVM_IssuerOptions;
    }

    public ByteArray getApplicationLifeCycleData() {
        return this.applicationLifeCycleData;
    }

    public void setApplicationLifeCycleData(ByteArray byteArray) {
        this.applicationLifeCycleData = byteArray;
    }

    public CardholderValidators getCardholderValidators() {
        return this.cardholderValidators;
    }

    public void setCardholderValidators(CardholderValidators cardholderValidators) {
        this.cardholderValidators = cardholderValidators;
    }

    public int getCVM_ResetTimeout() {
        return this.CVM_ResetTimeout;
    }

    public void setCVM_ResetTimeout(int i) {
        this.CVM_ResetTimeout = i;
    }

    public ByteArray getCardLayoutDescription() {
        return this.cardLayoutDescription;
    }

    public void setCardLayoutDescription(ByteArray byteArray) {
        this.cardLayoutDescription = byteArray;
    }

    public MagstripeCVM_IssuerOptions getMagstripeCVM_IssuerOptions() {
        return this.magstripeCVM_IssuerOptions;
    }

    public void setMagstripeCVM_IssuerOptions(MagstripeCVM_IssuerOptions magstripeCVM_IssuerOptions) {
        this.magstripeCVM_IssuerOptions = magstripeCVM_IssuerOptions;
    }

    public void wipe() {
        Utils.clearByteArray(this.applicationLifeCycleData);
        Utils.clearByteArray(this.cardLayoutDescription);
        Utils.clearByteArray(this.securityWord);
    }
}
