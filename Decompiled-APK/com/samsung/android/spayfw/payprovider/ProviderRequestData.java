package com.samsung.android.spayfw.payprovider;

import android.os.Bundle;
import com.google.gson.JsonObject;

/* renamed from: com.samsung.android.spayfw.payprovider.c */
public class ProviderRequestData {
    private int mErrorCode;
    private JsonObject oR;
    private Bundle oS;

    public Bundle cg() {
        return this.oS;
    }

    public void m823e(Bundle bundle) {
        this.oS = bundle;
    }

    public JsonObject ch() {
        return this.oR;
    }

    public void m822a(JsonObject jsonObject) {
        this.oR = jsonObject;
    }

    public int getErrorCode() {
        return this.mErrorCode;
    }

    public void setErrorCode(int i) {
        this.mErrorCode = i;
    }
}
