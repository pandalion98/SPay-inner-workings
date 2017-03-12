package org.bouncycastle.crypto.engines;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters;
import org.bouncycastle.util.BigIntegers;

public class RSABlindedEngine implements AsymmetricBlockCipher {
    private static final BigInteger ONE;
    private RSACoreEngine core;
    private RSAKeyParameters key;
    private SecureRandom random;

    static {
        ONE = BigInteger.valueOf(1);
    }

    public RSABlindedEngine() {
        this.core = new RSACoreEngine();
    }

    public int getInputBlockSize() {
        return this.core.getInputBlockSize();
    }

    public int getOutputBlockSize() {
        return this.core.getOutputBlockSize();
    }

    public void init(boolean z, CipherParameters cipherParameters) {
        this.core.init(z, cipherParameters);
        if (cipherParameters instanceof ParametersWithRandom) {
            ParametersWithRandom parametersWithRandom = (ParametersWithRandom) cipherParameters;
            this.key = (RSAKeyParameters) parametersWithRandom.getParameters();
            this.random = parametersWithRandom.getRandom();
            return;
        }
        this.key = (RSAKeyParameters) cipherParameters;
        this.random = new SecureRandom();
    }

    public byte[] processBlock(byte[] bArr, int i, int i2) {
        if (this.key == null) {
            throw new IllegalStateException("RSA engine not initialised");
        }
        BigInteger modulus;
        BigInteger convertInput = this.core.convertInput(bArr, i, i2);
        if (this.key instanceof RSAPrivateCrtKeyParameters) {
            RSAPrivateCrtKeyParameters rSAPrivateCrtKeyParameters = (RSAPrivateCrtKeyParameters) this.key;
            BigInteger publicExponent = rSAPrivateCrtKeyParameters.getPublicExponent();
            if (publicExponent != null) {
                modulus = rSAPrivateCrtKeyParameters.getModulus();
                BigInteger createRandomInRange = BigIntegers.createRandomInRange(ONE, modulus.subtract(ONE), this.random);
                modulus = this.core.processBlock(createRandomInRange.modPow(publicExponent, modulus).multiply(convertInput).mod(modulus)).multiply(createRandomInRange.modInverse(modulus)).mod(modulus);
            } else {
                modulus = this.core.processBlock(convertInput);
            }
        } else {
            modulus = this.core.processBlock(convertInput);
        }
        return this.core.convertOutput(modulus);
    }
}
