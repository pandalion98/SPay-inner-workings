/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.americanexpress.mobilepayments.hceclient.utils.tlv.framework;

import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagKey;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagValue;

public class TagDetail {
    private TagKey tagKey;
    private TagValue tagValue;

    public TagKey getTagKey() {
        return this.tagKey;
    }

    public TagValue getTagValue() {
        return this.tagValue;
    }

    public void setTagKey(TagKey tagKey) {
        this.tagKey = tagKey;
    }

    public void setTagValue(TagValue tagValue) {
        this.tagValue = tagValue;
    }

    public String toString() {
        return "TagDetail [tagKey=" + this.tagKey + ", value=" + this.tagValue + "]";
    }
}

