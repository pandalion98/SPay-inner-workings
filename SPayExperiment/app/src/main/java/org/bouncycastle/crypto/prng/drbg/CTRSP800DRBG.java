/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  org.bouncycastle.util.Arrays
 *  org.bouncycastle.util.encoders.Hex
 */
package org.bouncycastle.crypto.prng.drbg;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.prng.EntropySource;
import org.bouncycastle.crypto.prng.drbg.SP80090DRBG;
import org.bouncycastle.crypto.prng.drbg.Utils;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.encoders.Hex;

public class CTRSP800DRBG
implements SP80090DRBG {
    private static final int AES_MAX_BITS_REQUEST = 262144;
    private static final long AES_RESEED_MAX = 0x800000000000L;
    private static final byte[] K_BITS = Hex.decode((String)"000102030405060708090A0B0C0D0E0F101112131415161718191A1B1C1D1E1F");
    private static final int TDEA_MAX_BITS_REQUEST = 4096;
    private static final long TDEA_RESEED_MAX = 0x80000000L;
    private byte[] _Key;
    private byte[] _V;
    private BlockCipher _engine;
    private EntropySource _entropySource;
    private boolean _isTDEA = false;
    private int _keySizeInBits;
    private long _reseedCounter = 0L;
    private int _seedLength;

    public CTRSP800DRBG(BlockCipher blockCipher, int n2, int n3, EntropySource entropySource, byte[] arrby, byte[] arrby2) {
        this._entropySource = entropySource;
        this._engine = blockCipher;
        this._keySizeInBits = n2;
        this._seedLength = n2 + 8 * blockCipher.getBlockSize();
        this._isTDEA = this.isTDEA(blockCipher);
        if (n3 > 256) {
            throw new IllegalArgumentException("Requested security strength is not supported by the derivation function");
        }
        if (this.getMaxSecurityStrength(blockCipher, n2) < n3) {
            throw new IllegalArgumentException("Requested security strength is not supported by block cipher and key size");
        }
        if (entropySource.entropySize() < n3) {
            throw new IllegalArgumentException("Not enough entropy for security strength required");
        }
        this.CTR_DRBG_Instantiate_algorithm(entropySource.getEntropy(), arrby2, arrby);
    }

    private void BCC(byte[] arrby, byte[] arrby2, byte[] arrby3, byte[] arrby4) {
        int n2 = this._engine.getBlockSize();
        byte[] arrby5 = new byte[n2];
        int n3 = arrby4.length / n2;
        byte[] arrby6 = new byte[n2];
        this._engine.init(true, new KeyParameter(this.expandKey(arrby2)));
        this._engine.processBlock(arrby3, 0, arrby5, 0);
        for (int i2 = 0; i2 < n3; ++i2) {
            this.XOR(arrby6, arrby5, arrby4, i2 * n2);
            this._engine.processBlock(arrby6, 0, arrby5, 0);
        }
        System.arraycopy((Object)arrby5, (int)0, (Object)arrby, (int)0, (int)arrby.length);
    }

    /*
     * Enabled aggressive block sorting
     */
    private byte[] Block_Cipher_df(byte[] arrby, int n2) {
        int n3 = this._engine.getBlockSize();
        int n4 = arrby.length;
        int n5 = n2 / 8;
        byte[] arrby2 = new byte[n3 * ((-1 + (n3 + (1 + (n4 + 8)))) / n3)];
        this.copyIntToByteArray(arrby2, n4, 0);
        this.copyIntToByteArray(arrby2, n5, 4);
        System.arraycopy((Object)arrby, (int)0, (Object)arrby2, (int)8, (int)n4);
        arrby2[n4 + 8] = -128;
        byte[] arrby3 = new byte[n3 + this._keySizeInBits / 8];
        byte[] arrby4 = new byte[n3];
        byte[] arrby5 = new byte[n3];
        byte[] arrby6 = new byte[this._keySizeInBits / 8];
        System.arraycopy((Object)K_BITS, (int)0, (Object)arrby6, (int)0, (int)arrby6.length);
        int n6 = 0;
        while (8 * (n6 * n3) < this._keySizeInBits + n3 * 8) {
            this.copyIntToByteArray(arrby5, n6, 0);
            this.BCC(arrby4, arrby6, arrby5, arrby2);
            int n7 = arrby3.length - n6 * n3 > n3 ? n3 : arrby3.length - n6 * n3;
            System.arraycopy((Object)arrby4, (int)0, (Object)arrby3, (int)(n6 * n3), (int)n7);
            ++n6;
        }
        byte[] arrby7 = new byte[n3];
        System.arraycopy((Object)arrby3, (int)0, (Object)arrby6, (int)0, (int)arrby6.length);
        System.arraycopy((Object)arrby3, (int)arrby6.length, (Object)arrby7, (int)0, (int)arrby7.length);
        byte[] arrby8 = new byte[n2 / 2];
        this._engine.init(true, new KeyParameter(this.expandKey(arrby6)));
        int n8 = 0;
        while (n8 * n3 < arrby8.length) {
            this._engine.processBlock(arrby7, 0, arrby7, 0);
            int n9 = arrby8.length - n8 * n3 > n3 ? n3 : arrby8.length - n8 * n3;
            System.arraycopy((Object)arrby7, (int)0, (Object)arrby8, (int)(n8 * n3), (int)n9);
            ++n8;
        }
        return arrby8;
    }

    private void CTR_DRBG_Instantiate_algorithm(byte[] arrby, byte[] arrby2, byte[] arrby3) {
        byte[] arrby4 = this.Block_Cipher_df(Arrays.concatenate((byte[])arrby, (byte[])arrby2, (byte[])arrby3), this._seedLength);
        int n2 = this._engine.getBlockSize();
        this._Key = new byte[(7 + this._keySizeInBits) / 8];
        this._V = new byte[n2];
        this.CTR_DRBG_Update(arrby4, this._Key, this._V);
        this._reseedCounter = 1L;
    }

    private void CTR_DRBG_Reseed_algorithm(EntropySource entropySource, byte[] arrby) {
        this.CTR_DRBG_Update(this.Block_Cipher_df(Arrays.concatenate((byte[])entropySource.getEntropy(), (byte[])arrby), this._seedLength), this._Key, this._V);
        this._reseedCounter = 1L;
    }

    /*
     * Enabled aggressive block sorting
     */
    private void CTR_DRBG_Update(byte[] arrby, byte[] arrby2, byte[] arrby3) {
        byte[] arrby4 = new byte[arrby.length];
        byte[] arrby5 = new byte[this._engine.getBlockSize()];
        int n2 = this._engine.getBlockSize();
        this._engine.init(true, new KeyParameter(this.expandKey(arrby2)));
        int n3 = 0;
        do {
            if (n3 * n2 >= arrby.length) {
                this.XOR(arrby4, arrby, arrby4, 0);
                System.arraycopy((Object)arrby4, (int)0, (Object)arrby2, (int)0, (int)arrby2.length);
                System.arraycopy((Object)arrby4, (int)arrby2.length, (Object)arrby3, (int)0, (int)arrby3.length);
                return;
            }
            this.addOneTo(arrby3);
            this._engine.processBlock(arrby3, 0, arrby5, 0);
            int n4 = arrby4.length - n3 * n2 > n2 ? n2 : arrby4.length - n3 * n2;
            System.arraycopy((Object)arrby5, (int)0, (Object)arrby4, (int)(n3 * n2), (int)n4);
            ++n3;
        } while (true);
    }

    private void XOR(byte[] arrby, byte[] arrby2, byte[] arrby3, int n2) {
        for (int i2 = 0; i2 < arrby.length; ++i2) {
            arrby[i2] = (byte)(arrby2[i2] ^ arrby3[i2 + n2]);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private void addOneTo(byte[] arrby) {
        int n2 = 1;
        int n3 = 1;
        while (n2 <= arrby.length) {
            int n4 = n3 + (255 & arrby[arrby.length - n2]);
            n3 = n4 > 255 ? 1 : 0;
            arrby[arrby.length - n2] = (byte)n4;
            ++n2;
        }
        return;
    }

    private void copyIntToByteArray(byte[] arrby, int n2, int n3) {
        arrby[n3 + 0] = (byte)(n2 >> 24);
        arrby[n3 + 1] = (byte)(n2 >> 16);
        arrby[n3 + 2] = (byte)(n2 >> 8);
        arrby[n3 + 3] = (byte)n2;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private int getMaxSecurityStrength(BlockCipher blockCipher, int n2) {
        if (this.isTDEA(blockCipher) && n2 == 168) {
            return 112;
        }
        if (blockCipher.getAlgorithmName().equals((Object)"AES")) return n2;
        return -1;
    }

    private boolean isTDEA(BlockCipher blockCipher) {
        return blockCipher.getAlgorithmName().equals((Object)"DESede") || blockCipher.getAlgorithmName().equals((Object)"TDEA");
    }

    private void padKey(byte[] arrby, int n2, byte[] arrby2, int n3) {
        arrby2[n3 + 0] = (byte)(254 & arrby[n2 + 0]);
        arrby2[n3 + 1] = (byte)(arrby[n2 + 0] << 7 | (252 & arrby[n2 + 1]) >>> 1);
        arrby2[n3 + 2] = (byte)(arrby[n2 + 1] << 6 | (248 & arrby[n2 + 2]) >>> 2);
        arrby2[n3 + 3] = (byte)(arrby[n2 + 2] << 5 | (240 & arrby[n2 + 3]) >>> 3);
        arrby2[n3 + 4] = (byte)(arrby[n2 + 3] << 4 | (224 & arrby[n2 + 4]) >>> 4);
        arrby2[n3 + 5] = (byte)(arrby[n2 + 4] << 3 | (192 & arrby[n2 + 5]) >>> 5);
        arrby2[n3 + 6] = (byte)(arrby[n2 + 5] << 2 | (128 & arrby[n2 + 6]) >>> 6);
        arrby2[n3 + 7] = (byte)(arrby[n2 + 6] << 1);
        for (int i2 = n3; i2 <= n3 + 7; ++i2) {
            byte by = arrby2[i2];
            arrby2[i2] = (byte)(by & 254 | 1 & (1 ^ (by >> 1 ^ by >> 2 ^ by >> 3 ^ by >> 4 ^ by >> 5 ^ by >> 6 ^ by >> 7)));
        }
    }

    byte[] expandKey(byte[] arrby) {
        if (this._isTDEA) {
            byte[] arrby2 = new byte[24];
            this.padKey(arrby, 0, arrby2, 0);
            this.padKey(arrby, 7, arrby2, 8);
            this.padKey(arrby, 14, arrby2, 16);
            arrby = arrby2;
        }
        return arrby;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public int generate(byte[] arrby, byte[] arrby2, boolean bl) {
        byte[] arrby3;
        block14 : {
            block15 : {
                block13 : {
                    block12 : {
                        if (!this._isTDEA) break block12;
                        if (this._reseedCounter > 0x80000000L) break block13;
                        if (Utils.isTooLarge(arrby, 512)) {
                            throw new IllegalArgumentException("Number of bits per request limited to 4096");
                        }
                        break block14;
                    }
                    if (this._reseedCounter <= 0x800000000000L) break block15;
                }
                return -1;
            }
            if (Utils.isTooLarge(arrby, 32768)) {
                throw new IllegalArgumentException("Number of bits per request limited to 262144");
            }
        }
        if (bl) {
            this.CTR_DRBG_Reseed_algorithm(this._entropySource, arrby2);
            arrby2 = null;
        }
        if (arrby2 != null) {
            arrby3 = this.Block_Cipher_df(arrby2, this._seedLength);
            this.CTR_DRBG_Update(arrby3, this._Key, this._V);
        } else {
            arrby3 = new byte[this._seedLength];
        }
        byte[] arrby4 = new byte[this._V.length];
        this._engine.init(true, new KeyParameter(this.expandKey(this._Key)));
        int n2 = 0;
        do {
            if (n2 > arrby.length / arrby4.length) {
                this.CTR_DRBG_Update(arrby3, this._Key, this._V);
                this._reseedCounter = 1L + this._reseedCounter;
                return 8 * arrby.length;
            }
            int n3 = arrby.length - n2 * arrby4.length > arrby4.length ? arrby4.length : arrby.length - n2 * this._V.length;
            if (n3 != 0) {
                this.addOneTo(this._V);
                this._engine.processBlock(this._V, 0, arrby4, 0);
                System.arraycopy((Object)arrby4, (int)0, (Object)arrby, (int)(n2 * arrby4.length), (int)n3);
            }
            ++n2;
        } while (true);
    }

    @Override
    public int getBlockSize() {
        return 8 * this._V.length;
    }

    @Override
    public void reseed(byte[] arrby) {
        this.CTR_DRBG_Reseed_algorithm(this._entropySource, arrby);
    }
}

