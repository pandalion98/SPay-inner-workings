/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.americanexpress.sdkmodulelib.model;

public class TagInfo {
    private String rawEncodedLength;
    private int tagLength;
    private String tagName;
    private String tagValue;

    public String getRawEncodedLength() {
        return this.rawEncodedLength;
    }

    public int getTagLength() {
        return this.tagLength;
    }

    public String getTagName() {
        return this.tagName;
    }

    public String getTagValue() {
        return this.tagValue;
    }

    public void setRawEncodedLength(String string) {
        this.rawEncodedLength = string;
    }

    public void setTagLength(int n2) {
        this.tagLength = n2;
    }

    public void setTagName(String string) {
        this.tagName = string;
    }

    public void setTagValue(String string) {
        this.tagValue = string;
    }
}

