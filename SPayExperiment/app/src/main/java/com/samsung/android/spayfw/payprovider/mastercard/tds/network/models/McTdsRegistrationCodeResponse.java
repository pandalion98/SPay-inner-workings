/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.mastercard.tds.network.models;

import com.samsung.android.spayfw.payprovider.mastercard.tds.network.models.McTdsCommonResponse;

public class McTdsRegistrationCodeResponse
extends McTdsCommonResponse {
    private String registrationCode1;

    public String getRegistrationCode1() {
        return this.registrationCode1;
    }

    public void setRegistrationCode1(String string) {
        this.registrationCode1 = string;
    }
}

