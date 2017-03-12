package com.samsung.android.spayfw.payprovider.amex.models;

import com.google.gson.JsonObject;

public class AmexTransactionRequest {
    private String accessKey;
    private String accessKeySignature;
    private JsonObject secureDeviceData;
    private String tokenRefId;

    public void setAccessKey(String str) {
        this.accessKey = str;
    }

    public void setAccessKeySignature(String str) {
        this.accessKeySignature = str;
    }

    public void setSecureDeviceData(JsonObject jsonObject) {
        this.secureDeviceData = jsonObject;
    }

    public void setTokenRefId(String str) {
        this.tokenRefId = str;
    }
}
