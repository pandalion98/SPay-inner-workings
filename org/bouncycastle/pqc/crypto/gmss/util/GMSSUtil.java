package org.bouncycastle.pqc.crypto.gmss.util;

import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

public class GMSSUtil {
    public int bytesToIntLittleEndian(byte[] bArr) {
        return (((bArr[0] & GF2Field.MASK) | ((bArr[1] & GF2Field.MASK) << 8)) | ((bArr[2] & GF2Field.MASK) << 16)) | ((bArr[3] & GF2Field.MASK) << 24);
    }

    public int bytesToIntLittleEndian(byte[] bArr, int i) {
        int i2 = i + 1;
        int i3 = i2 + 1;
        return ((((bArr[i2] & GF2Field.MASK) << 8) | (bArr[i] & GF2Field.MASK)) | ((bArr[i3] & GF2Field.MASK) << 16)) | ((bArr[i3 + 1] & GF2Field.MASK) << 24);
    }

    public byte[] concatenateArray(byte[][] bArr) {
        Object obj = new byte[(bArr.length * bArr[0].length)];
        int i = 0;
        for (int i2 = 0; i2 < bArr.length; i2++) {
            System.arraycopy(bArr[i2], 0, obj, i, bArr[i2].length);
            i += bArr[i2].length;
        }
        return obj;
    }

    public int getLog(int i) {
        int i2 = 1;
        int i3 = 2;
        while (i3 < i) {
            i3 <<= 1;
            i2++;
        }
        return i2;
    }

    public byte[] intToBytesLittleEndian(int i) {
        return new byte[]{(byte) (i & GF2Field.MASK), (byte) ((i >> 8) & GF2Field.MASK), (byte) ((i >> 16) & GF2Field.MASK), (byte) ((i >> 24) & GF2Field.MASK)};
    }

    public void printArray(String str, byte[] bArr) {
        int i = 0;
        System.out.println(str);
        int i2 = 0;
        while (i < bArr.length) {
            System.out.println(i2 + "; " + bArr[i]);
            i2++;
            i++;
        }
    }

    public void printArray(String str, byte[][] bArr) {
        System.out.println(str);
        int i = 0;
        for (byte[] bArr2 : bArr) {
            int i2 = 0;
            while (i2 < bArr[0].length) {
                System.out.println(i + "; " + bArr2[i2]);
                i2++;
                i++;
            }
        }
    }

    public boolean testPowerOfTwo(int i) {
        int i2 = 1;
        while (i2 < i) {
            i2 <<= 1;
        }
        return i == i2;
    }
}
