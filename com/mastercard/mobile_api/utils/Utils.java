package com.mastercard.mobile_api.utils;

import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import com.americanexpress.mobilepayments.hceclient.utils.common.LLVARUtil;
import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.secureelement.CardException;
import com.samsung.android.spayfw.payprovider.visa.transaction.TransactionInfo;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

public class Utils {
    private static final String HEX_PREFIX = "0x";
    public static final String ZERO_PADDING = "0000000000000000";

    public static void clearByteArray(byte[] bArr) {
        if (bArr != null) {
            int length = bArr.length;
            for (int i = 0; i < length; i++) {
                bArr[i] = (byte) 0;
            }
        }
    }

    public static void clearCharArray(char[] cArr) {
        int length = cArr.length;
        for (int i = 0; i < length; i++) {
            cArr[i] = '\u0000';
        }
    }

    public static String toHexString(byte[] bArr, int i) {
        String str = "0123456789ABCDEF";
        StringBuffer stringBuffer = new StringBuffer();
        for (int i2 = 0; i2 < i; i2++) {
            byte b = bArr[i2];
            stringBuffer.append("0123456789ABCDEF".charAt((b >> 4) & 15));
            stringBuffer.append("0123456789ABCDEF".charAt(b & 15));
        }
        return stringBuffer.toString();
    }

