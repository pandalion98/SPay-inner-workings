/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.crypto.macs;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.modes.GCMBlockCipher;
import org.bouncycastle.crypto.params.AEADParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

public class GMac
implements Mac {
    private final GCMBlockCipher cipher;
    private final int macSizeBits;

    public GMac(GCMBlockCipher gCMBlockCipher) {
        this.cipher = gCMBlockCipher;
        this.macSizeBits = 128;
    }

    public GMac(GCMBlockCipher gCMBlockCipher, int n2) {
        this.cipher = gCMBlockCipher;
        this.macSizeBits = n2;
    }

    @Override
    public int doFinal(byte[] arrby, int n2) {
        try {
            int n3 = this.cipher.doFinal(arrby, n2);
            return n3;
        }
        catch (InvalidCipherTextException invalidCipherTextException) {
            throw new IllegalStateException(invalidCipherTextException.toString());
        }
    }

    @Override
    public String getAlgorithmName() {
        return this.cipher.getUnderlyingCipher().getAlgorithmName() + "-GMAC";
    }

    @Override
    public int getMacSize() {
        return this.macSizeBits / 8;
    }

    @Override
    public void init(CipherParameters cipherParameters) {
        if (cipherParameters instanceof ParametersWithIV) {
            ParametersWithIV parametersWithIV = (ParametersWithIV)cipherParameters;
            byte[] arrby = parametersWithIV.getIV();
            KeyParameter keyParameter = (KeyParameter)parametersWithIV.getParameters();
            this.cipher.init(true, new AEADParameters(keyParameter, this.macSizeBits, arrby));
            return;
        }
        throw new IllegalArgumentException("GMAC requires ParametersWithIV");
    }

    @Override
    public void reset() {
        this.cipher.reset();
    }

    @Override
    public void update(byte by) {
        this.cipher.processAADByte(by);
    }

    @Override
    public void update(byte[] arrby, int n2, int n3) {
        this.cipher.processAADBytes(arrby, n2, n3);
    }
}

