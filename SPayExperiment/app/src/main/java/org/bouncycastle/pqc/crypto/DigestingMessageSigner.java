/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.pqc.crypto;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.Signer;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.pqc.crypto.MessageSigner;

public class DigestingMessageSigner
implements Signer {
    private boolean forSigning;
    private final Digest messDigest;
    private final MessageSigner messSigner;

    public DigestingMessageSigner(MessageSigner messageSigner, Digest digest) {
        this.messSigner = messageSigner;
        this.messDigest = digest;
    }

    @Override
    public byte[] generateSignature() {
        if (!this.forSigning) {
            throw new IllegalStateException("RainbowDigestSigner not initialised for signature generation.");
        }
        byte[] arrby = new byte[this.messDigest.getDigestSize()];
        this.messDigest.doFinal(arrby, 0);
        return this.messSigner.generateSignature(arrby);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        this.forSigning = bl;
        AsymmetricKeyParameter asymmetricKeyParameter = cipherParameters instanceof ParametersWithRandom ? (AsymmetricKeyParameter)((ParametersWithRandom)cipherParameters).getParameters() : (AsymmetricKeyParameter)cipherParameters;
        if (bl && !asymmetricKeyParameter.isPrivate()) {
            throw new IllegalArgumentException("Signing Requires Private Key.");
        }
        if (!bl && asymmetricKeyParameter.isPrivate()) {
            throw new IllegalArgumentException("Verification Requires Public Key.");
        }
        this.reset();
        this.messSigner.init(bl, cipherParameters);
    }

    @Override
    public void reset() {
        this.messDigest.reset();
    }

    @Override
    public void update(byte by) {
        this.messDigest.update(by);
    }

    @Override
    public void update(byte[] arrby, int n, int n2) {
        this.messDigest.update(arrby, n, n2);
    }

    public boolean verify(byte[] arrby) {
        if (this.forSigning) {
            throw new IllegalStateException("RainbowDigestSigner not initialised for verification");
        }
        byte[] arrby2 = new byte[this.messDigest.getDigestSize()];
        this.messDigest.doFinal(arrby2, 0);
        return this.messSigner.verifySignature(arrby2, arrby);
    }

    @Override
    public boolean verifySignature(byte[] arrby) {
        return this.verify(arrby);
    }
}

