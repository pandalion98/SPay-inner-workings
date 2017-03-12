package com.samsung.android.spayfw.remoteservice.models;

public class Art {
    private String content;
    private int height;
    private transient String localFilePath;
    private String type;
    private String url;
    private String usage;
    private int width;

    public String getContent() {
        return this.content;
    }

    public int getHeight() {
        return this.height;
    }

    public String getLocalFilePath() {
        return this.localFilePath;
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

    public int getWidth() {
        return this.width;
    }

    public void setLocalFilePath(String str) {
        this.localFilePath = str;
    }
}
