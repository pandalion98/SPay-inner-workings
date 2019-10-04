/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

import com.google.gson.JsonObject;

public class IdvOptionData {
    private JsonObject data;
    private String id;
    private String scheme;
    private String type;
    private String value;

    public JsonObject getData() {
        return this.data;
    }

    public String getId() {
        return this.id;
    }

    public String getScheme() {
        return this.scheme;
    }

    public String getType() {
        return this.type;
    }

    public String getValue() {
        return this.value;
    }

    public void setData(JsonObject jsonObject) {
        this.data = jsonObject;
    }

    public void setId(String string) {
        this.id = string;
    }

    public void setScheme(String string) {
        this.scheme = string;
    }

    public void setType(String string) {
        this.type = string;
    }

    public void setValue(String string) {
        this.value = string;
    }
}

