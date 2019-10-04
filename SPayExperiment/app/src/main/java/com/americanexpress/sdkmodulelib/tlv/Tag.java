/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 */
package com.americanexpress.sdkmodulelib.tlv;

import com.americanexpress.sdkmodulelib.tlv.TagType;
import com.americanexpress.sdkmodulelib.tlv.TagValueType;

public interface Tag {
    public String getDescription();

    public String getName();

    public int getNumTagBytes();

    public byte[] getTagBytes();

    public Class getTagClass();

    public TagValueType getTagValueType();

    public TagType getType();

    public boolean isConstructed();

    public static final class Class
    extends Enum<Class> {
        private static final /* synthetic */ Class[] $VALUES;
        public static final /* enum */ Class APPLICATION;
        public static final /* enum */ Class CONTEXT_SPECIFIC;
        public static final /* enum */ Class PRIVATE;
        public static final /* enum */ Class UNIVERSAL;

        static {
            UNIVERSAL = new Class();
            APPLICATION = new Class();
            CONTEXT_SPECIFIC = new Class();
            PRIVATE = new Class();
            Class[] arrclass = new Class[]{UNIVERSAL, APPLICATION, CONTEXT_SPECIFIC, PRIVATE};
            $VALUES = arrclass;
        }

        public static Class valueOf(String string) {
            return (Class)Enum.valueOf(Class.class, (String)string);
        }

        public static Class[] values() {
            return (Class[])$VALUES.clone();
        }
    }

}

