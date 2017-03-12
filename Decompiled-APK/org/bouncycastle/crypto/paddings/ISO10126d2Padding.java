package org.bouncycastle.crypto.paddings;

import java.security.SecureRandom;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

public class ISO10126d2Padding implements BlockCipherPadding {
    SecureRandom random;

    public int addPadding(byte[] bArr, int i) {
        byte length = (byte) (bArr.length - i);
        while (i < bArr.length - 1) {
            bArr[i] = (byte) this.random.nextInt();
            i++;
        }
        bArr[i] = length;
        return length;
    }

    public String getPaddingName() {
        return "ISO10126-2";
    }

    public void init(SecureRandom secureRandom) {
        if (secureRandom != null) {
            this.random = secureRandom;
        } else {
            this.random = new SecureRandom();
        }
    }

    public int padCount(byte[] bArr) {
        int i = bArr[bArr.length - 1] & GF2Field.MASK;
        if (i <= bArr.length) {
            return i;
        }
        throw new InvalidCipherTextException("pad block corrupted");
    }
}
