/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 */
package com.samsung.contextservice.server.models;

import com.samsung.contextclient.data.JsonWraper;

public class Cache
extends JsonWraper {
    private String contentHash;
    private long expireAt;
    private String geohash;
    private String href;
    private String id;
    private long updatedAt;

    public String getContentHash() {
        return this.contentHash;
    }

    public long getExpireAt() {
        return this.expireAt;
    }

    public String getGeohash() {
        return this.geohash;
    }

    public String getHref() {
        return this.href;
    }

    public String getId() {
        return this.id;
    }

    public long getUpdatedAt() {
        return this.updatedAt;
    }

    public void setContentHash(String string) {
        this.contentHash = string;
    }

    public void setExpireAt(long l2) {
        this.expireAt = l2;
    }

    public void setGeohash(String string) {
        this.geohash = string;
    }

    public void setHref(String string) {
        this.href = string;
    }

    public void setId(String string) {
        this.id = string;
    }

    public void setUpdatedAt(long l2) {
        this.updatedAt = l2;
    }

    @Override
    public String toString() {
        return "id:" + this.id + ", href:" + this.href + ", geohash:" + this.geohash + ", updatedAt:" + this.updatedAt + ", expireAt:" + this.expireAt + ", contentHash:" + this.contentHash;
    }
}

