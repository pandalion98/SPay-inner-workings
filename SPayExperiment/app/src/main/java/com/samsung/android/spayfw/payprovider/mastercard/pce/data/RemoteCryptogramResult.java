/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.samsung.android.spayfw.payprovider.mastercard.pce.data;

import com.samsung.android.spayfw.payprovider.mastercard.pce.data.ReturnCode;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.TransactionOutput;

public class RemoteCryptogramResult {
    private final ReturnCode code;
    private TransactionOutput output;

    public RemoteCryptogramResult(ReturnCode returnCode) {
        this.code = returnCode;
    }

    public RemoteCryptogramResult(ReturnCode returnCode, TransactionOutput transactionOutput) {
        this.code = returnCode;
        this.output = transactionOutput;
    }

    public ReturnCode getCode() {
        return this.code;
    }

    public TransactionOutput getOutput() {
        return this.output;
    }
}

