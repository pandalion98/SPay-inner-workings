package com.mastercard.mobile_api.bytes;

public abstract class ByteArrayFactory {
    static ByteArrayFactory INSTANCE;

    public abstract boolean compareString(String str, String str2);

    public abstract ByteArray convertString(String str);

    public abstract String formatAtc(int i);

    public abstract int fromChar(char c);

    public abstract ByteArray fromHexString(String str);

    public abstract ByteArray getByteArray(int i);

    public abstract ByteArray getByteArray(byte[] bArr, int i);

    public abstract ByteArray getFromByteArray(ByteArray byteArray);

    public abstract ByteArray getFromWord(int i);

    public abstract String getHexStringLengthAsHex(String str);

    public abstract String getHexStringLengthAsHex(String str, int i);

    public abstract String getStringLengthAsHex(String str);

    public abstract String getUTF8String(ByteArray byteArray);

    public abstract String hexStringToBase64(String str);

    public abstract int hexToInteger(String str);

    public abstract String integerToHex(int i);

    public abstract boolean isNull(String str);

    public abstract String padleft(String str, int i, char c);

    public abstract int stringToInt(String str);

    public abstract char toChar(int i);

    public abstract String toString(ByteArray byteArray, int i, int i2);

    public static ByteArrayFactory getInstance() {
        return INSTANCE;
    }

    public static void setInstance(ByteArrayFactory byteArrayFactory) {
        INSTANCE = byteArrayFactory;
    }

    public Boolean isBitSet(byte b, int i) {
        return ((1 << i) & b) != 0 ? Boolean.valueOf(true) : Boolean.valueOf(false);
    }
}
