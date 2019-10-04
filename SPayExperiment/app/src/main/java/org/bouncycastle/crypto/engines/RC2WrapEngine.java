/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.security.SecureRandom
 *  org.bouncycastle.util.Arrays
 */
package org.bouncycastle.crypto.engines;

import java.security.SecureRandom;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.Wrapper;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.engines.RC2Engine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.util.Arrays;

public class RC2WrapEngine
implements Wrapper {
    private static final byte[] IV2 = new byte[]{74, -35, -94, 44, 121, -24, 33, 5};
    byte[] digest = new byte[20];
    private CBCBlockCipher engine;
    private boolean forWrapping;
    private byte[] iv;
    private CipherParameters param;
    private ParametersWithIV paramPlusIV;
    Digest sha1 = new SHA1Digest();
    private SecureRandom sr;

    private byte[] calculateCMSKeyChecksum(byte[] arrby) {
        byte[] arrby2 = new byte[8];
        this.sha1.update(arrby, 0, arrby.length);
        this.sha1.doFinal(this.digest, 0);
        System.arraycopy((Object)this.digest, (int)0, (Object)arrby2, (int)0, (int)8);
        return arrby2;
    }

    private boolean checkCMSKeyChecksum(byte[] arrby, byte[] arrby2) {
        return Arrays.constantTimeAreEqual((byte[])this.calculateCMSKeyChecksum(arrby), (byte[])arrby2);
    }

    @Override
    public String getAlgorithmName() {
        return "RC2";
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        CipherParameters cipherParameters2;
        this.forWrapping = bl;
        this.engine = new CBCBlockCipher(new RC2Engine());
        if (cipherParameters instanceof ParametersWithRandom) {
            ParametersWithRandom parametersWithRandom = (ParametersWithRandom)cipherParameters;
            this.sr = parametersWithRandom.getRandom();
            cipherParameters2 = parametersWithRandom.getParameters();
        } else {
            this.sr = new SecureRandom();
            cipherParameters2 = cipherParameters;
        }
        if (cipherParameters2 instanceof ParametersWithIV) {
            this.paramPlusIV = (ParametersWithIV)cipherParameters2;
            this.iv = this.paramPlusIV.getIV();
            this.param = this.paramPlusIV.getParameters();
            if (!this.forWrapping) {
                throw new IllegalArgumentException("You should not supply an IV for unwrapping");
            }
            if (this.iv != null && this.iv.length == 8) return;
            {
                throw new IllegalArgumentException("IV is not 8 octets");
            }
        } else {
            this.param = cipherParameters2;
            if (!this.forWrapping) return;
            {
                this.iv = new byte[8];
                this.sr.nextBytes(this.iv);
                this.paramPlusIV = new ParametersWithIV(this.param, this.iv);
            }
        }
    }

    @Override
    public byte[] unwrap(byte[] arrby, int n2, int n3) {
        if (this.forWrapping) {
            throw new IllegalStateException("Not set for unwrapping");
        }
        if (arrby == null) {
            throw new InvalidCipherTextException("Null pointer as ciphertext");
        }
        if (n3 % this.engine.getBlockSize() != 0) {
            throw new InvalidCipherTextException("Ciphertext not multiple of " + this.engine.getBlockSize());
        }
        ParametersWithIV parametersWithIV = new ParametersWithIV(this.param, IV2);
        this.engine.init(false, parametersWithIV);
        byte[] arrby2 = new byte[n3];
        System.arraycopy((Object)arrby, (int)n2, (Object)arrby2, (int)0, (int)n3);
        for (int i2 = 0; i2 < arrby2.length / this.engine.getBlockSize(); ++i2) {
            int n4 = i2 * this.engine.getBlockSize();
            this.engine.processBlock(arrby2, n4, arrby2, n4);
        }
        byte[] arrby3 = new byte[arrby2.length];
        for (int i3 = 0; i3 < arrby2.length; ++i3) {
            arrby3[i3] = arrby2[arrby2.length - (i3 + 1)];
        }
        this.iv = new byte[8];
        byte[] arrby4 = new byte[-8 + arrby3.length];
        System.arraycopy((Object)arrby3, (int)0, (Object)this.iv, (int)0, (int)8);
        System.arraycopy((Object)arrby3, (int)8, (Object)arrby4, (int)0, (int)(-8 + arrby3.length));
        this.paramPlusIV = new ParametersWithIV(this.param, this.iv);
        this.engine.init(false, this.paramPlusIV);
        byte[] arrby5 = new byte[arrby4.length];
        System.arraycopy((Object)arrby4, (int)0, (Object)arrby5, (int)0, (int)arrby4.length);
        for (int i4 = 0; i4 < arrby5.length / this.engine.getBlockSize(); ++i4) {
            int n5 = i4 * this.engine.getBlockSize();
            this.engine.processBlock(arrby5, n5, arrby5, n5);
        }
        byte[] arrby6 = new byte[-8 + arrby5.length];
        byte[] arrby7 = new byte[8];
        System.arraycopy((Object)arrby5, (int)0, (Object)arrby6, (int)0, (int)(-8 + arrby5.length));
        System.arraycopy((Object)arrby5, (int)(-8 + arrby5.length), (Object)arrby7, (int)0, (int)8);
        if (!this.checkCMSKeyChecksum(arrby6, arrby7)) {
            throw new InvalidCipherTextException("Checksum inside ciphertext is corrupted");
        }
        if (arrby6.length - (1 + (255 & arrby6[0])) > 7) {
            throw new InvalidCipherTextException("too many pad bytes (" + (arrby6.length - (1 + (255 & arrby6[0]))) + ")");
        }
        byte[] arrby8 = new byte[arrby6[0]];
        System.arraycopy((Object)arrby6, (int)1, (Object)arrby8, (int)0, (int)arrby8.length);
        return arrby8;
    }

    @Override
    public byte[] wrap(byte[] arrby, int n2, int n3) {
        int n4 = 0;
        if (!this.forWrapping) {
            throw new IllegalStateException("Not initialized for wrapping");
        }
        int n5 = n3 + 1;
        if (n5 % 8 != 0) {
            n5 += 8 - n5 % 8;
        }
        byte[] arrby2 = new byte[n5];
        arrby2[0] = (byte)n3;
        System.arraycopy((Object)arrby, (int)n2, (Object)arrby2, (int)1, (int)n3);
        byte[] arrby3 = new byte[-1 + (arrby2.length - n3)];
        if (arrby3.length > 0) {
            this.sr.nextBytes(arrby3);
            System.arraycopy((Object)arrby3, (int)0, (Object)arrby2, (int)(n3 + 1), (int)arrby3.length);
        }
        byte[] arrby4 = this.calculateCMSKeyChecksum(arrby2);
        byte[] arrby5 = new byte[arrby2.length + arrby4.length];
        System.arraycopy((Object)arrby2, (int)0, (Object)arrby5, (int)0, (int)arrby2.length);
        System.arraycopy((Object)arrby4, (int)0, (Object)arrby5, (int)arrby2.length, (int)arrby4.length);
        byte[] arrby6 = new byte[arrby5.length];
        System.arraycopy((Object)arrby5, (int)0, (Object)arrby6, (int)0, (int)arrby5.length);
        int n6 = arrby5.length / this.engine.getBlockSize();
        if (arrby5.length % this.engine.getBlockSize() != 0) {
            throw new IllegalStateException("Not multiple of block length");
        }
        this.engine.init(true, this.paramPlusIV);
        for (int i2 = 0; i2 < n6; ++i2) {
            int n7 = i2 * this.engine.getBlockSize();
            this.engine.processBlock(arrby6, n7, arrby6, n7);
        }
        byte[] arrby7 = new byte[this.iv.length + arrby6.length];
        System.arraycopy((Object)this.iv, (int)0, (Object)arrby7, (int)0, (int)this.iv.length);
        System.arraycopy((Object)arrby6, (int)0, (Object)arrby7, (int)this.iv.length, (int)arrby6.length);
        byte[] arrby8 = new byte[arrby7.length];
        for (int i3 = 0; i3 < arrby7.length; ++i3) {
            arrby8[i3] = arrby7[arrby7.length - (i3 + 1)];
        }
        ParametersWithIV parametersWithIV = new ParametersWithIV(this.param, IV2);
        this.engine.init(true, parametersWithIV);
        while (n4 < n6 + 1) {
            int n8 = n4 * this.engine.getBlockSize();
            this.engine.processBlock(arrby8, n8, arrby8, n8);
            ++n4;
        }
        return arrby8;
    }
}

