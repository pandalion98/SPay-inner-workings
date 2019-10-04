/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.String
 *  java.security.SecureRandom
 */
package org.bouncycastle.crypto.generators;

import java.security.SecureRandom;
import org.bouncycastle.crypto.CipherKeyGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;

public class Poly1305KeyGenerator
extends CipherKeyGenerator {
    private static final byte R_MASK_HIGH_4 = 15;
    private static final byte R_MASK_LOW_2 = -4;

    public static void checkKey(byte[] arrby) {
        if (arrby.length != 32) {
            throw new IllegalArgumentException("Poly1305 key must be 256 bits.");
        }
        Poly1305KeyGenerator.checkMask(arrby[19], (byte)15);
        Poly1305KeyGenerator.checkMask(arrby[23], (byte)15);
        Poly1305KeyGenerator.checkMask(arrby[27], (byte)15);
        Poly1305KeyGenerator.checkMask(arrby[31], (byte)15);
        Poly1305KeyGenerator.checkMask(arrby[20], (byte)-4);
        Poly1305KeyGenerator.checkMask(arrby[24], (byte)-4);
        Poly1305KeyGenerator.checkMask(arrby[28], (byte)-4);
    }

    private static void checkMask(byte by, byte by2) {
        if ((by & ~by2) != 0) {
            throw new IllegalArgumentException("Invalid format for r portion of Poly1305 key.");
        }
    }

    public static void clamp(byte[] arrby) {
        if (arrby.length != 32) {
            throw new IllegalArgumentException("Poly1305 key must be 256 bits.");
        }
        arrby[19] = (byte)(15 & arrby[19]);
        arrby[23] = (byte)(15 & arrby[23]);
        arrby[27] = (byte)(15 & arrby[27]);
        arrby[31] = (byte)(15 & arrby[31]);
        arrby[20] = (byte)(-4 & arrby[20]);
        arrby[24] = (byte)(-4 & arrby[24]);
        arrby[28] = (byte)(-4 & arrby[28]);
    }

    @Override
    public byte[] generateKey() {
        byte[] arrby = super.generateKey();
        Poly1305KeyGenerator.clamp(arrby);
        return arrby;
    }

    @Override
    public void init(KeyGenerationParameters keyGenerationParameters) {
        super.init(new KeyGenerationParameters(keyGenerationParameters.getRandom(), 256));
    }
}

