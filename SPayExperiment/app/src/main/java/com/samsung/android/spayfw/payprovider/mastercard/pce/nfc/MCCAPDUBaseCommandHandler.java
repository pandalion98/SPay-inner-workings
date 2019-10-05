/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.mastercard.pce.nfc;

import com.mastercard.mcbp.core.mcbpcards.profile.DC_CP;
import com.mastercard.mcbp.core.mcbpcards.profile.DC_CP_MPP;
import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.bytes.ByteArrayFactory;
import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.payprovider.mastercard.pce.context.MTBPTransactionContext;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionResult;

public abstract class MCCAPDUBaseCommandHandler {
    protected static final String TAG = "mcpce_MCCAPDUBaseCommandHandler";
    protected final ByteArrayFactory baf = ByteArrayFactory.getInstance();
    private MTBPTransactionContext mContext;
    protected DC_CP_MPP mPaymentProfile;

    protected MCCAPDUBaseCommandHandler() {
    }

    protected static boolean isTerminalOffline(byte by) {
        return (by & 15) == 3 || (by & 15) == 6;
    }

    protected MCCommandResult ERROR(int n2) {
        MCTransactionResult mCTransactionResult = MCCommandResult.iso7816ToResponseCode(n2);
        return new MCCommandResult(this.baf.getFromWord(n2), mCTransactionResult, MCCommandResult.TransitionState.TRANSITION_CANCEL_STATE);
    }

    public abstract boolean checkCLA(byte var1);

    protected void checkContext() {
        if (this.getTransactionContext() != null) {
            this.getTransactionContext().checkContext();
        }
    }

    protected abstract MCCommandResult checkP1P2Parameters(byte var1, byte var2);

    protected MCCommandResult completeCommand() {
        return new MCCommandResult(null, MCTransactionResult.COMMAND_COMPLETED, MCCommandResult.TransitionState.TRANSITION_NOT_REQUIRED);
    }

    protected MCCommandResult completeCommand(int n2) {
        return new MCCommandResult(this.baf.getFromWord(n2), MCTransactionResult.COMMAND_COMPLETED_ERROR, MCCommandResult.TransitionState.TRANSITION_NOT_REQUIRED);
    }

    protected MCCommandResult completeCommand(ByteArray byteArray) {
        return new MCCommandResult(byteArray, MCTransactionResult.COMMAND_COMPLETED, MCCommandResult.TransitionState.TRANSITION_NOT_REQUIRED);
    }

    protected MCCommandResult completeTransaction(ByteArray byteArray) {
        return new MCCommandResult(byteArray, MCTransactionResult.TRANSACTION_COMPLETED, MCCommandResult.TransitionState.TRANSITION_CANCEL_STATE);
    }

    protected abstract MCCommandResult generateResponseAPDU();

    protected DC_CP_MPP getPaymentProfile() {
        return this.mPaymentProfile;
    }

    protected byte[] getTASecureContainer() {
        if (this.getTransactionContext() != null && this.getTransactionContext().getTransactionCredentials() != null) {
            return this.getTransactionContext().getTransactionCredentials().getSecureObject();
        }
        return new byte[]{0, 1};
    }

    protected MTBPTransactionContext getTransactionContext() {
        return this.mContext;
    }

    public MCCommandResult processAPDU(ByteArray byteArray) {
        if (byteArray == null || byteArray.getLength() == 0) {
            Log.e("mcpce_MCCAPDUBaseCommandHandler", "Empty apdu. Return error code 27013");
            return this.ERROR(27013);
        }
        Log.i("mcpce_MCCAPDUBaseCommandHandler", "apdu:" + byteArray.getHexString());
        Log.d("mcpce_MCCAPDUBaseCommandHandler", "Check APDU parameters.");
        MCCommandResult mCCommandResult = this.checkP1P2Parameters(byteArray.getByte(2), byteArray.getByte(3));
        if (mCCommandResult != null) {
            Log.d("mcpce_MCCAPDUBaseCommandHandler", "checkP1P2Parameters response result: " + (Object)((Object)mCCommandResult.getResponseCode()));
        }
        if (!MCTransactionResult.COMMAND_COMPLETED.equals((Object)((Object)mCCommandResult.getResponseCode()))) {
            Log.e("mcpce_MCCAPDUBaseCommandHandler", "checkP1P2Parameters execution error, error code " + (Object)((Object)mCCommandResult.getResponseCode()));
            return mCCommandResult;
        }
        Log.d("mcpce_MCCAPDUBaseCommandHandler", "Process APDU.");
        MCCommandResult mCCommandResult2 = this.processCommand(byteArray);
        if (!MCTransactionResult.COMMAND_COMPLETED.equals((Object)((Object)mCCommandResult2.getResponseCode()))) {
            if (MCTransactionResult.TRANSACTION_COMPLETED.equals((Object)((Object)mCCommandResult2.getResponseCode()))) {
                Log.i("mcpce_MCCAPDUBaseCommandHandler", "ProccessAPDU Transaction completed  " + (Object)((Object)mCCommandResult2.getResponseCode()));
                return mCCommandResult2;
            }
            Log.e("mcpce_MCCAPDUBaseCommandHandler", "ProccessAPDU execution error, error code " + (Object)((Object)mCCommandResult2.getResponseCode()));
            return mCCommandResult2;
        }
        Log.i("mcpce_MCCAPDUBaseCommandHandler", "Generate APDU response.");
        return this.generateResponseAPDU();
    }

    protected abstract MCCommandResult processCommand(ByteArray var1);

    public void setTransactionContext(DC_CP dC_CP, MTBPTransactionContext mTBPTransactionContext) {
        this.mContext = mTBPTransactionContext;
        this.mPaymentProfile = dC_CP.getDC_CP_MPP();
    }

    protected MCCommandResult startTransition(ByteArray byteArray) {
        return new MCCommandResult(byteArray, MCTransactionResult.COMMAND_COMPLETED, MCCommandResult.TransitionState.TRANSITION_NEXT_STATE);
    }
}

