package org.bouncycastle.util;

import com.samsung.android.spaytui.SpayTuiTAInfo;
import java.math.BigInteger;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

public final class Arrays {

    public static class Iterator<T> implements java.util.Iterator<T> {
        private final T[] dataArray;
        private int position;

        public Iterator(T[] tArr) {
            this.position = 0;
            this.dataArray = tArr;
        }

        public boolean hasNext() {
            return this.position < this.dataArray.length;
        }

        public T next() {
            Object[] objArr = this.dataArray;
            int i = this.position;
            this.position = i + 1;
            return objArr[i];
        }

        public void remove() {
            throw new UnsupportedOperationException("Cannot remove element from an Array.");
        }
    }

    private Arrays() {
    }

    public static byte[] append(byte[] bArr, byte b) {
        if (bArr == null) {
            return new byte[]{b};
        }
        int length = bArr.length;
        Object obj = new byte[(length + 1)];
        System.arraycopy(bArr, 0, obj, 0, length);
        obj[length] = b;
        return obj;
    }

    public static int[] append(int[] iArr, int i) {
        if (iArr == null) {
            return new int[]{i};
        }
        int length = iArr.length;
        Object obj = new int[(length + 1)];
        System.arraycopy(iArr, 0, obj, 0, length);
        obj[length] = i;
        return obj;
    }

    public static short[] append(short[] sArr, short s) {
        if (sArr == null) {
            return new short[]{s};
        }
        int length = sArr.length;
        Object obj = new short[(length + 1)];
        System.arraycopy(sArr, 0, obj, 0, length);
        obj[length] = s;
        return obj;
    }

    public static boolean areEqual(byte[] bArr, byte[] bArr2) {
        if (bArr == bArr2) {
            return true;
        }
        if (bArr == null || bArr2 == null || bArr.length != bArr2.length) {
            return false;
        }
        for (int i = 0; i != bArr.length; i++) {
            if (bArr[i] != bArr2[i]) {
                return false;
            }
        }
        return true;
    }

    public static boolean areEqual(char[] cArr, char[] cArr2) {
        if (cArr == cArr2) {
            return true;
        }
        if (cArr == null || cArr2 == null || cArr.length != cArr2.length) {
            return false;
        }
        for (int i = 0; i != cArr.length; i++) {
            if (cArr[i] != cArr2[i]) {
                return false;
            }
        }
        return true;
    }

    public static boolean areEqual(int[] iArr, int[] iArr2) {
        if (iArr == iArr2) {
            return true;
        }
        if (iArr == null || iArr2 == null || iArr.length != iArr2.length) {
            return false;
        }
        for (int i = 0; i != iArr.length; i++) {
            if (iArr[i] != iArr2[i]) {
                return false;
            }
        }
        return true;
    }

    public static boolean areEqual(long[] jArr, long[] jArr2) {
        if (jArr == jArr2) {
            return true;
        }
        if (jArr == null || jArr2 == null || jArr.length != jArr2.length) {
            return false;
        }
        for (int i = 0; i != jArr.length; i++) {
            if (jArr[i] != jArr2[i]) {
                return false;
            }
        }
        return true;
    }

    public static boolean areEqual(Object[] objArr, Object[] objArr2) {
        if (objArr == objArr2) {
            return true;
        }
        if (objArr == null || objArr2 == null || objArr.length != objArr2.length) {
            return false;
        }
        for (int i = 0; i != objArr.length; i++) {
            Object obj = objArr[i];
            Object obj2 = objArr2[i];
            if (obj == null) {
                if (obj2 != null) {
                    return false;
                }
            } else if (!obj.equals(obj2)) {
                return false;
            }
        }
        return true;
    }

    public static boolean areEqual(boolean[] zArr, boolean[] zArr2) {
        if (zArr == zArr2) {
            return true;
        }
        if (zArr == null || zArr2 == null || zArr.length != zArr2.length) {
            return false;
        }
        for (int i = 0; i != zArr.length; i++) {
            if (zArr[i] != zArr2[i]) {
                return false;
            }
        }
        return true;
    }

    public static byte[] clone(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        Object obj = new byte[bArr.length];
        System.arraycopy(bArr, 0, obj, 0, bArr.length);
        return obj;
    }

    public static byte[] clone(byte[] bArr, byte[] bArr2) {
        if (bArr == null) {
            return null;
        }
        if (bArr2 == null || bArr2.length != bArr.length) {
            return clone(bArr);
        }
        System.arraycopy(bArr, 0, bArr2, 0, bArr2.length);
        return bArr2;
    }

    public static int[] clone(int[] iArr) {
        if (iArr == null) {
            return null;
        }
        Object obj = new int[iArr.length];
        System.arraycopy(iArr, 0, obj, 0, iArr.length);
        return obj;
    }

