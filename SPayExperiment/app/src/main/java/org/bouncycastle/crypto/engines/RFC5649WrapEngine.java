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
 *  org.bouncycastle.util.Pack
 */
package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.Wrapper;
import org.bouncycastle.crypto.engines.RFC3394WrapEngine;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Pack;

public class RFC5649WrapEngine
implements Wrapper {
    private BlockCipher engine;
    private byte[] extractedAIV = null;
    private boolean forWrapping;
    private byte[] highOrderIV = new byte[]{-90, 89, 89, -90};
    private KeyParameter param;
    private byte[] preIV = this.highOrderIV;

    public RFC5649WrapEngine(BlockCipher blockCipher) {
        this.engine = blockCipher;
    }

    private byte[] padPlaintext(byte[] arrby) {
        int n2 = arrby.length;
        int n3 = (8 - n2 % 8) % 8;
        byte[] arrby2 = new byte[n2 + n3];
        System.arraycopy((Object)arrby, (int)0, (Object)arrby2, (int)0, (int)n2);
        if (n3 != 0) {
            System.arraycopy((Object)new byte[n3], (int)0, (Object)arrby2, (int)n2, (int)n3);
        }
        return arrby2;
    }

    private byte[] rfc3394UnwrapNoIvCheck(byte[] arrby, int n2, int n3) {
        byte[] arrby2 = new byte[8];
        byte[] arrby3 = new byte[n3 - arrby2.length];
        byte[] arrby4 = new byte[arrby2.length];
        byte[] arrby5 = new byte[8 + arrby2.length];
        System.arraycopy((Object)arrby, (int)n2, (Object)arrby4, (int)0, (int)arrby2.length);
        System.arraycopy((Object)arrby, (int)(n2 + arrby2.length), (Object)arrby3, (int)0, (int)(n3 - arrby2.length));
        this.engine.init(false, this.param);
        int n4 = -1 + n3 / 8;
        for (int i2 = 5; i2 >= 0; --i2) {
            for (int i3 = n4; i3 >= 1; --i3) {
                System.arraycopy((Object)arrby4, (int)0, (Object)arrby5, (int)0, (int)arrby2.length);
                System.arraycopy((Object)arrby3, (int)(8 * (i3 - 1)), (Object)arrby5, (int)arrby2.length, (int)8);
                int n5 = i3 + n4 * i2;
                int n6 = 1;
                while (n5 != 0) {
                    byte by = (byte)n5;
                    int n7 = arrby2.length - n6;
                    arrby5[n7] = (byte)(by ^ arrby5[n7]);
                    n5 >>>= 8;
                    ++n6;
                }
                this.engine.processBlock(arrby5, 0, arrby5, 0);
                System.arraycopy((Object)arrby5, (int)0, (Object)arrby4, (int)0, (int)8);
                System.arraycopy((Object)arrby5, (int)8, (Object)arrby3, (int)(8 * (i3 - 1)), (int)8);
            }
        }
        this.extractedAIV = arrby4;
        return arrby3;
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
                this.preIV = ((ParametersWithIV)cipherParameters2).getIV();
                this.param = (KeyParameter)((ParametersWithIV)cipherParameters2).getParameters();
                if (this.preIV.length == 4) return;
                {
                    throw new IllegalArgumentException("IV length not equal to 4");
                }
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public byte[] unwrap(byte[] arrby, int n2, int n3) {
        byte[] arrby2;
        int n4;
        int n5;
        int n6;
        int n7;
        int n8 = 1;
        if (this.forWrapping) {
            throw new IllegalStateException("not set for unwrapping");
        }
        int n9 = n3 / 8;
        if (n9 * 8 != n3) {
            throw new InvalidCipherTextException("unwrap data must be a multiple of 8 bytes");
        }
        if (n9 == n8) {
            throw new InvalidCipherTextException("unwrap data must be at least 16 bytes");
        }
        byte[] arrby3 = new byte[n3];
        System.arraycopy((Object)arrby, (int)n2, (Object)arrby3, (int)0, (int)n3);
        byte[] arrby4 = new byte[n3];
        if (n9 == 2) {
            this.engine.init(false, this.param);
            for (int i2 = 0; i2 < arrby3.length; i2 += this.engine.getBlockSize()) {
                this.engine.processBlock(arrby3, i2, arrby4, i2);
            }
            this.extractedAIV = new byte[8];
            System.arraycopy((Object)arrby4, (int)0, (Object)this.extractedAIV, (int)0, (int)this.extractedAIV.length);
            arrby2 = new byte[arrby4.length - this.extractedAIV.length];
            System.arraycopy((Object)arrby4, (int)this.extractedAIV.length, (Object)arrby2, (int)0, (int)arrby2.length);
        } else {
            arrby2 = this.rfc3394UnwrapNoIvCheck(arrby, n2, n3);
        }
        byte[] arrby5 = new byte[4];
        byte[] arrby6 = new byte[4];
        System.arraycopy((Object)this.extractedAIV, (int)0, (Object)arrby5, (int)0, (int)arrby5.length);
        System.arraycopy((Object)this.extractedAIV, (int)arrby5.length, (Object)arrby6, (int)0, (int)arrby6.length);
        int n10 = Pack.bigEndianToInt((byte[])arrby6, (int)0);
        if (!Arrays.constantTimeAreEqual((byte[])arrby5, (byte[])this.preIV)) {
            n8 = 0;
        }
        if (n10 <= (n7 = arrby2.length) - 8) {
            n8 = 0;
        }
        if (n10 > n7) {
            n8 = 0;
        }
        if ((n6 = n7 - n10) >= arrby2.length) {
            n5 = arrby2.length;
            n4 = 0;
        } else {
            n4 = n8;
            n5 = n6;
        }
        byte[] arrby7 = new byte[n5];
        byte[] arrby8 = new byte[n5];
        System.arraycopy((Object)arrby2, (int)(arrby2.length - n5), (Object)arrby8, (int)0, (int)n5);
        if (!Arrays.constantTimeAreEqual((byte[])arrby8, (byte[])arrby7)) {
            n4 = 0;
        }
        if (n4 == 0) {
            throw new InvalidCipherTextException("checksum failed");
        }
        byte[] arrby9 = new byte[n10];
        System.arraycopy((Object)arrby2, (int)0, (Object)arrby9, (int)0, (int)arrby9.length);
        return arrby9;
    }

    @Override
    public byte[] wrap(byte[] arrby, int n2, int n3) {
        if (!this.forWrapping) {
            throw new IllegalStateException("not set for wrapping");
        }
        byte[] arrby2 = new byte[8];
        byte[] arrby3 = Pack.intToBigEndian((int)n3);
        System.arraycopy((Object)this.preIV, (int)0, (Object)arrby2, (int)0, (int)this.preIV.length);
        System.arraycopy((Object)arrby3, (int)0, (Object)arrby2, (int)this.preIV.length, (int)arrby3.length);
        byte[] arrby4 = new byte[n3];
        System.arraycopy((Object)arrby, (int)n2, (Object)arrby4, (int)0, (int)n3);
        byte[] arrby5 = this.padPlaintext(arrby4);
        if (arrby5.length == 8) {
            byte[] arrby6 = new byte[arrby5.length + arrby2.length];
            System.arraycopy((Object)arrby2, (int)0, (Object)arrby6, (int)0, (int)arrby2.length);
            System.arraycopy((Object)arrby5, (int)0, (Object)arrby6, (int)arrby2.length, (int)arrby5.length);
            this.engine.init(true, this.param);
            for (int i2 = 0; i2 < arrby6.length; i2 += this.engine.getBlockSize()) {
                this.engine.processBlock(arrby6, i2, arrby6, i2);
            }
            return arrby6;
        }
        RFC3394WrapEngine rFC3394WrapEngine = new RFC3394WrapEngine(this.engine);
        rFC3394WrapEngine.init(true, new ParametersWithIV(this.param, arrby2));
        return rFC3394WrapEngine.wrap(arrby5, n2, arrby5.length);
    }
}

