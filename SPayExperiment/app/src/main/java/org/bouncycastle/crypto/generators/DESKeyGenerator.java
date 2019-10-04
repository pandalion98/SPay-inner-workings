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
import org.bouncycastle.crypto.params.DESParameters;

public class DESKeyGenerator
extends CipherKeyGenerator {
    @Override
    public byte[] generateKey() {
        byte[] arrby = new byte[8];
        do {
            this.random.nextBytes(arrby);
            DESParameters.setOddParity(arrby);
        } while (DESParameters.isWeakKey(arrby, 0));
        return arrby;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void init(KeyGenerationParameters keyGenerationParameters) {
        super.init(keyGenerationParameters);
        if (this.strength == 0 || this.strength == 7) {
            this.strength = 8;
            return;
        } else {
            if (this.strength == 8) return;
            {
                throw new IllegalArgumentException("DES key must be 64 bits long.");
            }
        }
    }
}

