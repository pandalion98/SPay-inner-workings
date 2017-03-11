package com.samsung.android.spayfw.remoteservice.models;

public class Eula {
    private String content;
    private transient String localFilePath;
    private String locale;
    private String type;
    private String url;
    private String usage;

    public String getLocalFilePath() {
        return this.localFilePath;
    }

    public void setLocalFilePath(String str) {
        this.localFilePath = str;
    }

    public String getContent() {
        return this.content;
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
}
