/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.String
 *  java.security.Key
 *  java.security.PrivateKey
 *  java.security.PublicKey
 *  java.security.SecureRandom
 *  java.security.spec.AlgorithmParameterSpec
 */
package org.bouncycastle.pqc.jcajce.provider.mceliece;

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
import org.bouncycastle.pqc.crypto.mceliece.McElieceKeyParameters;
import org.bouncycastle.pqc.crypto.mceliece.McEliecePKCSCipher;
import org.bouncycastle.pqc.jcajce.provider.mceliece.McElieceKeysToParams;
import org.bouncycastle.pqc.jcajce.provider.util.AsymmetricBlockCipher;

public class McEliecePKCSCipherSpi
extends AsymmetricBlockCipher
implements PKCSObjectIdentifiers,
X509ObjectIdentifiers {
    private McEliecePKCSCipher cipher;
    private Digest digest;

    public McEliecePKCSCipherSpi(Digest digest, McEliecePKCSCipher mcEliecePKCSCipher) {
        this.digest = digest;
        this.cipher = mcEliecePKCSCipher;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public int getKeySize(Key key) {
        McElieceKeyParameters mcElieceKeyParameters;
        if (key instanceof PublicKey) {
            mcElieceKeyParameters = (McElieceKeyParameters)McElieceKeysToParams.generatePublicKeyParameter((PublicKey)key);
            do {
                return this.cipher.getKeySize(mcElieceKeyParameters);
                break;
            } while (true);
        }
        mcElieceKeyParameters = (McElieceKeyParameters)McElieceKeysToParams.generatePrivateKeyParameter((PrivateKey)key);
        return this.cipher.getKeySize(mcElieceKeyParameters);
    }

    @Override
    public String getName() {
        return "McEliecePKCS";
    }

    @Override
    protected void initCipherDecrypt(Key key, AlgorithmParameterSpec algorithmParameterSpec) {
        AsymmetricKeyParameter asymmetricKeyParameter = McElieceKeysToParams.generatePrivateKeyParameter((PrivateKey)key);
        this.digest.reset();
        this.cipher.init(false, asymmetricKeyParameter);
        this.maxPlainTextSize = this.cipher.maxPlainTextSize;
        this.cipherTextSize = this.cipher.cipherTextSize;
    }

    @Override
    protected void initCipherEncrypt(Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) {
        ParametersWithRandom parametersWithRandom = new ParametersWithRandom(McElieceKeysToParams.generatePublicKeyParameter((PublicKey)key), secureRandom);
        this.digest.reset();
        this.cipher.init(true, parametersWithRandom);
        this.maxPlainTextSize = this.cipher.maxPlainTextSize;
        this.cipherTextSize = this.cipher.cipherTextSize;
    }

    @Override
    protected byte[] messageDecrypt(byte[] arrby) {
        try {
            byte[] arrby2 = this.cipher.messageDecrypt(arrby);
            return arrby2;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    protected byte[] messageEncrypt(byte[] arrby) {
        try {
            byte[] arrby2 = this.cipher.messageEncrypt(arrby);
            return arrby2;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public static class McEliecePKCS
    extends McEliecePKCSCipherSpi {
        public McEliecePKCS() {
            super(new SHA1Digest(), new McEliecePKCSCipher());
        }
    }

    public static class McEliecePKCS224
    extends McEliecePKCSCipherSpi {
        public McEliecePKCS224() {
            super(new SHA224Digest(), new McEliecePKCSCipher());
        }
    }

    public static class McEliecePKCS256
    extends McEliecePKCSCipherSpi {
        public McEliecePKCS256() {
            super(new SHA256Digest(), new McEliecePKCSCipher());
        }
    }

    public static class McEliecePKCS384
    extends McEliecePKCSCipherSpi {
        public McEliecePKCS384() {
            super(new SHA384Digest(), new McEliecePKCSCipher());
        }
    }

    public static class McEliecePKCS512
    extends McEliecePKCSCipherSpi {
        public McEliecePKCS512() {
            super(new SHA512Digest(), new McEliecePKCSCipher());
        }
    }

}

