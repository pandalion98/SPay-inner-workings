package org.bouncycastle.crypto.paddings;

import java.security.SecureRandom;

public class ZeroBytePadding implements BlockCipherPadding {
    public int addPadding(byte[] bArr, int i) {
        int length = bArr.length - i;
        while (i < bArr.length) {
            bArr[i] = (byte) 0;
            i++;
        }
        return length;
    }

    public String getPaddingName() {
        return "ZeroByte";
    }

    public void init(SecureRandom secureRandom) {
    }

    public int padCount(byte[] bArr) {
        int length = bArr.length;
        while (length > 0 && bArr[length - 1] == null) {
            length--;
        }
        return bArr.length - length;
    }
}
