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

public class X923Padding
implements BlockCipherPadding {
    SecureRandom random = null;

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public int addPadding(byte[] arrby, int n2) {
        byte by = (byte)(arrby.length - n2);
        do {
            if (n2 >= -1 + arrby.length) {
                arrby[n2] = by;
                return by;
            }
            arrby[n2] = this.random == null ? (byte)0 : (byte)this.random.nextInt();
            ++n2;
        } while (true);
    }

    @Override
    public String getPaddingName() {
        return "X9.23";
    }

    @Override
    public void init(SecureRandom secureRandom) {
        this.random = secureRandom;
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

