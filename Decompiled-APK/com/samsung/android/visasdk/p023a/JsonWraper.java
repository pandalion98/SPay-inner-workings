package com.samsung.android.visasdk.p023a;

import com.google.gson.GsonBuilder;
import org.bouncycastle.jce.X509KeyUsage;

/* renamed from: com.samsung.android.visasdk.a.a */
public abstract class JsonWraper {
    public String toJson() {
        return new GsonBuilder().excludeFieldsWithModifiers(X509KeyUsage.digitalSignature).create().toJson((Object) this);
    }

    public String toString() {
        return toJson();
    }
}
