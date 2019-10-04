/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 *  java.lang.String
 */
package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

import com.google.gson.JsonObject;
import com.samsung.android.spayfw.appinterface.TokenStatus;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.CardInfo;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Expiry;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Id;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Mode;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Transaction;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Url;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.UserInfo;

public class TokenResponseData
extends Id {
    private CardInfo card;
    private JsonObject data;
    private Url enrollment;
    private Expiry expiry;
    private String href;
    private Mode presentation;
    private TokenStatus status;
    private String suffix;
    private Transaction transaction;
    private UserInfo user;

    public TokenResponseData(String string) {
        super(string);
    }

    public CardInfo getCard() {
        return this.card;
    }

    public JsonObject getData() {
        return this.data;
    }

    public Url getEnrollment() {
        return this.enrollment;
    }

    public Expiry getExpiry() {
        return this.expiry;
    }

    public String getHref() {
        return this.href;
    }

    public String[] getPresentationModes() {
        if (this.presentation != null) {
            return this.presentation.getModes();
        }
        return null;
    }

    public TokenStatus getStatus() {
        return this.status;
    }

    public String getSuffix() {
        return this.suffix;
    }

    public Transaction getTransaction() {
        return this.transaction;
    }

    public UserInfo getUser() {
        return this.user;
    }

    public void setData(JsonObject jsonObject) {
        this.data = jsonObject;
    }
}

