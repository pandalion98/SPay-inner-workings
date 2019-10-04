/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.visasdk.facade.data;

public final class TransactionError
extends Enum<TransactionError> {
    private static final /* synthetic */ TransactionError[] $VALUES;
    public static final /* enum */ TransactionError NO_AUTH_AMOUNT_REQ_NOT_SATISFIED;
    public static final /* enum */ TransactionError NO_AUTH_CURRENCY_REQ_NOT_SATISFIED;
    public static final /* enum */ TransactionError NO_AUTH_TRANSACTION_TYPE_REQ_NOT_SATISFIED;
    public static final /* enum */ TransactionError NO_ERROR;
    public static final /* enum */ TransactionError OTHER_ERROR;
    private final int error;

    static {
        NO_ERROR = new TransactionError(0);
        NO_AUTH_CURRENCY_REQ_NOT_SATISFIED = new TransactionError(1);
        NO_AUTH_AMOUNT_REQ_NOT_SATISFIED = new TransactionError(2);
        NO_AUTH_TRANSACTION_TYPE_REQ_NOT_SATISFIED = new TransactionError(3);
        OTHER_ERROR = new TransactionError(4);
        TransactionError[] arrtransactionError = new TransactionError[]{NO_ERROR, NO_AUTH_CURRENCY_REQ_NOT_SATISFIED, NO_AUTH_AMOUNT_REQ_NOT_SATISFIED, NO_AUTH_TRANSACTION_TYPE_REQ_NOT_SATISFIED, OTHER_ERROR};
        $VALUES = arrtransactionError;
    }

    private TransactionError(int n3) {
        this.error = n3;
    }

    public static TransactionError valueOf(String string) {
        return (TransactionError)Enum.valueOf(TransactionError.class, (String)string);
    }

    public static TransactionError[] values() {
        return (TransactionError[])$VALUES.clone();
    }

    public int getError() {
        return this.error;
    }
}

