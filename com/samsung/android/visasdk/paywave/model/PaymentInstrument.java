package com.samsung.android.visasdk.paywave.model;

public class PaymentInstrument {
    ExpirationDate expirationDate;
    String last4;

    public void setExpirationDate(ExpirationDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public ExpirationDate getExpirationDate() {
        return this.expirationDate;
    }

    public void setLast4(String str) {
        this.last4 = str;
    }

    public String getLast4() {
        return this.last4;
    }
}
