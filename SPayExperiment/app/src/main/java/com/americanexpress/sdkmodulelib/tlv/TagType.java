/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 */
package com.americanexpress.sdkmodulelib.tlv;

public final class TagType
extends Enum<TagType> {
    private static final /* synthetic */ TagType[] $VALUES;
    public static final /* enum */ TagType CONSTRUCTED;
    public static final /* enum */ TagType PRIMITIVE;

    static {
        PRIMITIVE = new TagType();
        CONSTRUCTED = new TagType();
        TagType[] arrtagType = new TagType[]{PRIMITIVE, CONSTRUCTED};
        $VALUES = arrtagType;
    }

    public static TagType valueOf(String string) {
        return (TagType)Enum.valueOf(TagType.class, (String)string);
    }

    public static TagType[] values() {
        return (TagType[])$VALUES.clone();
    }
}

