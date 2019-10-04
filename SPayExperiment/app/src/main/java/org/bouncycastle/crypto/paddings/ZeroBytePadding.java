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
import org.bouncycastle.crypto.paddings.BlockCipherPadding;

public class ZeroBytePadding
implements BlockCipherPadding {
    @Override
    public int addPadding(byte[] arrby, int n2) {
        int n3 = arrby.length - n2;
        while (n2 < arrby.length) {
            arrby[n2] = 0;
            ++n2;
        }
        return n3;
    }

    @Override
    public String getPaddingName() {
        return "ZeroByte";
    }

    @Override
    public void init(SecureRandom secureRandom) {
    }

    @Override
    public int padCount(byte[] arrby) {
        int n2 = arrby.length;
        while (n2 > 0 && arrby[n2 - 1] == 0) {
            --n2;
        }
        return arrby.length - n2;
    }
}

