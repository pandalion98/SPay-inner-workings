/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.mastercard.pce.context;

import android.os.Bundle;

import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionCompleteResult;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionCredentials;

public class SimplePayContextImpl
extends AbstractTransactionContextImpl {
    public SimplePayContextImpl(MCTransactionCredentials mCTransactionCredentials) {
        super(mCTransactionCredentials);
        Log.i("mcpce_AbstractTransactionContextImpl", "SimplePay detected");
    }

    @Override
    public void checkContext() {
        Log.d("mcpce_AbstractTransactionContextImpl", "SimplePay:checkContext skipped");
    }

    @Override
    protected MCTransactionCompleteResult getTransactionResult() {
        return new MCTransactionCompleteResult(MCTransactionCompleteResult.PaymentType.SIMPLE_PAY, null, this.mTransactionResult, this.mTransactionError);
    }

    @Override
    public Bundle stopNfc() {
        Bundle bundle = new Bundle();
        this.setNfcError(bundle);
        Log.d("mcpce_AbstractTransactionContextImpl", "SimplePay:stopNfc bundle:" + bundle.toString());
        return bundle;
    }
}

