package com.americanexpress.sdkmodulelib.model;

public class TagInfo {
    private String rawEncodedLength;
    private int tagLength;
    private String tagName;
    private String tagValue;

    public String getTagName() {
        return this.tagName;
    }

    public void setTagName(String str) {
        this.tagName = str;
    }

    public String getTagValue() {
        return this.tagValue;
    }

    public void setTagValue(String str) {
        this.tagValue = str;
    }

    public int getTagLength() {
        return this.tagLength;
    }

    public void setTagLength(int i) {
        this.tagLength = i;
    }

    public String getRawEncodedLength() {
        return this.rawEncodedLength;
    }

    public void setRawEncodedLength(String str) {
        this.rawEncodedLength = str;
    }
}
