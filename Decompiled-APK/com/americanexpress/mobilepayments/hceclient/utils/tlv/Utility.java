package com.americanexpress.mobilepayments.hceclient.utils.tlv;

import com.americanexpress.mobilepayments.hceclient.context.ApplicationInfoManager;
import com.americanexpress.mobilepayments.hceclient.utils.common.LLVARUtil;
import com.mastercard.mobile_api.utils.apdu.emv.PutTemplateApdu;
import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverInAppCryptoData;
import com.samsung.android.spayfw.payprovider.visa.transaction.TransactionInfo;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

public class Utility {
    protected static final byte[] encodingTable;

    static {
        encodingTable = new byte[]{(byte) 48, (byte) 49, (byte) 50, ApplicationInfoManager.TERM_XP3, (byte) 52, ApplicationInfoManager.MOB_CVM_TYP_DEV_PATTERN, (byte) 54, (byte) 55, (byte) 56, ApplicationInfoManager.EMV_MS, PutTemplateApdu.DIRECTORY_TEMPLATE_TAG, (byte) 98, (byte) 99, (byte) 100, (byte) 101, (byte) 102};
    }

    public static byte[] fromHexString(String str) {
        int i = 0;
        String removeSpaces = removeSpaces(str);
        if (removeSpaces.length() == 0) {
            return new byte[0];
        }
        if (removeSpaces.length() % 2 != 0) {
            throw new IllegalArgumentException("Input string must contain an even number of characters: " + removeSpaces);
        }
        byte[] bArr = new byte[(removeSpaces.length() / 2)];
        char[] toCharArray = removeSpaces.toCharArray();
        while (i < toCharArray.length) {
            StringBuilder stringBuilder = new StringBuilder(2);
            stringBuilder.append(toCharArray[i]).append(toCharArray[i + 1]);
            bArr[i / 2] = (byte) Integer.parseInt(stringBuilder.toString(), 16);
            i += 2;
        }
        return bArr;
    }

    public static String removeSpaces(String str) {
        return str.replaceAll(" ", BuildConfig.FLAVOR);
    }

    public static String byteArrayToHexString(byte[] bArr) {
        if (bArr == null) {
            return BuildConfig.FLAVOR;
        }
        return byteArrayToHexString(bArr, 0, bArr.length);
    }

    public static String byteArrayToHexString(byte[] bArr, int i, int i2) {
        if (bArr == null) {
            return BuildConfig.FLAVOR;
        }
        if (bArr.length < i + i2) {
            throw new IllegalArgumentException("startPos(" + i + ")+length(" + i2 + ") > byteArray.length(" + bArr.length + ")");
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i3 = 0; i3 < i2; i3++) {
            stringBuilder.append(Integer.toHexString((bArr[i + i3] & GF2Field.MASK) | -256).substring(6));
        }
        return stringBuilder.toString();
    }

    public static String int2Hex(int i) {
        String toHexString = Integer.toHexString(i);
        if (toHexString.length() % 2 != 0) {
            toHexString = TransactionInfo.VISA_TRANSACTIONTYPE_PURCHASE + toHexString;
        }
        return toHexString.toUpperCase();
    }

    public static boolean isBitSet(byte b, int i) {
        if (i < 1 || i > 8) {
            throw new IllegalArgumentException("parameter 'bitPos' must be between 1 and 8. bitPos=" + i);
        } else if (((b >>> (i - 1)) & 1) == 1) {
            return true;
        } else {
            return false;
        }
    }

    public static int byteArrayToInt(byte[] bArr) {
        return byteArrayToInt(bArr, 0, bArr.length);
    }

    public static int byteArrayToInt(byte[] bArr, int i, int i2) {
        int i3 = 0;
        if (bArr == null) {
            throw new IllegalArgumentException("Parameter 'byteArray' cannot be null");
        } else if (i2 <= 0 || i2 > 4) {
            throw new IllegalArgumentException("Length must be between 1 and 4. Length = " + i2);
        } else if (i2 == 4 && isBitSet(bArr[i], 8)) {
            throw new IllegalArgumentException("Signed bit is set (leftmost bit): " + byte2Hex(bArr[i]));
        } else {
            int i4 = 0;
            while (i3 < i2) {
                i4 += (bArr[i + i3] & GF2Field.MASK) << (((i2 - i3) - 1) * 8);
                i3++;
            }
            return i4;
        }
    }

    public static String byte2Hex(byte b) {
        String[] strArr = new String[]{TransactionInfo.VISA_TRANSACTIONTYPE_PURCHASE, TransactionInfo.VISA_TRANSACTIONTYPE_REFUND, TransactionInfo.VISA_TRANSACTIONSTATUS_REFUNDED, TransactionInfo.VISA_TRANSACTIONSTATUS_DECLINED, DiscoverInAppCryptoData.DEFAULT_ECI_INDICATOR, "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
        int i = b & GF2Field.MASK;
        return strArr[(i >>> 4) & 15] + strArr[i & 15];
    }

    public static String byte2BinaryLiteral(byte b) {
        String toBinaryString = Integer.toBinaryString(byteToInt(b));
        if (toBinaryString.length() >= 8) {
            return toBinaryString;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 8 - toBinaryString.length(); i++) {
            stringBuilder.append(LLVARUtil.EMPTY_STRING);
        }
        stringBuilder.append(toBinaryString);
        return stringBuilder.toString();
    }

    public static int byteToInt(byte b) {
        return b & GF2Field.MASK;
    }

    public static byte[] decodeHex(char[] cArr) {
        int i = 0;
        int length = cArr.length;
        if ((length & 1) != 0) {
            throw new RuntimeException("Odd number of characters.");
        }
        byte[] bArr = new byte[(length >> 1)];
        int i2 = 0;
        while (i < length) {
            i++;
            i++;
            bArr[i2] = (byte) (((toDigit(cArr[i], i) << 4) | toDigit(cArr[i], i)) & GF2Field.MASK);
            i2++;
        }
        return bArr;
    }

    protected static int toDigit(char c, int i) {
        int digit = Character.digit(c, 16);
        if (digit != -1) {
            return digit;
        }
        throw new RuntimeException("Illegal hexadecimal charcter " + c + " at index " + i);
    }

    public static String convertHexToAscii(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder stringBuilder2 = new StringBuilder();
        for (int i = 0; i < str.length() - 1; i += 2) {
            int parseInt = Integer.parseInt(str.substring(i, i + 2), 16);
            stringBuilder.append((char) parseInt);
            stringBuilder2.append(parseInt);
        }
        System.out.println("Decimal : " + stringBuilder2.toString());
        return stringBuilder.toString();
    }

    public static int hex2decimal(String str) {
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

    public static String constructLV(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(int2Hex(str.length() / 2)).append(str);
        return stringBuilder.toString();
    }

    public static void largeLog(String str, String str2) {
    }

    public static boolean isStringEmpty(String str) {
        if (str == null || str.trim().isEmpty()) {
            return true;
        }
        return false;
    }
}
