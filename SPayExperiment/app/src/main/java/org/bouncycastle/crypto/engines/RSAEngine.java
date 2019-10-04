/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 */
package org.bouncycastle.crypto.engines;

import java.math.BigInteger;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.engines.RSACoreEngine;

public class RSAEngine
implements AsymmetricBlockCipher {
    private RSACoreEngine core;

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
        if (this.core == null) {
            this.core = new RSACoreEngine();
        }
        this.core.init(bl, cipherParameters);
    }

    @Override
    public byte[] processBlock(byte[] arrby, int n2, int n3) {
        if (this.core == null) {
            throw new IllegalStateException("RSA engine not initialised");
        }
        return this.core.convertOutput(this.core.processBlock(this.core.convertInput(arrby, n2, n3)));
    }
}

