/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalStateException
 *  java.security.SecureRandom
 */
package org.bouncycastle.crypto.tls;

import java.security.SecureRandom;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DSA;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.Signer;
import org.bouncycastle.crypto.digests.NullDigest;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.signers.DSADigestSigner;
import org.bouncycastle.crypto.tls.AbstractTlsSigner;
import org.bouncycastle.crypto.tls.SignatureAndHashAlgorithm;
import org.bouncycastle.crypto.tls.TlsContext;
import org.bouncycastle.crypto.tls.TlsUtils;

public abstract class TlsDSASigner
extends AbstractTlsSigner {
    protected abstract DSA createDSAImpl(short var1);

    @Override
    public Signer createSigner(SignatureAndHashAlgorithm signatureAndHashAlgorithm, AsymmetricKeyParameter asymmetricKeyParameter) {
        return this.makeSigner(signatureAndHashAlgorithm, false, true, asymmetricKeyParameter);
    }

    @Override
    public Signer createVerifyer(SignatureAndHashAlgorithm signatureAndHashAlgorithm, AsymmetricKeyParameter asymmetricKeyParameter) {
        return this.makeSigner(signatureAndHashAlgorithm, false, false, asymmetricKeyParameter);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public byte[] generateRawSignature(SignatureAndHashAlgorithm signatureAndHashAlgorithm, AsymmetricKeyParameter asymmetricKeyParameter, byte[] arrby) {
        Signer signer = this.makeSigner(signatureAndHashAlgorithm, true, true, new ParametersWithRandom(asymmetricKeyParameter, this.context.getSecureRandom()));
        if (signatureAndHashAlgorithm == null) {
            signer.update(arrby, 16, 20);
            do {
                return signer.generateSignature();
                break;
            } while (true);
        }
        signer.update(arrby, 0, arrby.length);
        return signer.generateSignature();
    }

    protected abstract short getSignatureAlgorithm();

    protected CipherParameters makeInitParameters(boolean bl, CipherParameters cipherParameters) {
        return cipherParameters;
    }

    /*
     * Enabled aggressive block sorting
     */
    protected Signer makeSigner(SignatureAndHashAlgorithm signatureAndHashAlgorithm, boolean bl, boolean bl2, CipherParameters cipherParameters) {
        boolean bl3 = signatureAndHashAlgorithm != null;
        if (bl3 != TlsUtils.isTLSv12(this.context)) {
            throw new IllegalStateException();
        }
        if (signatureAndHashAlgorithm != null && signatureAndHashAlgorithm.getSignature() != this.getSignatureAlgorithm()) {
            throw new IllegalStateException();
        }
        short s2 = signatureAndHashAlgorithm == null ? (short)2 : (short)signatureAndHashAlgorithm.getHash();
        Digest digest = bl ? new NullDigest() : TlsUtils.createHash(s2);
        DSADigestSigner dSADigestSigner = new DSADigestSigner(this.createDSAImpl(s2), digest);
        dSADigestSigner.init(bl2, this.makeInitParameters(bl2, cipherParameters));
        return dSADigestSigner;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public boolean verifyRawSignature(SignatureAndHashAlgorithm signatureAndHashAlgorithm, byte[] arrby, AsymmetricKeyParameter asymmetricKeyParameter, byte[] arrby2) {
        Signer signer = this.makeSigner(signatureAndHashAlgorithm, true, false, asymmetricKeyParameter);
        if (signatureAndHashAlgorithm == null) {
            signer.update(arrby2, 16, 20);
            do {
                return signer.verifySignature(arrby);
                break;
            } while (true);
        }
        signer.update(arrby2, 0, arrby2.length);
        return signer.verifySignature(arrby);
    }
}

