package org.bouncycastle.crypto.paddings;

import java.security.SecureRandom;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

public class X923Padding implements BlockCipherPadding {
    SecureRandom random;

    public X923Padding() {
        this.random = null;
    }

    public int addPadding(byte[] bArr, int i) {
        byte length = (byte) (bArr.length - i);
        while (i < bArr.length - 1) {
            if (this.random == null) {
                bArr[i] = (byte) 0;
            } else {
                bArr[i] = (byte) this.random.nextInt();
            }
            i++;
        }
        bArr[i] = length;
        return length;
    }

    public String getPaddingName() {
        return "X9.23";
    }

    public void init(SecureRandom secureRandom) {
        this.random = secureRandom;
    }

    public int padCount(byte[] bArr) {
        int i = bArr[bArr.length - 1] & GF2Field.MASK;
        if (i <= bArr.length) {
            return i;
        }
        throw new InvalidCipherTextException("pad block corrupted");
    }
}
