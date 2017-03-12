package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

import com.google.gson.JsonObject;
import com.samsung.android.spayfw.appinterface.TokenStatus;

public class TokenResponseData extends Id {
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

    public TokenResponseData(String str) {
        super(str);
    }

    public CardInfo getCard() {
        return this.card;
    }

    public JsonObject getData() {
        return this.data;
    }

    public void setData(JsonObject jsonObject) {
        this.data = jsonObject;
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

    public String[] getPresentationModes() {
        if (this.presentation != null) {
            return this.presentation.getModes();
        }
        return null;
    }
}
