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

public class PKCS7Padding
implements BlockCipherPadding {
    @Override
    public int addPadding(byte[] arrby, int n2) {
        byte by = (byte)(arrby.length - n2);
        while (n2 < arrby.length) {
            arrby[n2] = by;
            ++n2;
        }
        return by;
    }

    @Override
    public String getPaddingName() {
        return "PKCS7";
    }

    @Override
    public void init(SecureRandom secureRandom) {
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public int padCount(byte[] arrby) {
        boolean bl;
        boolean bl2;
        int n2 = 255 & arrby[-1 + arrby.length];
        byte by = (byte)n2;
        boolean bl3 = n2 > arrby.length;
        boolean bl4 = n2 == 0;
        boolean bl5 = bl4 | bl3;
        for (int i2 = 0; i2 < arrby.length; bl5 |= bl2 & bl, ++i2) {
            bl2 = arrby.length - i2 <= n2;
            bl = arrby[i2] != by;
        }
        if (bl5) {
            throw new InvalidCipherTextException("pad block corrupted");
        }
        return n2;
    }
}

