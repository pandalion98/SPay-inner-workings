/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.crypto.tls;

import org.bouncycastle.crypto.Signer;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.tls.SignatureAndHashAlgorithm;
import org.bouncycastle.crypto.tls.TlsContext;
import org.bouncycastle.crypto.tls.TlsSigner;

public abstract class AbstractTlsSigner
implements TlsSigner {
    protected TlsContext context;

    @Override
    public Signer createSigner(AsymmetricKeyParameter asymmetricKeyParameter) {
        return this.createSigner(null, asymmetricKeyParameter);
    }

    @Override
    public Signer createVerifyer(AsymmetricKeyParameter asymmetricKeyParameter) {
        return this.createVerifyer(null, asymmetricKeyParameter);
    }

    @Override
    public byte[] generateRawSignature(AsymmetricKeyParameter asymmetricKeyParameter, byte[] arrby) {
        return this.generateRawSignature(null, asymmetricKeyParameter, arrby);
    }

    @Override
    public void init(TlsContext tlsContext) {
        this.context = tlsContext;
    }

    @Override
    public boolean verifyRawSignature(byte[] arrby, AsymmetricKeyParameter asymmetricKeyParameter, byte[] arrby2) {
        return this.verifyRawSignature(null, arrby, asymmetricKeyParameter, arrby2);
    }
}

