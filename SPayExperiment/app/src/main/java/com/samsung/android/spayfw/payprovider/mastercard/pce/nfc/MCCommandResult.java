/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.mastercard.pce.nfc;

import com.mastercard.mobile_api.bytes.ByteArray;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionResult;

public class MCCommandResult {
    private ByteArray mResponseAPDU;
    private MCTransactionResult mResultCode;
    private TransitionState mTransition;

    public MCCommandResult(ByteArray byteArray, MCTransactionResult mCTransactionResult, TransitionState transitionState) {
        this.mResponseAPDU = byteArray;
        this.mResultCode = mCTransactionResult;
        this.mTransition = transitionState;
    }

    public static MCTransactionResult iso7816ToResponseCode(int n2) {
        switch (n2) {
            default: {
                return MCTransactionResult.ERROR_NFC_COMMAND_UNKNOW_ERROR;
            }
            case 26368: {
                return MCTransactionResult.ERROR_NFC_TERMINAL_WRONG_COMMAND_LENGTH;
            }
            case 27392: {
                return MCTransactionResult.ERROR_NFC_TERMINAL_WRONG_PARAMS;
            }
            case 27011: 
            case 27012: 
            case 27266: 
            case 27268: 
            case 27272: {
                return MCTransactionResult.ERROR_NFC_TERMINAL_WRONG_DATA;
            }
            case 27270: {
                return MCTransactionResult.ERROR_NFC_TERMINAL_WRONG_PARAMS;
            }
            case 27904: {
                return MCTransactionResult.ERROR_NFC_COMMAND_NOT_SUPPORTED;
            }
            case 27013: {
                return MCTransactionResult.ERROR_NFC_COMMAND_INTERNAL_ERROR;
            }
            case 27267: 
        }
        return MCTransactionResult.ERROR_NFC_COMMAND_RECORD_NOT_FOUND;
    }

    public ByteArray getResponseAPDU() {
        return this.mResponseAPDU;
    }

    public MCTransactionResult getResponseCode() {
        return this.mResultCode;
    }

    public TransitionState getTransitionState() {
        return this.mTransition;
    }

    public void setResponseAPDU(ByteArray byteArray) {
        this.mResponseAPDU = byteArray;
    }

    public void setResponseCode(MCTransactionResult mCTransactionResult) {
        this.mResultCode = mCTransactionResult;
    }

    public static final class TransitionState
    extends Enum<TransitionState> {
        private static final /* synthetic */ TransitionState[] $VALUES;
        public static final /* enum */ TransitionState TRANSITION_CANCEL_STATE;
        public static final /* enum */ TransitionState TRANSITION_NEXT_STATE;
        public static final /* enum */ TransitionState TRANSITION_NOT_REQUIRED;

        static {
            TRANSITION_NOT_REQUIRED = new TransitionState();
            TRANSITION_NEXT_STATE = new TransitionState();
            TRANSITION_CANCEL_STATE = new TransitionState();
            TransitionState[] arrtransitionState = new TransitionState[]{TRANSITION_NOT_REQUIRED, TRANSITION_NEXT_STATE, TRANSITION_CANCEL_STATE};
            $VALUES = arrtransitionState;
        }

        public static TransitionState valueOf(String string) {
            return (TransitionState)Enum.valueOf(TransitionState.class, (String)string);
        }

        public static TransitionState[] values() {
            return (TransitionState[])$VALUES.clone();
        }
    }

}

