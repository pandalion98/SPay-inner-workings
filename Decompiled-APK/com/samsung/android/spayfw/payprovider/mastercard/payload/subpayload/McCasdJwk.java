package com.samsung.android.spayfw.payprovider.mastercard.payload.subpayload;

public class McCasdJwk {
    private String kty;
    private String[] x5c;

    public String getKty() {
        return this.kty;
    }

    public void setKty(String str) {
        this.kty = str;
    }

    public String[] getX5c() {
        return this.x5c;
    }

    public void setX5c(String[] strArr) {
        this.x5c = strArr;
    }
}
