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
import org.bouncycastle.crypto.params.ECDomainParameters;

public class ECKeyGenerationParameters
extends KeyGenerationParameters {
    private ECDomainParameters domainParams;

    public ECKeyGenerationParameters(ECDomainParameters eCDomainParameters, SecureRandom secureRandom) {
        super(secureRandom, eCDomainParameters.getN().bitLength());
        this.domainParams = eCDomainParameters;
    }

    public ECDomainParameters getDomainParameters() {
        return this.domainParams;
    }
}

