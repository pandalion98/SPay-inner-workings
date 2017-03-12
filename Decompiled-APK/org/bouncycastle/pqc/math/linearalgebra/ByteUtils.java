package org.bouncycastle.pqc.math.linearalgebra;

import com.americanexpress.mobilepayments.hceclient.utils.common.LLVARUtil;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;

public final class ByteUtils {
    private static final char[] HEX_CHARS;

    static {
        HEX_CHARS = new char[]{LLVARUtil.EMPTY_STRING, LLVARUtil.PLAIN_TEXT, LLVARUtil.HEX_STRING, '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    }

    private ByteUtils() {
    }

    public static byte[] clone(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        Object obj = new byte[bArr.length];
        System.arraycopy(bArr, 0, obj, 0, bArr.length);
        return obj;
    }

    public static byte[] concatenate(byte[] bArr, byte[] bArr2) {
        Object obj = new byte[(bArr.length + bArr2.length)];
        System.arraycopy(bArr, 0, obj, 0, bArr.length);
        System.arraycopy(bArr2, 0, obj, bArr.length, bArr2.length);
        return obj;
    }

    public static byte[] concatenate(byte[][] bArr) {
        int length = bArr[0].length;
        Object obj = new byte[(bArr.length * length)];
        int i = 0;
        for (Object arraycopy : bArr) {
            System.arraycopy(arraycopy, 0, obj, i, length);
            i += length;
        }
        return obj;
    }

    public static int deepHashCode(byte[] bArr) {
        int i = 1;
        for (byte b : bArr) {
            i = (i * 31) + b;
        }
        return i;
    }

    public static int deepHashCode(byte[][] bArr) {
        int i = 1;
        for (byte[] deepHashCode : bArr) {
            i = (i * 31) + deepHashCode(deepHashCode);
        }
        return i;
    }

    public static int deepHashCode(byte[][][] bArr) {
        int i = 1;
        for (byte[][] deepHashCode : bArr) {
            i = (i * 31) + deepHashCode(deepHashCode);
        }
        return i;
    }

    public static boolean equals(byte[] bArr, byte[] bArr2) {
        boolean z = true;
        if (bArr == null) {
            if (bArr2 != null) {
                z = false;
            }
            return z;
        } else if (bArr2 == null || bArr.length != bArr2.length) {
            return false;
        } else {
            boolean z2 = true;
            for (int length = bArr.length - 1; length >= 0; length--) {
                z2 &= bArr[length] == bArr2[length] ? 1 : 0;
            }
            return z2;
        }
    }

    public static boolean equals(byte[][] bArr, byte[][] bArr2) {
        if (bArr.length != bArr2.length) {
            return false;
        }
        boolean z = true;
        int length = bArr.length - 1;
        while (length >= 0) {
            boolean equals = equals(bArr[length], bArr2[length]) & z;
            length--;
            z = equals;
        }
        return z;
    }

    public static boolean equals(byte[][][] bArr, byte[][][] bArr2) {
        if (bArr.length != bArr2.length) {
            return false;
        }
        int length = bArr.length - 1;
        boolean z = true;
        while (length >= 0) {
            if (bArr[length].length != bArr2[length].length) {
                return false;
            }
            boolean z2 = z;
            for (int length2 = bArr[length].length - 1; length2 >= 0; length2--) {
                z2 &= equals(bArr[length][length2], bArr2[length][length2]);
            }
            length--;
            z = z2;
        }
        return z;
    }

    public static byte[] fromHexString(String str) {
        int i = 0;
        char[] toCharArray = str.toUpperCase().toCharArray();
        int i2 = 0;
        int i3 = 0;
        while (i2 < toCharArray.length) {
            if ((toCharArray[i2] >= LLVARUtil.EMPTY_STRING && toCharArray[i2] <= '9') || (toCharArray[i2] >= 'A' && toCharArray[i2] <= 'F')) {
                i3++;
            }
            i2++;
        }
        byte[] bArr = new byte[((i3 + 1) >> 1)];
        i2 = i3 & 1;
        while (i < toCharArray.length) {
            if (toCharArray[i] < LLVARUtil.EMPTY_STRING || toCharArray[i] > '9') {
                if (toCharArray[i] >= 'A' && toCharArray[i] <= 'F') {
                    i3 = i2 >> 1;
                    bArr[i3] = (byte) (bArr[i3] << 4);
                    i3 = i2 >> 1;
                    bArr[i3] = (byte) (bArr[i3] | ((toCharArray[i] - 65) + 10));
                }
                i++;
            } else {
                i3 = i2 >> 1;
                bArr[i3] = (byte) (bArr[i3] << 4);
                i3 = i2 >> 1;
                bArr[i3] = (byte) (bArr[i3] | (toCharArray[i] - 48));
            }
            i2++;
            i++;
        }
        return bArr;
    }

    public static byte[][] split(byte[] bArr, int i) {
        if (i > bArr.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        byte[][] bArr2 = new byte[][]{new byte[i], new byte[(bArr.length - i)]};
        System.arraycopy(bArr, 0, bArr2[0], 0, i);
        System.arraycopy(bArr, i, bArr2[1], 0, bArr.length - i);
        return bArr2;
    }

    public static byte[] subArray(byte[] bArr, int i) {
        return subArray(bArr, i, bArr.length);
    }

    public static byte[] subArray(byte[] bArr, int i, int i2) {
        Object obj = new byte[(i2 - i)];
        System.arraycopy(bArr, i, obj, 0, i2 - i);
        return obj;
    }

    public static String toBinaryString(byte[] bArr) {
        String str = BuildConfig.FLAVOR;
        for (int i = 0; i < bArr.length; i++) {
            byte b = bArr[i];
            int i2 = 0;
            while (i2 < 8) {
                i2++;
                str = str + ((b >>> i2) & 1);
            }
            if (i != bArr.length - 1) {
                str = str + " ";
            }
        }
        return str;
    }

    public static char[] toCharArray(byte[] bArr) {
        char[] cArr = new char[bArr.length];
        for (int i = 0; i < bArr.length; i++) {
            cArr[i] = (char) bArr[i];
        }
        return cArr;
    }

    public static String toHexString(byte[] bArr) {
        String str = BuildConfig.FLAVOR;
        for (int i = 0; i < bArr.length; i++) {
            str = (str + HEX_CHARS[(bArr[i] >>> 4) & 15]) + HEX_CHARS[bArr[i] & 15];
        }
        return str;
    }

    public static String toHexString(byte[] bArr, String str, String str2) {
        String str3 = new String(str);
        for (int i = 0; i < bArr.length; i++) {
            str3 = (str3 + HEX_CHARS[(bArr[i] >>> 4) & 15]) + HEX_CHARS[bArr[i] & 15];
            if (i < bArr.length - 1) {
                str3 = str3 + str2;
            }
        }
        return str3;
    }

    public static byte[] xor(byte[] bArr, byte[] bArr2) {
        byte[] bArr3 = new byte[bArr.length];
        for (int length = bArr.length - 1; length >= 0; length--) {
            bArr3[length] = (byte) (bArr[length] ^ bArr2[length]);
        }
        return bArr3;
    }
}
