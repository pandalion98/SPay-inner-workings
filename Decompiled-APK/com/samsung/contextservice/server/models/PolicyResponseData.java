package com.samsung.contextservice.server.models;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.samsung.contextclient.data.JsonWraper;

public class PolicyResponseData extends JsonWraper {
    private transient String href;
    private String id;
    private JsonElement policy;
    private String version;

    public String getId() {
        return this.id;
    }

    public void setId(String str) {
        this.id = str;
    }

    public String getHref() {
        return this.href;
    }

    public void setHref(String str) {
        this.href = str;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String str) {
        this.version = str;
    }

    public JsonElement getPolicy() {
        return this.policy;
    }

    public CtxPolicy getPolicyObject() {
        return (CtxPolicy) new Gson().fromJson(this.policy, CtxPolicy.class);
    }

    public void setPolicy(JsonElement jsonElement) {
        this.policy = jsonElement;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{\nid:");
        stringBuilder.append(this.id);
        stringBuilder.append(",\nhref:");
        stringBuilder.append(this.href);
        stringBuilder.append(",\nversion:");
        stringBuilder.append(this.version);
        stringBuilder.append(",\npolicy:");
        stringBuilder.append(this.policy);
        stringBuilder.append("\n}");
        return stringBuilder.toString();
    }
}
