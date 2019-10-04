/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayOutputStream
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.System
 *  java.lang.UnsupportedOperationException
 *  java.security.AlgorithmParameters
 *  java.security.InvalidKeyException
 *  java.security.InvalidParameterException
 *  java.security.PrivateKey
 *  java.security.PublicKey
 *  java.security.SecureRandom
 *  java.security.SignatureException
 *  java.security.SignatureSpi
 *  java.security.interfaces.RSAPrivateKey
 *  java.security.interfaces.RSAPublicKey
 *  java.security.spec.AlgorithmParameterSpec
 *  java.security.spec.MGF1ParameterSpec
 *  java.security.spec.PSSParameterSpec
 */
package org.bouncycastle.jcajce.provider.asymmetric.rsa;

import java.io.ByteArrayOutputStream;
import java.security.AlgorithmParameters;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.security.SignatureSpi;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PSSParameterSpec;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.engines.RSABlindedEngine;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.signers.PSSSigner;
import org.bouncycastle.jcajce.provider.asymmetric.rsa.RSAUtil;
import org.bouncycastle.jcajce.provider.util.DigestFactory;
import org.bouncycastle.jcajce.util.BCJcaJceHelper;
import org.bouncycastle.jcajce.util.JcaJceHelper;

public class PSSSignatureSpi
extends SignatureSpi {
    private Digest contentDigest;
    private AlgorithmParameters engineParams;
    private final JcaJceHelper helper = new BCJcaJceHelper();
    private boolean isRaw;
    private Digest mgfDigest;
    private PSSParameterSpec originalSpec;
    private PSSParameterSpec paramSpec;
    private PSSSigner pss;
    private int saltLength;
    private AsymmetricBlockCipher signer;
    private byte trailer;

    protected PSSSignatureSpi(AsymmetricBlockCipher asymmetricBlockCipher, PSSParameterSpec pSSParameterSpec) {
        this(asymmetricBlockCipher, pSSParameterSpec, false);
    }

    /*
     * Enabled aggressive block sorting
     */
    protected PSSSignatureSpi(AsymmetricBlockCipher asymmetricBlockCipher, PSSParameterSpec pSSParameterSpec, boolean bl) {
        this.signer = asymmetricBlockCipher;
        this.originalSpec = pSSParameterSpec;
        this.paramSpec = pSSParameterSpec == null ? PSSParameterSpec.DEFAULT : pSSParameterSpec;
        this.mgfDigest = DigestFactory.getDigest(this.paramSpec.getDigestAlgorithm());
        this.saltLength = this.paramSpec.getSaltLength();
        this.trailer = this.getTrailer(this.paramSpec.getTrailerField());
        this.isRaw = bl;
        this.setupContentDigest();
    }

    private byte getTrailer(int n) {
        if (n == 1) {
            return -68;
        }
        throw new IllegalArgumentException("unknown trailer field");
    }

    private void setupContentDigest() {
        if (this.isRaw) {
            this.contentDigest = new NullPssDigest(this.mgfDigest);
            return;
        }
        this.contentDigest = this.mgfDigest;
    }

    protected Object engineGetParameter(String string) {
        throw new UnsupportedOperationException("engineGetParameter unsupported");
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    protected AlgorithmParameters engineGetParameters() {
        if (this.engineParams != null || this.paramSpec == null) return this.engineParams;
        try {
            this.engineParams = this.helper.createAlgorithmParameters("PSS");
            this.engineParams.init((AlgorithmParameterSpec)this.paramSpec);
        }
        catch (Exception exception) {
            throw new RuntimeException(exception.toString());
        }
        return this.engineParams;
    }

    protected void engineInitSign(PrivateKey privateKey) {
        if (!(privateKey instanceof RSAPrivateKey)) {
            throw new InvalidKeyException("Supplied key is not a RSAPrivateKey instance");
        }
        this.pss = new PSSSigner(this.signer, this.contentDigest, this.mgfDigest, this.saltLength, this.trailer);
        this.pss.init(true, RSAUtil.generatePrivateKeyParameter((RSAPrivateKey)privateKey));
    }

    protected void engineInitSign(PrivateKey privateKey, SecureRandom secureRandom) {
        if (!(privateKey instanceof RSAPrivateKey)) {
            throw new InvalidKeyException("Supplied key is not a RSAPrivateKey instance");
        }
        this.pss = new PSSSigner(this.signer, this.contentDigest, this.mgfDigest, this.saltLength, this.trailer);
        this.pss.init(true, new ParametersWithRandom(RSAUtil.generatePrivateKeyParameter((RSAPrivateKey)privateKey), secureRandom));
    }

    protected void engineInitVerify(PublicKey publicKey) {
        if (!(publicKey instanceof RSAPublicKey)) {
            throw new InvalidKeyException("Supplied key is not a RSAPublicKey instance");
        }
        this.pss = new PSSSigner(this.signer, this.contentDigest, this.mgfDigest, this.saltLength, this.trailer);
        this.pss.init(false, RSAUtil.generatePublicKeyParameter((RSAPublicKey)publicKey));
    }

    protected void engineSetParameter(String string, Object object) {
        throw new UnsupportedOperationException("engineSetParameter unsupported");
    }

    protected void engineSetParameter(AlgorithmParameterSpec algorithmParameterSpec) {
        if (algorithmParameterSpec instanceof PSSParameterSpec) {
            PSSParameterSpec pSSParameterSpec = (PSSParameterSpec)algorithmParameterSpec;
            if (this.originalSpec != null && !DigestFactory.isSameDigest(this.originalSpec.getDigestAlgorithm(), pSSParameterSpec.getDigestAlgorithm())) {
                throw new InvalidParameterException("parameter must be using " + this.originalSpec.getDigestAlgorithm());
            }
            if (!pSSParameterSpec.getMGFAlgorithm().equalsIgnoreCase("MGF1") && !pSSParameterSpec.getMGFAlgorithm().equals((Object)PKCSObjectIdentifiers.id_mgf1.getId())) {
                throw new InvalidParameterException("unknown mask generation function specified");
            }
            if (!(pSSParameterSpec.getMGFParameters() instanceof MGF1ParameterSpec)) {
                throw new InvalidParameterException("unkown MGF parameters");
            }
            MGF1ParameterSpec mGF1ParameterSpec = (MGF1ParameterSpec)pSSParameterSpec.getMGFParameters();
            if (!DigestFactory.isSameDigest(mGF1ParameterSpec.getDigestAlgorithm(), pSSParameterSpec.getDigestAlgorithm())) {
                throw new InvalidParameterException("digest algorithm for MGF should be the same as for PSS parameters.");
            }
            Digest digest = DigestFactory.getDigest(mGF1ParameterSpec.getDigestAlgorithm());
            if (digest == null) {
                throw new InvalidParameterException("no match on MGF digest algorithm: " + mGF1ParameterSpec.getDigestAlgorithm());
            }
            this.engineParams = null;
            this.paramSpec = pSSParameterSpec;
            this.mgfDigest = digest;
            this.saltLength = this.paramSpec.getSaltLength();
            this.trailer = this.getTrailer(this.paramSpec.getTrailerField());
            this.setupContentDigest();
            return;
        }
        throw new InvalidParameterException("Only PSSParameterSpec supported");
    }

    protected byte[] engineSign() {
        try {
            byte[] arrby = this.pss.generateSignature();
            return arrby;
        }
        catch (CryptoException cryptoException) {
            throw new SignatureException(cryptoException.getMessage());
        }
    }

    protected void engineUpdate(byte by) {
        this.pss.update(by);
    }

    protected void engineUpdate(byte[] arrby, int n, int n2) {
        this.pss.update(arrby, n, n2);
    }

    protected boolean engineVerify(byte[] arrby) {
        return this.pss.verifySignature(arrby);
    }

    private class NullPssDigest
    implements Digest {
        private ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        private Digest baseDigest;
        private boolean oddTime = true;

        public NullPssDigest(Digest digest) {
            this.baseDigest = digest;
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public int doFinal(byte[] arrby, int n) {
            byte[] arrby2 = this.bOut.toByteArray();
            if (this.oddTime) {
                System.arraycopy((Object)arrby2, (int)0, (Object)arrby, (int)n, (int)arrby2.length);
            } else {
                this.baseDigest.update(arrby2, 0, arrby2.length);
                this.baseDigest.doFinal(arrby, n);
            }
            this.reset();
            boolean bl = this.oddTime;
            boolean bl2 = false;
            if (!bl) {
                bl2 = true;
            }
            this.oddTime = bl2;
            return arrby2.length;
        }

        @Override
        public String getAlgorithmName() {
            return "NULL";
        }

        public int getByteLength() {
            return 0;
        }

        @Override
        public int getDigestSize() {
            return this.baseDigest.getDigestSize();
        }

        @Override
        public void reset() {
            this.bOut.reset();
            this.baseDigest.reset();
        }

        @Override
        public void update(byte by) {
            this.bOut.write((int)by);
        }

        @Override
        public void update(byte[] arrby, int n, int n2) {
            this.bOut.write(arrby, n, n2);
        }
    }

    public static class PSSwithRSA
    extends PSSSignatureSpi {
        public PSSwithRSA() {
            super(new RSABlindedEngine(), null);
        }
    }

    public static class SHA1withRSA
    extends PSSSignatureSpi {
        public SHA1withRSA() {
            super(new RSABlindedEngine(), PSSParameterSpec.DEFAULT);
        }
    }

    public static class SHA224withRSA
    extends PSSSignatureSpi {
        public SHA224withRSA() {
            super(new RSABlindedEngine(), new PSSParameterSpec("SHA-224", "MGF1", (AlgorithmParameterSpec)new MGF1ParameterSpec("SHA-224"), 28, 1));
        }
    }

    public static class SHA256withRSA
    extends PSSSignatureSpi {
        public SHA256withRSA() {
            super(new RSABlindedEngine(), new PSSParameterSpec("SHA-256", "MGF1", (AlgorithmParameterSpec)new MGF1ParameterSpec("SHA-256"), 32, 1));
        }
    }

    public static class SHA384withRSA
    extends PSSSignatureSpi {
        public SHA384withRSA() {
            super(new RSABlindedEngine(), new PSSParameterSpec("SHA-384", "MGF1", (AlgorithmParameterSpec)new MGF1ParameterSpec("SHA-384"), 48, 1));
        }
    }

    public static class SHA512withRSA
    extends PSSSignatureSpi {
        public SHA512withRSA() {
            super(new RSABlindedEngine(), new PSSParameterSpec("SHA-512", "MGF1", (AlgorithmParameterSpec)new MGF1ParameterSpec("SHA-512"), 64, 1));
        }
    }

    public static class nonePSS
    extends PSSSignatureSpi {
        public nonePSS() {
            super(new RSABlindedEngine(), null, true);
        }
    }

}

