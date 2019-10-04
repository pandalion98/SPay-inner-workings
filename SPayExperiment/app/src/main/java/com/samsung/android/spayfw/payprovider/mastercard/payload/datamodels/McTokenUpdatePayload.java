/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.util.List
 */
package com.samsung.android.spayfw.payprovider.mastercard.payload.datamodels;

import com.samsung.android.spayfw.payprovider.mastercard.payload.subpayload.McProductConfig;
import com.samsung.android.spayfw.payprovider.mastercard.payload.subpayload.McTokenInfo;
import java.util.List;

public class McTokenUpdatePayload {
    private String mpaInstanceId;
    private McProductConfig[] productConfig;
    private List<String> suspendedby;
    private McTokenInfo tokenInfo;

    public String getMpaInstanceId() {
        return this.mpaInstanceId;
    }

    public McProductConfig[] getProductConfig() {
        return this.productConfig;
    }

    public List<String> getSuspendedby() {
        return this.suspendedby;
    }

    public McTokenInfo getTokenInfo() {
        return this.tokenInfo;
    }

    public void setMpaInstanceId(String string) {
        this.mpaInstanceId = string;
    }

    public void setProductConfig(McProductConfig[] arrmcProductConfig) {
        this.productConfig = arrmcProductConfig;
    }

    public void setSuspendedby(List<String> list) {
        this.suspendedby = list;
    }

    public void setTokenInfo(McTokenInfo mcTokenInfo) {
        this.tokenInfo = mcTokenInfo;
    }
}

