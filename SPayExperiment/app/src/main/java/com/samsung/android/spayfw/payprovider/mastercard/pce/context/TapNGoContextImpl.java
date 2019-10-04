/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.HashMap
 *  java.util.List
 *  java.util.Map
 */
package com.samsung.android.spayfw.payprovider.mastercard.pce.context;

import android.os.Bundle;
import com.samsung.android.spayfw.b.c;
import com.samsung.android.spayfw.payprovider.mastercard.pce.context.AbstractTransactionContextImpl;
import com.samsung.android.spayfw.payprovider.mastercard.pce.context.filtercriteria.MCFilterCriteria;
import com.samsung.android.spayfw.payprovider.mastercard.pce.context.filtercriteria.MCUkFilterCriteriaImpl;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionCompleteResult;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionCredentials;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionResult;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TapNGoContextImpl
extends AbstractTransactionContextImpl {
    protected static final String TAG = "mcpce_TapNGoContextImpl";
    private static final Map<MCTransactionResult, Integer> sSdkToAppErrorCodeMap = new HashMap();

    static {
        sSdkToAppErrorCodeMap.put((Object)MCTransactionResult.CONTEXT_CONFLICT_AMOUNT, (Object)-101);
        sSdkToAppErrorCodeMap.put((Object)MCTransactionResult.CONTEXT_CONFLICT_CURRENCY, (Object)-102);
        sSdkToAppErrorCodeMap.put((Object)MCTransactionResult.CONTEXT_CONFLICT_MCC, (Object)-104);
        sSdkToAppErrorCodeMap.put((Object)MCTransactionResult.CONTEXT_CONFLICT_CVM, (Object)-103);
    }

    public TapNGoContextImpl(MCTransactionCredentials mCTransactionCredentials) {
        super(mCTransactionCredentials);
        c.i(TAG, "TAP&Go detected. Setting filters");
        this.mFilters = TapNGoContextImpl.getTapNGoFilters();
    }

    public static List<MCFilterCriteria> getTapNGoFilters() {
        ArrayList arrayList = new ArrayList();
        arrayList.add((Object)new MCUkFilterCriteriaImpl());
        return arrayList;
    }

    @Override
    public void checkContext() {
        c.d(TAG, "checkContext");
        MCTransactionResult mCTransactionResult = this.filterCheck((List<MCFilterCriteria>)this.mFilters);
        if (mCTransactionResult != null && mCTransactionResult.equals((Object)MCTransactionResult.CONTEXT_CONFLICT_PASS)) {
            c.d(TAG, "checkContext Passed");
            this.setTransactionError(mCTransactionResult);
            return;
        }
        this.contextConflictError(mCTransactionResult);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    protected MCTransactionCompleteResult getTransactionResult() {
        MCTransactionCompleteResult.TerminalType terminalType;
        MCTransactionCompleteResult.PaymentType paymentType = MCTransactionCompleteResult.PaymentType.TAP_N_GO;
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

    protected boolean isTerminalTapNGoCapable() {
        return this.getTransactionError() != null && this.getTransactionError().equals((Object)MCTransactionResult.CONTEXT_CONFLICT_PASS);
    }

    protected void setTransitCheckResult(Bundle bundle) {
        MCTransactionCompleteResult mCTransactionCompleteResult = this.getTransactionResult();
        if (bundle == null || mCTransactionCompleteResult == null) {
            return;
        }
        MCTransactionResult mCTransactionResult = mCTransactionCompleteResult.getTransactionError();
        if (mCTransactionResult == null) {
            c.e(TAG, "Tap&Go No Err set:");
            return;
        }
        if (mCTransactionResult.equals((Object)MCTransactionResult.CONTEXT_CONFLICT_PASS)) {
            bundle.putInt("tapNGotransactionErrorCode", 0);
            c.d(TAG, "Tap&Go Success bundle:" + bundle.toString());
            return;
        }
        Integer n2 = (Integer)sSdkToAppErrorCodeMap.get((Object)mCTransactionResult);
        if (n2 == null) {
            c.e(TAG, "Tap&Go: Invalid resultCode for tap&Go: " + mCTransactionResult.name());
            return;
        }
        bundle.putInt("tapNGotransactionErrorCode", n2.intValue());
        c.e(TAG, "Tap&Go: error bundle:" + bundle.toString());
    }

    @Override
    public Bundle stopNfc() {
        Bundle bundle = new Bundle();
        this.setNfcError(bundle);
        this.setTransitCheckResult(bundle);
        this.setMerchantNameLocation(bundle);
        c.d(TAG, "tapNGo Bundle StopNfc: " + bundle.toString());
        return bundle;
    }
}

