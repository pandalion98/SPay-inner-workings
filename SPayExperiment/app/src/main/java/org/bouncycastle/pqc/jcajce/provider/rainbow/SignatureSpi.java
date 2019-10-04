/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.UnsupportedOperationException
 *  java.security.PrivateKey
 *  java.security.PublicKey
 *  java.security.SecureRandom
 *  java.security.SignatureException
 *  java.security.SignatureSpi
 *  java.security.spec.AlgorithmParameterSpec
 */
package org.bouncycastle.pqc.jcajce.provider.rainbow;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.security.spec.AlgorithmParameterSpec;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA224Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.digests.SHA384Digest;
import org.bouncycastle.crypto.digests.SHA512Digest;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.pqc.crypto.rainbow.RainbowSigner;
import org.bouncycastle.pqc.jcajce.provider.rainbow.RainbowKeysToParams;

public class SignatureSpi
extends java.security.SignatureSpi {
    private Digest digest;
    private SecureRandom random;
    private RainbowSigner signer;

    protected SignatureSpi(Digest digest, RainbowSigner rainbowSigner) {
        this.digest = digest;
        this.signer = rainbowSigner;
    }

    protected Object engineGetParameter(String string) {
        throw new UnsupportedOperationException("engineSetParameter unsupported");
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void engineInitSign(PrivateKey privateKey) {
        void var3_4;
        AsymmetricKeyParameter asymmetricKeyParameter = RainbowKeysToParams.generatePrivateKeyParameter(privateKey);
        if (this.random != null) {
            ParametersWithRandom parametersWithRandom = new ParametersWithRandom(asymmetricKeyParameter, this.random);
        } else {
            AsymmetricKeyParameter asymmetricKeyParameter2 = asymmetricKeyParameter;
        }
        this.digest.reset();
        this.signer.init(true, (CipherParameters)var3_4);
    }

    protected void engineInitSign(PrivateKey privateKey, SecureRandom secureRandom) {
        this.random = secureRandom;
        this.engineInitSign(privateKey);
    }

    protected void engineInitVerify(PublicKey publicKey) {
        AsymmetricKeyParameter asymmetricKeyParameter = RainbowKeysToParams.generatePublicKeyParameter(publicKey);
        this.digest.reset();
        this.signer.init(false, asymmetricKeyParameter);
    }

    protected void engineSetParameter(String string, Object object) {
        throw new UnsupportedOperationException("engineSetParameter unsupported");
    }

    protected void engineSetParameter(AlgorithmParameterSpec algorithmParameterSpec) {
        throw new UnsupportedOperationException("engineSetParameter unsupported");
    }

    protected byte[] engineSign() {
        byte[] arrby = new byte[this.digest.getDigestSize()];
        this.digest.doFinal(arrby, 0);
        try {
            byte[] arrby2 = this.signer.generateSignature(arrby);
            return arrby2;
        }
        catch (Exception exception) {
            throw new SignatureException(exception.toString());
        }
    }

    protected void engineUpdate(byte by) {
        this.digest.update(by);
    }

    protected void engineUpdate(byte[] arrby, int n, int n2) {
        this.digest.update(arrby, n, n2);
    }

    protected boolean engineVerify(byte[] arrby) {
        byte[] arrby2 = new byte[this.digest.getDigestSize()];
        this.digest.doFinal(arrby2, 0);
        return this.signer.verifySignature(arrby2, arrby);
    }

    public static class withSha224
    extends SignatureSpi {
        public withSha224() {
            super(new SHA224Digest(), new RainbowSigner());
        }
    }

    public static class withSha256
    extends SignatureSpi {
        public withSha256() {
            super(new SHA256Digest(), new RainbowSigner());
        }
    }

    public static class withSha384
    extends SignatureSpi {
        public withSha384() {
            super(new SHA384Digest(), new RainbowSigner());
        }
    }

    public static class withSha512
    extends SignatureSpi {
        public withSha512() {
            super(new SHA512Digest(), new RainbowSigner());
        }
    }

}

