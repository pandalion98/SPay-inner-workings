package com.samsung.android.spayfw.payprovider.mastercard.pce.context;

import android.os.Bundle;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionCompleteResult;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionCompleteResult.PaymentType;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionCompleteResult.TerminalType;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionCredentials;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionResult;

public class SimplePayContextWithFeedbackImpl extends SimplePayContextImpl {
    protected static final String TAG = "mcpce_SimplePayContextWithFeedbackImpl";

    public SimplePayContextWithFeedbackImpl(MCTransactionCredentials mCTransactionCredentials) {
        super(mCTransactionCredentials);
        Log.m287i(TAG, "SimplePay detected");
    }

    protected MCTransactionCompleteResult getTransactionResult() {
        return new MCTransactionCompleteResult(PaymentType.SIMPLE_PAY, isTerminalTapNGoCapable() ? TerminalType.TRANSIT : TerminalType.RETAIL, this.mTransactionResult, this.mTransactionError);
    }

    public Bundle stopNfc() {
        Bundle bundle = new Bundle();
        setNfcError(bundle);
        setTransitError(bundle);
        setMerchantNameLocation(bundle);
        Log.m285d(TAG, "stopNfc bundle:" + bundle.toString());
        return bundle;
    }

    private boolean isTerminalTapNGoCapable() {
        MCTransactionResult filterCheck = filterCheck(TapNGoContextImpl.getTapNGoFilters());
        return filterCheck != null && filterCheck.equals(MCTransactionResult.CONTEXT_CONFLICT_PASS);
    }

    protected void setTransitError(Bundle bundle) {
        if (bundle != null && getTransactionResult().isTerminalTapNGo()) {
            Log.m287i(TAG, "SimplePayContext: Terminal is tap&Go capable!!!");
            bundle.putInt("tapNGotransactionErrorCode", 0);
        }
    }
}
