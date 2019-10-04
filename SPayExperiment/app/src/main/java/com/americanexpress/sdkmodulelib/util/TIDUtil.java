/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.security.MessageDigest
 */
package com.americanexpress.sdkmodulelib.util;

import com.americanexpress.sdkmodulelib.tlv.HexUtil;
import java.security.MessageDigest;

public class TIDUtil {
    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static String calculate5CSCFromCryptogram(String string) {
        if (string.length() == 16) {
            String string2;
            String string3 = string.substring(10, 16);
            String string4 = TIDUtil.hex2decimal(string3) + "";
            if (string4.length() < 5) {
                string2 = TIDUtil.padLeft(string4, '0', 5);
                do {
                    return TIDUtil.padLeft(string2, '0', 6);
                    break;
                } while (true);
            }
            string2 = string4.substring(-5 + string4.length(), string4.length());
            return TIDUtil.padLeft(string2, '0', 6);
        }
        int n2 = string.length();
        String string5 = null;
        if (n2 != 5) return string5;
        return TIDUtil.padLeft(string, '0', 6);
    }

    /*
     * Enabled aggressive block sorting
     */
    public static String generateHash(boolean bl, String string, String string2, String string3, String string4) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(TIDUtil.padLeft(string2, '0', 48));
        stringBuilder.append(TIDUtil.padLeft(string, '0', 16));
        if (string3.length() == 8) {
            stringBuilder.append(string3);
        } else {
            if (string3.length() != 4) {
                throw new Exception("Invalid unpredictableNumber length");
            }
            stringBuilder.append(TIDUtil.padLeft(string3, '0', 8));
        }
        if (bl) {
            if (string4.length() != 16) {
                throw new Exception("Invalid cryptogram length");
            }
            stringBuilder.append(string4);
        } else {
            String string5 = TIDUtil.calculate5CSCFromCryptogram(string4);
            if (string5 == null) {
                throw new Exception("Invalid crypto length");
            }
            stringBuilder.append(string5);
        }
        String string6 = stringBuilder.toString();
        MessageDigest messageDigest = MessageDigest.getInstance((String)"SHA-256");
        messageDigest.update(string6.getBytes("UTF-8"));
        return HexUtil.byteArrayToHexString(messageDigest.digest());
    }

    private static int hex2decimal(String string) {
        String string2 = string.toUpperCase();
        int n2 = 0;
        for (int i2 = 0; i2 < string2.length(); ++i2) {
            n2 = "0123456789ABCDEF".indexOf((int)string2.charAt(i2)) + n2 * 16;
        }
        return n2;
    }

    private static String padLeft(String string, char c2, int n2) {
        String string2 = "%" + n2 + "s";
        Object[] arrobject = new Object[]{string + ""};
        return String.format((String)string2, (Object[])arrobject).replace(' ', c2);
    }
}

