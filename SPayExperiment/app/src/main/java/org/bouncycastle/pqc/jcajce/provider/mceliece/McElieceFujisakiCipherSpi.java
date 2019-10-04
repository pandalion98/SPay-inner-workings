/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayOutputStream
 *  java.lang.Exception
 *  java.lang.String
 *  java.security.Key
 *  java.security.PrivateKey
 *  java.security.PublicKey
 *  java.security.SecureRandom
 *  java.security.spec.AlgorithmParameterSpec
 */
package org.bouncycastle.pqc.jcajce.provider.mceliece;

import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
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
import org.bouncycastle.pqc.crypto.mceliece.McElieceFujisakiCipher;
import org.bouncycastle.pqc.jcajce.provider.mceliece.McElieceCCA2KeysToParams;
import org.bouncycastle.pqc.jcajce.provider.util.AsymmetricHybridCipher;

public class McElieceFujisakiCipherSpi
extends AsymmetricHybridCipher
implements PKCSObjectIdentifiers,
X509ObjectIdentifiers {
    private ByteArrayOutputStream buf;
    private McElieceFujisakiCipher cipher;
    private Digest digest;

    protected McElieceFujisakiCipherSpi(Digest digest, McElieceFujisakiCipher mcElieceFujisakiCipher) {
        this.digest = digest;
        this.cipher = mcElieceFujisakiCipher;
        this.buf = new ByteArrayOutputStream();
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
        byte[] arrby2 = this.buf.toByteArray();
        this.buf.reset();
        if (this.opMode == 1) {
            try {
                return this.cipher.messageEncrypt(arrby2);
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
        try {
            return this.cipher.messageDecrypt(arrby2);
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

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public int getKeySize(Key key) {
        McElieceCCA2KeyParameters mcElieceCCA2KeyParameters;
        if (key instanceof PublicKey) {
            mcElieceCCA2KeyParameters = (McElieceCCA2KeyParameters)McElieceCCA2KeysToParams.generatePublicKeyParameter((PublicKey)key);
            do {
                return this.cipher.getKeySize(mcElieceCCA2KeyParameters);
                break;
            } while (true);
        }
        mcElieceCCA2KeyParameters = (McElieceCCA2KeyParameters)McElieceCCA2KeysToParams.generatePrivateKeyParameter((PrivateKey)key);
        return this.cipher.getKeySize(mcElieceCCA2KeyParameters);
    }

    @Override
    public String getName() {
        return "McElieceFujisakiCipher";
    }

    @Override
    protected void initCipherDecrypt(Key key, AlgorithmParameterSpec algorithmParameterSpec) {
        AsymmetricKeyParameter asymmetricKeyParameter = McElieceCCA2KeysToParams.generatePrivateKeyParameter((PrivateKey)key);
        this.digest.reset();
        this.cipher.init(false, asymmetricKeyParameter);
    }

    @Override
    protected void initCipherEncrypt(Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) {
        ParametersWithRandom parametersWithRandom = new ParametersWithRandom(McElieceCCA2KeysToParams.generatePublicKeyParameter((PublicKey)key), secureRandom);
        this.digest.reset();
        this.cipher.init(true, parametersWithRandom);
    }

    public byte[] messageDecrypt(byte[] arrby) {
        try {
            byte[] arrby2 = this.cipher.messageDecrypt(arrby);
            return arrby2;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public byte[] messageEncrypt(byte[] arrby) {
        try {
            byte[] arrby2 = this.cipher.messageEncrypt(arrby);
            return arrby2;
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

    public static class McElieceFujisaki
    extends McElieceFujisakiCipherSpi {
        public McElieceFujisaki() {
            super(new SHA1Digest(), new McElieceFujisakiCipher());
        }
    }

    public static class McElieceFujisaki224
    extends McElieceFujisakiCipherSpi {
        public McElieceFujisaki224() {
            super(new SHA224Digest(), new McElieceFujisakiCipher());
        }
    }

    public static class McElieceFujisaki256
    extends McElieceFujisakiCipherSpi {
        public McElieceFujisaki256() {
            super(new SHA256Digest(), new McElieceFujisakiCipher());
        }
    }

    public static class McElieceFujisaki384
    extends McElieceFujisakiCipherSpi {
        public McElieceFujisaki384() {
            super(new SHA384Digest(), new McElieceFujisakiCipher());
        }
    }

    public static class McElieceFujisaki512
    extends McElieceFujisakiCipherSpi {
        public McElieceFujisaki512() {
            super(new SHA512Digest(), new McElieceFujisakiCipher());
        }
    }

}

