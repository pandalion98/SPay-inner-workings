/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalStateException
 *  java.security.SecureRandom
 */
package org.bouncycastle.crypto.tls;

import java.security.SecureRandom;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.Signer;
import org.bouncycastle.crypto.digests.NullDigest;
import org.bouncycastle.crypto.encodings.PKCS1Encoding;
import org.bouncycastle.crypto.engines.RSABlindedEngine;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.signers.GenericSigner;
import org.bouncycastle.crypto.signers.RSADigestSigner;
import org.bouncycastle.crypto.tls.AbstractTlsSigner;
import org.bouncycastle.crypto.tls.CombinedHash;
import org.bouncycastle.crypto.tls.SignatureAndHashAlgorithm;
import org.bouncycastle.crypto.tls.TlsContext;
import org.bouncycastle.crypto.tls.TlsUtils;

public class TlsRSASigner
extends AbstractTlsSigner {
    protected AsymmetricBlockCipher createRSAImpl() {
        return new PKCS1Encoding(new RSABlindedEngine());
    }

    @Override
    public Signer createSigner(SignatureAndHashAlgorithm signatureAndHashAlgorithm, AsymmetricKeyParameter asymmetricKeyParameter) {
        return this.makeSigner(signatureAndHashAlgorithm, false, true, new ParametersWithRandom(asymmetricKeyParameter, this.context.getSecureRandom()));
    }

    @Override
    public Signer createVerifyer(SignatureAndHashAlgorithm signatureAndHashAlgorithm, AsymmetricKeyParameter asymmetricKeyParameter) {
        return this.makeSigner(signatureAndHashAlgorithm, false, false, asymmetricKeyParameter);
    }

    @Override
    public byte[] generateRawSignature(SignatureAndHashAlgorithm signatureAndHashAlgorithm, AsymmetricKeyParameter asymmetricKeyParameter, byte[] arrby) {
        Signer signer = this.makeSigner(signatureAndHashAlgorithm, true, true, new ParametersWithRandom(asymmetricKeyParameter, this.context.getSecureRandom()));
        signer.update(arrby, 0, arrby.length);
        return signer.generateSignature();
    }

    @Override
    public boolean isValidPublicKey(AsymmetricKeyParameter asymmetricKeyParameter) {
        return asymmetricKeyParameter instanceof RSAKeyParameters && !asymmetricKeyParameter.isPrivate();
    }

    /*
     * Enabled aggressive block sorting
     */
    protected Signer makeSigner(SignatureAndHashAlgorithm signatureAndHashAlgorithm, boolean bl, boolean bl2, CipherParameters cipherParameters) {
        void var6_7;
        boolean bl3 = signatureAndHashAlgorithm != null;
        if (bl3 != TlsUtils.isTLSv12(this.context)) {
            throw new IllegalStateException();
        }
        if (signatureAndHashAlgorithm != null && signatureAndHashAlgorithm.getSignature() != 1) {
            throw new IllegalStateException();
        }
        if (bl) {
            NullDigest nullDigest = new NullDigest();
        } else if (signatureAndHashAlgorithm == null) {
            CombinedHash combinedHash = new CombinedHash();
        } else {
            Digest digest = TlsUtils.createHash(signatureAndHashAlgorithm.getHash());
        }
        Signer signer = signatureAndHashAlgorithm != null ? new RSADigestSigner((Digest)var6_7, TlsUtils.getOIDForHashAlgorithm(signatureAndHashAlgorithm.getHash())) : new GenericSigner(this.createRSAImpl(), (Digest)var6_7);
        signer.init(bl2, cipherParameters);
        return signer;
    }

    @Override
    public boolean verifyRawSignature(SignatureAndHashAlgorithm signatureAndHashAlgorithm, byte[] arrby, AsymmetricKeyParameter asymmetricKeyParameter, byte[] arrby2) {
        Signer signer = this.makeSigner(signatureAndHashAlgorithm, true, false, asymmetricKeyParameter);
        signer.update(arrby2, 0, arrby2.length);
        return signer.verifySignature(arrby);
    }
}

