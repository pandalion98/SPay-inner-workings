/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.security.SecureRandom
 */
package org.bouncycastle.crypto.paddings;

import java.security.SecureRandom;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.paddings.BlockCipherPadding;

public class ISO10126d2Padding
implements BlockCipherPadding {
    SecureRandom random;

    @Override
    public int addPadding(byte[] arrby, int n2) {
        byte by = (byte)(arrby.length - n2);
        while (n2 < -1 + arrby.length) {
            arrby[n2] = (byte)this.random.nextInt();
            ++n2;
        }
        arrby[n2] = by;
        return by;
    }

    @Override
    public String getPaddingName() {
        return "ISO10126-2";
    }

    @Override
    public void init(SecureRandom secureRandom) {
        if (secureRandom != null) {
            this.random = secureRandom;
            return;
        }
        this.random = new SecureRandom();
    }

    @Override
    public int padCount(byte[] arrby) {
        int n2 = 255 & arrby[-1 + arrby.length];
        if (n2 > arrby.length) {
            throw new InvalidCipherTextException("pad block corrupted");
        }
        return n2;
    }
}

