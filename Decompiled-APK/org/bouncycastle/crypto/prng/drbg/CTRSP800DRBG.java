package org.bouncycastle.crypto.prng.drbg;

import com.mastercard.mobile_api.utils.apdu.emv.VerifyPINApdu;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.macs.SkeinMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.prng.EntropySource;
import org.bouncycastle.crypto.tls.CipherSuite;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.encoders.Hex;

public class CTRSP800DRBG implements SP80090DRBG {
    private static final int AES_MAX_BITS_REQUEST = 262144;
    private static final long AES_RESEED_MAX = 140737488355328L;
    private static final byte[] K_BITS;
    private static final int TDEA_MAX_BITS_REQUEST = 4096;
    private static final long TDEA_RESEED_MAX = 2147483648L;
    private byte[] _Key;
    private byte[] _V;
    private BlockCipher _engine;
    private EntropySource _entropySource;
    private boolean _isTDEA;
    private int _keySizeInBits;
    private long _reseedCounter;
    private int _seedLength;

    static {
        K_BITS = Hex.decode("000102030405060708090A0B0C0D0E0F101112131415161718191A1B1C1D1E1F");
    }

    public CTRSP800DRBG(BlockCipher blockCipher, int i, int i2, EntropySource entropySource, byte[] bArr, byte[] bArr2) {
        this._reseedCounter = 0;
        this._isTDEA = false;
        this._entropySource = entropySource;
        this._engine = blockCipher;
        this._keySizeInBits = i;
        this._seedLength = (blockCipher.getBlockSize() * 8) + i;
        this._isTDEA = isTDEA(blockCipher);
        if (i2 > SkeinMac.SKEIN_256) {
            throw new IllegalArgumentException("Requested security strength is not supported by the derivation function");
        } else if (getMaxSecurityStrength(blockCipher, i) < i2) {
            throw new IllegalArgumentException("Requested security strength is not supported by block cipher and key size");
        } else if (entropySource.entropySize() < i2) {
            throw new IllegalArgumentException("Not enough entropy for security strength required");
        } else {
            CTR_DRBG_Instantiate_algorithm(entropySource.getEntropy(), bArr2, bArr);
        }
    }

    private void BCC(byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4) {
        int blockSize = this._engine.getBlockSize();
        Object obj = new byte[blockSize];
        int length = bArr4.length / blockSize;
        byte[] bArr5 = new byte[blockSize];
        this._engine.init(true, new KeyParameter(expandKey(bArr2)));
        this._engine.processBlock(bArr3, 0, obj, 0);
        for (int i = 0; i < length; i++) {
            XOR(bArr5, obj, bArr4, i * blockSize);
            this._engine.processBlock(bArr5, 0, obj, 0);
        }
        System.arraycopy(obj, 0, bArr, 0, bArr.length);
    }

    private byte[] Block_Cipher_df(byte[] bArr, int i) {
        int blockSize = this._engine.getBlockSize();
        int length = bArr.length;
        int i2 = i / 8;
        Object obj = new byte[((((((length + 8) + 1) + blockSize) - 1) / blockSize) * blockSize)];
        copyIntToByteArray(obj, length, 0);
        copyIntToByteArray(obj, i2, 4);
        System.arraycopy(bArr, 0, obj, 8, length);
        obj[length + 8] = VerifyPINApdu.P2_PLAINTEXT;
        Object obj2 = new byte[((this._keySizeInBits / 8) + blockSize)];
        Object obj3 = new byte[blockSize];
        byte[] bArr2 = new byte[blockSize];
        Object obj4 = new byte[(this._keySizeInBits / 8)];
        System.arraycopy(K_BITS, 0, obj4, 0, obj4.length);
        for (length = 0; (length * blockSize) * 8 < this._keySizeInBits + (blockSize * 8); length++) {
            copyIntToByteArray(bArr2, length, 0);
            BCC(obj3, obj4, bArr2, obj);
            System.arraycopy(obj3, 0, obj2, length * blockSize, obj2.length - (length * blockSize) > blockSize ? blockSize : obj2.length - (length * blockSize));
        }
        obj = new byte[blockSize];
        System.arraycopy(obj2, 0, obj4, 0, obj4.length);
        System.arraycopy(obj2, obj4.length, obj, 0, obj.length);
        obj2 = new byte[(i / 2)];
        this._engine.init(true, new KeyParameter(expandKey(obj4)));
        for (length = 0; length * blockSize < obj2.length; length++) {
            this._engine.processBlock(obj, 0, obj, 0);
            System.arraycopy(obj, 0, obj2, length * blockSize, obj2.length - (length * blockSize) > blockSize ? blockSize : obj2.length - (length * blockSize));
        }
        return obj2;
    }

