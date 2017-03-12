package com.samsung.android.spayfw.payprovider.mastercard.pce;

public class MCTransactionException extends MTBPPaymentException {
    private static final long serialVersionUID = 1;
    private Object mResponse;

    public MCTransactionException(Object obj) {
        super("Payment transaction exception");
        this.mResponse = obj;
    }

    public Object getResponse() {
        return this.mResponse;
    }
}
