package org.bouncycastle.pqc.crypto.mceliece;

import java.security.SecureRandom;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.prng.DigestRandomGenerator;
import org.bouncycastle.pqc.crypto.MessageEncryptor;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.bouncycastle.pqc.math.linearalgebra.GF2Vector;
import org.bouncycastle.pqc.math.linearalgebra.IntegerFunctions;

public class McElieceKobaraImaiCipher implements MessageEncryptor {
    private static final String DEFAULT_PRNG_NAME = "SHA1PRNG";
    public static final String OID = "1.3.6.1.4.1.8301.3.1.3.4.2.3";
    public static final byte[] PUBLIC_CONSTANT;
    private int f425k;
    McElieceCCA2KeyParameters key;
    private Digest messDigest;
    private int f426n;
    private SecureRandom sr;
    private int f427t;

    static {
        PUBLIC_CONSTANT = "a predetermined public constant".getBytes();
    }

    private void initCipherEncrypt(McElieceCCA2PublicKeyParameters mcElieceCCA2PublicKeyParameters) {
        this.messDigest = mcElieceCCA2PublicKeyParameters.getParameters().getDigest();
        this.f426n = mcElieceCCA2PublicKeyParameters.getN();
        this.f425k = mcElieceCCA2PublicKeyParameters.getK();
        this.f427t = mcElieceCCA2PublicKeyParameters.getT();
    }

    public int getKeySize(McElieceCCA2KeyParameters mcElieceCCA2KeyParameters) {
        if (mcElieceCCA2KeyParameters instanceof McElieceCCA2PublicKeyParameters) {
            return ((McElieceCCA2PublicKeyParameters) mcElieceCCA2KeyParameters).getN();
        }
        if (mcElieceCCA2KeyParameters instanceof McElieceCCA2PrivateKeyParameters) {
            return ((McElieceCCA2PrivateKeyParameters) mcElieceCCA2KeyParameters).getN();
        }
        throw new IllegalArgumentException("unsupported type");
    }

    public void init(boolean z, CipherParameters cipherParameters) {
        if (!z) {
            this.key = (McElieceCCA2PrivateKeyParameters) cipherParameters;
            initCipherDecrypt((McElieceCCA2PrivateKeyParameters) this.key);
        } else if (cipherParameters instanceof ParametersWithRandom) {
            ParametersWithRandom parametersWithRandom = (ParametersWithRandom) cipherParameters;
            this.sr = parametersWithRandom.getRandom();
            this.key = (McElieceCCA2PublicKeyParameters) parametersWithRandom.getParameters();
            initCipherEncrypt((McElieceCCA2PublicKeyParameters) this.key);
        } else {
            this.sr = new SecureRandom();
            this.key = (McElieceCCA2PublicKeyParameters) cipherParameters;
            initCipherEncrypt((McElieceCCA2PublicKeyParameters) this.key);
        }
    }

    public void initCipherDecrypt(McElieceCCA2PrivateKeyParameters mcElieceCCA2PrivateKeyParameters) {
        this.messDigest = mcElieceCCA2PrivateKeyParameters.getParameters().getDigest();
        this.f426n = mcElieceCCA2PrivateKeyParameters.getN();
        this.f425k = mcElieceCCA2PrivateKeyParameters.getK();
        this.f427t = mcElieceCCA2PrivateKeyParameters.getT();
    }

