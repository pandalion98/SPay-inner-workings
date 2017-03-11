package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

import com.google.gson.JsonObject;

public class IdvOptionData {
    private JsonObject data;
    private String id;
    private String scheme;
    private String type;
    private String value;

    public String getId() {
        return this.id;
    }

    public void setId(String str) {
        this.id = str;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String str) {
        this.type = str;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String str) {
        this.value = str;
    }

    public String getScheme() {
        return this.scheme;
    }

    public void setScheme(String str) {
        this.scheme = str;
    }

    public JsonObject getData() {
        return this.data;
    }

    public void setData(JsonObject jsonObject) {
        this.data = jsonObject;
    }
}
