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

public class McElieceFujisakiCipher implements MessageEncryptor {
    private static final String DEFAULT_PRNG_NAME = "SHA1PRNG";
    public static final String OID = "1.3.6.1.4.1.8301.3.1.3.4.2.1";
    private int f419k;
    McElieceCCA2KeyParameters key;
    private Digest messDigest;
    private int f420n;
    private SecureRandom sr;
    private int f421t;

    private void initCipherEncrypt(McElieceCCA2PublicKeyParameters mcElieceCCA2PublicKeyParameters) {
        this.sr = this.sr != null ? this.sr : new SecureRandom();
        this.messDigest = mcElieceCCA2PublicKeyParameters.getParameters().getDigest();
        this.f420n = mcElieceCCA2PublicKeyParameters.getN();
        this.f419k = mcElieceCCA2PublicKeyParameters.getK();
        this.f421t = mcElieceCCA2PublicKeyParameters.getT();
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
        this.f420n = mcElieceCCA2PrivateKeyParameters.getN();
        this.f421t = mcElieceCCA2PrivateKeyParameters.getT();
    }

    public byte[] messageDecrypt(byte[] bArr) {
        int i = (this.f420n + 7) >> 3;
        int length = bArr.length - i;
        byte[][] split = ByteUtils.split(bArr, i);
        byte[] bArr2 = split[0];
        byte[] bArr3 = split[1];
        GF2Vector[] decryptionPrimitive = McElieceCCA2Primitives.decryptionPrimitive((McElieceCCA2PrivateKeyParameters) this.key, GF2Vector.OS2VP(this.f420n, bArr2));
        bArr2 = decryptionPrimitive[0].getEncoded();
        Object obj = decryptionPrimitive[1];
        DigestRandomGenerator digestRandomGenerator = new DigestRandomGenerator(new SHA1Digest());
        digestRandomGenerator.addSeedMaterial(bArr2);
        byte[] bArr4 = new byte[length];
        digestRandomGenerator.nextBytes(bArr4);
        for (i = 0; i < length; i++) {
            bArr4[i] = (byte) (bArr4[i] ^ bArr3[i]);
        }
        byte[] concatenate = ByteUtils.concatenate(bArr2, bArr4);
        byte[] bArr5 = new byte[this.messDigest.getDigestSize()];
        this.messDigest.update(concatenate, 0, concatenate.length);
        this.messDigest.doFinal(bArr5, 0);
        if (Conversions.encode(this.f420n, this.f421t, bArr5).equals(obj)) {
            return bArr4;
        }
        throw new Exception("Bad Padding: invalid ciphertext");
    }

    public byte[] messageEncrypt(byte[] bArr) {
        GF2Vector gF2Vector = new GF2Vector(this.f419k, this.sr);
        byte[] encoded = gF2Vector.getEncoded();
        byte[] concatenate = ByteUtils.concatenate(encoded, bArr);
        this.messDigest.update(concatenate, 0, concatenate.length);
        concatenate = new byte[this.messDigest.getDigestSize()];
        this.messDigest.doFinal(concatenate, 0);
        byte[] encoded2 = McElieceCCA2Primitives.encryptionPrimitive((McElieceCCA2PublicKeyParameters) this.key, gF2Vector, Conversions.encode(this.f420n, this.f421t, concatenate)).getEncoded();
        DigestRandomGenerator digestRandomGenerator = new DigestRandomGenerator(new SHA1Digest());
        digestRandomGenerator.addSeedMaterial(encoded);
        encoded = new byte[bArr.length];
        digestRandomGenerator.nextBytes(encoded);
        for (int i = 0; i < bArr.length; i++) {
            encoded[i] = (byte) (encoded[i] ^ bArr[i]);
        }
        return ByteUtils.concatenate(encoded2, encoded);
    }
}
