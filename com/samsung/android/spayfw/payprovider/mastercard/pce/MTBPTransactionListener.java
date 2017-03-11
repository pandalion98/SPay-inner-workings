package com.samsung.android.spayfw.payprovider.mastercard.pce;

import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionResult;

public interface MTBPTransactionListener {
    void onTransactionCanceled(MCTransactionResult mCTransactionResult);

    void onTransactionSuccess();
}
