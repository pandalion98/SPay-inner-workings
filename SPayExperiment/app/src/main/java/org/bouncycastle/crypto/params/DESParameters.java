/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.String
 */
package org.bouncycastle.crypto.params;

import org.bouncycastle.crypto.params.KeyParameter;

public class DESParameters
extends KeyParameter {
    public static final int DES_KEY_LENGTH = 8;
    private static byte[] DES_weak_keys = new byte[]{1, 1, 1, 1, 1, 1, 1, 1, 31, 31, 31, 31, 14, 14, 14, 14, -32, -32, -32, -32, -15, -15, -15, -15, -2, -2, -2, -2, -2, -2, -2, -2, 1, -2, 1, -2, 1, -2, 1, -2, 31, -32, 31, -32, 14, -15, 14, -15, 1, -32, 1, -32, 1, -15, 1, -15, 31, -2, 31, -2, 14, -2, 14, -2, 1, 31, 1, 31, 1, 14, 1, 14, -32, -2, -32, -2, -15, -2, -15, -2, -2, 1, -2, 1, -2, 1, -2, 1, -32, 31, -32, 31, -15, 14, -15, 14, -32, 1, -32, 1, -15, 1, -15, 1, -2, 31, -2, 31, -2, 14, -2, 14, 31, 1, 31, 1, 14, 1, 14, 1, -2, -32, -2, -32, -2, -15, -2, -15};
    private static final int N_DES_WEAK_KEYS = 16;

    public DESParameters(byte[] arrby) {
        super(arrby);
        if (DESParameters.isWeakKey(arrby, 0)) {
            throw new IllegalArgumentException("attempt to create weak DES key");
        }
    }

    public static boolean isWeakKey(byte[] arrby, int n2) {
        boolean bl;
        block3 : {
            if (arrby.length - n2 < 8) {
                throw new IllegalArgumentException("key material too short.");
            }
            int n3 = 0;
            block0 : do {
                bl = false;
                if (n3 >= 16) break block3;
                for (int i2 = 0; i2 < 8; ++i2) {
                    if (arrby[i2 + n2] == DES_weak_keys[i2 + n3 * 8]) continue;
                    ++n3;
                    continue block0;
                }
                break;
            } while (true);
            bl = true;
        }
        return bl;
    }

    public static void setOddParity(byte[] arrby) {
        for (int i2 = 0; i2 < arrby.length; ++i2) {
            byte by = arrby[i2];
            arrby[i2] = (byte)(by & 254 | 1 & (1 ^ (by >> 1 ^ by >> 2 ^ by >> 3 ^ by >> 4 ^ by >> 5 ^ by >> 6 ^ by >> 7)));
        }
    }
}

