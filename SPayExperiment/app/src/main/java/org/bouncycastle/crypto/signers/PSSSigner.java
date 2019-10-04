/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.math.BigInteger
 *  java.security.SecureRandom
 */
package org.bouncycastle.crypto.signers;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.Signer;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.params.RSABlindingParameters;
import org.bouncycastle.crypto.params.RSAKeyParameters;

public class PSSSigner
implements Signer {
    public static final byte TRAILER_IMPLICIT = -68;
    private byte[] block;
    private AsymmetricBlockCipher cipher;
    private Digest contentDigest;
    private int emBits;
    private int hLen;
    private byte[] mDash;
    private Digest mgfDigest;
    private int mgfhLen;
    private SecureRandom random;
    private int sLen;
    private byte[] salt;
    private byte trailer;

    public PSSSigner(AsymmetricBlockCipher asymmetricBlockCipher, Digest digest, int n2) {
        this(asymmetricBlockCipher, digest, n2, -68);
    }

    public PSSSigner(AsymmetricBlockCipher asymmetricBlockCipher, Digest digest, int n2, byte by) {
        this(asymmetricBlockCipher, digest, digest, n2, by);
    }

    public PSSSigner(AsymmetricBlockCipher asymmetricBlockCipher, Digest digest, Digest digest2, int n2) {
        this(asymmetricBlockCipher, digest, digest2, n2, -68);
    }

    public PSSSigner(AsymmetricBlockCipher asymmetricBlockCipher, Digest digest, Digest digest2, int n2, byte by) {
        this.cipher = asymmetricBlockCipher;
        this.contentDigest = digest;
        this.mgfDigest = digest2;
        this.hLen = digest.getDigestSize();
        this.mgfhLen = digest2.getDigestSize();
        this.sLen = n2;
        this.salt = new byte[n2];
        this.mDash = new byte[n2 + 8 + this.hLen];
        this.trailer = by;
    }

    private void ItoOSP(int n2, byte[] arrby) {
        arrby[0] = (byte)(n2 >>> 24);
        arrby[1] = (byte)(n2 >>> 16);
        arrby[2] = (byte)(n2 >>> 8);
        arrby[3] = (byte)(n2 >>> 0);
    }

    private void clearBlock(byte[] arrby) {
        for (int i2 = 0; i2 != arrby.length; ++i2) {
            arrby[i2] = 0;
        }
    }

    private byte[] maskGeneratorFunction1(byte[] arrby, int n2, int n3, int n4) {
        int n5;
        byte[] arrby2 = new byte[n4];
        byte[] arrby3 = new byte[this.mgfhLen];
        byte[] arrby4 = new byte[4];
        this.mgfDigest.reset();
        for (n5 = 0; n5 < n4 / this.mgfhLen; ++n5) {
            this.ItoOSP(n5, arrby4);
            this.mgfDigest.update(arrby, n2, n3);
            this.mgfDigest.update(arrby4, 0, arrby4.length);
            this.mgfDigest.doFinal(arrby3, 0);
            System.arraycopy((Object)arrby3, (int)0, (Object)arrby2, (int)(n5 * this.mgfhLen), (int)this.mgfhLen);
        }
        if (n5 * this.mgfhLen < n4) {
            this.ItoOSP(n5, arrby4);
            this.mgfDigest.update(arrby, n2, n3);
            this.mgfDigest.update(arrby4, 0, arrby4.length);
            this.mgfDigest.doFinal(arrby3, 0);
            System.arraycopy((Object)arrby3, (int)0, (Object)arrby2, (int)(n5 * this.mgfhLen), (int)(arrby2.length - n5 * this.mgfhLen));
        }
        return arrby2;
    }

    @Override
    public byte[] generateSignature() {
        this.contentDigest.doFinal(this.mDash, this.mDash.length - this.hLen - this.sLen);
        if (this.sLen != 0) {
            this.random.nextBytes(this.salt);
            System.arraycopy((Object)this.salt, (int)0, (Object)this.mDash, (int)(this.mDash.length - this.sLen), (int)this.sLen);
        }
        byte[] arrby = new byte[this.hLen];
        this.contentDigest.update(this.mDash, 0, this.mDash.length);
        this.contentDigest.doFinal(arrby, 0);
        this.block[-1 + (-1 + (this.block.length - this.sLen) - this.hLen)] = 1;
        System.arraycopy((Object)this.salt, (int)0, (Object)this.block, (int)(-1 + (this.block.length - this.sLen - this.hLen)), (int)this.sLen);
        byte[] arrby2 = this.maskGeneratorFunction1(arrby, 0, arrby.length, -1 + (this.block.length - this.hLen));
        for (int i2 = 0; i2 != arrby2.length; ++i2) {
            byte[] arrby3 = this.block;
            arrby3[i2] = (byte)(arrby3[i2] ^ arrby2[i2]);
        }
        byte[] arrby4 = this.block;
        arrby4[0] = (byte)(arrby4[0] & 255 >> 8 * this.block.length - this.emBits);
        System.arraycopy((Object)arrby, (int)0, (Object)this.block, (int)(-1 + (this.block.length - this.hLen)), (int)this.hLen);
        this.block[-1 + this.block.length] = this.trailer;
        byte[] arrby5 = this.cipher.processBlock(this.block, 0, this.block.length);
        this.clearBlock(this.block);
        return arrby5;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        CipherParameters cipherParameters2;
        if (cipherParameters instanceof ParametersWithRandom) {
            ParametersWithRandom parametersWithRandom = (ParametersWithRandom)cipherParameters;
            cipherParameters2 = parametersWithRandom.getParameters();
            this.random = parametersWithRandom.getRandom();
        } else {
            if (bl) {
                this.random = new SecureRandom();
            }
            cipherParameters2 = cipherParameters;
        }
        this.cipher.init(bl, cipherParameters2);
        RSAKeyParameters rSAKeyParameters = cipherParameters2 instanceof RSABlindingParameters ? ((RSABlindingParameters)cipherParameters2).getPublicKey() : (RSAKeyParameters)cipherParameters2;
        this.emBits = -1 + rSAKeyParameters.getModulus().bitLength();
        if (this.emBits < 9 + (8 * this.hLen + 8 * this.sLen)) {
            throw new IllegalArgumentException("key too small for specified hash and salt lengths");
        }
        this.block = new byte[(7 + this.emBits) / 8];
        this.reset();
    }

    @Override
    public void reset() {
        this.contentDigest.reset();
    }

    @Override
    public void update(byte by) {
        this.contentDigest.update(by);
    }

    @Override
    public void update(byte[] arrby, int n2, int n3) {
        this.contentDigest.update(arrby, n2, n3);
    }

    @Override
    public boolean verifySignature(byte[] arrby) {
        this.contentDigest.doFinal(this.mDash, this.mDash.length - this.hLen - this.sLen);
        try {
            byte[] arrby2 = this.cipher.processBlock(arrby, 0, arrby.length);
            System.arraycopy((Object)arrby2, (int)0, (Object)this.block, (int)(this.block.length - arrby2.length), (int)arrby2.length);
        }
        catch (Exception exception) {
            return false;
        }
        if (this.block[-1 + this.block.length] != this.trailer) {
            this.clearBlock(this.block);
            return false;
        }
        byte[] arrby3 = this.maskGeneratorFunction1(this.block, -1 + (this.block.length - this.hLen), this.hLen, -1 + (this.block.length - this.hLen));
        for (int i2 = 0; i2 != arrby3.length; ++i2) {
            byte[] arrby4 = this.block;
            arrby4[i2] = (byte)(arrby4[i2] ^ arrby3[i2]);
        }
        byte[] arrby5 = this.block;
        arrby5[0] = (byte)(arrby5[0] & 255 >> 8 * this.block.length - this.emBits);
        for (int i3 = 0; i3 != -2 + (this.block.length - this.hLen - this.sLen); ++i3) {
            if (this.block[i3] == 0) continue;
            this.clearBlock(this.block);
            return false;
        }
        if (this.block[-2 + (this.block.length - this.hLen - this.sLen)] != 1) {
            this.clearBlock(this.block);
            return false;
        }
        System.arraycopy((Object)this.block, (int)(-1 + (this.block.length - this.sLen - this.hLen)), (Object)this.mDash, (int)(this.mDash.length - this.sLen), (int)this.sLen);
        this.contentDigest.update(this.mDash, 0, this.mDash.length);
        this.contentDigest.doFinal(this.mDash, this.mDash.length - this.hLen);
        int n2 = -1 + (this.block.length - this.hLen);
        for (int i4 = this.mDash.length - this.hLen; i4 != this.mDash.length; ++i4) {
            if ((this.block[n2] ^ this.mDash[i4]) != 0) {
                this.clearBlock(this.mDash);
                this.clearBlock(this.block);
                return false;
            }
            ++n2;
        }
        this.clearBlock(this.mDash);
        this.clearBlock(this.block);
        return true;
    }
}

