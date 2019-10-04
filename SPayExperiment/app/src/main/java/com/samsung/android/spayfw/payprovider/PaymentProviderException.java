/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 */
package com.samsung.android.spayfw.payprovider;

public class PaymentProviderException
extends Exception {
    private int errorCode;

    public PaymentProviderException(int n2) {
        this.errorCode = n2;
    }

    public int getErrorCode() {
        return this.errorCode;
    }
}

