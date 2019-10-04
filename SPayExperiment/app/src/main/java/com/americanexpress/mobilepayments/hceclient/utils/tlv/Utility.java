/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.PrintStream
 *  java.lang.Character
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.lang.System
 */
package com.americanexpress.mobilepayments.hceclient.utils.tlv;

import java.io.PrintStream;

public class Utility {
    protected static final byte[] encodingTable = new byte[]{48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102};

    public static String byte2BinaryLiteral(byte by) {
        String string = Integer.toBinaryString((int)Utility.byteToInt(by));
        if (string.length() < 8) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i2 = 0; i2 < 8 - string.length(); ++i2) {
                stringBuilder.append('0');
            }
            stringBuilder.append(string);
            return stringBuilder.toString();
        }
        return string;
    }

    public static String byte2Hex(byte by) {
        String[] arrstring = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
        int n2 = by & 255;
        int n3 = 15 & n2 >>> 4;
        int n4 = n2 & 15;
        return arrstring[n3] + arrstring[n4];
    }

    public static String byteArrayToHexString(byte[] arrby) {
        if (arrby == null) {
            return "";
        }
        return Utility.byteArrayToHexString(arrby, 0, arrby.length);
    }

    public static String byteArrayToHexString(byte[] arrby, int n2, int n3) {
        if (arrby == null) {
            return "";
        }
        if (arrby.length < n2 + n3) {
            throw new IllegalArgumentException("startPos(" + n2 + ")+length(" + n3 + ") > byteArray.length(" + arrby.length + ")");
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i2 = 0; i2 < n3; ++i2) {
            stringBuilder.append(Integer.toHexString((int)(-256 | 255 & arrby[n2 + i2])).substring(6));
        }
        return stringBuilder.toString();
    }

    public static int byteArrayToInt(byte[] arrby) {
        return Utility.byteArrayToInt(arrby, 0, arrby.length);
    }

    public static int byteArrayToInt(byte[] arrby, int n2, int n3) {
        if (arrby == null) {
            throw new IllegalArgumentException("Parameter 'byteArray' cannot be null");
        }
        if (n3 <= 0 || n3 > 4) {
            throw new IllegalArgumentException("Length must be between 1 and 4. Length = " + n3);
        }
        if (n3 == 4 && Utility.isBitSet(arrby[n2], 8)) {
            throw new IllegalArgumentException("Signed bit is set (leftmost bit): " + Utility.byte2Hex(arrby[n2]));
        }
        int n4 = 0;
        for (int i2 = 0; i2 < n3; ++i2) {
            n4 += (255 & arrby[n2 + i2]) << 8 * (-1 + (n3 - i2));
        }
        return n4;
    }

    public static int byteToInt(byte by) {
        return by & 255;
    }

    public static String constructLV(String string) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Utility.int2Hex(string.length() / 2)).append(string);
        return stringBuilder.toString();
    }

    public static String convertHexToAscii(String string) {
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder stringBuilder2 = new StringBuilder();
        for (int i2 = 0; i2 < -1 + string.length(); i2 += 2) {
            int n2 = Integer.parseInt((String)string.substring(i2, i2 + 2), (int)16);
            stringBuilder.append((char)n2);
            stringBuilder2.append(n2);
        }
        System.out.println("Decimal : " + stringBuilder2.toString());
        return stringBuilder.toString();
    }

    public static byte[] decodeHex(char[] arrc) {
        int n2 = 0;
        int n3 = arrc.length;
        if ((n3 & 1) != 0) {
            throw new RuntimeException("Odd number of characters.");
        }
        byte[] arrby = new byte[n3 >> 1];
        int n4 = 0;
        while (n2 < n3) {
            int n5 = Utility.toDigit(arrc[n2], n2) << 4;
            int n6 = n2 + 1;
            int n7 = n5 | Utility.toDigit(arrc[n6], n6);
            n2 = n6 + 1;
            arrby[n4] = (byte)(n7 & 255);
            ++n4;
        }
        return arrby;
    }

    public static byte[] fromHexString(String string) {
        String string2 = Utility.removeSpaces(string);
        if (string2.length() == 0) {
            return new byte[0];
        }
        if (string2.length() % 2 != 0) {
            throw new IllegalArgumentException("Input string must contain an even number of characters: " + string2);
        }
        byte[] arrby = new byte[string2.length() / 2];
        char[] arrc = string2.toCharArray();
        for (int i2 = 0; i2 < arrc.length; i2 += 2) {
            StringBuilder stringBuilder = new StringBuilder(2);
            stringBuilder.append(arrc[i2]).append(arrc[i2 + 1]);
            arrby[i2 / 2] = (byte)Integer.parseInt((String)stringBuilder.toString(), (int)16);
        }
        return arrby;
    }

    public static int hex2decimal(String string) {
        String string2 = string.toUpperCase();
        int n2 = 0;
        for (int i2 = 0; i2 < string2.length(); ++i2) {
            n2 = "0123456789ABCDEF".indexOf((int)string2.charAt(i2)) + n2 * 16;
        }
        return n2;
    }

    public static String int2Hex(int n2) {
        String string = Integer.toHexString((int)n2);
        if (string.length() % 2 != 0) {
            string = "0" + string;
        }
        return string.toUpperCase();
    }

    public static boolean isBitSet(byte by, int n2) {
        if (n2 < 1 || n2 > 8) {
            throw new IllegalArgumentException("parameter 'bitPos' must be between 1 and 8. bitPos=" + n2);
        }
        return (1 & by >>> n2 - 1) == 1;
    }

    public static boolean isStringEmpty(String string) {
        return string == null || string.trim().isEmpty();
    }

    public static void largeLog(String string, String string2) {
    }

    public static String removeSpaces(String string) {
        return string.replaceAll(" ", "");
    }

    protected static int toDigit(char c2, int n2) {
        int n3 = Character.digit((char)c2, (int)16);
        if (n3 == -1) {
            throw new RuntimeException("Illegal hexadecimal charcter " + c2 + " at index " + n2);
        }
        return n3;
    }
}

