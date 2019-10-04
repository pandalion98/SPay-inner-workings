/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.util.HashMap
 *  java.util.Map
 */
package com.samsung.android.visasdk.facade.data;

import com.samsung.android.visasdk.facade.data.TransactionError;
import java.util.HashMap;
import java.util.Map;

public class TransactionStatus {
    public static final String EXTRA_MERCHANT_NAME_TAG = "9F4E";
    public static final String EXTRA_PDOL_VALUES = "pdolValues";
    private TransactionError error;
    private Map<String, String> pdolValues;
    private boolean tapNGoAllowed;

    public TransactionStatus(TransactionError transactionError, boolean bl) {
        this.error = transactionError;
        this.tapNGoAllowed = bl;
    }

    public void addPdolValue(String string, String string2) {
        if (string == null || string2 == null) {
            return;
        }
        if (this.pdolValues == null) {
            this.pdolValues = new HashMap();
        }
        this.pdolValues.put((Object)string, (Object)string2);
    }

    public TransactionError getError() {
        return this.error;
    }

    public Map<String, String> getPdolValues() {
        return this.pdolValues;
    }

    public void initialize() {
        this.setTapNGoAllowed(false);
        this.setError(TransactionError.NO_ERROR);
        this.setPdolValues(null);
    }

    public boolean isTapNGoAllowed() {
        return this.tapNGoAllowed;
    }

    public void setError(TransactionError transactionError) {
        this.error = transactionError;
    }

    public void setPdolValues(Map<String, String> map) {
        this.pdolValues = map;
    }

    public void setTapNGoAllowed(boolean bl) {
        this.tapNGoAllowed = bl;
    }
}

