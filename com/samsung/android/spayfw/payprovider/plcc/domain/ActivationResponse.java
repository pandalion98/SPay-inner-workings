package com.samsung.android.spayfw.payprovider.plcc.domain;

import android.annotation.SuppressLint;
import com.google.gson.Gson;

@SuppressLint({"NamingConvention"})
public class ActivationResponse {
    private String encrypted;
    private String merchantId;
    private String mstSeqConfig;
    private String timestamp;

    public String getMerchantId() {
        return this.merchantId;
    }

    public String getEncrypted() {
        return this.encrypted;
    }

    public String getMstSeqConfig() {
        return this.mstSeqConfig;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public static ActivationResponse fromJson(String str) {
        return (ActivationResponse) new Gson().fromJson(str, ActivationResponse.class);
    }
}
