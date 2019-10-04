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
import org.bouncycastle.crypto.engines.DESedeEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.util.Arrays;

public class DESedeWrapEngine
implements Wrapper {
    private static final byte[] IV2 = new byte[]{74, -35, -94, 44, 121, -24, 33, 5};
    byte[] digest = new byte[20];
    private CBCBlockCipher engine;
    private boolean forWrapping;
    private byte[] iv;
    private KeyParameter param;
    private ParametersWithIV paramPlusIV;
    Digest sha1 = new SHA1Digest();

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

    private static byte[] reverse(byte[] arrby) {
        byte[] arrby2 = new byte[arrby.length];
        for (int i2 = 0; i2 < arrby.length; ++i2) {
            arrby2[i2] = arrby[arrby.length - (i2 + 1)];
        }
        return arrby2;
    }

    @Override
    public String getAlgorithmName() {
        return "DESede";
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        CipherParameters cipherParameters2;
        SecureRandom secureRandom;
        this.forWrapping = bl;
        this.engine = new CBCBlockCipher(new DESedeEngine());
        if (cipherParameters instanceof ParametersWithRandom) {
            ParametersWithRandom parametersWithRandom = (ParametersWithRandom)cipherParameters;
            CipherParameters cipherParameters3 = parametersWithRandom.getParameters();
            SecureRandom secureRandom2 = parametersWithRandom.getRandom();
            cipherParameters2 = cipherParameters3;
            secureRandom = secureRandom2;
        } else {
            secureRandom = new SecureRandom();
            cipherParameters2 = cipherParameters;
        }
        if (cipherParameters2 instanceof KeyParameter) {
            this.param = (KeyParameter)cipherParameters2;
            if (!this.forWrapping) return;
            {
                this.iv = new byte[8];
                secureRandom.nextBytes(this.iv);
                this.paramPlusIV = new ParametersWithIV(this.param, this.iv);
                return;
            }
        } else {
            if (!(cipherParameters2 instanceof ParametersWithIV)) return;
            {
                this.paramPlusIV = (ParametersWithIV)cipherParameters2;
                this.iv = this.paramPlusIV.getIV();
                this.param = (KeyParameter)this.paramPlusIV.getParameters();
                if (!this.forWrapping) {
                    throw new IllegalArgumentException("You should not supply an IV for unwrapping");
                }
                if (this.iv != null && this.iv.length == 8) return;
                {
                    throw new IllegalArgumentException("IV is not 8 octets");
                }
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
        int n4 = this.engine.getBlockSize();
        if (n3 % n4 != 0) {
            throw new InvalidCipherTextException("Ciphertext not multiple of " + n4);
        }
        ParametersWithIV parametersWithIV = new ParametersWithIV(this.param, IV2);
        this.engine.init(false, parametersWithIV);
        byte[] arrby2 = new byte[n3];
        for (int i2 = 0; i2 != n3; i2 += n4) {
            this.engine.processBlock(arrby, n2 + i2, arrby2, i2);
        }
        byte[] arrby3 = DESedeWrapEngine.reverse(arrby2);
        this.iv = new byte[8];
        byte[] arrby4 = new byte[-8 + arrby3.length];
        System.arraycopy((Object)arrby3, (int)0, (Object)this.iv, (int)0, (int)8);
        System.arraycopy((Object)arrby3, (int)8, (Object)arrby4, (int)0, (int)(-8 + arrby3.length));
        this.paramPlusIV = new ParametersWithIV(this.param, this.iv);
        this.engine.init(false, this.paramPlusIV);
        byte[] arrby5 = new byte[arrby4.length];
        for (int i3 = 0; i3 != arrby5.length; i3 += n4) {
            this.engine.processBlock(arrby4, i3, arrby5, i3);
        }
        byte[] arrby6 = new byte[-8 + arrby5.length];
        byte[] arrby7 = new byte[8];
        System.arraycopy((Object)arrby5, (int)0, (Object)arrby6, (int)0, (int)(-8 + arrby5.length));
        System.arraycopy((Object)arrby5, (int)(-8 + arrby5.length), (Object)arrby7, (int)0, (int)8);
        if (!this.checkCMSKeyChecksum(arrby6, arrby7)) {
            throw new InvalidCipherTextException("Checksum inside ciphertext is corrupted");
        }
        return arrby6;
    }

    @Override
    public byte[] wrap(byte[] arrby, int n2, int n3) {
        int n4 = 0;
        if (!this.forWrapping) {
            throw new IllegalStateException("Not initialized for wrapping");
        }
        byte[] arrby2 = new byte[n3];
        System.arraycopy((Object)arrby, (int)n2, (Object)arrby2, (int)0, (int)n3);
        byte[] arrby3 = this.calculateCMSKeyChecksum(arrby2);
        byte[] arrby4 = new byte[arrby2.length + arrby3.length];
        System.arraycopy((Object)arrby2, (int)0, (Object)arrby4, (int)0, (int)arrby2.length);
        System.arraycopy((Object)arrby3, (int)0, (Object)arrby4, (int)arrby2.length, (int)arrby3.length);
        int n5 = this.engine.getBlockSize();
        if (arrby4.length % n5 != 0) {
            throw new IllegalStateException("Not multiple of block length");
        }
        this.engine.init(true, this.paramPlusIV);
        byte[] arrby5 = new byte[arrby4.length];
        for (int i2 = 0; i2 != arrby4.length; i2 += n5) {
            this.engine.processBlock(arrby4, i2, arrby5, i2);
        }
        byte[] arrby6 = new byte[this.iv.length + arrby5.length];
        System.arraycopy((Object)this.iv, (int)0, (Object)arrby6, (int)0, (int)this.iv.length);
        System.arraycopy((Object)arrby5, (int)0, (Object)arrby6, (int)this.iv.length, (int)arrby5.length);
        byte[] arrby7 = DESedeWrapEngine.reverse(arrby6);
        ParametersWithIV parametersWithIV = new ParametersWithIV(this.param, IV2);
        this.engine.init(true, parametersWithIV);
        while (n4 != arrby7.length) {
            this.engine.processBlock(arrby7, n4, arrby7, n4);
            n4 += n5;
        }
        return arrby7;
    }
}

