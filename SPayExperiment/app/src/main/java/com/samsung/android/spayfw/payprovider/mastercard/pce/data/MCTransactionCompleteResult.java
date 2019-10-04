/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.mastercard.pce.data;

import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionResult;

public class MCTransactionCompleteResult {
    private PaymentType mPaymentType;
    private TerminalType mTerminalType;
    private MCTransactionResult mTransactionError;
    private MCTransactionResult mTransactionResult;

    public MCTransactionCompleteResult(PaymentType paymentType, TerminalType terminalType, MCTransactionResult mCTransactionResult, MCTransactionResult mCTransactionResult2) {
        this.mPaymentType = paymentType;
        this.mTransactionResult = mCTransactionResult;
        this.mTransactionError = mCTransactionResult2;
        this.mTerminalType = terminalType;
    }

    public MCTransactionResult getTransactionError() {
        return this.mTransactionError;
    }

    public MCTransactionResult getTransactionResult() {
        return this.mTransactionResult;
    }

    public boolean isPaymentTypeTapNGo() {
        if (this.mPaymentType == null) {
            return false;
        }
        return PaymentType.TAP_N_GO.equals((Object)this.mPaymentType);
    }

    public boolean isTerminalTapNGo() {
        if (this.mTerminalType == null) {
            return false;
        }
        return TerminalType.TRANSIT.equals((Object)this.mTerminalType);
    }

    public boolean isTransactionComplete() {
        if (this.mTransactionResult == null) {
            return false;
        }
        return MCTransactionResult.TRANSACTION_COMPLETED.equals((Object)this.mTransactionResult);
    }

    public void setTransactionError(MCTransactionResult mCTransactionResult) {
        this.mTransactionError = mCTransactionResult;
    }

    public static final class PaymentType
    extends Enum<PaymentType> {
        private static final /* synthetic */ PaymentType[] $VALUES;
        public static final /* enum */ PaymentType SIMPLE_PAY = new PaymentType();
        public static final /* enum */ PaymentType TAP_N_GO = new PaymentType();

        static {
            PaymentType[] arrpaymentType = new PaymentType[]{SIMPLE_PAY, TAP_N_GO};
            $VALUES = arrpaymentType;
        }

        public static PaymentType valueOf(String string) {
            return (PaymentType)Enum.valueOf(PaymentType.class, (String)string);
        }

        public static PaymentType[] values() {
            return (PaymentType[])$VALUES.clone();
        }
    }

    public static final class TerminalType
    extends Enum<TerminalType> {
        private static final /* synthetic */ TerminalType[] $VALUES;
        public static final /* enum */ TerminalType RETAIL = new TerminalType();
        public static final /* enum */ TerminalType TRANSIT = new TerminalType();

        static {
            TerminalType[] arrterminalType = new TerminalType[]{RETAIL, TRANSIT};
            $VALUES = arrterminalType;
        }

        public static TerminalType valueOf(String string) {
            return (TerminalType)Enum.valueOf(TerminalType.class, (String)string);
        }

        public static TerminalType[] values() {
            return (TerminalType[])$VALUES.clone();
        }
    }

}

