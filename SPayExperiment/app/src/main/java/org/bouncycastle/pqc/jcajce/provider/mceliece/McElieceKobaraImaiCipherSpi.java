/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayOutputStream
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.security.InvalidKeyException
 *  java.security.Key
 *  java.security.PrivateKey
 *  java.security.PublicKey
 *  java.security.SecureRandom
 *  java.security.spec.AlgorithmParameterSpec
 *  javax.crypto.BadPaddingException
 */
package org.bouncycastle.pqc.jcajce.provider.mceliece;

import java.io.ByteArrayOutputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.BadPaddingException;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.X509ObjectIdentifiers;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.digests.SHA224Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.digests.SHA384Digest;
import org.bouncycastle.crypto.digests.SHA512Digest;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.pqc.crypto.mceliece.McElieceCCA2KeyParameters;
import org.bouncycastle.pqc.crypto.mceliece.McElieceKobaraImaiCipher;
import org.bouncycastle.pqc.jcajce.provider.mceliece.McElieceCCA2KeysToParams;
import org.bouncycastle.pqc.jcajce.provider.util.AsymmetricHybridCipher;

public class McElieceKobaraImaiCipherSpi
extends AsymmetricHybridCipher
implements PKCSObjectIdentifiers,
X509ObjectIdentifiers {
    private ByteArrayOutputStream buf = new ByteArrayOutputStream();
    private McElieceKobaraImaiCipher cipher;
    private Digest digest;

    public McElieceKobaraImaiCipherSpi() {
        this.buf = new ByteArrayOutputStream();
    }

    protected McElieceKobaraImaiCipherSpi(Digest digest, McElieceKobaraImaiCipher mcElieceKobaraImaiCipher) {
        this.digest = digest;
        this.cipher = mcElieceKobaraImaiCipher;
        this.buf = new ByteArrayOutputStream();
    }

    private byte[] pad() {
        this.buf.write(1);
        byte[] arrby = this.buf.toByteArray();
        this.buf.reset();
        return arrby;
    }

    private byte[] unpad(byte[] arrby) {
        int n;
        for (n = -1 + arrby.length; n >= 0 && arrby[n] == 0; --n) {
        }
        if (arrby[n] != 1) {
            throw new BadPaddingException("invalid ciphertext");
        }
        byte[] arrby2 = new byte[n];
        System.arraycopy((Object)arrby, (int)0, (Object)arrby2, (int)0, (int)n);
        return arrby2;
    }

    @Override
    protected int decryptOutputSize(int n) {
        return 0;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public byte[] doFinal(byte[] arrby, int n, int n2) {
        this.update(arrby, n, n2);
        if (this.opMode == 1) {
            try {
                return this.cipher.messageEncrypt(this.pad());
            }
            catch (Exception exception) {
                exception.printStackTrace();
                do {
                    return null;
                    break;
                } while (true);
            }
        }
        if (this.opMode != 2) return null;
        byte[] arrby2 = this.buf.toByteArray();
        this.buf.reset();
        try {
            return this.unpad(this.cipher.messageDecrypt(arrby2));
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    protected int encryptOutputSize(int n) {
        return 0;
    }

    @Override
    public int getKeySize(Key key) {
        if (key instanceof PublicKey) {
            McElieceCCA2KeyParameters mcElieceCCA2KeyParameters = (McElieceCCA2KeyParameters)McElieceCCA2KeysToParams.generatePublicKeyParameter((PublicKey)key);
            return this.cipher.getKeySize(mcElieceCCA2KeyParameters);
        }
        if (key instanceof PrivateKey) {
            McElieceCCA2KeyParameters mcElieceCCA2KeyParameters = (McElieceCCA2KeyParameters)McElieceCCA2KeysToParams.generatePrivateKeyParameter((PrivateKey)key);
            return this.cipher.getKeySize(mcElieceCCA2KeyParameters);
        }
        throw new InvalidKeyException();
    }

    @Override
    public String getName() {
        return "McElieceKobaraImaiCipher";
    }

    @Override
    protected void initCipherDecrypt(Key key, AlgorithmParameterSpec algorithmParameterSpec) {
        this.buf.reset();
        AsymmetricKeyParameter asymmetricKeyParameter = McElieceCCA2KeysToParams.generatePrivateKeyParameter((PrivateKey)key);
        this.digest.reset();
        this.cipher.init(false, asymmetricKeyParameter);
    }

    @Override
    protected void initCipherEncrypt(Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) {
        this.buf.reset();
        ParametersWithRandom parametersWithRandom = new ParametersWithRandom(McElieceCCA2KeysToParams.generatePublicKeyParameter((PublicKey)key), secureRandom);
        this.digest.reset();
        this.cipher.init(true, parametersWithRandom);
    }

    public byte[] messageDecrypt() {
        byte[] arrby = this.buf.toByteArray();
        this.buf.reset();
        try {
            byte[] arrby2 = this.unpad(this.cipher.messageDecrypt(arrby));
            return arrby2;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public byte[] messageEncrypt() {
        try {
            byte[] arrby = this.cipher.messageEncrypt(this.pad());
            return arrby;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public byte[] update(byte[] arrby, int n, int n2) {
        this.buf.write(arrby, n, n2);
        return new byte[0];
    }

    public static class McElieceKobaraImai
    extends McElieceKobaraImaiCipherSpi {
        public McElieceKobaraImai() {
            super(new SHA1Digest(), new McElieceKobaraImaiCipher());
        }
    }

    public static class McElieceKobaraImai224
    extends McElieceKobaraImaiCipherSpi {
        public McElieceKobaraImai224() {
            super(new SHA224Digest(), new McElieceKobaraImaiCipher());
        }
    }

    public static class McElieceKobaraImai256
    extends McElieceKobaraImaiCipherSpi {
        public McElieceKobaraImai256() {
            super(new SHA256Digest(), new McElieceKobaraImaiCipher());
        }
    }

    public static class McElieceKobaraImai384
    extends McElieceKobaraImaiCipherSpi {
        public McElieceKobaraImai384() {
            super(new SHA384Digest(), new McElieceKobaraImaiCipher());
        }
    }

    public static class McElieceKobaraImai512
    extends McElieceKobaraImaiCipherSpi {
        public McElieceKobaraImai512() {
            super(new SHA512Digest(), new McElieceKobaraImaiCipher());
        }
    }

}

