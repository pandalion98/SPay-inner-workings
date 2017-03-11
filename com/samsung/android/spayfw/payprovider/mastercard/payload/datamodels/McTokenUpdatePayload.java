package com.samsung.android.spayfw.payprovider.mastercard.payload.datamodels;

import com.samsung.android.spayfw.payprovider.mastercard.payload.subpayload.McProductConfig;
import com.samsung.android.spayfw.payprovider.mastercard.payload.subpayload.McTokenInfo;
import java.util.List;

public class McTokenUpdatePayload {
    private String mpaInstanceId;
    private McProductConfig[] productConfig;
    private List<String> suspendedby;
    private McTokenInfo tokenInfo;

    public void setMpaInstanceId(String str) {
        this.mpaInstanceId = str;
    }

    public void setSuspendedby(List<String> list) {
        this.suspendedby = list;
    }

    public void setProductConfig(McProductConfig[] mcProductConfigArr) {
        this.productConfig = mcProductConfigArr;
    }

    public void setTokenInfo(McTokenInfo mcTokenInfo) {
        this.tokenInfo = mcTokenInfo;
    }

    public String getMpaInstanceId() {
        return this.mpaInstanceId;
    }

    public List<String> getSuspendedby() {
        return this.suspendedby;
    }

    public McProductConfig[] getProductConfig() {
        return this.productConfig;
    }

    public McTokenInfo getTokenInfo() {
        return this.tokenInfo;
    }
}
