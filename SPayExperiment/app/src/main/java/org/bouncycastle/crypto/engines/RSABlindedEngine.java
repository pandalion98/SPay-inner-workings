/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 *  java.security.SecureRandom
 *  org.bouncycastle.util.BigIntegers
 */
package org.bouncycastle.crypto.engines;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.engines.RSACoreEngine;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters;
import org.bouncycastle.util.BigIntegers;

public class RSABlindedEngine
implements AsymmetricBlockCipher {
    private static final BigInteger ONE = BigInteger.valueOf((long)1L);
    private RSACoreEngine core = new RSACoreEngine();
    private RSAKeyParameters key;
    private SecureRandom random;

    @Override
    public int getInputBlockSize() {
        return this.core.getInputBlockSize();
    }

    @Override
    public int getOutputBlockSize() {
        return this.core.getOutputBlockSize();
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        this.core.init(bl, cipherParameters);
        if (cipherParameters instanceof ParametersWithRandom) {
            ParametersWithRandom parametersWithRandom = (ParametersWithRandom)cipherParameters;
            this.key = (RSAKeyParameters)parametersWithRandom.getParameters();
            this.random = parametersWithRandom.getRandom();
            return;
        }
        this.key = (RSAKeyParameters)cipherParameters;
        this.random = new SecureRandom();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public byte[] processBlock(byte[] arrby, int n2, int n3) {
        BigInteger bigInteger;
        if (this.key == null) {
            throw new IllegalStateException("RSA engine not initialised");
        }
        BigInteger bigInteger2 = this.core.convertInput(arrby, n2, n3);
        if (this.key instanceof RSAPrivateCrtKeyParameters) {
            RSAPrivateCrtKeyParameters rSAPrivateCrtKeyParameters = (RSAPrivateCrtKeyParameters)this.key;
            BigInteger bigInteger3 = rSAPrivateCrtKeyParameters.getPublicExponent();
            if (bigInteger3 != null) {
                BigInteger bigInteger4 = rSAPrivateCrtKeyParameters.getModulus();
                BigInteger bigInteger5 = BigIntegers.createRandomInRange((BigInteger)ONE, (BigInteger)bigInteger4.subtract(ONE), (SecureRandom)this.random);
                BigInteger bigInteger6 = bigInteger5.modPow(bigInteger3, bigInteger4).multiply(bigInteger2).mod(bigInteger4);
                bigInteger = this.core.processBlock(bigInteger6).multiply(bigInteger5.modInverse(bigInteger4)).mod(bigInteger4);
                do {
                    return this.core.convertOutput(bigInteger);
                    break;
                } while (true);
            }
            bigInteger = this.core.processBlock(bigInteger2);
            return this.core.convertOutput(bigInteger);
        }
        bigInteger = this.core.processBlock(bigInteger2);
        return this.core.convertOutput(bigInteger);
    }
}

