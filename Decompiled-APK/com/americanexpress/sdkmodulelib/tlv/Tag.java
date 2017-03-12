package com.americanexpress.sdkmodulelib.tlv;

public interface Tag {

    public enum Class {
        UNIVERSAL,
        APPLICATION,
        CONTEXT_SPECIFIC,
        PRIVATE
    }

    String getDescription();

    String getName();

    int getNumTagBytes();

    byte[] getTagBytes();

    Class getTagClass();

    TagValueType getTagValueType();

    TagType getType();

    boolean isConstructed();
}
