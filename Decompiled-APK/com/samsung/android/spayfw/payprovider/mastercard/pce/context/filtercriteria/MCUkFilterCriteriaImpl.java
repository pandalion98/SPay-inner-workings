package com.samsung.android.spayfw.payprovider.mastercard.pce.context.filtercriteria;

import com.mastercard.mobile_api.bytes.ByteArray;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCFilter;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionInformation;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionResult;
import com.samsung.android.spayfw.payprovider.mastercard.pce.nfc.MCAPDUConstants.MCFilterConstants.UK;
import com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils;

public class MCUkFilterCriteriaImpl implements MCFilterCriteria {
    private static final String TAG = "mcpce_MCUkFilterCriteriaImpl";
    private final MCFilter mFilter;

    public MCUkFilterCriteriaImpl() {
        this.mFilter = new MCFilter(false, UK.AMOUNT, UK.CURRENCY, UK.MCC_LIST);
    }

    public MCTransactionResult filterCheck(MCTransactionInformation mCTransactionInformation) {
        Object obj = null;
        if (this.mFilter.getBlAmount() != null) {
            ByteArray currencyCode = mCTransactionInformation.getCurrencyCode();
            Object obj2 = (currencyCode == null || !(this.mFilter.getBlCurrency() == null || this.mFilter.getBlCurrency().isEqual(currencyCode))) ? null : 1;
            if (obj2 == null) {
                if (currencyCode != null) {
                    Log.m285d(TAG, "checkContext fail: 3.5 = " + currencyCode.getHexString());
                }
                return MCTransactionResult.CONTEXT_CONFLICT_CURRENCY;
            }
            currencyCode = mCTransactionInformation.getAmount();
            obj2 = ((this.mFilter.isBlExactAmount() || !McUtils.superior(this.mFilter.getBlAmount(), currencyCode)) && !(this.mFilter.isBlExactAmount() && this.mFilter.getBlAmount().isEqual(currencyCode))) ? null : 1;
            if (obj2 == null) {
                android.util.Log.e(TAG, "checkContext: amount mismatched, so create aac ");
                if (currencyCode != null) {
                    Log.m285d(TAG, "checkContext fail 3.6 = " + currencyCode.getHexString());
                }
                return MCTransactionResult.CONTEXT_CONFLICT_AMOUNT;
            }
        }
        ByteArray mccCategory = mCTransactionInformation.getMccCategory();
        if (this.mFilter.getBlMccList() == null || McUtils.contains(this.mFilter.getBlMccList(), mccCategory)) {
            obj = 1;
        }
        if (obj != null) {
            return MCTransactionResult.CONTEXT_CONFLICT_PASS;
        }
        if (mccCategory != null) {
            android.util.Log.e(TAG, "checkContext: mcc Category code failed = " + mccCategory.getHexString());
        }
        return MCTransactionResult.CONTEXT_CONFLICT_MCC;
    }
}
