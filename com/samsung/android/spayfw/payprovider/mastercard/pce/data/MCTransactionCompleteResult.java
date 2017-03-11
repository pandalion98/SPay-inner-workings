package com.samsung.android.spayfw.payprovider.mastercard.pce.data;

public class MCTransactionCompleteResult {
    private PaymentType mPaymentType;
    private TerminalType mTerminalType;
    private MCTransactionResult mTransactionError;
    private MCTransactionResult mTransactionResult;

    public enum PaymentType {
        SIMPLE_PAY,
        TAP_N_GO
    }

    public enum TerminalType {
        RETAIL,
        TRANSIT
    }

    public MCTransactionCompleteResult(PaymentType paymentType, TerminalType terminalType, MCTransactionResult mCTransactionResult, MCTransactionResult mCTransactionResult2) {
        this.mPaymentType = paymentType;
        this.mTransactionResult = mCTransactionResult;
        this.mTransactionError = mCTransactionResult2;
        this.mTerminalType = terminalType;
    }

    public boolean isTransactionComplete() {
        if (this.mTransactionResult == null) {
            return false;
        }
        return MCTransactionResult.TRANSACTION_COMPLETED.equals(this.mTransactionResult);
    }

    public boolean isTerminalTapNGo() {
        if (this.mTerminalType == null) {
            return false;
        }
        return TerminalType.TRANSIT.equals(this.mTerminalType);
    }

    public boolean isPaymentTypeTapNGo() {
        if (this.mPaymentType == null) {
            return false;
        }
        return PaymentType.TAP_N_GO.equals(this.mPaymentType);
    }

    public MCTransactionResult getTransactionResult() {
        return this.mTransactionResult;
    }

    public MCTransactionResult getTransactionError() {
        return this.mTransactionError;
    }

    public void setTransactionError(MCTransactionResult mCTransactionResult) {
        this.mTransactionError = mCTransactionResult;
    }
}
