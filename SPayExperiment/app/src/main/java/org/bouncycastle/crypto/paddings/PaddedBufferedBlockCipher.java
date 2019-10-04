/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.security.SecureRandom
 */
package org.bouncycastle.crypto.paddings;

import java.security.SecureRandom;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.paddings.BlockCipherPadding;
import org.bouncycastle.crypto.paddings.PKCS7Padding;
import org.bouncycastle.crypto.params.ParametersWithRandom;

public class PaddedBufferedBlockCipher
extends BufferedBlockCipher {
    BlockCipherPadding padding;

    public PaddedBufferedBlockCipher(BlockCipher blockCipher) {
        this(blockCipher, new PKCS7Padding());
    }

    public PaddedBufferedBlockCipher(BlockCipher blockCipher, BlockCipherPadding blockCipherPadding) {
        this.cipher = blockCipher;
        this.padding = blockCipherPadding;
        this.buf = new byte[blockCipher.getBlockSize()];
        this.bufOff = 0;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public int doFinal(byte[] arrby, int n2) {
        int n3;
        block7 : {
            block6 : {
                int n4;
                block5 : {
                    n4 = this.cipher.getBlockSize();
                    if (!this.forEncryption) break block5;
                    if (this.bufOff != n4) break block6;
                    if (n2 + n4 * 2 > arrby.length) {
                        this.reset();
                        throw new OutputLengthException("output buffer too short");
                    }
                    n3 = this.cipher.processBlock(this.buf, 0, arrby, n2);
                    this.bufOff = 0;
                    break block7;
                }
                if (this.bufOff != n4) {
                    this.reset();
                    throw new DataLengthException("last block incomplete in decryption");
                }
                int n5 = this.cipher.processBlock(this.buf, 0, this.buf, 0);
                this.bufOff = 0;
                try {
                    int n6 = n5 - this.padding.padCount(this.buf);
                    System.arraycopy((Object)this.buf, (int)0, (Object)arrby, (int)n2, (int)n6);
                    return n6;
                }
                finally {
                    this.reset();
                }
            }
            n3 = 0;
        }
        this.padding.addPadding(this.buf, this.bufOff);
        int n7 = n3 + this.cipher.processBlock(this.buf, 0, arrby, n2 + n3);
        this.reset();
        return n7;
    }

    @Override
    public int getOutputSize(int n2) {
        int n3 = n2 + this.bufOff;
        int n4 = n3 % this.buf.length;
        if (n4 == 0) {
            if (this.forEncryption) {
                n3 += this.buf.length;
            }
            return n3;
        }
        return n3 - n4 + this.buf.length;
    }

    @Override
    public int getUpdateOutputSize(int n2) {
        int n3 = n2 + this.bufOff;
        int n4 = n3 % this.buf.length;
        if (n4 == 0) {
            return Math.max((int)0, (int)(n3 - this.buf.length));
        }
        return n3 - n4;
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        this.forEncryption = bl;
        this.reset();
        if (cipherParameters instanceof ParametersWithRandom) {
            ParametersWithRandom parametersWithRandom = (ParametersWithRandom)cipherParameters;
            this.padding.init(parametersWithRandom.getRandom());
            this.cipher.init(bl, parametersWithRandom.getParameters());
            return;
        }
        this.padding.init(null);
        this.cipher.init(bl, cipherParameters);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public int processByte(byte by, byte[] arrby, int n2) {
        int n3;
        if (this.bufOff == this.buf.length) {
            n3 = this.cipher.processBlock(this.buf, 0, arrby, n2);
            this.bufOff = 0;
        } else {
            n3 = 0;
        }
        byte[] arrby2 = this.buf;
        int n4 = this.bufOff;
        this.bufOff = n4 + 1;
        arrby2[n4] = by;
        return n3;
    }

    @Override
    public int processBytes(byte[] arrby, int n2, int n3, byte[] arrby2, int n4) {
        int n5;
        int n6;
        int n7;
        if (n3 < 0) {
            throw new IllegalArgumentException("Can't have a negative input length!");
        }
        int n8 = this.getBlockSize();
        int n9 = this.getUpdateOutputSize(n3);
        if (n9 > 0 && n9 + n4 > arrby2.length) {
            throw new OutputLengthException("output buffer too short");
        }
        int n10 = this.buf.length - this.bufOff;
        if (n3 > n10) {
            System.arraycopy((Object)arrby, (int)n2, (Object)this.buf, (int)this.bufOff, (int)n10);
            n6 = 0 + this.cipher.processBlock(this.buf, 0, arrby2, n4);
            this.bufOff = 0;
            n5 = n3 - n10;
            n7 = n10 + n2;
            while (n5 > this.buf.length) {
                n6 += this.cipher.processBlock(arrby, n7, arrby2, n4 + n6);
                n5 -= n8;
                n7 += n8;
            }
        } else {
            n6 = 0;
            n7 = n2;
            n5 = n3;
        }
        System.arraycopy((Object)arrby, (int)n7, (Object)this.buf, (int)this.bufOff, (int)n5);
        this.bufOff = n5 + this.bufOff;
        return n6;
    }
}

