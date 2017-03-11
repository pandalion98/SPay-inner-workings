package com.samsung.android.visasdk.facade.data;

public enum TransactionError {
    NO_ERROR(0),
    NO_AUTH_CURRENCY_REQ_NOT_SATISFIED(1),
    NO_AUTH_AMOUNT_REQ_NOT_SATISFIED(2),
    NO_AUTH_TRANSACTION_TYPE_REQ_NOT_SATISFIED(3),
    OTHER_ERROR(4);
    
    private final int error;

    private TransactionError(int i) {
        this.error = i;
    }

    public int getError() {
        return this.error;
    }
}
