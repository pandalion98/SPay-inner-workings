package com.samsung.contextservice.server.models;

import com.google.gson.Gson;
import com.samsung.contextclient.data.JsonWraper;
import com.samsung.contextclient.data.Poi;
import java.util.ArrayList;

public class CacheFile extends JsonWraper {
    private String id;
    private ArrayList<Poi> pois;
    private String version;

    public void setId(String str) {
        this.id = str;
    }

    public String getId() {
        return this.id;
    }

    public void setVersion(String str) {
        this.version = str;
    }

    public String getVersion() {
        return this.version;
    }

    public void setPois(ArrayList<Poi> arrayList) {
        this.pois = arrayList;
    }

    public ArrayList<Poi> getPois() {
        return this.pois;
    }

    public static CacheFile fromJson(String str) {
        return (CacheFile) new Gson().fromJson(str, CacheFile.class);
    }

    public String toString() {
        return toJson();
    }
}
