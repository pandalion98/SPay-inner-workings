/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.mastercard.pce.context;

import android.os.Bundle;
import com.samsung.android.spayfw.b.c;
import com.samsung.android.spayfw.payprovider.mastercard.pce.context.AbstractTransactionContextImpl;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionCompleteResult;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionCredentials;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionResult;

public class SimplePayContextImpl
extends AbstractTransactionContextImpl {
    public SimplePayContextImpl(MCTransactionCredentials mCTransactionCredentials) {
        super(mCTransactionCredentials);
        c.i("mcpce_AbstractTransactionContextImpl", "SimplePay detected");
    }

    @Override
    public void checkContext() {
        c.d("mcpce_AbstractTransactionContextImpl", "SimplePay:checkContext skipped");
    }

    @Override
    protected MCTransactionCompleteResult getTransactionResult() {
        return new MCTransactionCompleteResult(MCTransactionCompleteResult.PaymentType.SIMPLE_PAY, null, this.mTransactionResult, this.mTransactionError);
    }

    @Override
    public Bundle stopNfc() {
        Bundle bundle = new Bundle();
        this.setNfcError(bundle);
        c.d("mcpce_AbstractTransactionContextImpl", "SimplePay:stopNfc bundle:" + bundle.toString());
        return bundle;
    }
}

