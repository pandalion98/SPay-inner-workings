/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.mastercard.tds.network.models;

import com.samsung.android.spayfw.payprovider.mastercard.tds.network.models.McTdsCommonResponse;

public class McTdsRegisterResponse
extends McTdsCommonResponse {
    private String authenticationCode;
    private String tdsUrl;

    public String getAuthenticationCode() {
        return this.authenticationCode;
    }

    public String getTdsUrl() {
        return this.tdsUrl;
    }

    public void setAuthenticationCode(String string) {
        this.authenticationCode = string;
    }

    public void setTdsUrl(String string) {
        this.tdsUrl = string;
    }
}

