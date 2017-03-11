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

public class McEliecePointchevalCipher implements MessageEncryptor {
    public static final String OID = "1.3.6.1.4.1.8301.3.1.3.4.2.2";
    private int f431k;
    McElieceCCA2KeyParameters key;
    private Digest messDigest;
    private int f432n;
    private SecureRandom sr;
    private int f433t;

    protected int decryptOutputSize(int i) {
        return 0;
    }

    protected int encryptOutputSize(int i) {
        return 0;
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
        this.f432n = mcElieceCCA2PrivateKeyParameters.getN();
        this.f431k = mcElieceCCA2PrivateKeyParameters.getK();
        this.f433t = mcElieceCCA2PrivateKeyParameters.getT();
    }

    public void initCipherEncrypt(McElieceCCA2PublicKeyParameters mcElieceCCA2PublicKeyParameters) {
        this.sr = this.sr != null ? this.sr : new SecureRandom();
        this.messDigest = mcElieceCCA2PublicKeyParameters.getParameters().getDigest();
        this.f432n = mcElieceCCA2PublicKeyParameters.getN();
        this.f431k = mcElieceCCA2PublicKeyParameters.getK();
        this.f433t = mcElieceCCA2PublicKeyParameters.getT();
    }

    public byte[] messageDecrypt(byte[] bArr) {
        int i = (this.f432n + 7) >> 3;
        int length = bArr.length - i;
        byte[][] split = ByteUtils.split(bArr, i);
        byte[] bArr2 = split[0];
        byte[] bArr3 = split[1];
        GF2Vector[] decryptionPrimitive = McElieceCCA2Primitives.decryptionPrimitive((McElieceCCA2PrivateKeyParameters) this.key, GF2Vector.OS2VP(this.f432n, bArr2));
        bArr2 = decryptionPrimitive[0].getEncoded();
        Object obj = decryptionPrimitive[1];
        DigestRandomGenerator digestRandomGenerator = new DigestRandomGenerator(new SHA1Digest());
        digestRandomGenerator.addSeedMaterial(bArr2);
        bArr2 = new byte[length];
        digestRandomGenerator.nextBytes(bArr2);
        for (i = 0; i < length; i++) {
            bArr2[i] = (byte) (bArr2[i] ^ bArr3[i]);
        }
        this.messDigest.update(bArr2, 0, bArr2.length);
        byte[] bArr4 = new byte[this.messDigest.getDigestSize()];
        this.messDigest.doFinal(bArr4, 0);
        if (Conversions.encode(this.f432n, this.f433t, bArr4).equals(obj)) {
            return ByteUtils.split(bArr2, length - (this.f431k >> 3))[0];
        }
        throw new Exception("Bad Padding: Invalid ciphertext.");
    }

    public byte[] messageEncrypt(byte[] bArr) {
        int i;
        int i2 = 0;
        int i3 = this.f431k >> 3;
        byte[] bArr2 = new byte[i3];
        this.sr.nextBytes(bArr2);
        GF2Vector gF2Vector = new GF2Vector(this.f431k, this.sr);
        byte[] encoded = gF2Vector.getEncoded();
        byte[] concatenate = ByteUtils.concatenate(bArr, bArr2);
        this.messDigest.update(concatenate, 0, concatenate.length);
        concatenate = new byte[this.messDigest.getDigestSize()];
        this.messDigest.doFinal(concatenate, 0);
        byte[] encoded2 = McElieceCCA2Primitives.encryptionPrimitive((McElieceCCA2PublicKeyParameters) this.key, gF2Vector, Conversions.encode(this.f432n, this.f433t, concatenate)).getEncoded();
        DigestRandomGenerator digestRandomGenerator = new DigestRandomGenerator(new SHA1Digest());
        digestRandomGenerator.addSeedMaterial(encoded);
        encoded = new byte[(bArr.length + i3)];
        digestRandomGenerator.nextBytes(encoded);
        for (i = 0; i < bArr.length; i++) {
            encoded[i] = (byte) (encoded[i] ^ bArr[i]);
        }
        while (i2 < i3) {
            i = bArr.length + i2;
            encoded[i] = (byte) (encoded[i] ^ bArr2[i2]);
            i2++;
        }
        return ByteUtils.concatenate(encoded2, encoded);
    }
}
