package com.samsung.android.spayfw.payprovider.mastercard.pce;

import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionResult;

public interface MTBPCardListener {
    void onCardDeactivated();

    void onCardSelected();

    void onTransactionCompleted();

    void onTransactionTerminated(MCTransactionResult mCTransactionResult);
}
