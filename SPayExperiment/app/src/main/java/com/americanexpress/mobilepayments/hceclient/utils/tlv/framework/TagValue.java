/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.americanexpress.mobilepayments.hceclient.utils.tlv.framework;

public class TagValue {
    private String value;

    public static TagValue fromString(String string) {
        TagValue tagValue = new TagValue();
        tagValue.setValue(string);
        return tagValue;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String string) {
        this.value = string;
    }

    public String toString() {
        return this.value;
    }
}

