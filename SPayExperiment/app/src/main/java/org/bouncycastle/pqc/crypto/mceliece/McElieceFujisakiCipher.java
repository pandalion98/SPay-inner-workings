/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.security.SecureRandom
 */
package org.bouncycastle.pqc.crypto.mceliece;

import java.security.SecureRandom;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.prng.DigestRandomGenerator;
import org.bouncycastle.pqc.crypto.MessageEncryptor;
import org.bouncycastle.pqc.crypto.mceliece.Conversions;
import org.bouncycastle.pqc.crypto.mceliece.McElieceCCA2KeyParameters;
import org.bouncycastle.pqc.crypto.mceliece.McElieceCCA2Parameters;
import org.bouncycastle.pqc.crypto.mceliece.McElieceCCA2Primitives;
import org.bouncycastle.pqc.crypto.mceliece.McElieceCCA2PrivateKeyParameters;
import org.bouncycastle.pqc.crypto.mceliece.McElieceCCA2PublicKeyParameters;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.bouncycastle.pqc.math.linearalgebra.GF2Vector;

public class McElieceFujisakiCipher
implements MessageEncryptor {
    private static final String DEFAULT_PRNG_NAME = "SHA1PRNG";
    public static final String OID = "1.3.6.1.4.1.8301.3.1.3.4.2.1";
    private int k;
    McElieceCCA2KeyParameters key;
    private Digest messDigest;
    private int n;
    private SecureRandom sr;
    private int t;

    /*
     * Enabled aggressive block sorting
     */
    private void initCipherEncrypt(McElieceCCA2PublicKeyParameters mcElieceCCA2PublicKeyParameters) {
        SecureRandom secureRandom = this.sr != null ? this.sr : new SecureRandom();
        this.sr = secureRandom;
        this.messDigest = mcElieceCCA2PublicKeyParameters.getParameters().getDigest();
        this.n = mcElieceCCA2PublicKeyParameters.getN();
        this.k = mcElieceCCA2PublicKeyParameters.getK();
        this.t = mcElieceCCA2PublicKeyParameters.getT();
    }

    public int getKeySize(McElieceCCA2KeyParameters mcElieceCCA2KeyParameters) {
        if (mcElieceCCA2KeyParameters instanceof McElieceCCA2PublicKeyParameters) {
            return ((McElieceCCA2PublicKeyParameters)mcElieceCCA2KeyParameters).getN();
        }
        if (mcElieceCCA2KeyParameters instanceof McElieceCCA2PrivateKeyParameters) {
            return ((McElieceCCA2PrivateKeyParameters)mcElieceCCA2KeyParameters).getN();
        }
        throw new IllegalArgumentException("unsupported type");
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        if (bl) {
            if (cipherParameters instanceof ParametersWithRandom) {
                ParametersWithRandom parametersWithRandom = (ParametersWithRandom)cipherParameters;
                this.sr = parametersWithRandom.getRandom();
                this.key = (McElieceCCA2PublicKeyParameters)parametersWithRandom.getParameters();
                this.initCipherEncrypt((McElieceCCA2PublicKeyParameters)this.key);
                return;
            }
            this.sr = new SecureRandom();
            this.key = (McElieceCCA2PublicKeyParameters)cipherParameters;
            this.initCipherEncrypt((McElieceCCA2PublicKeyParameters)this.key);
            return;
        }
        this.key = (McElieceCCA2PrivateKeyParameters)cipherParameters;
        this.initCipherDecrypt((McElieceCCA2PrivateKeyParameters)this.key);
    }

    public void initCipherDecrypt(McElieceCCA2PrivateKeyParameters mcElieceCCA2PrivateKeyParameters) {
        this.messDigest = mcElieceCCA2PrivateKeyParameters.getParameters().getDigest();
        this.n = mcElieceCCA2PrivateKeyParameters.getN();
        this.t = mcElieceCCA2PrivateKeyParameters.getT();
    }

    @Override
    public byte[] messageDecrypt(byte[] arrby) {
        int n = 7 + this.n >> 3;
        int n2 = arrby.length - n;
        byte[][] arrby2 = ByteUtils.split(arrby, n);
        byte[] arrby3 = arrby2[0];
        byte[] arrby4 = arrby2[1];
        GF2Vector gF2Vector = GF2Vector.OS2VP(this.n, arrby3);
        GF2Vector[] arrgF2Vector = McElieceCCA2Primitives.decryptionPrimitive((McElieceCCA2PrivateKeyParameters)this.key, gF2Vector);
        byte[] arrby5 = arrgF2Vector[0].getEncoded();
        GF2Vector gF2Vector2 = arrgF2Vector[1];
        DigestRandomGenerator digestRandomGenerator = new DigestRandomGenerator(new SHA1Digest());
        digestRandomGenerator.addSeedMaterial(arrby5);
        byte[] arrby6 = new byte[n2];
        digestRandomGenerator.nextBytes(arrby6);
        for (int i = 0; i < n2; ++i) {
            arrby6[i] = (byte)(arrby6[i] ^ arrby4[i]);
        }
        byte[] arrby7 = ByteUtils.concatenate(arrby5, arrby6);
        byte[] arrby8 = new byte[this.messDigest.getDigestSize()];
        this.messDigest.update(arrby7, 0, arrby7.length);
        this.messDigest.doFinal(arrby8, 0);
        if (!Conversions.encode(this.n, this.t, arrby8).equals(gF2Vector2)) {
            throw new Exception("Bad Padding: invalid ciphertext");
        }
        return arrby6;
    }

    @Override
    public byte[] messageEncrypt(byte[] arrby) {
        GF2Vector gF2Vector = new GF2Vector(this.k, this.sr);
        byte[] arrby2 = gF2Vector.getEncoded();
        byte[] arrby3 = ByteUtils.concatenate(arrby2, arrby);
        this.messDigest.update(arrby3, 0, arrby3.length);
        byte[] arrby4 = new byte[this.messDigest.getDigestSize()];
        this.messDigest.doFinal(arrby4, 0);
        GF2Vector gF2Vector2 = Conversions.encode(this.n, this.t, arrby4);
        byte[] arrby5 = McElieceCCA2Primitives.encryptionPrimitive((McElieceCCA2PublicKeyParameters)this.key, gF2Vector, gF2Vector2).getEncoded();
        DigestRandomGenerator digestRandomGenerator = new DigestRandomGenerator(new SHA1Digest());
        digestRandomGenerator.addSeedMaterial(arrby2);
        byte[] arrby6 = new byte[arrby.length];
        digestRandomGenerator.nextBytes(arrby6);
        for (int i = 0; i < arrby.length; ++i) {
            arrby6[i] = (byte)(arrby6[i] ^ arrby[i]);
        }
        return ByteUtils.concatenate(arrby5, arrby6);
    }
}

