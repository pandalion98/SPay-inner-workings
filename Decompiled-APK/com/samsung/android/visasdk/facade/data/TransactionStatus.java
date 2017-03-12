package com.samsung.android.visasdk.facade.data;

import java.util.HashMap;
import java.util.Map;

public class TransactionStatus {
    public static final String EXTRA_MERCHANT_NAME_TAG = "9F4E";
    public static final String EXTRA_PDOL_VALUES = "pdolValues";
    private TransactionError error;
    private Map<String, String> pdolValues;
    private boolean tapNGoAllowed;

    public TransactionStatus(TransactionError transactionError, boolean z) {
        this.error = transactionError;
        this.tapNGoAllowed = z;
    }

    public TransactionError getError() {
        return this.error;
    }

    public void setError(TransactionError transactionError) {
        this.error = transactionError;
    }

    public boolean isTapNGoAllowed() {
        return this.tapNGoAllowed;
    }

    public void setTapNGoAllowed(boolean z) {
        this.tapNGoAllowed = z;
    }

    public void setPdolValues(Map<String, String> map) {
        this.pdolValues = map;
    }

    public void addPdolValue(String str, String str2) {
        if (str != null && str2 != null) {
            if (this.pdolValues == null) {
                this.pdolValues = new HashMap();
            }
            this.pdolValues.put(str, str2);
        }
    }

    public Map<String, String> getPdolValues() {
        return this.pdolValues;
    }

    public void initialize() {
        setTapNGoAllowed(false);
        setError(TransactionError.NO_ERROR);
        setPdolValues(null);
    }
}
