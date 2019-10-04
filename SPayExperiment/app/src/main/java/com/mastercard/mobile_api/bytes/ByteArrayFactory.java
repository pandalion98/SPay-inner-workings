/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Boolean
 *  java.lang.Object
 *  java.lang.String
 */
package com.mastercard.mobile_api.bytes;

import com.mastercard.mobile_api.bytes.ByteArray;

public abstract class ByteArrayFactory {
    static ByteArrayFactory INSTANCE;

    public static ByteArrayFactory getInstance() {
        return INSTANCE;
    }

    public static void setInstance(ByteArrayFactory byteArrayFactory) {
        INSTANCE = byteArrayFactory;
    }

    public abstract boolean compareString(String var1, String var2);

    public abstract ByteArray convertString(String var1);

    public abstract String formatAtc(int var1);

    public abstract int fromChar(char var1);

    public abstract ByteArray fromHexString(String var1);

    public abstract ByteArray getByteArray(int var1);

    public abstract ByteArray getByteArray(byte[] var1, int var2);

    public abstract ByteArray getFromByteArray(ByteArray var1);

    public abstract ByteArray getFromWord(int var1);

    public abstract String getHexStringLengthAsHex(String var1);

    public abstract String getHexStringLengthAsHex(String var1, int var2);

    public abstract String getStringLengthAsHex(String var1);

    public abstract String getUTF8String(ByteArray var1);

    public abstract String hexStringToBase64(String var1);

    public abstract int hexToInteger(String var1);

    public abstract String integerToHex(int var1);

    public Boolean isBitSet(byte by, int n2) {
        if ((by & 1 << n2) != 0) {
            return true;
        }
        return false;
    }

    public abstract boolean isNull(String var1);

    public abstract String padleft(String var1, int var2, char var3);

    public abstract int stringToInt(String var1);

    public abstract char toChar(int var1);

    public abstract String toString(ByteArray var1, int var2, int var3);
}

