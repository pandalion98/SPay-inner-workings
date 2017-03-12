package com.americanexpress.sdkmodulelib.tlv;

import java.util.Arrays;

public class TagAndLength {
    private int length;
    private Tag tag;

    public TagAndLength(Tag tag, int i) {
        this.tag = tag;
        this.length = i;
    }

    public Tag getTag() {
        return this.tag;
    }

    public int getLength() {
        return this.length;
    }

    public byte[] getBytes() {
        byte[] tagBytes = this.tag.getTagBytes();
        tagBytes = Arrays.copyOf(tagBytes, tagBytes.length + 1);
        tagBytes[tagBytes.length - 1] = (byte) this.length;
        return tagBytes;
    }

    public String toString() {
        return this.tag.toString() + " length: " + this.length;
    }
}
