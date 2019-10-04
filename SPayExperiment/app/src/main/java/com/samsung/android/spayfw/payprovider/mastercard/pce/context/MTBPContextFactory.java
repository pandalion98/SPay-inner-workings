/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.samsung.android.spayfw.payprovider.mastercard.pce.context;

import com.samsung.android.spayfw.payprovider.mastercard.pce.context.MTBPTransactionContext;
import com.samsung.android.spayfw.payprovider.mastercard.pce.context.SimplePayContextWithFeedbackImpl;
import com.samsung.android.spayfw.payprovider.mastercard.pce.context.TapNGoContextImpl;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCCVMResult;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionCredentials;

public class MTBPContextFactory {
    public static MTBPTransactionContext getContext(MCTransactionCredentials mCTransactionCredentials) {
        if (MTBPContextFactory.isTapAndGo(mCTransactionCredentials)) {
            return new TapNGoContextImpl(mCTransactionCredentials);
        }
        return new SimplePayContextWithFeedbackImpl(mCTransactionCredentials);
    }

    /*
     * Enabled aggressive block sorting
     */
    private static boolean isTapAndGo(MCTransactionCredentials mCTransactionCredentials) {
        return mCTransactionCredentials != null && mCTransactionCredentials.getCVMResult() != null && !mCTransactionCredentials.getCVMResult().isCVMRequired();
    }
}

