/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Arrays
 */
package com.americanexpress.sdkmodulelib.tlv;

import com.americanexpress.sdkmodulelib.tlv.Tag;
import java.util.Arrays;

public class TagAndLength {
    private int length;
    private Tag tag;

    public TagAndLength(Tag tag, int n2) {
        this.tag = tag;
        this.length = n2;
    }

    public byte[] getBytes() {
        byte[] arrby = this.tag.getTagBytes();
        byte[] arrby2 = Arrays.copyOf((byte[])arrby, (int)(1 + arrby.length));
        arrby2[-1 + arrby2.length] = (byte)this.length;
        return arrby2;
    }

    public int getLength() {
        return this.length;
    }

    public Tag getTag() {
        return this.tag;
    }

    public String toString() {
        return this.tag.toString() + " length: " + this.length;
    }
}

