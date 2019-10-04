/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Comparable
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 */
package com.americanexpress.mobilepayments.hceclient.utils.tlv.framework;

public class TagKey
implements Comparable<TagKey> {
    private String dgi;
    private String key;
    private String tag;
    private int weight = 0;

    public TagKey() {
    }

    public TagKey(String string, String string2, int n2) {
        this.dgi = string;
        this.tag = string2;
        this.weight = n2;
    }

    public static TagKey fromString(String string) {
        String[] arrstring = string.split("-");
        TagKey tagKey = new TagKey();
        if (arrstring.length == 3) {
            tagKey.setDgi(arrstring[0]);
            tagKey.setTag(arrstring[1]);
            tagKey.setWeight(Integer.parseInt((String)arrstring[2]));
            return tagKey;
        }
        if (arrstring.length == 2) {
            tagKey.setKey(arrstring[0]);
            tagKey.setWeight(Integer.parseInt((String)arrstring[1]));
            return tagKey;
        }
        return null;
    }

    public int compareTo(TagKey tagKey) {
        return Integer.compare((int)this.weight, (int)tagKey.weight);
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null) {
            return false;
        }
        if (this.getClass() != object.getClass()) {
            return false;
        }
        TagKey tagKey = (TagKey)object;
        if (this.dgi == null ? tagKey.dgi != null : !this.dgi.equals((Object)tagKey.dgi)) {
            return false;
        }
        if (this.tag == null) {
            if (tagKey.tag == null) return true;
            return false;
        }
        if (!this.tag.equals((Object)tagKey.tag)) return false;
        return true;
    }

    public String getDgi() {
        return this.dgi;
    }

    public String getKey() {
        if (this.key == null || this.key.isEmpty()) {
            return this.dgi + "-" + this.tag;
        }
        return this.key;
    }

    public String getTag() {
        return this.tag;
    }

    public int getWeight() {
        return this.weight;
    }

    /*
     * Enabled aggressive block sorting
     */
    public int hashCode() {
        int n2 = this.dgi == null ? 0 : this.dgi.hashCode();
        int n3 = 31 * (n2 + 31);
        int n4 = this.key == null ? 0 : this.key.hashCode();
        int n5 = 31 * (n4 + n3);
        String string = this.tag;
        int n6 = 0;
        if (string == null) {
            return 31 * (n5 + n6) + this.weight;
        }
        n6 = this.tag.hashCode();
        return 31 * (n5 + n6) + this.weight;
    }

    public void setDgi(String string) {
        this.dgi = string;
    }

    public void setKey(String string) {
        this.key = string;
    }

    public void setTag(String string) {
        this.tag = string;
    }

    public void setWeight(int n2) {
        this.weight = n2;
    }

    public String toString() {
        return this.getKey() + "-" + this.weight;
    }
}

