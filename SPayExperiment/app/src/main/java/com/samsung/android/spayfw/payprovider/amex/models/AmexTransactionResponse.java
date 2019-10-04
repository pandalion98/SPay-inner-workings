/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 *  java.lang.Object
 */
package com.samsung.android.spayfw.payprovider.amex.models;

import com.google.gson.JsonObject;
import com.samsung.android.spayfw.payprovider.amex.models.AmexTransactionData;

public class AmexTransactionResponse {
    private JsonObject encryptedData;
    private JsonObject encryptionParameters;
    private AmexTransactionData[] transactionDetail;

    public JsonObject getEncryptedData() {
        return this.encryptedData;
    }

    public JsonObject getEncryptionParameters() {
        return this.encryptionParameters;
    }

    public AmexTransactionData[] getTransactionDetail() {
        return this.transactionDetail;
    }
}