    public static String bcdAmountArrayToString(byte[] bArr, int i, int i2) {
        String str = BuildConfig.FLAVOR;
        if (i >= bArr.length || i + i2 > bArr.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        int i3 = i;
        String str2 = str;
        int i4 = 1;
        while (i3 < i + i2) {
            byte b = (byte) ((bArr[i3] >>> 4) & 15);
            byte b2 = (byte) (bArr[i3] & 15);
            if (b > (byte) 9 || b2 > (byte) 9) {
                throw new IllegalArgumentException();
            }
            int i5;
            int i6;
            Integer num = new Integer(b);
            Integer num2 = new Integer(b2);
            if (i4 == 0 || b != null) {
                str = new StringBuilder(String.valueOf(str2)).append(num.toString()).toString();
                i5 = 0;
            } else {
                i6 = i4;
                str = str2;
                i5 = i6;
            }
            if (i5 == 0 || b2 != null) {
                str = new StringBuilder(String.valueOf(str)).append(num2.toString()).toString();
                i5 = 0;
            }
            if (i3 == (i + i2) - 2) {
                str = new StringBuilder(String.valueOf(str)).append(".").toString();
                i5 = 0;
            }
            i3++;
            i6 = i5;
            str2 = str;
            i4 = i6;
        }
        if (str2.equalsIgnoreCase(BuildConfig.FLAVOR) || str2.charAt(0) == '.') {
            return new StringBuilder(TransactionInfo.VISA_TRANSACTIONTYPE_PURCHASE).append(str2).toString();
        }
        return str2;
    }

    public static String bcdArrayToString(byte[] bArr, int i, int i2) {
        Object obj = BuildConfig.FLAVOR;
        Object obj2 = 1;
        if (i >= bArr.length || i + i2 > bArr.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        String stringBuilder;
        for (int i3 = i; i3 < i + i2; i3++) {
            byte b = (byte) ((bArr[i3] >>> 4) & 15);
            byte b2 = (byte) (bArr[i3] & 15);
            if (b > (byte) 9 || b2 > (byte) 9) {
                throw new IllegalArgumentException();
            }
            Integer num = new Integer(b);
            Integer num2 = new Integer(b2);
            if (obj2 == null || b != null) {
                stringBuilder = new StringBuilder(String.valueOf(stringBuilder)).append(num.toString()).toString();
                obj2 = null;
            }
            if (obj2 == null || b2 != null) {
                obj = new StringBuilder(String.valueOf(obj)).append(num2.toString()).toString();
                obj2 = null;
            }
        }
        return stringBuilder;
    }

    public static byte[] encodeByteArray(byte[] bArr) {
        int i = 0;
        int length = bArr.length;
        byte[] bArr2 = new byte[(((length % 2) + length) / 2)];
        int i2 = 0;
        while (i2 <= length - 2) {
            int i3 = i + 1;
            int i4 = i2 + 1;
            byte b = (byte) ((bArr[i2] - 48) << 4);
            i2 = i4 + 1;
            bArr2[i] = (byte) (((byte) (bArr[i4] - 48)) | b);
            i = i3;
        }
        if (length % 2 == 1) {
            bArr2[i] = (byte) (((bArr[i2] - 48) << 4) | 15);
        }
        return bArr2;
    }

    public static String getAsHexString(byte[] bArr) {
        return getAsHexString(bArr, false);
    }

    public static short makeShort(byte b, byte b2) {
        return (short) (((short) ((((short) (b & GF2Field.MASK)) & GF2Field.MASK) << 8)) | ((short) (b2 & GF2Field.MASK)));
    }

    public static final String getAsHexString(byte[] bArr, boolean z) {
        return getAsHexString(bArr, 0, bArr.length, z);
    }

    public static final String getAsHexString(int i, boolean z) {
        return new StringBuilder(String.valueOf(z ? HEX_PREFIX : BuildConfig.FLAVOR)).append(Integer.toHexString(i)).toString();
    }

    public static final String getAsHexString(byte[] bArr, int i, int i2) {
        return getAsHexString(bArr, i, i2, false);
    }

    public static final String getAsHexString(byte[] bArr, int i, int i2, boolean z) {
        StringBuffer stringBuffer = new StringBuffer();
        if (i + i2 > bArr.length) {
            i2 = bArr.length - i;
        }
        for (int i3 = i; i3 < i + i2; i3++) {
            appendByte(bArr[i3], stringBuffer);
        }
        return stringBuffer.toString().toUpperCase();
    }

    public static final byte[] readHexString(String str) {
        int i = 0;
        if (str == null || str.length() == 0 || str.equals(HEX_PREFIX)) {
            return new byte[0];
        }
        if (str.startsWith(HEX_PREFIX)) {
            str = str.substring(2);
        }
        byte[] bArr = new byte[(str.length() / 2)];
        while (i < bArr.length) {
            bArr[i] = (byte) (Integer.parseInt(str.substring(i * 2, (i * 2) + 2), 16) & GF2Field.MASK);
            i++;
        }
        return bArr;
    }

    private static final void appendByte(int i, StringBuffer stringBuffer) {
        String toHexString = Integer.toHexString(i & GF2Field.MASK);
        if (toHexString.length() < 2) {
            stringBuffer.append(LLVARUtil.EMPTY_STRING);
        }
        stringBuffer.append(toHexString);
    }

    public static final int readInt(byte[] bArr, int i, boolean z) {
        int length = bArr.length - i;
        if (length < 4) {
            Object obj = new byte[4];
            System.arraycopy(bArr, 0, obj, 4 - length, length);
            bArr = obj;
        }
        if (z) {
            return ((((bArr[i + 3] & GF2Field.MASK) << 24) | ((bArr[i + 2] & GF2Field.MASK) << 16)) | ((bArr[i + 1] & GF2Field.MASK) << 8)) | (bArr[i + 0] & GF2Field.MASK);
        }
        return ((((bArr[i + 0] & GF2Field.MASK) << 24) | ((bArr[i + 1] & GF2Field.MASK) << 16)) | ((bArr[i + 2] & GF2Field.MASK) << 8)) | (bArr[i + 3] & GF2Field.MASK);
    }

    public static final int readInt(byte[] bArr, int i) {
        return readInt(bArr, i, false);
    }

    public static final int readShort(byte[] bArr, int i, boolean z) {
        if (z) {
            return ((bArr[i + 1] << 8) | (bArr[i] & GF2Field.MASK)) & HCEClientConstants.HIGHEST_ATC_DEC_VALUE;
        }
        return ((bArr[i] << 8) | (bArr[i + 1] & GF2Field.MASK)) & HCEClientConstants.HIGHEST_ATC_DEC_VALUE;
    }

    public static final short readShort(byte[] bArr, int i) {
        return (short) readShort(bArr, i, false);
    }

    public static byte[] PrependLengthToByteArray(byte[] bArr) {
        Object obj = new byte[]{(byte) (bArr.length >>> 8), (byte) bArr.length};
        Object obj2 = new byte[(bArr.length + obj.length)];
        System.arraycopy(obj, 0, obj2, 0, obj.length);
        System.arraycopy(bArr, 0, obj2, obj.length, bArr.length);
        return obj2;
    }

    public static final void writeInt(ByteArray byteArray, int i, long j) {
        byteArray.setByte(i + 0, (byte) ((int) ((j >> 24) & 255)));
        byteArray.setByte(i + 1, (byte) ((int) ((j >> 16) & 255)));
        byteArray.setByte(i + 2, (byte) ((int) ((j >> 8) & 255)));
        byteArray.setByte(i + 3, (byte) ((int) (j & 255)));
    }

    public static final boolean arrayCompare(byte[] bArr, int i, byte[] bArr2, int i2, int i3) {
        if (i + i3 > bArr.length || i2 + i3 > bArr2.length) {
            return false;
        }
        for (int i4 = 0; i4 < i3; i4++) {
            if (bArr[i + i4] != bArr2[i2 + i4]) {
                return false;
            }
        }
        return true;
    }

    public static final boolean equals(byte[] bArr, byte[] bArr2, int i, int i2, int i3) {
        if (bArr == null && bArr2 == null) {
            return true;
        }
        if (bArr == null || bArr2 == null || i + i3 > bArr.length || i2 + i3 > bArr2.length) {
            return false;
        }
        for (int i4 = 0; i4 < i3; i4++) {
            if (bArr[i + i4] != bArr2[i2 + i4]) {
                return false;
            }
        }
        return true;
    }

    public static final boolean equals(byte[] bArr, byte[] bArr2) {
        if (bArr == null && bArr2 == null) {
            return true;
        }
        if (bArr == null || bArr2 == null || bArr.length != bArr2.length) {
            return false;
        }
        for (int i = 0; i < bArr.length; i++) {
            if (bArr[i] != bArr2[i]) {
                return false;
            }
        }
        return true;
    }

    public static int findTag(byte b, byte[] bArr, int i, int i2) {
        for (int i3 = 0; i3 < i2; i3++) {
            if (bArr[i + i3] == b) {
                return i3 + i;
            }
        }
        throw new CardException();
    }

    public static int findTag(short s, byte[] bArr, int i, int i2) {
        for (int i3 = 0; i3 < i2; i3++) {
            if (readShort(bArr, i + i3) == s) {
                return i3 + i;
            }
        }
        throw new CardException();
    }

    public static int lastIndexOf(String str, String str2) {
        int i = 0;
        int i2 = -1;
        while (i != -1) {
            i = str.indexOf(str2, i2 + 1);
            if (i != -1) {
                i2 = i;
            }
        }
        return i2;
    }

    public static byte[] prependLengthToByteArray(byte[] bArr) {
        Object obj = new byte[]{(byte) (bArr.length >>> 8), (byte) bArr.length};
        Object obj2 = new byte[(bArr.length + obj.length)];
        System.arraycopy(obj, 0, obj2, 0, obj.length);
        System.arraycopy(bArr, 0, obj2, obj.length, bArr.length);
        return obj2;
    }

    public static byte[] longToBCD(long j, int i) {
        int i2;
        long j2 = j;
        int i3 = 0;
        while (j2 != 0) {
            j2 /= 10;
            i3++;
        }
        int i4 = i3 % 2 == 0 ? i3 / 2 : (i3 + 1) / 2;
        if (i3 % 2 != 0) {
            i2 = 1;
        } else {
            i2 = 0;
        }
        Object obj = new byte[i4];
        for (int i5 = 0; i5 < i3; i5++) {
            byte b = (byte) ((int) (j % 10));
            if (i5 == i3 - 1 && r1 != 0) {
                obj[i5 / 2] = b;
            } else if (i5 % 2 == 0) {
                obj[i5 / 2] = b;
            } else {
                int i6 = i5 / 2;
                obj[i6] = (byte) (((byte) (b << 4)) | obj[i6]);
            }
            j /= 10;
        }
        for (i2 = 0; i2 < i4 / 2; i2++) {
            byte b2 = obj[i2];
            obj[i2] = obj[(i4 - i2) - 1];
            obj[(i4 - i2) - 1] = b2;
        }
        if (i == i4) {
            return obj;
        }
        Object obj2 = new byte[i];
        System.arraycopy(obj, 0, obj2, i - i4, i4);
        return obj2;
    }

    public static boolean isZero(ByteArray byteArray) {
        if (byteArray == null) {
            return false;
        }
        int length = byteArray.getLength();
        byte[] bytes = byteArray.getBytes();
        for (int i = 0; i < length; i++) {
            if (bytes[i] != null) {
                return false;
            }
        }
        return true;
    }

    public static void clearByteArray(ByteArray byteArray) {
        if (byteArray != null) {
            byteArray.clear();
        }
    }

    public static boolean superior(ByteArray byteArray, ByteArray byteArray2) {
        if (byteArray2 == null || byteArray == null) {
            return false;
        }
        int length = byteArray.getLength() - 1;
        while (length >= 0) {
            int i = byteArray.getByte(length) & GF2Field.MASK;
            int i2 = byteArray2.getByte(length) & GF2Field.MASK;
            if (i == i2) {
                length--;
            } else if (i > i2) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public static String bcdAmountArrayToString(ByteArray byteArray) {
        return bcdAmountArrayToString(byteArray.getBytes(), 0, byteArray.getLength());
    }

    public static int readShort(ByteArray byteArray) {
        return readShort(byteArray.getBytes(), 0);
    }
}
