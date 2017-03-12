package com.samsung.android.spayfw.payprovider.amex.models;

import com.google.gson.JsonObject;

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
