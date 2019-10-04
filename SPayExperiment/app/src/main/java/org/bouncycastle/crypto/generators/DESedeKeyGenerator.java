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
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.generators.DESKeyGenerator;
import org.bouncycastle.crypto.params.DESedeParameters;

public class DESedeKeyGenerator
extends DESKeyGenerator {
    @Override
    public byte[] generateKey() {
        byte[] arrby = new byte[this.strength];
        do {
            this.random.nextBytes(arrby);
            DESedeParameters.setOddParity(arrby);
        } while (DESedeParameters.isWeakKey(arrby, 0, arrby.length));
        return arrby;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void init(KeyGenerationParameters keyGenerationParameters) {
        this.random = keyGenerationParameters.getRandom();
        this.strength = (7 + keyGenerationParameters.getStrength()) / 8;
        if (this.strength == 0 || this.strength == 21) {
            this.strength = 24;
            return;
        } else {
            if (this.strength == 14) {
                this.strength = 16;
                return;
            }
            if (this.strength == 24 || this.strength == 16) return;
            {
                throw new IllegalArgumentException("DESede key must be 192 or 128 bits long.");
            }
        }
    }
}

