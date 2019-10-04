/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.security.SecureRandom
 */
package org.bouncycastle.crypto.encodings;

import java.security.SecureRandom;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.params.ParametersWithRandom;

public class OAEPEncoding
implements AsymmetricBlockCipher {
    private byte[] defHash;
    private AsymmetricBlockCipher engine;
    private boolean forEncryption;
    private Digest mgf1Hash;
    private SecureRandom random;

    public OAEPEncoding(AsymmetricBlockCipher asymmetricBlockCipher) {
        this(asymmetricBlockCipher, new SHA1Digest(), null);
    }

    public OAEPEncoding(AsymmetricBlockCipher asymmetricBlockCipher, Digest digest) {
        this(asymmetricBlockCipher, digest, null);
    }

    public OAEPEncoding(AsymmetricBlockCipher asymmetricBlockCipher, Digest digest, Digest digest2, byte[] arrby) {
        this.engine = asymmetricBlockCipher;
        this.mgf1Hash = digest2;
        this.defHash = new byte[digest.getDigestSize()];
        digest.reset();
        if (arrby != null) {
            digest.update(arrby, 0, arrby.length);
        }
        digest.doFinal(this.defHash, 0);
    }

    public OAEPEncoding(AsymmetricBlockCipher asymmetricBlockCipher, Digest digest, byte[] arrby) {
        this(asymmetricBlockCipher, digest, digest, arrby);
    }

    private void ItoOSP(int n2, byte[] arrby) {
        arrby[0] = (byte)(n2 >>> 24);
        arrby[1] = (byte)(n2 >>> 16);
        arrby[2] = (byte)(n2 >>> 8);
        arrby[3] = (byte)(n2 >>> 0);
    }

    private byte[] maskGeneratorFunction1(byte[] arrby, int n2, int n3, int n4) {
        int n5;
        byte[] arrby2 = new byte[n4];
        byte[] arrby3 = new byte[this.mgf1Hash.getDigestSize()];
        byte[] arrby4 = new byte[4];
        this.mgf1Hash.reset();
        for (n5 = 0; n5 < n4 / arrby3.length; ++n5) {
            this.ItoOSP(n5, arrby4);
            this.mgf1Hash.update(arrby, n2, n3);
            this.mgf1Hash.update(arrby4, 0, arrby4.length);
            this.mgf1Hash.doFinal(arrby3, 0);
            System.arraycopy((Object)arrby3, (int)0, (Object)arrby2, (int)(n5 * arrby3.length), (int)arrby3.length);
        }
        if (n5 * arrby3.length < n4) {
            this.ItoOSP(n5, arrby4);
            this.mgf1Hash.update(arrby, n2, n3);
            this.mgf1Hash.update(arrby4, 0, arrby4.length);
            this.mgf1Hash.doFinal(arrby3, 0);
            System.arraycopy((Object)arrby3, (int)0, (Object)arrby2, (int)(n5 * arrby3.length), (int)(arrby2.length - n5 * arrby3.length));
        }
        return arrby2;
    }

    /*
     * Enabled aggressive block sorting
     */
    public byte[] decodeBlock(byte[] arrby, int n2, int n3) {
        byte[] arrby2;
        byte[] arrby3 = this.engine.processBlock(arrby, n2, n3);
        if (arrby3.length < this.engine.getOutputBlockSize()) {
            arrby2 = new byte[this.engine.getOutputBlockSize()];
            System.arraycopy((Object)arrby3, (int)0, (Object)arrby2, (int)(arrby2.length - arrby3.length), (int)arrby3.length);
        } else {
            arrby2 = arrby3;
        }
        if (arrby2.length < 1 + 2 * this.defHash.length) {
            throw new InvalidCipherTextException("data too short");
        }
        byte[] arrby4 = this.maskGeneratorFunction1(arrby2, this.defHash.length, arrby2.length - this.defHash.length, this.defHash.length);
        for (int i2 = 0; i2 != this.defHash.length; ++i2) {
            arrby2[i2] = (byte)(arrby2[i2] ^ arrby4[i2]);
        }
        byte[] arrby5 = this.maskGeneratorFunction1(arrby2, 0, this.defHash.length, arrby2.length - this.defHash.length);
        for (int i3 = this.defHash.length; i3 != arrby2.length; ++i3) {
            arrby2[i3] = (byte)(arrby2[i3] ^ arrby5[i3 - this.defHash.length]);
        }
        boolean bl = false;
        for (int i4 = 0; i4 != this.defHash.length; ++i4) {
            if (this.defHash[i4] == arrby2[i4 + this.defHash.length]) continue;
            bl = true;
        }
        if (bl) {
            throw new InvalidCipherTextException("data hash wrong");
        }
        int n4 = 2 * this.defHash.length;
        do {
            if (n4 == arrby2.length || arrby2[n4] != 0) {
                if (n4 < -1 + arrby2.length && arrby2[n4] == 1) {
                    int n5 = n4 + 1;
                    byte[] arrby6 = new byte[arrby2.length - n5];
                    System.arraycopy((Object)arrby2, (int)n5, (Object)arrby6, (int)0, (int)arrby6.length);
                    return arrby6;
                }
                throw new InvalidCipherTextException("data start wrong " + n4);
            }
            ++n4;
        } while (true);
    }

    public byte[] encodeBlock(byte[] arrby, int n2, int n3) {
        byte[] arrby2 = new byte[1 + this.getInputBlockSize() + 2 * this.defHash.length];
        System.arraycopy((Object)arrby, (int)n2, (Object)arrby2, (int)(arrby2.length - n3), (int)n3);
        arrby2[-1 + (arrby2.length - n3)] = 1;
        System.arraycopy((Object)this.defHash, (int)0, (Object)arrby2, (int)this.defHash.length, (int)this.defHash.length);
        byte[] arrby3 = new byte[this.defHash.length];
        this.random.nextBytes(arrby3);
        byte[] arrby4 = this.maskGeneratorFunction1(arrby3, 0, arrby3.length, arrby2.length - this.defHash.length);
        for (int i2 = this.defHash.length; i2 != arrby2.length; ++i2) {
            arrby2[i2] = (byte)(arrby2[i2] ^ arrby4[i2 - this.defHash.length]);
        }
        System.arraycopy((Object)arrby3, (int)0, (Object)arrby2, (int)0, (int)this.defHash.length);
        byte[] arrby5 = this.maskGeneratorFunction1(arrby2, this.defHash.length, arrby2.length - this.defHash.length, this.defHash.length);
        for (int i3 = 0; i3 != this.defHash.length; ++i3) {
            arrby2[i3] = (byte)(arrby2[i3] ^ arrby5[i3]);
        }
        return this.engine.processBlock(arrby2, 0, arrby2.length);
    }

    @Override
    public int getInputBlockSize() {
        int n2 = this.engine.getInputBlockSize();
        if (this.forEncryption) {
            n2 = n2 - 1 - 2 * this.defHash.length;
        }
        return n2;
    }

    @Override
    public int getOutputBlockSize() {
        int n2 = this.engine.getOutputBlockSize();
        if (this.forEncryption) {
            return n2;
        }
        return n2 - 1 - 2 * this.defHash.length;
    }

    public AsymmetricBlockCipher getUnderlyingCipher() {
        return this.engine;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        this.random = cipherParameters instanceof ParametersWithRandom ? ((ParametersWithRandom)cipherParameters).getRandom() : new SecureRandom();
        this.engine.init(bl, cipherParameters);
        this.forEncryption = bl;
    }

    @Override
    public byte[] processBlock(byte[] arrby, int n2, int n3) {
        if (this.forEncryption) {
            return this.encodeBlock(arrby, n2, n3);
        }
        return this.decodeBlock(arrby, n2, n3);
    }
}

