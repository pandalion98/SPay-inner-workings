package com.samsung.android.spayfw.payprovider.mastercard.pce.nfc;

import com.mastercard.mcbp.core.mcbpcards.profile.DC_CP;
import com.mastercard.mcbp.core.mcbpcards.profile.DC_CP_MPP;
import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.bytes.ByteArrayFactory;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.mastercard.pce.context.MTBPTransactionContext;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionResult;
import com.samsung.android.spayfw.payprovider.mastercard.pce.nfc.MCCommandResult.TransitionState;

public abstract class MCCAPDUBaseCommandHandler {
    protected static final String TAG = "mcpce_MCCAPDUBaseCommandHandler";
    protected final ByteArrayFactory baf;
    private MTBPTransactionContext mContext;
    protected DC_CP_MPP mPaymentProfile;

    public abstract boolean checkCLA(byte b);

    protected abstract MCCommandResult checkP1P2Parameters(byte b, byte b2);

    protected abstract MCCommandResult generateResponseAPDU();

    protected abstract MCCommandResult processCommand(ByteArray byteArray);

    protected MCCAPDUBaseCommandHandler() {
        this.baf = ByteArrayFactory.getInstance();
    }

    public void setTransactionContext(DC_CP dc_cp, MTBPTransactionContext mTBPTransactionContext) {
        this.mContext = mTBPTransactionContext;
        this.mPaymentProfile = dc_cp.getDC_CP_MPP();
    }

    public MCCommandResult processAPDU(ByteArray byteArray) {
        if (byteArray == null || byteArray.getLength() == 0) {
            Log.m286e(TAG, "Empty apdu. Return error code 27013");
            return ERROR(27013);
        }
        Log.m287i(TAG, "apdu:" + byteArray.getHexString());
        Log.m285d(TAG, "Check APDU parameters.");
        MCCommandResult checkP1P2Parameters = checkP1P2Parameters(byteArray.getByte(2), byteArray.getByte(3));
        if (checkP1P2Parameters != null) {
            Log.m285d(TAG, "checkP1P2Parameters response result: " + checkP1P2Parameters.getResponseCode());
        }
        if (MCTransactionResult.COMMAND_COMPLETED.equals(checkP1P2Parameters.getResponseCode())) {
            Log.m285d(TAG, "Process APDU.");
            checkP1P2Parameters = processCommand(byteArray);
            if (MCTransactionResult.COMMAND_COMPLETED.equals(checkP1P2Parameters.getResponseCode())) {
                Log.m287i(TAG, "Generate APDU response.");
                return generateResponseAPDU();
            } else if (MCTransactionResult.TRANSACTION_COMPLETED.equals(checkP1P2Parameters.getResponseCode())) {
                Log.m287i(TAG, "ProccessAPDU Transaction completed  " + checkP1P2Parameters.getResponseCode());
                return checkP1P2Parameters;
            } else {
                Log.m286e(TAG, "ProccessAPDU execution error, error code " + checkP1P2Parameters.getResponseCode());
                return checkP1P2Parameters;
            }
        }
        Log.m286e(TAG, "checkP1P2Parameters execution error, error code " + checkP1P2Parameters.getResponseCode());
        return checkP1P2Parameters;
    }

    protected MCCommandResult ERROR(int i) {
        return new MCCommandResult(this.baf.getFromWord(i), MCCommandResult.iso7816ToResponseCode(i), TransitionState.TRANSITION_CANCEL_STATE);
    }

    protected MCCommandResult completeCommand() {
        return new MCCommandResult(null, MCTransactionResult.COMMAND_COMPLETED, TransitionState.TRANSITION_NOT_REQUIRED);
    }

    protected MCCommandResult completeCommand(int i) {
        return new MCCommandResult(this.baf.getFromWord(i), MCTransactionResult.COMMAND_COMPLETED_ERROR, TransitionState.TRANSITION_NOT_REQUIRED);
    }

    protected MCCommandResult completeCommand(ByteArray byteArray) {
        return new MCCommandResult(byteArray, MCTransactionResult.COMMAND_COMPLETED, TransitionState.TRANSITION_NOT_REQUIRED);
    }

    protected MCCommandResult startTransition(ByteArray byteArray) {
        return new MCCommandResult(byteArray, MCTransactionResult.COMMAND_COMPLETED, TransitionState.TRANSITION_NEXT_STATE);
    }

    protected MCCommandResult completeTransaction(ByteArray byteArray) {
        return new MCCommandResult(byteArray, MCTransactionResult.TRANSACTION_COMPLETED, TransitionState.TRANSITION_CANCEL_STATE);
    }

    protected MTBPTransactionContext getTransactionContext() {
        return this.mContext;
    }

    protected DC_CP_MPP getPaymentProfile() {
        return this.mPaymentProfile;
    }

    protected static boolean isTerminalOffline(byte b) {
        if ((b & 15) == 3 || (b & 15) == 6) {
            return true;
        }
        return false;
    }

    protected void checkContext() {
        if (getTransactionContext() != null) {
            getTransactionContext().checkContext();
        }
    }

    protected byte[] getTASecureContainer() {
        if (getTransactionContext() == null || getTransactionContext().getTransactionCredentials() == null) {
            return new byte[]{(byte) 0, (byte) 1};
        }
        return getTransactionContext().getTransactionCredentials().getSecureObject();
    }
}
