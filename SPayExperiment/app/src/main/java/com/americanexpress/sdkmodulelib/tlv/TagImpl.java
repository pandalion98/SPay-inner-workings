/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.PrintStream
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.lang.System
 *  java.util.Arrays
 */
package com.americanexpress.sdkmodulelib.tlv;

import com.americanexpress.sdkmodulelib.tlv.Tag;
import com.americanexpress.sdkmodulelib.tlv.TagType;
import com.americanexpress.sdkmodulelib.tlv.TagValueType;
import com.americanexpress.sdkmodulelib.tlv.Util;
import java.io.PrintStream;
import java.util.Arrays;

public class TagImpl
implements Tag {
    String description;
    byte[] idBytes;
    String name;
    Tag.Class tagClass;
    TagValueType tagValueType;
    TagType type;

    public TagImpl(String string, TagValueType tagValueType, String string2, String string3) {
        this.build(Util.fromHexString(string), tagValueType, string2, string3);
    }

    public TagImpl(byte[] arrby, TagValueType tagValueType, String string, String string2) {
        this.build(arrby, tagValueType, string, string2);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void build(byte[] arrby, TagValueType tagValueType, String string, String string2) {
        if (arrby == null) {
            throw new IllegalArgumentException("Param id cannot be null");
        }
        if (arrby.length == 0) {
            throw new IllegalArgumentException("Param id cannot be empty");
        }
        if (tagValueType == null) {
            throw new IllegalArgumentException("Param tagValueType cannot be null");
        }
        this.idBytes = arrby;
        String string3 = string != null ? string : "";
        this.name = string3;
        if (string2 == null) {
            string2 = "";
        }
        this.description = string2;
        this.tagValueType = tagValueType;
        this.type = Util.isBitSet(this.idBytes[0], 6) ? TagType.CONSTRUCTED : TagType.PRIMITIVE;
        byte by = (byte)(3 & this.idBytes[0] >>> 6);
        switch (by) {
            default: {
                throw new RuntimeException("UNEXPECTED TAG CLASS: " + Util.byte2BinaryLiteral(by) + " " + Util.byteArrayToHexString(this.idBytes) + " " + string);
            }
            case 0: {
                this.tagClass = Tag.Class.UNIVERSAL;
                return;
            }
            case 1: {
                this.tagClass = Tag.Class.APPLICATION;
                return;
            }
            case 2: {
                this.tagClass = Tag.Class.CONTEXT_SPECIFIC;
                return;
            }
            case 3: 
        }
        this.tagClass = Tag.Class.PRIVATE;
    }

    public static void main(String[] arrstring) {
        TagImpl tagImpl = new TagImpl("bf0c", TagValueType.BINARY, "", "");
        System.out.println((Object)tagImpl);
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean equals(Object object) {
        Tag tag;
        block3 : {
            block2 : {
                if (!(object instanceof Tag)) break block2;
                tag = (Tag)object;
                if (this.getTagBytes().length == tag.getTagBytes().length) break block3;
            }
            return false;
        }
        return Arrays.equals((byte[])this.getTagBytes(), (byte[])tag.getTagBytes());
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getNumTagBytes() {
        return this.idBytes.length;
    }

    @Override
    public byte[] getTagBytes() {
        return this.idBytes;
    }

    @Override
    public Tag.Class getTagClass() {
        return this.tagClass;
    }

    @Override
    public TagValueType getTagValueType() {
        return this.tagValueType;
    }

    @Override
    public TagType getType() {
        return this.type;
    }

    public int hashCode() {
        return 177 + Arrays.hashCode((byte[])this.idBytes);
    }

    @Override
    public boolean isConstructed() {
        return this.type == TagType.CONSTRUCTED;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Tag[");
        stringBuilder.append(Util.byteArrayToHexString(this.getTagBytes()));
        stringBuilder.append("] Name=");
        stringBuilder.append(this.getName());
        stringBuilder.append(", TagType=");
        stringBuilder.append((Object)this.getType());
        stringBuilder.append(", ValueType=");
        stringBuilder.append((Object)this.getTagValueType());
        stringBuilder.append(", Class=");
        stringBuilder.append((Object)this.tagClass);
        return stringBuilder.toString();
    }
}

