/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.mastercard.payload.subpayload;

import com.samsung.android.spayfw.payprovider.mastercard.payload.subpayload.McCardInfoWrapper;

public class McIssuerInitiatedDigitizationData {
    private McCardInfoWrapper cardInfo;
    private String tokenizationAuthenticationValue;

    public McCardInfoWrapper getCardInfo() {
        return this.cardInfo;
    }

    public String getTokenizationAuthenticationValue() {
        return this.tokenizationAuthenticationValue;
    }

    public void setCardInfo(McCardInfoWrapper mcCardInfoWrapper) {
        this.cardInfo = mcCardInfoWrapper;
    }

    public void setTokenizationAuthenticationValue(String string) {
        this.tokenizationAuthenticationValue = string;
    }
}

