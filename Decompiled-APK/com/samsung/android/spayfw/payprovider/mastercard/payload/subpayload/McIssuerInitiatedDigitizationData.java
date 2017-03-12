package com.samsung.android.spayfw.payprovider.mastercard.payload.subpayload;

public class McIssuerInitiatedDigitizationData {
    private McCardInfoWrapper cardInfo;
    private String tokenizationAuthenticationValue;

    public void setCardInfo(McCardInfoWrapper mcCardInfoWrapper) {
        this.cardInfo = mcCardInfoWrapper;
    }

    public void setTokenizationAuthenticationValue(String str) {
        this.tokenizationAuthenticationValue = str;
    }

    public McCardInfoWrapper getCardInfo() {
        return this.cardInfo;
    }

    public String getTokenizationAuthenticationValue() {
        return this.tokenizationAuthenticationValue;
    }
}
