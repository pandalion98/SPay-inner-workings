package com.americanexpress.sdkmodulelib.util;

import com.americanexpress.mobilepayments.hceclient.utils.common.LLVARUtil;
import com.americanexpress.sdkmodulelib.tlv.HexUtil;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.security.MessageDigest;

public class TIDUtil {
    public static String generateHash(boolean z, String str, String str2, String str3, String str4) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(padLeft(str2, LLVARUtil.EMPTY_STRING, 48));
        stringBuilder.append(padLeft(str, LLVARUtil.EMPTY_STRING, 16));
        if (str3.length() == 8) {
            stringBuilder.append(str3);
        } else if (str3.length() == 4) {
            stringBuilder.append(padLeft(str3, LLVARUtil.EMPTY_STRING, 8));
        } else {
            throw new Exception("Invalid unpredictableNumber length");
        }
        if (!z) {
            String calculate5CSCFromCryptogram = calculate5CSCFromCryptogram(str4);
            if (calculate5CSCFromCryptogram == null) {
                throw new Exception("Invalid crypto length");
            }
            stringBuilder.append(calculate5CSCFromCryptogram);
        } else if (str4.length() != 16) {
            throw new Exception("Invalid cryptogram length");
        } else {
            stringBuilder.append(str4);
        }
        String stringBuilder2 = stringBuilder.toString();
        MessageDigest instance = MessageDigest.getInstance("SHA-256");
        instance.update(stringBuilder2.getBytes("UTF-8"));
        return HexUtil.byteArrayToHexString(instance.digest());
    }

    private static String calculate5CSCFromCryptogram(String str) {
        if (str.length() == 16) {
            String str2 = hex2decimal(str.substring(10, 16)) + BuildConfig.FLAVOR;
            if (str2.length() < 5) {
                str2 = padLeft(str2, LLVARUtil.EMPTY_STRING, 5);
            } else {
                str2 = str2.substring(str2.length() - 5, str2.length());
            }
            return padLeft(str2, LLVARUtil.EMPTY_STRING, 6);
        } else if (str.length() == 5) {
            return padLeft(str, LLVARUtil.EMPTY_STRING, 6);
        } else {
            return null;
        }
    }

    private static int hex2decimal(String str) {
        int i = 0;
        String str2 = "0123456789ABCDEF";
        String toUpperCase = str.toUpperCase();
        int i2 = 0;
        while (i < toUpperCase.length()) {
            i2 = (i2 * 16) + str2.indexOf(toUpperCase.charAt(i));
            i++;
        }
        return i2;
    }

    private static String padLeft(String str, char c, int i) {
        return String.format("%" + i + "s", new Object[]{str + BuildConfig.FLAVOR}).replace(' ', c);
    }
}
