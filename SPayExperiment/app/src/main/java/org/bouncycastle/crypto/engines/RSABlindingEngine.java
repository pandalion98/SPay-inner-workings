/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.math.BigInteger
 */
package org.bouncycastle.crypto.engines;

import java.math.BigInteger;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.engines.RSACoreEngine;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.params.RSABlindingParameters;
import org.bouncycastle.crypto.params.RSAKeyParameters;

public class RSABlindingEngine
implements AsymmetricBlockCipher {
    private BigInteger blindingFactor;
    private RSACoreEngine core = new RSACoreEngine();
    private boolean forEncryption;
    private RSAKeyParameters key;

    private BigInteger blindMessage(BigInteger bigInteger) {
        return bigInteger.multiply(this.blindingFactor.modPow(this.key.getExponent(), this.key.getModulus())).mod(this.key.getModulus());
    }

    private BigInteger unblindMessage(BigInteger bigInteger) {
        BigInteger bigInteger2 = this.key.getModulus();
        return bigInteger.multiply(this.blindingFactor.modInverse(bigInteger2)).mod(bigInteger2);
    }

    @Override
    public int getInputBlockSize() {
        return this.core.getInputBlockSize();
    }

    @Override
    public int getOutputBlockSize() {
        return this.core.getOutputBlockSize();
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        RSABlindingParameters rSABlindingParameters = cipherParameters instanceof ParametersWithRandom ? (RSABlindingParameters)((ParametersWithRandom)cipherParameters).getParameters() : (RSABlindingParameters)cipherParameters;
        this.core.init(bl, rSABlindingParameters.getPublicKey());
        this.forEncryption = bl;
        this.key = rSABlindingParameters.getPublicKey();
        this.blindingFactor = rSABlindingParameters.getBlindingFactor();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public byte[] processBlock(byte[] arrby, int n2, int n3) {
        BigInteger bigInteger;
        BigInteger bigInteger2 = this.core.convertInput(arrby, n2, n3);
        if (this.forEncryption) {
            bigInteger = this.blindMessage(bigInteger2);
            do {
                return this.core.convertOutput(bigInteger);
                break;
            } while (true);
        }
        bigInteger = this.unblindMessage(bigInteger2);
        return this.core.convertOutput(bigInteger);
    }
}

