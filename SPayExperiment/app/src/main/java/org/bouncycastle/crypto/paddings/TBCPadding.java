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

public class TBCPadding
implements BlockCipherPadding {
    /*
     * Enabled aggressive block sorting
     */
    @Override
    public int addPadding(byte[] arrby, int n2) {
        byte by;
        int n3 = 255;
        int n4 = arrby.length - n2;
        if (n2 > 0) {
            if ((1 & arrby[n2 - 1]) != 0) {
                n3 = 0;
            }
            by = (byte)n3;
        } else {
            if ((1 & arrby[-1 + arrby.length]) != 0) {
                n3 = 0;
            }
            by = (byte)n3;
        }
        while (n2 < arrby.length) {
            arrby[n2] = by;
            ++n2;
        }
        return n4;
    }

    @Override
    public String getPaddingName() {
        return "TBC";
    }

    @Override
    public void init(SecureRandom secureRandom) {
    }

    @Override
    public int padCount(byte[] arrby) {
        int n2;
        byte by = arrby[-1 + arrby.length];
        for (n2 = -1 + arrby.length; n2 > 0 && arrby[n2 - 1] == by; --n2) {
        }
        return arrby.length - n2;
    }
}

