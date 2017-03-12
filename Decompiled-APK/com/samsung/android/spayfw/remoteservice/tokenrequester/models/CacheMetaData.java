package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

public class CacheMetaData {
    private String hash;
    private String href;
    private String id;
    private String type;
    private String updatedAt;

    public CacheMetaData(String str, String str2, String str3, String str4) {
        this.id = str;
        this.href = str3;
        this.updatedAt = str4;
        this.type = str2;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        CacheMetaData cacheMetaData = (CacheMetaData) obj;
        if (this.id.equals(cacheMetaData.id)) {
            return this.type.equals(cacheMetaData.type);
        }
        return false;
    }

    public int hashCode() {
        int i = 0;
        if (this.id != null) {
            i = this.id.hashCode();
        }
        if (this.type != null) {
            return (i * 31) + this.type.hashCode();
        }
        return i;
    }

    public String toString() {
        return "CacheMetaData{id='" + this.id + '\'' + ", type='" + this.type + '\'' + ", href='" + this.href + '\'' + ", updatedAt='" + this.updatedAt + '\'' + ", hash='" + this.hash + '\'' + '}';
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

    public String getHref() {
        return this.href;
    }

    public String getHash() {
        return this.hash;
    }
}
