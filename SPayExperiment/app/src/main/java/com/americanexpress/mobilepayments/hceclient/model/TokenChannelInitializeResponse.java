/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 */
package com.americanexpress.mobilepayments.hceclient.model;

import com.americanexpress.mobilepayments.hceclient.model.TokenOperationStatus;

public class TokenChannelInitializeResponse
extends TokenOperationStatus {
    private String ephemeralData;

    public String getEphemeralData() {
        return this.ephemeralData;
    }

    public void setEphemeralData(String string) {
        this.ephemeralData = string;
    }
}

