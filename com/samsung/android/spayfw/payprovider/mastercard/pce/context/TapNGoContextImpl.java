package com.samsung.android.spayfw.payprovider.mastercard.pce.context;

import android.os.Bundle;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.mastercard.pce.context.filtercriteria.MCFilterCriteria;
import com.samsung.android.spayfw.payprovider.mastercard.pce.context.filtercriteria.MCUkFilterCriteriaImpl;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionCompleteResult;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionCompleteResult.PaymentType;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionCompleteResult.TerminalType;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionCredentials;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionResult;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TapNGoContextImpl extends AbstractTransactionContextImpl {
    protected static final String TAG = "mcpce_TapNGoContextImpl";
    private static final Map<MCTransactionResult, Integer> sSdkToAppErrorCodeMap;

    static {
        sSdkToAppErrorCodeMap = new HashMap();
        sSdkToAppErrorCodeMap.put(MCTransactionResult.CONTEXT_CONFLICT_AMOUNT, Integer.valueOf(PaymentFramework.RESULT_CODE_FAIL_INVALID_TRANSACTION_AMOUNT));
        sSdkToAppErrorCodeMap.put(MCTransactionResult.CONTEXT_CONFLICT_CURRENCY, Integer.valueOf(PaymentFramework.RESULT_CODE_FAIL_PAY_INVALID_TRANSACTION_CURRENCY));
        sSdkToAppErrorCodeMap.put(MCTransactionResult.CONTEXT_CONFLICT_MCC, Integer.valueOf(PaymentFramework.RESULT_CODE_FAIL_PAY_INVALID_TRANSACTION_CATEGORY));
        sSdkToAppErrorCodeMap.put(MCTransactionResult.CONTEXT_CONFLICT_CVM, Integer.valueOf(PaymentFramework.RESULT_CODE_FAIL_PAY_INVALID_TRANSACTION_TYPE));
    }

    public TapNGoContextImpl(MCTransactionCredentials mCTransactionCredentials) {
        super(mCTransactionCredentials);
        Log.m287i(TAG, "TAP&Go detected. Setting filters");
        this.mFilters = getTapNGoFilters();
    }

    protected MCTransactionCompleteResult getTransactionResult() {
        return new MCTransactionCompleteResult(PaymentType.TAP_N_GO, isTerminalTapNGoCapable() ? TerminalType.TRANSIT : TerminalType.RETAIL, this.mTransactionResult, this.mTransactionError);
    }

    public Bundle stopNfc() {
        Bundle bundle = new Bundle();
        setNfcError(bundle);
        setTransitCheckResult(bundle);
        setMerchantNameLocation(bundle);
        Log.m285d(TAG, "tapNGo Bundle StopNfc: " + bundle.toString());
        return bundle;
    }

    public void checkContext() {
        Log.m285d(TAG, "checkContext");
        MCTransactionResult filterCheck = filterCheck(this.mFilters);
        if (filterCheck == null || !filterCheck.equals(MCTransactionResult.CONTEXT_CONFLICT_PASS)) {
            contextConflictError(filterCheck);
            return;
        }
        Log.m285d(TAG, "checkContext Passed");
        setTransactionError(filterCheck);
    }

    protected boolean isTerminalTapNGoCapable() {
        return getTransactionError() != null && getTransactionError().equals(MCTransactionResult.CONTEXT_CONFLICT_PASS);
    }

    protected void setTransitCheckResult(Bundle bundle) {
        MCTransactionCompleteResult transactionResult = getTransactionResult();
        if (bundle != null && transactionResult != null) {
            MCTransactionResult transactionError = transactionResult.getTransactionError();
            if (transactionError == null) {
                Log.m286e(TAG, "Tap&Go No Err set:");
            } else if (transactionError.equals(MCTransactionResult.CONTEXT_CONFLICT_PASS)) {
                bundle.putInt("tapNGotransactionErrorCode", 0);
                Log.m285d(TAG, "Tap&Go Success bundle:" + bundle.toString());
            } else {
                Integer num = (Integer) sSdkToAppErrorCodeMap.get(transactionError);
                if (num == null) {
                    Log.m286e(TAG, "Tap&Go: Invalid resultCode for tap&Go: " + transactionError.name());
                    return;
                }
                bundle.putInt("tapNGotransactionErrorCode", num.intValue());
                Log.m286e(TAG, "Tap&Go: error bundle:" + bundle.toString());
            }
        }
    }

    public static List<MCFilterCriteria> getTapNGoFilters() {
        List<MCFilterCriteria> arrayList = new ArrayList();
        arrayList.add(new MCUkFilterCriteriaImpl());
        return arrayList;
    }
}
