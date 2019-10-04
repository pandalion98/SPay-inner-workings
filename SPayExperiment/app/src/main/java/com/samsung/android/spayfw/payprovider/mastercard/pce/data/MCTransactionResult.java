/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.mastercard.pce.data;

public final class MCTransactionResult
extends Enum<MCTransactionResult> {
    private static final /* synthetic */ MCTransactionResult[] $VALUES;
    public static final /* enum */ MCTransactionResult COMMAND_COMPLETED;
    public static final /* enum */ MCTransactionResult COMMAND_COMPLETED_ERROR;
    public static final /* enum */ MCTransactionResult CONTEXT_CONFLICT;
    public static final /* enum */ MCTransactionResult CONTEXT_CONFLICT_AMOUNT;
    public static final /* enum */ MCTransactionResult CONTEXT_CONFLICT_CURRENCY;
    public static final /* enum */ MCTransactionResult CONTEXT_CONFLICT_CVM;
    public static final /* enum */ MCTransactionResult CONTEXT_CONFLICT_MCC;
    public static final /* enum */ MCTransactionResult CONTEXT_CONFLICT_PASS;
    public static final /* enum */ MCTransactionResult ERROR_MST_CANCELED;
    public static final /* enum */ MCTransactionResult ERROR_MST_INTERNAL_ERROR;
    public static final /* enum */ MCTransactionResult ERROR_NFC_COMMAND_CANCELED;
    public static final /* enum */ MCTransactionResult ERROR_NFC_COMMAND_INTERNAL_ERROR;
    public static final /* enum */ MCTransactionResult ERROR_NFC_COMMAND_NOT_SUPPORTED;
    public static final /* enum */ MCTransactionResult ERROR_NFC_COMMAND_RECORD_NOT_FOUND;
    public static final /* enum */ MCTransactionResult ERROR_NFC_COMMAND_UNKNOW_ERROR;
    public static final /* enum */ MCTransactionResult ERROR_NFC_TERMINAL_WRONG_COMMAND_LENGTH;
    public static final /* enum */ MCTransactionResult ERROR_NFC_TERMINAL_WRONG_DATA;
    public static final /* enum */ MCTransactionResult ERROR_NFC_TERMINAL_WRONG_PARAMS;
    public static final /* enum */ MCTransactionResult ERROR_TRANSACTION_CANCELED;
    public static final /* enum */ MCTransactionResult ERROR_WRONG_PROFILE;
    public static final /* enum */ MCTransactionResult MAGSTRIPE_COMPLETED;
    public static final /* enum */ MCTransactionResult MAGSTRIPE_FIRST_TAP;
    public static final /* enum */ MCTransactionResult MCHIP_COMPLETED;
    public static final /* enum */ MCTransactionResult MCHIP_FIRST_TAP;
    public static final /* enum */ MCTransactionResult TRANSACTION_COMPLETED;
    public static final /* enum */ MCTransactionResult TRANSACTION_COMPLETED_ERROR_AAC;
    public static final /* enum */ MCTransactionResult TRANSACTION_COMPLETED_ERROR_CCC_DECLINE;
    public static final /* enum */ MCTransactionResult UNSUPPORTED_TRANSIT;

    static {
        UNSUPPORTED_TRANSIT = new MCTransactionResult();
        CONTEXT_CONFLICT = new MCTransactionResult();
        MAGSTRIPE_COMPLETED = new MCTransactionResult();
        MAGSTRIPE_FIRST_TAP = new MCTransactionResult();
        MCHIP_COMPLETED = new MCTransactionResult();
        MCHIP_FIRST_TAP = new MCTransactionResult();
        ERROR_TRANSACTION_CANCELED = new MCTransactionResult();
        ERROR_WRONG_PROFILE = new MCTransactionResult();
        ERROR_NFC_COMMAND_CANCELED = new MCTransactionResult();
        ERROR_NFC_COMMAND_UNKNOW_ERROR = new MCTransactionResult();
        ERROR_NFC_COMMAND_INTERNAL_ERROR = new MCTransactionResult();
        ERROR_NFC_COMMAND_NOT_SUPPORTED = new MCTransactionResult();
        ERROR_NFC_COMMAND_RECORD_NOT_FOUND = new MCTransactionResult();
        ERROR_NFC_TERMINAL_WRONG_PARAMS = new MCTransactionResult();
        ERROR_NFC_TERMINAL_WRONG_COMMAND_LENGTH = new MCTransactionResult();
        ERROR_NFC_TERMINAL_WRONG_DATA = new MCTransactionResult();
        ERROR_MST_CANCELED = new MCTransactionResult();
        ERROR_MST_INTERNAL_ERROR = new MCTransactionResult();
        COMMAND_COMPLETED = new MCTransactionResult();
        TRANSACTION_COMPLETED = new MCTransactionResult();
        COMMAND_COMPLETED_ERROR = new MCTransactionResult();
        CONTEXT_CONFLICT_AMOUNT = new MCTransactionResult();
        CONTEXT_CONFLICT_CURRENCY = new MCTransactionResult();
        CONTEXT_CONFLICT_MCC = new MCTransactionResult();
        CONTEXT_CONFLICT_CVM = new MCTransactionResult();
        CONTEXT_CONFLICT_PASS = new MCTransactionResult();
        TRANSACTION_COMPLETED_ERROR_AAC = new MCTransactionResult();
        TRANSACTION_COMPLETED_ERROR_CCC_DECLINE = new MCTransactionResult();
        MCTransactionResult[] arrmCTransactionResult = new MCTransactionResult[]{UNSUPPORTED_TRANSIT, CONTEXT_CONFLICT, MAGSTRIPE_COMPLETED, MAGSTRIPE_FIRST_TAP, MCHIP_COMPLETED, MCHIP_FIRST_TAP, ERROR_TRANSACTION_CANCELED, ERROR_WRONG_PROFILE, ERROR_NFC_COMMAND_CANCELED, ERROR_NFC_COMMAND_UNKNOW_ERROR, ERROR_NFC_COMMAND_INTERNAL_ERROR, ERROR_NFC_COMMAND_NOT_SUPPORTED, ERROR_NFC_COMMAND_RECORD_NOT_FOUND, ERROR_NFC_TERMINAL_WRONG_PARAMS, ERROR_NFC_TERMINAL_WRONG_COMMAND_LENGTH, ERROR_NFC_TERMINAL_WRONG_DATA, ERROR_MST_CANCELED, ERROR_MST_INTERNAL_ERROR, COMMAND_COMPLETED, TRANSACTION_COMPLETED, COMMAND_COMPLETED_ERROR, CONTEXT_CONFLICT_AMOUNT, CONTEXT_CONFLICT_CURRENCY, CONTEXT_CONFLICT_MCC, CONTEXT_CONFLICT_CVM, CONTEXT_CONFLICT_PASS, TRANSACTION_COMPLETED_ERROR_AAC, TRANSACTION_COMPLETED_ERROR_CCC_DECLINE};
        $VALUES = arrmCTransactionResult;
    }

    public static MCTransactionResult valueOf(String string) {
        return (MCTransactionResult)Enum.valueOf(MCTransactionResult.class, (String)string);
    }

    public static MCTransactionResult[] values() {
        return (MCTransactionResult[])$VALUES.clone();
    }
}

