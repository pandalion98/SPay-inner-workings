/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.util.ArrayList
 */
package com.samsung.contextservice.server.models;

import com.samsung.contextclient.data.JsonWraper;
import com.samsung.contextservice.server.models.Cache;
import java.util.ArrayList;

public class CacheResponseData
extends JsonWraper {
    private ArrayList<Cache> caches;
    private transient String id;

    public ArrayList<Cache> getCaches() {
        return this.caches;
    }

    public String getId() {
        return this.id;
    }

    public void setCaches(ArrayList<Cache> arrayList) {
        this.caches = arrayList;
    }

    public void setId(String string) {
        this.id = string;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{\n");
        if (this.id != null) {
            stringBuilder.append("id:");
            stringBuilder.append(this.id);
            stringBuilder.append(",\n");
        }
        if (this.caches != null) {
            stringBuilder.append("caches:");
            stringBuilder.append(this.caches.toString());
            stringBuilder.append("\n");
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}

