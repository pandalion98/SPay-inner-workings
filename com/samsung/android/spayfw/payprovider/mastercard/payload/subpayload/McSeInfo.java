package com.samsung.android.spayfw.payprovider.mastercard.payload.subpayload;

import java.util.Map;

public class McSeInfo {
    private String seId;

    public void setSeId(String str) {
        this.seId = str;
    }

    public String getSeId() {
        return this.seId;
    }

    public McSeInfo(String str, Map map) {
        this.seId = str;
    }
}
