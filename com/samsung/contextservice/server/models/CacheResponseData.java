package com.samsung.contextservice.server.models;

import com.samsung.contextclient.data.JsonWraper;
import java.util.ArrayList;

public class CacheResponseData extends JsonWraper {
    private ArrayList<Cache> caches;
    private transient String id;

    public String getId() {
        return this.id;
    }

    public void setId(String str) {
        this.id = str;
    }

    public ArrayList<Cache> getCaches() {
        return this.caches;
    }

    public void setCaches(ArrayList<Cache> arrayList) {
        this.caches = arrayList;
    }

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
