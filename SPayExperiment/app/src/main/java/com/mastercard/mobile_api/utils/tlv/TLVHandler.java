/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.mastercard.mobile_api.utils.tlv;

public abstract class TLVHandler {
    public abstract void parseTag(byte var1, int var2, byte[] var3, int var4);

    public abstract void parseTag(short var1, int var2, byte[] var3, int var4);
}

