package com.americanexpress.mobilepayments.hceclient.utils.tlv.framework;

import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;

public class TagKey implements Comparable<TagKey> {
    private String dgi;
    private String key;
    private String tag;
    private int weight;

    public TagKey() {
        this.weight = 0;
    }

    public TagKey(String str, String str2, int i) {
        this.weight = 0;
        this.dgi = str;
        this.tag = str2;
        this.weight = i;
    }

    public String getDgi() {
        return this.dgi;
    }

    public void setDgi(String str) {
        this.dgi = str;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String str) {
        this.tag = str;
    }

    public String getKey() {
        if (this.key == null || this.key.isEmpty()) {
            return this.dgi + HCEClientConstants.TAG_KEY_SEPARATOR + this.tag;
        }
        return this.key;
    }

    public void setKey(String str) {
        this.key = str;
    }

    public int getWeight() {
        return this.weight;
    }

    public void setWeight(int i) {
        this.weight = i;
    }

    public int compareTo(TagKey tagKey) {
        return Integer.compare(this.weight, tagKey.weight);
    }

    public String toString() {
        return getKey() + HCEClientConstants.TAG_KEY_SEPARATOR + this.weight;
    }

    public static TagKey fromString(String str) {
        String[] split = str.split(HCEClientConstants.TAG_KEY_SEPARATOR);
        TagKey tagKey = new TagKey();
        if (split.length == 3) {
            tagKey.setDgi(split[0]);
            tagKey.setTag(split[1]);
            tagKey.setWeight(Integer.parseInt(split[2]));
            return tagKey;
        } else if (split.length != 2) {
            return null;
        } else {
            tagKey.setKey(split[0]);
            tagKey.setWeight(Integer.parseInt(split[1]));
            return tagKey;
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        TagKey tagKey = (TagKey) obj;
        if (this.dgi == null) {
            if (tagKey.dgi != null) {
                return false;
            }
        } else if (!this.dgi.equals(tagKey.dgi)) {
            return false;
        }
        if (this.tag == null) {
            if (tagKey.tag != null) {
                return false;
            }
            return true;
        } else if (this.tag.equals(tagKey.tag)) {
            return true;
        } else {
            return false;
        }
    }

    public int hashCode() {
        int i = 0;
        int hashCode = ((this.key == null ? 0 : this.key.hashCode()) + (((this.dgi == null ? 0 : this.dgi.hashCode()) + 31) * 31)) * 31;
        if (this.tag != null) {
            i = this.tag.hashCode();
        }
        return ((hashCode + i) * 31) + this.weight;
    }
}
