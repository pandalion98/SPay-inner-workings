/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  com.google.gson.JsonObject
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider;

import android.os.Bundle;
import com.google.gson.JsonObject;
import com.samsung.android.spayfw.payprovider.f;

public class e {
    private int errorCode;
    private String errorMessage;
    private f oW;
    private JsonObject oX;
    private Bundle oY;

    public void as(String string) {
        this.errorMessage = string;
    }

    public void b(JsonObject jsonObject) {
        this.oX = jsonObject;
    }

    public Bundle cg() {
        return this.oY;
    }

    public JsonObject cl() {
        return this.oX;
    }

    public void e(Bundle bundle) {
        this.oY = bundle;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public f getProviderTokenKey() {
        return this.oW;
    }

    public void setErrorCode(int n2) {
        this.errorCode = n2;
    }

    public void setProviderTokenKey(f f2) {
        this.oW = f2;
    }
}

