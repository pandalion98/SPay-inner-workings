package com.samsung.android.spayfw.payprovider.mastercard.pce.nfc;

import com.mastercard.mobile_api.bytes.ByteArray;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionResult;

public class MCCommandResult {
    private ByteArray mResponseAPDU;
    private MCTransactionResult mResultCode;
    private TransitionState mTransition;

    public enum TransitionState {
        TRANSITION_NOT_REQUIRED,
        TRANSITION_NEXT_STATE,
        TRANSITION_CANCEL_STATE
    }

    public MCCommandResult(ByteArray byteArray, MCTransactionResult mCTransactionResult, TransitionState transitionState) {
        this.mResponseAPDU = byteArray;
        this.mResultCode = mCTransactionResult;
        this.mTransition = transitionState;
    }

    public void setResponseCode(MCTransactionResult mCTransactionResult) {
        this.mResultCode = mCTransactionResult;
    }

    public MCTransactionResult getResponseCode() {
        return this.mResultCode;
    }

    public void setResponseAPDU(ByteArray byteArray) {
        this.mResponseAPDU = byteArray;
    }

    public ByteArray getResponseAPDU() {
        return this.mResponseAPDU;
    }

    public TransitionState getTransitionState() {
        return this.mTransition;
    }

    public static MCTransactionResult iso7816ToResponseCode(int i) {
        switch (i) {
            case 26368:
                return MCTransactionResult.ERROR_NFC_TERMINAL_WRONG_COMMAND_LENGTH;
            case 27011:
            case 27012:
            case 27266:
            case 27268:
            case 27272:
                return MCTransactionResult.ERROR_NFC_TERMINAL_WRONG_DATA;
            case 27013:
                return MCTransactionResult.ERROR_NFC_COMMAND_INTERNAL_ERROR;
            case 27267:
                return MCTransactionResult.ERROR_NFC_COMMAND_RECORD_NOT_FOUND;
            case 27270:
                return MCTransactionResult.ERROR_NFC_TERMINAL_WRONG_PARAMS;
            case 27392:
                return MCTransactionResult.ERROR_NFC_TERMINAL_WRONG_PARAMS;
            case 27904:
                return MCTransactionResult.ERROR_NFC_COMMAND_NOT_SUPPORTED;
            default:
                return MCTransactionResult.ERROR_NFC_COMMAND_UNKNOW_ERROR;
        }
    }
}
