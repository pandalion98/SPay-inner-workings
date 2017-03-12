package com.samsung.android.spayfw.payprovider.mastercard.pce.context;

import android.os.Bundle;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionCompleteResult;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionCompleteResult.PaymentType;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionCredentials;

public class SimplePayContextImpl extends AbstractTransactionContextImpl {
    public SimplePayContextImpl(MCTransactionCredentials mCTransactionCredentials) {
        super(mCTransactionCredentials);
        Log.m287i("mcpce_AbstractTransactionContextImpl", "SimplePay detected");
    }

    protected MCTransactionCompleteResult getTransactionResult() {
        return new MCTransactionCompleteResult(PaymentType.SIMPLE_PAY, null, this.mTransactionResult, this.mTransactionError);
    }

    public Bundle stopNfc() {
        Bundle bundle = new Bundle();
        setNfcError(bundle);
        Log.m285d("mcpce_AbstractTransactionContextImpl", "SimplePay:stopNfc bundle:" + bundle.toString());
        return bundle;
    }

    public void checkContext() {
        Log.m285d("mcpce_AbstractTransactionContextImpl", "SimplePay:checkContext skipped");
    }
}
