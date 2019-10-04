/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

public class CacheMetaData {
    private String hash;
    private String href;
    private String id;
    private String type;
    private String updatedAt;

    public CacheMetaData(String string, String string2, String string3, String string4) {
        this.id = string;
        this.href = string3;
        this.updatedAt = string4;
        this.type = string2;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        boolean bl = false;
        if (object == null) return bl;
        Class class_ = this.getClass();
        Class class_2 = object.getClass();
        bl = false;
        if (class_ != class_2) return bl;
        CacheMetaData cacheMetaData = (CacheMetaData)object;
        boolean bl2 = this.id.equals((Object)cacheMetaData.id);
        bl = false;
        if (!bl2) return bl;
        return this.type.equals((Object)cacheMetaData.type);
    }

    public String getHash() {
        return this.hash;
    }

    public String getHref() {
        return this.href;
    }

    public String getId() {
        return this.id;
    }

    public String getType() {
        return this.type;
    }

    public String getUpdatedAt() {
        return this.updatedAt;
    }

    public int hashCode() {
        String string = this.id;
        int n2 = 0;
        if (string != null) {
            n2 = this.id.hashCode();
        }
        if (this.type != null) {
            n2 = n2 * 31 + this.type.hashCode();
        }
        return n2;
    }

    public String toString() {
        return "CacheMetaData{id='" + this.id + '\'' + ", type='" + this.type + '\'' + ", href='" + this.href + '\'' + ", updatedAt='" + this.updatedAt + '\'' + ", hash='" + this.hash + '\'' + '}';
    }
}

