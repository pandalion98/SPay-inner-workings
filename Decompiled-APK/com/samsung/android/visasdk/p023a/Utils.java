package com.samsung.android.visasdk.p023a;

import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import com.samsung.android.visasdk.p025c.Log;
import java.util.Arrays;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

/* renamed from: com.samsung.android.visasdk.a.b */
public class Utils {
    private static final char[] HEX_ARRAY;

    public static byte m1279a(byte b, int i, int i2) {
        if (i < 1 || i > 8 || i2 < 0 || i2 > 1) {
            Log.m1301e("Utils", "parameter 'bitPos' must be between 1 and 8. value must be 0 or 1");
        }
        if (i2 == 1) {
            return (byte) ((1 << (i - 1)) | b);
        }
        return (byte) (((1 << (i - 1)) ^ -1) & b);
    }

    public static boolean isBitSet(byte b, int i) {
        if (i < 1 || i > 8) {
            Log.m1301e("Utils", "parameter 'bitPos' must be between 1 and 8");
        }
        if (((1 << (i - 1)) & b) != 0) {
            return true;
        }
        return false;
    }

    static {
        HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    }

    public static String m1285o(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        char[] cArr = new char[(bArr.length * 2)];
        for (int i = 0; i < bArr.length; i++) {
            int i2 = bArr[i] & GF2Field.MASK;
            cArr[i * 2] = HEX_ARRAY[i2 >>> 4];
            cArr[(i * 2) + 1] = HEX_ARRAY[i2 & 15];
        }
        return new String(cArr);
    }

    public static byte[] hexStringToBytes(String str) {
        if (str == null) {
            return null;
        }
        int length = str.length();
        byte[] bArr = new byte[(length / 2)];
        for (int i = 0; i < length; i += 2) {
            bArr[i / 2] = (byte) ((Character.digit(str.charAt(i), 16) << 4) | Character.digit(str.charAt(i + 1), 16));
        }
        return bArr;
    }

    public static byte[] m1282d(byte[] bArr, int i, int i2) {
        byte[] bArr2 = new byte[i2];
        Utils.m1281a(bArr, i, bArr2, 0, i2);
        return bArr2;
    }

    public static void m1280a(byte[] bArr, int i, byte b, int i2) {
        if (bArr != null && i < bArr.length) {
            int length = bArr.length < i + i2 ? bArr.length : i + i2;
            while (i < length) {
                bArr[i] = b;
                i++;
            }
        }
    }

    public static void m1281a(byte[] bArr, int i, byte[] bArr2, int i2, int i3) {
        if (bArr != null && bArr2 != null) {
            System.arraycopy(bArr, i, bArr2, i2, i3);
        }
    }

    public static boolean m1284g(byte[] bArr, byte[] bArr2) {
        return Arrays.equals(bArr, bArr2);
    }

    public static final short getShort(byte[] bArr, int i) {
        return (short) ((((short) bArr[i]) << 8) + (((short) bArr[(short) (i + 1)]) & GF2Field.MASK));
    }

    public static long fU() {
        return System.currentTimeMillis() / 1000;
    }

    public static void m1283e(String str, byte[] bArr) {
        if (Version.LOG_DEBUG) {
            Log.m1300d("Utils", str + Utils.m1285o(bArr));
        }
    }

    public static void m1286p(byte[] bArr) {
        if (bArr != null) {
            for (int i = 0; i < bArr.length; i++) {
                bArr[i] = (byte) 0;
            }
        }
    }

    public static String bF(String str) {
        if (str == null) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder(BuildConfig.FLAVOR);
        for (int i = 0; i < str.length(); i += 2) {
            int parseInt = Integer.parseInt(str.substring(i, i + 2), 16);
            if (parseInt == 0) {
                break;
            }
            stringBuilder.append((char) parseInt);
        }
        return stringBuilder.toString();
    }
}
