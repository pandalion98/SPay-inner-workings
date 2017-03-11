package com.samsung.android.spayfw.payprovider.mastercard.pce.context;

import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionCredentials;

public class MTBPContextFactory {
    public static MTBPTransactionContext getContext(MCTransactionCredentials mCTransactionCredentials) {
        return isTapAndGo(mCTransactionCredentials) ? new TapNGoContextImpl(mCTransactionCredentials) : new SimplePayContextWithFeedbackImpl(mCTransactionCredentials);
    }

    private static boolean isTapAndGo(MCTransactionCredentials mCTransactionCredentials) {
        if (mCTransactionCredentials == null || mCTransactionCredentials.getCVMResult() == null || mCTransactionCredentials.getCVMResult().isCVMRequired()) {
            return false;
        }
        return true;
    }
}
