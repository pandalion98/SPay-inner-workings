/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.math.BigInteger
 *  java.security.SecureRandom
 */
package org.bouncycastle.crypto.params;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.params.GOST3410Parameters;

public class GOST3410KeyGenerationParameters
extends KeyGenerationParameters {
    private GOST3410Parameters params;

    public GOST3410KeyGenerationParameters(SecureRandom secureRandom, GOST3410Parameters gOST3410Parameters) {
        super(secureRandom, -1 + gOST3410Parameters.getP().bitLength());
        this.params = gOST3410Parameters;
    }

    public GOST3410Parameters getParameters() {
        return this.params;
    }
}

