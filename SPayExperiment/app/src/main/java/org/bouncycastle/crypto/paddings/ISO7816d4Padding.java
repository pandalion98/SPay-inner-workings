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

public class ISO7816d4Padding
implements BlockCipherPadding {
    @Override
    public int addPadding(byte[] arrby, int n2) {
        int n3 = arrby.length - n2;
        arrby[n2] = -128;
        for (int i2 = n2 + 1; i2 < arrby.length; ++i2) {
            arrby[i2] = 0;
        }
        return n3;
    }

    @Override
    public String getPaddingName() {
        return "ISO7816-4";
    }

    @Override
    public void init(SecureRandom secureRandom) {
    }

    @Override
    public int padCount(byte[] arrby) {
        int n2;
        for (n2 = -1 + arrby.length; n2 > 0 && arrby[n2] == 0; --n2) {
        }
        if (arrby[n2] != -128) {
            throw new InvalidCipherTextException("pad block corrupted");
        }
        return arrby.length - n2;
    }
}

