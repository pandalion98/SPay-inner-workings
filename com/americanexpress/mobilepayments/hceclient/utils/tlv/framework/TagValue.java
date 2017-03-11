package com.americanexpress.mobilepayments.hceclient.utils.tlv.framework;

public class TagValue {
    private String value;

    public String getValue() {
        return this.value;
    }

    public void setValue(String str) {
        this.value = str;
    }

    public String toString() {
        return this.value;
    }

    public static TagValue fromString(String str) {
        TagValue tagValue = new TagValue();
        tagValue.setValue(str);
        return tagValue;
    }
}
