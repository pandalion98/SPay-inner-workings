/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.americanexpress.mobilepayments.hceclient.utils.common;

public class DOLTag {
    private boolean isConstructed;
    private int skipLen;
    private short tagLength;
    private String tagName;
    private short tagOffset;
    private String tagValue;

    public int getSkipLen() {
        return this.skipLen;
    }

    public short getTagLength() {
        return this.tagLength;
    }

    public String getTagName() {
        return this.tagName;
    }

    public short getTagOffset() {
        return this.tagOffset;
    }

    public String getTagValue() {
        return this.tagValue;
    }

    public boolean isConstructed() {
        return this.isConstructed;
    }

    public void setIsConstructed(boolean bl) {
        this.isConstructed = bl;
    }

    public void setSkipLen(int n2) {
        this.skipLen = n2;
    }

    public void setTagLength(short s2) {
        this.tagLength = s2;
    }

    public void setTagName(String string) {
        this.tagName = string;
    }

    public void setTagOffset(short s2) {
        this.tagOffset = s2;
    }

    public void setTagValue(String string) {
        this.tagValue = string;
    }

    public String toString() {
        return "PDOL{tagName='" + this.tagName + '\'' + ", tagLength='" + this.tagLength + '\'' + ", tagValue='" + this.tagValue + '\'' + ", tagOffset='" + this.tagOffset + '\'' + '}';
    }
}

