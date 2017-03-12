package org.bouncycastle.pqc.math.linearalgebra;

import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.math.BigInteger;

public final class IntUtils {
    private IntUtils() {
    }

    public static int[] clone(int[] iArr) {
        Object obj = new int[iArr.length];
        System.arraycopy(iArr, 0, obj, 0, iArr.length);
        return obj;
    }

    public static boolean equals(int[] iArr, int[] iArr2) {
        if (iArr.length != iArr2.length) {
            return false;
        }
        boolean z = true;
        for (int length = iArr.length - 1; length >= 0; length--) {
            z &= iArr[length] == iArr2[length] ? 1 : 0;
        }
        return z;
    }

    public static void fill(int[] iArr, int i) {
        for (int length = iArr.length - 1; length >= 0; length--) {
            iArr[length] = i;
        }
    }

    private static int partition(int[] iArr, int i, int i2, int i3) {
        int i4 = iArr[i3];
        iArr[i3] = iArr[i2];
        iArr[i2] = i4;
        int i5 = i;
        while (i < i2) {
            if (iArr[i] <= i4) {
                int i6 = iArr[i5];
                iArr[i5] = iArr[i];
                iArr[i] = i6;
                i5++;
            }
            i++;
        }
        i4 = iArr[i5];
        iArr[i5] = iArr[i2];
        iArr[i2] = i4;
        return i5;
    }

    public static void quicksort(int[] iArr) {
        quicksort(iArr, 0, iArr.length - 1);
    }

    public static void quicksort(int[] iArr, int i, int i2) {
        if (i2 > i) {
            int partition = partition(iArr, i, i2, i2);
            quicksort(iArr, i, partition - 1);
            quicksort(iArr, partition + 1, i2);
        }
    }

    public static int[] subArray(int[] iArr, int i, int i2) {
        Object obj = new int[(i2 - i)];
        System.arraycopy(iArr, i, obj, 0, i2 - i);
        return obj;
    }

    public static BigInteger[] toFlexiBigIntArray(int[] iArr) {
        BigInteger[] bigIntegerArr = new BigInteger[iArr.length];
        for (int i = 0; i < iArr.length; i++) {
            bigIntegerArr[i] = BigInteger.valueOf((long) iArr[i]);
        }
        return bigIntegerArr;
    }

    public static String toHexString(int[] iArr) {
        return ByteUtils.toHexString(BigEndianConversions.toByteArray(iArr));
    }

    public static String toString(int[] iArr) {
        String str = BuildConfig.FLAVOR;
        for (int i : iArr) {
            str = str + i + " ";
        }
        return str;
    }
}
