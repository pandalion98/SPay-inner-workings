/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.remoteservice.models;

public class Eula {
    private String content;
    private transient String localFilePath;
    private String locale;
    private String type;
    private String url;
    private String usage;

    public String getContent() {
        return this.content;
    }

    public String getLocalFilePath() {
        return this.localFilePath;
    }

    public String getLocale() {
        return this.locale;
    }

    public String getType() {
        return this.type;
    }

    public String getUrl() {
        return this.url;
    }

    public String getUsage() {
        return this.usage;
    }

    public void setLocalFilePath(String string) {
        this.localFilePath = string;
    }
}

