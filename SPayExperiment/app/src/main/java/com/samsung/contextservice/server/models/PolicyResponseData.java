/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.JsonElement
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 */
package com.samsung.contextservice.server.models;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.samsung.contextclient.data.JsonWraper;
import com.samsung.contextservice.server.models.CtxPolicy;

public class PolicyResponseData
extends JsonWraper {
    private transient String href;
    private String id;
    private JsonElement policy;
    private String version;

    public String getHref() {
        return this.href;
    }

    public String getId() {
        return this.id;
    }

    public JsonElement getPolicy() {
        return this.policy;
    }

    public CtxPolicy getPolicyObject() {
        return (CtxPolicy)new Gson().fromJson(this.policy, CtxPolicy.class);
    }

    public String getVersion() {
        return this.version;
    }

    public void setHref(String string) {
        this.href = string;
    }

    public void setId(String string) {
        this.id = string;
    }

    public void setPolicy(JsonElement jsonElement) {
        this.policy = jsonElement;
    }

    public void setVersion(String string) {
        this.version = string;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{\nid:");
        stringBuilder.append(this.id);
        stringBuilder.append(",\nhref:");
        stringBuilder.append(this.href);
        stringBuilder.append(",\nversion:");
        stringBuilder.append(this.version);
        stringBuilder.append(",\npolicy:");
        stringBuilder.append((Object)this.policy);
        stringBuilder.append("\n}");
        return stringBuilder.toString();
    }
}

