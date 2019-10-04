/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  java.lang.Object
 *  java.lang.String
 *  java.util.List
 */
package com.samsung.android.spayfw.payprovider.mastercard.pce.context;

import android.os.Bundle;
import com.samsung.android.spayfw.b.c;
import com.samsung.android.spayfw.payprovider.mastercard.pce.context.SimplePayContextImpl;
import com.samsung.android.spayfw.payprovider.mastercard.pce.context.TapNGoContextImpl;
import com.samsung.android.spayfw.payprovider.mastercard.pce.context.filtercriteria.MCFilterCriteria;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionCompleteResult;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionCredentials;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionResult;
import java.util.List;

public class SimplePayContextWithFeedbackImpl
extends SimplePayContextImpl {
    protected static final String TAG = "mcpce_SimplePayContextWithFeedbackImpl";

    public SimplePayContextWithFeedbackImpl(MCTransactionCredentials mCTransactionCredentials) {
        super(mCTransactionCredentials);
        c.i(TAG, "SimplePay detected");
    }

    private boolean isTerminalTapNGoCapable() {
        MCTransactionResult mCTransactionResult = this.filterCheck(TapNGoContextImpl.getTapNGoFilters());
        return mCTransactionResult != null && mCTransactionResult.equals((Object)MCTransactionResult.CONTEXT_CONFLICT_PASS);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    protected MCTransactionCompleteResult getTransactionResult() {
        MCTransactionCompleteResult.TerminalType terminalType;
        MCTransactionCompleteResult.PaymentType paymentType = MCTransactionCompleteResult.PaymentType.SIMPLE_PAY;
        if (this.isTerminalTapNGoCapable()) {
            terminalType = MCTransactionCompleteResult.TerminalType.TRANSIT;
            do {
                return new MCTransactionCompleteResult(paymentType, terminalType, this.mTransactionResult, this.mTransactionError);
                break;
            } while (true);
        }
        terminalType = MCTransactionCompleteResult.TerminalType.RETAIL;
        return new MCTransactionCompleteResult(paymentType, terminalType, this.mTransactionResult, this.mTransactionError);
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void setTransitError(Bundle bundle) {
        if (bundle == null || !this.getTransactionResult().isTerminalTapNGo()) {
            return;
        }
        c.i(TAG, "SimplePayContext: Terminal is tap&Go capable!!!");
        bundle.putInt("tapNGotransactionErrorCode", 0);
    }

    @Override
    public Bundle stopNfc() {
        Bundle bundle = new Bundle();
        this.setNfcError(bundle);
        this.setTransitError(bundle);
        this.setMerchantNameLocation(bundle);
        c.d(TAG, "stopNfc bundle:" + bundle.toString());
        return bundle;
    }
}

