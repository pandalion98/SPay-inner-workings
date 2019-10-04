/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 */
package com.samsung.contextservice.server.models;

import com.google.gson.Gson;
import com.samsung.contextclient.data.JsonWraper;
import com.samsung.contextclient.data.Poi;
import java.util.ArrayList;

public class CacheFile
extends JsonWraper {
    private String id;
    private ArrayList<Poi> pois;
    private String version;

    public static CacheFile fromJson(String string) {
        return (CacheFile)new Gson().fromJson(string, CacheFile.class);
    }

    public String getId() {
        return this.id;
    }

    public ArrayList<Poi> getPois() {
        return this.pois;
    }

    public String getVersion() {
        return this.version;
    }

    public void setId(String string) {
        this.id = string;
    }

    public void setPois(ArrayList<Poi> arrayList) {
        this.pois = arrayList;
    }

    public void setVersion(String string) {
        this.version = string;
    }

    @Override
    public String toString() {
        return this.toJson();
    }
}

