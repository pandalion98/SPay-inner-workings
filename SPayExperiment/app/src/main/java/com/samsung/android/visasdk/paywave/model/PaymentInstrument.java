/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.visasdk.paywave.model;

import com.samsung.android.visasdk.paywave.model.ExpirationDate;

public class PaymentInstrument {
    ExpirationDate expirationDate;
    String last4;

    public ExpirationDate getExpirationDate() {
        return this.expirationDate;
    }

    public String getLast4() {
        return this.last4;
    }

    public void setExpirationDate(ExpirationDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public void setLast4(String string) {
        this.last4 = string;
    }
}