    public static long[] clone(long[] jArr) {
        if (jArr == null) {
            return null;
        }
        Object obj = new long[jArr.length];
        System.arraycopy(jArr, 0, obj, 0, jArr.length);
        return obj;
    }

    public static long[] clone(long[] jArr, long[] jArr2) {
        if (jArr == null) {
            return null;
        }
        if (jArr2 == null || jArr2.length != jArr.length) {
            return clone(jArr);
        }
        System.arraycopy(jArr, 0, jArr2, 0, jArr2.length);
        return jArr2;
    }

    public static BigInteger[] clone(BigInteger[] bigIntegerArr) {
        if (bigIntegerArr == null) {
            return null;
        }
        Object obj = new BigInteger[bigIntegerArr.length];
        System.arraycopy(bigIntegerArr, 0, obj, 0, bigIntegerArr.length);
        return obj;
    }

    public static short[] clone(short[] sArr) {
        if (sArr == null) {
            return null;
        }
        Object obj = new short[sArr.length];
        System.arraycopy(sArr, 0, obj, 0, sArr.length);
        return obj;
    }

    public static byte[][] clone(byte[][] bArr) {
        if (bArr == null) {
            return (byte[][]) null;
        }
        byte[][] bArr2 = new byte[bArr.length][];
        for (int i = 0; i != bArr2.length; i++) {
            bArr2[i] = clone(bArr[i]);
        }
        return bArr2;
    }

    public static byte[][][] clone(byte[][][] bArr) {
        if (bArr == null) {
            return (byte[][][]) null;
        }
        byte[][][] bArr2 = new byte[bArr.length][][];
        for (int i = 0; i != bArr2.length; i++) {
            bArr2[i] = clone(bArr[i]);
        }
        return bArr2;
    }

    public static byte[] concatenate(byte[] bArr, byte[] bArr2) {
        if (bArr == null || bArr2 == null) {
            return bArr2 != null ? clone(bArr2) : clone(bArr);
        } else {
            Object obj = new byte[(bArr.length + bArr2.length)];
            System.arraycopy(bArr, 0, obj, 0, bArr.length);
            System.arraycopy(bArr2, 0, obj, bArr.length, bArr2.length);
            return obj;
        }
    }

    public static byte[] concatenate(byte[] bArr, byte[] bArr2, byte[] bArr3) {
        if (bArr == null || bArr2 == null || bArr3 == null) {
            return bArr2 == null ? concatenate(bArr, bArr3) : concatenate(bArr, bArr2);
        } else {
            Object obj = new byte[((bArr.length + bArr2.length) + bArr3.length)];
            System.arraycopy(bArr, 0, obj, 0, bArr.length);
            System.arraycopy(bArr2, 0, obj, bArr.length, bArr2.length);
            System.arraycopy(bArr3, 0, obj, bArr.length + bArr2.length, bArr3.length);
            return obj;
        }
    }

    public static byte[] concatenate(byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4) {
        if (bArr == null || bArr2 == null || bArr3 == null || bArr4 == null) {
            return bArr4 == null ? concatenate(bArr, bArr2, bArr3) : bArr3 == null ? concatenate(bArr, bArr2, bArr4) : bArr2 == null ? concatenate(bArr, bArr3, bArr4) : concatenate(bArr2, bArr3, bArr4);
        } else {
            Object obj = new byte[(((bArr.length + bArr2.length) + bArr3.length) + bArr4.length)];
            System.arraycopy(bArr, 0, obj, 0, bArr.length);
            System.arraycopy(bArr2, 0, obj, bArr.length, bArr2.length);
            System.arraycopy(bArr3, 0, obj, bArr.length + bArr2.length, bArr3.length);
            System.arraycopy(bArr4, 0, obj, (bArr.length + bArr2.length) + bArr3.length, bArr4.length);
            return obj;
        }
    }

    public static int[] concatenate(int[] iArr, int[] iArr2) {
        if (iArr == null) {
            return clone(iArr2);
        }
        if (iArr2 == null) {
            return clone(iArr);
        }
        Object obj = new int[(iArr.length + iArr2.length)];
        System.arraycopy(iArr, 0, obj, 0, iArr.length);
        System.arraycopy(iArr2, 0, obj, iArr.length, iArr2.length);
        return obj;
    }

    public static boolean constantTimeAreEqual(byte[] bArr, byte[] bArr2) {
        if (bArr == bArr2) {
            return true;
        }
        if (bArr == null || bArr2 == null || bArr.length != bArr2.length) {
            return false;
        }
        int i = 0;
        for (int i2 = 0; i2 != bArr.length; i2++) {
            i |= bArr[i2] ^ bArr2[i2];
        }
        return i == 0;
    }

