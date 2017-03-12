package com.samsung.android.spayfw.payprovider;

import android.os.Bundle;
import com.google.gson.JsonObject;

/* renamed from: com.samsung.android.spayfw.payprovider.e */
public class ProviderResponseData {
    private int errorCode;
    private String errorMessage;
    private ProviderTokenKey oW;
    private JsonObject oX;
    private Bundle oY;

    public Bundle cg() {
        return this.oY;
    }

    public void m1058e(Bundle bundle) {
        this.oY = bundle;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(int i) {
        this.errorCode = i;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void as(String str) {
        this.errorMessage = str;
    }

    public ProviderTokenKey getProviderTokenKey() {
        return this.oW;
    }

    public void setProviderTokenKey(ProviderTokenKey providerTokenKey) {
        this.oW = providerTokenKey;
    }

    public JsonObject cl() {
        return this.oX;
    }

    public void m1057b(JsonObject jsonObject) {
        this.oX = jsonObject;
    }
}
