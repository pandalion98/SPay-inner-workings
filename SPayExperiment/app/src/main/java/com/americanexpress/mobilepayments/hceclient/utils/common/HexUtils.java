/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Character
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 */
package com.americanexpress.mobilepayments.hceclient.utils.common;

import com.americanexpress.mobilepayments.hceclient.exception.HCEClientException;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.Constants;

public class HexUtils {
    public static short arrayFil(byte[] arrby, short s2, short s3, byte by) {
        do {
            short s4 = (short)(s3 - 1);
            if (s3 < 0) break;
            short s5 = (short)(s2 + 1);
            arrby[s2] = by;
            s3 = s4;
            s2 = s5;
        } while (true);
        return s2;
    }

    public static String byte2Hex(byte by) {
        String[] arrstring = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
        int n2 = by & 255;
        int n3 = 15 & n2 >>> 4;
        int n4 = n2 & 15;
        return arrstring[n3] + arrstring[n4];
    }

    public static short byte2Short(byte by, byte by2) {
        return (short)(by << 8 | by2 & 255);
    }

    public static String byteArrayToHexString(byte[] arrby) {
        if (arrby == null) {
            return "";
        }
        return HexUtils.byteArrayToHexString(arrby, 0, arrby.length);
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

    public static String byteFromHexString(String string, int n2) {
        if (string == null) {
            throw new HCEClientException("Input data is Null!!");
        }
        int n3 = n2 * 2;
        return string.substring(n3, n3 + 2);
    }

    public static String bytesFromHexString(String string, int n2, int n3) {
        if (string == null) {
            throw new HCEClientException("Input data is Null!!");
        }
        return string.substring(n2 * 2, n3 * 2);
    }

    public static short checkBIT(byte[] arrby, short s2, byte by) {
        if ((byte)(by & arrby[s2]) == by) {
            return Constants.MAGIC_TRUE;
        }
        return Constants.MAGIC_FALSE;
    }

    public static String getSafePrintChars(byte[] arrby) {
        if (arrby == null) {
            return "";
        }
        return HexUtils.getSafePrintChars(arrby, 0, arrby.length);
    }

    /*
     * Enabled aggressive block sorting
     */
    public static String getSafePrintChars(byte[] arrby, int n2, int n3) {
        if (arrby == null) {
            return "";
        }
        if (arrby.length < n2 + n3) {
            throw new IllegalArgumentException("startPos(" + n2 + ")+length(" + n3 + ") > byteArray.length(" + arrby.length + ")");
        }
        StringBuilder stringBuilder = new StringBuilder();
        int n4 = n2;
        while (n4 < n2 + n3) {
            if (arrby[n4] >= 32 && arrby[n4] < 127) {
                stringBuilder.append((char)arrby[n4]);
            } else {
                stringBuilder.append(".");
            }
            ++n4;
        }
        return stringBuilder.toString();
    }

    public static short getShort(byte[] arrby, int n2) {
        return HexUtils.byte2Short(arrby[n2], arrby[n2 + 1]);
    }

    public static String hexByteArrayToString(byte[] arrby) {
        if (arrby == null) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder(2 * arrby.length);
        for (byte by : arrby) {
            Object[] arrobject = new Object[]{by & 255};
            stringBuilder.append(String.format((String)"%02x", (Object[])arrobject));
        }
        return stringBuilder.toString().toUpperCase();
    }

    public static byte[] hexStringToByteArray(String string) {
        int n2 = string.length();
        byte[] arrby = new byte[n2 / 2];
        for (int i2 = 0; i2 < n2; i2 += 2) {
            arrby[i2 / 2] = (byte)((Character.digit((char)string.charAt(i2), (int)16) << 4) + Character.digit((char)string.charAt(i2 + 1), (int)16));
        }
        return arrby;
    }

    public static boolean isHex(String string) {
        return string.matches("^[a-fA-F0-9]*$");
    }

    public static String nBytesFromHexString(String string, int n2, int n3) {
        if (string == null) {
            throw new HCEClientException("Input data is Null!!");
        }
        int n4 = n2 * 2;
        return string.substring(n4, n4 + n3 * 2);
    }

    public static short secureCompare(byte[] arrby, short s2, byte[] arrby2, short s3, short s4) {
        if (s2 + s4 > arrby.length || s3 + s4 > arrby2.length) {
            return Constants.MAGIC_FALSE;
        }
        for (short s5 = 0; s5 < s4; s5 = (short)(s5 + 1)) {
            if ((byte)(arrby[s5 + s2] ^ arrby2[s5 + s3]) == 0) continue;
            return Constants.MAGIC_FALSE;
        }
        return Constants.MAGIC_TRUE;
    }

    public static short setShort(byte[] arrby, short s2, short s3) {
        arrby[s2] = (byte)(s3 >> 8);
        arrby[s2 + 1] = (byte)s3;
        return (short)(s2 + 2);
    }

    public static String short2Hex(short s2) {
        byte by = (byte)(s2 >>> 8);
        byte by2 = (byte)(s2 & 255);
        if (by == 0) {
            return HexUtils.byte2Hex(by2);
        }
        return HexUtils.byte2Hex(by) + HexUtils.byte2Hex(by2);
    }
}

