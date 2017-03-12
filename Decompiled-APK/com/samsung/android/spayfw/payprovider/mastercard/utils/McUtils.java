package com.samsung.android.spayfw.payprovider.mastercard.utils;

import android.content.Context;
import com.mastercard.mobile_api.bytes.ByteArray;
import com.samsung.android.spayfw.utils.Utils;
import java.util.Calendar;
import java.util.TimeZone;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

public class McUtils {
    public static final int LENGTH_ZERO = 0;
    public static final String TAG_PAY = "mcpce_";
    public static final String TAG_TDS = "mctds_";

    public static String getUtcTimeInMilliseconds() {
        return String.valueOf(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis());
    }

    public static String byteArrayToHex(byte[] bArr) {
        StringBuilder stringBuilder = new StringBuilder(bArr.length * 2);
        int length = bArr.length;
        for (int i = LENGTH_ZERO; i < length; i++) {
            stringBuilder.append(String.format("%02x", new Object[]{Integer.valueOf(bArr[i] & GF2Field.MASK)}));
        }
        return stringBuilder.toString();
    }

    public static String shortToHex(short s) {
        return byteArrayToHex(new byte[]{(byte) ((s >> 8) & GF2Field.MASK), (byte) (s & GF2Field.MASK)});
    }

    public static byte[] convertStirngToByteArray(String str) {
        int length = str.length();
        byte[] bArr = new byte[(length / 2)];
        for (int i = LENGTH_ZERO; i < length; i += 2) {
            bArr[i / 2] = (byte) ((Character.digit(str.charAt(i), 16) << 4) + Character.digit(str.charAt(i + 1), 16));
        }
        return bArr;
    }

    public static String byteToHex(byte b) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("%02x", new Object[]{Integer.valueOf(b & GF2Field.MASK)}));
        return stringBuilder.toString();
    }

    public static String copySrcToDes(int i, byte[] bArr, int i2) {
        Object obj = new byte[i];
        System.arraycopy(bArr, i2, obj, LENGTH_ZERO, i);
        return byteArrayToHex(obj);
    }

    public static synchronized String getRealTime(Context context) {
        String valueOf;
        synchronized (McUtils.class) {
            valueOf = String.valueOf(Utils.am(context));
        }
        return valueOf;
    }

    public static int unsignedByteToInt(byte b) {
        return b & GF2Field.MASK;
    }

    public static boolean contains(ByteArray[] byteArrayArr, ByteArray byteArray) {
        if (byteArrayArr == null || byteArrayArr.length < 1 || byteArray == null) {
            return false;
        }
        int length = byteArrayArr.length;
        for (int i = LENGTH_ZERO; i < length; i++) {
            ByteArray byteArray2 = byteArrayArr[i];
            if (byteArray2 != null && byteArray2.isEqual(byteArray)) {
                return true;
            }
        }
        return false;
    }

    public static void wipe(ByteArray[] byteArrayArr) {
        if (byteArrayArr != null) {
            int length = byteArrayArr.length;
            for (int i = LENGTH_ZERO; i < length; i++) {
                ByteArray byteArray = byteArrayArr[i];
                if (byteArray != null) {
                    com.mastercard.mobile_api.utils.Utils.clearByteArray(byteArray);
                }
            }
        }
    }

    public static boolean superior(ByteArray byteArray, ByteArray byteArray2) {
        if (byteArray2 == null || byteArray == null) {
            return false;
        }
        int length = byteArray.getLength();
        int i = LENGTH_ZERO;
        while (i < length) {
            int i2 = byteArray.getByte(i) & GF2Field.MASK;
            int i3 = byteArray2.getByte(i) & GF2Field.MASK;
            if (i2 == i3) {
                i++;
            } else if (i2 > i3) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public static boolean isStringValid(String str, int i, int i2) {
        return str != null && !str.isEmpty() && i < str.length() && i2 > str.length();
    }
}
