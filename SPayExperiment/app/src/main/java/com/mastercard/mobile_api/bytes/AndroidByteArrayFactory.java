/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.util.Log
 *  java.io.UnsupportedEncodingException
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.nio.ByteBuffer
 */
package com.mastercard.mobile_api.bytes;

import android.util.Log;
import com.mastercard.mobile_api.bytes.AndroidByteArray;
import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.bytes.ByteArrayFactory;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class AndroidByteArrayFactory
extends ByteArrayFactory {
    protected static final char[] HEX_DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    @Override
    public boolean compareString(String string, String string2) {
        return string.equals((Object)string2);
    }

    @Override
    public ByteArray convertString(String string) {
        return new AndroidByteArray(string.getBytes(), string.length());
    }

    @Override
    public String formatAtc(int n2) {
        return this.padleft(this.integerToHex(n2), 4, '0').toUpperCase();
    }

    @Override
    public int fromChar(char c2) {
        switch (c2) {
            default: {
                throw new IllegalArgumentException("Invalid hex digit [" + c2 + "]");
            }
            case '0': 
            case '1': 
            case '2': 
            case '3': 
            case '4': 
            case '5': 
            case '6': 
            case '7': 
            case '8': 
            case '9': {
                return c2 - 48;
            }
            case 'A': 
            case 'B': 
            case 'C': 
            case 'D': 
            case 'E': 
            case 'F': {
                return 10 + (c2 - 65);
            }
            case 'a': 
            case 'b': 
            case 'c': 
            case 'd': 
            case 'e': 
            case 'f': 
        }
        return 10 + (c2 - 97);
    }

    @Override
    public ByteArray fromHexString(String string) {
        return new AndroidByteArray(string);
    }

    @Override
    public ByteArray getByteArray(int n2) {
        return new AndroidByteArray(n2);
    }

    @Override
    public ByteArray getByteArray(byte[] arrby, int n2) {
        return new AndroidByteArray(arrby, n2);
    }

    @Override
    public ByteArray getFromByteArray(ByteArray byteArray) {
        return new AndroidByteArray(byteArray.getBytes());
    }

    @Override
    public ByteArray getFromWord(int n2) {
        return new AndroidByteArray(ByteBuffer.allocate((int)2).putShort((short)n2).array(), 2);
    }

    @Override
    public String getHexStringLengthAsHex(String string) {
        return this.integerToHex(this.fromHexString(string).getLength()).toUpperCase();
    }

    @Override
    public String getHexStringLengthAsHex(String string, int n2) {
        return this.padleft(this.integerToHex(this.fromHexString(string).getLength()), n2 * 2, '0').toUpperCase();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public String getStringLengthAsHex(String string) {
        String string2;
        try {
            String string3;
            string2 = string3 = this.integerToHex(string.getBytes("UTF-8").length);
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            string2 = null;
            return string2.toUpperCase();
        }
        do {
            return string2.toUpperCase();
            break;
        } while (true);
    }

    @Override
    public String getUTF8String(ByteArray byteArray) {
        return new String(byteArray.getBytes());
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public String hexStringToBase64(String string) {
        int n2 = 0;
        String string2 = "";
        int n3 = 0;
        do {
            if (n3 >= string.length()) break;
            String string3 = "000000" + Integer.toBinaryString((int)Integer.parseInt((String)string.substring(n3, n3 + 1), (int)16));
            string2 = String.valueOf((Object)string2) + string3.substring(-4 + string3.length());
            ++n3;
        } while (true);
        String string4 = String.valueOf((Object)string2) + "000000".substring(string2.length() % 6);
        Log.d((String)"base64", (String)("bin: " + string4));
        String string5 = "";
        while (n2 < string4.length()) {
            String string6 = string4.substring(n2, n2 + 6);
            string5 = String.valueOf((Object)string5) + "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt(Integer.parseInt((String)string6, (int)2));
            n2 += 6;
        }
        return String.valueOf((Object)string5) + "====".substring(string5.length() % 4);
    }

    @Override
    public int hexToInteger(String string) {
        return Integer.parseInt((String)string, (int)16);
    }

    @Override
    public String integerToHex(int n2) {
        String string = Integer.toHexString((int)n2);
        if (string.length() % 2 != 0) {
            string = this.padleft(string, 1 + string.length(), '0');
        }
        return string.toUpperCase();
    }

    @Override
    public boolean isNull(String string) {
        return string == null;
    }

    @Override
    public String padleft(String string, int n2, char c2) {
        String string2 = string.trim();
        StringBuilder stringBuilder = new StringBuilder(n2);
        int n3 = n2 - string2.length();
        do {
            int n4 = n3 - 1;
            if (n3 <= 0) {
                return stringBuilder.append(string2).toString();
            }
            stringBuilder.append(c2);
            n3 = n4;
        } while (true);
    }

    @Override
    public int stringToInt(String string) {
        return Integer.parseInt((String)string);
    }

    @Override
    public char toChar(int n2) {
        if (n2 < 0 || n2 > 15) {
            throw new IllegalArgumentException("Invalid hex digit [" + n2 + "]");
        }
        return HEX_DIGITS[n2];
    }

    @Override
    public String toString(ByteArray byteArray, int n2, int n3) {
        char[] arrc = new char[n3 * 2];
        int n4 = 0;
        int n5 = n2;
        while (n5 < n2 + n3) {
            byte by = byteArray.getByte(n5);
            int n6 = n4 + 1;
            arrc[n4] = HEX_DIGITS[15 & by >>> 4];
            n4 = n6 + 1;
            arrc[n6] = HEX_DIGITS[by & 15];
            ++n5;
        }
        return new String(arrc);
    }
}

