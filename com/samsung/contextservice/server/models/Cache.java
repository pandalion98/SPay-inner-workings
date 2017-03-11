package com.samsung.contextservice.server.models;

import com.samsung.contextclient.data.JsonWraper;

public class Cache extends JsonWraper {
    private String contentHash;
    private long expireAt;
    private String geohash;
    private String href;
    private String id;
    private long updatedAt;

    public void setId(String str) {
        this.id = str;
    }

    public String getId() {
        return this.id;
    }

    public void setHref(String str) {
        this.href = str;
    }

    public String getHref() {
        return this.href;
    }

    public void setGeohash(String str) {
        this.geohash = str;
    }

    public String getGeohash() {
        return this.geohash;
    }

    public void setUpdatedAt(long j) {
        this.updatedAt = j;
    }

    public long getUpdatedAt() {
        return this.updatedAt;
    }

    public void setExpireAt(long j) {
        this.expireAt = j;
    }

    public long getExpireAt() {
        return this.expireAt;
    }

    public void setContentHash(String str) {
        this.contentHash = str;
    }

    public String getContentHash() {
        return this.contentHash;
    }

    public String toString() {
        return "id:" + this.id + ", href:" + this.href + ", geohash:" + this.geohash + ", updatedAt:" + this.updatedAt + ", expireAt:" + this.expireAt + ", contentHash:" + this.contentHash;
    }
}
