/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.util.Log
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.mastercard.pce.context.filtercriteria;

import android.util.Log;
import com.mastercard.mobile_api.bytes.ByteArray;
import com.samsung.android.spayfw.b.c;
import com.samsung.android.spayfw.payprovider.mastercard.pce.context.filtercriteria.MCFilterCriteria;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCFilter;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionInformation;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionResult;
import com.samsung.android.spayfw.payprovider.mastercard.pce.nfc.MCAPDUConstants;
import com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils;

public class MCUkFilterCriteriaImpl
implements MCFilterCriteria {
    private static final String TAG = "mcpce_MCUkFilterCriteriaImpl";
    private final MCFilter mFilter = new MCFilter(false, MCAPDUConstants.MCFilterConstants.UK.AMOUNT, MCAPDUConstants.MCFilterConstants.UK.CURRENCY, MCAPDUConstants.MCFilterConstants.UK.MCC_LIST);

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public MCTransactionResult filterCheck(MCTransactionInformation mCTransactionInformation) {
        ByteArray byteArray;
        boolean bl;
        block11 : {
            block10 : {
                if (this.mFilter.getBlAmount() != null) {
                    ByteArray byteArray2 = mCTransactionInformation.getCurrencyCode();
                    boolean bl2 = byteArray2 != null && (this.mFilter.getBlCurrency() == null || this.mFilter.getBlCurrency().isEqual(byteArray2));
                    if (!bl2) {
                        if (byteArray2 != null) {
                            c.d(TAG, "checkContext fail: 3.5 = " + byteArray2.getHexString());
                        }
                        return MCTransactionResult.CONTEXT_CONFLICT_CURRENCY;
                    }
                    ByteArray byteArray3 = mCTransactionInformation.getAmount();
                    boolean bl3 = !this.mFilter.isBlExactAmount() && McUtils.superior(this.mFilter.getBlAmount(), byteArray3) || this.mFilter.isBlExactAmount() && this.mFilter.getBlAmount().isEqual(byteArray3);
                    if (!bl3) {
                        Log.e((String)TAG, (String)"checkContext: amount mismatched, so create aac ");
                        if (byteArray3 != null) {
                            c.d(TAG, "checkContext fail 3.6 = " + byteArray3.getHexString());
                        }
                        return MCTransactionResult.CONTEXT_CONFLICT_AMOUNT;
                    }
                }
                byteArray = mCTransactionInformation.getMccCategory();
                if (this.mFilter.getBlMccList() == null) break block10;
                boolean bl4 = McUtils.contains(this.mFilter.getBlMccList(), byteArray);
                bl = false;
                if (!bl4) break block11;
            }
            bl = true;
        }
        if (bl) {
            return MCTransactionResult.CONTEXT_CONFLICT_PASS;
        }
        if (byteArray != null) {
            Log.e((String)TAG, (String)("checkContext: mcc Category code failed = " + byteArray.getHexString()));
        }
        return MCTransactionResult.CONTEXT_CONFLICT_MCC;
    }
}

