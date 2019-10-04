/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  org.bouncycastle.util.Arrays
 */
package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.Wrapper;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.util.Arrays;

public class RFC3394WrapEngine
implements Wrapper {
    private BlockCipher engine;
    private boolean forWrapping;
    private byte[] iv = new byte[]{-90, -90, -90, -90, -90, -90, -90, -90};
    private KeyParameter param;
    private boolean wrapCipherMode;

    public RFC3394WrapEngine(BlockCipher blockCipher) {
        this(blockCipher, false);
    }

    /*
     * Enabled aggressive block sorting
     */
    public RFC3394WrapEngine(BlockCipher blockCipher, boolean bl) {
        this.engine = blockCipher;
        boolean bl2 = !bl;
        this.wrapCipherMode = bl2;
    }

    @Override
    public String getAlgorithmName() {
        return this.engine.getAlgorithmName();
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        this.forWrapping = bl;
        CipherParameters cipherParameters2 = cipherParameters instanceof ParametersWithRandom ? ((ParametersWithRandom)cipherParameters).getParameters() : cipherParameters;
        if (cipherParameters2 instanceof KeyParameter) {
            this.param = (KeyParameter)cipherParameters2;
            return;
        } else {
            if (!(cipherParameters2 instanceof ParametersWithIV)) return;
            {
                this.iv = ((ParametersWithIV)cipherParameters2).getIV();
                this.param = (KeyParameter)((ParametersWithIV)cipherParameters2).getParameters();
                if (this.iv.length == 8) return;
                {
                    throw new IllegalArgumentException("IV not equal to 8");
                }
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public byte[] unwrap(byte[] arrby, int n2, int n3) {
        if (this.forWrapping) {
            throw new IllegalStateException("not set for unwrapping");
        }
        int n4 = n3 / 8;
        if (n4 * 8 != n3) {
            throw new InvalidCipherTextException("unwrap data must be a multiple of 8 bytes");
        }
        byte[] arrby2 = new byte[n3 - this.iv.length];
        byte[] arrby3 = new byte[this.iv.length];
        byte[] arrby4 = new byte[8 + this.iv.length];
        System.arraycopy((Object)arrby, (int)n2, (Object)arrby3, (int)0, (int)this.iv.length);
        System.arraycopy((Object)arrby, (int)(n2 + this.iv.length), (Object)arrby2, (int)0, (int)(n3 - this.iv.length));
        BlockCipher blockCipher = this.engine;
        boolean bl = !this.wrapCipherMode;
        blockCipher.init(bl, this.param);
        int n5 = n4 - 1;
        int n6 = 5;
        do {
            if (n6 >= 0) {
            } else {
                if (!Arrays.constantTimeAreEqual((byte[])arrby3, (byte[])this.iv)) {
                    throw new InvalidCipherTextException("checksum failed");
                }
                return arrby2;
            }
            for (int i2 = n5; i2 >= 1; --i2) {
                System.arraycopy((Object)arrby3, (int)0, (Object)arrby4, (int)0, (int)this.iv.length);
                System.arraycopy((Object)arrby2, (int)(8 * (i2 - 1)), (Object)arrby4, (int)this.iv.length, (int)8);
                int n7 = 1;
                for (int i3 = i2 + n5 * n6; i3 != 0; i3 >>>= 8, ++n7) {
                    byte by = (byte)i3;
                    int n8 = this.iv.length - n7;
                    arrby4[n8] = (byte)(by ^ arrby4[n8]);
                }
                this.engine.processBlock(arrby4, 0, arrby4, 0);
                System.arraycopy((Object)arrby4, (int)0, (Object)arrby3, (int)0, (int)8);
                System.arraycopy((Object)arrby4, (int)8, (Object)arrby2, (int)(8 * (i2 - 1)), (int)8);
            }
            --n6;
        } while (true);
    }

    @Override
    public byte[] wrap(byte[] arrby, int n2, int n3) {
        if (!this.forWrapping) {
            throw new IllegalStateException("not set for wrapping");
        }
        int n4 = n3 / 8;
        if (n4 * 8 != n3) {
            throw new DataLengthException("wrap data must be a multiple of 8 bytes");
        }
        byte[] arrby2 = new byte[n3 + this.iv.length];
        byte[] arrby3 = new byte[8 + this.iv.length];
        System.arraycopy((Object)this.iv, (int)0, (Object)arrby2, (int)0, (int)this.iv.length);
        System.arraycopy((Object)arrby, (int)n2, (Object)arrby2, (int)this.iv.length, (int)n3);
        this.engine.init(this.wrapCipherMode, this.param);
        for (int i2 = 0; i2 != 6; ++i2) {
            for (int i3 = 1; i3 <= n4; ++i3) {
                System.arraycopy((Object)arrby2, (int)0, (Object)arrby3, (int)0, (int)this.iv.length);
                System.arraycopy((Object)arrby2, (int)(i3 * 8), (Object)arrby3, (int)this.iv.length, (int)8);
                this.engine.processBlock(arrby3, 0, arrby3, 0);
                int n5 = i3 + n4 * i2;
                int n6 = 1;
                while (n5 != 0) {
                    byte by = (byte)n5;
                    int n7 = this.iv.length - n6;
                    arrby3[n7] = (byte)(by ^ arrby3[n7]);
                    n5 >>>= 8;
                    ++n6;
                }
                System.arraycopy((Object)arrby3, (int)0, (Object)arrby2, (int)0, (int)8);
                System.arraycopy((Object)arrby3, (int)8, (Object)arrby2, (int)(i3 * 8), (int)8);
            }
        }
        return arrby2;
    }
}

