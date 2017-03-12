package com.samsung.android.spayfw.payprovider.mastercard.pce.context.filtercriteria;

import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionInformation;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionResult;

public interface MCFilterCriteria {
    MCTransactionResult filterCheck(MCTransactionInformation mCTransactionInformation);
}
