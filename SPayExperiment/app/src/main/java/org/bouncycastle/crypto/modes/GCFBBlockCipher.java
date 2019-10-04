/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 */
package org.bouncycastle.crypto.modes;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.StreamBlockCipher;
import org.bouncycastle.crypto.modes.CFBBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.params.ParametersWithSBox;

public class GCFBBlockCipher
extends StreamBlockCipher {
    private static final byte[] C = new byte[]{105, 0, 114, 34, 100, -55, 4, 35, -115, 58, -37, -106, 70, -23, 42, -60, 24, -2, -84, -108, 0, -19, 7, 18, -64, -122, -36, -62, -17, 76, -87, 43};
    private final CFBBlockCipher cfbEngine;
    private long counter = 0L;
    private boolean forEncryption;
    private KeyParameter key;

    public GCFBBlockCipher(BlockCipher blockCipher) {
        super(blockCipher);
        this.cfbEngine = new CFBBlockCipher(blockCipher, 8 * blockCipher.getBlockSize());
    }

    @Override
    protected byte calculateByte(byte by) {
        if (this.counter > 0L && this.counter % 1024L == 0L) {
            BlockCipher blockCipher = this.cfbEngine.getUnderlyingCipher();
            blockCipher.init(false, this.key);
            byte[] arrby = new byte[32];
            blockCipher.processBlock(C, 0, arrby, 0);
            blockCipher.processBlock(C, 8, arrby, 8);
            blockCipher.processBlock(C, 16, arrby, 16);
            blockCipher.processBlock(C, 24, arrby, 24);
            this.key = new KeyParameter(arrby);
            blockCipher.init(true, this.key);
            byte[] arrby2 = this.cfbEngine.getCurrentIV();
            blockCipher.processBlock(arrby2, 0, arrby2, 0);
            this.cfbEngine.init(this.forEncryption, new ParametersWithIV(this.key, arrby2));
        }
        this.counter = 1L + this.counter;
        return this.cfbEngine.calculateByte(by);
    }

    @Override
    public String getAlgorithmName() {
        String string = this.cfbEngine.getAlgorithmName();
        return string.substring(0, -1 + string.indexOf(47)) + "/G" + string.substring(1 + string.indexOf(47));
    }

    @Override
    public int getBlockSize() {
        return this.cfbEngine.getBlockSize();
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        this.counter = 0L;
        this.cfbEngine.init(bl, cipherParameters);
        this.forEncryption = bl;
        CipherParameters cipherParameters2 = cipherParameters instanceof ParametersWithIV ? ((ParametersWithIV)cipherParameters).getParameters() : cipherParameters;
        if (cipherParameters2 instanceof ParametersWithRandom) {
            cipherParameters2 = ((ParametersWithRandom)cipherParameters2).getParameters();
        }
        if (cipherParameters2 instanceof ParametersWithSBox) {
            cipherParameters2 = ((ParametersWithSBox)cipherParameters2).getParameters();
        }
        this.key = (KeyParameter)cipherParameters2;
    }

    @Override
    public int processBlock(byte[] arrby, int n2, byte[] arrby2, int n3) {
        this.processBytes(arrby, n2, this.cfbEngine.getBlockSize(), arrby2, n3);
        return this.cfbEngine.getBlockSize();
    }

    @Override
    public void reset() {
        this.counter = 0L;
        this.cfbEngine.reset();
    }
}

