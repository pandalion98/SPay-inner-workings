/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.annotation.SuppressLint
 *  com.google.gson.Gson
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.plcc.domain;

import android.annotation.SuppressLint;
import com.google.gson.Gson;

@SuppressLint(value={"NamingConvention"})
public class ActivationResponse {
    private String encrypted;
    private String merchantId;
    private String mstSeqConfig;
    private String timestamp;

    public static ActivationResponse fromJson(String string) {
        return (ActivationResponse)new Gson().fromJson(string, ActivationResponse.class);
    }

    public String getEncrypted() {
        return this.encrypted;
    }

    public String getMerchantId() {
        return this.merchantId;
    }

    public String getMstSeqConfig() {
        return this.mstSeqConfig;
    }

    public String getTimestamp() {
        return this.timestamp;
    }
}

