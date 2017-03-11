package com.americanexpress.mobilepayments.hceclient.utils.common;

import com.americanexpress.mobilepayments.hceclient.exception.HCEClientException;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.Constants;
import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverInAppCryptoData;
import com.samsung.android.spayfw.payprovider.visa.transaction.TransactionInfo;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

public class HexUtils {
    public static String byteFromHexString(String str, int i) {
        if (str == null) {
            throw new HCEClientException("Input data is Null!!");
        }
        int i2 = i * 2;
        return str.substring(i2, i2 + 2);
    }

    public static String bytesFromHexString(String str, int i, int i2) {
        if (str != null) {
            return str.substring(i * 2, i2 * 2);
        }
        throw new HCEClientException("Input data is Null!!");
    }

    public static String nBytesFromHexString(String str, int i, int i2) {
        if (str == null) {
            throw new HCEClientException("Input data is Null!!");
        }
        int i3 = i * 2;
        return str.substring(i3, (i2 * 2) + i3);
    }

    public static boolean isHex(String str) {
        if (str.matches("^[a-fA-F0-9]*$")) {
            return true;
        }
        return false;
    }

    public static String getSafePrintChars(byte[] bArr) {
        if (bArr == null) {
            return BuildConfig.FLAVOR;
        }
        return getSafePrintChars(bArr, 0, bArr.length);
    }

    public static String getSafePrintChars(byte[] bArr, int i, int i2) {
        if (bArr == null) {
            return BuildConfig.FLAVOR;
        }
        if (bArr.length < i + i2) {
            throw new IllegalArgumentException("startPos(" + i + ")+length(" + i2 + ") > byteArray.length(" + bArr.length + ")");
        }
        StringBuilder stringBuilder = new StringBuilder();
        int i3 = i;
        while (i3 < i + i2) {
            if (bArr[i3] < 32 || bArr[i3] >= 127) {
                stringBuilder.append(".");
            } else {
                stringBuilder.append((char) bArr[i3]);
            }
            i3++;
        }
        return stringBuilder.toString();
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

    public static String hexByteArrayToString(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder(bArr.length * 2);
        int length = bArr.length;
        for (int i = 0; i < length; i++) {
            stringBuilder.append(String.format("%02x", new Object[]{Integer.valueOf(bArr[i] & GF2Field.MASK)}));
        }
        return stringBuilder.toString().toUpperCase();
    }

    public static short setShort(byte[] bArr, short s, short s2) {
        bArr[s] = (byte) (s2 >> 8);
        bArr[s + 1] = (byte) s2;
        return (short) (s + 2);
    }

    public static short secureCompare(byte[] bArr, short s, byte[] bArr2, short s2, short s3) {
        if (s + s3 > bArr.length || s2 + s3 > bArr2.length) {
            return Constants.MAGIC_FALSE;
        }
        for (short s4 = (short) 0; s4 < s3; s4 = (short) (s4 + 1)) {
            if (((byte) (bArr[s4 + s] ^ bArr2[s4 + s2])) != null) {
                return Constants.MAGIC_FALSE;
            }
        }
        return Constants.MAGIC_TRUE;
    }

    public static byte[] hexStringToByteArray(String str) {
        int length = str.length();
        byte[] bArr = new byte[(length / 2)];
        for (int i = 0; i < length; i += 2) {
            bArr[i / 2] = (byte) ((Character.digit(str.charAt(i), 16) << 4) + Character.digit(str.charAt(i + 1), 16));
        }
        return bArr;
    }

    public static short byte2Short(byte b, byte b2) {
        return (short) ((b << 8) | (b2 & GF2Field.MASK));
    }

    public static short getShort(byte[] bArr, int i) {
        return byte2Short(bArr[i], bArr[i + 1]);
    }

    public static short checkBIT(byte[] bArr, short s, byte b) {
        if (((byte) (bArr[s] & b)) == b) {
            return Constants.MAGIC_TRUE;
        }
        return Constants.MAGIC_FALSE;
    }

    public static short arrayFil(byte[] bArr, short s, short s2, byte b) {
        while (true) {
            short s3 = (short) (s2 - 1);
            if (s2 < (short) 0) {
                return s;
            }
            short s4 = (short) (s + 1);
            bArr[s] = b;
            s2 = s3;
            s = s4;
        }
    }

    public static String byte2Hex(byte b) {
        String[] strArr = new String[]{TransactionInfo.VISA_TRANSACTIONTYPE_PURCHASE, TransactionInfo.VISA_TRANSACTIONTYPE_REFUND, TransactionInfo.VISA_TRANSACTIONSTATUS_REFUNDED, TransactionInfo.VISA_TRANSACTIONSTATUS_DECLINED, DiscoverInAppCryptoData.DEFAULT_ECI_INDICATOR, "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
        int i = b & GF2Field.MASK;
        return strArr[(i >>> 4) & 15] + strArr[i & 15];
    }

    public static String short2Hex(short s) {
        byte b = (byte) (s >>> 8);
        byte b2 = (byte) (s & GF2Field.MASK);
        if (b == null) {
            return byte2Hex(b2);
        }
        return byte2Hex(b) + byte2Hex(b2);
    }
}
