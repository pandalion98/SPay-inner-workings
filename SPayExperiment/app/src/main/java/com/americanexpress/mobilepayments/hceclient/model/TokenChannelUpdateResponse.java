/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 */
package com.americanexpress.mobilepayments.hceclient.model;

import com.americanexpress.mobilepayments.hceclient.model.TokenOperationStatus;

public class TokenChannelUpdateResponse
extends TokenOperationStatus {
    private String hmac;

    public String getHmac() {
        return this.hmac;
    }

    public void setHmac(String string) {
        this.hmac = string;
    }
}

