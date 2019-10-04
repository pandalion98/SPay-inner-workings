/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
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
import org.bouncycastle.pqc.math.linearalgebra.IntegerFunctions;

public class McElieceKobaraImaiCipher
implements MessageEncryptor {
    private static final String DEFAULT_PRNG_NAME = "SHA1PRNG";
    public static final String OID = "1.3.6.1.4.1.8301.3.1.3.4.2.3";
    public static final byte[] PUBLIC_CONSTANT = "a predetermined public constant".getBytes();
    private int k;
    McElieceCCA2KeyParameters key;
    private Digest messDigest;
    private int n;
    private SecureRandom sr;
    private int t;

    private void initCipherEncrypt(McElieceCCA2PublicKeyParameters mcElieceCCA2PublicKeyParameters) {
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
        this.k = mcElieceCCA2PrivateKeyParameters.getK();
        this.t = mcElieceCCA2PrivateKeyParameters.getT();
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public byte[] messageDecrypt(byte[] arrby) {
        byte[] arrby2;
        int n = this.n >> 3;
        if (arrby.length < n) {
            throw new Exception("Bad Padding: Ciphertext too short.");
        }
        int n2 = this.messDigest.getDigestSize();
        int n3 = this.k >> 3;
        int n4 = arrby.length - n;
        if (n4 > 0) {
            byte[][] arrby3 = ByteUtils.split(arrby, n4);
            byte[] arrby4 = arrby3[0];
            arrby = arrby3[1];
            arrby2 = arrby4;
        } else {
            arrby2 = new byte[]{};
        }
        GF2Vector gF2Vector = GF2Vector.OS2VP(this.n, arrby);
        GF2Vector[] arrgF2Vector = McElieceCCA2Primitives.decryptionPrimitive((McElieceCCA2PrivateKeyParameters)this.key, gF2Vector);
        byte[] arrby5 = arrgF2Vector[0].getEncoded();
        GF2Vector gF2Vector2 = arrgF2Vector[1];
        if (arrby5.length > n3) {
            arrby5 = ByteUtils.subArray(arrby5, 0, n3);
        }
        byte[] arrby6 = ByteUtils.concatenate(ByteUtils.concatenate(arrby2, Conversions.decode(this.n, this.t, gF2Vector2)), arrby5);
        int n5 = arrby6.length - n2;
        byte[][] arrby7 = ByteUtils.split(arrby6, n2);
        byte[] arrby8 = arrby7[0];
        byte[] arrby9 = arrby7[1];
        byte[] arrby10 = new byte[this.messDigest.getDigestSize()];
        this.messDigest.update(arrby9, 0, arrby9.length);
        this.messDigest.doFinal(arrby10, 0);
        for (int i = n2 - 1; i >= 0; --i) {
            arrby10[i] = (byte)(arrby10[i] ^ arrby8[i]);
        }
        DigestRandomGenerator digestRandomGenerator = new DigestRandomGenerator(new SHA1Digest());
        digestRandomGenerator.addSeedMaterial(arrby10);
        byte[] arrby11 = new byte[n5];
        digestRandomGenerator.nextBytes(arrby11);
        for (int i = n5 - 1; i >= 0; --i) {
            arrby11[i] = (byte)(arrby11[i] ^ arrby9[i]);
        }
        if (arrby11.length < n5) {
            throw new Exception("Bad Padding: invalid ciphertext");
        }
        byte[][] arrby12 = ByteUtils.split(arrby11, n5 - PUBLIC_CONSTANT.length);
        byte[] arrby13 = arrby12[0];
        if (!ByteUtils.equals(arrby12[1], PUBLIC_CONSTANT)) {
            throw new Exception("Bad Padding: invalid ciphertext");
        }
        return arrby13;
    }

    @Override
    public byte[] messageEncrypt(byte[] arrby) {
        int n = this.messDigest.getDigestSize();
        int n2 = this.k >> 3;
        int n3 = -1 + IntegerFunctions.binomial(this.n, this.t).bitLength() >> 3;
        int n4 = n2 + n3 - n - PUBLIC_CONSTANT.length;
        if (arrby.length > n4) {
            n4 = arrby.length;
        }
        int n5 = n4 + PUBLIC_CONSTANT.length;
        int n6 = n5 + n - n2 - n3;
        byte[] arrby2 = new byte[n5];
        System.arraycopy((Object)arrby, (int)0, (Object)arrby2, (int)0, (int)arrby.length);
        System.arraycopy((Object)PUBLIC_CONSTANT, (int)0, (Object)arrby2, (int)n4, (int)PUBLIC_CONSTANT.length);
        byte[] arrby3 = new byte[n];
        this.sr.nextBytes(arrby3);
        DigestRandomGenerator digestRandomGenerator = new DigestRandomGenerator(new SHA1Digest());
        digestRandomGenerator.addSeedMaterial(arrby3);
        byte[] arrby4 = new byte[n5];
        digestRandomGenerator.nextBytes(arrby4);
        for (int i = n5 - 1; i >= 0; --i) {
            arrby4[i] = (byte)(arrby4[i] ^ arrby2[i]);
        }
        byte[] arrby5 = new byte[this.messDigest.getDigestSize()];
        this.messDigest.update(arrby4, 0, arrby4.length);
        this.messDigest.doFinal(arrby5, 0);
        for (int i = n - 1; i >= 0; --i) {
            arrby5[i] = (byte)(arrby5[i] ^ arrby3[i]);
        }
        byte[] arrby6 = ByteUtils.concatenate(arrby5, arrby4);
        byte[] arrby7 = new byte[]{};
        if (n6 > 0) {
            arrby7 = new byte[n6];
            System.arraycopy((Object)arrby6, (int)0, (Object)arrby7, (int)0, (int)n6);
        }
        byte[] arrby8 = arrby7;
        byte[] arrby9 = new byte[n3];
        System.arraycopy((Object)arrby6, (int)n6, (Object)arrby9, (int)0, (int)n3);
        byte[] arrby10 = new byte[n2];
        System.arraycopy((Object)arrby6, (int)(n3 + n6), (Object)arrby10, (int)0, (int)n2);
        GF2Vector gF2Vector = GF2Vector.OS2VP(this.k, arrby10);
        GF2Vector gF2Vector2 = Conversions.encode(this.n, this.t, arrby9);
        byte[] arrby11 = McElieceCCA2Primitives.encryptionPrimitive((McElieceCCA2PublicKeyParameters)this.key, gF2Vector, gF2Vector2).getEncoded();
        if (n6 > 0) {
            arrby11 = ByteUtils.concatenate(arrby8, arrby11);
        }
        return arrby11;
    }
}

