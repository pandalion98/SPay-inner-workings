package com.samsung.android.visasdk.paywave.model;

public class ProvisionedData {
    private PaymentInstrument paymentInstrument;
    private TokenInfo tokenInfo;

    public PaymentInstrument getPaymentInstrument() {
        return this.paymentInstrument;
    }

    public TokenInfo getTokenInfo() {
        return this.tokenInfo;
    }

    public void setPaymentInstrument(PaymentInstrument paymentInstrument) {
        this.paymentInstrument = paymentInstrument;
    }

    public void setTokenInfo(TokenInfo tokenInfo) {
        this.tokenInfo = tokenInfo;
    }
}
