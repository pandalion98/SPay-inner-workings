/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  com.google.gson.JsonObject
 *  java.lang.Object
 */
package com.samsung.android.spayfw.payprovider;

import android.os.Bundle;
import com.google.gson.JsonObject;

public class c {
    private int mErrorCode;
    private JsonObject oR;
    private Bundle oS;

    public void a(JsonObject jsonObject) {
        this.oR = jsonObject;
    }

    public Bundle cg() {
        return this.oS;
    }

    public JsonObject ch() {
        return this.oR;
    }

    public void e(Bundle bundle) {
        this.oS = bundle;
    }

    public int getErrorCode() {
        return this.mErrorCode;
    }

    public void setErrorCode(int n2) {
        this.mErrorCode = n2;
    }
}