    private void CTR_DRBG_Instantiate_algorithm(byte[] bArr, byte[] bArr2, byte[] bArr3) {
        byte[] Block_Cipher_df = Block_Cipher_df(Arrays.concatenate(bArr, bArr2, bArr3), this._seedLength);
        int blockSize = this._engine.getBlockSize();
        this._Key = new byte[((this._keySizeInBits + 7) / 8)];
        this._V = new byte[blockSize];
        CTR_DRBG_Update(Block_Cipher_df, this._Key, this._V);
        this._reseedCounter = 1;
    }

    private void CTR_DRBG_Reseed_algorithm(EntropySource entropySource, byte[] bArr) {
        CTR_DRBG_Update(Block_Cipher_df(Arrays.concatenate(entropySource.getEntropy(), bArr), this._seedLength), this._Key, this._V);
        this._reseedCounter = 1;
    }

    private void CTR_DRBG_Update(byte[] bArr, byte[] bArr2, byte[] bArr3) {
        Object obj = new byte[bArr.length];
        Object obj2 = new byte[this._engine.getBlockSize()];
        int blockSize = this._engine.getBlockSize();
        this._engine.init(true, new KeyParameter(expandKey(bArr2)));
        for (int i = 0; i * blockSize < bArr.length; i++) {
            addOneTo(bArr3);
            this._engine.processBlock(bArr3, 0, obj2, 0);
            System.arraycopy(obj2, 0, obj, i * blockSize, obj.length - (i * blockSize) > blockSize ? blockSize : obj.length - (i * blockSize));
        }
        XOR(obj, bArr, obj, 0);
        System.arraycopy(obj, 0, bArr2, 0, bArr2.length);
        System.arraycopy(obj, bArr2.length, bArr3, 0, bArr3.length);
    }

    private void XOR(byte[] bArr, byte[] bArr2, byte[] bArr3, int i) {
        for (int i2 = 0; i2 < bArr.length; i2++) {
            bArr[i2] = (byte) (bArr2[i2] ^ bArr3[i2 + i]);
        }
    }

    private void addOneTo(byte[] bArr) {
        int i = 1;
        for (int i2 = 1; i2 <= bArr.length; i2++) {
            int i3 = (bArr[bArr.length - i2] & GF2Field.MASK) + i;
            i = i3 > GF2Field.MASK ? 1 : 0;
            bArr[bArr.length - i2] = (byte) i3;
        }
    }

    private void copyIntToByteArray(byte[] bArr, int i, int i2) {
        bArr[i2 + 0] = (byte) (i >> 24);
        bArr[i2 + 1] = (byte) (i >> 16);
        bArr[i2 + 2] = (byte) (i >> 8);
        bArr[i2 + 3] = (byte) i;
    }

    private int getMaxSecurityStrength(BlockCipher blockCipher, int i) {
        return (isTDEA(blockCipher) && i == CipherSuite.TLS_PSK_WITH_AES_128_GCM_SHA256) ? 112 : !blockCipher.getAlgorithmName().equals("AES") ? -1 : i;
    }

    private boolean isTDEA(BlockCipher blockCipher) {
        return blockCipher.getAlgorithmName().equals("DESede") || blockCipher.getAlgorithmName().equals("TDEA");
    }

