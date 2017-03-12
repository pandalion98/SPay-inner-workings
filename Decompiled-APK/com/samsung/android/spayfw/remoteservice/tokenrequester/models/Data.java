package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

import com.google.gson.JsonObject;

public class Data {
    private JsonObject data;

    public JsonObject getData() {
        return this.data;
    }

    public void setData(JsonObject jsonObject) {
        this.data = jsonObject;
    }
}
