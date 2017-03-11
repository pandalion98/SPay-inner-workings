package com.americanexpress.mobilepayments.hceclient.utils.tlv.framework;

public class TagDetail {
    private TagKey tagKey;
    private TagValue tagValue;

    public TagValue getTagValue() {
        return this.tagValue;
    }

    public void setTagValue(TagValue tagValue) {
        this.tagValue = tagValue;
    }

    public TagKey getTagKey() {
        return this.tagKey;
    }

    public void setTagKey(TagKey tagKey) {
        this.tagKey = tagKey;
    }

    public String toString() {
        return "TagDetail [tagKey=" + this.tagKey + ", value=" + this.tagValue + "]";
    }
}
