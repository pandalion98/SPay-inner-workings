package com.samsung.contextclient.data;

import com.google.gson.GsonBuilder;
import org.bouncycastle.jce.X509KeyUsage;

public abstract class JsonWraper {
    public String toJson() {
        return new GsonBuilder().excludeFieldsWithModifiers(X509KeyUsage.digitalSignature).create().toJson((Object) this);
    }

    public String toString() {
        return toJson();
    }
}
