/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.mastercard.pce;

import com.samsung.android.spayfw.payprovider.mastercard.pce.MTBPPaymentException;

public class MCTransactionException
extends MTBPPaymentException {
    private static final long serialVersionUID = 1L;
    private Object mResponse;

    public MCTransactionException(Object object) {
        super("Payment transaction exception");
        this.mResponse = object;
    }

    public Object getResponse() {
        return this.mResponse;
    }
}

