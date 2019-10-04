/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.security.SecureRandom
 */
package org.bouncycastle.pqc.crypto.mceliece;

import java.security.SecureRandom;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.pqc.crypto.mceliece.McElieceParameters;

public class McElieceKeyGenerationParameters
extends KeyGenerationParameters {
    private McElieceParameters params;

    public McElieceKeyGenerationParameters(SecureRandom secureRandom, McElieceParameters mcElieceParameters) {
        super(secureRandom, 256);
        this.params = mcElieceParameters;
    }

    public McElieceParameters getParameters() {
        return this.params;
    }
}

