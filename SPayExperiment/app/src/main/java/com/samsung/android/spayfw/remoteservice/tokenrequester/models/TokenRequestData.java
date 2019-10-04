/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 *  java.lang.Object
 */
package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

import com.google.gson.JsonObject;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Activation;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Id;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.TimeStamp;

public class TokenRequestData {
    private TimeStamp acceptance;
    private Activation activation;
    private JsonObject data;
    private Id enrollment;

    public TokenRequestData(Id id, TimeStamp timeStamp) {
        this.enrollment = id;
        this.acceptance = timeStamp;
    }

    public void setActivation(Activation activation) {
        this.activation = activation;
    }

    public void setData(JsonObject jsonObject) {
        this.data = jsonObject;
    }
}