    public byte[] messageDecrypt(byte[] bArr) {
        int i = this.f426n >> 3;
        if (bArr.length < i) {
            throw new Exception("Bad Padding: Ciphertext too short.");
        }
        byte[] bArr2;
        byte[] bArr3;
        int digestSize = this.messDigest.getDigestSize();
        int i2 = this.f425k >> 3;
        i = bArr.length - i;
        if (i > 0) {
            byte[][] split = ByteUtils.split(bArr, i);
            bArr2 = split[0];
            bArr = split[1];
            bArr3 = bArr2;
        } else {
            bArr3 = new byte[0];
        }
        GF2Vector[] decryptionPrimitive = McElieceCCA2Primitives.decryptionPrimitive((McElieceCCA2PrivateKeyParameters) this.key, GF2Vector.OS2VP(this.f426n, bArr));
        bArr2 = decryptionPrimitive[0].getEncoded();
        GF2Vector gF2Vector = decryptionPrimitive[1];
        if (bArr2.length > i2) {
            bArr2 = ByteUtils.subArray(bArr2, 0, i2);
        }
        bArr2 = ByteUtils.concatenate(ByteUtils.concatenate(bArr3, Conversions.decode(this.f426n, this.f427t, gF2Vector)), bArr2);
        int length = bArr2.length - digestSize;
        byte[][] split2 = ByteUtils.split(bArr2, digestSize);
        byte[] bArr4 = split2[0];
        byte[] bArr5 = split2[1];
        byte[] bArr6 = new byte[this.messDigest.getDigestSize()];
        this.messDigest.update(bArr5, 0, bArr5.length);
        this.messDigest.doFinal(bArr6, 0);
        for (i = digestSize - 1; i >= 0; i--) {
            bArr6[i] = (byte) (bArr6[i] ^ bArr4[i]);
        }
        DigestRandomGenerator digestRandomGenerator = new DigestRandomGenerator(new SHA1Digest());
        digestRandomGenerator.addSeedMaterial(bArr6);
        byte[] bArr7 = new byte[length];
        digestRandomGenerator.nextBytes(bArr7);
        for (i = length - 1; i >= 0; i--) {
            bArr7[i] = (byte) (bArr7[i] ^ bArr5[i]);
        }
        if (bArr7.length < length) {
            throw new Exception("Bad Padding: invalid ciphertext");
        }
        split2 = ByteUtils.split(bArr7, length - PUBLIC_CONSTANT.length);
        bArr3 = split2[0];
        if (ByteUtils.equals(split2[1], PUBLIC_CONSTANT)) {
            return bArr3;
        }
        throw new Exception("Bad Padding: invalid ciphertext");
    }

    public byte[] messageEncrypt(byte[] bArr) {
        int digestSize = this.messDigest.getDigestSize();
        int i = this.f425k >> 3;
        int bitLength = (IntegerFunctions.binomial(this.f426n, this.f427t).bitLength() - 1) >> 3;
        int length = ((i + bitLength) - digestSize) - PUBLIC_CONSTANT.length;
        if (bArr.length > length) {
            length = bArr.length;
        }
        int length2 = PUBLIC_CONSTANT.length + length;
        int i2 = ((length2 + digestSize) - i) - bitLength;
        Object obj = new byte[length2];
        System.arraycopy(bArr, 0, obj, 0, bArr.length);
        System.arraycopy(PUBLIC_CONSTANT, 0, obj, length, PUBLIC_CONSTANT.length);
        byte[] bArr2 = new byte[digestSize];
        this.sr.nextBytes(bArr2);
        DigestRandomGenerator digestRandomGenerator = new DigestRandomGenerator(new SHA1Digest());
        digestRandomGenerator.addSeedMaterial(bArr2);
        byte[] bArr3 = new byte[length2];
        digestRandomGenerator.nextBytes(bArr3);
        for (length = length2 - 1; length >= 0; length--) {
            bArr3[length] = (byte) (bArr3[length] ^ obj[length]);
        }
        byte[] bArr4 = new byte[this.messDigest.getDigestSize()];
        this.messDigest.update(bArr3, 0, bArr3.length);
        this.messDigest.doFinal(bArr4, 0);
        for (length = digestSize - 1; length >= 0; length--) {
            bArr4[length] = (byte) (bArr4[length] ^ bArr2[length]);
        }
        Object concatenate = ByteUtils.concatenate(bArr4, bArr3);
        byte[] bArr5 = new byte[0];
        if (i2 > 0) {
            bArr5 = new byte[i2];
            System.arraycopy(concatenate, 0, bArr5, 0, i2);
        }
        byte[] bArr6 = bArr5;
        Object obj2 = new byte[bitLength];
        System.arraycopy(concatenate, i2, obj2, 0, bitLength);
        obj = new byte[i];
        System.arraycopy(concatenate, bitLength + i2, obj, 0, i);
        bArr5 = McElieceCCA2Primitives.encryptionPrimitive((McElieceCCA2PublicKeyParameters) this.key, GF2Vector.OS2VP(this.f425k, obj), Conversions.encode(this.f426n, this.f427t, obj2)).getEncoded();
        return i2 > 0 ? ByteUtils.concatenate(bArr6, bArr5) : bArr5;
    }
}
