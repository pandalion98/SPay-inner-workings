/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.mastercard.mcbp.core.mcbpcards.profile;

import com.mastercard.mcbp.core.mcbpcards.profile.CardholderValidators;
import com.mastercard.mcbp.core.mcbpcards.profile.MChipCVM_IssuerOptions;
import com.mastercard.mcbp.core.mcbpcards.profile.MagstripeCVM_IssuerOptions;
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

    public ByteArray getApplicationLifeCycleData() {
        return this.applicationLifeCycleData;
    }

    public int getCVM_ResetTimeout() {
        return this.CVM_ResetTimeout;
    }

    public ByteArray getCardLayoutDescription() {
        return this.cardLayoutDescription;
    }

    public CardholderValidators getCardholderValidators() {
        return this.cardholderValidators;
    }

    public int getDualTapResetTimeout() {
        return this.dualTapResetTimeout;
    }

    public MagstripeCVM_IssuerOptions getMagstripeCVM_IssuerOptions() {
        return this.magstripeCVM_IssuerOptions;
    }

    public ByteArray getSecurityWord() {
        return this.securityWord;
    }

    public MChipCVM_IssuerOptions getmChipCVM_IssuerOptions() {
        return this.mChipCVM_IssuerOptions;
    }

    public void setApplicationLifeCycleData(ByteArray byteArray) {
        this.applicationLifeCycleData = byteArray;
    }

    public void setCVM_ResetTimeout(int n2) {
        this.CVM_ResetTimeout = n2;
    }

    public void setCardLayoutDescription(ByteArray byteArray) {
        this.cardLayoutDescription = byteArray;
    }

    public void setCardholderValidators(CardholderValidators cardholderValidators) {
        this.cardholderValidators = cardholderValidators;
    }

    public void setDualTapResetTimeout(int n2) {
        this.dualTapResetTimeout = n2;
    }

    public void setMagstripeCVM_IssuerOptions(MagstripeCVM_IssuerOptions magstripeCVM_IssuerOptions) {
        this.magstripeCVM_IssuerOptions = magstripeCVM_IssuerOptions;
    }

    public void setSecurityWord(ByteArray byteArray) {
        this.securityWord = byteArray;
    }

    public void setmChipCVM_IssuerOptions(MChipCVM_IssuerOptions mChipCVM_IssuerOptions) {
        this.mChipCVM_IssuerOptions = mChipCVM_IssuerOptions;
    }

    public void wipe() {
        Utils.clearByteArray(this.applicationLifeCycleData);
        Utils.clearByteArray(this.cardLayoutDescription);
        Utils.clearByteArray(this.securityWord);
    }
}