    public static boolean contains(int[] iArr, int i) {
        for (int i2 : iArr) {
            if (i2 == i) {
                return true;
            }
        }
        return false;
    }

    public static boolean contains(short[] sArr, short s) {
        for (short s2 : sArr) {
            if (s2 == s) {
                return true;
            }
        }
        return false;
    }

    public static byte[] copyOf(byte[] bArr, int i) {
        Object obj = new byte[i];
        if (i < bArr.length) {
            System.arraycopy(bArr, 0, obj, 0, i);
        } else {
            System.arraycopy(bArr, 0, obj, 0, bArr.length);
        }
        return obj;
    }

    public static char[] copyOf(char[] cArr, int i) {
        Object obj = new char[i];
        if (i < cArr.length) {
            System.arraycopy(cArr, 0, obj, 0, i);
        } else {
            System.arraycopy(cArr, 0, obj, 0, cArr.length);
        }
        return obj;
    }

    public static int[] copyOf(int[] iArr, int i) {
        Object obj = new int[i];
        if (i < iArr.length) {
            System.arraycopy(iArr, 0, obj, 0, i);
        } else {
            System.arraycopy(iArr, 0, obj, 0, iArr.length);
        }
        return obj;
    }

    public static long[] copyOf(long[] jArr, int i) {
        Object obj = new long[i];
        if (i < jArr.length) {
            System.arraycopy(jArr, 0, obj, 0, i);
        } else {
            System.arraycopy(jArr, 0, obj, 0, jArr.length);
        }
        return obj;
    }

    public static BigInteger[] copyOf(BigInteger[] bigIntegerArr, int i) {
        Object obj = new BigInteger[i];
        if (i < bigIntegerArr.length) {
            System.arraycopy(bigIntegerArr, 0, obj, 0, i);
        } else {
            System.arraycopy(bigIntegerArr, 0, obj, 0, bigIntegerArr.length);
        }
        return obj;
    }

    public static byte[] copyOfRange(byte[] bArr, int i, int i2) {
        int length = getLength(i, i2);
        Object obj = new byte[length];
        if (bArr.length - i < length) {
            System.arraycopy(bArr, i, obj, 0, bArr.length - i);
        } else {
            System.arraycopy(bArr, i, obj, 0, length);
        }
        return obj;
    }

    public static int[] copyOfRange(int[] iArr, int i, int i2) {
        int length = getLength(i, i2);
        Object obj = new int[length];
        if (iArr.length - i < length) {
            System.arraycopy(iArr, i, obj, 0, iArr.length - i);
        } else {
            System.arraycopy(iArr, i, obj, 0, length);
        }
        return obj;
    }

    public static long[] copyOfRange(long[] jArr, int i, int i2) {
        int length = getLength(i, i2);
        Object obj = new long[length];
        if (jArr.length - i < length) {
            System.arraycopy(jArr, i, obj, 0, jArr.length - i);
        } else {
            System.arraycopy(jArr, i, obj, 0, length);
        }
        return obj;
    }

    public static BigInteger[] copyOfRange(BigInteger[] bigIntegerArr, int i, int i2) {
        int length = getLength(i, i2);
        Object obj = new BigInteger[length];
        if (bigIntegerArr.length - i < length) {
            System.arraycopy(bigIntegerArr, i, obj, 0, bigIntegerArr.length - i);
        } else {
            System.arraycopy(bigIntegerArr, i, obj, 0, length);
        }
        return obj;
    }

    public static void fill(byte[] bArr, byte b) {
        for (int i = 0; i < bArr.length; i++) {
            bArr[i] = b;
        }
    }

    public static void fill(char[] cArr, char c) {
        for (int i = 0; i < cArr.length; i++) {
            cArr[i] = c;
        }
    }

    public static void fill(int[] iArr, int i) {
        for (int i2 = 0; i2 < iArr.length; i2++) {
            iArr[i2] = i;
        }
    }

    public static void fill(long[] jArr, long j) {
        for (int i = 0; i < jArr.length; i++) {
            jArr[i] = j;
        }
    }

    public static void fill(short[] sArr, short s) {
        for (int i = 0; i < sArr.length; i++) {
            sArr[i] = s;
        }
    }

    private static int getLength(int i, int i2) {
        int i3 = i2 - i;
        if (i3 >= 0) {
            return i3;
        }
        StringBuffer stringBuffer = new StringBuffer(i);
        stringBuffer.append(" > ").append(i2);
        throw new IllegalArgumentException(stringBuffer.toString());
    }

