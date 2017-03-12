package com.samsung.android.spayfw.payprovider;

/* renamed from: com.samsung.android.spayfw.payprovider.d */
public class ProviderRequestStatus {
    private int mRequestType;
    private ProviderTokenKey oT;
    private int oU;
    private String oV;

    public ProviderRequestStatus(int i, int i2, ProviderTokenKey providerTokenKey) {
        this.mRequestType = i;
        this.oU = i2;
        this.oT = providerTokenKey;
    }

    public int ci() {
        return this.oU;
    }

    public int getRequestType() {
        return this.mRequestType;
    }

    public String cj() {
        return this.oV;
    }

    public ProviderTokenKey ck() {
        return this.oT;
    }

    public void ar(String str) {
        this.oV = str;
    }
}
