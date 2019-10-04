/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 */
package com.americanexpress.sdkmodulelib.tlv;

public final class TagValueType
extends Enum<TagValueType> {
    private static final /* synthetic */ TagValueType[] $VALUES;
    public static final /* enum */ TagValueType BINARY = new TagValueType();
    public static final /* enum */ TagValueType DOL;
    public static final /* enum */ TagValueType MIXED;
    public static final /* enum */ TagValueType NUMERIC;
    public static final /* enum */ TagValueType TEMPLATE;
    public static final /* enum */ TagValueType TEXT;

    static {
        NUMERIC = new TagValueType();
        TEXT = new TagValueType();
        MIXED = new TagValueType();
        DOL = new TagValueType();
        TEMPLATE = new TagValueType();
        TagValueType[] arrtagValueType = new TagValueType[]{BINARY, NUMERIC, TEXT, MIXED, DOL, TEMPLATE};
        $VALUES = arrtagValueType;
    }

    public static TagValueType valueOf(String string) {
        return (TagValueType)Enum.valueOf(TagValueType.class, (String)string);
    }

    public static TagValueType[] values() {
        return (TagValueType[])$VALUES.clone();
    }
}