    public static int hashCode(byte[] bArr) {
        if (bArr == null) {
            return 0;
        }
        int length = bArr.length;
        int i = length + 1;
        while (true) {
            length--;
            if (length < 0) {
                return i;
            }
            i = (i * SpayTuiTAInfo.SPAY_TA_TYPE_TEE_TUI) ^ bArr[length];
        }
    }

    public static int hashCode(byte[] bArr, int i, int i2) {
        if (bArr == null) {
            return 0;
        }
        int i3 = i2 + 1;
        while (true) {
            i2--;
            if (i2 < 0) {
                return i3;
            }
            i3 = (i3 * SpayTuiTAInfo.SPAY_TA_TYPE_TEE_TUI) ^ bArr[i + i2];
        }
    }

    public static int hashCode(char[] cArr) {
        if (cArr == null) {
            return 0;
        }
        int length = cArr.length;
        int i = length + 1;
        while (true) {
            length--;
            if (length < 0) {
                return i;
            }
            i = (i * SpayTuiTAInfo.SPAY_TA_TYPE_TEE_TUI) ^ cArr[length];
        }
    }

    public static int hashCode(int[] iArr) {
        if (iArr == null) {
            return 0;
        }
        int length = iArr.length;
        int i = length + 1;
        while (true) {
            length--;
            if (length < 0) {
                return i;
            }
            i = (i * SpayTuiTAInfo.SPAY_TA_TYPE_TEE_TUI) ^ iArr[length];
        }
    }

    public static int hashCode(int[] iArr, int i, int i2) {
        if (iArr == null) {
            return 0;
        }
        int i3 = i2 + 1;
        while (true) {
            i2--;
            if (i2 < 0) {
                return i3;
            }
            i3 = (i3 * SpayTuiTAInfo.SPAY_TA_TYPE_TEE_TUI) ^ iArr[i + i2];
        }
    }

    public static int hashCode(Object[] objArr) {
        if (objArr == null) {
            return 0;
        }
        int length = objArr.length;
        int i = length + 1;
        while (true) {
            length--;
            if (length < 0) {
                return i;
            }
            i = (i * SpayTuiTAInfo.SPAY_TA_TYPE_TEE_TUI) ^ objArr[length].hashCode();
        }
    }

    public static int hashCode(short[] sArr) {
        if (sArr == null) {
            return 0;
        }
        int length = sArr.length;
        int i = length + 1;
        while (true) {
            length--;
            if (length < 0) {
                return i;
            }
            i = (i * SpayTuiTAInfo.SPAY_TA_TYPE_TEE_TUI) ^ (sArr[length] & GF2Field.MASK);
        }
    }

    public static int hashCode(int[][] iArr) {
        int i = 0;
        int i2 = 0;
        while (i != iArr.length) {
            i2 = (i2 * SpayTuiTAInfo.SPAY_TA_TYPE_TEE_TUI) + hashCode(iArr[i]);
            i++;
        }
        return i2;
    }

    public static int hashCode(short[][] sArr) {
        int i = 0;
        int i2 = 0;
        while (i != sArr.length) {
            i2 = (i2 * SpayTuiTAInfo.SPAY_TA_TYPE_TEE_TUI) + hashCode(sArr[i]);
            i++;
        }
        return i2;
    }

    public static int hashCode(short[][][] sArr) {
        int i = 0;
        int i2 = 0;
        while (i != sArr.length) {
            i2 = (i2 * SpayTuiTAInfo.SPAY_TA_TYPE_TEE_TUI) + hashCode(sArr[i]);
            i++;
        }
        return i2;
    }

    public static byte[] prepend(byte[] bArr, byte b) {
        if (bArr == null) {
            return new byte[]{b};
        }
        int length = bArr.length;
        Object obj = new byte[(length + 1)];
        System.arraycopy(bArr, 0, obj, 1, length);
        obj[0] = b;
        return obj;
    }

    public static int[] prepend(int[] iArr, int i) {
        if (iArr == null) {
            return new int[]{i};
        }
        int length = iArr.length;
        Object obj = new int[(length + 1)];
        System.arraycopy(iArr, 0, obj, 1, length);
        obj[0] = i;
        return obj;
    }

    public static short[] prepend(short[] sArr, short s) {
        if (sArr == null) {
            return new short[]{s};
        }
        int length = sArr.length;
        Object obj = new short[(length + 1)];
        System.arraycopy(sArr, 0, obj, 1, length);
        obj[0] = s;
        return obj;
    }

    public static byte[] reverse(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        int i = 0;
        int length = bArr.length;
        byte[] bArr2 = new byte[length];
        while (true) {
            length--;
            if (length < 0) {
                return bArr2;
            }
            int i2 = i + 1;
            bArr2[length] = bArr[i];
            i = i2;
        }
    }
}
