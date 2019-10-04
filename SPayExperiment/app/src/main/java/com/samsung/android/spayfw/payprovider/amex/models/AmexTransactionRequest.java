/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.amex.models;

import com.google.gson.JsonObject;

public class AmexTransactionRequest {
    private String accessKey;
    private String accessKeySignature;
    private JsonObject secureDeviceData;
    private String tokenRefId;

    public void setAccessKey(String string) {
        this.accessKey = string;
    }

    public void setAccessKeySignature(String string) {
        this.accessKeySignature = string;
    }

    public void setSecureDeviceData(JsonObject jsonObject) {
        this.secureDeviceData = jsonObject;
    }

    public void setTokenRefId(String string) {
        this.tokenRefId = string;
    }
}

