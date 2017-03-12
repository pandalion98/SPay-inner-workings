package com.americanexpress.mobilepayments.hceclient.utils.common;

public class DOLTag {
    private boolean isConstructed;
    private int skipLen;
    private short tagLength;
    private String tagName;
    private short tagOffset;
    private String tagValue;

    public short getTagLength() {
        return this.tagLength;
    }

    public void setTagLength(short s) {
        this.tagLength = s;
    }

    public int getSkipLen() {
        return this.skipLen;
    }

    public void setSkipLen(int i) {
        this.skipLen = i;
    }

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

    public short getTagOffset() {
        return this.tagOffset;
    }

    public void setTagOffset(short s) {
        this.tagOffset = s;
    }

    public void setIsConstructed(boolean z) {
        this.isConstructed = z;
    }

    public boolean isConstructed() {
        return this.isConstructed;
    }

    public String toString() {
        return "PDOL{tagName='" + this.tagName + '\'' + ", tagLength='" + this.tagLength + '\'' + ", tagValue='" + this.tagValue + '\'' + ", tagOffset='" + this.tagOffset + '\'' + '}';
    }
}