    private void padKey(byte[] bArr, int i, byte[] bArr2, int i2) {
        bArr2[i2 + 0] = (byte) (bArr[i + 0] & 254);
        bArr2[i2 + 1] = (byte) ((bArr[i + 0] << 7) | ((bArr[i + 1] & 252) >>> 1));
        bArr2[i2 + 2] = (byte) ((bArr[i + 1] << 6) | ((bArr[i + 2] & 248) >>> 2));
        bArr2[i2 + 3] = (byte) ((bArr[i + 2] << 5) | ((bArr[i + 3] & 240) >>> 3));
        bArr2[i2 + 4] = (byte) ((bArr[i + 3] << 4) | ((bArr[i + 4] & 224) >>> 4));
        bArr2[i2 + 5] = (byte) ((bArr[i + 4] << 3) | ((bArr[i + 5] & CipherSuite.TLS_RSA_WITH_CAMELLIA_256_CBC_SHA256) >>> 5));
        bArr2[i2 + 6] = (byte) ((bArr[i + 5] << 2) | ((bArr[i + 6] & X509KeyUsage.digitalSignature) >>> 6));
        bArr2[i2 + 7] = (byte) (bArr[i + 6] << 1);
        for (int i3 = i2; i3 <= i2 + 7; i3++) {
            byte b = bArr2[i3];
            bArr2[i3] = (byte) (((((b >> 7) ^ ((((((b >> 1) ^ (b >> 2)) ^ (b >> 3)) ^ (b >> 4)) ^ (b >> 5)) ^ (b >> 6))) ^ 1) & 1) | (b & 254));
        }
    }

    byte[] expandKey(byte[] bArr) {
        if (!this._isTDEA) {
            return bArr;
        }
        byte[] bArr2 = new byte[24];
        padKey(bArr, 0, bArr2, 0);
        padKey(bArr, 7, bArr2, 8);
        padKey(bArr, 14, bArr2, 16);
        return bArr2;
    }

    public int generate(byte[] bArr, byte[] bArr2, boolean z) {
        byte[] Block_Cipher_df;
        if (this._isTDEA) {
            if (this._reseedCounter > TDEA_RESEED_MAX) {
                return -1;
            }
            if (Utils.isTooLarge(bArr, SkeinMac.SKEIN_512)) {
                throw new IllegalArgumentException("Number of bits per request limited to 4096");
            }
        } else if (this._reseedCounter > AES_RESEED_MAX) {
            return -1;
        } else {
            if (Utils.isTooLarge(bArr, X509KeyUsage.decipherOnly)) {
                throw new IllegalArgumentException("Number of bits per request limited to 262144");
            }
        }
        if (z) {
            CTR_DRBG_Reseed_algorithm(this._entropySource, bArr2);
            bArr2 = null;
        }
        if (bArr2 != null) {
            Block_Cipher_df = Block_Cipher_df(bArr2, this._seedLength);
            CTR_DRBG_Update(Block_Cipher_df, this._Key, this._V);
        } else {
            Block_Cipher_df = new byte[this._seedLength];
        }
        Object obj = new byte[this._V.length];
        this._engine.init(true, new KeyParameter(expandKey(this._Key)));
        for (int i = 0; i <= bArr.length / obj.length; i++) {
            int length = bArr.length - (obj.length * i) > obj.length ? obj.length : bArr.length - (this._V.length * i);
            if (length != 0) {
                addOneTo(this._V);
                this._engine.processBlock(this._V, 0, obj, 0);
                System.arraycopy(obj, 0, bArr, obj.length * i, length);
            }
        }
        CTR_DRBG_Update(Block_Cipher_df, this._Key, this._V);
        this._reseedCounter++;
        return bArr.length * 8;
    }

    public int getBlockSize() {
        return this._V.length * 8;
    }

    public void reseed(byte[] bArr) {
        CTR_DRBG_Reseed_algorithm(this._entropySource, bArr);
    }
}
