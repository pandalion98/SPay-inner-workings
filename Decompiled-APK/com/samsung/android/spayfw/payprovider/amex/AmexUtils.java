package com.samsung.android.spayfw.payprovider.amex;

import android.text.TextUtils;
import com.americanexpress.mobilepayments.hceclient.utils.common.LLVARUtil;
import com.samsung.android.spayfw.p002b.Log;
import java.security.MessageDigest;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;
import org.bouncycastle.util.encoders.Base64;

public class AmexUtils {
    private static final char[] HEX_ARRAY;
    protected static final char[] hexArray;
    private static int pB;

    public static final class LupcMetaData {
        public int nfcLupcCount;
        public long nfcLupcExpiry;
        public int otherLupcCount;
        public long otherLupcExpiry;
    }

    static {
        pB = 10;
        HEX_ARRAY = "0123456789ABCDEF".toCharArray();
        hexArray = new char[]{LLVARUtil.EMPTY_STRING, LLVARUtil.PLAIN_TEXT, LLVARUtil.HEX_STRING, '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    }

    public static String az(String str) {
        String substring;
        String str2 = null;
        try {
            MessageDigest instance = MessageDigest.getInstance("SHA-256");
            byte[] bytes = str.getBytes("UTF-8");
            instance.update(bytes);
            byte[] digest = instance.digest();
            int i = pB;
            String encodeHex = encodeHex(bytes);
            String str3 = encodeHex(digest) + "00000001";
            byte[] bArr = new byte[32];
            while (i > 0) {
                str3 = m760n(encodeHex, str3);
                xor(bArr, decodeHex(str3));
                i--;
                str2 = encodeHex(bArr);
            }
            if (str2 == null) {
                return str2;
            }
            if (str2.length() > 64) {
                substring = str2.substring(0, 64);
            } else {
                substring = str2;
            }
            try {
                return new String(Base64.encode(decodeHex(substring)));
            } catch (Exception e) {
                return substring;
            }
        } catch (Exception e2) {
            return null;
        }
    }

    private static void xor(byte[] bArr, byte[] bArr2) {
        for (int i = 0; i < bArr.length; i++) {
            bArr[i] = (byte) (bArr[i] ^ bArr2[i]);
        }
    }

    private static String m760n(String str, String str2) {
        Object obj = new byte[65];
        Object obj2 = new byte[65];
        byte[] bArr = new byte[16];
        try {
            MessageDigest instance = MessageDigest.getInstance("SHA-256");
            Object decodeHex = decodeHex(str);
            byte[] decodeHex2 = decodeHex(str2);
            if (decodeHex.length > 64) {
                instance.update(decodeHex);
                decodeHex = instance.digest();
            }
            System.arraycopy(decodeHex, 0, obj2, 0, decodeHex.length);
            System.arraycopy(decodeHex, 0, obj, 0, decodeHex.length);
            for (int i = 0; i < 64; i++) {
                obj2[i] = (byte) (obj2[i] ^ 54);
                obj[i] = (byte) (obj[i] ^ 92);
            }
            instance.update(obj2, 0, 64);
            instance.update(decodeHex2, 0, decodeHex2.length);
            byte[] digest = instance.digest();
            instance.update(obj, 0, 64);
            instance.update(digest, 0, instance.getDigestLength());
            return encodeHex(instance.digest());
        } catch (Throwable e) {
            Log.m284c("AmexUtils", e.getMessage(), e);
            return null;
        }
    }

    public static final String encodeHex(byte[] bArr) {
        int i = 0;
        int length = bArr.length;
        char[] cArr = new char[(length << 1)];
        for (int i2 = 0; i2 < length; i2++) {
            int i3 = i + 1;
            cArr[i] = HEX_ARRAY[(bArr[i2] & 240) >>> 4];
            i = i3 + 1;
            cArr[i3] = HEX_ARRAY[bArr[i2] & 15];
        }
        return new String(cArr);
    }

    public static final byte[] decodeHex(String str) {
        int length = str.length();
        byte[] bArr = new byte[(length / 2)];
        for (int i = 0; i < length; i += 2) {
            bArr[i / 2] = (byte) ((Character.digit(str.charAt(i), 16) << 4) + Character.digit(str.charAt(i + 1), 16));
        }
        return bArr;
    }

    public static String byteArrayToHex(byte[] bArr) {
        char[] cArr = new char[(bArr.length * 2)];
        for (int i = 0; i < bArr.length; i++) {
            int i2 = bArr[i] & GF2Field.MASK;
            cArr[i * 2] = hexArray[i2 >>> 4];
            cArr[(i * 2) + 1] = hexArray[i2 & 15];
        }
        return new String(cArr);
    }

    public static LupcMetaData aA(String str) {
        if (TextUtils.isEmpty(str)) {
            Log.m286e("AmexUtils", "metaDataBlob is empty!");
            return null;
        } else if (str.startsWith("E6")) {
            int length = (str.length() - 4) / 2;
            int parseInt = Integer.parseInt(str.substring(2, 4), 16);
            if (length != parseInt) {
                Log.m286e("AmexUtils", "Length of data " + length + " does not match extracted length " + parseInt);
                return null;
            }
            LupcMetaData lupcMetaData = new LupcMetaData();
            if (str.substring(4, 6).equals("16")) {
                int parseInt2 = Integer.parseInt(str.substring(6, 8), 16) * 2;
                lupcMetaData.nfcLupcCount = Integer.parseInt(str.substring(8, parseInt2 + 8));
                parseInt2 += 8;
                if (str.substring(parseInt2, parseInt2 + 2).equals("17")) {
                    parseInt2 += 2;
                    int parseInt3 = Integer.parseInt(str.substring(parseInt2, parseInt2 + 2), 16) * 2;
                    parseInt2 += 2;
                    lupcMetaData.nfcLupcExpiry = Long.parseLong(str.substring(parseInt2, parseInt2 + parseInt3));
                    parseInt2 += parseInt3;
                    if (parseInt2 == (parseInt * 2) + 4) {
                        return lupcMetaData;
                    }
                    if (str.substring(parseInt2, parseInt2 + 2).equals("53")) {
                        parseInt = parseInt2 + 2;
                        parseInt2 = Integer.parseInt(str.substring(parseInt, parseInt + 2), 16) * 2;
                        parseInt += 2;
                        lupcMetaData.otherLupcCount = Integer.parseInt(str.substring(parseInt, parseInt + parseInt2));
                        parseInt += parseInt2;
                        if (str.substring(parseInt, parseInt + 2).equals("54")) {
                            int i = parseInt + 2;
                            parseInt = Integer.parseInt(str.substring(i, i + 2), 16) * 2;
                            i += 2;
                            lupcMetaData.otherLupcExpiry = Long.parseLong(str.substring(i, i + parseInt));
                            i += parseInt;
                            return lupcMetaData;
                        }
                        Log.m286e("AmexUtils", "Other LUPC Expiry Tag not found");
                        return null;
                    }
                    Log.m286e("AmexUtils", "Other LUPC Count Tag not found");
                    return null;
                }
                Log.m286e("AmexUtils", "NFC LUPC Expiry Tag not found");
                return null;
            }
            Log.m286e("AmexUtils", "NFC LUPC Count Tag not found");
            return null;
        } else {
            Log.m286e("AmexUtils", "LUPC MetaData Tag not found");
            return null;
        }
    }
}
